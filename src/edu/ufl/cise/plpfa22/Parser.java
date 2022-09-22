package edu.ufl.cise.plpfa22;

import java.util.ArrayList;
import java.util.List;

import edu.ufl.cise.plpfa22.IToken.Kind;
import edu.ufl.cise.plpfa22.ast.ASTNode;
import edu.ufl.cise.plpfa22.ast.ASTVisitor;
import edu.ufl.cise.plpfa22.ast.Block;
import edu.ufl.cise.plpfa22.ast.ConstDec;
import edu.ufl.cise.plpfa22.ast.Expression;
import edu.ufl.cise.plpfa22.ast.ExpressionBinary;
import edu.ufl.cise.plpfa22.ast.ExpressionBooleanLit;
import edu.ufl.cise.plpfa22.ast.ExpressionNumLit;
import edu.ufl.cise.plpfa22.ast.ExpressionStringLit;
import edu.ufl.cise.plpfa22.ast.Ident;
import edu.ufl.cise.plpfa22.ast.ProcDec;
import edu.ufl.cise.plpfa22.ast.Program;
import edu.ufl.cise.plpfa22.ast.Statement;
import edu.ufl.cise.plpfa22.ast.StatementAssign;
import edu.ufl.cise.plpfa22.ast.StatementBlock;
import edu.ufl.cise.plpfa22.ast.StatementCall;
import edu.ufl.cise.plpfa22.ast.StatementEmpty;
import edu.ufl.cise.plpfa22.ast.StatementIf;
import edu.ufl.cise.plpfa22.ast.StatementInput;
import edu.ufl.cise.plpfa22.ast.StatementOutput;
import edu.ufl.cise.plpfa22.ast.StatementWhile;
import edu.ufl.cise.plpfa22.ast.VarDec;

public class Parser implements IParser {
	
	private static final Kind KW_END = null;
	private static final Boolean False = null;
	public ILexer lexer;
	
	public Parser(ILexer lexer) {
		super();
		this.lexer = lexer;		
	}
	
	@Override
	public ASTNode parse() throws PLPException {
		// TODO Auto-generated method stub
		
		return program();
	}
	
	public Program program() throws LexicalException, SyntaxException {
		IToken firstToken = this.lexer.peek();
		Block b = block();
		IToken t = this.lexer.next();
		if(t.getKind() != Kind.EOF || t.getKind() != Kind.DOT) {
			throw new SyntaxException();
		}
		return new Program(firstToken, b);
	}
	
	public Block block() throws LexicalException, SyntaxException {
		IToken firstToken = this.lexer.peek();
		List<ConstDec> c = ConstDecs();
		List<VarDec> v = VarDecs();
		List<ProcDec> p = ProcDecs();
		Statement s = Stmt();
		return new Block(firstToken, c, v, p, s);
	}
	
	public Statement Stmt() throws LexicalException, SyntaxException {
		// TODO Auto-generated method stub
		IToken firstToken = this.lexer.next();
		Statement s;
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
			case KW_CALL ->{
				IToken nextToken = this.lexer.next();
				// Error thrown if kind of next token is not IDENT
				Ident id = new Ident(nextToken);
				s = new StatementCall(firstToken, id);
			}
			case QUESTION -> {
				IToken nextToken = this.lexer.next();
				Ident id = new Ident(nextToken);
				s = new StatementInput(firstToken, id);
			}
			case BANG -> {
				IToken nextToken = this.lexer.next();
				Ident id = new Ident(nextToken);
				Expression e = Expr();
				s = new StatementOutput(firstToken, e);
			}
			case KW_BEGIN ->{
				IToken nextToken = this.lexer.next();
				Ident id = new Ident(nextToken);
				
				List<Statement> stmt = new ArrayList<Statement>();
				while(this.lexer.peek().getStringValue() == ";")
				{
					this.lexer.next();
					stmt.add(Stmt());
				}
				if(this.lexer.peek().getKind()==KW_END){
					s = new StatementBlock(firstToken, stmt);
					this.lexer.next();
				}
				else {
					throw new SyntaxException("'END' keyword expected");
				}
			}
			
			case KW_IF -> {
				Expression e = Expr();
				//s = new StatementAssign(nextToken, id, e);
				if(this.lexer.peek().getKind()==Kind.KW_THEN) {
					this.lexer.next();
					Statement stmt = Stmt();
					s = new StatementIf(this.lexer.next(), e, stmt);
				}
				else {
					throw new SyntaxException("'THEN' keyword expected");
				}
			}
			
			case KW_WHILE -> {
				Expression e = Expr();
				if(this.lexer.peek().getKind()==Kind.KW_DO) {
					//statement_while
					this.lexer.next();
					Statement stmt = Stmt();
					s = new StatementWhile(this.lexer.next(), e, stmt);
				}
				else {
					throw new SyntaxException("'DO' keyword expected");
				}
			}
			//handle 'null case' left to be implemented
			case DOT -> {
				s = new StatementEmpty(firstToken);
			}
			default -> {
				throw new SyntaxException();
			}
		}
		return s;
	}
	
	
	
	private List<ProcDec> ProcDecs() {
		// TODO Auto-generated method stub
		return null;
	}

	private List<VarDec> VarDecs() {
		// TODO Auto-generated method stub
		return null;
	}

	private List<ConstDec> ConstDecs() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Boolean checkOperators(Kind operatorToken) {
		Boolean CheckResult = False;
		if(operatorToken == Kind.GT || operatorToken == Kind.LT || operatorToken == Kind.EQ || operatorToken==Kind.NEQ || operatorToken==Kind.LE || operatorToken==Kind.GE) {
				CheckResult= true; 
		}
		return CheckResult;
	}
	
	public Expression Expr() throws SyntaxException, LexicalException {
		// TODO Auto-generated method stub
		//IToken firstToken = this.lexer.next();
		IToken FirstToken = this.lexer.next();
		Expression FirstAddExpr = AdditiveExpr(); 
		Kind operatorToken = this.lexer.peek().getKind();
		while(checkOperators(operatorToken)) {
			//List<Expression> expressionArray = new ArrayList<Expression>();
			IToken operator = this.lexer.next();
			Expression SecondAddExpr = AdditiveExpr();
			FirstAddExpr = new ExpressionBinary(FirstToken,FirstAddExpr,operator, SecondAddExpr);
			
		}
		return FirstAddExpr;
	}
	
	public Expression AdditiveExpr() throws SyntaxException, LexicalException {
		IToken FirstToken = this.lexer.next();
		Expression FirstAddExpr = MultiplicationExpr();
		Kind operatorToken = this.lexer.peek().getKind();
		return null;
	}
	
	public Boolean checkOperators_multi(Kind operatorToken) {
		Boolean CheckResult = False;
		if(operatorToken == Kind.P || operatorToken == Kind.minus || operatorToken == Kind.EQ || operatorToken==Kind.NEQ || operatorToken==Kind.LE || operatorToken==Kind.GE) {
				CheckResult= true; 
		}
		return CheckResult;
	}
	
	public Expression MultiplicationExpr() throws SyntaxException, LexicalException {
		IToken FirstToken = this.lexer.next();
		Expression FirstAddExpr = PrimaryExpr(); 
		Kind operatorToken = this.lexer.peek().getKind();
		while(checkOperators_multi(operatorToken)) {
			IToken operator = this.lexer.next();
			Expression SecondAddExpr = AdditiveExpr();
			FirstAddExpr = new ExpressionBinary(FirstToken,FirstAddExpr,operator, SecondAddExpr);
		}
		return FirstAddExpr;
	}
	
	public Expression PrimaryExpr() throws SyntaxException, LexicalException {
		Expression e = Expr();
		switch(e.getType()) {
//			case identifier -> {
//				Ident id = new Ident(firstToken);
//				IToken nextToken = this.lexer.next();
//			}
//			case const_var -> {
//
//				const_car();
//			}
//			case expression -> {
//				
//			}
//			default -> throw new IllegalArgumentException("Unexpected value: " + firstToken.getKind());
		}
		return null;
	}
	
	public Expression Constant_Value() throws SyntaxException, LexicalException {
		//Expression e = Expr();
		IToken FirstToken = this.lexer.next();
		switch(FirstToken.getKind()) {
			case NUM_LIT -> {
				ExpressionNumLit expnum = new ExpressionNumLit(FirstToken);
			}
			case STRING_LIT -> {
				ExpressionStringLit expnum = new ExpressionStringLit(FirstToken);
			}
			case BOOLEAN_LIT -> {
				ExpressionBooleanLit expnum = new ExpressionBooleanLit(FirstToken);
			}
			default -> throw new IllegalArgumentException("Unexpected value: " + FirstToken.getKind());
		}
		return null;
	}
	
}
