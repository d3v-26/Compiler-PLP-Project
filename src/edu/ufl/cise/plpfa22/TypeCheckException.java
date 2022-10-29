/**  This code is provided for solely for use of students in the course COP5556 Programming Language Principles at the 
 * University of Florida during the Fall Semester 2022 as part of the course project.  No other use is authorized. 
 */

package edu.ufl.cise.plpfa22;

import edu.ufl.cise.plpfa22.IToken.SourceLocation;

@SuppressWarnings("serial")
public class TypeCheckException extends PLPException {

	public TypeCheckException() {
		// TODO Auto-generated constructor stub
	}

	public TypeCheckException(String error_message, int line, int column) {
		super(error_message, line, column);
	}

//	public TypeCheckException(String error_message, SourceLocation loc) {
//		super(error_message, loc);
//	}

	public TypeCheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TypeCheckException(String message, Throwable cause) {
		super(message, cause);
	}

	public TypeCheckException(String message) {
		super(message);
	}

	public TypeCheckException(Throwable cause) {
		super(cause);
	}

}
