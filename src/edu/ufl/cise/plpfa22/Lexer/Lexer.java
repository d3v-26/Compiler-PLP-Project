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
		return null;
	}

	@Override
	public IToken peek() throws LexicalException {
		// TODO Auto-generated method stub
		if(this.input.length() == 0) {
			return new Token(Kind.EOF);
		}
		return this.getToken();
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
		}
	}
}
