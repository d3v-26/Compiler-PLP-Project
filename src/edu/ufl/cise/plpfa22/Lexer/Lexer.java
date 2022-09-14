package edu.ufl.cise.plpfa22.Lexer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import edu.ufl.cise.plpfa22.Lexer.IToken.Kind;
import edu.ufl.cise.plpfa22.Lexer.IToken.SourceLocation;

/**
 * 
 * @author Dev's PC, Dharmam's PC
 * LEXER Class has following Constructors:
 * 	- Lexer(String)
 * 
 * LEXER Class has following methods:
 * 	- IToken next()
 *  - IToken peek()
 *  - IToken getToken()
 *  - void handleNullToken()
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
		// Lexer Constructor
		this.pos = 0;
		this.line = 0;
		this.input = input;
		this.lines = input.lines().toList();
	}
	
	
	
	@Override
	public IToken next() throws LexicalException {
		// This Method returns the next token based on input and increments the internal pointer.
		
		// First Check if the input is empty or Current Position of the internal pointer is empty, if so then return EOF token
		if(this.input.length() == 0 || this.line >= this.lines.size()|| this.lines.get(line).length() == 0) {
			return new Token(Kind.EOF);
		}
		
		//Calling getToken() to fetch the next token
		IToken token =  this.getToken();
		
		//In our code, null token is returned for handling of whitespaces and comments
		if(token == null) {
			this.handleNullToken();
			
			// Calling the method again as we need a next token after handling null token.
			return this.next();
		}
		else {
			
			// Now increment the internal pointer by token's char[] length
			// If we encounter new line, increment the line pointer.
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
		// This Method returns the next token based on input and does not increment the internal pointer.
		
		// First Check if the input is empty or Current Position of the internal pointer is empty, if so then return EOF token
		if(this.input.length() == 0 || this.line >= this.lines.size()|| this.lines.get(line).length() == 0) {
			return new Token(Kind.EOF);
		}
		
		//Calling getToken() to fetch the next token
		IToken token =  this.getToken();
		
		//In our code, null token is returned for handling of whitespaces and comments
		if(token == null) {
			this.handleNullToken();
			
			// Calling the method again as we need a next token after handling null token.
			return this.peek();
		}
		else {
			return token;
		}
	}
	
	public SourceLocation getSource(int line, int pos) {
		// This method instantiates a SourceLocation record object based on the variables line and pos.
		
		return new SourceLocation(line + 1, pos + 1);
	}
	
	public void handleNullToken() {
		// This method handles null tokens 
		// if this method encounters comments or whitespaces then it increments internal pointer, essentially skipping over them.
		
		// Fetch current line and current character in that line
		String line = this.lines.get(this.line);
		char currChar = line.charAt(this.pos);
		
		// While current char is white space increment internal pointer.
		while(currChar == ' ' | currChar == '\n' | currChar == '\t' | currChar == '\r') {
			this.pos += 1;
			if(this.pos > line.length()) {
				this.line = this.line + 1;
				this.pos = 0;
			}
			currChar = line.charAt(this.pos);
		}
		
		// If current line is a comment, skip it.
		if(currChar == '/' & line.charAt(this.pos+1) == '/') {
			this.line = this.line + 1;
			this.pos = 0;
		}
	}
	
	public IToken getToken() throws LexicalException {
		// This method consists of core logic behind lexer as it generates new tokens on demand per each call.
		
		int line = this.line;
		int startPos = this.pos;
		
		// Set current state to START
		States currentState = States.START;
		
		// If the current line pointer is out of scope for input, return EOF
		if (this.line >= this.lines.size()) {
			return new Token(Kind.EOF);
		}
		
		// Fetch current line and current character
		String currLine = this.lines.get(this.line);
		char currentChar = currLine.charAt(this.pos);

		// Initialized Boolean literals, Keywords, and identifers array
		String[] bool_lit = {"TRUE", "FALSE"};
		String[] KW = {"CONST", "VAR", "PROCEDURE", "CALL", "BEGIN", "END", "IF", "THEN", "WHILE", "DO"};
		Kind[] tk = new Kind[] {Kind.KW_CONST, Kind.KW_VAR, Kind.KW_PROCEDURE, Kind.KW_CALL, Kind.KW_BEGIN, Kind.KW_END, Kind.KW_IF, Kind.KW_THEN, Kind.KW_WHILE, Kind.KW_DO};
		
		// This array will consist of all characters from 'a',..,'z','A',..,'Z','_','$','0',..,'9'
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
		
		// If current char is a character form identifiers array change currentChar variable to 'a', so that we can identify what case to switch to.
		for(int i = 0; i < 54; i++) {
			if(currentChar == identifiers[i]) {				
				currentChar = 'a';
				break;
			}
		}
		
		// Switch based on current Char
		switch(currentChar) {
			case ' ', '\n', '\r', '\t' -> {
				// return null if whitespaces are encoutered.
				return null;
			}
			
			// Single Char operators are self explanatory.
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
				// For this case it could match both > and >=, so we'll check if next character is '='
				// If such is the case then return GE token else GT token.
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
				// Just as above this case could match both < and <=
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
				// We accept := but not only :, so we'll check if there is a next character that is '=', if not we'll throw an error.
				if(currentState == States.START) {
					currentState = States.HAVE_COLON;
					if (startPos+1 < currLine.length() && currLine.charAt(startPos+1) == '=') {
						char[] text = {':','='};
						currentState = States.NEQ;
						return new Token(Kind.NEQ, text, this.getSource(line, pos));
					}
					else {
						String errMessage = "':' is followed by an illegal character";
						throw new LexicalException(errMessage, this.getSource(line, pos));
					}
					
				}
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
			}
			case 'a' -> {
				// This case matches all identifiers
				// While in this case we'll match characters that are in identifiers array.
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
					
					// While current character if from identifiers array, we'll store it as identifier string.
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
					
					// Check if the identifier is a boolean literal or not.
					boolean isbool = Arrays.asList(bool_lit).contains(identifier);
					if(isbool) {
						boolean boolval = false;
						currentState = States.BOOL_IDENT;
						if(identifier.equals("TRUE")) {
							boolval = true;
						}
						return new Token(Kind.BOOLEAN_LIT, identifier.toCharArray(), this.getSource(line, pos), boolval);
						
					}
					
					// Check if the identifier is a Keyword or not.
					List<String> kwlist = Arrays.asList(KW);
					boolean iskw = kwlist.contains(identifier);
					if(iskw) {
						currentState = States.KW;
						Kind k = tk[kwlist.indexOf(identifier)];
						return new Token(k, identifier.toCharArray(), this.getSource(line, pos));
					}
					
					// Else return the identifier.
					return new Token(Kind.IDENT, identifier.toCharArray(), this.getSource(line, pos));
				}					
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
			}
			
			case '0' ->{				
				// This case is self explanatory
				if (currentState == States.START) {
					char[] text = {'0'};
					return new Token(Kind.NUM_LIT, text, this.getSource(line, pos), 0);
				}
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
				
			}
			
			case '1','2','3','4','5','6','7','8','9' -> {
				// This case matches the integer literals
				
				if (currentState == States.START) {
					String number = "";
					number += currentChar;
					char[] nums = {'0','1','2','3','4','5','6','7','8','9'};
					
					// while current char is in nums array store it in the numbers string
					while(startPos < currLine.length()) {
						boolean contains = false;
						char nextChar = 0;
						startPos++;
						if(startPos < currLine.length()) {							
							nextChar = currLine.charAt(startPos);
						}
						else {
							break;
						}
						for(char x : nums) {
							if(x == nextChar) {
								contains = true;
								break;
							}
						}
						if(contains) {
							number += nextChar;
							continue;
						}
						else {
							break;
						}
					}
					int num = 0;
					try {						
						// We have the number as a string, we'll try to parse it.
						// Failure in parsing may be caused by the number being too big or it being an invalid number 
						// Incase of failure to parse we'll throw an Exception
						num = Integer.parseInt(number);						
					}
					catch(NumberFormatException e) {
						throw new LexicalException("Unable to Parse String", this.getSource(line, pos));
					}
					return new Token(Kind.NUM_LIT, number.toCharArray(), this.getSource(line, pos), num);
				}
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
				
			}
			
			case '"' -> {
				
				// This case matches the string literals
				int s = startPos;
				char[] c = {'b','t','n','f','r','"','\'','\\'};
				char[] cc = {'\b','\t', '\n', '\f', '\r', '\"', '\'','\\'};
				if(currentState == States.START) {
					currentState = States.HAVE_QUOTE;
					String str = "";
					str += '"';
					
					// While we don't encounter another '"' fetch next character
					// Along the way we'll check for all characters in cc array
					while(startPos < currLine.length()) {
						++startPos;
						char nextChar = 0; 
						if(startPos < currLine.length()) {							
							nextChar = currLine.charAt(startPos);
						}
						else {
							break;
						}
						
						// We have used states to check if the string literal is acceptable or not
						if(nextChar == '"' & currentState == States.HAVE_QUOTE ) {
							str += '"';
							currentState = States.HAVE_UNQUOTE;
							break;
						}
						else if(nextChar == '\\' & currentState == States.HAVE_QUOTE) {
							currentState = States.HAVE_QUOTE_SLASH;							
						}
						else if(currentState == States.HAVE_QUOTE_SLASH) {
							boolean contains = false;
							int inx = -1;
							for(int i = 0; i<c.length; i++) {
								char x = c[i];
								if(x == nextChar) {
									contains = true;
									inx = i;
									break;
								}
							}
							if(!contains) {
								throw new LexicalException("Invalid Character followed by \\", this.getSource(line, pos));
							}
							else {								
								str+=cc[inx];
								currentState = States.HAVE_QUOTE;
							}
						}
						else {							
							str += nextChar;
						}
					}
					
					// Return the string token.
					String val = str.substring(1,str.length()-1);
					String text = currLine.substring(s, startPos+1);
					if(currentState == States.HAVE_UNQUOTE) {						
						return new Token(Kind.STRING_LIT, text.toCharArray(), this.getSource(line, pos), val);
					}
					else {
						throw new LexicalException("Expected Unquote", this.getSource(line, pos));
					}
				}
				else {
					throw new LexicalException("Invalid Character for Current State", this.getSource(line, pos));
				}
			}
			
			default -> {
				throw new LexicalException("Invalid Character", this.getSource(line, pos));
			}
			
		  
		}
	}

}
