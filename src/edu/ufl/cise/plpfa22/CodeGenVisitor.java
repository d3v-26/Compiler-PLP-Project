package edu.ufl.cise.plpfa22;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import edu.ufl.cise.plpfa22.ast.ASTVisitor;
import edu.ufl.cise.plpfa22.IToken.Kind;
import edu.ufl.cise.plpfa22.ast.Block;
import edu.ufl.cise.plpfa22.ast.ConstDec;
import edu.ufl.cise.plpfa22.ast.ExpressionBinary;
import edu.ufl.cise.plpfa22.ast.ExpressionBooleanLit;
import edu.ufl.cise.plpfa22.ast.ExpressionIdent;
import edu.ufl.cise.plpfa22.ast.ExpressionNumLit;
import edu.ufl.cise.plpfa22.ast.ExpressionStringLit;
import edu.ufl.cise.plpfa22.ast.Ident;
import edu.ufl.cise.plpfa22.ast.ProcDec;
import edu.ufl.cise.plpfa22.ast.Program;
import edu.ufl.cise.plpfa22.ast.Statement;
import edu.ufl.cise.plpfa22.ast.StatementAssign;
import edu.ufl.cise.plpfa22.ast.StatementBlock;
import edu.ufl.cise.plpfa22.ast.StatementCall;
import edu.ufl.cise.plpfa22.ast.StatementEmpty;
import edu.ufl.cise.plpfa22.ast.StatementIf;
import edu.ufl.cise.plpfa22.ast.StatementInput;
import edu.ufl.cise.plpfa22.ast.StatementOutput;
import edu.ufl.cise.plpfa22.ast.StatementWhile;
import edu.ufl.cise.plpfa22.ast.Types.Type;
import edu.ufl.cise.plpfa22.ast.VarDec;

public class CodeGenVisitor implements ASTVisitor, Opcodes {

	final String packageName;
	final String className;
	final String sourceFileName;
	final String fullyQualifiedClassName; 
	final String classDesc;
	
	ClassWriter classWriter;

	
	public CodeGenVisitor(String className, String packageName, String sourceFileName) {
		super();
		this.packageName = packageName;
		this.className = className;
		this.sourceFileName = sourceFileName;
		this.fullyQualifiedClassName = packageName + "/" + className;
		this.classDesc="L"+this.fullyQualifiedClassName+';';
	}

	public void comparisonInstructions(MethodVisitor mv, int opcode)
	{
		Label labelComparisonFalseBr = new Label();
		mv.visitJumpInsn(opcode, labelComparisonFalseBr);
		mv.visitInsn(ICONST_1); // bool true
		Label labelPostComparison = new Label();
		mv.visitJumpInsn(GOTO, labelPostComparison);
		mv.visitLabel(labelComparisonFalseBr);
		mv.visitInsn(ICONST_0); // bool false
		mv.visitLabel(labelPostComparison);
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws PLPException {
		MethodVisitor methodVisitor = (MethodVisitor)arg;
		methodVisitor.visitCode();
		for (ConstDec constDec : block.constDecs) {
			constDec.visit(this, null);
		}
		for (VarDec varDec : block.varDecs) {
			varDec.visit(this, methodVisitor);
		}
		for (ProcDec procDec: block.procedureDecs) {
			procDec.visit(this, null);
		}
		//add instructions from statement to method
		block.statement.visit(this, arg);
		methodVisitor.visitInsn(RETURN);
		methodVisitor.visitMaxs(0,0);
		methodVisitor.visitEnd();
		return null;

	}

	@Override
	public Object visitProgram(Program program, Object arg) throws PLPException {
		//create a classWriter and visit it
		classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		//Hint:  if you get failures in the visitMaxs, try creating a ClassWriter with 0
		// instead of ClassWriter.COMPUTE_FRAMES.  The result will not be a valid classfile,
		// but you will be able to print it so you can see the instructions.  After fixing,
		// restore ClassWriter.COMPUTE_FRAMES
		classWriter.visit(V18, ACC_PUBLIC | ACC_SUPER, fullyQualifiedClassName, null, "java/lang/Object", null);

		//get a method visitor for the main method.		
		MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
		//visit the block, passing it the methodVisitor
		program.block.visit(this, methodVisitor);
		//finish up the class
        classWriter.visitEnd();
        //return the bytes making up the classfile
		return classWriter.toByteArray();
	}

	@Override
	public Object visitStatementAssign(StatementAssign statementAssign, Object arg) throws PLPException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitVarDec(VarDec varDec, Object arg) throws PLPException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatementCall(StatementCall statementCall, Object arg) throws PLPException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatementInput(StatementInput statementInput, Object arg) throws PLPException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatementOutput(StatementOutput statementOutput, Object arg) throws PLPException {
		MethodVisitor mv = (MethodVisitor)arg;
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		statementOutput.expression.visit(this, arg);
		Type etype = statementOutput.expression.getType();
		String JVMType = (etype.equals(Type.NUMBER) ? "I" : (etype.equals(Type.BOOLEAN) ? "Z" : "Ljava/lang/String;"));
		String printlnSig = "(" + JVMType +")V";
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", printlnSig, false);
		return null;
	}

	@Override
	public Object visitStatementBlock(StatementBlock statementBlock, Object arg) throws PLPException {
		for (Statement statement : statementBlock.statements) {
			statement.visit(this, arg);
		}
		return null;
	}

	@Override
	public Object visitStatementIf(StatementIf statementIf, Object arg) throws PLPException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatementWhile(StatementWhile statementWhile, Object arg) throws PLPException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpressionBinary(ExpressionBinary expressionBinary, Object arg) throws PLPException {
		MethodVisitor mv = (MethodVisitor) arg;
		Type argType = expressionBinary.e0.getType();
		Kind op = expressionBinary.op.getKind();
		IToken e1=expressionBinary.e1.firstToken;
		IToken e0=expressionBinary.e0.firstToken;
		switch (argType) {
		case NUMBER -> {
			switch (op) {
				case PLUS -> mv.visitInsn(IADD);
				case MINUS -> mv.visitInsn(ISUB);
				case TIMES -> mv.visitInsn(IMUL);
				case DIV -> mv.visitInsn(IDIV);
				case MOD -> mv.visitInsn(IREM);
				case EQ -> comparisonInstructions(mv, IF_ICMPNE);
				case NEQ -> comparisonInstructions(mv, IF_ICMPEQ);
				case LT -> comparisonInstructions(mv, IF_ICMPGE);
				case LE -> comparisonInstructions(mv, IF_ICMPGT);
				case GT -> comparisonInstructions(mv, IF_ICMPLE);
				case GE -> comparisonInstructions(mv, IF_ICMPLT);
				default ->throw new IllegalStateException("code gen bug in visitExpressionBinary NUMBER");
			};
		}
		case BOOLEAN -> {
//			implementation of TRUE = TRUE AND FALSE=FALSE is not done.
			if(expressionBinary.e0.getFirstToken().getBooleanValue() != expressionBinary.e1.getFirstToken().getBooleanValue())
			{
				if(op == Kind.GT && e0.getBooleanValue()==Boolean.TRUE) {
					mv.visitLdcInsn(e0.getBooleanValue());
				}
				else if(op==Kind.LT && e1.getBooleanValue()==Boolean.TRUE) {
					mv.visitLdcInsn(e1.getBooleanValue());
				}
				else if(op==Kind.NEQ)
				{
					mv.visitLdcInsn(Boolean.TRUE);
				}
				else if(op==Kind.GE && e0.getBooleanValue()==Boolean.TRUE)
				{
					mv.visitLdcInsn(Boolean.TRUE);
				}
				else if(op==Kind.LE && e1.getBooleanValue()==Boolean.TRUE)
				{
					mv.visitLdcInsn(Boolean.TRUE);
				}
				else {
					mv.visitLdcInsn(Boolean.FALSE);
				}
			}
			else {
				if(op == Kind.EQ || op == Kind.GE || op == Kind.LE) {
					mv.visitLdcInsn(Boolean.TRUE);
				}
				else {
					mv.visitLdcInsn(Boolean.FALSE);
				}
			}
			
			return null;
		}
		case STRING -> {
			switch (op) {
			case EQ -> {
				mv.visitLdcInsn(expressionBinary.e1.firstToken.getStringValue().equals(expressionBinary.e0.firstToken.getStringValue()));

			}
			case NEQ ->{
				mv.visitLdcInsn(!expressionBinary.e1.firstToken.getStringValue().equals(expressionBinary.e0.firstToken.getStringValue()));
			}
			case LT -> {
				mv.visitLdcInsn(expressionBinary.e1.firstToken.getStringValue().startsWith(expressionBinary.e0.firstToken.getStringValue())&& 
						!expressionBinary.e1.firstToken.getStringValue().equals((expressionBinary.e0.firstToken.getStringValue())));
			}
			case LE -> {
				mv.visitLdcInsn(expressionBinary.e1.firstToken.getStringValue().startsWith(expressionBinary.e0.firstToken.getStringValue()));
			}
			case GT -> {
				mv.visitLdcInsn(expressionBinary.e0.firstToken.getStringValue().endsWith(expressionBinary.e1.firstToken.getStringValue())&& 
						!expressionBinary.e1.firstToken.getStringValue().equals(expressionBinary.e0.firstToken.getStringValue()));
			}
			case GE -> {
				mv.visitLdcInsn(expressionBinary.e0.firstToken.getStringValue().endsWith(expressionBinary.e1.firstToken.getStringValue()));
			}
			default -> {
				throw new IllegalStateException("code gen bug in visitExpressionBinary STRING");
			}
			}
			;
		}
		default -> {
			throw new IllegalStateException("code gen bug in visitExpressionBinary");
		}
		}
		return null;
	}

	@Override
	public Object visitExpressionIdent(ExpressionIdent expressionIdent, Object arg) throws PLPException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpressionNumLit(ExpressionNumLit expressionNumLit, Object arg) throws PLPException {
		MethodVisitor mv = (MethodVisitor)arg;
		mv.visitLdcInsn(expressionNumLit.getFirstToken().getIntValue());
		return null;
	}

	@Override
	public Object visitExpressionStringLit(ExpressionStringLit expressionStringLit, Object arg) throws PLPException {
		MethodVisitor mv = (MethodVisitor)arg;
		mv.visitLdcInsn(expressionStringLit.getFirstToken().getStringValue());
		return null;
	}

	@Override
	public Object visitExpressionBooleanLit(ExpressionBooleanLit expressionBooleanLit, Object arg) throws PLPException {
		MethodVisitor mv = (MethodVisitor)arg;
		mv.visitLdcInsn(expressionBooleanLit.getFirstToken().getBooleanValue());
		return null;
	}

	@Override
	public Object visitProcedure(ProcDec procDec, Object arg) throws PLPException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitConstDec(ConstDec constDec, Object arg) throws PLPException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatementEmpty(StatementEmpty statementEmpty, Object arg) throws PLPException {
		return null;
	}

	@Override
	public Object visitIdent(Ident ident, Object arg) throws PLPException {
		throw new UnsupportedOperationException();
	}
	
}
