/**  This code is provided for solely for use of students in the course COP5556 Programming Language Principles at the 
 * University of Florida during the Fall Semester 2022 as part of the course project.  No other use is authorized. 
 */

package edu.ufl.cise.plpfa22.ast;

import edu.ufl.cise.plpfa22.PLPException;

public interface ASTVisitor {

	Object visitBlock(Block block, Object arg) throws PLPException;

	Object visitProgram(Program program, Object arg)throws PLPException;
	
	Object visitStatementAssign(StatementAssign statementAssign, Object arg) throws PLPException;

	Object visitVarDec(VarDec varDec, Object arg) throws PLPException;

	Object visitStatementCall(StatementCall statementCall, Object arg) throws PLPException;

	Object visitStatementInput(StatementInput statementInput, Object arg) throws PLPException;

	Object visitStatementOutput(StatementOutput statementOutput, Object arg) throws PLPException;

	Object visitStatementBlock(StatementBlock statementBlock, Object arg) throws PLPException;

	Object visitStatementIf(StatementIf statementIf, Object arg) throws PLPException;

	Object visitStatementWhile(StatementWhile statementWhile, Object arg) throws PLPException;

	Object visitExpressionBinary(ExpressionBinary expressionBinary, Object arg) throws PLPException;

	Object visitExpressionIdent(ExpressionIdent expressionIdent, Object arg) throws PLPException;

	Object visitExpressionNumLit(ExpressionNumLit expressionNumLit, Object arg) throws PLPException;

	Object visitExpressionStringLit(ExpressionStringLit expressionStringLit, Object arg) throws PLPException;

	Object visitExpressionBooleanLit(ExpressionBooleanLit expressionBooleanLit, Object arg) throws PLPException;

	Object visitProcedure(ProcDec procDec, Object arg) throws PLPException;

	Object visitConstDec(ConstDec constDec, Object arg) throws PLPException;

	Object visitStatementEmpty(StatementEmpty statementEmpty, Object arg) throws PLPException;

	Object visitIdent(Ident ident, Object arg) throws PLPException;
}
