/**  This code is provided for solely for use of students in the course COP5556 Programming Language Principles at the 
 * University of Florida during the Fall Semester 2022 as part of the course project.  No other use is authorized. 
 */

package edu.ufl.cise.plpfa22.ast;

import edu.ufl.cise.plpfa22.IToken;
import edu.ufl.cise.plpfa22.PLPException;

public class StatementWhile extends Statement {
	
	public final Expression expression;
	public final Statement statement;



	public StatementWhile(IToken firstToken, Expression expression, Statement statement) {
		super(firstToken);
		this.expression = expression;
		this.statement = statement;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws PLPException {
		return v.visitStatementWhile(this, arg);
	}

	@Override
	public String toString() {
		return "StatementWhile [" + (expression != null ? "expression=" + expression + ", " : "")
				+ (statement != null ? "statement=" + statement : "") + "]";
	}
	
	

}
