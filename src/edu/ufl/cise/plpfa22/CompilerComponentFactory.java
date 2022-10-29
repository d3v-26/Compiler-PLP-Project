/**  This code is provided for solely for use of students in the course COP5556 Programming Language Principles at the 
 * University of Florida during the Fall Semester 2022 as part of the course project.  No other use is authorized. 
 */

package edu.ufl.cise.plpfa22;

import edu.ufl.cise.plpfa22.ast.ASTVisitor;

public class CompilerComponentFactory {

	public static ILexer getLexer(String input) {
		return new Lexer(input);
	}
	
	public static IParser getParser(ILexer lexer) {
		return new Parser(lexer);
	}
	
	public static ASTVisitor getScopeVisitor() {
		return new ASTVisitorImpl();
	}

	public static ASTVisitor getTypeInferenceVisitor() {
		return new TypeCheckVisitorImpl();
	}
}
