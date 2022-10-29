/**  This code is provided for solely for use of students in the course COP5556 Programming Language Principles at the 
 * University of Florida during the Fall Semester 2022 as part of the course project.  No other use is authorized. 
 */


package edu.ufl.cise.plpfa22.ast;

import edu.ufl.cise.plpfa22.PLPException;
import edu.ufl.cise.plpfa22.ast.Types.Type;

public class PrettyPrintVisitor implements ASTVisitor {

	
public static String AST2String(ASTNode ast) throws PLPException {
    PrettyPrintVisitor v = new PrettyPrintVisitor("  ");
    String s = (String) ast.visit(v, null);
    return s;
}
	
	static class PPVStringBuilder{
		static String indent = "";
		
		public PPVStringBuilder(String indent){
			this.indent=indent;
		}

		StringBuilder sb = new StringBuilder();
		String currIndent = indent;
		
		PPVStringBuilder append(String text) {
			sb.append('\n').append(currIndent).append(text);
			return this;
		}
		
		PPVStringBuilder down(String text) {
			down();
			sb.append('\n').append(currIndent).append(text);
			return this;
		}
		
		PPVStringBuilder down() {
			currIndent = currIndent + indent;
			return this;
		}
		
		PPVStringBuilder up(String text) {
			up();
			sb.append('\n').append(currIndent).append(text);
			return this;			
		}
		
		PPVStringBuilder up() {
			currIndent = currIndent.substring(indent.length());
			return this;			
		}
		
		@Override
		public String toString() {
			return sb.toString();
		}
		
	}
	
	
	PPVStringBuilder sb;
	
	public PrettyPrintVisitor(String indent) {
		sb = new PPVStringBuilder(indent);
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws PLPException {
		sb.append("BLOCK");
		sb.down("ConstDecs " + (block.constDecs.size() == 0 ? " none" : "") );
		sb.down();
		for (ConstDec dec : block.constDecs) {
			dec.visit(this, arg);
		}
		sb.up();
		sb.append("VarDecs"+ (block.varDecs.size() == 0 ? " none" : ""));
		sb.down();
		for (VarDec dec : block.varDecs) {
			dec.visit(this, arg);
		}
		sb.up();
		sb.append("ProcDecs"+ (block.procedureDecs.size() == 0 ? " none" : ""));
		sb.down();
		for (ProcDec dec : block.procedureDecs) {
			dec.visit(this, arg);
		}
		sb.up();
		sb.append("STATEMENT");
		sb.down();
		block.statement.visit(this,arg);
		sb.up("END OF STATEMENT");
		sb.up();
		sb.append("END OF BLOCK");
		return null;
	}

	@Override
	public Object visitProgram(Program program, Object arg) throws PLPException {
		sb.append("PROGRAM");
		sb.down();
		program.block.visit(this, null);
		sb.up("END OF PROGRAM");
		return sb.toString();
	}

	@Override
	public Object visitStatementAssign(StatementAssign statementAssign, Object arg) throws PLPException {
		sb.append("ASSIGNMENT");
		sb.down();
		statementAssign.ident.visit(this, null);
		statementAssign.expression.visit(this, null);
		sb.up();
		return null;
	}

	@Override
	public Object visitVarDec(VarDec varDec, Object arg) throws PLPException {
		String identText = String.valueOf(varDec.ident.getText());
		int nest = varDec.getNest();
		Types.Type type = varDec.getType();
		sb.append("VAR " + identText +  " at nest level " + nest + " type="+type);
		return null;
	}

	@Override
	public Object visitStatementCall(StatementCall statementCall, Object arg) throws PLPException {
		sb.append("CALL");
		sb.down();
		statementCall.ident.visit(this, null);
		sb.up();
		return null;		
	}

	@Override
	public Object visitStatementInput(StatementInput statementInput, Object arg) throws PLPException {
		sb.append("INPUT");
		sb.down();
		statementInput.ident.visit(this, null);
		sb.up();
		return null;		
	}

	@Override
	public Object visitStatementOutput(StatementOutput statementOutput, Object arg) throws PLPException {
		sb.append("OUTPUT");
		sb.down();
		statementOutput.expression.visit(this, arg);
		sb.up();
		return null;
	}

	@Override
	public Object visitStatementBlock(StatementBlock statementBlock, Object arg) throws PLPException {
		sb.append("BEGIN");
		sb.down();
		for(Statement s: statementBlock.statements) {
			s.visit(this, arg);
		}
		sb.up("END");
		return null;
	}

	@Override
	public Object visitStatementIf(StatementIf statementIf, Object arg) throws PLPException {
		sb.append("IF");
		sb.down();
		statementIf.expression.visit(this,arg);
		sb.up("THEN");
		sb.down();
		statementIf.statement.visit(this,arg);
		sb.up("END OF IF");
		return null;
	}

	@Override
	public Object visitStatementWhile(StatementWhile statementWhile, Object arg) throws PLPException {
		sb.append("WHILE");
		sb.down();
		statementWhile.expression.visit(this,arg);
		sb.up("DO");
		sb.down();
		statementWhile.statement.visit(this,arg);
		sb.up("END OF WHILE");
		return null;
	}

	@Override
	public Object visitExpressionBinary(ExpressionBinary expressionBinary, Object arg) throws PLPException {
		sb.append("binary expr");
		sb.down();
		expressionBinary.e0.visit(this, arg);
		sb.append(String.valueOf(expressionBinary.op.getText()));
		expressionBinary.e1.visit(this,arg);
		sb.up();
		return null;
	}

	@Override
	public Object visitExpressionIdent(ExpressionIdent expressionIdent, Object arg) throws PLPException {
		Declaration dec = expressionIdent.getDec();
		int decNest = dec.getNest();
		int identNest = expressionIdent.getNest();
		Types.Type type = expressionIdent.getType();
		String name = String.valueOf(expressionIdent.getFirstToken().getText());
		sb.append("ExpressionIdent  " + name + " identNest=" + identNest + " decNest="+decNest + " type="+type);
		return null;
	}

	@Override
	public Object visitExpressionNumLit(ExpressionNumLit expressionNumLit, Object arg) throws PLPException {
		sb.append("NumLit " + expressionNumLit.getFirstToken().getIntValue());
		return null;
	}

	@Override
	public Object visitExpressionStringLit(ExpressionStringLit expressionStringLit, Object arg) throws PLPException {
		sb.append("StringLit \"" + expressionStringLit.getFirstToken().getStringValue() + "\"");
		return null;
	}

	@Override
	public Object visitExpressionBooleanLit(ExpressionBooleanLit expressionBooleanLit, Object arg) throws PLPException {
		sb.append("BooleanLit " + expressionBooleanLit.getFirstToken().getBooleanValue());
		return null;
	}

	@Override
	public Object visitProcedure(ProcDec procDec, Object arg) throws PLPException {
		String name = String.valueOf(procDec.ident.getText());
		int nest = procDec.getNest();
		sb.append("PROCEDURE " + name + " at nesting level " + nest );
		sb.down();
		procDec.block.visit(this, arg);
		sb.up("END OF PROCEDURE " + name );
		return null;
	}

	@Override
	public Object visitConstDec(ConstDec constDec, Object arg) throws PLPException {
		String identText = String.valueOf(constDec.ident.getText());
		int nest = constDec.getNest();
		String valueText = constDec.val.toString();
		Type type = constDec.getType();
		sb.append("CONST " + identText + "=" + valueText + " at nest level " + nest+ " type=" + type);
		return null;
	}

	@Override
	public Object visitStatementEmpty(StatementEmpty statementEmpty, Object arg) throws PLPException {
		sb.append("EmptyStatement");
		return null;
	}

	@Override
	public Object visitIdent(Ident ident, Object arg) throws PLPException {
		Declaration dec = ident.getDec();
		int decNest = dec.getNest();
		int identNest = ident.getNest();
		String name = String.valueOf(ident.getFirstToken().getText());
		Type type = dec.getType();
		sb.append("Ident  " + name + " identNest=" + identNest + " decNest="+decNest + " type=" + type);
		return null;
	}

}
