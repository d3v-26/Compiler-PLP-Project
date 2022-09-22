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
import edu.ufl.cise.plpfa22.ast.ExpressionIdent;
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
	
	public List<ProcDec> ProcDecs() throws LexicalException, SyntaxException{
		// TODO Auto-generated method stub
		List<ProcDec> p = new ArrayList<ProcDec>();
		
		while(this.lexer.peek().getKind() == Kind.KW_PROCEDURE) {
			IToken firstToken = this.lexer.next();
			IToken id = null;
			if(this.lexer.peek().getKind() == Kind.IDENT) {
				id = this.lexer.next();
			}
			// else error
			//check if ;
			this.lexer.next();
			Block b = block();
			this.lexer.next();
			p.add(new ProcDec(firstToken, id, b));
		}
		
		return p;
	}

	public List<VarDec> VarDecs() throws LexicalException, SyntaxException {
		// TODO Auto-generated method stub
		List<VarDec> v = new ArrayList<VarDec>();
		IToken firstToken = this.lexer.peek();
		while(firstToken.getKind() == Kind.KW_VAR) {
			this.lexer.next();
			// Check if identifier
			IToken id = this.lexer.next();
			v.add(new VarDec(firstToken, id));
			while(this.lexer.peek().getKind() == Kind.COMMA) {
				this.lexer.next();
				v.add(new VarDec(firstToken, this.lexer.next()));
			}
			// Check semicolon
			this.lexer.next();
		}
		
		return v;
	}

	public List<ConstDec> ConstDecs() throws LexicalException, SyntaxException {
		// TODO Auto-generated method stub
		List<ConstDec> c = new ArrayList<ConstDec>();
		IToken firstToken = this.lexer.peek();
		while(firstToken.getKind() == Kind.KW_CONST) {
			this.lexer.next();
			// Check if identifier
			IToken id = this.lexer.next();
			this.lexer.next();
			Expression const_val = ConstantValue();
			c.add(new ConstDec(firstToken, id, const_val));
			while(this.lexer.peek().getKind() == Kind.COMMA) {
				this.lexer.next();
				id = this.lexer.next();
				this.lexer.next();
				const_val = ConstantValue();
				c.add(new ConstDec(firstToken, id, const_val));
			}
			// Check semicolon
			this.lexer.next();
		}
		
		return c;
	}
	
	public Boolean checkOperators1(Kind operatorToken) {
		Boolean CheckResult = False;
		if(operatorToken == Kind.GT || operatorToken == Kind.LT || operatorToken == Kind.EQ || operatorToken==Kind.NEQ || operatorToken==Kind.LE || operatorToken==Kind.GE) {
				CheckResult= true; 
		}
		return CheckResult;
	}
	
	public Boolean checkOperators2(Kind operatorToken) {
		Boolean CheckResult = False;
		if(operatorToken == Kind.PLUS || operatorToken == Kind.MINUS) {
				CheckResult= true; 
		}
		return CheckResult;
	}
	
	public Boolean checkOperators3(Kind operatorToken) {
		Boolean CheckResult = False;
		if(operatorToken == Kind.TIMES || operatorToken == Kind.DIV || operatorToken == Kind.MOD) {
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
		while(checkOperators1(operatorToken)) {
			//List<Expression> expressionArray = new ArrayList<Expression>();
			IToken operator = this.lexer.next();
			Expression SecondAddExpr = AdditiveExpr();
			FirstAddExpr = new ExpressionBinary(FirstToken, FirstAddExpr, operator, SecondAddExpr);
			operatorToken = this.lexer.peek().getKind();
		}
		return FirstAddExpr;
	}
	
	public Expression AdditiveExpr() throws SyntaxException, LexicalException {
		IToken FirstToken = this.lexer.next();
		Expression FirstMulExpr = MultiplicativeExpr();
		Kind operatorToken = this.lexer.peek().getKind();
		while(checkOperators2(operatorToken)) {
			//List<Expression> expressionArray = new ArrayList<Expression>();
			IToken operator = this.lexer.next();
			Expression SecondMulExpr = MultiplicativeExpr();
			FirstMulExpr = new ExpressionBinary(FirstToken, FirstMulExpr, operator, SecondMulExpr);
			operatorToken = this.lexer.peek().getKind();
		}
		return FirstMulExpr;
	}
	
	public Expression MultiplicativeExpr() throws SyntaxException, LexicalException {
		IToken FirstToken = this.lexer.next();
		Expression FirstPriExpr = PrimaryExpr(); 
		Kind operatorToken = this.lexer.peek().getKind();
		while(checkOperators3(operatorToken)) {
			IToken operator = this.lexer.next();
			Expression SecondPriExpr = PrimaryExpr();
			FirstPriExpr = new ExpressionBinary(FirstToken, FirstPriExpr, operator, SecondPriExpr);
			operatorToken = this.lexer.peek().getKind();
		}
		return FirstPriExpr;
	}
	
	public Expression PrimaryExpr() throws SyntaxException, LexicalException {
		IToken firstToken = this.lexer.peek();
		if(firstToken.getKind() == Kind.LPAREN) {
			this.lexer.next();
			Expression e = Expr();
			// Check for RParen
			this.lexer.next();
			return e;
		}
		else if(firstToken.getKind() == Kind.IDENT) {
			return new ExpressionIdent(this.lexer.next());
		}
		else if(firstToken.getKind() == Kind.NUM_LIT || firstToken.getKind() == Kind.BOOLEAN_LIT || firstToken.getKind() == Kind.STRING_LIT) {
			return ConstantValue();
		}
		else {
			throw new SyntaxException();
		}
	}
	
	public Expression ConstantValue() throws SyntaxException, LexicalException {
		//Expression e = Expr();
		IToken FirstToken = this.lexer.next();
		switch(FirstToken.getKind()) {
			case NUM_LIT -> {
				ExpressionNumLit expnum = new ExpressionNumLit(FirstToken);
				return expnum;
			}
			case STRING_LIT -> {
				ExpressionStringLit expnum = new ExpressionStringLit(FirstToken);
				return expnum;
			}
			case BOOLEAN_LIT -> {
				ExpressionBooleanLit expnum = new ExpressionBooleanLit(FirstToken);
				return expnum;
			}
			default -> throw new SyntaxException("Unexpected value: " + FirstToken.getKind());
		}
		
	}
	
}
