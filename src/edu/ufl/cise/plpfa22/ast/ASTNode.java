/**  This code is provided for solely for use of students in the course COP5556 Programming Language Principles at the 
 * University of Florida during the Fall Semester 2022 as part of the course project.  No other use is authorized. 
 */
package edu.ufl.cise.plpfa22.ast;

import edu.ufl.cise.plpfa22.IToken;
import edu.ufl.cise.plpfa22.IToken.SourceLocation;
import edu.ufl.cise.plpfa22.PLPException;
import edu.ufl.cise.plpfa22.Token;

public abstract class ASTNode {
	
	public final IToken firstToken;
	
	public ASTNode(IToken firstToken) {
		this.firstToken = firstToken;
	}
	
	public SourceLocation getSourceLocation() {
		return firstToken.getSourceLocation();
	}

	@Override
	public String toString() {
		return "ASTNode [" + (firstToken != null ? "firstToken=" + firstToken : "") + "]";
	}


	public IToken getFirstToken() {
		return firstToken;
	}
	
	public abstract Object visit(ASTVisitor v, Object arg) throws  PLPException;

	
}	