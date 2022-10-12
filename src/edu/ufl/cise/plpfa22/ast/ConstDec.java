/**  This code is provided for solely for use of students in the course COP5556 Programming Language Principles at the 
 * University of Florida during the Fall Semester 2022 as part of the course project.  No other use is authorized. 
 */

package edu.ufl.cise.plpfa22.ast;

import edu.ufl.cise.plpfa22.IToken;
import edu.ufl.cise.plpfa22.PLPException;

public class ConstDec extends Declaration {
	
	public final IToken ident;
	public final Object val;
    


	public ConstDec(IToken firstToken, IToken id, Object val) {
		super(firstToken);
		this.ident = id;
		this.val = val;
	}


	@Override
	public Object visit(ASTVisitor v, Object arg) throws PLPException {
		return v.visitConstDec(this, arg);
	}


	@Override
	public String toString() {
		return "ConstDec [" + (ident != null ? "ident=" + ident + ", " : "") + (val != null ? "val=" + val + ", " : "")
				+ (type != null ? "type=" + type : "") + "]";
	}

}
