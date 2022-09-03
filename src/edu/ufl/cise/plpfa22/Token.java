package edu.ufl.cise.plpfa22;

public class Token implements IToken {
	
	public Kind kind;
	public SourceLocation sourceLocation;
	public int intValue;
	public boolean booleanValue;
	public String stringValue;
	
	public Token(Kind kind, SourceLocation sourceLocation) {
		super();
		this.kind = kind;
		this.sourceLocation = sourceLocation;
	}
	
	public Token(Kind kind, SourceLocation sourceLocation, int intValue) {
		super();
		this.kind = kind;
		this.sourceLocation = sourceLocation;
		this.intValue = intValue;
	}	

	public Token(Kind kind, SourceLocation sourceLocation, boolean booleanValue) {
		super();
		this.kind = kind;
		this.sourceLocation = sourceLocation;
		this.booleanValue = booleanValue;
	}

	
	public Token(Kind kind, SourceLocation sourceLocation, String stringValue) {
		super();
		this.kind = kind;
		this.sourceLocation = sourceLocation;
		this.stringValue = stringValue;
	}


	@Override
	public Kind getKind() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SourceLocation getSourceLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getIntValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getBooleanValue() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getStringValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
