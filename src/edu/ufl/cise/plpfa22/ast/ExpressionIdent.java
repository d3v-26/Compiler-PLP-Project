/**  This code is provided for solely for use of students in the course COP5556 Programming Language Principles at the 
 * University of Florida during the Fall Semester 2022 as part of the course project.  No other use is authorized. 
 */

package edu.ufl.cise.plpfa22.ast;

import edu.ufl.cise.plpfa22.IToken;
import edu.ufl.cise.plpfa22.PLPException;

public class ExpressionIdent extends Expression {
	
	Declaration dec;
	
	public ExpressionIdent(IToken firstToken) {
		super(firstToken);
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws PLPException {
		return v.visitExpressionIdent(this, arg);
	}

	
	@Override
	public String toString() {
		return "ExpressionIdent [" + (dec != null ? "dec=" + dec + ", " : "")
				+ (type != null ? "type=" + type + ", " : "") + (firstToken != null ? "firstToken=" + firstToken : "")
				+ "]";
	}


	public Declaration getDec() {
		return dec;
	}


	public void setDec(Declaration dec) {
		this.dec = dec;
	}
	
	

}
