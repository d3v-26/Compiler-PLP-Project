/**  This code is provided for solely for use of students in the course COP5556 Programming Language Principles at the 
 * University of Florida during the Fall Semester 2022 as part of the course project.  No other use is authorized. 
 */

package edu.ufl.cise.plpfa22.ast;

import java.util.List;

import edu.ufl.cise.plpfa22.IToken;
import edu.ufl.cise.plpfa22.PLPException;

public class Block extends ASTNode {
	

	public final List<ConstDec> constDecs;
	public final List<VarDec> varDecs;
	public final List<ProcDec> procedureDecs;
	public final Statement statement;

	public Block(IToken firstToken, List<ConstDec> c, List<VarDec> v,
			List<ProcDec> p, Statement statement) {
		super(firstToken);
		this.constDecs = c;
		this.varDecs = v;
		this.procedureDecs = p;
		this.statement = statement;
	}

	@Override
	public Object visit(ASTVisitor v, Object arg) throws PLPException {
		return v.visitBlock(this, arg);
	}

	@Override
	public String toString() {
		return "Block [" + (constDecs != null ? "constDecs=" + constDecs + ", " : "")
				+ (varDecs != null ? "varDecs=" + varDecs + ", " : "")
				+ (procedureDecs != null ? "procedureDecs=" + procedureDecs + ", " : "")
				+ (statement != null ? "statement=" + statement : "") + "]";
	}

	
}
