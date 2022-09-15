/**  This code is provided for solely for use of students in the course COP5556 Programming Language Principles at the 
 * University of Florida during the Fall Semester 2022 as part of the course project.  No other use is authorized. 
 */

package edu.ufl.cise.plpfa22.Lexer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import edu.ufl.cise.plpfa22.CompilerComponentFactory;
import edu.ufl.cise.plpfa22.Lexer.IToken.Kind;

class LexerTest {

	/*** Useful functions ***/
	ILexer getLexer(String input) {
		return CompilerComponentFactory.getLexer(input);
	}

	// makes it easy to turn output on and off (and less typing than
	// System.out.println)
	static final boolean VERBOSE = true;

	void show(Object obj) {
		if (VERBOSE) {
			System.out.println(obj);
		}
	}

	// check that this token has the expected text.
	void checkText(IToken t, String expectedText) {
		assertArrayEquals(expectedText.toCharArray(), t.getText());
	}

	// check that this token has the expected location.
	void checkLocation(IToken t, int expectedLine, int expectedColumn) {
		assertEquals(new IToken.SourceLocation(expectedLine, expectedColumn), t.getSourceLocation());
	}

	// check that this token has the expected kind
	void checkToken(IToken t, Kind expectedKind) {
		assertEquals(expectedKind, t.getKind());
	}

	// check that the token has the expected kind and position
	void checkToken(IToken t, Kind expectedKind, int expectedLine, int expectedColumn) {
		assertEquals(expectedKind, t.getKind());
		this.checkLocation(t, expectedLine, expectedColumn);
	}

	// check if the token has the expected kind, location, and text.
	void checkToken(IToken t, Kind expectedKind, int expectedLine, int expectedColumn, String expectedText) {
		assertEquals(expectedKind, t.getKind());
		this.checkLocation(t, expectedLine, expectedColumn);
		assertArrayEquals(expectedText.toCharArray(), t.getText());
	}

	// check that this token is an IDENT and has the expected name
	void checkIdent(IToken t, String expectedName) {
		assertEquals(Kind.IDENT, t.getKind());
		assertEquals(expectedName, String.valueOf(t.getText()));
	}

	// check that this token is an IDENT, has the expected name, and has the
	// expected position
	void checkIdent(IToken t, String expectedName, int expectedLine, int expectedColumn) {
		checkIdent(t, expectedName);
		this.checkLocation(t, expectedLine, expectedColumn);
	}

	// check that this token is a boolean_lit, has the expected name and position
	void checkBoolean(IToken t, boolean expectedBooleanValue, int expectedLine, int expectedColumn) {
		assertEquals(Kind.BOOLEAN_LIT, t.getKind());
		if (expectedBooleanValue) {
			this.checkText(t, "TRUE");
		} else {
			this.checkText(t, "FALSE");
		}
		assertEquals(expectedBooleanValue, t.getBooleanValue());
		this.checkLocation(t, expectedLine, expectedColumn);
	}

	// check that this token is an NUM_LIT with expected int value
	void checkInt(IToken t, int expectedValue) {
		assertEquals(Kind.NUM_LIT, t.getKind());
		assertEquals(expectedValue, t.getIntValue());
	}

	// check that this token is an NUM_LIT with expected int value and position
	void checkInt(IToken t, int expectedValue, int expectedLine, int expectedColumn) {
		checkInt(t, expectedValue);
		this.checkLocation(t, expectedLine, expectedColumn);
	}

	void checkStringValue(IToken t, String expectedString) {
		assertEquals(expectedString, t.getStringValue());
	}

	// check that this token is a string and has expected value and position
	void checkString(IToken t, String expectText, String expectStringValue, int expectedLine, int expectedColumn) {
		this.checkToken(t, Kind.STRING_LIT);
		this.checkText(t, expectText);
		this.checkStringValue(t, expectStringValue);
		this.checkLocation(t, expectedLine, expectedColumn);
	}

	// check that this token is the EOF token
	void checkEOF(IToken t) {
		checkToken(t, Kind.EOF);
	}
	
	void checkBool(IToken t, boolean expectedValue) {
		assertEquals(Kind.BOOLEAN_LIT, t.getKind());
		assertEquals(expectedValue, t.getBooleanValue());	
	}
	
	void checkBool(IToken t, boolean expectedValue, int expectedLine, int expectedColumn) {
		checkBool(t,expectedValue);
		assertEquals(new IToken.SourceLocation(expectedLine,expectedColumn), t.getSourceLocation());		
	}
	
	void checkString(IToken t, String expectedValue, int expectedLine, int expectedColumn) {
        assertEquals(Kind.STRING_LIT, t.getKind());
        assertEquals(expectedValue, t.getStringValue());
        assertEquals(new IToken.SourceLocation(expectedLine,expectedColumn), t.getSourceLocation());
    }
	
	/*** Tests ****/

	// The lexer should add an EOF token to the end.
	@Test
	void testEmpty() throws LexicalException {
		String input = "";
		show(input);
		ILexer lexer = getLexer(input);
		show(lexer);
		checkEOF(lexer.next());
	}

	// A couple of single character tokens
	@Test
	void testSingleChar0() throws LexicalException {
		String input = """
				+
				-
				""";
		show(input);
		ILexer lexer = getLexer(input);
		checkToken(lexer.next(), Kind.PLUS, 1, 1, "+");
		checkToken(lexer.next(), Kind.MINUS, 2, 1, "-");
		checkEOF(lexer.next());
	}

	@Test
	void testSingleChar1() throws LexicalException {
		String input = """
				+- *
				;;;
				(() )  #/ /
				""";
		show(input);
		ILexer lexer = getLexer(input);
		checkToken(lexer.next(), Kind.PLUS, 1, 1, "+");
		checkToken(lexer.next(), Kind.MINUS, 1, 2, "-");
		checkToken(lexer.next(), Kind.TIMES, 1, 4, "*");
		checkToken(lexer.next(), Kind.SEMI, 2, 1, ";");
		checkToken(lexer.next(), Kind.SEMI, 2, 2, ";");
		checkToken(lexer.next(), Kind.SEMI, 2, 3, ";");
		checkToken(lexer.next(), Kind.LPAREN, 3, 1, "(");
		checkToken(lexer.next(), Kind.LPAREN, 3, 2, "(");
		checkToken(lexer.next(), Kind.RPAREN, 3, 3, ")");
		checkToken(lexer.next(), Kind.RPAREN, 3, 5, ")");
		checkToken(lexer.next(), Kind.NEQ, 3, 8, "#");
		checkToken(lexer.next(), Kind.DIV, 3, 9, "/");
		checkToken(lexer.next(), Kind.DIV, 3, 11, "/");

		checkEOF(lexer.next());
	}

	@Test
	void testSingleChar2() throws LexicalException {
		String input = """
				.,;()+-*/%?!:==#<<=>>=
				""";
		show(input);
		ILexer lexer = getLexer(input);
		checkToken(lexer.next(), Kind.DOT, 1, 1, ".");
		checkToken(lexer.next(), Kind.COMMA, 1, 2, ",");
		checkToken(lexer.next(), Kind.SEMI, 1, 3, ";");
		checkToken(lexer.next(), Kind.LPAREN, 1, 4, "(");
		checkToken(lexer.next(), Kind.RPAREN, 1, 5, ")");
		checkToken(lexer.next(), Kind.PLUS, 1, 6, "+");
		checkToken(lexer.next(), Kind.MINUS, 1, 7, "-");
		checkToken(lexer.next(), Kind.TIMES, 1, 8, "*");
		checkToken(lexer.next(), Kind.DIV, 1, 9, "/");
		checkToken(lexer.next(), Kind.MOD, 1, 10, "%");
		checkToken(lexer.next(), Kind.QUESTION, 1, 11, "?");
		checkToken(lexer.next(), Kind.BANG, 1, 12, "!");
		checkToken(lexer.next(), Kind.ASSIGN, 1, 13, ":=");
		checkToken(lexer.next(), Kind.EQ, 1, 15, "=");
		checkToken(lexer.next(), Kind.NEQ, 1, 16, "#");
		checkToken(lexer.next(), Kind.LT, 1, 17, "<");
		checkToken(lexer.next(), Kind.LE, 1, 18, "<=");
		checkToken(lexer.next(), Kind.GT, 1, 20, ">");
		checkToken(lexer.next(), Kind.GE, 1, 21, ">=");

		checkEOF(lexer.next());
	}

	@Test
	void testSingleInt() throws LexicalException {
		String input = "1";
		show(input);
		ILexer lexer = getLexer(input);
		checkInt(lexer.next(), 1);
		checkEOF(lexer.next());
	}

	@Test
	void testSingleLineInt() throws LexicalException {
		String input = "1023456789";
		show(input);
		ILexer lexer = getLexer(input);
		checkInt(lexer.next(), 1023456789);
		checkEOF(lexer.next());
	}

	@Test
	void testNewLineInt() throws LexicalException {
		String input = """
				1
				0
				""";
		show(input);
		ILexer lexer = getLexer(input);
		checkInt(lexer.next(), 1);
		checkInt(lexer.next(), 0);
		checkEOF(lexer.next());
	}

	// comments should be skipped
	@Test
	void testComment0() throws LexicalException {
		// Note that the quotes around "This is a string" are passed to the lexer.
		String input = """
				"This is a string"
				// this is a comment
				*
				""";
		show(input);
		ILexer lexer = getLexer(input);
		checkToken(lexer.next(), Kind.STRING_LIT, 1, 1);
		checkToken(lexer.next(), Kind.TIMES, 3, 1);
		checkEOF(lexer.next());
	}

	// Several identifiers to test positions
	@Test
	public void testIdent0Comment() throws LexicalException {
		// notice that the characters before ghi is a \t and a space ' '
		String input = """
				abc
				// haha
				// hello! how are you?
				// I'm fine thank you and you?
				  def
					 ghi

				""";
		show(input);
		ILexer lexer = getLexer(input);
		checkIdent(lexer.next(), "abc", 1, 1);
		checkIdent(lexer.next(), "def", 5, 3);
		checkIdent(lexer.next(), "ghi", 6, 3);
		checkEOF(lexer.next());
	}

	// Example for testing input with an illegal character
	@Test
	void testError0() throws LexicalException {
		String input = """
				abc
				@
				""";
		show(input);
		ILexer lexer = getLexer(input);
		// this check should succeed
		checkIdent(lexer.next(), "abc");
		// this is expected to throw an exception since @ is not a legal
		// character unless it is part of a string or comment
		assertThrows(LexicalException.class, () -> {
			@SuppressWarnings("unused")
			IToken token = lexer.next();
		});
	}

	// Several identifiers to test positions
	@Test
	public void testIdent0() throws LexicalException {
		// notice that the characters before ghi is 5 spaces ' '
		String input = """
				abc
				  def
				     ghi

				""";
		show(input);
		ILexer lexer = getLexer(input);
		checkIdent(lexer.next(), "abc", 1, 1);
		checkIdent(lexer.next(), "def", 2, 3);
		checkIdent(lexer.next(), "ghi", 3, 6);
		checkEOF(lexer.next());
	}

	@Test
	public void testIdenAndReserved() throws LexicalException {
		String input = """
				AbcTRUE
				""";
		show(input);
		ILexer lexer = getLexer(input);
		checkIdent(lexer.next(), "AbcTRUE", 1, 1);
		checkEOF(lexer.next());
	}

	@Test
	public void testReservedWords() throws LexicalException {
		String input = """
				TRUE CONST
				PROCEDURE
				DO    CALL
				FALSE
				a123 456b
				""";
		show(input);
		ILexer lexer = getLexer(input);
		checkBoolean(lexer.next(), true, 1, 1);
		checkToken(lexer.next(), Kind.KW_CONST, 1, 6);
		checkToken(lexer.next(), Kind.KW_PROCEDURE, 2, 1);
		checkToken(lexer.next(), Kind.KW_DO, 3, 1);
		checkToken(lexer.next(), Kind.KW_CALL, 3, 7);
		checkBoolean(lexer.next(), false, 4, 1);
		checkIdent(lexer.next(), "a123", 5, 1);
		checkInt(lexer.next(), 456, 5, 6);
		checkIdent(lexer.next(), "b", 5, 9);
		checkEOF(lexer.next());
	}

	@Test
	public void testIdenInt() throws LexicalException {
		String input = """
				a123 456b
				""";
		show(input);
		ILexer lexer = getLexer(input);
		checkIdent(lexer.next(), "a123", 1, 1);
		checkInt(lexer.next(), 456, 1, 6);
		checkIdent(lexer.next(), "b", 1, 9);
		checkEOF(lexer.next());
	}

	// Example showing how to handle number that are too big.
	@Test
	public void testIntTooBig() throws LexicalException {
		String input = """
				42
				99999999999999999999999999999999999999999999999999999999999999999999999
				""";
		ILexer lexer = getLexer(input);
		checkInt(lexer.next(), 42);
		Exception e = assertThrows(LexicalException.class, () -> {
			lexer.next();
		});
	}

	@Test
	public void testEscapeSequences0() throws LexicalException {
		String input = "\"\\b \\t \\n \\f \\r \"";
		show(input);
		ILexer lexer = getLexer(input);
		IToken t = lexer.next();
		String val = t.getStringValue();
		String expectedStringValue = "\b \t \n \f \r ";
		assertEquals(expectedStringValue, val);
		String text = String.valueOf(t.getText());
		String expectedText = "\"\\b \\t \\n \\f \\r \"";
		assertEquals(expectedText, text);
	}

	@Test
	public void testEscapeSequences1() throws LexicalException {
		String input = "   \" ...  \\\"  \\\'  \\\\  \"";
		show(input);
		ILexer lexer = getLexer(input);
		IToken t = lexer.next();
		String val = t.getStringValue();
		String expectedStringValue = " ...  \"  \'  \\  ";
		assertEquals(expectedStringValue, val);
		String text = String.valueOf(t.getText());
		String expectedText = "\" ...  \\\"  \\\'  \\\\  \""; // almost the same as input, but white space is omitted
		assertEquals(expectedText, text);
	}

	@Test
	public void testSimpleString() throws LexicalException {
		String input = """
				"string"
				""";
		show(input);
		ILexer lexer = getLexer(input);
		checkString(lexer.next(), "\"string\"", "string", 1, 1);
		checkEOF(lexer.next());
	}

	@Test
	public void testStringWithEscapeSequences() throws LexicalException {
		String input = """
				"Escape Sequences\\t"
				""";
		show(input);
		ILexer lexer = getLexer(input);
		checkString(lexer.next(), "\"Escape Sequences\\t\"", "Escape Sequences\t", 1, 1);
		checkEOF(lexer.next());
	}

	@Test
	public void testStringWithNewLine() throws LexicalException {
		String input = """
				"ha
				halo\\nha"
				abc
				""";

		show(input);
		ILexer lexer = getLexer(input);
		checkString(lexer.next(), "\"ha\nhalo\\nha\"", "ha\nhalo\nha", 1, 1);
		checkIdent(lexer.next(), "abc", 3, 1);
		checkEOF(lexer.next());
	}

	@Test
	public void testStringWithNewLine1() throws LexicalException {
		String input = """
				\"ha
				halo\nha\"abc
				""";

		show(input);
		ILexer lexer = getLexer(input);
		checkString(lexer.next(), "\"ha\nhalo\nha\"", "ha\nhalo\nha", 1, 1);
		show("pass1");
		checkIdent(lexer.next(), "abc", 3, 4);
		checkEOF(lexer.next());
	}

	@Test
	public void testSimpleGetStringValue() throws LexicalException {
		String input = """
				"a b c"
				""";
		show(input);
		ILexer lexer = getLexer(input);
		checkString(lexer.next(), "\"a b c\"", "a b c", 1, 1);
	}

	@Test
	public void testGetStringValue() throws LexicalException {
		String input = """
				"a b\\nc"
				""";
		show(input);
		ILexer lexer = getLexer(input);
		checkString(lexer.next(), "\"a b\\nc\"", "a b\nc", 1, 1);
	}

	@Test
	public void testGetStringValue1() throws LexicalException {
		String input = """
				\"abc\\b\"
				""";
		show(input);
		ILexer lexer = getLexer(input);
		checkString(lexer.next(), "\"abc\\b\"", "abc\b", 1, 1);
	}

	@Test
	public void testSimplePeek() throws LexicalException {
		String input = """
				peek123
				""";
		show(input);
		ILexer lexer = getLexer(input);
		checkIdent(lexer.peek(), "peek123", 1, 1);
	}

	@Test
	public void testPeekAndNext() throws LexicalException {
		String input = """
				123peek
				""";
		show(input);
		ILexer lexer = getLexer(input);
		checkInt(lexer.peek(), 123, 1, 1);
		checkInt(lexer.next(), 123, 1, 1);
		checkIdent(lexer.next(), "peek", 1, 4);
	}

	@Test
	public void testStringPeekAndNext() throws LexicalException {
		String input = """
				"This is a string"
				123peek
				""";
		show(input);
		ILexer lexer = getLexer(input);
		checkString(lexer.peek(), "\"This is a string\"", "This is a string", 1, 1);
		checkString(lexer.next(), "\"This is a string\"", "This is a string", 1, 1);
		checkInt(lexer.next(), 123, 2, 1);
		checkIdent(lexer.next(), "peek", 2, 4);
	}

	// Mix of ID's, Num_lit, comments, string_lit's
	@Test
	public void testIDNNUM() throws LexicalException {
		String input = """
				df123 345 g546 IF
				//next is string

				 "Hello, World"
				""";
		ILexer lexer = getLexer(input);
		checkIdent(lexer.next(), "df123", 1, 1);
		checkInt(lexer.next(), 345, 1, 7);
		checkIdent(lexer.next(), "g546", 1, 11);
		checkToken(lexer.next(), Kind.KW_IF, 1, 16);
		checkToken(lexer.next(), Kind.STRING_LIT, 4, 2);
		checkEOF(lexer.next());
	}

	@Test
	public void testInvalidIdent() throws LexicalException {
		String input = """
				$valid_123
				valid_and_symbol+
				invalid^
				""";
		show(input);
		ILexer lexer = getLexer(input);
		// all good
		checkIdent(lexer.next(), "$valid_123", 1, 1);
		// broken up into an ident and a plus
		checkIdent(lexer.next(), "valid_and_symbol", 2, 1);
		checkToken(lexer.next(), Kind.PLUS, 2, 17);
		// broken up into a valid ident and an invalid one (throws ex)
		checkIdent(lexer.next(), "invalid", 3, 1);
		assertThrows(LexicalException.class, () -> {
			lexer.next();
		});
	}

	@Test
	public void testIdenString() throws LexicalException {
		String input = """
				$valid_123 _haha \"hello_ **\"*?
				""";
		show(input);
		ILexer lexer = getLexer(input);
		this.checkIdent(lexer.next(), "$valid_123", 1, 1);
		this.checkIdent(lexer.next(), "_haha", 1, 12);
		this.checkString(lexer.next(), "\"hello_ **\"", "hello_ **", 1, 18);
		this.checkToken(lexer.peek(), Kind.TIMES, 1, 29);
		this.checkToken(lexer.next(), Kind.TIMES, 1, 29);
		this.checkToken(lexer.next(), Kind.QUESTION, 1, 30);
	}
	
	@Test
    public void testLongInput0() throws LexicalException {
        String input = """
        VAR x = 0;
        VAR y = TRUE;
        VAR z = "a";
        DO
            x = x + 1;
            y = !y;
            z = z + "a";
        WHILE (x < 10)
        """;
        ILexer lexer = getLexer(input);
        checkToken(lexer.next(), Kind.KW_VAR, 1, 1);
        checkIdent(lexer.next(), "x", 1, 5);
        checkToken(lexer.next(), Kind.EQ, 1, 7);
        checkInt(lexer.next(), 0, 1, 9);
        checkToken(lexer.next(), Kind.SEMI, 1, 10);

        checkToken(lexer.next(), Kind.KW_VAR, 2, 1);
        checkIdent(lexer.next(), "y", 2, 5);
        checkToken(lexer.next(), Kind.EQ, 2, 7);
        checkBool(lexer.next(), true, 2, 9);
        checkToken(lexer.next(), Kind.SEMI, 2, 13);

        checkToken(lexer.next(), Kind.KW_VAR, 3, 1);
        checkIdent(lexer.next(), "z", 3, 5);
        checkToken(lexer.next(), Kind.EQ, 3, 7);
        checkString(lexer.next(), "a", 3, 9);
        checkToken(lexer.next(), Kind.SEMI, 3, 12);

        checkToken(lexer.next(), Kind.KW_DO, 4, 1);

        checkIdent(lexer.next(), "x", 5, 5);
        checkToken(lexer.next(), Kind.EQ, 5, 7);
        checkIdent(lexer.next(), "x", 5, 9);
        checkToken(lexer.next(), Kind.PLUS, 5, 11);
        checkInt(lexer.next(), 1, 5, 13);
        checkToken(lexer.next(), Kind.SEMI, 5, 14);

        checkIdent(lexer.next(), "y", 6, 5);
        checkToken(lexer.next(), Kind.EQ, 6, 7);
        checkToken(lexer.next(), Kind.BANG, 6, 9);
        checkIdent(lexer.next(), "y", 6, 10);
        checkToken(lexer.next(), Kind.SEMI, 6, 11);

        checkIdent(lexer.next(), "z", 7, 5);
        checkToken(lexer.next(), Kind.EQ, 7, 7);
        checkIdent(lexer.next(), "z", 7, 9);
        checkToken(lexer.next(), Kind.PLUS, 7, 11);
        checkString(lexer.next(), "a", 7, 13);
        checkToken(lexer.next(), Kind.SEMI, 7, 16);

        checkToken(lexer.next(), Kind.KW_WHILE, 8, 1);
        checkToken(lexer.next(), Kind.LPAREN, 8, 7);
        checkIdent(lexer.next(), "x", 8, 8);
        checkToken(lexer.next(), Kind.LT, 8, 10);
        checkInt(lexer.next(), 10, 8, 12);
        checkToken(lexer.next(), Kind.RPAREN, 8, 14);

        checkEOF(lexer.next());
    }
	
	//Test 1
	//identifier.
	@Test
	void testID() throws LexicalException {
	  String input = "ad23";
	  show(input);
	  ILexer lexer = getLexer(input);
	  show(lexer);
	  checkToken(lexer.next(), Kind.IDENT, 1,1);
	  checkEOF(lexer.next());
	}

	//Test 2
	//booleans
	    //check that this token is an BOOLEAN_LIT with expected value
	    void checkBoolean(IToken t, boolean expectedValue) {
	      assertEquals(Kind.BOOLEAN_LIT, t.getKind());
	      assertEquals(expectedValue, t.getBooleanValue());
	    }
	    @Test
	    public void testBooleans() throws LexicalException {
	      String input = """
	          TRUE
	          FALSE
	          """;
	      ILexer lexer = getLexer(input);
	       checkBoolean(lexer.next(), true);
	          checkBoolean(lexer.next(), false);
	     checkEOF(lexer.next());
	    }


	//Test 4
	//. , ; ( ) + - * / % ? ! := = # < <= > >=
	@Test
	public void testAllSymmbols() throws LexicalException {
	  String input ="""
	        . , ; ( ) + - * / %
	        //next is line 3
	        ? ! := = # < <= > >=
	        """;

	  ILexer lexer = getLexer(input);
	  checkToken(lexer.next(), Kind.DOT, 1,1);
	  checkToken(lexer.next(), Kind.COMMA, 1,3);
	  checkToken(lexer.next(), Kind.SEMI, 1,5);
	  checkToken(lexer.next(), Kind.LPAREN, 1,7);
	  checkToken(lexer.next(), Kind.RPAREN, 1,9);
	  checkToken(lexer.next(), Kind.PLUS, 1,11);
	  checkToken(lexer.next(), Kind.MINUS, 1,13);
	  checkToken(lexer.next(), Kind.TIMES, 1,15);
	  checkToken(lexer.next(), Kind.DIV, 1,17);
	  checkToken(lexer.next(), Kind.MOD, 1,19);
	  checkToken(lexer.next(), Kind.QUESTION, 3,1);
	  checkToken(lexer.next(), Kind.BANG, 3,3);
	  checkToken(lexer.next(), Kind.ASSIGN, 3,5);
	  checkToken(lexer.next(), Kind.EQ, 3,8);
	  checkToken(lexer.next(), Kind.NEQ, 3,10);
	  checkToken(lexer.next(), Kind.LT, 3,12);
	  checkToken(lexer.next(), Kind.LE, 3,14);
	  checkToken(lexer.next(), Kind.GT, 3,17);
	  checkToken(lexer.next(), Kind.GE, 3,19);
	  checkEOF(lexer.next());
	}


	//Test 5
	//reserved words
	@Test
	public void testAllreserved1() throws LexicalException {
	  String input ="""
	        CONST VAR PROCEDURE
	             CALL BEGIN END
	                //next is line 3
	                IF THEN WHILE DO
	       
	        """;

	  ILexer lexer = getLexer(input);
	  checkToken(lexer.next(), Kind.KW_CONST, 1,1);
	  checkToken(lexer.next(), Kind.KW_VAR, 1,7);
	  checkToken(lexer.next(), Kind.KW_PROCEDURE, 1,11);
	  checkToken(lexer.next(), Kind.KW_CALL, 2,6);
	  checkToken(lexer.next(), Kind.KW_BEGIN, 2,11);
	  checkToken(lexer.next(), Kind.KW_END, 2,17);
	  checkToken(lexer.next(), Kind.KW_IF, 4,9);
	  checkToken(lexer.next(), Kind.KW_THEN, 4,12);
	  checkToken(lexer.next(), Kind.KW_WHILE, 4,17);
	  checkToken(lexer.next(), Kind.KW_DO, 4,23);
	  checkEOF(lexer.next());
	}


	//Test 6
	//12+3
	@Test
	public void testNoSpace() throws LexicalException {
	  String input ="""
	        12+3
	        """;

	  ILexer lexer = getLexer(input);
	  checkInt(lexer.next(), 12, 1, 1);
	  checkToken(lexer.next(), Kind.PLUS, 1, 3 );
	  checkInt(lexer.next(), 3, 1, 4);
	  checkEOF(lexer.next());
	}

	@Test
	public void testStringLineNum() throws LexicalException {
	    String input = """
	        "Hello\\nWorld"
	        "Hello\\tAgain"
	        """;
	    show(input);
	    ILexer lexer = getLexer(input);
	    // escape char within string affects line number
	    checkString(lexer.next(), "Hello\nWorld", 1, 1);
	    checkString(lexer.next(), "Hello\tAgain", 2, 1);
	    checkEOF(lexer.next());
	}

	//Test 8
	@Test
	    void testAllChars() throws LexicalException {
	        String input = """
	                .,; ()+-*/%?!:==#<<=>>=
	                """;
	        show(input);
	        ILexer lexer = getLexer(input);
	        checkToken(lexer.next(), Kind.DOT, 1,1);
	        checkToken(lexer.next(), Kind.COMMA, 1,2);
	        checkToken(lexer.next(), Kind.SEMI, 1,3);
	        checkToken(lexer.next(), Kind.LPAREN, 1,5);
	        checkToken(lexer.next(), Kind.RPAREN, 1,6);
	        checkToken(lexer.next(), Kind.PLUS, 1,7);
	        checkToken(lexer.next(), Kind.MINUS, 1,8);
	        checkToken(lexer.next(), Kind.TIMES, 1,9);
	        checkToken(lexer.next(), Kind.DIV, 1,10);
	        checkToken(lexer.next(), Kind.MOD, 1,11);
	        checkToken(lexer.next(), Kind.QUESTION, 1,12);
	        checkToken(lexer.next(), Kind.BANG, 1,13);
	        checkToken(lexer.next(), Kind.ASSIGN, 1,14);
	        checkToken(lexer.next(), Kind.EQ, 1,16);
	        checkToken(lexer.next(), Kind.NEQ, 1,17);
	        checkToken(lexer.next(), Kind.LT, 1,18);
	        checkToken(lexer.next(), Kind.LE, 1,19);
	        checkToken(lexer.next(), Kind.GT, 1,21);
	        checkToken(lexer.next(), Kind.GE, 1,22);
	        checkEOF(lexer.next());
	    }

	//Test 9
	@Test
	// make sure your program does not confuse comments with divide
	    void testCommentWithDiv() throws LexicalException {
	        String input = """
	                ///
	                """;
	        show(input);
	        ILexer lexer = getLexer(input);
	        checkEOF(lexer.next());
	    }

	//Test 10
	@Test
	// nonzero numbers can’t start with 0
	    public void testNumberStartWithZero() throws LexicalException {
	        String input = """
	                010
	                """;
	        show(input);
	        ILexer lexer = getLexer(input);
	        checkInt(lexer.next(), 0, 1,1);
	        checkInt(lexer.next(), 10, 1,2);
	        checkEOF(lexer.next());
	    }

	//Test 11
	@Test
	    public void testKeywordBacktoBack() throws LexicalException {
	        String input = """
	                DOWHILE
	                """;
	        show(input);
	        ILexer lexer = getLexer(input);
	       checkIdent(lexer.next(), "DOWHILE", 1, 1);
	        checkEOF(lexer.next());
	    }

	
	//Test 13
	@Test
	    public void testUnterminatedString() throws LexicalException {
	        String input = """
	                "unterminated
	                """;
	        ILexer lexer = getLexer(input);
	        assertThrows(LexicalException.class, () -> {
	            lexer.next();
	        });
	    }

	//Test 14
	@Test
	    public void testInvalidEscapeSequence() throws LexicalException {
	        String input = "\"esc\\\"";
	        show(input);
	        ILexer lexer = getLexer(input);
	        assertThrows(LexicalException.class, () -> {
	            lexer.next();
	        });
	    }
	
	@Test
    void testColon() throws LexicalException {
        String input = """
        foo
        :bar
                """;
        show(input);
        ILexer lexer = getLexer(input);
        checkIdent(lexer.next(), "foo");
        assertThrows(LexicalException.class, () -> {
      @SuppressWarnings("unused")
      IToken token = lexer.next();
    });
    }

	//Test 17
	//to test correct function of newline in string
	@Test
	    void stringSpaces() throws LexicalException
	    {

	        String input = """
	                "Line 1 \n"
	                "Line 3 \\n"
	                "Line 4"
	                "Column\t""Column\\t"abc
	                """;         
	                

	        show(input);
	        ILexer lexer = getLexer(input);
	        checkToken(lexer.next(), Kind.STRING_LIT, 1,1);
	        checkToken(lexer.next(), Kind.STRING_LIT, 3,1);
	        checkToken(lexer.next(), Kind.STRING_LIT, 4,1);
	        checkToken(lexer.next(), Kind.STRING_LIT, 5,1);
	        checkToken(lexer.next(), Kind.STRING_LIT, 5,10);
	        checkToken(lexer.next(), Kind.IDENT, 5,20);
	    }


	    //Test 18
	    @Test
	    void testIncompleteAssign() throws LexicalException {
	        String input = """
	                .,;:
	                """;
	        show(input);
	        ILexer lexer = getLexer(input);
	        checkToken(lexer.next(), Kind.DOT, 1, 1);
	        checkToken(lexer.next(), Kind.COMMA, 1, 2);
	        checkToken(lexer.next(), Kind.SEMI, 1, 3);
	        assertThrows(LexicalException.class, () -> {
	            lexer.next();
	        });
	        checkEOF(lexer.next());
	    }

	// TEST 21
	@Test
	    public void testSingleLenChars() throws LexicalException {
	        String input = """
	                \r\nabc
	                \t\t\t
	                \tdef
	                """;	        
	        ILexer lexer = getLexer(input);
	        this.checkIdent(lexer.next(), "abc", 2, 1);
	        this.checkIdent(lexer.next(), "def", 4, 2);
	        checkEOF(lexer.next());
	    }


}
