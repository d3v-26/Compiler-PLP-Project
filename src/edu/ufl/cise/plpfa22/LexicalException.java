/**  This code is provided for solely for use of students in the course COP5556 Programming Language Principles at the 
 * University of Florida during the Fall Semester 2022 as part of the course project.  No other use is authorized. 
 */

package edu.ufl.cise.plpfa22;

import edu.ufl.cise.plpfa22.IToken.SourceLocation;

@SuppressWarnings("serial")
public class LexicalException extends PLPException {

	public LexicalException() {
		super();
	}

	public LexicalException(String error_message, int line, int column) {
		super(error_message, line, column);
	}

	public LexicalException(String error_message, SourceLocation loc) {
		super(loc.line()+ ":" + loc.column() + " " + error_message);
	}

	public LexicalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LexicalException(String message, Throwable cause) {
		super(message, cause);
	}

	public LexicalException(String message) {
		super(message);
	}

	public LexicalException(Throwable cause) {
		super(cause);
	}

}
