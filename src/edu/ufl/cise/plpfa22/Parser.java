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
	
	public boolean isKind(IToken t, Kind kind) {
		return t.getKind() == kind;
	}
	
	public void consume() throws LexicalException {
		this.lexer.next();
	}
	
	public IToken match(Kind kind) throws LexicalException, SyntaxException {
		IToken t = this.lexer.next();
		if(!isKind(t, kind)) {
			throw new SyntaxException("Expected Kind: "+kind+" Found: "+t.getKind());
		}
		return t;
	}
	
	public Object getValue(Expression e) throws SyntaxException {
		IToken firstToken = e.firstToken;
		if(isKind(firstToken, Kind.NUM_LIT)) {
			return firstToken.getIntValue();
		}
		else if(isKind(firstToken, Kind.BOOLEAN_LIT)) {
			return firstToken.getBooleanValue();
		}
		else if(isKind(firstToken, Kind.STRING_LIT)) {
			return firstToken.getStringValue();
		}
		else {
			throw new SyntaxException("Cannot evaluate Expression");
		}
	}
	
	public Program program() throws LexicalException, SyntaxException {
		IToken firstToken = this.lexer.peek();
		Block b = block();
		match(Kind.DOT);
		match(Kind.EOF);
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
		IToken firstToken = this.lexer.peek();
		Statement s;
		switch(firstToken.getKind()) {
			case IDENT -> {
				match(Kind.IDENT);
				Ident id = new Ident(firstToken);
				match(Kind.ASSIGN);
				Expression e = Expr();
				s = new StatementAssign(firstToken, id, e);
			}
			case KW_CALL ->{
				match(Kind.KW_CALL);
				IToken identToken = match(Kind.IDENT);
				Ident id = new Ident(identToken);
				s = new StatementCall(firstToken, id);
			}
			case QUESTION -> {
				match(Kind.QUESTION);
				IToken identToken = match(Kind.IDENT);
				Ident id = new Ident(identToken);
				s = new StatementInput(firstToken, id);
			}
			case BANG -> {
				match(Kind.BANG);
				Expression e = Expr();
				s = new StatementOutput(firstToken, e);
			}
			case KW_BEGIN ->{
				match(Kind.KW_BEGIN);
				List<Statement> stmt = new ArrayList<Statement>();
				Statement firstStatement = Stmt();
				
				stmt.add(firstStatement);			
				while(isKind(this.lexer.peek(), Kind.SEMI))
				{					
					consume();
					if(this.lexer.peek().getKind() == Kind.KW_END) {
						break;
					}
					Statement nextStatement = Stmt();
					stmt.add(nextStatement);
				}
				match(Kind.KW_END);
				s = new StatementBlock(firstToken, stmt);				
			}
			
			case KW_IF -> {
				match(Kind.KW_IF);
				Expression e = Expr();
				
				match(Kind.KW_THEN);
				Statement stmt = Stmt();
				s = new StatementIf(firstToken, e, stmt);
			}
			
			case KW_WHILE -> {
				match(Kind.KW_WHILE);
				Expression e = Expr();
				match(Kind.KW_DO);
				Statement stmt = Stmt();
				s = new StatementWhile(firstToken, e, stmt);
			}
			case SEMI, DOT, KW_END -> {
				s = new StatementEmpty(firstToken);
			}
			default -> {
				throw new SyntaxException("Invalid Statement");
			}
		}
		return s;
	}
	
	public List<ProcDec> ProcDecs() throws LexicalException, SyntaxException{
		// TODO Auto-generated method stub
		List<ProcDec> p = new ArrayList<ProcDec>();
		while(isKind(this.lexer.peek(), Kind.KW_PROCEDURE)) {
			IToken firstToken = this.lexer.next();
			IToken id = match(Kind.IDENT);			
			match(Kind.SEMI);
			Block b = block();
			match(Kind.SEMI);
			p.add(new ProcDec(firstToken, id, b));
		}
		return p;
	}

	public List<VarDec> VarDecs() throws LexicalException, SyntaxException {
		// TODO Auto-generated method stub
		List<VarDec> v = new ArrayList<VarDec>();
		IToken firstToken = this.lexer.peek();
		while(isKind(this.lexer.peek(), Kind.KW_VAR)) {
			firstToken = match(Kind.KW_VAR);
			IToken id = match(Kind.IDENT);
			v.add(new VarDec(firstToken, id));
			while(isKind(this.lexer.peek(), Kind.COMMA)) {
				consume();
				id = match(Kind.IDENT);
				v.add(new VarDec(firstToken, id));
			}
			match(Kind.SEMI);
		}		
		return v;
	}

	public List<ConstDec> ConstDecs() throws LexicalException, SyntaxException {
		// TODO Auto-generated method stub
		List<ConstDec> c = new ArrayList<ConstDec>();
		IToken firstToken = this.lexer.peek();
		while(isKind(this.lexer.peek(), Kind.KW_CONST)) {
			firstToken = match(Kind.KW_CONST);
			IToken id = match(Kind.IDENT);
			match(Kind.EQ);
			Expression const_val = ConstantValue();
			Object val = getValue(const_val);
			c.add(new ConstDec(firstToken, id, val));
			while(isKind(this.lexer.peek(), Kind.COMMA)) {
				consume();
				id = match(Kind.IDENT);
				match(Kind.EQ);
				const_val = ConstantValue();
				val = getValue(const_val);
				c.add(new ConstDec(firstToken, id, val));
			}
			match(Kind.SEMI);
		}
		return c;
	}
	
	public Boolean checkOperators1(Kind operatorToken) {		
		return operatorToken == Kind.GT || operatorToken == Kind.LT || operatorToken == Kind.EQ || operatorToken==Kind.NEQ || operatorToken==Kind.LE || operatorToken==Kind.GE;
	}
	
	public Boolean checkOperators2(Kind operatorToken) {
		return operatorToken == Kind.PLUS || operatorToken == Kind.MINUS;
	}
	
	public Boolean checkOperators3(Kind operatorToken) {
		return operatorToken == Kind.TIMES || operatorToken == Kind.DIV || operatorToken == Kind.MOD;
	}
	
	public Expression Expr() throws SyntaxException, LexicalException {
		// TODO Auto-generated method stub
		IToken firstToken = this.lexer.peek();
		Expression firstAddExpr = AdditiveExpr(); 
		Kind operatorToken = this.lexer.peek().getKind();
		while(checkOperators1(operatorToken)) {
			IToken operator = this.lexer.next();
			Expression secondAddExpr = AdditiveExpr();
			firstAddExpr = new ExpressionBinary(firstToken, firstAddExpr, operator, secondAddExpr);
			operatorToken = this.lexer.peek().getKind();
		}
		return firstAddExpr;
	}
	
	public Expression AdditiveExpr() throws SyntaxException, LexicalException {
		IToken firstToken = this.lexer.peek();
		Expression firstMulExpr = MultiplicativeExpr();
		Kind operatorToken = this.lexer.peek().getKind();
		while(checkOperators2(operatorToken)) {
			IToken operator = this.lexer.next();
			Expression secondMulExpr = MultiplicativeExpr();
			firstMulExpr = new ExpressionBinary(firstToken, firstMulExpr, operator, secondMulExpr);
			operatorToken = this.lexer.peek().getKind();
		}
		return firstMulExpr;
	}
	
	public Expression MultiplicativeExpr() throws SyntaxException, LexicalException {
		IToken firstToken = this.lexer.peek();
		Expression firstPriExpr = PrimaryExpr(); 
		Kind operatorToken = this.lexer.peek().getKind();
		while(checkOperators3(operatorToken)) {
			IToken operator = this.lexer.next();
			Expression secondPriExpr = PrimaryExpr();
			firstPriExpr = new ExpressionBinary(firstToken, firstPriExpr, operator, secondPriExpr);
			operatorToken = this.lexer.peek().getKind();
		}
		return firstPriExpr;
	}
	
	public Expression PrimaryExpr() throws SyntaxException, LexicalException {
		IToken firstToken = this.lexer.peek();
		if(isKind(firstToken, Kind.LPAREN)) {
			consume();
			Expression e = Expr();
			match(Kind.RPAREN);
			return e;
		}
		else if(isKind(firstToken, Kind.IDENT)) {
			IToken id = match(Kind.IDENT);
			return new ExpressionIdent(id);
		}
		else {
			return ConstantValue();
		}		
	}
	
	public Expression ConstantValue() throws SyntaxException, LexicalException {
		IToken firstToken = this.lexer.next();
		switch(firstToken.getKind()) {
			case NUM_LIT -> {
				return new ExpressionNumLit(firstToken);
			}
			case STRING_LIT -> {
				return new ExpressionStringLit(firstToken);
			}
			case BOOLEAN_LIT -> {
				return new ExpressionBooleanLit(firstToken);				
			}
			default -> throw new SyntaxException("Unexpected Token: " + firstToken.getKind());
		}
		
	}
	
}
