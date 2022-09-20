package edu.ufl.cise.plpfa22;

import edu.ufl.cise.plpfa22.IToken.SourceLocation;

@SuppressWarnings("serial")
public class SyntaxException extends PLPException {

	public SyntaxException() {
		super();
	}

	public SyntaxException(String error_message, int line, int column) {
		super(error_message, line, column);
	}

//	public SyntaxException(String error_message, SourceLocation loc) {
//		super(error_message, loc);
//	}

	public SyntaxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public SyntaxException(String message, Throwable cause) {
		super(message, cause);
	}

	public SyntaxException(String message) {
		super(message);
	}

	public SyntaxException(Throwable cause) {
		super(cause);
	}


}
