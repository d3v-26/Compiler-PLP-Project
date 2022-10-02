package edu.ufl.cise.plpfa22;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import edu.ufl.cise.plpfa22.ast.ASTNode;
import edu.ufl.cise.plpfa22.ast.Block;
import edu.ufl.cise.plpfa22.ast.ConstDec;
import edu.ufl.cise.plpfa22.ast.Expression;
import edu.ufl.cise.plpfa22.ast.ExpressionBinary;
import edu.ufl.cise.plpfa22.ast.ExpressionBooleanLit;
import edu.ufl.cise.plpfa22.ast.ExpressionIdent;
import edu.ufl.cise.plpfa22.ast.ExpressionNumLit;
import edu.ufl.cise.plpfa22.ast.ExpressionStringLit;
import edu.ufl.cise.plpfa22.ast.Ident;
import edu.ufl.cise.plpfa22.ast.ProcDec;
import edu.ufl.cise.plpfa22.ast.Program;
import edu.ufl.cise.plpfa22.ast.Statement;
import edu.ufl.cise.plpfa22.ast.StatementAssign;
import edu.ufl.cise.plpfa22.ast.StatementBlock;
import edu.ufl.cise.plpfa22.ast.StatementCall;
import edu.ufl.cise.plpfa22.ast.StatementEmpty;
import edu.ufl.cise.plpfa22.ast.StatementIf;
import edu.ufl.cise.plpfa22.ast.StatementInput;
import edu.ufl.cise.plpfa22.ast.StatementOutput;
import edu.ufl.cise.plpfa22.ast.StatementWhile;
import edu.ufl.cise.plpfa22.ast.VarDec;

class ParserTest {

	ASTNode getAST(String input) throws PLPException {
		IParser parser = CompilerComponentFactory.getParser(CompilerComponentFactory.getLexer(input));
		return parser.parse();
	}

	@Test
//shortest legal program
	void test0() throws PLPException {
		String input = """
				.""";
		ASTNode ast = getAST(input);
		assertThat("", ast, instanceOf(Program.class));
		Block v0 = ((Program) ast).block;
		assertThat("", v0, instanceOf(Block.class));
		List<ConstDec> v1 = ((Block) v0).constDecs;
		assertEquals(0, v1.size());
		List<VarDec> v2 = ((Block) v0).varDecs;
		assertEquals(0, v2.size());
		List<ProcDec> v3 = ((Block) v0).procedureDecs;
		assertEquals(0, v3.size());
		Statement v4 = ((Block) v0).statement;
		assertThat("", v4, instanceOf(StatementEmpty.class));
	}

	@Test
	void test1() throws PLPException {
		String input = """
				! 0 .""";		
		ASTNode ast = getAST(input);
		assertThat("", ast, instanceOf(Program.class));
		Block v0 = ((Program) ast).block;
		assertThat("", v0, instanceOf(Block.class));
		List<ConstDec> v1 = ((Block) v0).constDecs;
		assertEquals(0, v1.size());
		List<VarDec> v2 = ((Block) v0).varDecs;
		assertEquals(0, v2.size());
		List<ProcDec> v3 = ((Block) v0).procedureDecs;
		assertEquals(0, v3.size());
		Statement v4 = ((Block) v0).statement;
		assertThat("", v4, instanceOf(StatementOutput.class));
		Expression v5 = ((StatementOutput) v4).expression;
		assertThat("", v5, instanceOf(ExpressionNumLit.class));
		IToken v6 = ((ExpressionNumLit) v5).firstToken;
		assertEquals("0", String.valueOf(v6.getText()));
	}

	@Test
	void test2() throws PLPException {
		String input = """
				! "hello" .""";
		ASTNode ast = getAST(input);
		assertThat("", ast, instanceOf(Program.class));
		Block v0 = ((Program) ast).block;
		assertThat("", v0, instanceOf(Block.class));
		List<ConstDec> v1 = ((Block) v0).constDecs;
		assertEquals(0, v1.size());
		List<VarDec> v2 = ((Block) v0).varDecs;
		assertEquals(0, v2.size());
		List<ProcDec> v3 = ((Block) v0).procedureDecs;
		assertEquals(0, v3.size());
		Statement v4 = ((Block) v0).statement;
		assertThat("", v4, instanceOf(StatementOutput.class));
		Expression v5 = ((StatementOutput) v4).expression;
		assertThat("", v5, instanceOf(ExpressionStringLit.class));
		IToken v6 = ((ExpressionStringLit) v5).firstToken;
		assertEquals("hello", v6.getStringValue());
	}

	@Test
	void test3() throws PLPException {
		String input = """
				! TRUE .""";
		ASTNode ast = getAST(input);
		assertThat("", ast, instanceOf(Program.class));
		Block v0 = ((Program) ast).block;
		assertThat("", v0, instanceOf(Block.class));
		List<ConstDec> v1 = ((Block) v0).constDecs;
		assertEquals(0, v1.size());
		List<VarDec> v2 = ((Block) v0).varDecs;
		assertEquals(0, v2.size());
		List<ProcDec> v3 = ((Block) v0).procedureDecs;
		assertEquals(0, v3.size());
		Statement v4 = ((Block) v0).statement;
		assertThat("", v4, instanceOf(StatementOutput.class));
		Expression v5 = ((StatementOutput) v4).expression;
		assertThat("", v5, instanceOf(ExpressionBooleanLit.class));
		IToken v6 = ((ExpressionBooleanLit) v5).firstToken;
		assertEquals("TRUE", String.valueOf(v6.getText()));
	}

	@Test
	void test4() throws PLPException {
		String input = """
				! abc
				.
				""";
		ASTNode ast = getAST(input);
		assertThat("", ast, instanceOf(Program.class));
		Block v0 = ((Program) ast).block;
		assertThat("", v0, instanceOf(Block.class));
		List<ConstDec> v1 = ((Block) v0).constDecs;
		assertEquals(0, v1.size());
		List<VarDec> v2 = ((Block) v0).varDecs;
		assertEquals(0, v2.size());
		List<ProcDec> v3 = ((Block) v0).procedureDecs;
		assertEquals(0, v3.size());
		Statement v4 = ((Block) v0).statement;
		assertThat("", v4, instanceOf(StatementOutput.class));
		Expression v5 = ((StatementOutput) v4).expression;
		assertThat("", v5, instanceOf(ExpressionIdent.class));
		IToken v6 = ((ExpressionIdent) v5).firstToken;
		assertEquals("abc", String.valueOf(v6.getText()));
	}

	@Test
	void test5() throws PLPException {
		String input = """
				VAR abc;
				.
				""";
		ASTNode ast = getAST(input);
		assertThat("", ast, instanceOf(Program.class));
		Block v0 = ((Program) ast).block;
		assertThat("", v0, instanceOf(Block.class));
		List<ConstDec> v1 = ((Block) v0).constDecs;
		assertEquals(0, v1.size());
		List<VarDec> v2 = ((Block) v0).varDecs;
		assertEquals(1, v2.size());
		assertThat("", v2.get(0), instanceOf(VarDec.class));
		IToken v3 = ((VarDec) v2.get(0)).ident;
		assertEquals("abc", String.valueOf(v3.getText()));
		List<ProcDec> v4 = ((Block) v0).procedureDecs;
		assertEquals(0, v4.size());
		Statement v5 = ((Block) v0).statement;
		assertThat("", v5, instanceOf(StatementEmpty.class));
	}

	@Test
	void test6() throws PLPException {
		String input = """
				BEGIN
				! "hello";
				! TRUE;
				!  33 ;
				! variable
				END
				.
				""";
		ASTNode ast = getAST(input);
		assertThat("", ast, instanceOf(Program.class));
		Block v0 = ((Program) ast).block;
		assertThat("", v0, instanceOf(Block.class));
		List<ConstDec> v1 = ((Block) v0).constDecs;
		assertEquals(0, v1.size());
		List<VarDec> v2 = ((Block) v0).varDecs;
		assertEquals(0, v2.size());
		List<ProcDec> v3 = ((Block) v0).procedureDecs;
		assertEquals(0, v3.size());
		Statement v4 = ((Block) v0).statement;
		assertThat("", v4, instanceOf(StatementBlock.class));
		List<Statement> v5 = ((StatementBlock) v4).statements;
		assertThat("", v5.get(0), instanceOf(StatementOutput.class));
		Expression v6 = ((StatementOutput) v5.get(0)).expression;
		assertThat("", v6, instanceOf(ExpressionStringLit.class));
		IToken v7 = ((ExpressionStringLit) v6).firstToken;
		assertEquals("hello", v7.getStringValue());
		assertThat("", v5.get(1), instanceOf(StatementOutput.class));
		Expression v8 = ((StatementOutput) v5.get(1)).expression;
		assertThat("", v8, instanceOf(ExpressionBooleanLit.class));
		IToken v9 = ((ExpressionBooleanLit) v8).firstToken;
		assertEquals("TRUE", String.valueOf(v9.getText()));
		assertThat("", v5.get(2), instanceOf(StatementOutput.class));
		Expression v10 = ((StatementOutput) v5.get(2)).expression;
		assertThat("", v10, instanceOf(ExpressionNumLit.class));
		IToken v11 = ((ExpressionNumLit) v10).firstToken;
		assertEquals("33", String.valueOf(v11.getText()));
		assertThat("", v5.get(3), instanceOf(StatementOutput.class));
		Expression v12 = ((StatementOutput) v5.get(3)).expression;
		assertThat("", v12, instanceOf(ExpressionIdent.class));
		IToken v13 = ((ExpressionIdent) v12).firstToken;
		assertEquals("variable", String.valueOf(v13.getText()));
	}

	@Test
	void test7() throws PLPException {
		String input = """
				BEGIN
				? abc;
				! variable
				END
				.
				""";
		ASTNode ast = getAST(input);
		assertThat("", ast, instanceOf(Program.class));
		Block v0 = ((Program) ast).block;
		assertThat("", v0, instanceOf(Block.class));
		List<ConstDec> v1 = ((Block) v0).constDecs;
		assertEquals(0, v1.size());
		List<VarDec> v2 = ((Block) v0).varDecs;
		assertEquals(0, v2.size());
		List<ProcDec> v3 = ((Block) v0).procedureDecs;
		assertEquals(0, v3.size());
		Statement v4 = ((Block) v0).statement;
		assertThat("", v4, instanceOf(StatementBlock.class));
		List<Statement> v5 = ((StatementBlock) v4).statements;
		assertThat("", v5.get(0), instanceOf(StatementInput.class));
		Ident v6 = ((StatementInput) v5.get(0)).ident;
		assertEquals("abc", String.valueOf(v6.getText()));
		assertThat("", v5.get(1), instanceOf(StatementOutput.class));
		Expression v7 = ((StatementOutput) v5.get(1)).expression;
		assertThat("", v7, instanceOf(ExpressionIdent.class));
		IToken v8 = ((ExpressionIdent) v7).firstToken;
		assertEquals("variable", String.valueOf(v8.getText()));
	}

	@Test
	void test8() throws PLPException {
		String input = """
				CONST a = 3, b = TRUE, c = "hello";
				.
				""";
		ASTNode ast = getAST(input);
		assertThat("", ast, instanceOf(Program.class));
		Block v0 = ((Program) ast).block;
		assertThat("", v0, instanceOf(Block.class));
		List<ConstDec> v1 = ((Block) v0).constDecs;
		assertEquals(3, v1.size());
		assertThat("", v1.get(0), instanceOf(ConstDec.class));
		IToken v2 = ((ConstDec) v1.get(0)).ident;
		assertEquals("a", String.valueOf(v2.getText()));
		Integer v3 = (Integer) ((ConstDec) v1.get(0)).val;
		assertEquals(3, v3);
		assertThat("", v1.get(1), instanceOf(ConstDec.class));
		IToken v4 = ((ConstDec) v1.get(1)).ident;
		assertEquals("b", String.valueOf(v4.getText()));
		Boolean v5 = (Boolean) ((ConstDec) v1.get(1)).val;
		assertEquals(true, v5);
		assertThat("", v1.get(2), instanceOf(ConstDec.class));
		IToken v6 = ((ConstDec) v1.get(2)).ident;
		assertEquals("c", String.valueOf(v6.getText()));
		String v7 = (String) ((ConstDec) v1.get(2)).val;
		assertEquals("hello", v7);
		List<VarDec> v8 = ((Block) v0).varDecs;
		assertEquals(0, v8.size());
		List<ProcDec> v9 = ((Block) v0).procedureDecs;
		assertEquals(0, v9.size());
		Statement v10 = ((Block) v0).statement;
		assertThat("", v10, instanceOf(StatementEmpty.class));
	}

	@Test
	void test9() throws PLPException {
		String input = """
				BEGIN
				x := 3;
				y := "hello";
				b := FALSE
				END
				.
				""";
		ASTNode ast = getAST(input);
		assertThat("", ast, instanceOf(Program.class));
		Block v0 = ((Program) ast).block;
		assertThat("", v0, instanceOf(Block.class));
		List<ConstDec> v1 = ((Block) v0).constDecs;
		assertEquals(0, v1.size());
		List<VarDec> v2 = ((Block) v0).varDecs;
		assertEquals(0, v2.size());
		List<ProcDec> v3 = ((Block) v0).procedureDecs;
		assertEquals(0, v3.size());
		Statement v4 = ((Block) v0).statement;
		assertThat("", v4, instanceOf(StatementBlock.class));
		List<Statement> v5 = ((StatementBlock) v4).statements;
		assertThat("", v5.get(0), instanceOf(StatementAssign.class));
		Ident v6 = ((StatementAssign) v5.get(0)).ident;
		assertEquals("x", String.valueOf(v6.getText()));
		Expression v7 = ((StatementAssign) v5.get(0)).expression;
		assertThat("", v7, instanceOf(ExpressionNumLit.class));
		IToken v8 = ((ExpressionNumLit) v7).firstToken;
		assertEquals("3", String.valueOf(v8.getText()));
		assertThat("", v5.get(1), instanceOf(StatementAssign.class));
		Ident v9 = ((StatementAssign) v5.get(1)).ident;
		assertEquals("y", String.valueOf(v9.getText()));
		Expression v10 = ((StatementAssign) v5.get(1)).expression;
		assertThat("", v10, instanceOf(ExpressionStringLit.class));
		IToken v11 = ((ExpressionStringLit) v10).firstToken;
		assertEquals("hello", v11.getStringValue());
		assertThat("", v5.get(2), instanceOf(StatementAssign.class));
		Ident v12 = ((StatementAssign) v5.get(2)).ident;
		assertEquals("b", String.valueOf(v12.getText()));
		Expression v13 = ((StatementAssign) v5.get(2)).expression;
		assertThat("", v13, instanceOf(ExpressionBooleanLit.class));
		IToken v14 = ((ExpressionBooleanLit) v13).firstToken;
		assertEquals("FALSE", String.valueOf(v14.getText()));
	}

	@Test
	void test10() throws PLPException {
		String input = """
				BEGIN
				CALL x
				END
				.
				""";
		ASTNode ast = getAST(input);
		assertThat("", ast, instanceOf(Program.class));
		Block v0 = ((Program) ast).block;
		assertThat("", v0, instanceOf(Block.class));
		List<ConstDec> v1 = ((Block) v0).constDecs;
		assertEquals(0, v1.size());
		List<VarDec> v2 = ((Block) v0).varDecs;
		assertEquals(0, v2.size());
		List<ProcDec> v3 = ((Block) v0).procedureDecs;
		assertEquals(0, v3.size());
		Statement v4 = ((Block) v0).statement;
		assertThat("", v4, instanceOf(StatementBlock.class));
		List<Statement> v5 = ((StatementBlock) v4).statements;
		assertThat("", v5.get(0), instanceOf(StatementCall.class));
		Ident v6 = ((StatementCall) v5.get(0)).ident;
		assertEquals("x", String.valueOf(v6.getText()));
	}

	@Test
	void test11() throws PLPException {
		String input = """
				CONST a=3;
				VAR x,y,z;
				PROCEDURE p;
				  VAR j;
				  BEGIN
				     ? x;
				     IF x = 0 THEN ! y ;
				     WHILE j < 24 DO CALL z
				  END;
				! z
				.
				""";
//		ILexer l = CompilerComponentFactory.getLexer(input);
//		for(int i =0;i<45;i++) {
//			System.out.println("Token: "+l.next().getKind());
//		}
		ASTNode ast = getAST(input);
		assertThat("", ast, instanceOf(Program.class));
		Block v0 = ((Program) ast).block;
		assertThat("", v0, instanceOf(Block.class));
		List<ConstDec> v1 = ((Block) v0).constDecs;
		assertEquals(1, v1.size());
		assertThat("", v1.get(0), instanceOf(ConstDec.class));
		IToken v2 = ((ConstDec) v1.get(0)).ident;
		assertEquals("a", String.valueOf(v2.getText()));
		Integer v3 = (Integer) ((ConstDec) v1.get(0)).val;
		assertEquals(3, v3);
		List<VarDec> v4 = ((Block) v0).varDecs;
		assertEquals(3, v4.size());
		assertThat("", v4.get(0), instanceOf(VarDec.class));
		IToken v5 = ((VarDec) v4.get(0)).ident;
		assertEquals("x", String.valueOf(v5.getText()));
		assertThat("", v4.get(1), instanceOf(VarDec.class));
		IToken v6 = ((VarDec) v4.get(1)).ident;
		assertEquals("y", String.valueOf(v6.getText()));
		assertThat("", v4.get(2), instanceOf(VarDec.class));
		IToken v7 = ((VarDec) v4.get(2)).ident;
		assertEquals("z", String.valueOf(v7.getText()));
		List<ProcDec> v8 = ((Block) v0).procedureDecs;
		assertEquals(1, v8.size());
		assertThat("", v8.get(0), instanceOf(ProcDec.class));
		IToken v9 = ((ProcDec) v8.get(0)).ident;
		assertEquals("p", String.valueOf(v9.getText()));
		Block v10 = ((ProcDec) v8.get(0)).block;
		assertThat("", v10, instanceOf(Block.class));
		List<ConstDec> v11 = ((Block) v10).constDecs;
		assertEquals(0, v11.size());
		List<VarDec> v12 = ((Block) v10).varDecs;
		assertEquals(1, v12.size());
		assertThat("", v12.get(0), instanceOf(VarDec.class));
		IToken v13 = ((VarDec) v12.get(0)).ident;
		assertEquals("j", String.valueOf(v13.getText()));
		List<ProcDec> v14 = ((Block) v10).procedureDecs;
		assertEquals(0, v14.size());
		Statement v15 = ((Block) v10).statement;
		assertThat("", v15, instanceOf(StatementBlock.class));
		List<Statement> v16 = ((StatementBlock) v15).statements;
		assertThat("", v16.get(0), instanceOf(StatementInput.class));
		Ident v17 = ((StatementInput) v16.get(0)).ident;
		assertEquals("x", String.valueOf(v17.getText()));
		assertThat("", v16.get(1), instanceOf(StatementIf.class));
		Expression v18 = ((StatementIf) v16.get(1)).expression;
		assertThat("", v18, instanceOf(ExpressionBinary.class));
		Expression v19 = ((ExpressionBinary) v18).e0;
		assertThat("", v19, instanceOf(ExpressionIdent.class));
		IToken v20 = ((ExpressionIdent) v19).firstToken;
		assertEquals("x", String.valueOf(v20.getText()));
		Expression v21 = ((ExpressionBinary) v18).e1;
		assertThat("", v21, instanceOf(ExpressionNumLit.class));
		IToken v22 = ((ExpressionNumLit) v21).firstToken;
		assertEquals("0", String.valueOf(v22.getText()));
		IToken v23 = ((ExpressionBinary) v18).op;
		assertEquals("=", String.valueOf(v23.getText()));
		Statement v24 = ((StatementIf) v16.get(1)).statement;
		assertThat("", v24, instanceOf(StatementOutput.class));
		Expression v25 = ((StatementOutput) v24).expression;
		assertThat("", v25, instanceOf(ExpressionIdent.class));
		IToken v26 = ((ExpressionIdent) v25).firstToken;
		assertEquals("y", String.valueOf(v26.getText()));
		assertThat("", v16.get(2), instanceOf(StatementWhile.class));
		Expression v27 = ((StatementWhile) v16.get(2)).expression;
		assertThat("", v27, instanceOf(ExpressionBinary.class));
		Expression v28 = ((ExpressionBinary) v27).e0;
		assertThat("", v28, instanceOf(ExpressionIdent.class));
		IToken v29 = ((ExpressionIdent) v28).firstToken;
		assertEquals("j", String.valueOf(v29.getText()));
		Expression v30 = ((ExpressionBinary) v27).e1;
		assertThat("", v30, instanceOf(ExpressionNumLit.class));
		IToken v31 = ((ExpressionNumLit) v30).firstToken;
		assertEquals("24", String.valueOf(v31.getText()));
		IToken v32 = ((ExpressionBinary) v27).op;
		assertEquals("<", String.valueOf(v32.getText()));
		Statement v33 = ((StatementWhile) v16.get(2)).statement;
		assertThat("", v33, instanceOf(StatementCall.class));
		Ident v34 = ((StatementCall) v33).ident;
		assertEquals("z", String.valueOf(v34.getText()));
		Statement v35 = ((Block) v0).statement;
		assertThat("", v35, instanceOf(StatementOutput.class));
		Expression v36 = ((StatementOutput) v35).expression;
		assertThat("", v36, instanceOf(ExpressionIdent.class));
		IToken v37 = ((ExpressionIdent) v36).firstToken;
		assertEquals("z", String.valueOf(v37.getText()));
	}

	@Test
	void test12() throws PLPException {
		String input = """
				CONST a=3;
				
				VAR x,y,z;
				
				PROCEDURE p;
				
				
				  VAR j;
				  BEGIN
				     ? x;
				     IF x = 0 THEN ! y ;
				     WHILE j < 24 DO CALL			 z
				     
				     
				     
				  END;
				! a+b - (c/e) * 35/(3+4)
				.
				""";
		ASTNode ast = getAST(input);
		assertThat("", ast, instanceOf(Program.class));
		Block v0 = ((Program) ast).block;
		assertThat("", v0, instanceOf(Block.class));
		List<ConstDec> v1 = ((Block) v0).constDecs;
		assertEquals(1, v1.size());
		assertThat("", v1.get(0), instanceOf(ConstDec.class));
		IToken v2 = ((ConstDec) v1.get(0)).ident;
		assertEquals("a", String.valueOf(v2.getText()));
		Integer v3 = (Integer) ((ConstDec) v1.get(0)).val;
		assertEquals(3, v3);
		List<VarDec> v4 = ((Block) v0).varDecs;
		assertEquals(3, v4.size());
		assertThat("", v4.get(0), instanceOf(VarDec.class));
		IToken v5 = ((VarDec) v4.get(0)).ident;
		assertEquals("x", String.valueOf(v5.getText()));
		assertThat("", v4.get(1), instanceOf(VarDec.class));
		IToken v6 = ((VarDec) v4.get(1)).ident;
		assertEquals("y", String.valueOf(v6.getText()));
		assertThat("", v4.get(2), instanceOf(VarDec.class));
		IToken v7 = ((VarDec) v4.get(2)).ident;
		assertEquals("z", String.valueOf(v7.getText()));
		List<ProcDec> v8 = ((Block) v0).procedureDecs;
		assertEquals(1, v8.size());
		assertThat("", v8.get(0), instanceOf(ProcDec.class));
		IToken v9 = ((ProcDec) v8.get(0)).ident;
		assertEquals("p", String.valueOf(v9.getText()));
		Block v10 = ((ProcDec) v8.get(0)).block;
		assertThat("", v10, instanceOf(Block.class));
		List<ConstDec> v11 = ((Block) v10).constDecs;
		assertEquals(0, v11.size());
		List<VarDec> v12 = ((Block) v10).varDecs;
		assertEquals(1, v12.size());
		assertThat("", v12.get(0), instanceOf(VarDec.class));
		IToken v13 = ((VarDec) v12.get(0)).ident;
		assertEquals("j", String.valueOf(v13.getText()));
		List<ProcDec> v14 = ((Block) v10).procedureDecs;
		assertEquals(0, v14.size());
		Statement v15 = ((Block) v10).statement;
		assertThat("", v15, instanceOf(StatementBlock.class));
		List<Statement> v16 = ((StatementBlock) v15).statements;
		assertThat("", v16.get(0), instanceOf(StatementInput.class));
		Ident v17 = ((StatementInput) v16.get(0)).ident;
		assertEquals("x", String.valueOf(v17.getText()));
		assertThat("", v16.get(1), instanceOf(StatementIf.class));
		Expression v18 = ((StatementIf) v16.get(1)).expression;
		assertThat("", v18, instanceOf(ExpressionBinary.class));
		Expression v19 = ((ExpressionBinary) v18).e0;
		assertThat("", v19, instanceOf(ExpressionIdent.class));
		IToken v20 = ((ExpressionIdent) v19).firstToken;
		assertEquals("x", String.valueOf(v20.getText()));
		Expression v21 = ((ExpressionBinary) v18).e1;
		assertThat("", v21, instanceOf(ExpressionNumLit.class));
		IToken v22 = ((ExpressionNumLit) v21).firstToken;
		assertEquals("0", String.valueOf(v22.getText()));
		IToken v23 = ((ExpressionBinary) v18).op;
		assertEquals("=", String.valueOf(v23.getText()));
		Statement v24 = ((StatementIf) v16.get(1)).statement;
		assertThat("", v24, instanceOf(StatementOutput.class));
		Expression v25 = ((StatementOutput) v24).expression;
		assertThat("", v25, instanceOf(ExpressionIdent.class));
		IToken v26 = ((ExpressionIdent) v25).firstToken;
		assertEquals("y", String.valueOf(v26.getText()));
		assertThat("", v16.get(2), instanceOf(StatementWhile.class));
		Expression v27 = ((StatementWhile) v16.get(2)).expression;
		assertThat("", v27, instanceOf(ExpressionBinary.class));
		Expression v28 = ((ExpressionBinary) v27).e0;
		assertThat("", v28, instanceOf(ExpressionIdent.class));
		IToken v29 = ((ExpressionIdent) v28).firstToken;
		assertEquals("j", String.valueOf(v29.getText()));
		Expression v30 = ((ExpressionBinary) v27).e1;
		assertThat("", v30, instanceOf(ExpressionNumLit.class));
		IToken v31 = ((ExpressionNumLit) v30).firstToken;
		assertEquals("24", String.valueOf(v31.getText()));
		IToken v32 = ((ExpressionBinary) v27).op;
		assertEquals("<", String.valueOf(v32.getText()));
		Statement v33 = ((StatementWhile) v16.get(2)).statement;
		assertThat("", v33, instanceOf(StatementCall.class));
		Ident v34 = ((StatementCall) v33).ident;
		assertEquals("z", String.valueOf(v34.getText()));
		Statement v35 = ((Block) v0).statement;
		assertThat("", v35, instanceOf(StatementOutput.class));
		Expression v36 = ((StatementOutput) v35).expression;
		assertThat("", v36, instanceOf(ExpressionBinary.class));
		Expression v37 = ((ExpressionBinary) v36).e0;
		assertThat("", v37, instanceOf(ExpressionBinary.class));
		Expression v38 = ((ExpressionBinary) v37).e0;
		assertThat("", v38, instanceOf(ExpressionIdent.class));
		IToken v39 = ((ExpressionIdent) v38).firstToken;
		assertEquals("a", String.valueOf(v39.getText()));
		Expression v40 = ((ExpressionBinary) v37).e1;
		assertThat("", v40, instanceOf(ExpressionIdent.class));
		IToken v41 = ((ExpressionIdent) v40).firstToken;
		assertEquals("b", String.valueOf(v41.getText()));
		IToken v42 = ((ExpressionBinary) v37).op;
		assertEquals("+", String.valueOf(v42.getText()));
		Expression v43 = ((ExpressionBinary) v36).e1;
		assertThat("", v43, instanceOf(ExpressionBinary.class));
		Expression v44 = ((ExpressionBinary) v43).e0;
		assertThat("", v44, instanceOf(ExpressionBinary.class));
		Expression v45 = ((ExpressionBinary) v44).e0;
		assertThat("", v45, instanceOf(ExpressionBinary.class));
		Expression v46 = ((ExpressionBinary) v45).e0;
		assertThat("", v46, instanceOf(ExpressionIdent.class));
		IToken v47 = ((ExpressionIdent) v46).firstToken;
		assertEquals("c", String.valueOf(v47.getText()));
		Expression v48 = ((ExpressionBinary) v45).e1;
		assertThat("", v48, instanceOf(ExpressionIdent.class));
		IToken v49 = ((ExpressionIdent) v48).firstToken;
		assertEquals("e", String.valueOf(v49.getText()));
		IToken v50 = ((ExpressionBinary) v45).op;
		assertEquals("/", String.valueOf(v50.getText()));
		Expression v51 = ((ExpressionBinary) v44).e1;
		assertThat("", v51, instanceOf(ExpressionNumLit.class));
		IToken v52 = ((ExpressionNumLit) v51).firstToken;
		assertEquals("35", String.valueOf(v52.getText()));
		IToken v53 = ((ExpressionBinary) v44).op;
		assertEquals("*", String.valueOf(v53.getText()));
		Expression v54 = ((ExpressionBinary) v43).e1;
		assertThat("", v54, instanceOf(ExpressionBinary.class));
		Expression v55 = ((ExpressionBinary) v54).e0;
		assertThat("", v55, instanceOf(ExpressionNumLit.class));
		IToken v56 = ((ExpressionNumLit) v55).firstToken;
		assertEquals("3", String.valueOf(v56.getText()));
		Expression v57 = ((ExpressionBinary) v54).e1;
		assertThat("", v57, instanceOf(ExpressionNumLit.class));
		IToken v58 = ((ExpressionNumLit) v57).firstToken;
		assertEquals("4", String.valueOf(v58.getText()));
		IToken v59 = ((ExpressionBinary) v54).op;
		assertEquals("+", String.valueOf(v59.getText()));
		IToken v60 = ((ExpressionBinary) v43).op;
		assertEquals("/", String.valueOf(v60.getText()));
		IToken v61 = ((ExpressionBinary) v36).op;
		assertEquals("-", String.valueOf(v61.getText()));
	}

	@Test
	void test13() throws PLPException {
		String input = """
				CONST a * b;
				.
				""";
		assertThrows(SyntaxException.class, () -> {
			@SuppressWarnings("unused")
			ASTNode ast = getAST(input);
		});
	}

	@Test
	void test14() throws PLPException {
		String input = """
				PROCEDURE 42
				.
				""";
		assertThrows(SyntaxException.class, () -> {
			@SuppressWarnings("unused")
			ASTNode ast = getAST(input);
		});
	}

	@Test
//The error in this example should be found by the Lexer
	void test15() throws PLPException {
		String input = """
				VAR @;
				.
				""";
		assertThrows(LexicalException.class, () -> {
			@SuppressWarnings("unused")
			ASTNode ast = getAST(input);
		});
	}
	
	@Test
	   void coleTest1() throws PLPException{
	       String input = """
	           VAR x,y;
	           PROCEDURE random;
	               CALL random_int;
	           BEGIN
	           x := 10;
	           y := random;
	           IF (x > y) THEN ! \"Number 1 is larger than number 2\";
	           IF (x < y) THEN ! \"Number 2 is larger than number 1\";
	           IF (x = y) THEN ! \"Number 1 is equal to number 2\"
	           END
	           .
	           """;
	       ASTNode ast = getAST(input);
	       assertThat("", ast, instanceOf(Program.class));
	       Block v0 = ((Program) ast).block;
	       assertThat("", v0, instanceOf(Block.class));
	       List<VarDec> v1 = ((Block) v0).varDecs;
	       assertEquals(2, v1.size());
	       IToken v2 = ((VarDec) v1.get(0)).ident;
	       assertEquals("x", String.valueOf(v2.getText()));
	       IToken v3 = ((VarDec) v1.get(1)).ident;
	       assertEquals("y", String.valueOf(v3.getText()));
	       List<ProcDec> v4 = ((Block) v0).procedureDecs;
	       assertEquals(1, v4.size());
	       IToken v5 = ((ProcDec) v4.get(0)).ident;
	       assertEquals("random", String.valueOf(v5.getText()));
	       Block v6 = ((ProcDec) v4.get(0)).block;
	       Statement v7 = ((Block) v6).statement;
	       assertThat("", v7, instanceOf(StatementCall.class));
	       Ident v8 = ((StatementCall) v7).ident;
	       assertEquals("random_int", String.valueOf(v8.getText()));
	       Statement v9 = ((Block) v0).statement;
	       assertThat("", v9, instanceOf(StatementBlock.class));
	       Statement v10 = ((StatementBlock) v9).statements.get(0);
	       assertThat("", v10, instanceOf(StatementAssign.class));
	       Ident v11 = ((StatementAssign) v10).ident;
	       assertEquals("x", String.valueOf(v11.getText()));
	       Expression v12 = ((StatementAssign) v10).expression;
	       assertThat("", v12, instanceOf(ExpressionNumLit.class));
	       Statement v13 = ((StatementBlock) v9).statements.get(1);
	       assertThat("", v13, instanceOf(StatementAssign.class));
	       Ident v14 = ((StatementAssign) v13).ident;
	       assertEquals("y", String.valueOf(v14.getText()));
	       Expression v15 = ((StatementAssign) v13).expression;
	       assertThat("", v15, instanceOf(ExpressionIdent.class));
	       Statement v16 = ((StatementBlock) v9).statements.get(2);
	       assertThat("", v16, instanceOf(StatementIf.class));
	       Expression v17 = ((StatementIf) v16).expression;
	       assertEquals("x", String.valueOf(((ExpressionBinary) v17).e0.getFirstToken().getText()));
	       assertEquals(">", String.valueOf(((ExpressionBinary) v17).op.getText()));
	       assertEquals("y", String.valueOf(((ExpressionBinary) v17).e1.getFirstToken().getText()));
	       Statement v18 = ((StatementIf) v16).statement;
	       assertThat("", v18, instanceOf(StatementOutput.class));
	       Expression v19 = ((StatementOutput) v18).expression;
	       assertThat("", v19, instanceOf(ExpressionStringLit.class));
	       assertEquals("\"Number 1 is larger than number 2\"", String.valueOf(v19.getFirstToken().getText()));
	       Statement v20 = ((StatementBlock) v9).statements.get(3);
	       assertThat("", v20, instanceOf(StatementIf.class));
	       Expression v21 = ((StatementIf) v20).expression;
	       assertEquals("x", String.valueOf(((ExpressionBinary) v21).e0.getFirstToken().getText()));
	       assertEquals("<", String.valueOf(((ExpressionBinary) v21).op.getText()));
	       assertEquals("y", String.valueOf(((ExpressionBinary) v21).e1.getFirstToken().getText()));
	       Statement v22 = ((StatementIf) v20).statement;
	       assertThat("", v22, instanceOf(StatementOutput.class));
	       Expression v23 = ((StatementOutput) v22).expression;
	       assertThat("", v23, instanceOf(ExpressionStringLit.class));
	       assertEquals("\"Number 2 is larger than number 1\"", String.valueOf(v23.getFirstToken().getText()));
	       Statement v24 = ((StatementBlock) v9).statements.get(4);
	       assertThat("", v24, instanceOf(StatementIf.class));
	       Expression v25 = ((StatementIf) v24).expression;
	       assertEquals("x", String.valueOf(((ExpressionBinary) v25).e0.getFirstToken().getText()));
	       assertEquals("=", String.valueOf(((ExpressionBinary) v25).op.getText()));
	       assertEquals("y", String.valueOf(((ExpressionBinary) v25).e1.getFirstToken().getText()));
	       Statement v26 = ((StatementIf) v24).statement;
	       assertThat("", v26, instanceOf(StatementOutput.class));
	       Expression v27 = ((StatementOutput) v26).expression;
	       assertThat("", v27, instanceOf(ExpressionStringLit.class));
	       assertEquals("\"Number 1 is equal to number 2\"", 
	       String.valueOf(v27.getFirstToken().getText()));
	   }
	
	@Test
	   void coleTest2() throws PLPException{
	       String input = """
	               ! x = y # 10 > 7 <= 45 < 50 >= 56
	               .
	               """;
	       ASTNode ast = getAST(input);
	       assertThat("", ast, instanceOf(Program.class));
	       Block v0 = ((Program) ast).block;
	       assertThat("", v0, instanceOf(Block.class));
	       Statement v1 = ((Block) v0).statement;
	       assertThat("", v1, instanceOf(StatementOutput.class));
	       Expression v2 = ((StatementOutput) v1).expression;
	       assertThat("", v2, instanceOf(ExpressionBinary.class));
	       assertEquals(">=", String.valueOf(((ExpressionBinary) v2).op.getText()));
	       assertEquals("56", String.valueOf(((ExpressionBinary) v2).e1.getFirstToken().getText()));
	       Expression v3 = ((ExpressionBinary) v2).e0;
	       assertThat("", v3, instanceOf(ExpressionBinary.class));
	       assertEquals("<", String.valueOf(((ExpressionBinary) v3).op.getText()));
	       assertEquals("50", String.valueOf(((ExpressionBinary) v3).e1.getFirstToken().getText()));
	       Expression v4 = ((ExpressionBinary) v3).e0;
	       assertThat("", v4, instanceOf(ExpressionBinary.class));
	       assertEquals("<=", String.valueOf(((ExpressionBinary) v4).op.getText()));
	       assertEquals("45", String.valueOf(((ExpressionBinary) v4).e1.getFirstToken().getText()));
	       Expression v5 = ((ExpressionBinary) v4).e0;
	       assertThat("", v5, instanceOf(ExpressionBinary.class));
	       assertEquals(">", String.valueOf(((ExpressionBinary) v5).op.getText()));
	       assertEquals("7", String.valueOf(((ExpressionBinary) v5).e1.getFirstToken().getText()));
	       Expression v6 = ((ExpressionBinary) v5).e0;
	       assertThat("", v6, instanceOf(ExpressionBinary.class));
	       assertEquals("#", String.valueOf(((ExpressionBinary) v6).op.getText()));
	       assertEquals("10", String.valueOf(((ExpressionBinary) v6).e1.getFirstToken().getText()));
	       Expression v7 = ((ExpressionBinary) v6).e0;
	       assertThat("", v7, instanceOf(ExpressionBinary.class));
	       assertEquals("=", String.valueOf(((ExpressionBinary) v7).op.getText()));
	       assertEquals("y", String.valueOf(((ExpressionBinary) v7).e1.getFirstToken().getText()));
	       assertEquals("x", String.valueOf(((ExpressionBinary) v7).e0.getFirstToken().getText()));
	   }
	@Test
	   void test18() throws PLPException {
	       String input = """
	               ! ((2-a)+4)+3
	               .
	               """;
	       ASTNode ast = getAST(input);
	       assertThat("", ast, instanceOf(Program.class));
	       Block v0 = ((Program) ast).block;
	       Statement v1 = ((Block) v0).statement;
	       assertThat("", v1, instanceOf(StatementOutput.class));
	       Expression v2 = ((StatementOutput) v1).expression;
	       assertThat("",v2, instanceOf(ExpressionBinary.class));
	       Expression v3 = ((ExpressionBinary) v2).e0;
	       assertThat("", v3, instanceOf(ExpressionBinary.class));
	       Expression v4 = ((ExpressionBinary) v3).e0;
	       assertThat("", v4, instanceOf(ExpressionBinary.class));
	       Expression v5 = ((ExpressionBinary) v4).e0;
	       assertThat("", v5, instanceOf(ExpressionNumLit.class));
	       assertEquals("2", String.valueOf(v5.firstToken.getText()));
	       Expression v6 = ((ExpressionBinary) v4).e1;
	       assertThat("", v6, instanceOf(ExpressionIdent.class));
	       assertEquals("a", String.valueOf(v6.firstToken.getText()));
	       Expression v7 = ((ExpressionBinary) v3).e1;
	       assertThat("", v7, instanceOf(ExpressionNumLit.class));
	       assertEquals("4", String.valueOf(v7.firstToken.getText()));
	       Expression v8 = ((ExpressionBinary) v2).e1;
	       assertThat("", v8, instanceOf(ExpressionNumLit.class));
	       assertEquals("3", String.valueOf(v8.firstToken.getText()));
	       IToken v9 = ((ExpressionBinary) v2).op;
	       assertEquals("+", String.valueOf(v9.getText()));
	       IToken v10 = ((ExpressionBinary) v3).op;
	       assertEquals("+", String.valueOf(v10.getText()));
	       IToken v11 = ((ExpressionBinary) v4).op;
	       assertEquals("-", String.valueOf(v11.getText()));
	   }
	 

	// Test if you are checking for EOF
	    @Test
	    void InvalidEndChar() throws PLPException {
	        String input = """
	                VAR abc;
	                . bruh
	                """;
	        assertThrows(SyntaxException.class, () -> {
	            @SuppressWarnings("unused")
	            ASTNode ast = getAST(input);
	        });
	    }

	    @Test

        // Test the expression with parenthesis

        void shr_test1() throws PLPException {

                String input = """

                            ! 40/((4+1)*2)

                            .

                                """;

                ASTNode ast = getAST(input);

                assertThat("", ast, instanceOf(Program.class));

                Block v0 = ((Program) ast).block;

                assertThat("", v0, instanceOf(Block.class));

                List<ConstDec> v1 = ((Block) v0).constDecs;

                assertEquals(0, v1.size());

                List<VarDec> v2 = ((Block) v0).varDecs;

                assertEquals(0, v2.size());

                List<ProcDec> v3 = ((Block) v0).procedureDecs;

                assertEquals(0, v3.size());

                Statement v4 = ((Block) v0).statement;

                assertThat("", v4, instanceOf(StatementOutput.class));

                Expression v5 = ((StatementOutput) v4).expression;

                assertThat("", v5, instanceOf(ExpressionBinary.class));

                Expression v6 = ((ExpressionBinary) v5).e0;

                assertThat("", v6, instanceOf(ExpressionNumLit.class));

                IToken v7 = ((ExpressionNumLit) v6).firstToken;

                assertEquals("40",String.valueOf(v7.getText()));

                IToken v8 = ((ExpressionBinary) v5).op;

                assertEquals("/", String.valueOf(v8.getText()));

                Expression v9 = ((ExpressionBinary) v5).e1;

                assertThat("", v9, instanceOf(ExpressionBinary.class));

                Expression v10 = ((ExpressionBinary) v9).e0;

                assertThat("", v10, instanceOf(ExpressionBinary.class));

                Expression v11 = ((ExpressionBinary) v10).e0;

                assertThat("", v11, instanceOf(ExpressionNumLit.class));

                IToken v12 = ((ExpressionNumLit) v11).firstToken;

                assertEquals("4", String.valueOf(v12.getText()));

                IToken v13 = ((ExpressionBinary) v10).op;

                assertEquals("+", String.valueOf(v13.getText()));

                Expression v14 = ((ExpressionBinary) v10).e1;

                assertThat("", v14, instanceOf(ExpressionNumLit.class));

                IToken v15 = ((ExpressionNumLit) v14).firstToken;

                assertEquals("1", String.valueOf(v15.getText()));

                IToken v16 = ((ExpressionBinary) v9).op;

                assertEquals("*", String.valueOf(v16.getText()));

                Expression v17 = ((ExpressionBinary) v9).e1;

                assertThat("", v14, instanceOf(ExpressionNumLit.class));

                IToken v18 = ((ExpressionNumLit) v17).firstToken;

                assertEquals("2", String.valueOf(v18.getText()));

        }


        @Test

        // Test the expression with parenthesis

        void shr_test2() throws PLPException {

                String input = """

                            ! 2+3*4

                            .

                                """;

                ASTNode ast = getAST(input);

                assertThat("", ast, instanceOf(Program.class));

                Block v0 = ((Program) ast).block;

                assertThat("", v0, instanceOf(Block.class));

                List<ConstDec> v1 = ((Block) v0).constDecs;

                assertEquals(0, v1.size());

                List<VarDec> v2 = ((Block) v0).varDecs;

                assertEquals(0, v2.size());

                List<ProcDec> v3 = ((Block) v0).procedureDecs;

                assertEquals(0, v3.size());

                Statement v4 = ((Block) v0).statement;

                assertThat("", v4, instanceOf(StatementOutput.class));

                Expression v5 = ((StatementOutput) v4).expression;

                assertThat("", v5, instanceOf(ExpressionBinary.class));

                Expression v6 = ((ExpressionBinary) v5).e0;

                assertThat("", v6, instanceOf(ExpressionNumLit.class));

                IToken v7 = ((ExpressionNumLit) v6).firstToken;

                assertEquals("2",String.valueOf(v7.getText()));

                IToken v8 = ((ExpressionBinary) v5).op;

                assertEquals("+", String.valueOf(v8.getText()));

                Expression v9 = ((ExpressionBinary) v5).e1;

                assertThat("", v9, instanceOf(ExpressionBinary.class));

                Expression v10 = ((ExpressionBinary) v9).e0;

                assertThat("", v10, instanceOf(ExpressionNumLit.class));

                IToken v11 = ((ExpressionNumLit) v10).firstToken;

                assertEquals("3", String.valueOf(v11.getText()));

                IToken v12 = ((ExpressionBinary) v9).op;

                assertEquals("*", String.valueOf(v12.getText()));

                Expression v13 = ((ExpressionBinary) v9).e1;

                assertThat("", v13, instanceOf(ExpressionNumLit.class));

                IToken v14 = ((ExpressionNumLit) v13).firstToken;

                assertEquals("4",String.valueOf(v14.getText()));

        }


@Test

// Test the Procedure keyword

void shr_test3() throws PLPException {

  String input = """

            PROCEDURE m;

               ! "Procedure is working." ;

            .

        """;

  // According to Phrase Structure, Should have “;” at end of procedure.

  // DS - 2022-09-30

  ASTNode ast = getAST(input);

  assertThat("", ast, instanceOf(Program.class));

  Block v0 = ((Program) ast).block;

  assertThat("", v0, instanceOf(Block.class));

  List<ConstDec> v1 = ((Block) v0).constDecs;

  assertEquals(0, v1.size());

  List<VarDec> v2 = ((Block) v0).varDecs;

  assertEquals(0, v2.size());

  List<ProcDec> v3 = ((Block) v0).procedureDecs;

  assertEquals(1, v3.size());

  assertThat("", v3.get(0), instanceOf(ProcDec.class));

  Statement v4 = ((Block) v0).statement;

  assertThat("", v4, instanceOf(StatementEmpty.class));

  IToken v5 = ((ProcDec) v3.get(0)).ident;

  assertEquals("m", String.valueOf(v5.getText()));

  Block v6 = ((ProcDec) v3.get(0)).block;

  assertThat("", v6, instanceOf(Block.class));

  List<ConstDec> v7 = ((Block) v6).constDecs;

  assertEquals(0, v7.size());

  List<VarDec> v8 = ((Block) v6).varDecs;

  assertEquals(0, v8.size());

  List<ProcDec> v9 = ((Block) v6).procedureDecs;

  assertEquals(0, v9.size());

  Block v11 = ((ProcDec) v3.get(0)).block;

  Statement v12 = ((Block) v11).statement;

  assertThat("", v12, instanceOf(StatementOutput.class));

  Expression v13 = ((StatementOutput) v12).expression;

  assertThat("", v13, instanceOf(ExpressionStringLit.class));

  assertEquals("Procedure is working.", v13.getFirstToken().getStringValue());

}


// Test the expression with parenthesis and incorrect semicolon

@Test

void shr_test4() {

  String input = """

            ! 40/((4+1)*2);

            .

        """;

  assertThrows(SyntaxException.class, () -> {

     @SuppressWarnings("unused")

     ASTNode ast = getAST(input);

  });

}


@Test

void shr_test5() throws PLPException {

  String input = """

            WHILE (x > y) DO ! \"Number 1 is larger than number 2\"

            .

        """;

  // <program> <statement (WHILE)> <statement (!)> <expression> . . . does not

  //   have a terminal “;” DS 2022-09-30

  ASTNode ast = getAST(input);

  assertThat("", ast, instanceOf(Program.class));

  Block v0 = ((Program) ast).block;

  assertThat("", v0, instanceOf(Block.class));

  List<ConstDec> v1 = ((Block) v0).constDecs;

  assertEquals(0, v1.size());

  List<VarDec> v2 = ((Block) v0).varDecs;

  assertEquals(0, v2.size());

  List<ProcDec> v3 = ((Block) v0).procedureDecs;

  assertEquals(0, v3.size());

  Statement v4 = ((Block) v0).statement;

  assertThat("", v4, instanceOf(StatementWhile.class));

  Expression v5 = ((StatementWhile) v4).expression;

  assertEquals("x", String.valueOf(((ExpressionBinary) v5).e0.getFirstToken().getText()));

  assertEquals(">", String.valueOf(((ExpressionBinary) v5).op.getText()));

  assertEquals("y", String.valueOf(((ExpressionBinary) v5).e1.getFirstToken().getText()));

  Statement v6 = ((StatementWhile) v4).statement;

  assertThat("", v6, instanceOf(StatementOutput.class));

  Expression v7 = ((StatementOutput) v6).expression;

  assertThat("", v7, instanceOf(ExpressionStringLit.class));

  assertEquals("\"Number 1 is larger than number 2\"", String.valueOf(v7.getFirstToken().getText()));

}


@Test

   void coleTest3() throws PLPException {

   String input = """

               ! \"Spooky Month\";

               ? input

               .

           """;

   assertThrows(SyntaxException.class, () -> {

       @SuppressWarnings("unused")

       ASTNode ast = getAST(input);

   });

} //added missing parentheses




//Testing a few errors


@Test

void aTest() throws PLPException

{

    String input = """

                PROCEDURE;

                .

            """;

    assertThrows(SyntaxException.class, () -> {

            @SuppressWarnings("unused")

            ASTNode ast = getAST(input);

    });

}


@Test

void aTest2() throws PLPException

{

    String input = """

                PROCEDURE a;

                            VAR b;

                    CONST abc = 5

                .

            """;

    assertThrows(SyntaxException.class, () -> {

            @SuppressWarnings("unused")

            ASTNode ast = getAST(input);

    });

}


@Test

void aTest3() throws PLPException

{

    String input = """

                VAR abc = 5;

                ! abc

                .

            """;

    assertThrows(SyntaxException.class, () -> {

            @SuppressWarnings("unused")

            ASTNode ast = getAST(input);

    });

}


@Test

void aTest4() throws PLPException

{

    String input = """

                CONST abc = 012;

                .

            """;

    assertThrows(SyntaxException.class, () -> {

            @SuppressWarnings("unused")

            ASTNode ast = getAST(input);

    });

}


@Test

void aTest5() throws PLPException

{

    String input = """

                VAR a, b, c;

                c := a +-/ b

               

                .

            """;

    assertThrows(SyntaxException.class, () -> {

            @SuppressWarnings("unused")

            ASTNode ast = getAST(input);

    });

}


@Test

void aTest6() throws PLPException

{

    String input = """

                VAR a b c;

                .

            """;

    assertThrows(SyntaxException.class, () -> {

            @SuppressWarnings("unused")

            ASTNode ast = getAST(input);

    });

}



//testing comments

@Test

void aTest7() throws PLPException

{

    String input = """

                //VAR ignore this;

                VAR var1;

                .

            """;

    ASTNode ast = getAST(input);

    assertThat("", ast, instanceOf(Program.class));

    Block v0 = ((Program) ast).block;

    assertThat("", v0, instanceOf(Block.class));

    List<ConstDec> v1 = ((Block) v0).constDecs;

    assertEquals(0, v1.size());

    List<VarDec> v2 = ((Block) v0).varDecs;

    assertEquals(1, v2.size());

    assertThat("", v2.get(0), instanceOf(VarDec.class));

    IToken v3 = ((VarDec) v2.get(0)).ident;

    assertEquals("var1", String.valueOf(v3.getText()));

    List<ProcDec> v4 = ((Block) v0).procedureDecs;

    assertEquals(0, v4.size());

    Statement v5 = ((Block) v0).statement;

    assertThat("", v5, instanceOf(StatementEmpty.class));

   

}


@Test

void aTest8() throws PLPException

{

    String input = """

                CONST TRUE = 1;

                .

            """;

    assertThrows(SyntaxException.class, () -> {

            @SuppressWarnings("unused")

            ASTNode ast = getAST(input);

    });

}


@Test

void aTest9() throws PLPException {	

    String input = """

                    WHILE "xyz"

                    DO

                    .

                    """;

    ASTNode ast = getAST(input);

    assertThat("", ast, instanceOf(Program.class));

    Block v0 = ((Program) ast).block;

    assertThat("", v0, instanceOf(Block.class));

    List<ConstDec> v1 = ((Block) v0).constDecs;

    assertEquals(0, v1.size());

    List<VarDec> v2 = ((Block) v0).varDecs;

    assertEquals(0, v2.size());

    List<ProcDec> v3 = ((Block) v0).procedureDecs;

    assertEquals(0, v3.size());

    Statement v4 = ((Block) v0).statement;

    assertThat("", v4, instanceOf(StatementWhile.class));

    Expression v5 = ((StatementWhile) v4).expression;

    assertThat("", v5, instanceOf(ExpressionStringLit.class));

    IToken v6 = ((ExpressionStringLit) v5).firstToken;

    assertEquals("xyz", v6.getStringValue());

    Statement v7 = ((StatementWhile) v4).statement;

    assertThat("", v7, instanceOf(StatementEmpty.class));

}


}
