/**  This code is provided for solely for use of students in the course COP5556 Programming Language Principles at the 
 * University of Florida during the Fall Semester 2022 as part of the course project.  No other use is authorized. 
 */

package edu.ufl.cise.plpfa22.ast;

import edu.ufl.cise.plpfa22.IToken;
import edu.ufl.cise.plpfa22.PLPException;

public class StatementAssign extends Statement {
	
	public final Ident ident;
	public final Expression expression;
	
	public StatementAssign(IToken firstToken, Ident var, Expression e) {
		super(firstToken);
		this.ident = var;
		this.expression = e;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws PLPException {
		return v.visitStatementAssign(this, arg);
	}

	@Override
	public String toString() {
		return "StatementAssign [" + (ident != null ? "var=" + ident + ", " : "") + (expression != null ? "e=" + expression : "") + "]";
	}
	
	

}
