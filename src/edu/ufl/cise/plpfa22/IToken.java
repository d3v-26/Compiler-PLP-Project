/**  This code is provided for solely for use of students in the course COP5556 Programming Language Principles at the 
 * University of Florida during the Fall Semester 2022 as part of the course project.  No other use is authorized. 
 */


package edu.ufl.cise.plpfa22;

public interface IToken {
	
	
	    /**
	     * Represents the location in the source code.  Lines and columns begin counting at 1.  
	     */
	    public record SourceLocation(int line, int column) {}  
	    
	    public static enum Kind {
	    	IDENT,
	    	NUM_LIT,
	    	STRING_LIT,
	    	BOOLEAN_LIT,   //TRUE, FALSE
	    	DOT,	// .    	
	    	COMMA,  // , 
	    	SEMI,   // ;
	    	QUOTE,  // "
	    	LPAREN, // (
	    	RPAREN, // )
	    	PLUS,   // +
	    	MINUS,  // -
	    	TIMES,  // *
	    	DIV,    // /
	    	MOD,    // %
	    	QUESTION,// ?
	    	BANG,    // !
	    	ASSIGN, // :=
	    	EQ,     // =
	    	NEQ,    // #
	    	LT,     // <
	    	LE,     // <=
	    	GT,     // >
	    	GE,     // >=
	    	KW_CONST,
	    	KW_VAR,
	    	KW_PROCEDURE,
	    	KW_CALL,
	    	KW_BEGIN,
	    	KW_END,
	    	KW_IF,
	    	KW_THEN,
	    	KW_WHILE,
	    	KW_DO,	    	
	    	EOF,  // used as a sential, does not correspond to input	    	
	        ERROR, // use to avoid exceptions if scanning all input at once
	}

	/**
	 * Returns the Kind of this IToken
	 * @return
	 */
	public Kind getKind();

	/**
	 * Returns a char array containing the characters from the source program that represent this IToken.  
	 * 
	 * Note that if the the IToken kind is a STRING_LIT, the characters are the raw characters from the source, including the delimiters and unprocessed 
	 * escape sequences.  
	 * 
	 * @return
	 */
	public char[] getText();

    /** 
     * Returns a SourceLocation record containing the line and position in the line of the first character in this IToken.
     * 
     * @return  
     */
	public SourceLocation getSourceLocation();
	
	/**
	 * Precondition:  getKind == NUM_LIT
	 * @returns int value represented by the characters in this IToken
	 */
	public int getIntValue();

	/**
	 * Precondition:  getKind == BOOLEAN_LIT
	 * @return boolean value represented by the characters in this IToken
	 */
	public boolean getBooleanValue();

	/**
	 * Precondition:  getKind == STRING_LIT
	 * @return String value represented by the characters in this IToken.  The returned String does not include the delimiters, and escape sequences have been handled.
	 */
	public String getStringValue();

}
