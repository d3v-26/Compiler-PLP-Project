/**  This code is provided for solely for use of students in the course COP5556 Programming Language Principles at the 
 * University of Florida during the Fall Semester 2022 as part of the course project.  No other use is authorized. 
 */

package edu.ufl.cise.plpfa22.ast;

import edu.ufl.cise.plpfa22.IToken;
import edu.ufl.cise.plpfa22.PLPException;

public class ExpressionBinary extends Expression {

	public final Expression e0;
	public final IToken op;
	public final Expression e1;
	
	public ExpressionBinary(IToken firstToken, Expression e0, IToken op, Expression e1) {
		super(firstToken);
		this.e0 = e0;
		this.op = op;
		this.e1 = e1;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws PLPException {
		return v.visitExpressionBinary(this, arg);
	}

	@Override
	public String toString() {
		return "ExpressionBinary [" + (e0 != null ? "e0=" + e0 + ", " : "") + (op != null ? "op=" + op + ", " : "")
				+ (e1 != null ? "e1=" + e1 : "") + "]";
	}

	
}
