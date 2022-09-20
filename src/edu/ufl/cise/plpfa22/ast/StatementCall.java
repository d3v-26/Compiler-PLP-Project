/**  This code is provided for solely for use of students in the course COP5556 Programming Language Principles at the 
 * University of Florida during the Fall Semester 2022 as part of the course project.  No other use is authorized. 
 */

package edu.ufl.cise.plpfa22.ast;

import edu.ufl.cise.plpfa22.IToken;
import edu.ufl.cise.plpfa22.PLPException;

public class StatementCall extends Statement {
	
	public final Ident ident;

	public StatementCall(IToken firstToken, Ident ident) {
		super(firstToken);
		this.ident = ident;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws PLPException {
		return v.visitStatementCall(this, arg);
	}

	@Override
	public String toString() {
		return "StatementCall [" + (ident != null ? "ident=" + ident : "") + "]";
	}

	
}
