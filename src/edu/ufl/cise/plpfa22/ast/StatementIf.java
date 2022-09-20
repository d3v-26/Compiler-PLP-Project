/**  This code is provided for solely for use of students in the course COP5556 Programming Language Principles at the 
 * University of Florida during the Fall Semester 2022 as part of the course project.  No other use is authorized. 
 */

package edu.ufl.cise.plpfa22.ast;

import edu.ufl.cise.plpfa22.IToken;
import edu.ufl.cise.plpfa22.PLPException;

public class StatementIf extends Statement {
	
	public final Expression expression;
	public final Statement statement;


	@Override
	public Object visit(ASTVisitor v, Object arg) throws PLPException {
		return v.visitStatementIf(this, arg);
	}


	public StatementIf(IToken firstToken, Expression condition, Statement statement) {
		super(firstToken);
		this.expression = condition;
		this.statement = statement;
	}

}
