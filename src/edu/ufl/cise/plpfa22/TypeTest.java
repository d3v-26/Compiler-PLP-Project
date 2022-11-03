package edu.ufl.cise.plpfa22;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.DisplayNameGenerator;


import edu.ufl.cise.plpfa22.ast.ASTNode;
import edu.ufl.cise.plpfa22.ast.ASTVisitor;
import edu.ufl.cise.plpfa22.ast.PrettyPrintVisitor;

@DisplayNameGeneration(DisplayNameGenerator.Standard.class)
class TypeTest {
	static final boolean VERBOSE = true;
	
	ASTNode getAST(String input) throws PLPException {
       IParser parser = CompilerComponentFactory.getParser(CompilerComponentFactory.getLexer(input));
       ASTNode ast =  parser.parse();
       ASTVisitor scopes = CompilerComponentFactory.getScopeVisitor();
       ast.visit(scopes, null);
       return ast;
	}
	
	ASTNode checkTypes(ASTNode ast) throws PLPException {
	    ASTVisitor types = CompilerComponentFactory.getTypeInferenceVisitor();
	    ast.visit(types, null);
	    return ast;
	}
	
	void show(ASTNode ast) throws PLPException {
		if(VERBOSE) {
			if (ast != null) {System.out.println(PrettyPrintVisitor.AST2String(ast));}
			else {System.out.println("ast = null");}
		}
	}
	
	void show(Object obj ) {
		if(VERBOSE) {System.out.println(obj);}
	}
	
	//Use this for tests that are successfully typed
	void runTest(String input, TestInfo testInfo) throws PLPException {
		show("\n**********" + testInfo.getDisplayName().split("[(]")[0] + "*************");
		show(input);
		ASTNode ast = getAST(input);
		checkTypes(ast);
		show(ast);
	}
	
	//Use this one for tests that should detect an error
	void runTest(String input, TestInfo testInfo, @SuppressWarnings("rawtypes") Class expectedException) {
		show("\n**********" + testInfo.getDisplayName().split("[(]")[0] + "*************");
		show(input);
		assertThrows(TypeCheckException.class, () -> {
			ASTNode ast = null ;
			try {
				ast = getAST(input);
				checkTypes(ast);
			}
			catch(Exception e) {
				System.out.println("Exception thrown:  "+ e.getClass() +" "+ e.getMessage());
				show(ast);
				throw e;
			}
			show(ast);
		});	
	}
	@Test
	void test_numlit(TestInfo testInfo) throws PLPException{
		String input = """
		! 0 .""";
		runTest(input, testInfo);
	}
	
	@Test
	void stringlit(TestInfo testInfo) throws PLPException{
		String input = """
		! "hello" .
		""";
		runTest(input, testInfo);
	}
	
	@Test
	void booleanlit(TestInfo testInfo) throws PLPException{
		String input = """
		! TRUE .
		""";
		runTest(input, testInfo);
	}
	
	
	@Test
	void error_outputProcedure(TestInfo testInfo) throws PLPException{
		String input = """
				PROCEDURE p;;
		        !p //Cannot output a procedure
		        .
		        """;
		runTest(input, testInfo, TypeCheckException.class);
	}
	
	
	@Test
	void unusedVariable(TestInfo testInfo) throws PLPException{
		String input = """
		VAR abc; //never used, so this is legal
		.
		""";
		runTest(input, testInfo);
	}
	
	@Test
	void insufficientTypeInfo(TestInfo testInfo) throws PLPException{
		String input = """
		VAR abc; 
		! abc
		.
		""";
		runTest(input, testInfo, TypeCheckException.class);
	}
	
	@Test
	void constants(TestInfo testInfo) throws PLPException{
		String input = """
		CONST a = 3, b = TRUE, c = "hello";
		.
		""";
		runTest(input, testInfo);
	}
	@Test
	
	void inferVars0(TestInfo testInfo) throws PLPException{
		String input = """
				VAR x,y,z;
				BEGIN
				x := 3;
				y := "hello";
				z := FALSE
				END
				.
				""";
		runTest(input, testInfo);
	}
	
	@Test
	void error_assignment0(TestInfo testInfo) throws PLPException{
		String input = """
				VAR x,y,z;
				BEGIN
				x := 3;
				y := "hello";
				z := FALSE;
				y := x;  //attempting to assign number to string
				END
				.
				""";
		runTest(input, testInfo, TypeCheckException.class);
	}
	
	@Test
	void error_assignment1(TestInfo testInfo) throws PLPException{
		String input = """
				VAR x,y,z;
				BEGIN
				x := 3;
				y := "hello";
				z := FALSE;
				x := z;  //type error, x is number, z is boolean
				END
				.
				""";
		runTest(input, testInfo, TypeCheckException.class);
	}
	
	@Test
	void any_location(TestInfo testInfo) throws PLPException{
		String input = """
				VAR x;
				BEGIN
				? x;     //should still type this, even if 
				x := 3;  //type-determining usage follows it
				END
				.
				""";
		runTest(input, testInfo);
	}
	
	
	@Test
	void error_notEnoughTypeInfo0(TestInfo testInfo) throws PLPException{
		String input = """
		CONST a=3;
		VAR x,y,z;
		PROCEDURE p;
		  VAR j;
		  BEGIN
		     ? x;
		     IF x = 0 THEN ! y ;  //cannot type y
		     WHILE j < 24 DO CALL z
		  END;
		! z
		.
		""";
		runTest(input, testInfo, TypeCheckException.class);
	}
	
	
	@Test
	void error_notEnoughTypeInfo1(TestInfo testInfo) throws PLPException{
		String input = """
		VAR x,y,z;
		PROCEDURE p;
			CONST x = 3;
			? z;
		BEGIN
		?x  //This x has not been assigned
		END
		.
		""";
		runTest(input, testInfo, TypeCheckException.class);
	}
	
	@Test
	void PVC0(TestInfo testInfo) throws PLPException{
		String input = """
		VAR a,b;
		PROCEDURE p;
		  CONST a = 2, b=3;
		  PROCEDURE q;
		     CONST b=5;
		     VAR a;
		
		     PROCEDURE r;
		        VAR b;
		        BEGIN
		        b := 3;
		        a := 2;
		        ! "a=";
		        ! a;
		        ! "b=";
		        ! b;
		        END;
		    CALL r;
		  CALL q;
		BEGIN
		   CALL p;
		   a := 0;
		   b := TRUE
		   END
		   .
		""";
		runTest(input, testInfo);
	}
	
	@Test
	void error_cannotTypez(TestInfo testInfo) throws PLPException{
		String input = """
		CONST a = 3;
		VAR x,y,z;
		PROCEDURE p;
		   CONST a = 4;
		   VAR y,z; //this z cannot be typed
		   PROCEDURE q;
		      CONST a=5;
		      VAR z;
		      BEGIN
		        x:=a;
		        z:=a;
		        ! "this is q";
		        !x;
		        !z
		       END;
		   BEGIN
		      x := a;
		      y := a;
		      ! "this is p";
		      !x;
		      !z;
		      CALL q;
		   END;
		BEGIN
		   !a;
		   x := a;
		   y := a*2;
		   z := a*3;
		   CALL p;
		END .
		""";
		runTest(input, testInfo, TypeCheckException.class);
	}
	
	
	
	@Test
	void indirectRecursiveCalls(TestInfo testInfo) throws PLPException{
		String input = """
		PROCEDURE p;
		    CALL q;
		PROCEDURE q;
		    CALL p;
		! "done"
		.
		""";
		runTest(input,testInfo);
	}
	
	@Test
	void error_assigntoconstant(TestInfo testInfo) throws PLPException{
		String input = """
		CONST a=3;
		CONST b="String", c=TRUE;
		VAR x,y;
		PROCEDURE z;
				x:=4
		;
		IF a#x
		THEN
			c:=FALSE
		.
		""";
		runTest(input, testInfo, TypeCheckException.class);
	}
	
	@Test
	void testIfThen(TestInfo testInfo) throws PLPException{
		String input = """
		CONST a=3;
		CONST b="String";
		VAR x,y,c;
		PROCEDURE z;
				x:=4
		;
		IF a#x
		THEN
			c:=FALSE
		.
		""";
		runTest(input, testInfo);
	}
	
	
	@Test
	void while_do(TestInfo testInfo) throws PLPException{
		String input = """
		VAR abc;
		PROCEDURE hello;
		BEGIN
				WHILE abc#0
				DO
				abc := abc-1;
		END;.
		""";
		runTest(input, testInfo);
	}
	
	
	@Test
	void error_inputToProc(TestInfo testInfo) throws PLPException{
		String input = """
		PROCEDURE p;
			PROCEDURE q;;;
		
		PROCEDURE q;
			PROCEDURE p;;;
		
		PROCEDURE r;
			PROCEDURE p;
				PROCEDURE q;
					VAR r;
					BEGIN
						r:=3;
						IF r=3
						THEN
							WHILE r>=0
							DO
								r:=r+r
					END
				;
				CALL q
			;
			? r //r is a procedure, this is not legal.
		;
		!p
		.
		""";
		runTest(input, testInfo, TypeCheckException.class);
	}
	
	
	@Test
	void call(TestInfo testInfo) throws PLPException{
		String input = """
		PROCEDURE p;
			PROCEDURE p;
				PROCEDURE p;
					?p
				;
				CALL p
			;
		;
		.
		""";
		runTest(input, testInfo, TypeCheckException.class);
	}
	
	
	@Test
	void error_ifThenAssignToConstant(TestInfo testInfo) throws PLPException{
		String input = """
		CONST d=2 , e=34, f=34, g="TRUE";
		VAR a,b,c;
		IF b<=c
		THEN
			IF c>=d
			THEN
				IF d#e
				THEN
					IF e=f  //attempting to assign to a constant
					THEN
						BEGIN
							g:=TRUE;
							!f
						END
		.
		""";
		runTest(input,testInfo, TypeCheckException.class);
	}
	
	@Test
	void binaryExpression0(TestInfo testInfo) throws PLPException{
		String input = """
				CONST n=42, s="this is a string", x=TRUE;
				VAR a,b,c,d,e,f,g;
				BEGIN
				a := 4;
				b := n + 4;
				c := b - a;
				d := s + s;
				e := a*b;
				f := a/b;
				g := a%b;			
				END
				.
				""";
		runTest(input,testInfo);
	}
	
	
	
	@Test
	void binaryExpression1(TestInfo testInfo) throws PLPException{
		String input = """
		CONST d=2 , e=34, f=34, g="TRUE";
		VAR a,b,c;
		PROCEDURE whilen;
			VAR a,b,c;
			WHILE ((a+b)=c)
			DO
				WHILE ((c-d)#e)
				DO
					WHILE ((e%2)#(c/2))
					DO
						BEGIN
							?a;
							!b;
							c:=0
						END
			;
		.
		""";
		runTest(input, testInfo);
	}
	
	
	@Test
	void binaryExpression2(TestInfo testInfo) throws PLPException{
		String input = """
				CONST n="42", s="this is a string", x="TRUE";
				VAR a,b,c,d,e,f,g;
				BEGIN
				a := "4";
				b := n + "4";
				d := s + s;			
				END
				.
				""";
		runTest(input,testInfo);
	}
	
	
	
	@Test
	void expressions0(TestInfo testInfo) throws PLPException{
			String input = """
			VAR x,y,z;
			BEGIN
			x := 0;
			y := 1;
			z := FALSE;
			! (x = y) * z
			END
			.
			""";
			runTest(input,testInfo);
			}	
	
	@Test
	void expressions2(TestInfo testInfo) throws PLPException{
			String input = """
			VAR x,y,z;
			BEGIN
			! (x = y) * z;
			x := 0;
			z := FALSE
			END
			.
			""";
			runTest(input,testInfo);
			}	
	
	@Test
	void inferxzunusedy(TestInfo testInfo) throws PLPException{
			String input = """
			VAR x,y,z;
			BEGIN
			z := "hello";
			! z;
			z := x;
			! z
			END
			.
			""";
			runTest(input,testInfo);
	
			}
	
	@Test
	void expressionsOnStrings0(TestInfo testInfo) throws PLPException{
		String input = """
				CONST a = "hello";
				VAR x,y,z;
				BEGIN
				x := a;
				y := x+y+z
				END
				.
				""";
		runTest(input,testInfo);
	}
	
	@Test
	void expressionsOnStrings1(TestInfo testInfo) throws PLPException{
		String input = """
				CONST a = "hello";
				VAR x,y,z;
				BEGIN
				!y;
				x := a;
				y := x+y+z
				END
				.
				""";
		runTest(input,testInfo);
	}
	@Test
	void ss_testProcedureAssignment(TestInfo testInfo) {
	   String input = """
	           PROCEDURE A;
	           ;
	                 PROCEDURE B;
	                 ;
	                 A := B  //Cannot assign procedure to another
	                 .
	                 """;
	   runTest(input, testInfo, TypeCheckException.class);

	}


	@Test
	void ss_testAssignIntToString(TestInfo testInfo) {
	   String input = """
	           VAR x;
	           BEGIN
	           x:= 5;
	           x:= "test"
	           END
	           .
	           """;
	   runTest(input, testInfo, TypeCheckException.class);
	}

	@Test
	void ss_testInferType(TestInfo testInfo) throws PLPException {
	   String input = """
	           VAR x, y, z;
	           BEGIN
	           y := 1;
	           z := FALSE;
	           ! (x = y) * z // Inferred type of x
	           END
	           .
	           """;
	   runTest(input, testInfo);

	}

	@Test
	void ss_testCannotInferType(TestInfo testInfo) throws PLPException {
	   String input = """
	           VAR x, y, z;
	           BEGIN
	           z := FALSE;
	           ! (x = y) * z
	           END
	           .
	           """;
	   runTest(input, testInfo, TypeCheckException.class);

	}

	@Test
	void ss_testIncorrectAssignmentComparison(TestInfo testInfo) throws PLPException {
	   String input = """
	           VAR x, y, z;
	           BEGIN
	           x := 10;
	           y := "hello";
	           z := FALSE;
	           ! (x = y) * z
	           END
	           .
	           """;
	   runTest(input, testInfo, TypeCheckException.class);
	}

	@Test
	void ss_testIncorrectGuardCondition(TestInfo testInfo) throws PLPException {
	   String input = """
	           VAR x, y, z;
	           BEGIN
	           x := 10;
	           y := "hello";
	           z := FALSE;
	           IF y THEN ! y ;
	           END
	           .
	           """;
	   runTest(input, testInfo, TypeCheckException.class);
	}

	@Test
	void ss_testIncorrectWhileGuardCondition(TestInfo testInfo) throws PLPException {
	   String input = """
	           CONST x = 5;
	           BEGIN
	               WHILE x
	               DO
	               !x;
	           END
	           .
	           """;
	   runTest(input, testInfo, TypeCheckException.class);
	}



	@Test
	void ss_testIncorrectTypeAfterAssign(TestInfo testInfo) throws PLPException {
	   String input = """
	           VAR x, y, z;
	           BEGIN
	           x := 10;
	           z := FALSE;
	           ! (x = y) * z; //Type error
	           y := "hello"
	           END
	           .
	           """;
	   runTest(input, testInfo, TypeCheckException.class);
	}

	@Test
	   void inputToConst(TestInfo testInfo) throws PLPException{
	       String input = """
	        CONST e = 5;
	        ? e
	        .
	        """;
	       runTest(input,testInfo, TypeCheckException.class);
	   }

	@Test
	void nithin0_test(TestInfo testInfo) throws PLPException {
	   String input = """
	           CONST a="hello", b =1, c=TRUE;
	           CONST d=0;
	           VAR x,y,z;
	           BEGIN
	           !y;
	           x := a;
	           y := x+y+z;
	           //z := 0
	           END
	           .
	           """;
	   runTest(input, testInfo);
	}

	@Test
	void nithin1_test(TestInfo testInfo) throws PLPException {
	   String input = """
	           CONST a="hello", b =1, c=TRUE;
	           CONST d=0;
	           VAR x,y,z;
	           BEGIN
	           !y;
	           x := a;
	           y := x+y+z;
	           z := 0  //type not compatible
	           END
	           .
	           """;
	runTest(input, testInfo, TypeCheckException .class);
	}

	@Test
	void nithin2_test(TestInfo testInfo) throws PLPException {
	   String input = """
	           CONST a="hello", b =1, c=TRUE;
	           CONST d=FALSE;
	           VAR x,y,z;
	           BEGIN
	           ! ((x=y)=d) * z;  //x, y cannot be inferred
	           z := TRUE
	           END
	           .
	           """;
	   runTest(input, testInfo, TypeCheckException .class);
	}

	@Test
	void nithin3_test(TestInfo testInfo) throws PLPException {
	   String input = """
	           CONST a="hello", b=1, c=TRUE;
	           CONST d=FALSE;
	           VAR x,y,z;
	           PROCEDURE e;
	               VAR x;   //creating a new local variable to replace global x
	               PROCEDURE f;
	               x:=4   //type assigned to this local variable
	               ;
	           CALL f
	           ;
	           BEGIN
	           CALL e;
	           ! ((x=y)=d) * z;  //x, y cannot be inferred
	           z := TRUE
	           END
	           .
	           """;
	   runTest(input, testInfo, TypeCheckException .class);
	}

	@Test
	void nithin4_test(TestInfo testInfo) throws PLPException {
	   String input = """
	           CONST a="hello", b=1, c=TRUE;
	           CONST d=FALSE;
	           VAR x,y,z;
	           PROCEDURE e;
	               //VAR x;      no new variable declared
	               PROCEDURE f;
	               x:=4          //Inferring the type of the global variable in this scope
	               ;
	           CALL f
	           ;
	           BEGIN
	           CALL e;
	           ! ((x=y)=d) * z;  //x, y can be inferred
	           z := TRUE
	           END
	           .
	           """;
	   runTest(input, testInfo);
	}

	@Test
	void nithin5_test(TestInfo testInfo) throws PLPException {
	   String input = """
	           CONST a="hello", b=1, c=TRUE;
	           CONST d=FALSE;
	           VAR x,y,z;
	           PROCEDURE e;
	           ;
	           BEGIN
	           CALL e;
	           CALL x
	           END
	           .
	           """;
	   runTest(input, testInfo, TypeCheckException .class);
	}

	@Test
	    void error_reassignVar(TestInfo testInfo) throws PLPException{
	    String input = """
	            VAR x;
	            BEGIN
	            x:=5;
	            x:="test"    // we are not allowed to reassign a var to a different type
	            END
	            .
	            """;
	    runTest(input,testInfo, TypeCheckException.class);
	}


}


