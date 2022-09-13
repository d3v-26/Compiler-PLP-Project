package edu.ufl.cise.plpfa22.Lexer;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Stream;

import edu.ufl.cise.plpfa22.Lexer.IToken.Kind;
import edu.ufl.cise.plpfa22.Lexer.IToken.SourceLocation;

/**
 * 
 * @author Dev's PC & Dharmam's PC
 * LEXER Class has following Constructors:
 * 	- Lexer(String)
 * 
 * LEXER Class has following methods:
 * 	- IToken next()
 *  - IToken peek()  
 *  - SourceLocation getSource()
 *  - IToken getToken()
 */


public class Lexer implements ILexer {

	public String input;
	public int pos, line;
	public List<String> lines;
	
	public enum States{
		START,
		HAVE_GT,
		HAVE_LT,
		HAVE_QUOTE,
		HAVE_QUOTE_SLASH,
		HAVE_UNQUOTE,
		HAVE_COLON,
		HAVE_EQUALS,
		GE, LE, NEQ,
		IDENT, KW, BOOL_IDENT
	}
	
	public Lexer(String input) {
		// TODO Auto-generated constructor `stub
		this.pos = 0;
		this.line = 0;
		this.input = input;
		this.lines = input.lines().toList();
	}

	@Override
	public IToken next() throws LexicalException {
		// TODO Auto-generated method stub
		if(this.input.length() == 0 || this.line >= this.lines.size()|| this.lines.get(line).length() == 0) {
			return new Token(Kind.EOF);
		}
		IToken token =  this.getToken();
		if(token == null) {
			this.handleNullToken();
			return this.next();
		}
		else {			
			this.pos += token.getText().length;			
			if(this.pos >= this.lines.get(line).length()) {
				this.pos = 0;
				this.line = this.line + 1;
			}
			else if((int)this.lines.get(line).charAt(this.pos) == 10 | (int)this.lines.get(line).charAt(this.pos) == 13 ) {
				this.pos = 0;
				this.line = this.line + 1;				
			}
			return token;
		}
	}

	@Override
	public IToken peek() throws LexicalException {
		// TODO Auto-generated method stub
		if(this.input.length() == 0 || this.line >= this.lines.size()|| this.lines.get(line).length() == 0) {
			return new Token(Kind.EOF);
		}
		IToken token =  this.getToken();
		if(token == null) {
			this.handleNullToken();
			return this.peek();
		}
		else {
			return token;
		}
	}

	public SourceLocation getSource(int line, int pos) {
		return new SourceLocation(line + 1, pos + 1);
	}

	public IToken getToken() throws LexicalException {
		int line = this.line;
		int startPos = this.pos;
		States currentState = States.START;
		if (this.line >= this.lines.size()) {
			return new Token(Kind.EOF);
		}
		String currLine = this.lines.get(this.line);
		char currentChar = currLine.charAt(this.pos);

		String[] bool_lit = {"TRUE", "FALSE"};
		String[] KW = {"CONST", "VAR", "PROCEDURE", "CALL", "BEGIN", "END", "IF", "THEN", "WHILE", "DO"};
		Kind[] tk = new Kind[] {Kind.KW_CONST, Kind.KW_VAR, Kind.KW_PROCEDURE, Kind.KW_CALL, Kind.KW_BEGIN, Kind.KW_END, Kind.KW_IF, Kind.KW_THEN, Kind.KW_WHILE, Kind.KW_DO};
		
		char[] identifiers = new char[64];
		
		int index = 0;
		for(char i = 'a'; i <= 'z'; i++) {
			identifiers[index] = i;
			index++;
		}
		for(char i = 'A'; i <= 'Z'; i++) {
			identifiers[index] = i;
			index++;
		}
		identifiers[index] = '_';
		identifiers[++index] = '$';
		index++;
		
		
		switch(currentChar) {
			case ' ', '\n', '\r', '\t' -> {
				return null;
			}
			case '+' -> {
				if(currentState == States.START) {
					char[] text = {'+'};
					return new Token(Kind.PLUS, text, this.getSource(line, pos));
				}
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
			}
			case '.' -> {
				if(currentState == States.START) {
					char[] text = {'.'};
					return new Token(Kind.DOT, text, this.getSource(line, pos));
				}
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
			}
			case ',' -> {
				if(currentState == States.START) {
					char[] text = {','};
					return new Token(Kind.COMMA, text, this.getSource(line, pos));
				}
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
			}
			case ';' -> {
				if(currentState == States.START) {
					char[] text = {';'};
					return new Token(Kind.SEMI, text, this.getSource(line, pos));
				}
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
			}
			case '(' -> {
				if(currentState == States.START) {
					char[] text = {'('};
					return new Token(Kind.LPAREN, text, this.getSource(line, pos));
				}
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
			}
			case ')' -> {
				if(currentState == States.START) {
					char[] text = {')'};
					return new Token(Kind.RPAREN, text, this.getSource(line, pos));
				}
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
			}
			case '-' -> {
				if(currentState == States.START) {
					char[] text = {'-'};
					return new Token(Kind.MINUS, text, this.getSource(line, pos));
				}
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
			}
			case '*' -> {
				if(currentState == States.START) {
					char[] text = {'*'};
					return new Token(Kind.TIMES, text, this.getSource(line, pos));
				}
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
			}
			case '/' -> {
				if(currentState == States.START) {
					
					if(currLine.charAt(startPos+1) == '/')
					{		
						return null;
					}						
					else {
						char[] text = {'/'};
						return new Token(Kind.DIV, text, this.getSource(line, pos));
					}						
				}
				
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
			}
			case '%' -> {
				if(currentState == States.START) {
					char[] text = {'%'};
					return new Token(Kind.MOD, text, this.getSource(line, pos));
				}
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
			}
			case '?' -> {
				if(currentState == States.START) {
					char[] text = {'?'};
					return new Token(Kind.QUESTION, text, this.getSource(line, pos));
				}
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
			}
			case '!' -> {
				if(currentState == States.START) {
					char[] text = {'!'};
					return new Token(Kind.BANG, text, this.getSource(line, pos));
				}
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
			}
			case '=' -> {
				if(currentState == States.START) {
					char[] text = {'='};
					return new Token(Kind.EQ, text, this.getSource(line, pos));
				}
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
			}
			case '#' -> {
				if(currentState == States.START) {
					char[] text = {'#'};
					return new Token(Kind.NEQ, text, this.getSource(line, pos));
				}
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
			}
			case '>' -> {
				if(currentState == States.START) {
					currentState = States.HAVE_GT;
					if (startPos+1 < currLine.length() && currLine.charAt(startPos+1) == '=') {
						char[] text = {'>','='};
						currentState = States.GE;
						return new Token(Kind.GE, text, this.getSource(line, pos));
					}
					else {
						char[] text = {'>'};
						return new Token(Kind.GT, text, this.getSource(line, pos));
					}
					
				}
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
			}
			case '<' -> {
				if(currentState == States.START) {
					currentState = States.HAVE_LT;
					if (startPos+1 < currLine.length() && currLine.charAt(startPos+1) == '=') {
						char[] text = {'<','='};
						currentState = States.LE;
						return new Token(Kind.LE, text, this.getSource(line, pos));
					}
					else {
						char[] text = {'<'};
						return new Token(Kind.LT, text, this.getSource(line, pos));
					}
					
				}
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
			}
			case ':' -> {
				if(currentState == States.START) {
					currentState = States.HAVE_COLON;
					if (startPos+1 < currLine.length() && currLine.charAt(startPos+1) == '=') {
						char[] text = {':','='};
						currentState = States.NEQ;
						return new Token(Kind.NEQ, text, this.getSource(line, pos));
					}
					else {
						String errMessage = "':' is followed by an illegal character";
						return new Token(Kind.ERROR, errMessage.toCharArray(), this.getSource(line, pos));
					}
					
				}
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
			}
			case 'a' -> {
				if(currentState == States.START) {
					currentChar = currLine.charAt(startPos);
					
					String ident = "";
					ident += currentChar;
					
					currentState = States.IDENT;
					
					for(char i = '0'; i <= '9'; i++) {
						identifiers[index] = i;
						index++;
					}
					
					String identifier = "";
					
					identifier += currentChar;
					while(startPos < currLine.length()) {
						boolean contains = false;
						startPos++;
						char nextChar = 0;
						if(startPos < currLine.length()) {							
							nextChar = currLine.charAt(startPos);
						}
						else {
							break;
						}
						for(char x : identifiers) {
							if(x == nextChar) {
								contains = true;
								break;
							}
						}
						if(contains) {
							identifier += nextChar;
							continue;
						}
						else {
							break;
						}
					}
					boolean isbool = Arrays.asList(bool_lit).contains(identifier);
					if(isbool) {
						boolean boolval = false;
						currentState = States.BOOL_IDENT;
						if(identifier.equals("TRUE")) {
							boolval = true;
						}
						return new Token(Kind.BOOLEAN_LIT, identifier.toCharArray(), this.getSource(line, pos), boolval);
						
					}
					List<String> kwlist = Arrays.asList(KW);
					boolean iskw = kwlist.contains(identifier);
					if(iskw) {
						currentState = States.KW;
						Kind k = tk[kwlist.indexOf(identifier)];
						return new Token(k, identifier.toCharArray(), this.getSource(line, pos));
					}
					
					return new Token(Kind.IDENT, identifier.toCharArray(), this.getSource(line, pos));
				}					
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
			}
		}
	}
}
