/**  This code is provided for solely for use of students in the course COP5556 Programming Language Principles at the 
 * University of Florida during the Fall Semester 2022 as part of the course project.  No other use is authorized. 
 */

package edu.ufl.cise.plpfa22.ast;

import edu.ufl.cise.plpfa22.IToken;
import edu.ufl.cise.plpfa22.PLPException;

public class ExpressionNumLit extends Expression {
	
	public ExpressionNumLit(IToken firstToken) {
		super(firstToken);
	}

	@Override
	public String toString() {
		return "ExpressionNumLit [" + (firstToken != null ? "firstToken=" + firstToken : "") + "]";
	}


	@Override
	public Object visit(ASTVisitor v, Object arg) throws PLPException {
		return v.visitExpressionNumLit(this, arg);
	}

}
