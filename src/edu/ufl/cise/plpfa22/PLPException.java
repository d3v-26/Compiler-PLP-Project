/**  This code is provided for solely for use of students in the course COP5556 Programming Language Principles at the 
 * University of Florida during the Fall Semester 2022 as part of the course project.  No other use is authorized. 
 */
package edu.ufl.cise.plpfa22;



@SuppressWarnings("serial")
public class PLPException extends Exception {
	
	public PLPException() {
		super();
	}

	public PLPException(String error_message, int line, int column) {
		super(line + ":" + column + "  " + error_message);
	}
	
	public PLPException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PLPException(String message, Throwable cause) {
		super(message, cause);
	}

	public PLPException(String message) {
		super(message);
	}

	public PLPException(Throwable cause) {
		super(cause);
	}
	


}
