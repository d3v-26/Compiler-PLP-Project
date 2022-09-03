package edu.ufl.cise.plpfa22.Lexer;

public class Token implements IToken {
	
	public Kind kind;
	public char[] text;
	public int intValue;
	public String stringValue;
	public boolean booleanValue;
	public SourceLocation sourceLocation;
	
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
		// TODO Auto-generated method stub
		return this.kind;
	}

	@Override
	public char[] getText() {
		// TODO Auto-generated method stub
		return this.text;
	}

	@Override
	public SourceLocation getSourceLocation() {
		// TODO Auto-generated method stub
		return this.sourceLocation;
	}

	@Override
	public int getIntValue() {
		// TODO Auto-generated method stub
		if(this.kind != Kind.NUM_LIT) {
			return 0;
		}
		return this.intValue;
	}

	@Override
	public boolean getBooleanValue() {
		// TODO Auto-generated method stub
		if(this.kind != Kind.BOOLEAN_LIT) {
			return false;
		}
		return this.booleanValue;
	}

	@Override
	public String getStringValue() {
		// TODO Auto-generated method stub
		if(this.kind != Kind.STRING_LIT) {
			return new String();
		}
		return this.stringValue;
	}

}
