/**  This code is provided for solely for use of students in the course COP5556 Programming Language Principles at the 
 * University of Florida during the Fall Semester 2022 as part of the course project.  No other use is authorized. 
 */

package edu.ufl.cise.plpfa22.ast;

import java.util.List;

import edu.ufl.cise.plpfa22.IToken;
import edu.ufl.cise.plpfa22.PLPException;

public class StatementBlock extends Statement {
	
	public final List<Statement> statements;

	@Override
	public Object visit(ASTVisitor v, Object arg) throws PLPException {
		return v.visitStatementBlock(this, arg);
	}

	public StatementBlock(IToken firstToken, List<Statement> statements) {
		super(firstToken);
		this.statements = statements;
	}

	@Override
	public String toString() {
		return "StatementBlock [" + (statements != null ? "statements=" + statements : "") + "]";
	}

	
}
