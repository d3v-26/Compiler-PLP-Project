package edu.ufl.cise.plpfa22;

import java.util.ArrayList;
import java.util.List;

import edu.ufl.cise.plpfa22.IToken.Kind;
import edu.ufl.cise.plpfa22.ast.ASTNode;
import edu.ufl.cise.plpfa22.ast.ASTVisitor;
import edu.ufl.cise.plpfa22.ast.Block;
import edu.ufl.cise.plpfa22.ast.ConstDec;
import edu.ufl.cise.plpfa22.ast.Expression;
import edu.ufl.cise.plpfa22.ast.Ident;
import edu.ufl.cise.plpfa22.ast.ProcDec;
import edu.ufl.cise.plpfa22.ast.Program;
import edu.ufl.cise.plpfa22.ast.Statement;
import edu.ufl.cise.plpfa22.ast.StatementAssign;
import edu.ufl.cise.plpfa22.ast.StatementEmpty;
import edu.ufl.cise.plpfa22.ast.VarDec;

public class Parser implements IParser {
	
	public ILexer lexer;
	
	public Parser(ILexer lexer) {
		super();
		this.lexer = lexer;		
	}
	
	@Override
	public ASTNode parse() throws PLPException {
		// TODO Auto-generated method stub
		
		// Program's First Token
		IToken firstToken = this.lexer.next();
		Statement s;
		// Switch on firstToken to match IDENT, CALL, ?, !, BEGIN, IF, WHILE, DOT
		switch(firstToken.getKind()) {
			case IDENT -> {
				Ident id = new Ident(firstToken);
				IToken nextToken = this.lexer.next();
				if(nextToken.getKind() != Kind.ASSIGN) {
					throw new SyntaxException();
				}
				else {
					Expression e = Expr();
					s = new StatementAssign(firstToken, id, e);
				}
			} 
			default -> {
				throw new SyntaxException();
			}
		}
		
		// Block's First Token
		IToken blockFirstToken = null;
		
		
		// CONSTs
		List<ConstDec> constants = new ArrayList<ConstDec>();
		
		// VARs
		List<VarDec> variables = new ArrayList<VarDec>();
		
		// PROCEDUREs
		List<ProcDec> procedures = new ArrayList<ProcDec>();
		
		// STATEMENTs we need specific call

		
		// Block, for that we need CONSTs, VARs, PROCEDUREs and STATEMENTs
		Block block = new Block(blockFirstToken, constants, variables, procedures, s);
		
		// We need to return AST of class Program
		Program program = new Program(firstToken, block);
		return program;
	}

	private Expression Expr() {
		// TODO Auto-generated method stub
		return null;
	}

}
