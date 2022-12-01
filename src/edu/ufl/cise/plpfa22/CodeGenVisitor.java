package edu.ufl.cise.plpfa22;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import edu.ufl.cise.plpfa22.ast.ASTVisitor;
import edu.ufl.cise.plpfa22.CodeGenUtils.GenClass;
import edu.ufl.cise.plpfa22.IToken.Kind;
import edu.ufl.cise.plpfa22.ast.Block;
import edu.ufl.cise.plpfa22.ast.ConstDec;
import edu.ufl.cise.plpfa22.ast.Declaration;
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
	
	public void stringComparisonInstructions(MethodVisitor mv, int opcode, String method)
	{
		mv.visitVarInsn(ASTORE, 1); // Store the first string
		mv.visitVarInsn(ASTORE, 2); // Store the next string
		
		// Load both strings
		mv.visitVarInsn(ALOAD, 2); 
		mv.visitVarInsn(ALOAD, 1);
		
		if("startsWith".equals(method)) mv.visitInsn(SWAP);
		
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", method, "(Ljava/lang/String;)Z", false);
		
		// Load both strings
		mv.visitVarInsn(ALOAD, 2); 
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
		
		if(opcode == IAND) comparisonInstructions(mv, IFNE);
		
		mv.visitInsn(opcode);
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
		
		List<String> classNames = new ArrayList<String>();
		
		// Creates ClassWriter for main class
		classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		classWriter.visit(V18, ACC_PUBLIC | ACC_SUPER, fullyQualifiedClassName, null, "java/lang/Object", new String[] { "java/lang/Runnable" });
		
		// Invokes a simple ASTVisitor to visit all procedure declarations and annotate them with their JVM names
			// Incomplete.
			// Simple AStVisitor = new ASTVisitor()
			// ast.visit(ASTVisitor, String[] names);
			// List<String> classNames = ast.visit(new CCF.ASTVisitorImpl(), fullyQualifiedClassName);
		// End
		
		// Passes Class Writer to Block visit
		program.block.visit(this, classWriter);
		//finish up the class
        classWriter.visitEnd();
        
        // Other Bookkeeping Details
        // ???
        
        List<GenClass> genClasses = new ArrayList<GenClass>();
        
        //return the bytes making up the classfile
		return classWriter.toByteArray();
		
		// return List<GenClass>
		// For el in className:
		// 		genClasses.push(el, byte_code_for_el);
		
		
		// Return genClasses.
		
	}

	@Override
	public Object visitStatementAssign(StatementAssign statementAssign, Object arg) throws PLPException {
		
		statementAssign.expression.visit(this, arg);	//	visit the expression
		
		statementAssign.ident.visit(this, arg);			//	visit the ident
		
		return null;
	}

	@Override
	public Object visitVarDec(VarDec varDec, Object arg) throws PLPException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatementCall(StatementCall statementCall, Object arg) throws PLPException {
		
		MethodVisitor mv = (MethodVisitor)arg;
		
		// Creates ClassWriter for given class
		classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		
		String className = fullyQualifiedClassName;
		
		try {
			mv.visitVarInsn(ALOAD, 0);
			// concat full name behind classname i.e. + "$" + statementCall.ident	
		}
		finally{
			// itereate list of all procedure names to find out enclosing procedure
			// concat full name behind classname i.e. + "$" + statementCall.ident	
		}
		
		classWriter.visit(V18, ACC_PUBLIC | ACC_SUPER, className , "<init>", "java/lang/Object", new String[] { "java/lang/Runnable" });
		
		//finish up the class
		classWriter.visitEnd();
		
		return null;
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
		for(Statement s : statementBlock.statements) {
			s.visit(this, arg);
		}
		return null;
	}

	@Override
	public Object visitStatementIf(StatementIf statementIf, Object arg) throws PLPException {
		MethodVisitor mv = (MethodVisitor)arg;
		statementIf.expression.visit(this, arg);
		Label labelComparisonFalseBr = new Label();
		mv.visitJumpInsn(IFEQ, labelComparisonFalseBr);

		statementIf.statement.visit(this, arg);
		mv.visitLabel(labelComparisonFalseBr);
		return null;
	}

	@Override
	public Object visitStatementWhile(StatementWhile statementWhile, Object arg) throws PLPException {
		//	created according to official format
		MethodVisitor mv = (MethodVisitor)arg;
		
		Label GuardLabel = new Label();						//      declarations of labels
		Label BodyLabel = new Label();
		
		mv.visitJumpInsn(GOTO, GuardLabel);					//		GOTO GuardLabel

		mv.visitLabel(BodyLabel);							//		BodyLabel
		
		statementWhile.statement.visit(this, arg);			//		visit Loop body  

		mv.visitLabel(GuardLabel);    						//		GuardLabel

		statementWhile.expression.visit(this, arg);         //		Evaluate guard expression

		mv.visitJumpInsn(IFNE, BodyLabel);					//		IFNE BodyLabel
		
		// 	ref : visit local variables in block
		return null;
	}

	@Override
	public Object visitExpressionBinary(ExpressionBinary expressionBinary, Object arg) throws PLPException {
		MethodVisitor mv = (MethodVisitor) arg;
		Type argType = expressionBinary.e0.getType();
		Kind op = expressionBinary.op.getKind();
		if(argType != Type.NUMBER && argType != Type.BOOLEAN && argType != Type.STRING) throw new IllegalStateException("code gen bug in visitExpressionBinary");
		expressionBinary.e0.visit(this, arg);
		expressionBinary.e1.visit(this, arg);
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
				switch (op) {
					case PLUS -> mv.visitInsn(IOR);
					case TIMES -> mv.visitInsn(IAND);
					case EQ -> comparisonInstructions(mv, IF_ICMPNE);
					case NEQ -> comparisonInstructions(mv, IF_ICMPEQ);
					case LT -> comparisonInstructions(mv, IF_ICMPGE);
					case LE -> comparisonInstructions(mv, IF_ICMPGT);
					case GT -> comparisonInstructions(mv, IF_ICMPLE);
					case GE -> comparisonInstructions(mv, IF_ICMPLT);
					default ->throw new IllegalStateException("code gen bug in visitExpressionBinary BOOLEAN");
				};
			}
			case STRING -> {
				switch (op) {
					case PLUS -> mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "concat", "(Ljava/lang/String;)Ljava/lang/String;", false);
					case EQ -> mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
					case NEQ -> {
						mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
						comparisonInstructions(mv, IFNE);
					}
					case LT -> stringComparisonInstructions(mv, IAND, "startsWith");
					case LE -> stringComparisonInstructions(mv, IOR,  "startsWith");
					case GT -> stringComparisonInstructions(mv, IAND, "endsWith");
					case GE -> stringComparisonInstructions(mv, IOR,  "endsWith");
					default ->throw new IllegalStateException("code gen bug in visitExpressionBinary STRING");
				};
			}
			default -> {
				throw new IllegalStateException("code gen bug in visitExpressionBinary");
			}
		}
		return null;
	}

	@Override
	public Object visitExpressionIdent(ExpressionIdent expressionIdent, Object arg) throws PLPException {
		
		MethodVisitor mv = (MethodVisitor)arg;
		
		Declaration declaration = expressionIdent.getDec();
		
		if (declaration instanceof VARIABLE) {			// VARIABLE NEEDS TO BE REPLACED
			
			switch(declaration.getType())
			{
			
			case NUMBER:
				mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, className, declaration.firstToken.getStringValue(), "I");
                break;
                
			case BOOLEAN:
				mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, className, declaration.firstToken.getStringValue(), "Z");
                break;
            
			case STRING:
				mv.visitVarInsn(ALOAD, 0);
                mv.visitFieldInsn(GETFIELD, className, declaration.firstToken.getStringValue(), "Ljava/lang/String;");
                break;
				
			default:
				throw new RuntimeException("Not a valid Identifier");
			
			}
		}
		
		else {
			
			mv.visitVarInsn(ILOAD, declaration.getNest());		// if variable is constant, just load the value
			
		}
	
		
		return null;
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
		// get Current class name	edu/ufl/cise/plpfa22/prog
		String className = fullyQualifiedClassName;
		
		// Append procDec.procedure name
		// New class name edu/ufl/cise/plpfa22/prog$p
		className.concat(procDec.ident.getStringValue());
		
		
		// Creates ClassWriter for given class
		classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		
		//add field for reference to enclosing class (this$n where n is nesting level)      // IMPLEMENTATION REQUIRED
		
		//create init method that takes an instance of enclosing class as parameter and initializes this$n,then invokes superclass constructor (java/lang/Object).
		classWriter.visit(V18, ACC_PUBLIC | ACC_SUPER, className, "<init>", "java/lang/Object", new String[] { "java/lang/Runnable" });
		
		//Visit block to create run method  // visitBlock(Block);							// IMPLEMENTATION REQUIRED
		
		// Pop current proc name
		// New class name edu/ufl/cise/plpfa22/prog
		
		return null;
	}

	@Override
	public Object visitConstDec(ConstDec constDec, Object arg) throws PLPException {
		return null;
	}

	@Override
	public Object visitStatementEmpty(StatementEmpty statementEmpty, Object arg) throws PLPException {
		return null;
	}

	@Override
	public Object visitIdent(Ident ident, Object arg) throws PLPException {
//		Declaration declaration= ident.getDec();
//		
		MethodVisitor mv = (MethodVisitor)arg;
//		
//		
//		
//		if (declaration instanceof VARIABLE) {			// VARIABLE NEEDS TO BE REPLACED
//			
//			switch(declaration.getType())
//			{
//			
//			case NUMBER:
//				mv.visitVarInsn(ALOAD, 0);
//                mv.visitFieldInsn(GETFIELD, className, declaration.firstToken.getStringValue(), "I");
//                break;
//                
//			case BOOLEAN:
//				mv.visitVarInsn(ALOAD, 0);
//                mv.visitFieldInsn(GETFIELD, className, declaration.firstToken.getStringValue(), "Z");
//                break;
//            
//			case STRING:
//				mv.visitVarInsn(ALOAD, 0);
//                mv.visitFieldInsn(GETFIELD, className, declaration.firstToken.getStringValue(), "Ljava/lang/String;");
//                break;
//				
//			default:
//				throw new RuntimeException("Not a valid Identifier");
//			
//			}
//		}
//		
//		else {
//			
//			mv.visitVarInsn(ILOAD, declaration.getNest());		// if variable is constant, just load the value
//			
//		}
//		
		Type t = ident.getDec().getType();
		String type = t == Type.NUMBER ? "I" : t == Type.BOOLEAN ? "Z" : "Ljava/lang/String;"; 
		mv.visitFieldInsn(PUTFIELD, fullyQualifiedClassName, String.valueOf(ident.firstToken.getText()), type);
		
		return null;
	}

}
