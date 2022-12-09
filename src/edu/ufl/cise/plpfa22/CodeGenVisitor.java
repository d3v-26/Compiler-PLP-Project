package edu.ufl.cise.plpfa22;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import edu.ufl.cise.plpfa22.ast.ASTVisitor;
import edu.ufl.cise.plpfa22.CodeGenUtils.*;
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

@SuppressWarnings("unchecked")
public class CodeGenVisitor implements ASTVisitor, Opcodes {

	final String packageName;
	final String className;
	final String sourceFileName;
	final String fullyQualifiedClassName; 
	final String classDesc;
	List<GenClass> classList = new ArrayList<GenClass>();
	List<String> classes = new ArrayList<String>();
	
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
	public Object visitProgram(Program program, Object arg) throws PLPException {
		
		// Create ClassWriter for main class
		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		classWriter.visit(V16, ACC_PUBLIC | ACC_SUPER, fullyQualifiedClassName, null, "java/lang/Object", new String[] { "java/lang/Runnable" });
		
		// Code for <init>
		MethodVisitor c = classWriter.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		c.visitCode();
		c.visitVarInsn(ALOAD, 0);
		c.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
		c.visitInsn(RETURN);
		c.visitMaxs(0, 0);
		c.visitEnd();
		
		// Code for main
		MethodVisitor mv = classWriter.visitMethod(ACC_PUBLIC | ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
		mv.visitCode();
		mv.visitTypeInsn(NEW, fullyQualifiedClassName);
		mv.visitInsn(DUP);
		mv.visitMethodInsn(INVOKESPECIAL, fullyQualifiedClassName, "<init>", "()V", false);
		mv.visitMethodInsn(INVOKESPECIAL, fullyQualifiedClassName, "run", "()V", false);
		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		mv.visitEnd();
		
		classes.add(fullyQualifiedClassName);
		
		// Get args
		List<Object> args = Arrays.asList(classWriter, fullyQualifiedClassName, "this");
		
		// Passes args to Block visit
		List<GenClass> children = (List<GenClass>) program.block.visit(this, args);
		
		//finish up the class
        classWriter.visitEnd();
        
        // Add main byte code to class list
        classList.add(new GenClass(fullyQualifiedClassName, classWriter.toByteArray()));
        classList.addAll(children);
        
        // Return the said class list
        return classList;
		
	}

	
	@Override
	public Object visitBlock(Block block, Object arg) throws PLPException {
		List<Object> args = (List<Object>) arg;
		ClassWriter classWriter = (ClassWriter) args.get(0);
		String classname = (String) args.get(1);
		//String parent = (String) args.get(0);
		
		MethodVisitor methodVisitor = classWriter.visitMethod(ACC_PUBLIC, "run", "()V", null, null);
		methodVisitor.visitCode();
		
		for (ConstDec constDec : block.constDecs) {
			constDec.visit(this, classWriter);
		}
		for (VarDec varDec : block.varDecs) {
			varDec.visit(this, classWriter);
		}
		
		List<GenClass> children = new ArrayList<GenClass>();
		for (ProcDec procDec: block.procedureDecs) {
			children.addAll((Collection <? extends GenClass>)procDec.visit(this, classname));
		}
		
		List<Object> stmt_args = Arrays.asList(methodVisitor, classname);
		block.statement.visit(this, stmt_args);
		methodVisitor.visitInsn(RETURN);
		methodVisitor.visitMaxs(0,0);
		methodVisitor.visitEnd();
		return children;

	}

	@Override
	public Object visitStatementAssign(StatementAssign statementAssign, Object arg) throws PLPException {
		List<Object> args = (List<Object>) arg;
		MethodVisitor mv = (MethodVisitor) args.get(0);
		String classname = (String) args.get(1);
		
		List<Object> expr_args = Arrays.asList(mv, classname);
		statementAssign.expression.visit(this, expr_args);	//	visit the expression and evaluate it
		
		int nest = statementAssign.ident.getDec().getNest();
		String parent = classes.get(nest), id = statementAssign.ident.getFirstToken().getStringValue();
		Type t = statementAssign.expression.getType();
		
		mv.visitVarInsn(ALOAD, 0);
		if(classes.size() != nest+1)
		{
			mv.visitFieldInsn(GETFIELD, classname, "this$"+nest, CodeGenUtils.toJVMClassDesc(parent));
		}
		mv.visitInsn(SWAP);
		String type = t == Type.NUMBER ? "I" : t == Type.BOOLEAN ? "Z" : "Ljava/lang/String;";
		mv.visitFieldInsn(PUTFIELD, parent, id, type);
		
		//statementAssign.ident.visit(this, arg);			//	visit the ident
		
		return null;
	}

	@Override
	public Object visitVarDec(VarDec varDec, Object arg) throws PLPException {
		ClassWriter classWriter = (ClassWriter) arg;
        Type t = varDec.getType();
        String type = t == Type.NUMBER ? "I" : t == Type.BOOLEAN ? "Z" : "Ljava/lang/String;";
        classWriter.visitField(ACC_PUBLIC, varDec.ident.getStringValue(), type, null, null);
        classWriter.visitEnd();
        return null;
	}

	@Override
	public Object visitStatementCall(StatementCall statementCall, Object arg) throws PLPException {
		
		String proc = statementCall.ident.getFirstToken().getStringValue();
		List<Object> args = (List<Object>) arg;
		MethodVisitor mv = (MethodVisitor) args.get(0);
		String classname = (String) args.get(1);
		int nest = statementCall.ident.getDec().getNest();
		String parent = classes.get(nest);
	
		String callProcname = parent + "$" + proc;
		
		mv.visitTypeInsn(NEW, callProcname);
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, 0);
		
		if(classes.size() != nest+1)
		{
			mv.visitFieldInsn(GETFIELD, classname, "this$" + nest, CodeGenUtils.toJVMClassDesc(parent));
		}
		mv.visitMethodInsn(INVOKESPECIAL, callProcname, "<init>", "("+CodeGenUtils.toJVMClassDesc(parent)+")V", false);
		mv.visitMethodInsn(INVOKESPECIAL, callProcname, "run", "()V", false);
		return null;
	}

	@Override
	public Object visitStatementInput(StatementInput statementInput, Object arg) {
		
		List<Object> args = (List<Object>) arg;
		MethodVisitor mv = (MethodVisitor) args.get(0);
		String classname = (String) args.get(1);
		
		String id = statementInput.ident.getFirstToken().getStringValue();
		Type t = statementInput.ident.getDec().getType();
		mv.visitVarInsn(ALOAD, 0);
		mv.visitTypeInsn(NEW, "java/util/Scanner");
		mv.visitInsn(DUP);
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream");
		mv.visitMethodInsn(INVOKESPECIAL, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V", false);
		
		switch(t)
		{
			case STRING -> mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/Scanner", "nextLine", "()Ljava/lang/String;", false);
			case NUMBER -> mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/Scanner", "nextInt", "()I", false);
			case BOOLEAN -> {
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/Scanner", "nextLine", "()Ljava/lang/String;", false);
				mv.visitLdcInsn("TRUE");
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
				
			}
			default -> {
				// we Should not reach here!!
				throw new IllegalStateException("bug!");
			}
		}

		String type = t == Type.NUMBER ? "I" : t == Type.BOOLEAN ? "Z" : "Ljava/lang/String;";
		mv.visitFieldInsn(PUTFIELD, classname, id, type);
		return null;
	}

	@Override
	public Object visitStatementOutput(StatementOutput statementOutput, Object arg) throws PLPException {
		List<Object> args = (List<Object>) arg;
		MethodVisitor mv = (MethodVisitor) args.get(0);
		String classname = (String) args.get(1);
		
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		List<Object> stmt_args = Arrays.asList(mv, classname);
		statementOutput.expression.visit(this, stmt_args);
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
		List<Object> args = (List<Object>) arg;
		MethodVisitor mv = (MethodVisitor) args.get(0);
		String classname = (String) args.get(1);
		List<Object> expr_args = Arrays.asList(mv, classname);
		statementIf.expression.visit(this, expr_args);
		Label labelComparisonFalseBr = new Label();
		mv.visitJumpInsn(IFEQ, labelComparisonFalseBr);

		List<Object> stmt_args = Arrays.asList(mv, classname);
		statementIf.statement.visit(this, stmt_args);
		mv.visitLabel(labelComparisonFalseBr);
		return null;
	}

	@Override
	public Object visitStatementWhile(StatementWhile statementWhile, Object arg) throws PLPException {
		List<Object> args = (List<Object>) arg;
        MethodVisitor mv = (MethodVisitor) args.get(0);
        String classname = (String) args.get(1);
        Label labelWhile = new Label();
        mv.visitLabel(labelWhile);
        List<Object> expr_args = Arrays.asList(mv, classname);
        statementWhile.expression.visit(this, expr_args);
        final Label labelCompFalseBr = new Label();
        // if expression is false then jump to the end label
        mv.visitJumpInsn(IFEQ, labelCompFalseBr);
        // executed only if expression true
        statementWhile.statement.visit(this, arg);
        mv.visitJumpInsn(GOTO, labelWhile);
        mv.visitLabel(labelCompFalseBr);
        return null;
	}

	@Override
	public Object visitExpressionBinary(ExpressionBinary expressionBinary, Object arg) throws PLPException {
		List<Object> args = (List<Object>) arg;
        MethodVisitor mv = (MethodVisitor) args.get(0);
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
		
		List<Object> args = (List<Object>) arg;
		MethodVisitor mv = (MethodVisitor) args.get(0);
		String classname = (String) args.get(1);
		
		Declaration dec = expressionIdent.getDec();
		int cnest = expressionIdent.getNest();
		int nest = dec.getNest();
		int inx = classname.lastIndexOf("$");
		String parent = null;
		if(inx != -1) parent = classname.substring(0, inx);
		String id = String.valueOf(expressionIdent.firstToken.getText());
		Type t = expressionIdent.getType();
			
//      for (int i = totalClasses - 1; i >= 0; i--) {
//      c.visitVarInsn(ALOAD, 0);
//      c.visitVarInsn(ALOAD, 1);
//      
//      for (int j = 1; j < totalClasses - i; j++) {
//          c.visitFieldInsn(GETFIELD, classes.get(totalClasses - j), "this$0", CodeGenUtils.toJVMClassDesc(classes.get(totalClasses - j - 1)));
//      }
//      
//      //c.visitFieldInsn(PUTFIELD, className, "this$" + i, CodeGenUtils.toJVMClassDesc(classes.get(i)));
//      c.visitFieldInsn(PUTFIELD, className, "this$" + i, CodeGenUtils.toJVMClassDesc(parent));
//  }
  
		if(dec instanceof ConstDec) {
			ConstDec c = (ConstDec) dec;
			mv.visitLdcInsn(c.val);			
		}
		else {
			String currentClass = classname;
			String parentClass = parent;
			mv.visitVarInsn(ALOAD, 0);
			// GETFIELD
			for(int i = cnest-1; i >= nest; i--)
			{
				mv.visitFieldInsn(GETFIELD, currentClass, "this$"+i, CodeGenUtils.toJVMClassDesc(parentClass));
				int cinx = currentClass.lastIndexOf("$");
				if(cinx == -1) break;
				currentClass = currentClass.substring(0, cinx);
				
				int pinx = parentClass.lastIndexOf("$");
				if(pinx == -1) break;
				parentClass = parentClass.substring(0, pinx);
			}

			String type = t == Type.NUMBER ? "I" : t == Type.BOOLEAN ? "Z" : "Ljava/lang/String;";

			mv.visitFieldInsn(GETFIELD, currentClass, id, type);

		}
			
		
		return null;
	}

	@Override
	public Object visitExpressionNumLit(ExpressionNumLit expressionNumLit, Object arg) throws PLPException {
		List<Object> args = (List<Object>) arg;
		MethodVisitor mv = (MethodVisitor) args.get(0);
		mv.visitLdcInsn(expressionNumLit.getFirstToken().getIntValue());
		return null;
	}

	@Override
	public Object visitExpressionStringLit(ExpressionStringLit expressionStringLit, Object arg) throws PLPException {
		List<Object> args = (List<Object>) arg;
		MethodVisitor mv = (MethodVisitor) args.get(0);
		mv.visitLdcInsn(expressionStringLit.getFirstToken().getStringValue());
		return null;
	}

	@Override
	public Object visitExpressionBooleanLit(ExpressionBooleanLit expressionBooleanLit, Object arg) throws PLPException {
		List<Object> args = (List<Object>) arg;
		MethodVisitor mv = (MethodVisitor) args.get(0);
		mv.visitLdcInsn(expressionBooleanLit.getFirstToken().getBooleanValue());
		return null;
	}

	@Override
	public Object visitProcedure(ProcDec procDec, Object arg) throws PLPException {
		ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        String parent = (String) arg;
        String proc = procDec.ident.getStringValue();

        String className = parent + "$" + proc;
        classWriter.visit(V16, ACC_PUBLIC | ACC_SUPER, className, null, "java/lang/Object", new String[]{"java/lang/Runnable"});

        classWriter.visitNestHost(fullyQualifiedClassName);
        classWriter.visitInnerClass(className, parent, proc, 0);

        for (int i = 0; i < classes.size(); i++) {
            classWriter.visitField(ACC_FINAL | ACC_SYNTHETIC, "this$" + i, CodeGenUtils.toJVMClassDesc(classes.get(i)), null, null);
            classWriter.visitEnd();
        }

        MethodVisitor c = classWriter.visitMethod(0, "<init>", "(" + CodeGenUtils.toJVMClassDesc(parent) + ")V", null, null);
        c.visitCode();

        int totalClasses = classes.size();
        for (int i = totalClasses - 1; i >= 0; i--) {
            c.visitVarInsn(ALOAD, 0);
            c.visitVarInsn(ALOAD, 1);
            
            for (int j = 1; j < totalClasses - i; j++) {
                c.visitFieldInsn(GETFIELD, classes.get(totalClasses - j), "this$0", CodeGenUtils.toJVMClassDesc(classes.get(totalClasses - j - 1)));
            }
            
            c.visitFieldInsn(PUTFIELD, className, "this$" + i, CodeGenUtils.toJVMClassDesc(classes.get(i)));
        }

        c.visitVarInsn(ALOAD, 0);
        c.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        c.visitInsn(RETURN);
        c.visitMaxs(0, 0);
        c.visitEnd();
        
        classes.add(className);
        List<Object> block_args = Arrays.asList(classWriter, className);
        List<GenClass> children = (List<GenClass>) procDec.block.visit(this, block_args);
        classWriter.visitEnd();
        

        List<GenClass> allClasses = new ArrayList<>();
        
        allClasses.add(new GenClass(className, classWriter.toByteArray()));
        allClasses.addAll(children);
        classes.remove(classes.size() - 1);
        
        return allClasses;
	}

	@Override
	public Object visitConstDec(ConstDec constDec, Object arg) throws PLPException {
		ClassWriter classWriter = (ClassWriter) arg;
        Type t = constDec.getType();
        String type = t == Type.NUMBER ? "I" : t == Type.BOOLEAN ? "Z" : "Ljava/lang/String;";
        classWriter.visitField(ACC_PUBLIC | ACC_FINAL, constDec.ident.getStringValue(), type, null, constDec.val);
        classWriter.visitEnd();
        return null;
	}

	@Override
	public Object visitStatementEmpty(StatementEmpty statementEmpty, Object arg) throws PLPException {
		return null;
	}

	@Override
	public Object visitIdent(Ident ident, Object arg) throws PLPException {
		return null;
	}

}
