package edu.ufl.cise.plpfa22.Lexer;


/**
 * @author Dev's PC
 * TOKEN Class has following Constructors:
 * 	- Token(Kind):
 *  - Token(Kind, Char[], SourceLocation): 
 *  - Token(Kind, Char[], SourceLocation, int): 
 *  - Token(Kind, Char[], SourceLocation, boolean): 
 *  - Token(Kind, Char[], SourceLocation, String): 
 *  
 * TOKEN Class has following Methods:
 * 	- Kind getKind():
 *  - Char[] getText():
 *  - SourceLocation getSourceLocation():
 *  - int getIntValue(): 
 *  - boolean getBooleanValue():
 *  - String getStringValue(): 
 */

public class Token implements IToken {
	
	private Kind kind;
	private char[] text;
	private int intValue;
	private String stringValue;
	private boolean booleanValue;
	private SourceLocation sourceLocation;
	
	public Token(Kind kind) {
		super();
		this.kind = kind;
	}

	public Token(Kind kind, char[] text, SourceLocation sourceLocation) {
		super();
		this.kind = kind;
		this.text = text;
		this.sourceLocation = sourceLocation;
	}
	
	public Token(Kind kind, char[] text, SourceLocation sourceLocation, int intValue) {
		super();
		this.kind = kind;
		this.text = text;
		this.intValue = intValue;
		this.sourceLocation = sourceLocation;
	}	

	public Token(Kind kind, char[] text, SourceLocation sourceLocation, boolean booleanValue) {
		super();
		this.kind = kind;
		this.text = text;
		this.booleanValue = booleanValue;
		this.sourceLocation = sourceLocation;
	}

	
	public Token(Kind kind, char[] text, SourceLocation sourceLocation, String stringValue) {
		super();
		this.kind = kind;
		this.text = text;
		this.stringValue = stringValue;
		this.sourceLocation = sourceLocation;
	}


	@Override
	public Kind getKind() {
		// Accessed as: Token.getKind(), Returns: Kind of the Token, Return Type: Kind;
		return this.kind;
	}

	@Override
	public char[] getText() {
		// Accessed as: Token.getText(), Returns: Text of the Token, Return Type: Char[];
		return this.text;
	}

	@Override
	public SourceLocation getSourceLocation() {
		// Accessed as: Token.getSourceLocation(), Returns: Location of the Token, Return Type: SourceLocation;
		return this.sourceLocation;
	}

	@Override
	public int getIntValue() {
		// Accessed as: Token.getIntValue(), Returns: Integer value of the Token, Return Type: int;
		if(this.kind != Kind.NUM_LIT) {
			return 0;
		}
		return this.intValue;
	}

	@Override
	public boolean getBooleanValue() {
		// Accessed as: Token.getBooleanValue(), Returns: Boolean value of the Token, Return Type: boolean;
		if(this.kind != Kind.BOOLEAN_LIT) {
			return false;
		}
		return this.booleanValue;
	}

	@Override
	public String getStringValue() {
		// Accessed as: Token.getStringValue(), Returns: String value of the Token, Return Type: String;
		if(this.kind != Kind.STRING_LIT) {
			return new String();
		}
		return this.stringValue;
	}

}
