package edu.ufl.cise.plpfa22;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import edu.ufl.cise.plpfa22.ast.ASTNode;
import edu.ufl.cise.plpfa22.ast.ASTVisitor;
import edu.ufl.cise.plpfa22.ast.Block;
import edu.ufl.cise.plpfa22.ast.ConstDec;
import edu.ufl.cise.plpfa22.ast.Declaration;
import edu.ufl.cise.plpfa22.ast.Expression;
import edu.ufl.cise.plpfa22.ast.ExpressionIdent;
import edu.ufl.cise.plpfa22.ast.ExpressionNumLit;
import edu.ufl.cise.plpfa22.ast.ExpressionBinary;
import edu.ufl.cise.plpfa22.ast.ExpressionBooleanLit;
import edu.ufl.cise.plpfa22.ast.Ident;
import edu.ufl.cise.plpfa22.ast.ProcDec;
import edu.ufl.cise.plpfa22.ast.Program;
import edu.ufl.cise.plpfa22.ast.Statement;
import edu.ufl.cise.plpfa22.ast.StatementIf;
import edu.ufl.cise.plpfa22.ast.StatementAssign;
import edu.ufl.cise.plpfa22.ast.StatementBlock;
import edu.ufl.cise.plpfa22.ast.StatementCall;
import edu.ufl.cise.plpfa22.ast.StatementEmpty;
import edu.ufl.cise.plpfa22.ast.StatementInput;
import edu.ufl.cise.plpfa22.ast.StatementOutput;
import edu.ufl.cise.plpfa22.ast.StatementWhile;
import edu.ufl.cise.plpfa22.ast.VarDec;

class ScopeTest {
	
	ASTNode getDecoratedAST(String input) throws PLPException {
		IParser parser = CompilerComponentFactory.getParser(CompilerComponentFactory.getLexer(input));
		ASTNode ast = parser.parse();
		ASTVisitor scopes = CompilerComponentFactory.getScopeVisitor();
		ast.visit(scopes, null);
		return ast;
	}

	@Test
	void test0() throws PLPException {
		String input = """
				CONST a = 3;
				! a
				.
				""";
		ASTNode ast = getDecoratedAST(input);
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
		int v4 = ((ConstDec) v1.get(0)).getNest();
		assertEquals(0, v4);
		List<VarDec> v5 = ((Block) v0).varDecs;
		assertEquals(0, v5.size());
		List<ProcDec> v6 = ((Block) v0).procedureDecs;
		assertEquals(0, v6.size());
		Statement v7 = ((Block) v0).statement;
		assertThat("", v7, instanceOf(StatementOutput.class));
		Expression v8 = ((StatementOutput) v7).expression;
		assertThat("", v8, instanceOf(ExpressionIdent.class));
		IToken v9 = ((ExpressionIdent) v8).firstToken;
		assertEquals("a", String.valueOf(v9.getText()));
		int v10 = ((ExpressionIdent) v8).getNest();
		assertEquals(0, v10);
		Declaration v11 = ((ExpressionIdent) v8).getDec();
		assertThat("", v11, instanceOf(ConstDec.class));
		IToken v12 = ((ConstDec) v11).ident;
		assertEquals("a", String.valueOf(v12.getText()));
		Integer v13 = (Integer) ((ConstDec) v11).val;
		assertEquals(3, v13);
		int v14 = ((ConstDec) v11).getNest();
		assertEquals(0, v14);
	}

	@Test
	void test1() throws PLPException {
		String input = """
				VAR a;
				BEGIN
				?a ;
				!a
				END
				.
				""";
		ASTNode ast = getDecoratedAST(input);
		assertThat("", ast, instanceOf(Program.class));
		Block v0 = ((Program) ast).block;
		assertThat("", v0, instanceOf(Block.class));
		List<ConstDec> v1 = ((Block) v0).constDecs;
		assertEquals(0, v1.size());
		List<VarDec> v2 = ((Block) v0).varDecs;
		assertEquals(1, v2.size());
		assertThat("", v2.get(0), instanceOf(VarDec.class));
		IToken v3 = ((VarDec) v2.get(0)).ident;
		assertEquals("a", String.valueOf(v3.getText()));
		int v4 = ((VarDec) v2.get(0)).getNest();
		assertEquals(0, v4);
		List<ProcDec> v5 = ((Block) v0).procedureDecs;
		assertEquals(0, v5.size());
		Statement v6 = ((Block) v0).statement;
		assertThat("", v6, instanceOf(StatementBlock.class));
		List<Statement> v7 = ((StatementBlock) v6).statements;
		assertThat("", v7.get(0), instanceOf(StatementInput.class));
		Ident v8 = ((StatementInput) v7.get(0)).ident;
		assertThat("", v8, instanceOf(Ident.class));
		IToken v9 = ((Ident) v8).firstToken;
		assertEquals("a", String.valueOf(v9.getText()));
		int v10 = ((Ident) v8).getNest();
		assertEquals(0, v10);
		Declaration v11 = ((Ident) v8).getDec();
		assertThat("", v11, instanceOf(VarDec.class));
		IToken v12 = ((VarDec) v11).ident;
		assertEquals("a", String.valueOf(v12.getText()));
		int v13 = ((VarDec) v11).getNest();
		assertEquals(0, v13);
		assertThat("", v7.get(1), instanceOf(StatementOutput.class));
		Expression v14 = ((StatementOutput) v7.get(1)).expression;
		assertThat("", v14, instanceOf(ExpressionIdent.class));
		IToken v15 = ((ExpressionIdent) v14).firstToken;
		assertEquals("a", String.valueOf(v15.getText()));
		int v16 = ((ExpressionIdent) v14).getNest();
		assertEquals(0, v16);
		Declaration v17 = ((ExpressionIdent) v14).getDec();
		assertThat("", v17, instanceOf(VarDec.class));
		IToken v18 = ((VarDec) v17).ident;
		assertEquals("a", String.valueOf(v18.getText()));
		int v19 = ((VarDec) v17).getNest();
		assertEquals(0, v19);
	}

	@Test
	void test2() throws PLPException {
		String input = """
				CONST a = 4;
				CALL a  //remember, no type checking yet
				.

				""";
		ASTNode ast = getDecoratedAST(input);
		assertThat("", ast, instanceOf(Program.class));
		Block v0 = ((Program) ast).block;
		assertThat("", v0, instanceOf(Block.class));
		List<ConstDec> v1 = ((Block) v0).constDecs;
		assertEquals(1, v1.size());
		assertThat("", v1.get(0), instanceOf(ConstDec.class));
		IToken v2 = ((ConstDec) v1.get(0)).ident;
		assertEquals("a", String.valueOf(v2.getText()));
		Integer v3 = (Integer) ((ConstDec) v1.get(0)).val;
		assertEquals(4, v3);
		int v4 = ((ConstDec) v1.get(0)).getNest();
		assertEquals(0, v4);
		List<VarDec> v5 = ((Block) v0).varDecs;
		assertEquals(0, v5.size());
		List<ProcDec> v6 = ((Block) v0).procedureDecs;
		assertEquals(0, v6.size());
		Statement v7 = ((Block) v0).statement;
		assertThat("", v7, instanceOf(StatementCall.class));
		Ident v8 = ((StatementCall) v7).ident;
		assertThat("", v8, instanceOf(Ident.class));
		IToken v9 = ((Ident) v8).firstToken;
		assertEquals("a", String.valueOf(v9.getText()));
		int v10 = ((Ident) v8).getNest();
		assertEquals(0, v10);
		Declaration v11 = ((Ident) v8).getDec();
		assertThat("", v11, instanceOf(ConstDec.class));
		IToken v12 = ((ConstDec) v11).ident;
		assertEquals("a", String.valueOf(v12.getText()));
		Integer v13 = (Integer) ((ConstDec) v11).val;
		assertEquals(4, v13);
		int v14 = ((ConstDec) v11).getNest();
		assertEquals(0, v14);
	}

	@Test
	void test3() throws PLPException {
		String input = """
				VAR x;
				PROCEDURE p;
				   !x;
				.
				""";
		ASTNode ast = getDecoratedAST(input);
		assertThat("", ast, instanceOf(Program.class));
		Block v0 = ((Program) ast).block;
		assertThat("", v0, instanceOf(Block.class));
		List<ConstDec> v1 = ((Block) v0).constDecs;
		assertEquals(0, v1.size());
		List<VarDec> v2 = ((Block) v0).varDecs;
		assertEquals(1, v2.size());
		assertThat("", v2.get(0), instanceOf(VarDec.class));
		IToken v3 = ((VarDec) v2.get(0)).ident;
		assertEquals("x", String.valueOf(v3.getText()));
		int v4 = ((VarDec) v2.get(0)).getNest();
		assertEquals(0, v4);
		List<ProcDec> v5 = ((Block) v0).procedureDecs;
		assertEquals(1, v5.size());
		assertThat("", v5.get(0), instanceOf(ProcDec.class));
		IToken v6 = ((ProcDec) v5.get(0)).ident;
		assertEquals("p", String.valueOf(v6.getText()));
		int v7 = ((ProcDec) v5.get(0)).getNest();
		assertEquals(0, v7);
		Block v8 = ((ProcDec) v5.get(0)).block;
		assertThat("", v8, instanceOf(Block.class));
		List<ConstDec> v9 = ((Block) v8).constDecs;
		assertEquals(0, v9.size());
		List<VarDec> v10 = ((Block) v8).varDecs;
		assertEquals(0, v10.size());
		List<ProcDec> v11 = ((Block) v8).procedureDecs;
		assertEquals(0, v11.size());
		Statement v12 = ((Block) v8).statement;
		assertThat("", v12, instanceOf(StatementOutput.class));
		Expression v13 = ((StatementOutput) v12).expression;
		assertThat("", v13, instanceOf(ExpressionIdent.class));
		IToken v14 = ((ExpressionIdent) v13).firstToken;
		assertEquals("x", String.valueOf(v14.getText()));
		int v15 = ((ExpressionIdent) v13).getNest();
		assertEquals(1, v15);
		Declaration v16 = ((ExpressionIdent) v13).getDec();
		assertThat("", v16, instanceOf(VarDec.class));
		IToken v17 = ((VarDec) v16).ident;
		assertEquals("x", String.valueOf(v17.getText()));
		int v18 = ((VarDec) v16).getNest();
		assertEquals(0, v18);
		Statement v19 = ((Block) v0).statement;
		assertThat("", v19, instanceOf(StatementEmpty.class));
	}

	@Test
	void test4() throws PLPException {
		String input = """

				VAR x;
				PROCEDURE p;
				   CONST x = 2;
				   !x;
				!x
				.
				""";
		ASTNode ast = getDecoratedAST(input);
		assertThat("", ast, instanceOf(Program.class));
		Block v0 = ((Program) ast).block;
		assertThat("", v0, instanceOf(Block.class));
		List<ConstDec> v1 = ((Block) v0).constDecs;
		assertEquals(0, v1.size());
		List<VarDec> v2 = ((Block) v0).varDecs;
		assertEquals(1, v2.size());
		assertThat("", v2.get(0), instanceOf(VarDec.class));
		IToken v3 = ((VarDec) v2.get(0)).ident;
		assertEquals("x", String.valueOf(v3.getText()));
		int v4 = ((VarDec) v2.get(0)).getNest();
		assertEquals(0, v4);
		List<ProcDec> v5 = ((Block) v0).procedureDecs;
		assertEquals(1, v5.size());
		assertThat("", v5.get(0), instanceOf(ProcDec.class));
		IToken v6 = ((ProcDec) v5.get(0)).ident;
		assertEquals("p", String.valueOf(v6.getText()));
		int v7 = ((ProcDec) v5.get(0)).getNest();
		assertEquals(0, v7);
		Block v8 = ((ProcDec) v5.get(0)).block;
		assertThat("", v8, instanceOf(Block.class));
		List<ConstDec> v9 = ((Block) v8).constDecs;
		assertEquals(1, v9.size());
		assertThat("", v9.get(0), instanceOf(ConstDec.class));
		IToken v10 = ((ConstDec) v9.get(0)).ident;
		assertEquals("x", String.valueOf(v10.getText()));
		Integer v11 = (Integer) ((ConstDec) v9.get(0)).val;
		assertEquals(2, v11);
		int v12 = ((ConstDec) v9.get(0)).getNest();
		assertEquals(1, v12);
		List<VarDec> v13 = ((Block) v8).varDecs;
		assertEquals(0, v13.size());
		List<ProcDec> v14 = ((Block) v8).procedureDecs;
		assertEquals(0, v14.size());
		Statement v15 = ((Block) v8).statement;
		assertThat("", v15, instanceOf(StatementOutput.class));
		Expression v16 = ((StatementOutput) v15).expression;
		assertThat("", v16, instanceOf(ExpressionIdent.class));
		IToken v17 = ((ExpressionIdent) v16).firstToken;
		assertEquals("x", String.valueOf(v17.getText()));
		int v18 = ((ExpressionIdent) v16).getNest();
		assertEquals(1, v18);
		Declaration v19 = ((ExpressionIdent) v16).getDec();
		assertThat("", v19, instanceOf(ConstDec.class));
		IToken v20 = ((ConstDec) v19).ident;
		assertEquals("x", String.valueOf(v20.getText()));
		Integer v21 = (Integer) ((ConstDec) v19).val;
		assertEquals(2, v21);
		int v22 = ((ConstDec) v19).getNest();
		assertEquals(1, v22);
		Statement v23 = ((Block) v0).statement;
		assertThat("", v23, instanceOf(StatementOutput.class));
		Expression v24 = ((StatementOutput) v23).expression;
		assertThat("", v24, instanceOf(ExpressionIdent.class));
		IToken v25 = ((ExpressionIdent) v24).firstToken;
		assertEquals("x", String.valueOf(v25.getText()));
		int v26 = ((ExpressionIdent) v24).getNest();
		assertEquals(0, v26);
		Declaration v27 = ((ExpressionIdent) v24).getDec();
		assertThat("", v27, instanceOf(VarDec.class));
		IToken v28 = ((VarDec) v27).ident;
		assertEquals("x", String.valueOf(v28.getText()));
		int v29 = ((VarDec) v27).getNest();
		assertEquals(0, v29);
	}

	@Test
	void test5() throws PLPException {
		String input = """
				PROCEDURE p;
				     CALL q;
				PROCEDURE q;
				;
				CALL p
				.
				""";
		ASTNode ast = getDecoratedAST(input);
		assertThat("", ast, instanceOf(Program.class));
		Block v0 = ((Program) ast).block;
		assertThat("", v0, instanceOf(Block.class));
		List<ConstDec> v1 = ((Block) v0).constDecs;
		assertEquals(0, v1.size());
		List<VarDec> v2 = ((Block) v0).varDecs;
		assertEquals(0, v2.size());
		List<ProcDec> v3 = ((Block) v0).procedureDecs;
		assertEquals(2, v3.size());
		assertThat("", v3.get(0), instanceOf(ProcDec.class));
		IToken v4 = ((ProcDec) v3.get(0)).ident;
		assertEquals("p", String.valueOf(v4.getText()));
		int v5 = ((ProcDec) v3.get(0)).getNest();
		assertEquals(0, v5);
		Block v6 = ((ProcDec) v3.get(0)).block;
		assertThat("", v6, instanceOf(Block.class));
		List<ConstDec> v7 = ((Block) v6).constDecs;
		assertEquals(0, v7.size());
		List<VarDec> v8 = ((Block) v6).varDecs;
		assertEquals(0, v8.size());
		List<ProcDec> v9 = ((Block) v6).procedureDecs;
		assertEquals(0, v9.size());
		Statement v10 = ((Block) v6).statement;
		assertThat("", v10, instanceOf(StatementCall.class));
		Ident v11 = ((StatementCall) v10).ident;
		assertThat("", v11, instanceOf(Ident.class));
		IToken v12 = ((Ident) v11).firstToken;
		assertEquals("q", String.valueOf(v12.getText()));
		int v13 = ((Ident) v11).getNest();
		assertEquals(1, v13);
		Declaration v14 = ((Ident) v11).getDec();
		assertThat("", v14, instanceOf(ProcDec.class));
		IToken v15 = ((ProcDec) v14).ident;
		assertEquals("q", String.valueOf(v15.getText()));
		int v16 = ((ProcDec) v14).getNest();
		assertEquals(0, v16);
		assertThat("", v3.get(1), instanceOf(ProcDec.class));
		IToken v17 = ((ProcDec) v3.get(1)).ident;
		assertEquals("q", String.valueOf(v17.getText()));
		int v18 = ((ProcDec) v3.get(1)).getNest();
		assertEquals(0, v18);
		Block v19 = ((ProcDec) v3.get(1)).block;
		assertThat("", v19, instanceOf(Block.class));
		List<ConstDec> v20 = ((Block) v19).constDecs;
		assertEquals(0, v20.size());
		List<VarDec> v21 = ((Block) v19).varDecs;
		assertEquals(0, v21.size());
		List<ProcDec> v22 = ((Block) v19).procedureDecs;
		assertEquals(0, v22.size());
		Statement v23 = ((Block) v19).statement;
		assertThat("", v23, instanceOf(StatementEmpty.class));
		Statement v24 = ((Block) v0).statement;
		assertThat("", v24, instanceOf(StatementCall.class));
		Ident v25 = ((StatementCall) v24).ident;
		assertThat("", v25, instanceOf(Ident.class));
		IToken v26 = ((Ident) v25).firstToken;
		assertEquals("p", String.valueOf(v26.getText()));
		int v27 = ((Ident) v25).getNest();
		assertEquals(0, v27);
		Declaration v28 = ((Ident) v25).getDec();
		assertThat("", v28, instanceOf(ProcDec.class));
		IToken v29 = ((ProcDec) v28).ident;
		assertEquals("p", String.valueOf(v29.getText()));
		int v30 = ((ProcDec) v28).getNest();
		assertEquals(0, v30);
	}

	@Test
	void test6() throws PLPException {
		String input = """
				PROCEDURE p;
					CALL q;
				PROCEDURE q;
				    CALL p;
				.
				""";
		ASTNode ast = getDecoratedAST(input);
		assertThat("", ast, instanceOf(Program.class));
		Block v0 = ((Program) ast).block;
		assertThat("", v0, instanceOf(Block.class));
		List<ConstDec> v1 = ((Block) v0).constDecs;
		assertEquals(0, v1.size());
		List<VarDec> v2 = ((Block) v0).varDecs;
		assertEquals(0, v2.size());
		List<ProcDec> v3 = ((Block) v0).procedureDecs;
		assertEquals(2, v3.size());
		assertThat("", v3.get(0), instanceOf(ProcDec.class));
		IToken v4 = ((ProcDec) v3.get(0)).ident;
		assertEquals("p", String.valueOf(v4.getText()));
		int v5 = ((ProcDec) v3.get(0)).getNest();
		assertEquals(0, v5);
		Block v6 = ((ProcDec) v3.get(0)).block;
		assertThat("", v6, instanceOf(Block.class));
		List<ConstDec> v7 = ((Block) v6).constDecs;
		assertEquals(0, v7.size());
		List<VarDec> v8 = ((Block) v6).varDecs;
		assertEquals(0, v8.size());
		List<ProcDec> v9 = ((Block) v6).procedureDecs;
		assertEquals(0, v9.size());
		Statement v10 = ((Block) v6).statement;
		assertThat("", v10, instanceOf(StatementCall.class));
		Ident v11 = ((StatementCall) v10).ident;
		assertThat("", v11, instanceOf(Ident.class));
		IToken v12 = ((Ident) v11).firstToken;
		assertEquals("q", String.valueOf(v12.getText()));
		int v13 = ((Ident) v11).getNest();
		assertEquals(1, v13);
		Declaration v14 = ((Ident) v11).getDec();
		assertThat("", v14, instanceOf(ProcDec.class));
		IToken v15 = ((ProcDec) v14).ident;
		assertEquals("q", String.valueOf(v15.getText()));
		int v16 = ((ProcDec) v14).getNest();
		assertEquals(0, v16);
		assertThat("", v3.get(1), instanceOf(ProcDec.class));
		IToken v17 = ((ProcDec) v3.get(1)).ident;
		assertEquals("q", String.valueOf(v17.getText()));
		int v18 = ((ProcDec) v3.get(1)).getNest();
		assertEquals(0, v18);
		Block v19 = ((ProcDec) v3.get(1)).block;
		assertThat("", v19, instanceOf(Block.class));
		List<ConstDec> v20 = ((Block) v19).constDecs;
		assertEquals(0, v20.size());
		List<VarDec> v21 = ((Block) v19).varDecs;
		assertEquals(0, v21.size());
		List<ProcDec> v22 = ((Block) v19).procedureDecs;
		assertEquals(0, v22.size());
		Statement v23 = ((Block) v19).statement;
		assertThat("", v23, instanceOf(StatementCall.class));
		Ident v24 = ((StatementCall) v23).ident;
		assertThat("", v24, instanceOf(Ident.class));
		IToken v25 = ((Ident) v24).firstToken;
		assertEquals("p", String.valueOf(v25.getText()));
		int v26 = ((Ident) v24).getNest();
		assertEquals(1, v26);
		Declaration v27 = ((Ident) v24).getDec();
		assertThat("", v27, instanceOf(ProcDec.class));
		IToken v28 = ((ProcDec) v27).ident;
		assertEquals("p", String.valueOf(v28.getText()));
		int v29 = ((ProcDec) v27).getNest();
		assertEquals(0, v29);
		Statement v30 = ((Block) v0).statement;
		assertThat("", v30, instanceOf(StatementEmpty.class));
	}

	@Test
	void test7() throws PLPException {
		String input = """
				CONST a * b;
				""";
		assertThrows(SyntaxException.class, () -> {
			@SuppressWarnings("unused")
			ASTNode ast = getDecoratedAST(input);
		});
	}

	@Test
	void test8() throws PLPException {
		String input = """
				PROCEDURE 42
				""";
		assertThrows(SyntaxException.class, () -> {
			@SuppressWarnings("unused")
			ASTNode ast = getDecoratedAST(input);
		});
	}

	@Test
	void test9() throws PLPException {
		String input = """
				VAR @;
				""";
		assertThrows(LexicalException.class, () -> {
			@SuppressWarnings("unused")
			ASTNode ast = getDecoratedAST(input);
		});
	}

	@Test
	void test10() throws PLPException {
		String input = """
				! abc
				.
				""";
		assertThrows(ScopeException.class, () -> { // abc not declared
			@SuppressWarnings("unused")
			ASTNode ast = getDecoratedAST(input);
		});
	}

	@Test
	void test11() throws PLPException {
		String input = """
						VAR p;
						PROCEDURE p;
						;
						.
				""";
		assertThrows(ScopeException.class, () -> { // p declared twice in same scope
			@SuppressWarnings("unused")
			ASTNode ast = getDecoratedAST(input);
		});
	}

	@Test
	void test12() throws PLPException {
		String input = """
						CONST a=43;
						VAR a;
											.
				""";
		assertThrows(ScopeException.class, () -> {// a declared twice in same scope
			@SuppressWarnings("unused")
			ASTNode ast = getDecoratedAST(input);
		});
	}
	

@Test
    void paramTest1() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            a := b+c/(a-4)
                        ;
                        .
                        """;
        ASTNode ast = getDecoratedAST(input);
        assertThat("", ast, instanceOf(Program.class));
        Block v0 = ((Program) ast).block;
        assertThat("", v0, instanceOf(Block.class));
        List<ConstDec> v1 = ((Block) v0).constDecs;
        assertEquals(3, v1.size());
        ConstDec v2 = v1.get(0);
        assertThat("", v2, instanceOf(ConstDec.class));
        IToken v3 = ((ConstDec)v2).ident;
        assertEquals("a", String.valueOf(v3.getText()));
        Integer v4 = (Integer)((ConstDec)v2).val;
        assertEquals(1, v4);
        assertEquals(0, v2.getNest());
        ConstDec v6 = v1.get(1);
        assertThat("", v6, instanceOf(ConstDec.class));
        IToken v7 = ((ConstDec)v6).ident;
        assertEquals("b", String.valueOf(v7.getText()));
        Boolean v8 = (Boolean)((ConstDec)v6).val;
        assertEquals(true, v8);
        assertEquals(0, v6.getNest());
        ConstDec v10 = v1.get(2);
        assertThat("", v10, instanceOf(ConstDec.class));
        IToken v11 = ((ConstDec)v10).ident;
        assertEquals("c", String.valueOf(v11.getText()));
        String v12 = (String)((ConstDec)v10).val;
        assertEquals("a", v12);
        assertEquals(0, v10.getNest());
        List<VarDec> v14 = ((Block) v0).varDecs;
        assertEquals(1, v14.size());
        VarDec v15 = v14.get(0);
        assertThat("", v15, instanceOf(VarDec.class));
        IToken v16 = ((VarDec)v15).ident;
        assertEquals("d", String.valueOf(v16.getText()));
        assertEquals(0, v15.getNest());
        List<ProcDec> v17 = ((Block) v0).procedureDecs;
        assertEquals(1, v17.size());
        ProcDec v18 = v17.get(0);
        assertThat("", v18, instanceOf(ProcDec.class));
        IToken v19 = v18.ident;
        assertEquals("p1", String.valueOf(v19.getText()));
        assertEquals(0, v18.getNest());
        Block v20 = v18.block;
        assertThat("", v20, instanceOf(Block.class));
        List<ConstDec> v21 = ((Block) v20).constDecs;
        assertEquals(0, v21.size());
        List<VarDec> v22 = ((Block) v20).varDecs;
        assertEquals(0, v22.size());
        List<ProcDec> v23 = ((Block) v20).procedureDecs;
        assertEquals(0, v23.size());
        Statement v24 = ((Block) v20).statement;
        assertThat("", v24, instanceOf(StatementAssign.class));
        Ident v25 = ((StatementAssign) v24).ident;
        assertThat("", v25, instanceOf(Ident.class));
        assertEquals("a", String.valueOf(v25.firstToken.getText()));
        assertEquals(1, ((Ident) v25).getNest());
        Declaration v26 = ((Ident) v25).getDec();
        IToken v27 = ((ConstDec)v26).ident;
        assertEquals("a", String.valueOf(v27.getText()));
        Integer v28 = (Integer)((ConstDec)v26).val;
        assertEquals(1, v28);
        assertEquals(0, v26.getNest());
        Expression v30 = ((StatementAssign) v24).expression;
        Expression v31 = ((ExpressionBinary) v30).e0;
        assertThat("", v31, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v31.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v31).getNest());
        Declaration v32 = ((ExpressionIdent) v31).getDec();
        IToken v33 = ((ConstDec)v32).ident;
        assertEquals("b", String.valueOf(v33.getText()));
        Boolean v34 = (Boolean)((ConstDec)v32).val;
        assertEquals(true, v34);
        assertEquals(0, v32.getNest());
        assertEquals("+", String.valueOf(((ExpressionBinary) v30).op.getText()));
        Expression v36 = ((ExpressionBinary) v30).e1;
        Expression v37 = ((ExpressionBinary) v36).e0;
        assertThat("", v37, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v37.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v37).getNest());
        Declaration v38 = ((ExpressionIdent) v37).getDec();
        IToken v39 = ((ConstDec)v38).ident;
        assertEquals("c", String.valueOf(v39.getText()));
        String v40 = (String)((ConstDec)v38).val;
        assertEquals("a", v40);
        assertEquals(0, v38.getNest());
        assertEquals("/", String.valueOf(((ExpressionBinary) v36).op.getText()));
        Expression v42 = ((ExpressionBinary) v36).e1;
        Expression v43 = ((ExpressionBinary) v42).e0;
        assertThat("", v43, instanceOf(ExpressionIdent.class));
        assertEquals("a", String.valueOf(v43.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v43).getNest());
        Declaration v44 = ((ExpressionIdent) v43).getDec();
        IToken v45 = ((ConstDec)v44).ident;
        assertEquals("a", String.valueOf(v45.getText()));
        Integer v46 = (Integer)((ConstDec)v44).val;
        assertEquals(1, v46);
        assertEquals(0, v44.getNest());
        assertEquals("-", String.valueOf(((ExpressionBinary) v42).op.getText()));
        Expression v48 = ((ExpressionBinary) v42).e1;
        assertThat("", v48, instanceOf(ExpressionNumLit.class));
        assertEquals("4", String.valueOf(v48.firstToken.getText()));
        Statement v49 = ((Block) v0).statement;
        assertThat("", v49, instanceOf(StatementEmpty.class));
        
    }
    
    @Test
    void paramTest2() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            CALL c
                        ;
                        .
                        """;
        ASTNode ast = getDecoratedAST(input);
        assertThat("", ast, instanceOf(Program.class));
        Block v0 = ((Program) ast).block;
        assertThat("", v0, instanceOf(Block.class));
        List<ConstDec> v1 = ((Block) v0).constDecs;
        assertEquals(3, v1.size());
        ConstDec v2 = v1.get(0);
        assertThat("", v2, instanceOf(ConstDec.class));
        IToken v3 = ((ConstDec)v2).ident;
        assertEquals("a", String.valueOf(v3.getText()));
        Integer v4 = (Integer)((ConstDec)v2).val;
        assertEquals(1, v4);
        assertEquals(0, v2.getNest());
        ConstDec v6 = v1.get(1);
        assertThat("", v6, instanceOf(ConstDec.class));
        IToken v7 = ((ConstDec)v6).ident;
        assertEquals("b", String.valueOf(v7.getText()));
        Boolean v8 = (Boolean)((ConstDec)v6).val;
        assertEquals(true, v8);
        assertEquals(0, v6.getNest());
        ConstDec v10 = v1.get(2);
        assertThat("", v10, instanceOf(ConstDec.class));
        IToken v11 = ((ConstDec)v10).ident;
        assertEquals("c", String.valueOf(v11.getText()));
        String v12 = (String)((ConstDec)v10).val;
        assertEquals("a", v12);
        assertEquals(0, v10.getNest());
        List<VarDec> v14 = ((Block) v0).varDecs;
        assertEquals(1, v14.size());
        VarDec v15 = v14.get(0);
        assertThat("", v15, instanceOf(VarDec.class));
        IToken v16 = ((VarDec)v15).ident;
        assertEquals("d", String.valueOf(v16.getText()));
        assertEquals(0, v15.getNest());
        List<ProcDec> v17 = ((Block) v0).procedureDecs;
        assertEquals(1, v17.size());
        ProcDec v18 = v17.get(0);
        assertThat("", v18, instanceOf(ProcDec.class));
        IToken v19 = v18.ident;
        assertEquals("p1", String.valueOf(v19.getText()));
        assertEquals(0, v18.getNest());
        Block v20 = v18.block;
        assertThat("", v20, instanceOf(Block.class));
        List<ConstDec> v21 = ((Block) v20).constDecs;
        assertEquals(0, v21.size());
        List<VarDec> v22 = ((Block) v20).varDecs;
        assertEquals(0, v22.size());
        List<ProcDec> v23 = ((Block) v20).procedureDecs;
        assertEquals(0, v23.size());
        Statement v24 = ((Block) v20).statement;
        assertThat("", v24, instanceOf(StatementCall.class));
        Ident v25 = ((StatementCall) v24).ident;
        assertThat("", v25, instanceOf(Ident.class));
        assertEquals("c", String.valueOf(v25.firstToken.getText()));
        assertEquals(1, ((Ident) v25).getNest());
        Declaration v26 = ((Ident) v25).getDec();
        IToken v27 = ((ConstDec)v26).ident;
        assertEquals("c", String.valueOf(v27.getText()));
        String v28 = (String)((ConstDec)v26).val;
        assertEquals("a", v28);
        assertEquals(0, v26.getNest());
        Statement v30 = ((Block) v0).statement;
        assertThat("", v30, instanceOf(StatementEmpty.class));
    }
    
    @Test
    void paramTest3() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            ? c
                        ;
                        .
                        """;
        ASTNode ast = getDecoratedAST(input);
        assertThat("", ast, instanceOf(Program.class));
        Block v0 = ((Program) ast).block;
        assertThat("", v0, instanceOf(Block.class));
        List<ConstDec> v1 = ((Block) v0).constDecs;
        assertEquals(3, v1.size());
        ConstDec v2 = v1.get(0);
        assertThat("", v2, instanceOf(ConstDec.class));
        IToken v3 = ((ConstDec)v2).ident;
        assertEquals("a", String.valueOf(v3.getText()));
        Integer v4 = (Integer)((ConstDec)v2).val;
        assertEquals(1, v4);
        assertEquals(0, v2.getNest());
        ConstDec v6 = v1.get(1);
        assertThat("", v6, instanceOf(ConstDec.class));
        IToken v7 = ((ConstDec)v6).ident;
        assertEquals("b", String.valueOf(v7.getText()));
        Boolean v8 = (Boolean)((ConstDec)v6).val;
        assertEquals(true, v8);
        assertEquals(0, v6.getNest());
        ConstDec v10 = v1.get(2);
        assertThat("", v10, instanceOf(ConstDec.class));
        IToken v11 = ((ConstDec)v10).ident;
        assertEquals("c", String.valueOf(v11.getText()));
        String v12 = (String)((ConstDec)v10).val;
        assertEquals("a", v12);
        assertEquals(0, v10.getNest());
        List<VarDec> v14 = ((Block) v0).varDecs;
        assertEquals(1, v14.size());
        VarDec v15 = v14.get(0);
        assertThat("", v15, instanceOf(VarDec.class));
        IToken v16 = ((VarDec)v15).ident;
        assertEquals("d", String.valueOf(v16.getText()));
        assertEquals(0, v15.getNest());
        List<ProcDec> v17 = ((Block) v0).procedureDecs;
        assertEquals(1, v17.size());
        ProcDec v18 = v17.get(0);
        assertThat("", v18, instanceOf(ProcDec.class));
        IToken v19 = v18.ident;
        assertEquals("p1", String.valueOf(v19.getText()));
        assertEquals(0, v18.getNest());
        Block v20 = v18.block;
        assertThat("", v20, instanceOf(Block.class));
        List<ConstDec> v21 = ((Block) v20).constDecs;
        assertEquals(0, v21.size());
        List<VarDec> v22 = ((Block) v20).varDecs;
        assertEquals(0, v22.size());
        List<ProcDec> v23 = ((Block) v20).procedureDecs;
        assertEquals(0, v23.size());
        Statement v24 = ((Block) v20).statement;
        assertThat("", v24, instanceOf(StatementInput.class));
        Ident v25 = ((StatementInput)v24).ident;
        assertThat("", v25, instanceOf(Ident.class));
        assertEquals("c", String.valueOf(v25.firstToken.getText()));
        assertEquals(1, ((Ident) v25).getNest());
        Declaration v26 = ((Ident) v25).getDec();
        IToken v27 = ((ConstDec)v26).ident;
        assertEquals("c", String.valueOf(v27.getText()));
        String v28 = (String)((ConstDec)v26).val;
        assertEquals("a", v28);
        assertEquals(0, v26.getNest());
        Statement v30 = ((Block) v0).statement;
        assertThat("", v30, instanceOf(StatementEmpty.class));
    }
    
    @Test
    void paramTest4() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            ! c
                        ;
                        .
                        """;
        ASTNode ast = getDecoratedAST(input);
        assertThat("", ast, instanceOf(Program.class));
        Block v0 = ((Program) ast).block;
        assertThat("", v0, instanceOf(Block.class));
        List<ConstDec> v1 = ((Block) v0).constDecs;
        assertEquals(3, v1.size());
        ConstDec v2 = v1.get(0);
        assertThat("", v2, instanceOf(ConstDec.class));
        IToken v3 = ((ConstDec)v2).ident;
        assertEquals("a", String.valueOf(v3.getText()));
        Integer v4 = (Integer)((ConstDec)v2).val;
        assertEquals(1, v4);
        assertEquals(0, v2.getNest());
        ConstDec v6 = v1.get(1);
        assertThat("", v6, instanceOf(ConstDec.class));
        IToken v7 = ((ConstDec)v6).ident;
        assertEquals("b", String.valueOf(v7.getText()));
        Boolean v8 = (Boolean)((ConstDec)v6).val;
        assertEquals(true, v8);
        assertEquals(0, v6.getNest());
        ConstDec v10 = v1.get(2);
        assertThat("", v10, instanceOf(ConstDec.class));
        IToken v11 = ((ConstDec)v10).ident;
        assertEquals("c", String.valueOf(v11.getText()));
        String v12 = (String)((ConstDec)v10).val;
        assertEquals("a", v12);
        assertEquals(0, v10.getNest());
        List<VarDec> v14 = ((Block) v0).varDecs;
        assertEquals(1, v14.size());
        VarDec v15 = v14.get(0);
        assertThat("", v15, instanceOf(VarDec.class));
        IToken v16 = ((VarDec)v15).ident;
        assertEquals("d", String.valueOf(v16.getText()));
        assertEquals(0, v15.getNest());
        List<ProcDec> v17 = ((Block) v0).procedureDecs;
        assertEquals(1, v17.size());
        ProcDec v18 = v17.get(0);
        assertThat("", v18, instanceOf(ProcDec.class));
        IToken v19 = v18.ident;
        assertEquals("p1", String.valueOf(v19.getText()));
        assertEquals(0, v18.getNest());
        Block v20 = v18.block;
        assertThat("", v20, instanceOf(Block.class));
        List<ConstDec> v21 = ((Block) v20).constDecs;
        assertEquals(0, v21.size());
        List<VarDec> v22 = ((Block) v20).varDecs;
        assertEquals(0, v22.size());
        List<ProcDec> v23 = ((Block) v20).procedureDecs;
        assertEquals(0, v23.size());
        Statement v24 = ((Block) v20).statement;
        assertThat("", v24, instanceOf(StatementOutput.class));
        Expression v25 = ((StatementOutput) v24).expression;
        assertThat("", v25, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v25.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v25).getNest());
        Declaration v26 = ((ExpressionIdent) v25).getDec();
        IToken v27 = ((ConstDec)v26).ident;
        assertEquals("c", String.valueOf(v27.getText()));
        String v28 = (String)((ConstDec)v26).val;
        assertEquals("a", v28);
        assertEquals(0, v26.getNest());
        Statement v30 = ((Block) v0).statement;
        assertThat("", v30, instanceOf(StatementEmpty.class));
    }
    
    @Test
    void paramTest5() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            BEGIN
                                ! c;
                                b := c;
                                c := b-d>=TRUE
                            END
                        ;
                        .
                        """;
        ASTNode ast = getDecoratedAST(input);
        assertThat("", ast, instanceOf(Program.class));
        Block v0 = ((Program) ast).block;
        assertThat("", v0, instanceOf(Block.class));
        List<ConstDec> v1 = ((Block) v0).constDecs;
        assertEquals(3, v1.size());
        ConstDec v2 = v1.get(0);
        assertThat("", v2, instanceOf(ConstDec.class));
        IToken v3 = ((ConstDec)v2).ident;
        assertEquals("a", String.valueOf(v3.getText()));
        Integer v4 = (Integer)((ConstDec)v2).val;
        assertEquals(1, v4);
        assertEquals(0, v2.getNest());
        ConstDec v6 = v1.get(1);
        assertThat("", v6, instanceOf(ConstDec.class));
        IToken v7 = ((ConstDec)v6).ident;
        assertEquals("b", String.valueOf(v7.getText()));
        Boolean v8 = (Boolean)((ConstDec)v6).val;
        assertEquals(true, v8);
        assertEquals(0, v6.getNest());
        ConstDec v10 = v1.get(2);
        assertThat("", v10, instanceOf(ConstDec.class));
        IToken v11 = ((ConstDec)v10).ident;
        assertEquals("c", String.valueOf(v11.getText()));
        String v12 = (String)((ConstDec)v10).val;
        assertEquals("a", v12);
        assertEquals(0, v10.getNest());
        List<VarDec> v14 = ((Block) v0).varDecs;
        assertEquals(1, v14.size());
        VarDec v15 = v14.get(0);
        assertThat("", v15, instanceOf(VarDec.class));
        IToken v16 = ((VarDec)v15).ident;
        assertEquals("d", String.valueOf(v16.getText()));
        assertEquals(0, v15.getNest());
        List<ProcDec> v17 = ((Block) v0).procedureDecs;
        assertEquals(1, v17.size());
        ProcDec v18 = v17.get(0);
        assertThat("", v18, instanceOf(ProcDec.class));
        IToken v19 = v18.ident;
        assertEquals("p1", String.valueOf(v19.getText()));
        assertEquals(0, v18.getNest());
        Block v20 = v18.block;
        assertThat("", v20, instanceOf(Block.class));
        List<ConstDec> v21 = ((Block) v20).constDecs;
        assertEquals(0, v21.size());
        List<VarDec> v22 = ((Block) v20).varDecs;
        assertEquals(0, v22.size());
        List<ProcDec> v23 = ((Block) v20).procedureDecs;
        assertEquals(0, v23.size());
        Statement v24 = ((Block) v20).statement;
        assertThat("", v24, instanceOf(StatementBlock.class));
        Statement v25 = ((StatementBlock) v24).statements.get(0);
        assertThat("", v25, instanceOf(StatementOutput.class));
        Expression v26 = ((StatementOutput) v25).expression;
        assertThat("", v26, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v26.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v26).getNest());
        Declaration v27 = ((ExpressionIdent) v26).getDec();
        IToken v28 = ((ConstDec)v27).ident;
        assertEquals("c", String.valueOf(v28.getText()));
        String v29 = (String)((ConstDec)v27).val;
        assertEquals("a", v29);
        assertEquals(0, v27.getNest());
        Statement v31 = ((StatementBlock) v24).statements.get(1);
        assertThat("", v31, instanceOf(StatementAssign.class));
        Ident v32 = ((StatementAssign) v31).ident;
        assertThat("", v32, instanceOf(Ident.class));
        assertEquals("b", String.valueOf(v32.firstToken.getText()));
        assertEquals(1, ((Ident) v32).getNest());
        Declaration v33 = ((Ident) v32).getDec();
        IToken v34 = ((ConstDec)v33).ident;
        assertEquals("b", String.valueOf(v34.getText()));
        Boolean v35 = (Boolean)((ConstDec)v33).val;
        assertEquals(true, v35);
        assertEquals(0, v33.getNest());
        Expression v37 = ((StatementAssign) v31).expression;
        assertThat("", v37, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v37.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v37).getNest());
        Declaration v38 = ((ExpressionIdent) v37).getDec();
        IToken v39 = ((ConstDec)v38).ident;
        assertEquals("c", String.valueOf(v39.getText()));
        String v40 = (String)((ConstDec)v38).val;
        assertEquals("a", v40);
        assertEquals(0, v38.getNest());
        Statement v42 = ((StatementBlock) v24).statements.get(2);
        assertThat("", v42, instanceOf(StatementAssign.class));
        Ident v43 = ((StatementAssign) v42).ident;
        assertThat("", v43, instanceOf(Ident.class));
        assertEquals("c", String.valueOf(v43.firstToken.getText()));
        assertEquals(1, ((Ident) v43).getNest());
        Declaration v44 = ((Ident) v43).getDec();
        IToken v45 = ((ConstDec)v44).ident;
        assertEquals("c", String.valueOf(v45.getText()));
        String v46 = (String)((ConstDec)v44).val;
        assertEquals("a", v46);
        assertEquals(0, v44.getNest());
        Expression v48 = ((StatementAssign) v42).expression;
        Expression v49 = ((ExpressionBinary) v48).e0;
        Expression v50 = ((ExpressionBinary) v49).e0;
        assertThat("", v50, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v50.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v50).getNest());
        Declaration v51 = ((ExpressionIdent) v50).getDec();
        IToken v52 = ((ConstDec)v51).ident;
        assertEquals("b", String.valueOf(v52.getText()));
        Boolean v53 = (Boolean)((ConstDec)v51).val;
        assertEquals(true, v53);
        assertEquals(0, v51.getNest());
        assertEquals("-", String.valueOf(((ExpressionBinary) v49).op.getText()));
        Expression v55 = ((ExpressionBinary) v49).e1;
        assertThat("", v55, instanceOf(ExpressionIdent.class));
        assertEquals("d", String.valueOf(v55.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v55).getNest());
        Declaration v56 = ((ExpressionIdent) v55).getDec();
        IToken v57 = ((VarDec)v56).ident;
        assertEquals("d", String.valueOf(v57.getText()));
        assertEquals(0, v56.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v48).op.getText()));
        Expression v58 = ((ExpressionBinary) v48).e1;
        assertThat("", v58, instanceOf(ExpressionBooleanLit.class));
        assertEquals("TRUE", String.valueOf(v58.firstToken.getText()));
        Statement v59 = ((Block) v0).statement;
        assertThat("", v59, instanceOf(StatementEmpty.class));
    }
    
    @Test
    void paramTest6() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            IF a>=b+(3-2) THEN
                                BEGIN
                                    ! c;
                                    b := c;
                                    c := b-d>=TRUE
                                END
                        ;
                        .
                        """;
        ASTNode ast = getDecoratedAST(input);
        assertThat("", ast, instanceOf(Program.class));
        Block v0 = ((Program) ast).block;
        assertThat("", v0, instanceOf(Block.class));
        List<ConstDec> v1 = ((Block) v0).constDecs;
        assertEquals(3, v1.size());
        ConstDec v2 = v1.get(0);
        assertThat("", v2, instanceOf(ConstDec.class));
        IToken v3 = ((ConstDec)v2).ident;
        assertEquals("a", String.valueOf(v3.getText()));
        Integer v4 = (Integer)((ConstDec)v2).val;
        assertEquals(1, v4);
        assertEquals(0, v2.getNest());
        ConstDec v6 = v1.get(1);
        assertThat("", v6, instanceOf(ConstDec.class));
        IToken v7 = ((ConstDec)v6).ident;
        assertEquals("b", String.valueOf(v7.getText()));
        Boolean v8 = (Boolean)((ConstDec)v6).val;
        assertEquals(true, v8);
        assertEquals(0, v6.getNest());
        ConstDec v10 = v1.get(2);
        assertThat("", v10, instanceOf(ConstDec.class));
        IToken v11 = ((ConstDec)v10).ident;
        assertEquals("c", String.valueOf(v11.getText()));
        String v12 = (String)((ConstDec)v10).val;
        assertEquals("a", v12);
        assertEquals(0, v10.getNest());
        List<VarDec> v14 = ((Block) v0).varDecs;
        assertEquals(1, v14.size());
        VarDec v15 = v14.get(0);
        assertThat("", v15, instanceOf(VarDec.class));
        IToken v16 = ((VarDec)v15).ident;
        assertEquals("d", String.valueOf(v16.getText()));
        assertEquals(0, v15.getNest());
        List<ProcDec> v17 = ((Block) v0).procedureDecs;
        assertEquals(1, v17.size());
        ProcDec v18 = v17.get(0);
        assertThat("", v18, instanceOf(ProcDec.class));
        IToken v19 = v18.ident;
        assertEquals("p1", String.valueOf(v19.getText()));
        assertEquals(0, v18.getNest());
        Block v20 = v18.block;
        assertThat("", v20, instanceOf(Block.class));
        List<ConstDec> v21 = ((Block) v20).constDecs;
        assertEquals(0, v21.size());
        List<VarDec> v22 = ((Block) v20).varDecs;
        assertEquals(0, v22.size());
        List<ProcDec> v23 = ((Block) v20).procedureDecs;
        assertEquals(0, v23.size());
        Statement v24 = ((Block) v20).statement;
        Expression v25 = ((StatementIf) v24).expression;
        Expression v26 = ((ExpressionBinary) v25).e0;
        assertThat("", v26, instanceOf(ExpressionIdent.class));
        assertEquals("a", String.valueOf(v26.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v26).getNest());
        Declaration v27 = ((ExpressionIdent) v26).getDec();
        IToken v28 = ((ConstDec)v27).ident;
        assertEquals("a", String.valueOf(v28.getText()));
        Integer v29 = (Integer)((ConstDec)v27).val;
        assertEquals(1, v29);
        assertEquals(0, v27.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v25).op.getText()));
        Expression v31 = ((ExpressionBinary) v25).e1;
        Expression v32 = ((ExpressionBinary) v31).e0;
        assertThat("", v32, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v32.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v32).getNest());
        Declaration v33 = ((ExpressionIdent) v32).getDec();
        IToken v34 = ((ConstDec)v33).ident;
        assertEquals("b", String.valueOf(v34.getText()));
        Boolean v35 = (Boolean)((ConstDec)v33).val;
        assertEquals(true, v35);
        assertEquals(0, v33.getNest());
        assertEquals("+", String.valueOf(((ExpressionBinary) v31).op.getText()));
        Expression v37 = ((ExpressionBinary) v31).e1;
        Expression v38 = ((ExpressionBinary) v37).e0;
        assertThat("", v38, instanceOf(ExpressionNumLit.class));
        assertEquals("3", String.valueOf(v38.firstToken.getText()));
        assertEquals("-", String.valueOf(((ExpressionBinary) v37).op.getText()));
        Expression v39 = ((ExpressionBinary) v37).e1;
        assertThat("", v39, instanceOf(ExpressionNumLit.class));
        assertEquals("2", String.valueOf(v39.firstToken.getText()));
        Statement v40 = ((StatementIf) v24).statement;
        assertThat("", v40, instanceOf(StatementBlock.class));
        Statement v41 = ((StatementBlock) v40).statements.get(0);
        assertThat("", v41, instanceOf(StatementOutput.class));
        Expression v42 = ((StatementOutput) v41).expression;
        assertThat("", v42, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v42.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v42).getNest());
        Declaration v43 = ((ExpressionIdent) v42).getDec();
        IToken v44 = ((ConstDec)v43).ident;
        assertEquals("c", String.valueOf(v44.getText()));
        String v45 = (String)((ConstDec)v43).val;
        assertEquals("a", v45);
        assertEquals(0, v43.getNest());
        Statement v47 = ((StatementBlock) v40).statements.get(1);
        assertThat("", v47, instanceOf(StatementAssign.class));
        Ident v48 = ((StatementAssign) v47).ident;
        assertThat("", v48, instanceOf(Ident.class));
        assertEquals("b", String.valueOf(v48.firstToken.getText()));
        assertEquals(1, ((Ident) v48).getNest());
        Declaration v49 = ((Ident) v48).getDec();
        IToken v50 = ((ConstDec)v49).ident;
        assertEquals("b", String.valueOf(v50.getText()));
        Boolean v51 = (Boolean)((ConstDec)v49).val;
        assertEquals(true, v51);
        assertEquals(0, v49.getNest());
        Expression v53 = ((StatementAssign) v47).expression;
        assertThat("", v53, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v53.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v53).getNest());
        Declaration v54 = ((ExpressionIdent) v53).getDec();
        IToken v55 = ((ConstDec)v54).ident;
        assertEquals("c", String.valueOf(v55.getText()));
        String v56 = (String)((ConstDec)v54).val;
        assertEquals("a", v56);
        assertEquals(0, v54.getNest());
        Statement v58 = ((StatementBlock) v40).statements.get(2);
        assertThat("", v58, instanceOf(StatementAssign.class));
        Ident v59 = ((StatementAssign) v58).ident;
        assertThat("", v59, instanceOf(Ident.class));
        assertEquals("c", String.valueOf(v59.firstToken.getText()));
        assertEquals(1, ((Ident) v59).getNest());
        Declaration v60 = ((Ident) v59).getDec();
        IToken v61 = ((ConstDec)v60).ident;
        assertEquals("c", String.valueOf(v61.getText()));
        String v62 = (String)((ConstDec)v60).val;
        assertEquals("a", v62);
        assertEquals(0, v60.getNest());
        Expression v64 = ((StatementAssign) v58).expression;
        Expression v65 = ((ExpressionBinary) v64).e0;
        Expression v66 = ((ExpressionBinary) v65).e0;
        assertThat("", v66, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v66.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v66).getNest());
        Declaration v67 = ((ExpressionIdent) v66).getDec();
        IToken v68 = ((ConstDec)v67).ident;
        assertEquals("b", String.valueOf(v68.getText()));
        Boolean v69 = (Boolean)((ConstDec)v67).val;
        assertEquals(true, v69);
        assertEquals(0, v67.getNest());
        assertEquals("-", String.valueOf(((ExpressionBinary) v65).op.getText()));
        Expression v71 = ((ExpressionBinary) v65).e1;
        assertThat("", v71, instanceOf(ExpressionIdent.class));
        assertEquals("d", String.valueOf(v71.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v71).getNest());
        Declaration v72 = ((ExpressionIdent) v71).getDec();
        IToken v73 = ((VarDec)v72).ident;
        assertEquals("d", String.valueOf(v73.getText()));
        assertEquals(0, v72.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v64).op.getText()));
        Expression v74 = ((ExpressionBinary) v64).e1;
        assertThat("", v74, instanceOf(ExpressionBooleanLit.class));
        assertEquals("TRUE", String.valueOf(v74.firstToken.getText()));
        Statement v75 = ((Block) v0).statement;
        assertThat("", v75, instanceOf(StatementEmpty.class));
    }
    
    @Test
    void paramTest7() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            WHILE a>=b+(3-2) DO
                                BEGIN
                                    ! c;
                                    b := c;
                                    c := b-d>=TRUE
                                END
                        ;
                        .
                        """;
        ASTNode ast = getDecoratedAST(input);
        assertThat("", ast, instanceOf(Program.class));
        Block v0 = ((Program) ast).block;
        assertThat("", v0, instanceOf(Block.class));
        List<ConstDec> v1 = ((Block) v0).constDecs;
        assertEquals(3, v1.size());
        ConstDec v2 = v1.get(0);
        assertThat("", v2, instanceOf(ConstDec.class));
        IToken v3 = ((ConstDec)v2).ident;
        assertEquals("a", String.valueOf(v3.getText()));
        Integer v4 = (Integer)((ConstDec)v2).val;
        assertEquals(1, v4);
        assertEquals(0, v2.getNest());
        ConstDec v6 = v1.get(1);
        assertThat("", v6, instanceOf(ConstDec.class));
        IToken v7 = ((ConstDec)v6).ident;
        assertEquals("b", String.valueOf(v7.getText()));
        Boolean v8 = (Boolean)((ConstDec)v6).val;
        assertEquals(true, v8);
        assertEquals(0, v6.getNest());
        ConstDec v10 = v1.get(2);
        assertThat("", v10, instanceOf(ConstDec.class));
        IToken v11 = ((ConstDec)v10).ident;
        assertEquals("c", String.valueOf(v11.getText()));
        String v12 = (String)((ConstDec)v10).val;
        assertEquals("a", v12);
        assertEquals(0, v10.getNest());
        List<VarDec> v14 = ((Block) v0).varDecs;
        assertEquals(1, v14.size());
        VarDec v15 = v14.get(0);
        assertThat("", v15, instanceOf(VarDec.class));
        IToken v16 = ((VarDec)v15).ident;
        assertEquals("d", String.valueOf(v16.getText()));
        assertEquals(0, v15.getNest());
        List<ProcDec> v17 = ((Block) v0).procedureDecs;
        assertEquals(1, v17.size());
        ProcDec v18 = v17.get(0);
        assertThat("", v18, instanceOf(ProcDec.class));
        IToken v19 = v18.ident;
        assertEquals("p1", String.valueOf(v19.getText()));
        assertEquals(0, v18.getNest());
        Block v20 = v18.block;
        assertThat("", v20, instanceOf(Block.class));
        List<ConstDec> v21 = ((Block) v20).constDecs;
        assertEquals(0, v21.size());
        List<VarDec> v22 = ((Block) v20).varDecs;
        assertEquals(0, v22.size());
        List<ProcDec> v23 = ((Block) v20).procedureDecs;
        assertEquals(0, v23.size());
        Statement v24 = ((Block) v20).statement;
        Expression v25 = ((StatementWhile) v24).expression;
        Expression v26 = ((ExpressionBinary) v25).e0;
        assertThat("", v26, instanceOf(ExpressionIdent.class));
        assertEquals("a", String.valueOf(v26.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v26).getNest());
        Declaration v27 = ((ExpressionIdent) v26).getDec();
        IToken v28 = ((ConstDec)v27).ident;
        assertEquals("a", String.valueOf(v28.getText()));
        Integer v29 = (Integer)((ConstDec)v27).val;
        assertEquals(1, v29);
        assertEquals(0, v27.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v25).op.getText()));
        Expression v31 = ((ExpressionBinary) v25).e1;
        Expression v32 = ((ExpressionBinary) v31).e0;
        assertThat("", v32, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v32.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v32).getNest());
        Declaration v33 = ((ExpressionIdent) v32).getDec();
        IToken v34 = ((ConstDec)v33).ident;
        assertEquals("b", String.valueOf(v34.getText()));
        Boolean v35 = (Boolean)((ConstDec)v33).val;
        assertEquals(true, v35);
        assertEquals(0, v33.getNest());
        assertEquals("+", String.valueOf(((ExpressionBinary) v31).op.getText()));
        Expression v37 = ((ExpressionBinary) v31).e1;
        Expression v38 = ((ExpressionBinary) v37).e0;
        assertThat("", v38, instanceOf(ExpressionNumLit.class));
        assertEquals("3", String.valueOf(v38.firstToken.getText()));
        assertEquals("-", String.valueOf(((ExpressionBinary) v37).op.getText()));
        Expression v39 = ((ExpressionBinary) v37).e1;
        assertThat("", v39, instanceOf(ExpressionNumLit.class));
        assertEquals("2", String.valueOf(v39.firstToken.getText()));
        Statement v40 = ((StatementWhile) v24).statement;
        assertThat("", v40, instanceOf(StatementBlock.class));
        Statement v41 = ((StatementBlock) v40).statements.get(0);
        assertThat("", v41, instanceOf(StatementOutput.class));
        Expression v42 = ((StatementOutput) v41).expression;
        assertThat("", v42, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v42.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v42).getNest());
        Declaration v43 = ((ExpressionIdent) v42).getDec();
        IToken v44 = ((ConstDec)v43).ident;
        assertEquals("c", String.valueOf(v44.getText()));
        String v45 = (String)((ConstDec)v43).val;
        assertEquals("a", v45);
        assertEquals(0, v43.getNest());
        Statement v47 = ((StatementBlock) v40).statements.get(1);
        assertThat("", v47, instanceOf(StatementAssign.class));
        Ident v48 = ((StatementAssign) v47).ident;
        assertThat("", v48, instanceOf(Ident.class));
        assertEquals("b", String.valueOf(v48.firstToken.getText()));
        assertEquals(1, ((Ident) v48).getNest());
        Declaration v49 = ((Ident) v48).getDec();
        IToken v50 = ((ConstDec)v49).ident;
        assertEquals("b", String.valueOf(v50.getText()));
        Boolean v51 = (Boolean)((ConstDec)v49).val;
        assertEquals(true, v51);
        assertEquals(0, v49.getNest());
        Expression v53 = ((StatementAssign) v47).expression;
        assertThat("", v53, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v53.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v53).getNest());
        Declaration v54 = ((ExpressionIdent) v53).getDec();
        IToken v55 = ((ConstDec)v54).ident;
        assertEquals("c", String.valueOf(v55.getText()));
        String v56 = (String)((ConstDec)v54).val;
        assertEquals("a", v56);
        assertEquals(0, v54.getNest());
        Statement v58 = ((StatementBlock) v40).statements.get(2);
        assertThat("", v58, instanceOf(StatementAssign.class));
        Ident v59 = ((StatementAssign) v58).ident;
        assertThat("", v59, instanceOf(Ident.class));
        assertEquals("c", String.valueOf(v59.firstToken.getText()));
        assertEquals(1, ((Ident) v59).getNest());
        Declaration v60 = ((Ident) v59).getDec();
        IToken v61 = ((ConstDec)v60).ident;
        assertEquals("c", String.valueOf(v61.getText()));
        String v62 = (String)((ConstDec)v60).val;
        assertEquals("a", v62);
        assertEquals(0, v60.getNest());
        Expression v64 = ((StatementAssign) v58).expression;
        Expression v65 = ((ExpressionBinary) v64).e0;
        Expression v66 = ((ExpressionBinary) v65).e0;
        assertThat("", v66, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v66.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v66).getNest());
        Declaration v67 = ((ExpressionIdent) v66).getDec();
        IToken v68 = ((ConstDec)v67).ident;
        assertEquals("b", String.valueOf(v68.getText()));
        Boolean v69 = (Boolean)((ConstDec)v67).val;
        assertEquals(true, v69);
        assertEquals(0, v67.getNest());
        assertEquals("-", String.valueOf(((ExpressionBinary) v65).op.getText()));
        Expression v71 = ((ExpressionBinary) v65).e1;
        assertThat("", v71, instanceOf(ExpressionIdent.class));
        assertEquals("d", String.valueOf(v71.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v71).getNest());
        Declaration v72 = ((ExpressionIdent) v71).getDec();
        IToken v73 = ((VarDec)v72).ident;
        assertEquals("d", String.valueOf(v73.getText()));
        assertEquals(0, v72.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v64).op.getText()));
        Expression v74 = ((ExpressionBinary) v64).e1;
        assertThat("", v74, instanceOf(ExpressionBooleanLit.class));
        assertEquals("TRUE", String.valueOf(v74.firstToken.getText()));
        Statement v75 = ((Block) v0).statement;
        assertThat("", v75, instanceOf(StatementEmpty.class));
    }
    
    @Test
    void paramTest8() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            CONST d="n", e=5;
                            VAR b;
                            PROCEDURE p2;
                                a := b+c/(a-4)/e+d
                            ;
                            a := b+c/(a-4)/d
                        ;
                        .
                        """;
        ASTNode ast = getDecoratedAST(input);
        assertThat("", ast, instanceOf(Program.class));
        Block v0 = ((Program) ast).block;
        assertThat("", v0, instanceOf(Block.class));
        List<ConstDec> v1 = ((Block) v0).constDecs;
        assertEquals(3, v1.size());
        ConstDec v2 = v1.get(0);
        assertThat("", v2, instanceOf(ConstDec.class));
        IToken v3 = ((ConstDec)v2).ident;
        assertEquals("a", String.valueOf(v3.getText()));
        Integer v4 = (Integer)((ConstDec)v2).val;
        assertEquals(1, v4);
        assertEquals(0, v2.getNest());
        ConstDec v6 = v1.get(1);
        assertThat("", v6, instanceOf(ConstDec.class));
        IToken v7 = ((ConstDec)v6).ident;
        assertEquals("b", String.valueOf(v7.getText()));
        Boolean v8 = (Boolean)((ConstDec)v6).val;
        assertEquals(true, v8);
        assertEquals(0, v6.getNest());
        ConstDec v10 = v1.get(2);
        assertThat("", v10, instanceOf(ConstDec.class));
        IToken v11 = ((ConstDec)v10).ident;
        assertEquals("c", String.valueOf(v11.getText()));
        String v12 = (String)((ConstDec)v10).val;
        assertEquals("a", v12);
        assertEquals(0, v10.getNest());
        List<VarDec> v14 = ((Block) v0).varDecs;
        assertEquals(1, v14.size());
        VarDec v15 = v14.get(0);
        assertThat("", v15, instanceOf(VarDec.class));
        IToken v16 = ((VarDec)v15).ident;
        assertEquals("d", String.valueOf(v16.getText()));
        assertEquals(0, v15.getNest());
        List<ProcDec> v17 = ((Block) v0).procedureDecs;
        assertEquals(1, v17.size());
        ProcDec v18 = v17.get(0);
        assertThat("", v18, instanceOf(ProcDec.class));
        IToken v19 = v18.ident;
        assertEquals("p1", String.valueOf(v19.getText()));
        assertEquals(0, v18.getNest());
        Block v20 = v18.block;
        assertThat("", v20, instanceOf(Block.class));
        List<ConstDec> v21 = ((Block) v20).constDecs;
        assertEquals(2, v21.size());
        ConstDec v22 = v21.get(0);
        assertThat("", v22, instanceOf(ConstDec.class));
        IToken v23 = ((ConstDec)v22).ident;
        assertEquals("d", String.valueOf(v23.getText()));
        String v24 = (String)((ConstDec)v22).val;
        assertEquals("n", v24);
        assertEquals(1, v22.getNest());
        ConstDec v26 = v21.get(1);
        assertThat("", v26, instanceOf(ConstDec.class));
        IToken v27 = ((ConstDec)v26).ident;
        assertEquals("e", String.valueOf(v27.getText()));
        Integer v28 = (Integer)((ConstDec)v26).val;
        assertEquals(5, v28);
        assertEquals(1, v26.getNest());
        List<VarDec> v30 = ((Block) v20).varDecs;
        assertEquals(1, v30.size());
        VarDec v31 = v30.get(0);
        assertThat("", v31, instanceOf(VarDec.class));
        IToken v32 = ((VarDec)v31).ident;
        assertEquals("b", String.valueOf(v32.getText()));
        assertEquals(1, v31.getNest());
        List<ProcDec> v33 = ((Block) v20).procedureDecs;
        assertEquals(1, v33.size());
        ProcDec v34 = v33.get(0);
        assertThat("", v34, instanceOf(ProcDec.class));
        IToken v35 = v34.ident;
        assertEquals("p2", String.valueOf(v35.getText()));
        assertEquals(1, v34.getNest());
        Block v36 = v34.block;
        assertThat("", v36, instanceOf(Block.class));
        List<ConstDec> v37 = ((Block) v36).constDecs;
        assertEquals(0, v37.size());
        List<VarDec> v38 = ((Block) v36).varDecs;
        assertEquals(0, v38.size());
        List<ProcDec> v39 = ((Block) v36).procedureDecs;
        assertEquals(0, v39.size());
        Statement v40 = ((Block) v36).statement;
        assertThat("", v40, instanceOf(StatementAssign.class));
        Ident v41 = ((StatementAssign) v40).ident;
        assertThat("", v41, instanceOf(Ident.class));
        assertEquals("a", String.valueOf(v41.firstToken.getText()));
        assertEquals(2, ((Ident) v41).getNest());
        Declaration v42 = ((Ident) v41).getDec();
        IToken v43 = ((ConstDec)v42).ident;
        assertEquals("a", String.valueOf(v43.getText()));
        Integer v44 = (Integer)((ConstDec)v42).val;
        assertEquals(1, v44);
        assertEquals(0, v42.getNest());
        Expression v46 = ((StatementAssign) v40).expression;
        Expression v47 = ((ExpressionBinary) v46).e0;
        Expression v48 = ((ExpressionBinary) v47).e0;
        assertThat("", v48, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v48.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v48).getNest());
        Declaration v49 = ((ExpressionIdent) v48).getDec();
        IToken v50 = ((VarDec)v49).ident;
        assertEquals("b", String.valueOf(v50.getText()));
        assertEquals(1, v49.getNest());
        assertEquals("+", String.valueOf(((ExpressionBinary) v47).op.getText()));
        Expression v51 = ((ExpressionBinary) v47).e1;
        Expression v52 = ((ExpressionBinary) v51).e0;
        Expression v53 = ((ExpressionBinary) v52).e0;
        assertThat("", v53, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v53.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v53).getNest());
        Declaration v54 = ((ExpressionIdent) v53).getDec();
        IToken v55 = ((ConstDec)v54).ident;
        assertEquals("c", String.valueOf(v55.getText()));
        String v56 = (String)((ConstDec)v54).val;
        assertEquals("a", v56);
        assertEquals(0, v54.getNest());
        assertEquals("/", String.valueOf(((ExpressionBinary) v52).op.getText()));
        Expression v58 = ((ExpressionBinary) v52).e1;
        Expression v59 = ((ExpressionBinary) v58).e0;
        assertThat("", v59, instanceOf(ExpressionIdent.class));
        assertEquals("a", String.valueOf(v59.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v59).getNest());
        Declaration v60 = ((ExpressionIdent) v59).getDec();
        IToken v61 = ((ConstDec)v60).ident;
        assertEquals("a", String.valueOf(v61.getText()));
        Integer v62 = (Integer)((ConstDec)v60).val;
        assertEquals(1, v62);
        assertEquals(0, v60.getNest());
        assertEquals("-", String.valueOf(((ExpressionBinary) v58).op.getText()));
        Expression v64 = ((ExpressionBinary) v58).e1;
        assertThat("", v64, instanceOf(ExpressionNumLit.class));
        assertEquals("4", String.valueOf(v64.firstToken.getText()));
        assertEquals("/", String.valueOf(((ExpressionBinary) v51).op.getText()));
        Expression v65 = ((ExpressionBinary) v51).e1;
        assertThat("", v65, instanceOf(ExpressionIdent.class));
        assertEquals("e", String.valueOf(v65.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v65).getNest());
        Declaration v66 = ((ExpressionIdent) v65).getDec();
        IToken v67 = ((ConstDec)v66).ident;
        assertEquals("e", String.valueOf(v67.getText()));
        Integer v68 = (Integer)((ConstDec)v66).val;
        assertEquals(5, v68);
        assertEquals(1, v66.getNest());
        assertEquals("+", String.valueOf(((ExpressionBinary) v46).op.getText()));
        Expression v70 = ((ExpressionBinary) v46).e1;
        assertThat("", v70, instanceOf(ExpressionIdent.class));
        assertEquals("d", String.valueOf(v70.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v70).getNest());
        Declaration v71 = ((ExpressionIdent) v70).getDec();
        IToken v72 = ((ConstDec)v71).ident;
        assertEquals("d", String.valueOf(v72.getText()));
        String v73 = (String)((ConstDec)v71).val;
        assertEquals("n", v73);
        assertEquals(1, v71.getNest());
        Statement v75 = ((Block) v20).statement;
        assertThat("", v75, instanceOf(StatementAssign.class));
        Ident v76 = ((StatementAssign) v75).ident;
        assertThat("", v76, instanceOf(Ident.class));
        assertEquals("a", String.valueOf(v76.firstToken.getText()));
        assertEquals(1, ((Ident) v76).getNest());
        Declaration v77 = ((Ident) v76).getDec();
        IToken v78 = ((ConstDec)v77).ident;
        assertEquals("a", String.valueOf(v78.getText()));
        Integer v79 = (Integer)((ConstDec)v77).val;
        assertEquals(1, v79);
        assertEquals(0, v77.getNest());
        Expression v81 = ((StatementAssign) v75).expression;
        Expression v82 = ((ExpressionBinary) v81).e0;
        assertThat("", v82, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v82.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v82).getNest());
        Declaration v83 = ((ExpressionIdent) v82).getDec();
        IToken v84 = ((VarDec)v83).ident;
        assertEquals("b", String.valueOf(v84.getText()));
        assertEquals(1, v83.getNest());
        assertEquals("+", String.valueOf(((ExpressionBinary) v81).op.getText()));
        Expression v85 = ((ExpressionBinary) v81).e1;
        Expression v86 = ((ExpressionBinary) v85).e0;
        Expression v87 = ((ExpressionBinary) v86).e0;
        assertThat("", v87, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v87.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v87).getNest());
        Declaration v88 = ((ExpressionIdent) v87).getDec();
        IToken v89 = ((ConstDec)v88).ident;
        assertEquals("c", String.valueOf(v89.getText()));
        String v90 = (String)((ConstDec)v88).val;
        assertEquals("a", v90);
        assertEquals(0, v88.getNest());
        assertEquals("/", String.valueOf(((ExpressionBinary) v86).op.getText()));
        Expression v92 = ((ExpressionBinary) v86).e1;
        Expression v93 = ((ExpressionBinary) v92).e0;
        assertThat("", v93, instanceOf(ExpressionIdent.class));
        assertEquals("a", String.valueOf(v93.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v93).getNest());
        Declaration v94 = ((ExpressionIdent) v93).getDec();
        IToken v95 = ((ConstDec)v94).ident;
        assertEquals("a", String.valueOf(v95.getText()));
        Integer v96 = (Integer)((ConstDec)v94).val;
        assertEquals(1, v96);
        assertEquals(0, v94.getNest());
        assertEquals("-", String.valueOf(((ExpressionBinary) v92).op.getText()));
        Expression v98 = ((ExpressionBinary) v92).e1;
        assertThat("", v98, instanceOf(ExpressionNumLit.class));
        assertEquals("4", String.valueOf(v98.firstToken.getText()));
        assertEquals("/", String.valueOf(((ExpressionBinary) v85).op.getText()));
        Expression v99 = ((ExpressionBinary) v85).e1;
        assertThat("", v99, instanceOf(ExpressionIdent.class));
        assertEquals("d", String.valueOf(v99.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v99).getNest());
        Declaration v100 = ((ExpressionIdent) v99).getDec();
        IToken v101 = ((ConstDec)v100).ident;
        assertEquals("d", String.valueOf(v101.getText()));
        String v102 = (String)((ConstDec)v100).val;
        assertEquals("n", v102);
        assertEquals(1, v100.getNest());
        Statement v104 = ((Block) v0).statement;
        assertThat("", v104, instanceOf(StatementEmpty.class));
    }
    
    @Test
    void paramTest9() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            PROCEDURE p2;
                                VAR c;
                                CALL c
                            ;
                        ;
                        .
                        """;
        ASTNode ast = getDecoratedAST(input);
        assertThat("", ast, instanceOf(Program.class));
        Block v0 = ((Program) ast).block;
        assertThat("", v0, instanceOf(Block.class));
        List<ConstDec> v1 = ((Block) v0).constDecs;
        assertEquals(3, v1.size());
        ConstDec v2 = v1.get(0);
        assertThat("", v2, instanceOf(ConstDec.class));
        IToken v3 = ((ConstDec)v2).ident;
        assertEquals("a", String.valueOf(v3.getText()));
        Integer v4 = (Integer)((ConstDec)v2).val;
        assertEquals(1, v4);
        assertEquals(0, v2.getNest());
        ConstDec v6 = v1.get(1);
        assertThat("", v6, instanceOf(ConstDec.class));
        IToken v7 = ((ConstDec)v6).ident;
        assertEquals("b", String.valueOf(v7.getText()));
        Boolean v8 = (Boolean)((ConstDec)v6).val;
        assertEquals(true, v8);
        assertEquals(0, v6.getNest());
        ConstDec v10 = v1.get(2);
        assertThat("", v10, instanceOf(ConstDec.class));
        IToken v11 = ((ConstDec)v10).ident;
        assertEquals("c", String.valueOf(v11.getText()));
        String v12 = (String)((ConstDec)v10).val;
        assertEquals("a", v12);
        assertEquals(0, v10.getNest());
        List<VarDec> v14 = ((Block) v0).varDecs;
        assertEquals(1, v14.size());
        VarDec v15 = v14.get(0);
        assertThat("", v15, instanceOf(VarDec.class));
        IToken v16 = ((VarDec)v15).ident;
        assertEquals("d", String.valueOf(v16.getText()));
        assertEquals(0, v15.getNest());
        List<ProcDec> v17 = ((Block) v0).procedureDecs;
        assertEquals(1, v17.size());
        ProcDec v18 = v17.get(0);
        assertThat("", v18, instanceOf(ProcDec.class));
        IToken v19 = v18.ident;
        assertEquals("p1", String.valueOf(v19.getText()));
        assertEquals(0, v18.getNest());
        Block v20 = v18.block;
        assertThat("", v20, instanceOf(Block.class));
        List<ConstDec> v21 = ((Block) v20).constDecs;
        assertEquals(0, v21.size());
        List<VarDec> v22 = ((Block) v20).varDecs;
        assertEquals(0, v22.size());
        List<ProcDec> v23 = ((Block) v20).procedureDecs;
        assertEquals(1, v23.size());
        ProcDec v24 = v23.get(0);
        assertThat("", v24, instanceOf(ProcDec.class));
        IToken v25 = v24.ident;
        assertEquals("p2", String.valueOf(v25.getText()));
        assertEquals(1, v24.getNest());
        Block v26 = v24.block;
        assertThat("", v26, instanceOf(Block.class));
        List<ConstDec> v27 = ((Block) v26).constDecs;
        assertEquals(0, v27.size());
        List<VarDec> v28 = ((Block) v26).varDecs;
        assertEquals(1, v28.size());
        VarDec v29 = v28.get(0);
        assertThat("", v29, instanceOf(VarDec.class));
        IToken v30 = ((VarDec)v29).ident;
        assertEquals("c", String.valueOf(v30.getText()));
        assertEquals(2, v29.getNest());
        List<ProcDec> v31 = ((Block) v26).procedureDecs;
        assertEquals(0, v31.size());
        Statement v32 = ((Block) v26).statement;
        assertThat("", v32, instanceOf(StatementCall.class));
        Ident v33 = ((StatementCall) v32).ident;
        assertThat("", v33, instanceOf(Ident.class));
        assertEquals("c", String.valueOf(v33.firstToken.getText()));
        assertEquals(2, ((Ident) v33).getNest());
        Declaration v34 = ((Ident) v33).getDec();
        IToken v35 = ((VarDec)v34).ident;
        assertEquals("c", String.valueOf(v35.getText()));
        assertEquals(2, v34.getNest());
        Statement v36 = ((Block) v20).statement;
        assertThat("", v36, instanceOf(StatementEmpty.class));
        Statement v37 = ((Block) v0).statement;
        assertThat("", v37, instanceOf(StatementEmpty.class));
    }
    
    @Test
    void paramTest10() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            CONST c = 4;
                            PROCEDURE p2;
                                VAR c;
                                PROCEDURE p3;
                                    ? c
                                ;
                            ;
                        ;
                        .
                        """;
        ASTNode ast = getDecoratedAST(input);
        assertThat("", ast, instanceOf(Program.class));
        Block v0 = ((Program) ast).block;
        assertThat("", v0, instanceOf(Block.class));
        List<ConstDec> v1 = ((Block) v0).constDecs;
        assertEquals(3, v1.size());
        ConstDec v2 = v1.get(0);
        assertThat("", v2, instanceOf(ConstDec.class));
        IToken v3 = ((ConstDec)v2).ident;
        assertEquals("a", String.valueOf(v3.getText()));
        Integer v4 = (Integer)((ConstDec)v2).val;
        assertEquals(1, v4);
        assertEquals(0, v2.getNest());
        ConstDec v6 = v1.get(1);
        assertThat("", v6, instanceOf(ConstDec.class));
        IToken v7 = ((ConstDec)v6).ident;
        assertEquals("b", String.valueOf(v7.getText()));
        Boolean v8 = (Boolean)((ConstDec)v6).val;
        assertEquals(true, v8);
        assertEquals(0, v6.getNest());
        ConstDec v10 = v1.get(2);
        assertThat("", v10, instanceOf(ConstDec.class));
        IToken v11 = ((ConstDec)v10).ident;
        assertEquals("c", String.valueOf(v11.getText()));
        String v12 = (String)((ConstDec)v10).val;
        assertEquals("a", v12);
        assertEquals(0, v10.getNest());
        List<VarDec> v14 = ((Block) v0).varDecs;
        assertEquals(1, v14.size());
        VarDec v15 = v14.get(0);
        assertThat("", v15, instanceOf(VarDec.class));
        IToken v16 = ((VarDec)v15).ident;
        assertEquals("d", String.valueOf(v16.getText()));
        assertEquals(0, v15.getNest());
        List<ProcDec> v17 = ((Block) v0).procedureDecs;
        assertEquals(1, v17.size());
        ProcDec v18 = v17.get(0);
        assertThat("", v18, instanceOf(ProcDec.class));
        IToken v19 = v18.ident;
        assertEquals("p1", String.valueOf(v19.getText()));
        assertEquals(0, v18.getNest());
        Block v20 = v18.block;
        assertThat("", v20, instanceOf(Block.class));
        List<ConstDec> v21 = ((Block) v20).constDecs;
        assertEquals(1, v21.size());
        ConstDec v22 = v21.get(0);
        assertThat("", v22, instanceOf(ConstDec.class));
        IToken v23 = ((ConstDec)v22).ident;
        assertEquals("c", String.valueOf(v23.getText()));
        Integer v24 = (Integer)((ConstDec)v22).val;
        assertEquals(4, v24);
        assertEquals(1, v22.getNest());
        List<VarDec> v26 = ((Block) v20).varDecs;
        assertEquals(0, v26.size());
        List<ProcDec> v27 = ((Block) v20).procedureDecs;
        assertEquals(1, v27.size());
        ProcDec v28 = v27.get(0);
        assertThat("", v28, instanceOf(ProcDec.class));
        IToken v29 = v28.ident;
        assertEquals("p2", String.valueOf(v29.getText()));
        assertEquals(1, v28.getNest());
        Block v30 = v28.block;
        assertThat("", v30, instanceOf(Block.class));
        List<ConstDec> v31 = ((Block) v30).constDecs;
        assertEquals(0, v31.size());
        List<VarDec> v32 = ((Block) v30).varDecs;
        assertEquals(1, v32.size());
        VarDec v33 = v32.get(0);
        assertThat("", v33, instanceOf(VarDec.class));
        IToken v34 = ((VarDec)v33).ident;
        assertEquals("c", String.valueOf(v34.getText()));
        assertEquals(2, v33.getNest());
        List<ProcDec> v35 = ((Block) v30).procedureDecs;
        assertEquals(1, v35.size());
        ProcDec v36 = v35.get(0);
        assertThat("", v36, instanceOf(ProcDec.class));
        IToken v37 = v36.ident;
        assertEquals("p3", String.valueOf(v37.getText()));
        assertEquals(2, v36.getNest());
        Block v38 = v36.block;
        assertThat("", v38, instanceOf(Block.class));
        List<ConstDec> v39 = ((Block) v38).constDecs;
        assertEquals(0, v39.size());
        List<VarDec> v40 = ((Block) v38).varDecs;
        assertEquals(0, v40.size());
        List<ProcDec> v41 = ((Block) v38).procedureDecs;
        assertEquals(0, v41.size());
        Statement v42 = ((Block) v38).statement;
        assertThat("", v42, instanceOf(StatementInput.class));
        Ident v43 = ((StatementInput)v42).ident;
        assertThat("", v43, instanceOf(Ident.class));
        assertEquals("c", String.valueOf(v43.firstToken.getText()));
        assertEquals(3, ((Ident) v43).getNest());
        Declaration v44 = ((Ident) v43).getDec();
        IToken v45 = ((VarDec)v44).ident;
        assertEquals("c", String.valueOf(v45.getText()));
        assertEquals(2, v44.getNest());
        Statement v46 = ((Block) v30).statement;
        assertThat("", v46, instanceOf(StatementEmpty.class));
        Statement v47 = ((Block) v20).statement;
        assertThat("", v47, instanceOf(StatementEmpty.class));
        Statement v48 = ((Block) v0).statement;
        assertThat("", v48, instanceOf(StatementEmpty.class));
    }
    
    @Test
    void paramTest11() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            PROCEDURE p2;
                                ! c
                            ;
                            ! p2
                        ;
                        .
                        """;
        ASTNode ast = getDecoratedAST(input);
        assertThat("", ast, instanceOf(Program.class));
        Block v0 = ((Program) ast).block;
        assertThat("", v0, instanceOf(Block.class));
        List<ConstDec> v1 = ((Block) v0).constDecs;
        assertEquals(3, v1.size());
        ConstDec v2 = v1.get(0);
        assertThat("", v2, instanceOf(ConstDec.class));
        IToken v3 = ((ConstDec)v2).ident;
        assertEquals("a", String.valueOf(v3.getText()));
        Integer v4 = (Integer)((ConstDec)v2).val;
        assertEquals(1, v4);
        assertEquals(0, v2.getNest());
        ConstDec v6 = v1.get(1);
        assertThat("", v6, instanceOf(ConstDec.class));
        IToken v7 = ((ConstDec)v6).ident;
        assertEquals("b", String.valueOf(v7.getText()));
        Boolean v8 = (Boolean)((ConstDec)v6).val;
        assertEquals(true, v8);
        assertEquals(0, v6.getNest());
        ConstDec v10 = v1.get(2);
        assertThat("", v10, instanceOf(ConstDec.class));
        IToken v11 = ((ConstDec)v10).ident;
        assertEquals("c", String.valueOf(v11.getText()));
        String v12 = (String)((ConstDec)v10).val;
        assertEquals("a", v12);
        assertEquals(0, v10.getNest());
        List<VarDec> v14 = ((Block) v0).varDecs;
        assertEquals(1, v14.size());
        VarDec v15 = v14.get(0);
        assertThat("", v15, instanceOf(VarDec.class));
        IToken v16 = ((VarDec)v15).ident;
        assertEquals("d", String.valueOf(v16.getText()));
        assertEquals(0, v15.getNest());
        List<ProcDec> v17 = ((Block) v0).procedureDecs;
        assertEquals(1, v17.size());
        ProcDec v18 = v17.get(0);
        assertThat("", v18, instanceOf(ProcDec.class));
        IToken v19 = ((ProcDec)v18).ident;
        assertEquals("p1", String.valueOf(v19.getText()));
        assertEquals(0, ((ProcDec)v18).getNest());
        Block v20 = ((ProcDec)v18).block;
        assertThat("", v20, instanceOf(Block.class));
        List<ConstDec> v21 = ((Block) v20).constDecs;
        assertEquals(0, v21.size());
        List<VarDec> v22 = ((Block) v20).varDecs;
        assertEquals(0, v22.size());
        List<ProcDec> v23 = ((Block) v20).procedureDecs;
        assertEquals(1, v23.size());
        ProcDec v24 = v23.get(0);
        assertThat("", v24, instanceOf(ProcDec.class));
        IToken v25 = ((ProcDec)v24).ident;
        assertEquals("p2", String.valueOf(v25.getText()));
        assertEquals(1, ((ProcDec)v24).getNest());
        Block v26 = ((ProcDec)v24).block;
        assertThat("", v26, instanceOf(Block.class));
        List<ConstDec> v27 = ((Block) v26).constDecs;
        assertEquals(0, v27.size());
        List<VarDec> v28 = ((Block) v26).varDecs;
        assertEquals(0, v28.size());
        List<ProcDec> v29 = ((Block) v26).procedureDecs;
        assertEquals(0, v29.size());
        Statement v30 = ((Block) v26).statement;
        assertThat("", v30, instanceOf(StatementOutput.class));
        Expression v31 = ((StatementOutput) v30).expression;
        assertThat("", v31, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v31.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v31).getNest());
        Declaration v32 = ((ExpressionIdent) v31).getDec();
        IToken v33 = ((ConstDec)v32).ident;
        assertEquals("c", String.valueOf(v33.getText()));
        String v34 = (String)((ConstDec)v32).val;
        assertEquals("a", v34);
        assertEquals(0, v32.getNest());
        Statement v36 = ((Block) v20).statement;
        assertThat("", v36, instanceOf(StatementOutput.class));
        Expression v37 = ((StatementOutput) v36).expression;
        assertThat("", v37, instanceOf(ExpressionIdent.class));
        assertEquals("p2", String.valueOf(v37.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v37).getNest());
        Declaration v38 = ((ExpressionIdent) v37).getDec();
        IToken v39 = ((ProcDec)v38).ident;
        assertEquals("p2", String.valueOf(v39.getText()));
        assertEquals(1, ((ProcDec)v38).getNest());
        Block v40 = ((ProcDec)v38).block;
        assertThat("", v40, instanceOf(Block.class));
        List<ConstDec> v41 = ((Block) v40).constDecs;
        assertEquals(0, v41.size());
        List<VarDec> v42 = ((Block) v40).varDecs;
        assertEquals(0, v42.size());
        List<ProcDec> v43 = ((Block) v40).procedureDecs;
        assertEquals(0, v43.size());
        Statement v44 = ((Block) v40).statement;
        assertThat("", v44, instanceOf(StatementOutput.class));
        Expression v45 = ((StatementOutput) v44).expression;
        assertThat("", v45, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v45.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v45).getNest());
        Declaration v46 = ((ExpressionIdent) v45).getDec();
        IToken v47 = ((ConstDec)v46).ident;
        assertEquals("c", String.valueOf(v47.getText()));
        String v48 = (String)((ConstDec)v46).val;
        assertEquals("a", v48);
        assertEquals(0, v46.getNest());
        Statement v50 = ((Block) v0).statement;
        assertThat("", v50, instanceOf(StatementEmpty.class));
    }
    
    @Test
    void paramTest12() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            VAR a, d;
                            PROCEDURE p2;
                                BEGIN
                                    ! c;
                                    b := c;
                                    c := b-d>=TRUE
                                END
                            ;
                            BEGIN
                                    ! c;
                                    b := c;
                                    c := b-d>=TRUE
                            END
                        ;
                        .
                        """;
        ASTNode ast = getDecoratedAST(input);
        assertThat("", ast, instanceOf(Program.class));
        Block v0 = ((Program) ast).block;
        assertThat("", v0, instanceOf(Block.class));
        List<ConstDec> v1 = ((Block) v0).constDecs;
        assertEquals(3, v1.size());
        ConstDec v2 = v1.get(0);
        assertThat("", v2, instanceOf(ConstDec.class));
        IToken v3 = ((ConstDec)v2).ident;
        assertEquals("a", String.valueOf(v3.getText()));
        Integer v4 = (Integer)((ConstDec)v2).val;
        assertEquals(1, v4);
        assertEquals(0, v2.getNest());
        ConstDec v6 = v1.get(1);
        assertThat("", v6, instanceOf(ConstDec.class));
        IToken v7 = ((ConstDec)v6).ident;
        assertEquals("b", String.valueOf(v7.getText()));
        Boolean v8 = (Boolean)((ConstDec)v6).val;
        assertEquals(true, v8);
        assertEquals(0, v6.getNest());
        ConstDec v10 = v1.get(2);
        assertThat("", v10, instanceOf(ConstDec.class));
        IToken v11 = ((ConstDec)v10).ident;
        assertEquals("c", String.valueOf(v11.getText()));
        String v12 = (String)((ConstDec)v10).val;
        assertEquals("a", v12);
        assertEquals(0, v10.getNest());
        List<VarDec> v14 = ((Block) v0).varDecs;
        assertEquals(1, v14.size());
        VarDec v15 = v14.get(0);
        assertThat("", v15, instanceOf(VarDec.class));
        IToken v16 = ((VarDec)v15).ident;
        assertEquals("d", String.valueOf(v16.getText()));
        assertEquals(0, v15.getNest());
        List<ProcDec> v17 = ((Block) v0).procedureDecs;
        assertEquals(1, v17.size());
        ProcDec v18 = v17.get(0);
        assertThat("", v18, instanceOf(ProcDec.class));
        IToken v19 = ((ProcDec)v18).ident;
        assertEquals("p1", String.valueOf(v19.getText()));
        assertEquals(0, ((ProcDec)v18).getNest());
        Block v20 = ((ProcDec)v18).block;
        assertThat("", v20, instanceOf(Block.class));
        List<ConstDec> v21 = ((Block) v20).constDecs;
        assertEquals(0, v21.size());
        List<VarDec> v22 = ((Block) v20).varDecs;
        assertEquals(2, v22.size());
        VarDec v23 = v22.get(0);
        assertThat("", v23, instanceOf(VarDec.class));
        IToken v24 = ((VarDec)v23).ident;
        assertEquals("a", String.valueOf(v24.getText()));
        assertEquals(1, v23.getNest());
        VarDec v25 = v22.get(1);
        assertThat("", v25, instanceOf(VarDec.class));
        IToken v26 = ((VarDec)v25).ident;
        assertEquals("d", String.valueOf(v26.getText()));
        assertEquals(1, v25.getNest());
        List<ProcDec> v27 = ((Block) v20).procedureDecs;
        assertEquals(1, v27.size());
        ProcDec v28 = v27.get(0);
        assertThat("", v28, instanceOf(ProcDec.class));
        IToken v29 = ((ProcDec)v28).ident;
        assertEquals("p2", String.valueOf(v29.getText()));
        assertEquals(1, ((ProcDec)v28).getNest());
        Block v30 = ((ProcDec)v28).block;
        assertThat("", v30, instanceOf(Block.class));
        List<ConstDec> v31 = ((Block) v30).constDecs;
        assertEquals(0, v31.size());
        List<VarDec> v32 = ((Block) v30).varDecs;
        assertEquals(0, v32.size());
        List<ProcDec> v33 = ((Block) v30).procedureDecs;
        assertEquals(0, v33.size());
        Statement v34 = ((Block) v30).statement;
        assertThat("", v34, instanceOf(StatementBlock.class));
        Statement v35 = ((StatementBlock) v34).statements.get(0);
        assertThat("", v35, instanceOf(StatementOutput.class));
        Expression v36 = ((StatementOutput) v35).expression;
        assertThat("", v36, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v36.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v36).getNest());
        Declaration v37 = ((ExpressionIdent) v36).getDec();
        IToken v38 = ((ConstDec)v37).ident;
        assertEquals("c", String.valueOf(v38.getText()));
        String v39 = (String)((ConstDec)v37).val;
        assertEquals("a", v39);
        assertEquals(0, v37.getNest());
        Statement v41 = ((StatementBlock) v34).statements.get(1);
        assertThat("", v41, instanceOf(StatementAssign.class));
        Ident v42 = ((StatementAssign) v41).ident;
        assertThat("", v42, instanceOf(Ident.class));
        assertEquals("b", String.valueOf(v42.firstToken.getText()));
        assertEquals(2, ((Ident) v42).getNest());
        Declaration v43 = ((Ident) v42).getDec();
        IToken v44 = ((ConstDec)v43).ident;
        assertEquals("b", String.valueOf(v44.getText()));
        Boolean v45 = (Boolean)((ConstDec)v43).val;
        assertEquals(true, v45);
        assertEquals(0, v43.getNest());
        Expression v47 = ((StatementAssign) v41).expression;
        assertThat("", v47, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v47.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v47).getNest());
        Declaration v48 = ((ExpressionIdent) v47).getDec();
        IToken v49 = ((ConstDec)v48).ident;
        assertEquals("c", String.valueOf(v49.getText()));
        String v50 = (String)((ConstDec)v48).val;
        assertEquals("a", v50);
        assertEquals(0, v48.getNest());
        Statement v52 = ((StatementBlock) v34).statements.get(2);
        assertThat("", v52, instanceOf(StatementAssign.class));
        Ident v53 = ((StatementAssign) v52).ident;
        assertThat("", v53, instanceOf(Ident.class));
        assertEquals("c", String.valueOf(v53.firstToken.getText()));
        assertEquals(2, ((Ident) v53).getNest());
        Declaration v54 = ((Ident) v53).getDec();
        IToken v55 = ((ConstDec)v54).ident;
        assertEquals("c", String.valueOf(v55.getText()));
        String v56 = (String)((ConstDec)v54).val;
        assertEquals("a", v56);
        assertEquals(0, v54.getNest());
        Expression v58 = ((StatementAssign) v52).expression;
        Expression v59 = ((ExpressionBinary) v58).e0;
        Expression v60 = ((ExpressionBinary) v59).e0;
        assertThat("", v60, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v60.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v60).getNest());
        Declaration v61 = ((ExpressionIdent) v60).getDec();
        IToken v62 = ((ConstDec)v61).ident;
        assertEquals("b", String.valueOf(v62.getText()));
        Boolean v63 = (Boolean)((ConstDec)v61).val;
        assertEquals(true, v63);
        assertEquals(0, v61.getNest());
        assertEquals("-", String.valueOf(((ExpressionBinary) v59).op.getText()));
        Expression v65 = ((ExpressionBinary) v59).e1;
        assertThat("", v65, instanceOf(ExpressionIdent.class));
        assertEquals("d", String.valueOf(v65.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v65).getNest());
        Declaration v66 = ((ExpressionIdent) v65).getDec();
        IToken v67 = ((VarDec)v66).ident;
        assertEquals("d", String.valueOf(v67.getText()));
        assertEquals(1, v66.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v58).op.getText()));
        Expression v68 = ((ExpressionBinary) v58).e1;
        assertThat("", v68, instanceOf(ExpressionBooleanLit.class));
        assertEquals("TRUE", String.valueOf(v68.firstToken.getText()));
        Statement v69 = ((Block) v20).statement;
        assertThat("", v69, instanceOf(StatementBlock.class));
        Statement v70 = ((StatementBlock) v69).statements.get(0);
        assertThat("", v70, instanceOf(StatementOutput.class));
        Expression v71 = ((StatementOutput) v70).expression;
        assertThat("", v71, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v71.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v71).getNest());
        Declaration v72 = ((ExpressionIdent) v71).getDec();
        IToken v73 = ((ConstDec)v72).ident;
        assertEquals("c", String.valueOf(v73.getText()));
        String v74 = (String)((ConstDec)v72).val;
        assertEquals("a", v74);
        assertEquals(0, v72.getNest());
        Statement v76 = ((StatementBlock) v69).statements.get(1);
        assertThat("", v76, instanceOf(StatementAssign.class));
        Ident v77 = ((StatementAssign) v76).ident;
        assertThat("", v77, instanceOf(Ident.class));
        assertEquals("b", String.valueOf(v77.firstToken.getText()));
        assertEquals(1, ((Ident) v77).getNest());
        Declaration v78 = ((Ident) v77).getDec();
        IToken v79 = ((ConstDec)v78).ident;
        assertEquals("b", String.valueOf(v79.getText()));
        Boolean v80 = (Boolean)((ConstDec)v78).val;
        assertEquals(true, v80);
        assertEquals(0, v78.getNest());
        Expression v82 = ((StatementAssign) v76).expression;
        assertThat("", v82, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v82.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v82).getNest());
        Declaration v83 = ((ExpressionIdent) v82).getDec();
        IToken v84 = ((ConstDec)v83).ident;
        assertEquals("c", String.valueOf(v84.getText()));
        String v85 = (String)((ConstDec)v83).val;
        assertEquals("a", v85);
        assertEquals(0, v83.getNest());
        Statement v87 = ((StatementBlock) v69).statements.get(2);
        assertThat("", v87, instanceOf(StatementAssign.class));
        Ident v88 = ((StatementAssign) v87).ident;
        assertThat("", v88, instanceOf(Ident.class));
        assertEquals("c", String.valueOf(v88.firstToken.getText()));
        assertEquals(1, ((Ident) v88).getNest());
        Declaration v89 = ((Ident) v88).getDec();
        IToken v90 = ((ConstDec)v89).ident;
        assertEquals("c", String.valueOf(v90.getText()));
        String v91 = (String)((ConstDec)v89).val;
        assertEquals("a", v91);
        assertEquals(0, v89.getNest());
        Expression v93 = ((StatementAssign) v87).expression;
        Expression v94 = ((ExpressionBinary) v93).e0;
        Expression v95 = ((ExpressionBinary) v94).e0;
        assertThat("", v95, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v95.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v95).getNest());
        Declaration v96 = ((ExpressionIdent) v95).getDec();
        IToken v97 = ((ConstDec)v96).ident;
        assertEquals("b", String.valueOf(v97.getText()));
        Boolean v98 = (Boolean)((ConstDec)v96).val;
        assertEquals(true, v98);
        assertEquals(0, v96.getNest());
        assertEquals("-", String.valueOf(((ExpressionBinary) v94).op.getText()));
        Expression v100 = ((ExpressionBinary) v94).e1;
        assertThat("", v100, instanceOf(ExpressionIdent.class));
        assertEquals("d", String.valueOf(v100.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v100).getNest());
        Declaration v101 = ((ExpressionIdent) v100).getDec();
        IToken v102 = ((VarDec)v101).ident;
        assertEquals("d", String.valueOf(v102.getText()));
        assertEquals(1, v101.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v93).op.getText()));
        Expression v103 = ((ExpressionBinary) v93).e1;
        assertThat("", v103, instanceOf(ExpressionBooleanLit.class));
        assertEquals("TRUE", String.valueOf(v103.firstToken.getText()));
        Statement v104 = ((Block) v0).statement;
        assertThat("", v104, instanceOf(StatementEmpty.class));
    }
    
    @Test
    void paramTest13() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            IF a>=b+(3-2) THEN
                                BEGIN
                                    ! c;
                                    b := c;
                                    c := b-d>=TRUE
                                END
                        ;
                        .
                        """;
        ASTNode ast = getDecoratedAST(input);
        assertThat("", ast, instanceOf(Program.class));
        Block v0 = ((Program) ast).block;
        assertThat("", v0, instanceOf(Block.class));
        List<ConstDec> v1 = ((Block) v0).constDecs;
        assertEquals(3, v1.size());
        ConstDec v2 = v1.get(0);
        assertThat("", v2, instanceOf(ConstDec.class));
        IToken v3 = ((ConstDec)v2).ident;
        assertEquals("a", String.valueOf(v3.getText()));
        Integer v4 = (Integer)((ConstDec)v2).val;
        assertEquals(1, v4);
        assertEquals(0, v2.getNest());
        ConstDec v6 = v1.get(1);
        assertThat("", v6, instanceOf(ConstDec.class));
        IToken v7 = ((ConstDec)v6).ident;
        assertEquals("b", String.valueOf(v7.getText()));
        Boolean v8 = (Boolean)((ConstDec)v6).val;
        assertEquals(true, v8);
        assertEquals(0, v6.getNest());
        ConstDec v10 = v1.get(2);
        assertThat("", v10, instanceOf(ConstDec.class));
        IToken v11 = ((ConstDec)v10).ident;
        assertEquals("c", String.valueOf(v11.getText()));
        String v12 = (String)((ConstDec)v10).val;
        assertEquals("a", v12);
        assertEquals(0, v10.getNest());
        List<VarDec> v14 = ((Block) v0).varDecs;
        assertEquals(1, v14.size());
        VarDec v15 = v14.get(0);
        assertThat("", v15, instanceOf(VarDec.class));
        IToken v16 = ((VarDec)v15).ident;
        assertEquals("d", String.valueOf(v16.getText()));
        assertEquals(0, v15.getNest());
        List<ProcDec> v17 = ((Block) v0).procedureDecs;
        assertEquals(1, v17.size());
        ProcDec v18 = v17.get(0);
        assertThat("", v18, instanceOf(ProcDec.class));
        IToken v19 = ((ProcDec)v18).ident;
        assertEquals("p1", String.valueOf(v19.getText()));
        assertEquals(0, ((ProcDec)v18).getNest());
        Block v20 = ((ProcDec)v18).block;
        assertThat("", v20, instanceOf(Block.class));
        List<ConstDec> v21 = ((Block) v20).constDecs;
        assertEquals(0, v21.size());
        List<VarDec> v22 = ((Block) v20).varDecs;
        assertEquals(0, v22.size());
        List<ProcDec> v23 = ((Block) v20).procedureDecs;
        assertEquals(0, v23.size());
        Statement v24 = ((Block) v20).statement;
        Expression v25 = ((StatementIf) v24).expression;
        Expression v26 = ((ExpressionBinary) v25).e0;
        assertThat("", v26, instanceOf(ExpressionIdent.class));
        assertEquals("a", String.valueOf(v26.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v26).getNest());
        Declaration v27 = ((ExpressionIdent) v26).getDec();
        IToken v28 = ((ConstDec)v27).ident;
        assertEquals("a", String.valueOf(v28.getText()));
        Integer v29 = (Integer)((ConstDec)v27).val;
        assertEquals(1, v29);
        assertEquals(0, v27.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v25).op.getText()));
        Expression v31 = ((ExpressionBinary) v25).e1;
        Expression v32 = ((ExpressionBinary) v31).e0;
        assertThat("", v32, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v32.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v32).getNest());
        Declaration v33 = ((ExpressionIdent) v32).getDec();
        IToken v34 = ((ConstDec)v33).ident;
        assertEquals("b", String.valueOf(v34.getText()));
        Boolean v35 = (Boolean)((ConstDec)v33).val;
        assertEquals(true, v35);
        assertEquals(0, v33.getNest());
        assertEquals("+", String.valueOf(((ExpressionBinary) v31).op.getText()));
        Expression v37 = ((ExpressionBinary) v31).e1;
        Expression v38 = ((ExpressionBinary) v37).e0;
        assertThat("", v38, instanceOf(ExpressionNumLit.class));
        assertEquals("3", String.valueOf(v38.firstToken.getText()));
        assertEquals("-", String.valueOf(((ExpressionBinary) v37).op.getText()));
        Expression v39 = ((ExpressionBinary) v37).e1;
        assertThat("", v39, instanceOf(ExpressionNumLit.class));
        assertEquals("2", String.valueOf(v39.firstToken.getText()));
        Statement v40 = ((StatementIf) v24).statement;
        assertThat("", v40, instanceOf(StatementBlock.class));
        Statement v41 = ((StatementBlock) v40).statements.get(0);
        assertThat("", v41, instanceOf(StatementOutput.class));
        Expression v42 = ((StatementOutput) v41).expression;
        assertThat("", v42, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v42.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v42).getNest());
        Declaration v43 = ((ExpressionIdent) v42).getDec();
        IToken v44 = ((ConstDec)v43).ident;
        assertEquals("c", String.valueOf(v44.getText()));
        String v45 = (String)((ConstDec)v43).val;
        assertEquals("a", v45);
        assertEquals(0, v43.getNest());
        Statement v47 = ((StatementBlock) v40).statements.get(1);
        assertThat("", v47, instanceOf(StatementAssign.class));
        Ident v48 = ((StatementAssign) v47).ident;
        assertThat("", v48, instanceOf(Ident.class));
        assertEquals("b", String.valueOf(v48.firstToken.getText()));
        assertEquals(1, ((Ident) v48).getNest());
        Declaration v49 = ((Ident) v48).getDec();
        IToken v50 = ((ConstDec)v49).ident;
        assertEquals("b", String.valueOf(v50.getText()));
        Boolean v51 = (Boolean)((ConstDec)v49).val;
        assertEquals(true, v51);
        assertEquals(0, v49.getNest());
        Expression v53 = ((StatementAssign) v47).expression;
        assertThat("", v53, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v53.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v53).getNest());
        Declaration v54 = ((ExpressionIdent) v53).getDec();
        IToken v55 = ((ConstDec)v54).ident;
        assertEquals("c", String.valueOf(v55.getText()));
        String v56 = (String)((ConstDec)v54).val;
        assertEquals("a", v56);
        assertEquals(0, v54.getNest());
        Statement v58 = ((StatementBlock) v40).statements.get(2);
        assertThat("", v58, instanceOf(StatementAssign.class));
        Ident v59 = ((StatementAssign) v58).ident;
        assertThat("", v59, instanceOf(Ident.class));
        assertEquals("c", String.valueOf(v59.firstToken.getText()));
        assertEquals(1, ((Ident) v59).getNest());
        Declaration v60 = ((Ident) v59).getDec();
        IToken v61 = ((ConstDec)v60).ident;
        assertEquals("c", String.valueOf(v61.getText()));
        String v62 = (String)((ConstDec)v60).val;
        assertEquals("a", v62);
        assertEquals(0, v60.getNest());
        Expression v64 = ((StatementAssign) v58).expression;
        Expression v65 = ((ExpressionBinary) v64).e0;
        Expression v66 = ((ExpressionBinary) v65).e0;
        assertThat("", v66, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v66.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v66).getNest());
        Declaration v67 = ((ExpressionIdent) v66).getDec();
        IToken v68 = ((ConstDec)v67).ident;
        assertEquals("b", String.valueOf(v68.getText()));
        Boolean v69 = (Boolean)((ConstDec)v67).val;
        assertEquals(true, v69);
        assertEquals(0, v67.getNest());
        assertEquals("-", String.valueOf(((ExpressionBinary) v65).op.getText()));
        Expression v71 = ((ExpressionBinary) v65).e1;
        assertThat("", v71, instanceOf(ExpressionIdent.class));
        assertEquals("d", String.valueOf(v71.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v71).getNest());
        Declaration v72 = ((ExpressionIdent) v71).getDec();
        IToken v73 = ((VarDec)v72).ident;
        assertEquals("d", String.valueOf(v73.getText()));
        assertEquals(0, v72.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v64).op.getText()));
        Expression v74 = ((ExpressionBinary) v64).e1;
        assertThat("", v74, instanceOf(ExpressionBooleanLit.class));
        assertEquals("TRUE", String.valueOf(v74.firstToken.getText()));
        Statement v75 = ((Block) v0).statement;
        assertThat("", v75, instanceOf(StatementEmpty.class));
    }
    
    @Test
    void paramTest14() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            PROCEDURE p2;
                                WHILE a>=b+(3-2) DO
                                    BEGIN
                                        ! c;
                                        b := c;
                                        c := b-d>=TRUE
                                    END
                            ;
                            d := 6
                        ;
                        .
                        """;
        ASTNode ast = getDecoratedAST(input);
        assertThat("", ast, instanceOf(Program.class));
        Block v0 = ((Program) ast).block;
        assertThat("", v0, instanceOf(Block.class));
        List<ConstDec> v1 = ((Block) v0).constDecs;
        assertEquals(3, v1.size());
        ConstDec v2 = v1.get(0);
        assertThat("", v2, instanceOf(ConstDec.class));
        IToken v3 = ((ConstDec)v2).ident;
        assertEquals("a", String.valueOf(v3.getText()));
        Integer v4 = (Integer)((ConstDec)v2).val;
        assertEquals(1, v4);
        assertEquals(0, v2.getNest());
        ConstDec v6 = v1.get(1);
        assertThat("", v6, instanceOf(ConstDec.class));
        IToken v7 = ((ConstDec)v6).ident;
        assertEquals("b", String.valueOf(v7.getText()));
        Boolean v8 = (Boolean)((ConstDec)v6).val;
        assertEquals(true, v8);
        assertEquals(0, v6.getNest());
        ConstDec v10 = v1.get(2);
        assertThat("", v10, instanceOf(ConstDec.class));
        IToken v11 = ((ConstDec)v10).ident;
        assertEquals("c", String.valueOf(v11.getText()));
        String v12 = (String)((ConstDec)v10).val;
        assertEquals("a", v12);
        assertEquals(0, v10.getNest());
        List<VarDec> v14 = ((Block) v0).varDecs;
        assertEquals(1, v14.size());
        VarDec v15 = v14.get(0);
        assertThat("", v15, instanceOf(VarDec.class));
        IToken v16 = ((VarDec)v15).ident;
        assertEquals("d", String.valueOf(v16.getText()));
        assertEquals(0, v15.getNest());
        List<ProcDec> v17 = ((Block) v0).procedureDecs;
        assertEquals(1, v17.size());
        ProcDec v18 = v17.get(0);
        assertThat("", v18, instanceOf(ProcDec.class));
        IToken v19 = ((ProcDec)v18).ident;
        assertEquals("p1", String.valueOf(v19.getText()));
        assertEquals(0, ((ProcDec)v18).getNest());
        Block v20 = ((ProcDec)v18).block;
        assertThat("", v20, instanceOf(Block.class));
        List<ConstDec> v21 = ((Block) v20).constDecs;
        assertEquals(0, v21.size());
        List<VarDec> v22 = ((Block) v20).varDecs;
        assertEquals(0, v22.size());
        List<ProcDec> v23 = ((Block) v20).procedureDecs;
        assertEquals(1, v23.size());
        ProcDec v24 = v23.get(0);
        assertThat("", v24, instanceOf(ProcDec.class));
        IToken v25 = ((ProcDec)v24).ident;
        assertEquals("p2", String.valueOf(v25.getText()));
        assertEquals(1, ((ProcDec)v24).getNest());
        Block v26 = ((ProcDec)v24).block;
        assertThat("", v26, instanceOf(Block.class));
        List<ConstDec> v27 = ((Block) v26).constDecs;
        assertEquals(0, v27.size());
        List<VarDec> v28 = ((Block) v26).varDecs;
        assertEquals(0, v28.size());
        List<ProcDec> v29 = ((Block) v26).procedureDecs;
        assertEquals(0, v29.size());
        Statement v30 = ((Block) v26).statement;
        Expression v31 = ((StatementWhile) v30).expression;
        Expression v32 = ((ExpressionBinary) v31).e0;
        assertThat("", v32, instanceOf(ExpressionIdent.class));
        assertEquals("a", String.valueOf(v32.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v32).getNest());
        Declaration v33 = ((ExpressionIdent) v32).getDec();
        IToken v34 = ((ConstDec)v33).ident;
        assertEquals("a", String.valueOf(v34.getText()));
        Integer v35 = (Integer)((ConstDec)v33).val;
        assertEquals(1, v35);
        assertEquals(0, v33.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v31).op.getText()));
        Expression v37 = ((ExpressionBinary) v31).e1;
        Expression v38 = ((ExpressionBinary) v37).e0;
        assertThat("", v38, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v38.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v38).getNest());
        Declaration v39 = ((ExpressionIdent) v38).getDec();
        IToken v40 = ((ConstDec)v39).ident;
        assertEquals("b", String.valueOf(v40.getText()));
        Boolean v41 = (Boolean)((ConstDec)v39).val;
        assertEquals(true, v41);
        assertEquals(0, v39.getNest());
        assertEquals("+", String.valueOf(((ExpressionBinary) v37).op.getText()));
        Expression v43 = ((ExpressionBinary) v37).e1;
        Expression v44 = ((ExpressionBinary) v43).e0;
        assertThat("", v44, instanceOf(ExpressionNumLit.class));
        assertEquals("3", String.valueOf(v44.firstToken.getText()));
        assertEquals("-", String.valueOf(((ExpressionBinary) v43).op.getText()));
        Expression v45 = ((ExpressionBinary) v43).e1;
        assertThat("", v45, instanceOf(ExpressionNumLit.class));
        assertEquals("2", String.valueOf(v45.firstToken.getText()));
        Statement v46 = ((StatementWhile) v30).statement;
        assertThat("", v46, instanceOf(StatementBlock.class));
        Statement v47 = ((StatementBlock) v46).statements.get(0);
        assertThat("", v47, instanceOf(StatementOutput.class));
        Expression v48 = ((StatementOutput) v47).expression;
        assertThat("", v48, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v48.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v48).getNest());
        Declaration v49 = ((ExpressionIdent) v48).getDec();
        IToken v50 = ((ConstDec)v49).ident;
        assertEquals("c", String.valueOf(v50.getText()));
        String v51 = (String)((ConstDec)v49).val;
        assertEquals("a", v51);
        assertEquals(0, v49.getNest());
        Statement v53 = ((StatementBlock) v46).statements.get(1);
        assertThat("", v53, instanceOf(StatementAssign.class));
        Ident v54 = ((StatementAssign) v53).ident;
        assertThat("", v54, instanceOf(Ident.class));
        assertEquals("b", String.valueOf(v54.firstToken.getText()));
        assertEquals(2, ((Ident) v54).getNest());
        Declaration v55 = ((Ident) v54).getDec();
        IToken v56 = ((ConstDec)v55).ident;
        assertEquals("b", String.valueOf(v56.getText()));
        Boolean v57 = (Boolean)((ConstDec)v55).val;
        assertEquals(true, v57);
        assertEquals(0, v55.getNest());
        Expression v59 = ((StatementAssign) v53).expression;
        assertThat("", v59, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v59.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v59).getNest());
        Declaration v60 = ((ExpressionIdent) v59).getDec();
        IToken v61 = ((ConstDec)v60).ident;
        assertEquals("c", String.valueOf(v61.getText()));
        String v62 = (String)((ConstDec)v60).val;
        assertEquals("a", v62);
        assertEquals(0, v60.getNest());
        Statement v64 = ((StatementBlock) v46).statements.get(2);
        assertThat("", v64, instanceOf(StatementAssign.class));
        Ident v65 = ((StatementAssign) v64).ident;
        assertThat("", v65, instanceOf(Ident.class));
        assertEquals("c", String.valueOf(v65.firstToken.getText()));
        assertEquals(2, ((Ident) v65).getNest());
        Declaration v66 = ((Ident) v65).getDec();
        IToken v67 = ((ConstDec)v66).ident;
        assertEquals("c", String.valueOf(v67.getText()));
        String v68 = (String)((ConstDec)v66).val;
        assertEquals("a", v68);
        assertEquals(0, v66.getNest());
        Expression v70 = ((StatementAssign) v64).expression;
        Expression v71 = ((ExpressionBinary) v70).e0;
        Expression v72 = ((ExpressionBinary) v71).e0;
        assertThat("", v72, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v72.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v72).getNest());
        Declaration v73 = ((ExpressionIdent) v72).getDec();
        IToken v74 = ((ConstDec)v73).ident;
        assertEquals("b", String.valueOf(v74.getText()));
        Boolean v75 = (Boolean)((ConstDec)v73).val;
        assertEquals(true, v75);
        assertEquals(0, v73.getNest());
        assertEquals("-", String.valueOf(((ExpressionBinary) v71).op.getText()));
        Expression v77 = ((ExpressionBinary) v71).e1;
        assertThat("", v77, instanceOf(ExpressionIdent.class));
        assertEquals("d", String.valueOf(v77.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v77).getNest());
        Declaration v78 = ((ExpressionIdent) v77).getDec();
        IToken v79 = ((VarDec)v78).ident;
        assertEquals("d", String.valueOf(v79.getText()));
        assertEquals(0, v78.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v70).op.getText()));
        Expression v80 = ((ExpressionBinary) v70).e1;
        assertThat("", v80, instanceOf(ExpressionBooleanLit.class));
        assertEquals("TRUE", String.valueOf(v80.firstToken.getText()));
        Statement v81 = ((Block) v20).statement;
        assertThat("", v81, instanceOf(StatementAssign.class));
        Ident v82 = ((StatementAssign) v81).ident;
        assertThat("", v82, instanceOf(Ident.class));
        assertEquals("d", String.valueOf(v82.firstToken.getText()));
        assertEquals(1, ((Ident) v82).getNest());
        Declaration v83 = ((Ident) v82).getDec();
        IToken v84 = ((VarDec)v83).ident;
        assertEquals("d", String.valueOf(v84.getText()));
        assertEquals(0, v83.getNest());
        Expression v85 = ((StatementAssign) v81).expression;
        assertThat("", v85, instanceOf(ExpressionNumLit.class));
        assertEquals("6", String.valueOf(v85.firstToken.getText()));
        Statement v86 = ((Block) v0).statement;
        assertThat("", v86, instanceOf(StatementEmpty.class));
    }
    
    @Test
    void paramTest15() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            CONST d="n", e=5;
                            VAR b;
                            PROCEDURE p2;
                                a := b+c/(a-4)/e+d
                            ;
                            PROCEDURE p3;
                                a := b+c/(a-4)/e+d
                            ;
                            a := b+c/(a-4)/d
                            
                        ;
                        PROCEDURE p4;
                            c := 6
                        ;
                        .
                        """;
        ASTNode ast = getDecoratedAST(input);
        assertThat("", ast, instanceOf(Program.class));
        Block v0 = ((Program) ast).block;
        assertThat("", v0, instanceOf(Block.class));
        List<ConstDec> v1 = ((Block) v0).constDecs;
        assertEquals(3, v1.size());
        ConstDec v2 = v1.get(0);
        assertThat("", v2, instanceOf(ConstDec.class));
        IToken v3 = ((ConstDec)v2).ident;
        assertEquals("a", String.valueOf(v3.getText()));
        Integer v4 = (Integer)((ConstDec)v2).val;
        assertEquals(1, v4);
        assertEquals(0, v2.getNest());
        ConstDec v6 = v1.get(1);
        assertThat("", v6, instanceOf(ConstDec.class));
        IToken v7 = ((ConstDec)v6).ident;
        assertEquals("b", String.valueOf(v7.getText()));
        Boolean v8 = (Boolean)((ConstDec)v6).val;
        assertEquals(true, v8);
        assertEquals(0, v6.getNest());
        ConstDec v10 = v1.get(2);
        assertThat("", v10, instanceOf(ConstDec.class));
        IToken v11 = ((ConstDec)v10).ident;
        assertEquals("c", String.valueOf(v11.getText()));
        String v12 = (String)((ConstDec)v10).val;
        assertEquals("a", v12);
        assertEquals(0, v10.getNest());
        List<VarDec> v14 = ((Block) v0).varDecs;
        assertEquals(1, v14.size());
        VarDec v15 = v14.get(0);
        assertThat("", v15, instanceOf(VarDec.class));
        IToken v16 = ((VarDec)v15).ident;
        assertEquals("d", String.valueOf(v16.getText()));
        assertEquals(0, v15.getNest());
        List<ProcDec> v17 = ((Block) v0).procedureDecs;
        assertEquals(2, v17.size());
        ProcDec v18 = v17.get(0);
        assertThat("", v18, instanceOf(ProcDec.class));
        IToken v19 = ((ProcDec)v18).ident;
        assertEquals("p1", String.valueOf(v19.getText()));
        assertEquals(0, ((ProcDec)v18).getNest());
        Block v20 = ((ProcDec)v18).block;
        assertThat("", v20, instanceOf(Block.class));
        List<ConstDec> v21 = ((Block) v20).constDecs;
        assertEquals(2, v21.size());
        ConstDec v22 = v21.get(0);
        assertThat("", v22, instanceOf(ConstDec.class));
        IToken v23 = ((ConstDec)v22).ident;
        assertEquals("d", String.valueOf(v23.getText()));
        String v24 = (String)((ConstDec)v22).val;
        assertEquals("n", v24);
        assertEquals(1, v22.getNest());
        ConstDec v26 = v21.get(1);
        assertThat("", v26, instanceOf(ConstDec.class));
        IToken v27 = ((ConstDec)v26).ident;
        assertEquals("e", String.valueOf(v27.getText()));
        Integer v28 = (Integer)((ConstDec)v26).val;
        assertEquals(5, v28);
        assertEquals(1, v26.getNest());
        List<VarDec> v30 = ((Block) v20).varDecs;
        assertEquals(1, v30.size());
        VarDec v31 = v30.get(0);
        assertThat("", v31, instanceOf(VarDec.class));
        IToken v32 = ((VarDec)v31).ident;
        assertEquals("b", String.valueOf(v32.getText()));
        assertEquals(1, v31.getNest());
        List<ProcDec> v33 = ((Block) v20).procedureDecs;
        assertEquals(2, v33.size());
        ProcDec v34 = v33.get(0);
        assertThat("", v34, instanceOf(ProcDec.class));
        IToken v35 = ((ProcDec)v34).ident;
        assertEquals("p2", String.valueOf(v35.getText()));
        assertEquals(1, ((ProcDec)v34).getNest());
        Block v36 = ((ProcDec)v34).block;
        assertThat("", v36, instanceOf(Block.class));
        List<ConstDec> v37 = ((Block) v36).constDecs;
        assertEquals(0, v37.size());
        List<VarDec> v38 = ((Block) v36).varDecs;
        assertEquals(0, v38.size());
        List<ProcDec> v39 = ((Block) v36).procedureDecs;
        assertEquals(0, v39.size());
        Statement v40 = ((Block) v36).statement;
        assertThat("", v40, instanceOf(StatementAssign.class));
        Ident v41 = ((StatementAssign) v40).ident;
        assertThat("", v41, instanceOf(Ident.class));
        assertEquals("a", String.valueOf(v41.firstToken.getText()));
        assertEquals(2, ((Ident) v41).getNest());
        Declaration v42 = ((Ident) v41).getDec();
        IToken v43 = ((ConstDec)v42).ident;
        assertEquals("a", String.valueOf(v43.getText()));
        Integer v44 = (Integer)((ConstDec)v42).val;
        assertEquals(1, v44);
        assertEquals(0, v42.getNest());
        Expression v46 = ((StatementAssign) v40).expression;
        Expression v47 = ((ExpressionBinary) v46).e0;
        Expression v48 = ((ExpressionBinary) v47).e0;
        assertThat("", v48, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v48.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v48).getNest());
        Declaration v49 = ((ExpressionIdent) v48).getDec();
        IToken v50 = ((VarDec)v49).ident;
        assertEquals("b", String.valueOf(v50.getText()));
        assertEquals(1, v49.getNest());
        assertEquals("+", String.valueOf(((ExpressionBinary) v47).op.getText()));
        Expression v51 = ((ExpressionBinary) v47).e1;
        Expression v52 = ((ExpressionBinary) v51).e0;
        Expression v53 = ((ExpressionBinary) v52).e0;
        assertThat("", v53, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v53.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v53).getNest());
        Declaration v54 = ((ExpressionIdent) v53).getDec();
        IToken v55 = ((ConstDec)v54).ident;
        assertEquals("c", String.valueOf(v55.getText()));
        String v56 = (String)((ConstDec)v54).val;
        assertEquals("a", v56);
        assertEquals(0, v54.getNest());
        assertEquals("/", String.valueOf(((ExpressionBinary) v52).op.getText()));
        Expression v58 = ((ExpressionBinary) v52).e1;
        Expression v59 = ((ExpressionBinary) v58).e0;
        assertThat("", v59, instanceOf(ExpressionIdent.class));
        assertEquals("a", String.valueOf(v59.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v59).getNest());
        Declaration v60 = ((ExpressionIdent) v59).getDec();
        IToken v61 = ((ConstDec)v60).ident;
        assertEquals("a", String.valueOf(v61.getText()));
        Integer v62 = (Integer)((ConstDec)v60).val;
        assertEquals(1, v62);
        assertEquals(0, v60.getNest());
        assertEquals("-", String.valueOf(((ExpressionBinary) v58).op.getText()));
        Expression v64 = ((ExpressionBinary) v58).e1;
        assertThat("", v64, instanceOf(ExpressionNumLit.class));
        assertEquals("4", String.valueOf(v64.firstToken.getText()));
        assertEquals("/", String.valueOf(((ExpressionBinary) v51).op.getText()));
        Expression v65 = ((ExpressionBinary) v51).e1;
        assertThat("", v65, instanceOf(ExpressionIdent.class));
        assertEquals("e", String.valueOf(v65.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v65).getNest());
        Declaration v66 = ((ExpressionIdent) v65).getDec();
        IToken v67 = ((ConstDec)v66).ident;
        assertEquals("e", String.valueOf(v67.getText()));
        Integer v68 = (Integer)((ConstDec)v66).val;
        assertEquals(5, v68);
        assertEquals(1, v66.getNest());
        assertEquals("+", String.valueOf(((ExpressionBinary) v46).op.getText()));
        Expression v70 = ((ExpressionBinary) v46).e1;
        assertThat("", v70, instanceOf(ExpressionIdent.class));
        assertEquals("d", String.valueOf(v70.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v70).getNest());
        Declaration v71 = ((ExpressionIdent) v70).getDec();
        IToken v72 = ((ConstDec)v71).ident;
        assertEquals("d", String.valueOf(v72.getText()));
        String v73 = (String)((ConstDec)v71).val;
        assertEquals("n", v73);
        assertEquals(1, v71.getNest());
        ProcDec v75 = v33.get(1);
        assertThat("", v75, instanceOf(ProcDec.class));
        IToken v76 = ((ProcDec)v75).ident;
        assertEquals("p3", String.valueOf(v76.getText()));
        assertEquals(1, ((ProcDec)v75).getNest());
        Block v77 = ((ProcDec)v75).block;
        assertThat("", v77, instanceOf(Block.class));
        List<ConstDec> v78 = ((Block) v77).constDecs;
        assertEquals(0, v78.size());
        List<VarDec> v79 = ((Block) v77).varDecs;
        assertEquals(0, v79.size());
        List<ProcDec> v80 = ((Block) v77).procedureDecs;
        assertEquals(0, v80.size());
        Statement v81 = ((Block) v77).statement;
        assertThat("", v81, instanceOf(StatementAssign.class));
        Ident v82 = ((StatementAssign) v81).ident;
        assertThat("", v82, instanceOf(Ident.class));
        assertEquals("a", String.valueOf(v82.firstToken.getText()));
        assertEquals(2, ((Ident) v82).getNest());
        Declaration v83 = ((Ident) v82).getDec();
        IToken v84 = ((ConstDec)v83).ident;
        assertEquals("a", String.valueOf(v84.getText()));
        Integer v85 = (Integer)((ConstDec)v83).val;
        assertEquals(1, v85);
        assertEquals(0, v83.getNest());
        Expression v87 = ((StatementAssign) v81).expression;
        Expression v88 = ((ExpressionBinary) v87).e0;
        Expression v89 = ((ExpressionBinary) v88).e0;
        assertThat("", v89, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v89.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v89).getNest());
        Declaration v90 = ((ExpressionIdent) v89).getDec();
        IToken v91 = ((VarDec)v90).ident;
        assertEquals("b", String.valueOf(v91.getText()));
        assertEquals(1, v90.getNest());
        assertEquals("+", String.valueOf(((ExpressionBinary) v88).op.getText()));
        Expression v92 = ((ExpressionBinary) v88).e1;
        Expression v93 = ((ExpressionBinary) v92).e0;
        Expression v94 = ((ExpressionBinary) v93).e0;
        assertThat("", v94, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v94.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v94).getNest());
        Declaration v95 = ((ExpressionIdent) v94).getDec();
        IToken v96 = ((ConstDec)v95).ident;
        assertEquals("c", String.valueOf(v96.getText()));
        String v97 = (String)((ConstDec)v95).val;
        assertEquals("a", v97);
        assertEquals(0, v95.getNest());
        assertEquals("/", String.valueOf(((ExpressionBinary) v93).op.getText()));
        Expression v99 = ((ExpressionBinary) v93).e1;
        Expression v100 = ((ExpressionBinary) v99).e0;
        assertThat("", v100, instanceOf(ExpressionIdent.class));
        assertEquals("a", String.valueOf(v100.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v100).getNest());
        Declaration v101 = ((ExpressionIdent) v100).getDec();
        IToken v102 = ((ConstDec)v101).ident;
        assertEquals("a", String.valueOf(v102.getText()));
        Integer v103 = (Integer)((ConstDec)v101).val;
        assertEquals(1, v103);
        assertEquals(0, v101.getNest());
        assertEquals("-", String.valueOf(((ExpressionBinary) v99).op.getText()));
        Expression v105 = ((ExpressionBinary) v99).e1;
        assertThat("", v105, instanceOf(ExpressionNumLit.class));
        assertEquals("4", String.valueOf(v105.firstToken.getText()));
        assertEquals("/", String.valueOf(((ExpressionBinary) v92).op.getText()));
        Expression v106 = ((ExpressionBinary) v92).e1;
        assertThat("", v106, instanceOf(ExpressionIdent.class));
        assertEquals("e", String.valueOf(v106.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v106).getNest());
        Declaration v107 = ((ExpressionIdent) v106).getDec();
        IToken v108 = ((ConstDec)v107).ident;
        assertEquals("e", String.valueOf(v108.getText()));
        Integer v109 = (Integer)((ConstDec)v107).val;
        assertEquals(5, v109);
        assertEquals(1, v107.getNest());
        assertEquals("+", String.valueOf(((ExpressionBinary) v87).op.getText()));
        Expression v111 = ((ExpressionBinary) v87).e1;
        assertThat("", v111, instanceOf(ExpressionIdent.class));
        assertEquals("d", String.valueOf(v111.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v111).getNest());
        Declaration v112 = ((ExpressionIdent) v111).getDec();
        IToken v113 = ((ConstDec)v112).ident;
        assertEquals("d", String.valueOf(v113.getText()));
        String v114 = (String)((ConstDec)v112).val;
        assertEquals("n", v114);
        assertEquals(1, v112.getNest());
        Statement v116 = ((Block) v20).statement;
        assertThat("", v116, instanceOf(StatementAssign.class));
        Ident v117 = ((StatementAssign) v116).ident;
        assertThat("", v117, instanceOf(Ident.class));
        assertEquals("a", String.valueOf(v117.firstToken.getText()));
        assertEquals(1, ((Ident) v117).getNest());
        Declaration v118 = ((Ident) v117).getDec();
        IToken v119 = ((ConstDec)v118).ident;
        assertEquals("a", String.valueOf(v119.getText()));
        Integer v120 = (Integer)((ConstDec)v118).val;
        assertEquals(1, v120);
        assertEquals(0, v118.getNest());
        Expression v122 = ((StatementAssign) v116).expression;
        Expression v123 = ((ExpressionBinary) v122).e0;
        assertThat("", v123, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v123.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v123).getNest());
        Declaration v124 = ((ExpressionIdent) v123).getDec();
        IToken v125 = ((VarDec)v124).ident;
        assertEquals("b", String.valueOf(v125.getText()));
        assertEquals(1, v124.getNest());
        assertEquals("+", String.valueOf(((ExpressionBinary) v122).op.getText()));
        Expression v126 = ((ExpressionBinary) v122).e1;
        Expression v127 = ((ExpressionBinary) v126).e0;
        Expression v128 = ((ExpressionBinary) v127).e0;
        assertThat("", v128, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v128.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v128).getNest());
        Declaration v129 = ((ExpressionIdent) v128).getDec();
        IToken v130 = ((ConstDec)v129).ident;
        assertEquals("c", String.valueOf(v130.getText()));
        String v131 = (String)((ConstDec)v129).val;
        assertEquals("a", v131);
        assertEquals(0, v129.getNest());
        assertEquals("/", String.valueOf(((ExpressionBinary) v127).op.getText()));
        Expression v133 = ((ExpressionBinary) v127).e1;
        Expression v134 = ((ExpressionBinary) v133).e0;
        assertThat("", v134, instanceOf(ExpressionIdent.class));
        assertEquals("a", String.valueOf(v134.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v134).getNest());
        Declaration v135 = ((ExpressionIdent) v134).getDec();
        IToken v136 = ((ConstDec)v135).ident;
        assertEquals("a", String.valueOf(v136.getText()));
        Integer v137 = (Integer)((ConstDec)v135).val;
        assertEquals(1, v137);
        assertEquals(0, v135.getNest());
        assertEquals("-", String.valueOf(((ExpressionBinary) v133).op.getText()));
        Expression v139 = ((ExpressionBinary) v133).e1;
        assertThat("", v139, instanceOf(ExpressionNumLit.class));
        assertEquals("4", String.valueOf(v139.firstToken.getText()));
        assertEquals("/", String.valueOf(((ExpressionBinary) v126).op.getText()));
        Expression v140 = ((ExpressionBinary) v126).e1;
        assertThat("", v140, instanceOf(ExpressionIdent.class));
        assertEquals("d", String.valueOf(v140.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v140).getNest());
        Declaration v141 = ((ExpressionIdent) v140).getDec();
        IToken v142 = ((ConstDec)v141).ident;
        assertEquals("d", String.valueOf(v142.getText()));
        String v143 = (String)((ConstDec)v141).val;
        assertEquals("n", v143);
        assertEquals(1, v141.getNest());
        ProcDec v145 = v17.get(1);
        assertThat("", v145, instanceOf(ProcDec.class));
        IToken v146 = ((ProcDec)v145).ident;
        assertEquals("p4", String.valueOf(v146.getText()));
        assertEquals(0, ((ProcDec)v145).getNest());
        Block v147 = ((ProcDec)v145).block;
        assertThat("", v147, instanceOf(Block.class));
        List<ConstDec> v148 = ((Block) v147).constDecs;
        assertEquals(0, v148.size());
        List<VarDec> v149 = ((Block) v147).varDecs;
        assertEquals(0, v149.size());
        List<ProcDec> v150 = ((Block) v147).procedureDecs;
        assertEquals(0, v150.size());
        Statement v151 = ((Block) v147).statement;
        assertThat("", v151, instanceOf(StatementAssign.class));
        Ident v152 = ((StatementAssign) v151).ident;
        assertThat("", v152, instanceOf(Ident.class));
        assertEquals("c", String.valueOf(v152.firstToken.getText()));
        assertEquals(1, ((Ident) v152).getNest());
        Declaration v153 = ((Ident) v152).getDec();
        IToken v154 = ((ConstDec)v153).ident;
        assertEquals("c", String.valueOf(v154.getText()));
        String v155 = (String)((ConstDec)v153).val;
        assertEquals("a", v155);
        assertEquals(0, v153.getNest());
        Expression v157 = ((StatementAssign) v151).expression;
        assertThat("", v157, instanceOf(ExpressionNumLit.class));
        assertEquals("6", String.valueOf(v157.firstToken.getText()));
        Statement v158 = ((Block) v0).statement;
        assertThat("", v158, instanceOf(StatementEmpty.class));
    }
    
    @Test
    void paramTest16() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            PROCEDURE p2;
                                VAR p1;
                                CALL p3
                            ;
                            PROCEDURE p3;
                                VAR c;
                                CALL p2
                            ;
                        ;
                        PROCEDURE p4;
                        ;
                        .
                        """;
        ASTNode ast = getDecoratedAST(input);
        assertThat("", ast, instanceOf(Program.class));
        Block v0 = ((Program) ast).block;
        assertThat("", v0, instanceOf(Block.class));
        List<ConstDec> v1 = ((Block) v0).constDecs;
        assertEquals(3, v1.size());
        ConstDec v2 = v1.get(0);
        assertThat("", v2, instanceOf(ConstDec.class));
        IToken v3 = ((ConstDec)v2).ident;
        assertEquals("a", String.valueOf(v3.getText()));
        Integer v4 = (Integer)((ConstDec)v2).val;
        assertEquals(1, v4);
        assertEquals(0, v2.getNest());
        ConstDec v6 = v1.get(1);
        assertThat("", v6, instanceOf(ConstDec.class));
        IToken v7 = ((ConstDec)v6).ident;
        assertEquals("b", String.valueOf(v7.getText()));
        Boolean v8 = (Boolean)((ConstDec)v6).val;
        assertEquals(true, v8);
        assertEquals(0, v6.getNest());
        ConstDec v10 = v1.get(2);
        assertThat("", v10, instanceOf(ConstDec.class));
        IToken v11 = ((ConstDec)v10).ident;
        assertEquals("c", String.valueOf(v11.getText()));
        String v12 = (String)((ConstDec)v10).val;
        assertEquals("a", v12);
        assertEquals(0, v10.getNest());
        List<VarDec> v14 = ((Block) v0).varDecs;
        assertEquals(1, v14.size());
        VarDec v15 = v14.get(0);
        assertThat("", v15, instanceOf(VarDec.class));
        IToken v16 = ((VarDec)v15).ident;
        assertEquals("d", String.valueOf(v16.getText()));
        assertEquals(0, v15.getNest());
        List<ProcDec> v17 = ((Block) v0).procedureDecs;
        assertEquals(2, v17.size());
        ProcDec v18 = v17.get(0);
        assertThat("", v18, instanceOf(ProcDec.class));
        IToken v19 = ((ProcDec)v18).ident;
        assertEquals("p1", String.valueOf(v19.getText()));
        assertEquals(0, ((ProcDec)v18).getNest());
        Block v20 = ((ProcDec)v18).block;
        assertThat("", v20, instanceOf(Block.class));
        List<ConstDec> v21 = ((Block) v20).constDecs;
        assertEquals(0, v21.size());
        List<VarDec> v22 = ((Block) v20).varDecs;
        assertEquals(0, v22.size());
        List<ProcDec> v23 = ((Block) v20).procedureDecs;
        assertEquals(2, v23.size());
        ProcDec v24 = v23.get(0);
        assertThat("", v24, instanceOf(ProcDec.class));
        IToken v25 = ((ProcDec)v24).ident;
        assertEquals("p2", String.valueOf(v25.getText()));
        assertEquals(1, ((ProcDec)v24).getNest());
        Block v26 = ((ProcDec)v24).block;
        assertThat("", v26, instanceOf(Block.class));
        List<ConstDec> v27 = ((Block) v26).constDecs;
        assertEquals(0, v27.size());
        List<VarDec> v28 = ((Block) v26).varDecs;
        assertEquals(1, v28.size());
        VarDec v29 = v28.get(0);
        assertThat("", v29, instanceOf(VarDec.class));
        IToken v30 = ((VarDec)v29).ident;
        assertEquals("p1", String.valueOf(v30.getText()));
        assertEquals(2, v29.getNest());
        List<ProcDec> v31 = ((Block) v26).procedureDecs;
        assertEquals(0, v31.size());
        Statement v32 = ((Block) v26).statement;
        assertThat("", v32, instanceOf(StatementCall.class));
        Ident v33 = ((StatementCall) v32).ident;
        assertThat("", v33, instanceOf(Ident.class));
        assertEquals("p3", String.valueOf(v33.firstToken.getText()));
        assertEquals(2, ((Ident) v33).getNest());
        Declaration v34 = ((Ident) v33).getDec();
        IToken v35 = ((ProcDec)v34).ident;
        assertEquals("p3", String.valueOf(v35.getText()));
        assertEquals(1, ((ProcDec)v34).getNest());
        ProcDec v36 = v23.get(1);
        assertThat("", v36, instanceOf(ProcDec.class));
        IToken v37 = ((ProcDec)v36).ident;
        assertEquals("p3", String.valueOf(v37.getText()));
        assertEquals(1, ((ProcDec)v36).getNest());
        Block v38 = ((ProcDec)v36).block;
        assertThat("", v38, instanceOf(Block.class));
        List<ConstDec> v39 = ((Block) v38).constDecs;
        assertEquals(0, v39.size());
        List<VarDec> v40 = ((Block) v38).varDecs;
        assertEquals(1, v40.size());
        VarDec v41 = v40.get(0);
        assertThat("", v41, instanceOf(VarDec.class));
        IToken v42 = ((VarDec)v41).ident;
        assertEquals("c", String.valueOf(v42.getText()));
        assertEquals(2, v41.getNest());
        List<ProcDec> v43 = ((Block) v38).procedureDecs;
        assertEquals(0, v43.size());
        Statement v44 = ((Block) v38).statement;
        assertThat("", v44, instanceOf(StatementCall.class));
        Ident v45 = ((StatementCall) v44).ident;
        assertThat("", v45, instanceOf(Ident.class));
        assertEquals("p2", String.valueOf(v45.firstToken.getText()));
        assertEquals(2, ((Ident) v45).getNest());
        Declaration v46 = ((Ident) v45).getDec();
        IToken v47 = ((ProcDec)v46).ident;
        assertEquals("p2", String.valueOf(v47.getText()));
        assertEquals(1, ((ProcDec)v46).getNest());
        Statement v48 = ((Block) v20).statement;
        assertThat("", v48, instanceOf(StatementEmpty.class));
        ProcDec v49 = v17.get(1);
        assertThat("", v49, instanceOf(ProcDec.class));
        IToken v50 = ((ProcDec)v49).ident;
        assertEquals("p4", String.valueOf(v50.getText()));
        assertEquals(0, ((ProcDec)v49).getNest());
        Block v51 = ((ProcDec)v49).block;
        assertThat("", v51, instanceOf(Block.class));
        List<ConstDec> v52 = ((Block) v51).constDecs;
        assertEquals(0, v52.size());
        List<VarDec> v53 = ((Block) v51).varDecs;
        assertEquals(0, v53.size());
        List<ProcDec> v54 = ((Block) v51).procedureDecs;
        assertEquals(0, v54.size());
        Statement v55 = ((Block) v51).statement;
        assertThat("", v55, instanceOf(StatementEmpty.class));
        Statement v56 = ((Block) v0).statement;
        assertThat("", v56, instanceOf(StatementEmpty.class));
    }
    
    @Test
    void paramTest17() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            CONST c = 4;
                            PROCEDURE p2;
                                VAR c;
                                VAR e;
                                PROCEDURE p3;
                                    VAR e;
                                        ? c
                                    ;
                                PROCEDURE p4;
                                    e := e + c
                                ;
                            ;
                        ;
                        .
                        """;
        ASTNode ast = getDecoratedAST(input);
        assertThat("", ast, instanceOf(Program.class));
        Block v0 = ((Program) ast).block;
        assertThat("", v0, instanceOf(Block.class));
        List<ConstDec> v1 = ((Block) v0).constDecs;
        assertEquals(3, v1.size());
        ConstDec v2 = v1.get(0);
        assertThat("", v2, instanceOf(ConstDec.class));
        IToken v3 = ((ConstDec)v2).ident;
        assertEquals("a", String.valueOf(v3.getText()));
        Integer v4 = (Integer)((ConstDec)v2).val;
        assertEquals(1, v4);
        assertEquals(0, v2.getNest());
        ConstDec v6 = v1.get(1);
        assertThat("", v6, instanceOf(ConstDec.class));
        IToken v7 = ((ConstDec)v6).ident;
        assertEquals("b", String.valueOf(v7.getText()));
        Boolean v8 = (Boolean)((ConstDec)v6).val;
        assertEquals(true, v8);
        assertEquals(0, v6.getNest());
        ConstDec v10 = v1.get(2);
        assertThat("", v10, instanceOf(ConstDec.class));
        IToken v11 = ((ConstDec)v10).ident;
        assertEquals("c", String.valueOf(v11.getText()));
        String v12 = (String)((ConstDec)v10).val;
        assertEquals("a", v12);
        assertEquals(0, v10.getNest());
        List<VarDec> v14 = ((Block) v0).varDecs;
        assertEquals(1, v14.size());
        VarDec v15 = v14.get(0);
        assertThat("", v15, instanceOf(VarDec.class));
        IToken v16 = ((VarDec)v15).ident;
        assertEquals("d", String.valueOf(v16.getText()));
        assertEquals(0, v15.getNest());
        List<ProcDec> v17 = ((Block) v0).procedureDecs;
        assertEquals(1, v17.size());
        ProcDec v18 = v17.get(0);
        assertThat("", v18, instanceOf(ProcDec.class));
        IToken v19 = ((ProcDec)v18).ident;
        assertEquals("p1", String.valueOf(v19.getText()));
        assertEquals(0, ((ProcDec)v18).getNest());
        Block v20 = ((ProcDec)v18).block;
        assertThat("", v20, instanceOf(Block.class));
        List<ConstDec> v21 = ((Block) v20).constDecs;
        assertEquals(1, v21.size());
        ConstDec v22 = v21.get(0);
        assertThat("", v22, instanceOf(ConstDec.class));
        IToken v23 = ((ConstDec)v22).ident;
        assertEquals("c", String.valueOf(v23.getText()));
        Integer v24 = (Integer)((ConstDec)v22).val;
        assertEquals(4, v24);
        assertEquals(1, v22.getNest());
        List<VarDec> v26 = ((Block) v20).varDecs;
        assertEquals(0, v26.size());
        List<ProcDec> v27 = ((Block) v20).procedureDecs;
        assertEquals(1, v27.size());
        ProcDec v28 = v27.get(0);
        assertThat("", v28, instanceOf(ProcDec.class));
        IToken v29 = ((ProcDec)v28).ident;
        assertEquals("p2", String.valueOf(v29.getText()));
        assertEquals(1, ((ProcDec)v28).getNest());
        Block v30 = ((ProcDec)v28).block;
        assertThat("", v30, instanceOf(Block.class));
        List<ConstDec> v31 = ((Block) v30).constDecs;
        assertEquals(0, v31.size());
        List<VarDec> v32 = ((Block) v30).varDecs;
        assertEquals(2, v32.size());
        VarDec v33 = v32.get(0);
        assertThat("", v33, instanceOf(VarDec.class));
        IToken v34 = ((VarDec)v33).ident;
        assertEquals("c", String.valueOf(v34.getText()));
        assertEquals(2, v33.getNest());
        VarDec v35 = v32.get(1);
        assertThat("", v35, instanceOf(VarDec.class));
        IToken v36 = ((VarDec)v35).ident;
        assertEquals("e", String.valueOf(v36.getText()));
        assertEquals(2, v35.getNest());
        List<ProcDec> v37 = ((Block) v30).procedureDecs;
        assertEquals(2, v37.size());
        ProcDec v38 = v37.get(0);
        assertThat("", v38, instanceOf(ProcDec.class));
        IToken v39 = ((ProcDec)v38).ident;
        assertEquals("p3", String.valueOf(v39.getText()));
        assertEquals(2, ((ProcDec)v38).getNest());
        Block v40 = ((ProcDec)v38).block;
        assertThat("", v40, instanceOf(Block.class));
        List<ConstDec> v41 = ((Block) v40).constDecs;
        assertEquals(0, v41.size());
        List<VarDec> v42 = ((Block) v40).varDecs;
        assertEquals(1, v42.size());
        VarDec v43 = v42.get(0);
        assertThat("", v43, instanceOf(VarDec.class));
        IToken v44 = ((VarDec)v43).ident;
        assertEquals("e", String.valueOf(v44.getText()));
        assertEquals(3, v43.getNest());
        List<ProcDec> v45 = ((Block) v40).procedureDecs;
        assertEquals(0, v45.size());
        Statement v46 = ((Block) v40).statement;
        assertThat("", v46, instanceOf(StatementInput.class));
        Ident v47 = ((StatementInput)v46).ident;
        assertThat("", v47, instanceOf(Ident.class));
        assertEquals("c", String.valueOf(v47.firstToken.getText()));
        assertEquals(3, ((Ident) v47).getNest());
        Declaration v48 = ((Ident) v47).getDec();
        IToken v49 = ((VarDec)v48).ident;
        assertEquals("c", String.valueOf(v49.getText()));
        assertEquals(2, v48.getNest());
        ProcDec v50 = v37.get(1);
        assertThat("", v50, instanceOf(ProcDec.class));
        IToken v51 = ((ProcDec)v50).ident;
        assertEquals("p4", String.valueOf(v51.getText()));
        assertEquals(2, ((ProcDec)v50).getNest());
        Block v52 = ((ProcDec)v50).block;
        assertThat("", v52, instanceOf(Block.class));
        List<ConstDec> v53 = ((Block) v52).constDecs;
        assertEquals(0, v53.size());
        List<VarDec> v54 = ((Block) v52).varDecs;
        assertEquals(0, v54.size());
        List<ProcDec> v55 = ((Block) v52).procedureDecs;
        assertEquals(0, v55.size());
        Statement v56 = ((Block) v52).statement;
        assertThat("", v56, instanceOf(StatementAssign.class));
        Ident v57 = ((StatementAssign) v56).ident;
        assertThat("", v57, instanceOf(Ident.class));
        assertEquals("e", String.valueOf(v57.firstToken.getText()));
        assertEquals(3, ((Ident) v57).getNest());
        Declaration v58 = ((Ident) v57).getDec();
        IToken v59 = ((VarDec)v58).ident;
        assertEquals("e", String.valueOf(v59.getText()));
        assertEquals(2, v58.getNest());
        Expression v60 = ((StatementAssign) v56).expression;
        Expression v61 = ((ExpressionBinary) v60).e0;
        assertThat("", v61, instanceOf(ExpressionIdent.class));
        assertEquals("e", String.valueOf(v61.firstToken.getText()));
        assertEquals(3, ((ExpressionIdent) v61).getNest());
        Declaration v62 = ((ExpressionIdent) v61).getDec();
        IToken v63 = ((VarDec)v62).ident;
        assertEquals("e", String.valueOf(v63.getText()));
        assertEquals(2, v62.getNest());
        assertEquals("+", String.valueOf(((ExpressionBinary) v60).op.getText()));
        Expression v64 = ((ExpressionBinary) v60).e1;
        assertThat("", v64, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v64.firstToken.getText()));
        assertEquals(3, ((ExpressionIdent) v64).getNest());
        Declaration v65 = ((ExpressionIdent) v64).getDec();
        IToken v66 = ((VarDec)v65).ident;
        assertEquals("c", String.valueOf(v66.getText()));
        assertEquals(2, v65.getNest());
        Statement v67 = ((Block) v30).statement;
        assertThat("", v67, instanceOf(StatementEmpty.class));
        Statement v68 = ((Block) v20).statement;
        assertThat("", v68, instanceOf(StatementEmpty.class));
        Statement v69 = ((Block) v0).statement;
        assertThat("", v69, instanceOf(StatementEmpty.class));
    }
    
    @Test
    void paramTest18() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            PROCEDURE p2;
                                ! c
                            ;
                            PROCEDURE p3;
                                ! d
                            ;
                            ! p2
                        ;
                        PROCEDURE p4;
                        ;
                        .
                        """;
        ASTNode ast = getDecoratedAST(input);
        assertThat("", ast, instanceOf(Program.class));
        Block v0 = ((Program) ast).block;
        assertThat("", v0, instanceOf(Block.class));
        List<ConstDec> v1 = ((Block) v0).constDecs;
        assertEquals(3, v1.size());
        ConstDec v2 = v1.get(0);
        assertThat("", v2, instanceOf(ConstDec.class));
        IToken v3 = ((ConstDec)v2).ident;
        assertEquals("a", String.valueOf(v3.getText()));
        Integer v4 = (Integer)((ConstDec)v2).val;
        assertEquals(1, v4);
        assertEquals(0, v2.getNest());
        ConstDec v6 = v1.get(1);
        assertThat("", v6, instanceOf(ConstDec.class));
        IToken v7 = ((ConstDec)v6).ident;
        assertEquals("b", String.valueOf(v7.getText()));
        Boolean v8 = (Boolean)((ConstDec)v6).val;
        assertEquals(true, v8);
        assertEquals(0, v6.getNest());
        ConstDec v10 = v1.get(2);
        assertThat("", v10, instanceOf(ConstDec.class));
        IToken v11 = ((ConstDec)v10).ident;
        assertEquals("c", String.valueOf(v11.getText()));
        String v12 = (String)((ConstDec)v10).val;
        assertEquals("a", v12);
        assertEquals(0, v10.getNest());
        List<VarDec> v14 = ((Block) v0).varDecs;
        assertEquals(1, v14.size());
        VarDec v15 = v14.get(0);
        assertThat("", v15, instanceOf(VarDec.class));
        IToken v16 = ((VarDec)v15).ident;
        assertEquals("d", String.valueOf(v16.getText()));
        assertEquals(0, v15.getNest());
        List<ProcDec> v17 = ((Block) v0).procedureDecs;
        assertEquals(2, v17.size());
        ProcDec v18 = v17.get(0);
        assertThat("", v18, instanceOf(ProcDec.class));
        IToken v19 = ((ProcDec)v18).ident;
        assertEquals("p1", String.valueOf(v19.getText()));
        assertEquals(0, ((ProcDec)v18).getNest());
        Block v20 = ((ProcDec)v18).block;
        assertThat("", v20, instanceOf(Block.class));
        List<ConstDec> v21 = ((Block) v20).constDecs;
        assertEquals(0, v21.size());
        List<VarDec> v22 = ((Block) v20).varDecs;
        assertEquals(0, v22.size());
        List<ProcDec> v23 = ((Block) v20).procedureDecs;
        assertEquals(2, v23.size());
        ProcDec v24 = v23.get(0);
        assertThat("", v24, instanceOf(ProcDec.class));
        IToken v25 = ((ProcDec)v24).ident;
        assertEquals("p2", String.valueOf(v25.getText()));
        assertEquals(1, ((ProcDec)v24).getNest());
        Block v26 = ((ProcDec)v24).block;
        assertThat("", v26, instanceOf(Block.class));
        List<ConstDec> v27 = ((Block) v26).constDecs;
        assertEquals(0, v27.size());
        List<VarDec> v28 = ((Block) v26).varDecs;
        assertEquals(0, v28.size());
        List<ProcDec> v29 = ((Block) v26).procedureDecs;
        assertEquals(0, v29.size());
        Statement v30 = ((Block) v26).statement;
        assertThat("", v30, instanceOf(StatementOutput.class));
        Expression v31 = ((StatementOutput) v30).expression;
        assertThat("", v31, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v31.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v31).getNest());
        Declaration v32 = ((ExpressionIdent) v31).getDec();
        IToken v33 = ((ConstDec)v32).ident;
        assertEquals("c", String.valueOf(v33.getText()));
        String v34 = (String)((ConstDec)v32).val;
        assertEquals("a", v34);
        assertEquals(0, v32.getNest());
        ProcDec v36 = v23.get(1);
        assertThat("", v36, instanceOf(ProcDec.class));
        IToken v37 = ((ProcDec)v36).ident;
        assertEquals("p3", String.valueOf(v37.getText()));
        assertEquals(1, ((ProcDec)v36).getNest());
        Block v38 = ((ProcDec)v36).block;
        assertThat("", v38, instanceOf(Block.class));
        List<ConstDec> v39 = ((Block) v38).constDecs;
        assertEquals(0, v39.size());
        List<VarDec> v40 = ((Block) v38).varDecs;
        assertEquals(0, v40.size());
        List<ProcDec> v41 = ((Block) v38).procedureDecs;
        assertEquals(0, v41.size());
        Statement v42 = ((Block) v38).statement;
        assertThat("", v42, instanceOf(StatementOutput.class));
        Expression v43 = ((StatementOutput) v42).expression;
        assertThat("", v43, instanceOf(ExpressionIdent.class));
        assertEquals("d", String.valueOf(v43.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v43).getNest());
        Declaration v44 = ((ExpressionIdent) v43).getDec();
        IToken v45 = ((VarDec)v44).ident;
        assertEquals("d", String.valueOf(v45.getText()));
        assertEquals(0, v44.getNest());
        Statement v46 = ((Block) v20).statement;
        assertThat("", v46, instanceOf(StatementOutput.class));
        Expression v47 = ((StatementOutput) v46).expression;
        assertThat("", v47, instanceOf(ExpressionIdent.class));
        assertEquals("p2", String.valueOf(v47.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v47).getNest());
        Declaration v48 = ((ExpressionIdent) v47).getDec();
        IToken v49 = ((ProcDec)v48).ident;
        assertEquals("p2", String.valueOf(v49.getText()));
        assertEquals(1, ((ProcDec)v48).getNest());
        ProcDec v50 = v17.get(1);
        assertThat("", v50, instanceOf(ProcDec.class));
        IToken v51 = ((ProcDec)v50).ident;
        assertEquals("p4", String.valueOf(v51.getText()));
        assertEquals(0, ((ProcDec)v50).getNest());
        Block v52 = ((ProcDec)v50).block;
        assertThat("", v52, instanceOf(Block.class));
        List<ConstDec> v53 = ((Block) v52).constDecs;
        assertEquals(0, v53.size());
        List<VarDec> v54 = ((Block) v52).varDecs;
        assertEquals(0, v54.size());
        List<ProcDec> v55 = ((Block) v52).procedureDecs;
        assertEquals(0, v55.size());
        Statement v56 = ((Block) v52).statement;
        assertThat("", v56, instanceOf(StatementEmpty.class));
        Statement v57 = ((Block) v0).statement;
        assertThat("", v57, instanceOf(StatementEmpty.class));
    }
    
    @Test
    void paramTest19() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            VAR a, d;
                            PROCEDURE p2;
                                BEGIN
                                    ! c;
                                    b := c;
                                    c := b-d>=TRUE
                                END
                            ;
                            BEGIN
                                    ! c;
                                    b := c;
                                    c := b-d>=TRUE
                            END
                        ;
                        PROCEDURE p3;
                            VAR a, d;
                            PROCEDURE p2;
                                BEGIN
                                    ! c;
                                    b := c;
                                    c := b-d>=TRUE
                                END
                            ;
                            BEGIN
                                    ! c;
                                    b := c;
                                    c := b-d>=TRUE
                            END
                        ;
                        .
                        """;
        ASTNode ast = getDecoratedAST(input);
        assertThat("", ast, instanceOf(Program.class));
        Block v0 = ((Program) ast).block;
        assertThat("", v0, instanceOf(Block.class));
        List<ConstDec> v1 = ((Block) v0).constDecs;
        assertEquals(3, v1.size());
        ConstDec v2 = v1.get(0);
        assertThat("", v2, instanceOf(ConstDec.class));
        IToken v3 = ((ConstDec)v2).ident;
        assertEquals("a", String.valueOf(v3.getText()));
        Integer v4 = (Integer)((ConstDec)v2).val;
        assertEquals(1, v4);
        assertEquals(0, v2.getNest());
        ConstDec v6 = v1.get(1);
        assertThat("", v6, instanceOf(ConstDec.class));
        IToken v7 = ((ConstDec)v6).ident;
        assertEquals("b", String.valueOf(v7.getText()));
        Boolean v8 = (Boolean)((ConstDec)v6).val;
        assertEquals(true, v8);
        assertEquals(0, v6.getNest());
        ConstDec v10 = v1.get(2);
        assertThat("", v10, instanceOf(ConstDec.class));
        IToken v11 = ((ConstDec)v10).ident;
        assertEquals("c", String.valueOf(v11.getText()));
        String v12 = (String)((ConstDec)v10).val;
        assertEquals("a", v12);
        assertEquals(0, v10.getNest());
        List<VarDec> v14 = ((Block) v0).varDecs;
        assertEquals(1, v14.size());
        VarDec v15 = v14.get(0);
        assertThat("", v15, instanceOf(VarDec.class));
        IToken v16 = ((VarDec)v15).ident;
        assertEquals("d", String.valueOf(v16.getText()));
        assertEquals(0, v15.getNest());
        List<ProcDec> v17 = ((Block) v0).procedureDecs;
        assertEquals(2, v17.size());
        ProcDec v18 = v17.get(0);
        assertThat("", v18, instanceOf(ProcDec.class));
        IToken v19 = ((ProcDec)v18).ident;
        assertEquals("p1", String.valueOf(v19.getText()));
        assertEquals(0, ((ProcDec)v18).getNest());
        Block v20 = ((ProcDec)v18).block;
        assertThat("", v20, instanceOf(Block.class));
        List<ConstDec> v21 = ((Block) v20).constDecs;
        assertEquals(0, v21.size());
        List<VarDec> v22 = ((Block) v20).varDecs;
        assertEquals(2, v22.size());
        VarDec v23 = v22.get(0);
        assertThat("", v23, instanceOf(VarDec.class));
        IToken v24 = ((VarDec)v23).ident;
        assertEquals("a", String.valueOf(v24.getText()));
        assertEquals(1, v23.getNest());
        VarDec v25 = v22.get(1);
        assertThat("", v25, instanceOf(VarDec.class));
        IToken v26 = ((VarDec)v25).ident;
        assertEquals("d", String.valueOf(v26.getText()));
        assertEquals(1, v25.getNest());
        List<ProcDec> v27 = ((Block) v20).procedureDecs;
        assertEquals(1, v27.size());
        ProcDec v28 = v27.get(0);
        assertThat("", v28, instanceOf(ProcDec.class));
        IToken v29 = ((ProcDec)v28).ident;
        assertEquals("p2", String.valueOf(v29.getText()));
        assertEquals(1, ((ProcDec)v28).getNest());
        Block v30 = ((ProcDec)v28).block;
        assertThat("", v30, instanceOf(Block.class));
        List<ConstDec> v31 = ((Block) v30).constDecs;
        assertEquals(0, v31.size());
        List<VarDec> v32 = ((Block) v30).varDecs;
        assertEquals(0, v32.size());
        List<ProcDec> v33 = ((Block) v30).procedureDecs;
        assertEquals(0, v33.size());
        Statement v34 = ((Block) v30).statement;
        assertThat("", v34, instanceOf(StatementBlock.class));
        Statement v35 = ((StatementBlock) v34).statements.get(0);
        assertThat("", v35, instanceOf(StatementOutput.class));
        Expression v36 = ((StatementOutput) v35).expression;
        assertThat("", v36, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v36.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v36).getNest());
        Declaration v37 = ((ExpressionIdent) v36).getDec();
        IToken v38 = ((ConstDec)v37).ident;
        assertEquals("c", String.valueOf(v38.getText()));
        String v39 = (String)((ConstDec)v37).val;
        assertEquals("a", v39);
        assertEquals(0, v37.getNest());
        Statement v41 = ((StatementBlock) v34).statements.get(1);
        assertThat("", v41, instanceOf(StatementAssign.class));
        Ident v42 = ((StatementAssign) v41).ident;
        assertThat("", v42, instanceOf(Ident.class));
        assertEquals("b", String.valueOf(v42.firstToken.getText()));
        assertEquals(2, ((Ident) v42).getNest());
        Declaration v43 = ((Ident) v42).getDec();
        IToken v44 = ((ConstDec)v43).ident;
        assertEquals("b", String.valueOf(v44.getText()));
        Boolean v45 = (Boolean)((ConstDec)v43).val;
        assertEquals(true, v45);
        assertEquals(0, v43.getNest());
        Expression v47 = ((StatementAssign) v41).expression;
        assertThat("", v47, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v47.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v47).getNest());
        Declaration v48 = ((ExpressionIdent) v47).getDec();
        IToken v49 = ((ConstDec)v48).ident;
        assertEquals("c", String.valueOf(v49.getText()));
        String v50 = (String)((ConstDec)v48).val;
        assertEquals("a", v50);
        assertEquals(0, v48.getNest());
        Statement v52 = ((StatementBlock) v34).statements.get(2);
        assertThat("", v52, instanceOf(StatementAssign.class));
        Ident v53 = ((StatementAssign) v52).ident;
        assertThat("", v53, instanceOf(Ident.class));
        assertEquals("c", String.valueOf(v53.firstToken.getText()));
        assertEquals(2, ((Ident) v53).getNest());
        Declaration v54 = ((Ident) v53).getDec();
        IToken v55 = ((ConstDec)v54).ident;
        assertEquals("c", String.valueOf(v55.getText()));
        String v56 = (String)((ConstDec)v54).val;
        assertEquals("a", v56);
        assertEquals(0, v54.getNest());
        Expression v58 = ((StatementAssign) v52).expression;
        Expression v59 = ((ExpressionBinary) v58).e0;
        Expression v60 = ((ExpressionBinary) v59).e0;
        assertThat("", v60, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v60.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v60).getNest());
        Declaration v61 = ((ExpressionIdent) v60).getDec();
        IToken v62 = ((ConstDec)v61).ident;
        assertEquals("b", String.valueOf(v62.getText()));
        Boolean v63 = (Boolean)((ConstDec)v61).val;
        assertEquals(true, v63);
        assertEquals(0, v61.getNest());
        assertEquals("-", String.valueOf(((ExpressionBinary) v59).op.getText()));
        Expression v65 = ((ExpressionBinary) v59).e1;
        assertThat("", v65, instanceOf(ExpressionIdent.class));
        assertEquals("d", String.valueOf(v65.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v65).getNest());
        Declaration v66 = ((ExpressionIdent) v65).getDec();
        IToken v67 = ((VarDec)v66).ident;
        assertEquals("d", String.valueOf(v67.getText()));
        assertEquals(1, v66.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v58).op.getText()));
        Expression v68 = ((ExpressionBinary) v58).e1;
        assertThat("", v68, instanceOf(ExpressionBooleanLit.class));
        assertEquals("TRUE", String.valueOf(v68.firstToken.getText()));
        Statement v69 = ((Block) v20).statement;
        assertThat("", v69, instanceOf(StatementBlock.class));
        Statement v70 = ((StatementBlock) v69).statements.get(0);
        assertThat("", v70, instanceOf(StatementOutput.class));
        Expression v71 = ((StatementOutput) v70).expression;
        assertThat("", v71, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v71.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v71).getNest());
        Declaration v72 = ((ExpressionIdent) v71).getDec();
        IToken v73 = ((ConstDec)v72).ident;
        assertEquals("c", String.valueOf(v73.getText()));
        String v74 = (String)((ConstDec)v72).val;
        assertEquals("a", v74);
        assertEquals(0, v72.getNest());
        Statement v76 = ((StatementBlock) v69).statements.get(1);
        assertThat("", v76, instanceOf(StatementAssign.class));
        Ident v77 = ((StatementAssign) v76).ident;
        assertThat("", v77, instanceOf(Ident.class));
        assertEquals("b", String.valueOf(v77.firstToken.getText()));
        assertEquals(1, ((Ident) v77).getNest());
        Declaration v78 = ((Ident) v77).getDec();
        IToken v79 = ((ConstDec)v78).ident;
        assertEquals("b", String.valueOf(v79.getText()));
        Boolean v80 = (Boolean)((ConstDec)v78).val;
        assertEquals(true, v80);
        assertEquals(0, v78.getNest());
        Expression v82 = ((StatementAssign) v76).expression;
        assertThat("", v82, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v82.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v82).getNest());
        Declaration v83 = ((ExpressionIdent) v82).getDec();
        IToken v84 = ((ConstDec)v83).ident;
        assertEquals("c", String.valueOf(v84.getText()));
        String v85 = (String)((ConstDec)v83).val;
        assertEquals("a", v85);
        assertEquals(0, v83.getNest());
        Statement v87 = ((StatementBlock) v69).statements.get(2);
        assertThat("", v87, instanceOf(StatementAssign.class));
        Ident v88 = ((StatementAssign) v87).ident;
        assertThat("", v88, instanceOf(Ident.class));
        assertEquals("c", String.valueOf(v88.firstToken.getText()));
        assertEquals(1, ((Ident) v88).getNest());
        Declaration v89 = ((Ident) v88).getDec();
        IToken v90 = ((ConstDec)v89).ident;
        assertEquals("c", String.valueOf(v90.getText()));
        String v91 = (String)((ConstDec)v89).val;
        assertEquals("a", v91);
        assertEquals(0, v89.getNest());
        Expression v93 = ((StatementAssign) v87).expression;
        Expression v94 = ((ExpressionBinary) v93).e0;
        Expression v95 = ((ExpressionBinary) v94).e0;
        assertThat("", v95, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v95.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v95).getNest());
        Declaration v96 = ((ExpressionIdent) v95).getDec();
        IToken v97 = ((ConstDec)v96).ident;
        assertEquals("b", String.valueOf(v97.getText()));
        Boolean v98 = (Boolean)((ConstDec)v96).val;
        assertEquals(true, v98);
        assertEquals(0, v96.getNest());
        assertEquals("-", String.valueOf(((ExpressionBinary) v94).op.getText()));
        Expression v100 = ((ExpressionBinary) v94).e1;
        assertThat("", v100, instanceOf(ExpressionIdent.class));
        assertEquals("d", String.valueOf(v100.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v100).getNest());
        Declaration v101 = ((ExpressionIdent) v100).getDec();
        IToken v102 = ((VarDec)v101).ident;
        assertEquals("d", String.valueOf(v102.getText()));
        assertEquals(1, v101.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v93).op.getText()));
        Expression v103 = ((ExpressionBinary) v93).e1;
        assertThat("", v103, instanceOf(ExpressionBooleanLit.class));
        assertEquals("TRUE", String.valueOf(v103.firstToken.getText()));
        ProcDec v104 = v17.get(1);
        assertThat("", v104, instanceOf(ProcDec.class));
        IToken v105 = ((ProcDec)v104).ident;
        assertEquals("p3", String.valueOf(v105.getText()));
        assertEquals(0, ((ProcDec)v104).getNest());
        Block v106 = ((ProcDec)v104).block;
        assertThat("", v106, instanceOf(Block.class));
        List<ConstDec> v107 = ((Block) v106).constDecs;
        assertEquals(0, v107.size());
        List<VarDec> v108 = ((Block) v106).varDecs;
        assertEquals(2, v108.size());
        VarDec v109 = v108.get(0);
        assertThat("", v109, instanceOf(VarDec.class));
        IToken v110 = ((VarDec)v109).ident;
        assertEquals("a", String.valueOf(v110.getText()));
        assertEquals(1, v109.getNest());
        VarDec v111 = v108.get(1);
        assertThat("", v111, instanceOf(VarDec.class));
        IToken v112 = ((VarDec)v111).ident;
        assertEquals("d", String.valueOf(v112.getText()));
        assertEquals(1, v111.getNest());
        List<ProcDec> v113 = ((Block) v106).procedureDecs;
        assertEquals(1, v113.size());
        ProcDec v114 = v113.get(0);
        assertThat("", v114, instanceOf(ProcDec.class));
        IToken v115 = ((ProcDec)v114).ident;
        assertEquals("p2", String.valueOf(v115.getText()));
        assertEquals(1, ((ProcDec)v114).getNest());
        Block v116 = ((ProcDec)v114).block;
        assertThat("", v116, instanceOf(Block.class));
        List<ConstDec> v117 = ((Block) v116).constDecs;
        assertEquals(0, v117.size());
        List<VarDec> v118 = ((Block) v116).varDecs;
        assertEquals(0, v118.size());
        List<ProcDec> v119 = ((Block) v116).procedureDecs;
        assertEquals(0, v119.size());
        Statement v120 = ((Block) v116).statement;
        assertThat("", v120, instanceOf(StatementBlock.class));
        Statement v121 = ((StatementBlock) v120).statements.get(0);
        assertThat("", v121, instanceOf(StatementOutput.class));
        Expression v122 = ((StatementOutput) v121).expression;
        assertThat("", v122, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v122.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v122).getNest());
        Declaration v123 = ((ExpressionIdent) v122).getDec();
        IToken v124 = ((ConstDec)v123).ident;
        assertEquals("c", String.valueOf(v124.getText()));
        String v125 = (String)((ConstDec)v123).val;
        assertEquals("a", v125);
        assertEquals(0, v123.getNest());
        Statement v127 = ((StatementBlock) v120).statements.get(1);
        assertThat("", v127, instanceOf(StatementAssign.class));
        Ident v128 = ((StatementAssign) v127).ident;
        assertThat("", v128, instanceOf(Ident.class));
        assertEquals("b", String.valueOf(v128.firstToken.getText()));
        assertEquals(2, ((Ident) v128).getNest());
        Declaration v129 = ((Ident) v128).getDec();
        IToken v130 = ((ConstDec)v129).ident;
        assertEquals("b", String.valueOf(v130.getText()));
        Boolean v131 = (Boolean)((ConstDec)v129).val;
        assertEquals(true, v131);
        assertEquals(0, v129.getNest());
        Expression v133 = ((StatementAssign) v127).expression;
        assertThat("", v133, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v133.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v133).getNest());
        Declaration v134 = ((ExpressionIdent) v133).getDec();
        IToken v135 = ((ConstDec)v134).ident;
        assertEquals("c", String.valueOf(v135.getText()));
        String v136 = (String)((ConstDec)v134).val;
        assertEquals("a", v136);
        assertEquals(0, v134.getNest());
        Statement v138 = ((StatementBlock) v120).statements.get(2);
        assertThat("", v138, instanceOf(StatementAssign.class));
        Ident v139 = ((StatementAssign) v138).ident;
        assertThat("", v139, instanceOf(Ident.class));
        assertEquals("c", String.valueOf(v139.firstToken.getText()));
        assertEquals(2, ((Ident) v139).getNest());
        Declaration v140 = ((Ident) v139).getDec();
        IToken v141 = ((ConstDec)v140).ident;
        assertEquals("c", String.valueOf(v141.getText()));
        String v142 = (String)((ConstDec)v140).val;
        assertEquals("a", v142);
        assertEquals(0, v140.getNest());
        Expression v144 = ((StatementAssign) v138).expression;
        Expression v145 = ((ExpressionBinary) v144).e0;
        Expression v146 = ((ExpressionBinary) v145).e0;
        assertThat("", v146, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v146.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v146).getNest());
        Declaration v147 = ((ExpressionIdent) v146).getDec();
        IToken v148 = ((ConstDec)v147).ident;
        assertEquals("b", String.valueOf(v148.getText()));
        Boolean v149 = (Boolean)((ConstDec)v147).val;
        assertEquals(true, v149);
        assertEquals(0, v147.getNest());
        assertEquals("-", String.valueOf(((ExpressionBinary) v145).op.getText()));
        Expression v151 = ((ExpressionBinary) v145).e1;
        assertThat("", v151, instanceOf(ExpressionIdent.class));
        assertEquals("d", String.valueOf(v151.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v151).getNest());
        Declaration v152 = ((ExpressionIdent) v151).getDec();
        IToken v153 = ((VarDec)v152).ident;
        assertEquals("d", String.valueOf(v153.getText()));
        assertEquals(1, v152.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v144).op.getText()));
        Expression v154 = ((ExpressionBinary) v144).e1;
        assertThat("", v154, instanceOf(ExpressionBooleanLit.class));
        assertEquals("TRUE", String.valueOf(v154.firstToken.getText()));
        Statement v155 = ((Block) v106).statement;
        assertThat("", v155, instanceOf(StatementBlock.class));
        Statement v156 = ((StatementBlock) v155).statements.get(0);
        assertThat("", v156, instanceOf(StatementOutput.class));
        Expression v157 = ((StatementOutput) v156).expression;
        assertThat("", v157, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v157.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v157).getNest());
        Declaration v158 = ((ExpressionIdent) v157).getDec();
        IToken v159 = ((ConstDec)v158).ident;
        assertEquals("c", String.valueOf(v159.getText()));
        String v160 = (String)((ConstDec)v158).val;
        assertEquals("a", v160);
        assertEquals(0, v158.getNest());
        Statement v162 = ((StatementBlock) v155).statements.get(1);
        assertThat("", v162, instanceOf(StatementAssign.class));
        Ident v163 = ((StatementAssign) v162).ident;
        assertThat("", v163, instanceOf(Ident.class));
        assertEquals("b", String.valueOf(v163.firstToken.getText()));
        assertEquals(1, ((Ident) v163).getNest());
        Declaration v164 = ((Ident) v163).getDec();
        IToken v165 = ((ConstDec)v164).ident;
        assertEquals("b", String.valueOf(v165.getText()));
        Boolean v166 = (Boolean)((ConstDec)v164).val;
        assertEquals(true, v166);
        assertEquals(0, v164.getNest());
        Expression v168 = ((StatementAssign) v162).expression;
        assertThat("", v168, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v168.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v168).getNest());
        Declaration v169 = ((ExpressionIdent) v168).getDec();
        IToken v170 = ((ConstDec)v169).ident;
        assertEquals("c", String.valueOf(v170.getText()));
        String v171 = (String)((ConstDec)v169).val;
        assertEquals("a", v171);
        assertEquals(0, v169.getNest());
        Statement v173 = ((StatementBlock) v155).statements.get(2);
        assertThat("", v173, instanceOf(StatementAssign.class));
        Ident v174 = ((StatementAssign) v173).ident;
        assertThat("", v174, instanceOf(Ident.class));
        assertEquals("c", String.valueOf(v174.firstToken.getText()));
        assertEquals(1, ((Ident) v174).getNest());
        Declaration v175 = ((Ident) v174).getDec();
        IToken v176 = ((ConstDec)v175).ident;
        assertEquals("c", String.valueOf(v176.getText()));
        String v177 = (String)((ConstDec)v175).val;
        assertEquals("a", v177);
        assertEquals(0, v175.getNest());
        Expression v179 = ((StatementAssign) v173).expression;
        Expression v180 = ((ExpressionBinary) v179).e0;
        Expression v181 = ((ExpressionBinary) v180).e0;
        assertThat("", v181, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v181.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v181).getNest());
        Declaration v182 = ((ExpressionIdent) v181).getDec();
        IToken v183 = ((ConstDec)v182).ident;
        assertEquals("b", String.valueOf(v183.getText()));
        Boolean v184 = (Boolean)((ConstDec)v182).val;
        assertEquals(true, v184);
        assertEquals(0, v182.getNest());
        assertEquals("-", String.valueOf(((ExpressionBinary) v180).op.getText()));
        Expression v186 = ((ExpressionBinary) v180).e1;
        assertThat("", v186, instanceOf(ExpressionIdent.class));
        assertEquals("d", String.valueOf(v186.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v186).getNest());
        Declaration v187 = ((ExpressionIdent) v186).getDec();
        IToken v188 = ((VarDec)v187).ident;
        assertEquals("d", String.valueOf(v188.getText()));
        assertEquals(1, v187.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v179).op.getText()));
        Expression v189 = ((ExpressionBinary) v179).e1;
        assertThat("", v189, instanceOf(ExpressionBooleanLit.class));
        assertEquals("TRUE", String.valueOf(v189.firstToken.getText()));
        Statement v190 = ((Block) v0).statement;
        assertThat("", v190, instanceOf(StatementEmpty.class));
    }
    
    @Test
    void paramTest20() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            IF a>=b+(3-2) THEN
                                BEGIN
                                    ! c;
                                    b := c;
                                    c := b-d>=TRUE
                                END
                        ;
                        PROCEDURE p2;
                            IF a>=b+(3-2) THEN
                                BEGIN
                                    ! c;
                                    b := c;
                                    c := b-d>=TRUE
                                END
                        ;
                        .
                        """;
        ASTNode ast = getDecoratedAST(input);
        assertThat("", ast, instanceOf(Program.class));
        Block v0 = ((Program) ast).block;
        assertThat("", v0, instanceOf(Block.class));
        List<ConstDec> v1 = ((Block) v0).constDecs;
        assertEquals(3, v1.size());
        ConstDec v2 = v1.get(0);
        assertThat("", v2, instanceOf(ConstDec.class));
        IToken v3 = ((ConstDec)v2).ident;
        assertEquals("a", String.valueOf(v3.getText()));
        Integer v4 = (Integer)((ConstDec)v2).val;
        assertEquals(1, v4);
        assertEquals(0, v2.getNest());
        ConstDec v6 = v1.get(1);
        assertThat("", v6, instanceOf(ConstDec.class));
        IToken v7 = ((ConstDec)v6).ident;
        assertEquals("b", String.valueOf(v7.getText()));
        Boolean v8 = (Boolean)((ConstDec)v6).val;
        assertEquals(true, v8);
        assertEquals(0, v6.getNest());
        ConstDec v10 = v1.get(2);
        assertThat("", v10, instanceOf(ConstDec.class));
        IToken v11 = ((ConstDec)v10).ident;
        assertEquals("c", String.valueOf(v11.getText()));
        String v12 = (String)((ConstDec)v10).val;
        assertEquals("a", v12);
        assertEquals(0, v10.getNest());
        List<VarDec> v14 = ((Block) v0).varDecs;
        assertEquals(1, v14.size());
        VarDec v15 = v14.get(0);
        assertThat("", v15, instanceOf(VarDec.class));
        IToken v16 = ((VarDec)v15).ident;
        assertEquals("d", String.valueOf(v16.getText()));
        assertEquals(0, v15.getNest());
        List<ProcDec> v17 = ((Block) v0).procedureDecs;
        assertEquals(2, v17.size());
        ProcDec v18 = v17.get(0);
        assertThat("", v18, instanceOf(ProcDec.class));
        IToken v19 = ((ProcDec)v18).ident;
        assertEquals("p1", String.valueOf(v19.getText()));
        assertEquals(0, ((ProcDec)v18).getNest());
        Block v20 = ((ProcDec)v18).block;
        assertThat("", v20, instanceOf(Block.class));
        List<ConstDec> v21 = ((Block) v20).constDecs;
        assertEquals(0, v21.size());
        List<VarDec> v22 = ((Block) v20).varDecs;
        assertEquals(0, v22.size());
        List<ProcDec> v23 = ((Block) v20).procedureDecs;
        assertEquals(0, v23.size());
        Statement v24 = ((Block) v20).statement;
        Expression v25 = ((StatementIf) v24).expression;
        Expression v26 = ((ExpressionBinary) v25).e0;
        assertThat("", v26, instanceOf(ExpressionIdent.class));
        assertEquals("a", String.valueOf(v26.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v26).getNest());
        Declaration v27 = ((ExpressionIdent) v26).getDec();
        IToken v28 = ((ConstDec)v27).ident;
        assertEquals("a", String.valueOf(v28.getText()));
        Integer v29 = (Integer)((ConstDec)v27).val;
        assertEquals(1, v29);
        assertEquals(0, v27.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v25).op.getText()));
        Expression v31 = ((ExpressionBinary) v25).e1;
        Expression v32 = ((ExpressionBinary) v31).e0;
        assertThat("", v32, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v32.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v32).getNest());
        Declaration v33 = ((ExpressionIdent) v32).getDec();
        IToken v34 = ((ConstDec)v33).ident;
        assertEquals("b", String.valueOf(v34.getText()));
        Boolean v35 = (Boolean)((ConstDec)v33).val;
        assertEquals(true, v35);
        assertEquals(0, v33.getNest());
        assertEquals("+", String.valueOf(((ExpressionBinary) v31).op.getText()));
        Expression v37 = ((ExpressionBinary) v31).e1;
        Expression v38 = ((ExpressionBinary) v37).e0;
        assertThat("", v38, instanceOf(ExpressionNumLit.class));
        assertEquals("3", String.valueOf(v38.firstToken.getText()));
        assertEquals("-", String.valueOf(((ExpressionBinary) v37).op.getText()));
        Expression v39 = ((ExpressionBinary) v37).e1;
        assertThat("", v39, instanceOf(ExpressionNumLit.class));
        assertEquals("2", String.valueOf(v39.firstToken.getText()));
        Statement v40 = ((StatementIf) v24).statement;
        assertThat("", v40, instanceOf(StatementBlock.class));
        Statement v41 = ((StatementBlock) v40).statements.get(0);
        assertThat("", v41, instanceOf(StatementOutput.class));
        Expression v42 = ((StatementOutput) v41).expression;
        assertThat("", v42, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v42.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v42).getNest());
        Declaration v43 = ((ExpressionIdent) v42).getDec();
        IToken v44 = ((ConstDec)v43).ident;
        assertEquals("c", String.valueOf(v44.getText()));
        String v45 = (String)((ConstDec)v43).val;
        assertEquals("a", v45);
        assertEquals(0, v43.getNest());
        Statement v47 = ((StatementBlock) v40).statements.get(1);
        assertThat("", v47, instanceOf(StatementAssign.class));
        Ident v48 = ((StatementAssign) v47).ident;
        assertThat("", v48, instanceOf(Ident.class));
        assertEquals("b", String.valueOf(v48.firstToken.getText()));
        assertEquals(1, ((Ident) v48).getNest());
        Declaration v49 = ((Ident) v48).getDec();
        IToken v50 = ((ConstDec)v49).ident;
        assertEquals("b", String.valueOf(v50.getText()));
        Boolean v51 = (Boolean)((ConstDec)v49).val;
        assertEquals(true, v51);
        assertEquals(0, v49.getNest());
        Expression v53 = ((StatementAssign) v47).expression;
        assertThat("", v53, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v53.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v53).getNest());
        Declaration v54 = ((ExpressionIdent) v53).getDec();
        IToken v55 = ((ConstDec)v54).ident;
        assertEquals("c", String.valueOf(v55.getText()));
        String v56 = (String)((ConstDec)v54).val;
        assertEquals("a", v56);
        assertEquals(0, v54.getNest());
        Statement v58 = ((StatementBlock) v40).statements.get(2);
        assertThat("", v58, instanceOf(StatementAssign.class));
        Ident v59 = ((StatementAssign) v58).ident;
        assertThat("", v59, instanceOf(Ident.class));
        assertEquals("c", String.valueOf(v59.firstToken.getText()));
        assertEquals(1, ((Ident) v59).getNest());
        Declaration v60 = ((Ident) v59).getDec();
        IToken v61 = ((ConstDec)v60).ident;
        assertEquals("c", String.valueOf(v61.getText()));
        String v62 = (String)((ConstDec)v60).val;
        assertEquals("a", v62);
        assertEquals(0, v60.getNest());
        Expression v64 = ((StatementAssign) v58).expression;
        Expression v65 = ((ExpressionBinary) v64).e0;
        Expression v66 = ((ExpressionBinary) v65).e0;
        assertThat("", v66, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v66.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v66).getNest());
        Declaration v67 = ((ExpressionIdent) v66).getDec();
        IToken v68 = ((ConstDec)v67).ident;
        assertEquals("b", String.valueOf(v68.getText()));
        Boolean v69 = (Boolean)((ConstDec)v67).val;
        assertEquals(true, v69);
        assertEquals(0, v67.getNest());
        assertEquals("-", String.valueOf(((ExpressionBinary) v65).op.getText()));
        Expression v71 = ((ExpressionBinary) v65).e1;
        assertThat("", v71, instanceOf(ExpressionIdent.class));
        assertEquals("d", String.valueOf(v71.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v71).getNest());
        Declaration v72 = ((ExpressionIdent) v71).getDec();
        IToken v73 = ((VarDec)v72).ident;
        assertEquals("d", String.valueOf(v73.getText()));
        assertEquals(0, v72.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v64).op.getText()));
        Expression v74 = ((ExpressionBinary) v64).e1;
        assertThat("", v74, instanceOf(ExpressionBooleanLit.class));
        assertEquals("TRUE", String.valueOf(v74.firstToken.getText()));
        ProcDec v75 = v17.get(1);
        assertThat("", v75, instanceOf(ProcDec.class));
        IToken v76 = ((ProcDec)v75).ident;
        assertEquals("p2", String.valueOf(v76.getText()));
        assertEquals(0, ((ProcDec)v75).getNest());
        Block v77 = ((ProcDec)v75).block;
        assertThat("", v77, instanceOf(Block.class));
        List<ConstDec> v78 = ((Block) v77).constDecs;
        assertEquals(0, v78.size());
        List<VarDec> v79 = ((Block) v77).varDecs;
        assertEquals(0, v79.size());
        List<ProcDec> v80 = ((Block) v77).procedureDecs;
        assertEquals(0, v80.size());
        Statement v81 = ((Block) v77).statement;
        Expression v82 = ((StatementIf) v81).expression;
        Expression v83 = ((ExpressionBinary) v82).e0;
        assertThat("", v83, instanceOf(ExpressionIdent.class));
        assertEquals("a", String.valueOf(v83.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v83).getNest());
        Declaration v84 = ((ExpressionIdent) v83).getDec();
        IToken v85 = ((ConstDec)v84).ident;
        assertEquals("a", String.valueOf(v85.getText()));
        Integer v86 = (Integer)((ConstDec)v84).val;
        assertEquals(1, v86);
        assertEquals(0, v84.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v82).op.getText()));
        Expression v88 = ((ExpressionBinary) v82).e1;
        Expression v89 = ((ExpressionBinary) v88).e0;
        assertThat("", v89, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v89.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v89).getNest());
        Declaration v90 = ((ExpressionIdent) v89).getDec();
        IToken v91 = ((ConstDec)v90).ident;
        assertEquals("b", String.valueOf(v91.getText()));
        Boolean v92 = (Boolean)((ConstDec)v90).val;
        assertEquals(true, v92);
        assertEquals(0, v90.getNest());
        assertEquals("+", String.valueOf(((ExpressionBinary) v88).op.getText()));
        Expression v94 = ((ExpressionBinary) v88).e1;
        Expression v95 = ((ExpressionBinary) v94).e0;
        assertThat("", v95, instanceOf(ExpressionNumLit.class));
        assertEquals("3", String.valueOf(v95.firstToken.getText()));
        assertEquals("-", String.valueOf(((ExpressionBinary) v94).op.getText()));
        Expression v96 = ((ExpressionBinary) v94).e1;
        assertThat("", v96, instanceOf(ExpressionNumLit.class));
        assertEquals("2", String.valueOf(v96.firstToken.getText()));
        Statement v97 = ((StatementIf) v81).statement;
        assertThat("", v97, instanceOf(StatementBlock.class));
        Statement v98 = ((StatementBlock) v97).statements.get(0);
        assertThat("", v98, instanceOf(StatementOutput.class));
        Expression v99 = ((StatementOutput) v98).expression;
        assertThat("", v99, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v99.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v99).getNest());
        Declaration v100 = ((ExpressionIdent) v99).getDec();
        IToken v101 = ((ConstDec)v100).ident;
        assertEquals("c", String.valueOf(v101.getText()));
        String v102 = (String)((ConstDec)v100).val;
        assertEquals("a", v102);
        assertEquals(0, v100.getNest());
        Statement v104 = ((StatementBlock) v97).statements.get(1);
        assertThat("", v104, instanceOf(StatementAssign.class));
        Ident v105 = ((StatementAssign) v104).ident;
        assertThat("", v105, instanceOf(Ident.class));
        assertEquals("b", String.valueOf(v105.firstToken.getText()));
        assertEquals(1, ((Ident) v105).getNest());
        Declaration v106 = ((Ident) v105).getDec();
        IToken v107 = ((ConstDec)v106).ident;
        assertEquals("b", String.valueOf(v107.getText()));
        Boolean v108 = (Boolean)((ConstDec)v106).val;
        assertEquals(true, v108);
        assertEquals(0, v106.getNest());
        Expression v110 = ((StatementAssign) v104).expression;
        assertThat("", v110, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v110.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v110).getNest());
        Declaration v111 = ((ExpressionIdent) v110).getDec();
        IToken v112 = ((ConstDec)v111).ident;
        assertEquals("c", String.valueOf(v112.getText()));
        String v113 = (String)((ConstDec)v111).val;
        assertEquals("a", v113);
        assertEquals(0, v111.getNest());
        Statement v115 = ((StatementBlock) v97).statements.get(2);
        assertThat("", v115, instanceOf(StatementAssign.class));
        Ident v116 = ((StatementAssign) v115).ident;
        assertThat("", v116, instanceOf(Ident.class));
        assertEquals("c", String.valueOf(v116.firstToken.getText()));
        assertEquals(1, ((Ident) v116).getNest());
        Declaration v117 = ((Ident) v116).getDec();
        IToken v118 = ((ConstDec)v117).ident;
        assertEquals("c", String.valueOf(v118.getText()));
        String v119 = (String)((ConstDec)v117).val;
        assertEquals("a", v119);
        assertEquals(0, v117.getNest());
        Expression v121 = ((StatementAssign) v115).expression;
        Expression v122 = ((ExpressionBinary) v121).e0;
        Expression v123 = ((ExpressionBinary) v122).e0;
        assertThat("", v123, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v123.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v123).getNest());
        Declaration v124 = ((ExpressionIdent) v123).getDec();
        IToken v125 = ((ConstDec)v124).ident;
        assertEquals("b", String.valueOf(v125.getText()));
        Boolean v126 = (Boolean)((ConstDec)v124).val;
        assertEquals(true, v126);
        assertEquals(0, v124.getNest());
        assertEquals("-", String.valueOf(((ExpressionBinary) v122).op.getText()));
        Expression v128 = ((ExpressionBinary) v122).e1;
        assertThat("", v128, instanceOf(ExpressionIdent.class));
        assertEquals("d", String.valueOf(v128.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v128).getNest());
        Declaration v129 = ((ExpressionIdent) v128).getDec();
        IToken v130 = ((VarDec)v129).ident;
        assertEquals("d", String.valueOf(v130.getText()));
        assertEquals(0, v129.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v121).op.getText()));
        Expression v131 = ((ExpressionBinary) v121).e1;
        assertThat("", v131, instanceOf(ExpressionBooleanLit.class));
        assertEquals("TRUE", String.valueOf(v131.firstToken.getText()));
        Statement v132 = ((Block) v0).statement;
        assertThat("", v132, instanceOf(StatementEmpty.class));
    }
    
    @Test
    void paramTest21() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            PROCEDURE p2;
                                WHILE a>=b+(3-2) DO
                                    BEGIN
                                        ! c;
                                        b := c;
                                        c := b-d>=TRUE
                                    END
                            ;
                            PROCEDURE p3;
                                WHILE a>=b+(3-2) DO
                                    BEGIN
                                        ! c;
                                        b := c;
                                        c := b-d>=TRUE
                                    END
                            ;
                            d := 6
                        ;
                        .
                        """;
        ASTNode ast = getDecoratedAST(input);
        assertThat("", ast, instanceOf(Program.class));
        Block v0 = ((Program) ast).block;
        assertThat("", v0, instanceOf(Block.class));
        List<ConstDec> v1 = ((Block) v0).constDecs;
        assertEquals(3, v1.size());
        ConstDec v2 = v1.get(0);
        assertThat("", v2, instanceOf(ConstDec.class));
        IToken v3 = ((ConstDec)v2).ident;
        assertEquals("a", String.valueOf(v3.getText()));
        Integer v4 = (Integer)((ConstDec)v2).val;
        assertEquals(1, v4);
        assertEquals(0, v2.getNest());
        ConstDec v6 = v1.get(1);
        assertThat("", v6, instanceOf(ConstDec.class));
        IToken v7 = ((ConstDec)v6).ident;
        assertEquals("b", String.valueOf(v7.getText()));
        Boolean v8 = (Boolean)((ConstDec)v6).val;
        assertEquals(true, v8);
        assertEquals(0, v6.getNest());
        ConstDec v10 = v1.get(2);
        assertThat("", v10, instanceOf(ConstDec.class));
        IToken v11 = ((ConstDec)v10).ident;
        assertEquals("c", String.valueOf(v11.getText()));
        String v12 = (String)((ConstDec)v10).val;
        assertEquals("a", v12);
        assertEquals(0, v10.getNest());
        List<VarDec> v14 = ((Block) v0).varDecs;
        assertEquals(1, v14.size());
        VarDec v15 = v14.get(0);
        assertThat("", v15, instanceOf(VarDec.class));
        IToken v16 = ((VarDec)v15).ident;
        assertEquals("d", String.valueOf(v16.getText()));
        assertEquals(0, v15.getNest());
        List<ProcDec> v17 = ((Block) v0).procedureDecs;
        assertEquals(1, v17.size());
        ProcDec v18 = v17.get(0);
        assertThat("", v18, instanceOf(ProcDec.class));
        IToken v19 = ((ProcDec)v18).ident;
        assertEquals("p1", String.valueOf(v19.getText()));
        assertEquals(0, ((ProcDec)v18).getNest());
        Block v20 = ((ProcDec)v18).block;
        assertThat("", v20, instanceOf(Block.class));
        List<ConstDec> v21 = ((Block) v20).constDecs;
        assertEquals(0, v21.size());
        List<VarDec> v22 = ((Block) v20).varDecs;
        assertEquals(0, v22.size());
        List<ProcDec> v23 = ((Block) v20).procedureDecs;
        assertEquals(2, v23.size());
        ProcDec v24 = v23.get(0);
        assertThat("", v24, instanceOf(ProcDec.class));
        IToken v25 = ((ProcDec)v24).ident;
        assertEquals("p2", String.valueOf(v25.getText()));
        assertEquals(1, ((ProcDec)v24).getNest());
        Block v26 = ((ProcDec)v24).block;
        assertThat("", v26, instanceOf(Block.class));
        List<ConstDec> v27 = ((Block) v26).constDecs;
        assertEquals(0, v27.size());
        List<VarDec> v28 = ((Block) v26).varDecs;
        assertEquals(0, v28.size());
        List<ProcDec> v29 = ((Block) v26).procedureDecs;
        assertEquals(0, v29.size());
        Statement v30 = ((Block) v26).statement;
        Expression v31 = ((StatementWhile) v30).expression;
        Expression v32 = ((ExpressionBinary) v31).e0;
        assertThat("", v32, instanceOf(ExpressionIdent.class));
        assertEquals("a", String.valueOf(v32.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v32).getNest());
        Declaration v33 = ((ExpressionIdent) v32).getDec();
        IToken v34 = ((ConstDec)v33).ident;
        assertEquals("a", String.valueOf(v34.getText()));
        Integer v35 = (Integer)((ConstDec)v33).val;
        assertEquals(1, v35);
        assertEquals(0, v33.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v31).op.getText()));
        Expression v37 = ((ExpressionBinary) v31).e1;
        Expression v38 = ((ExpressionBinary) v37).e0;
        assertThat("", v38, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v38.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v38).getNest());
        Declaration v39 = ((ExpressionIdent) v38).getDec();
        IToken v40 = ((ConstDec)v39).ident;
        assertEquals("b", String.valueOf(v40.getText()));
        Boolean v41 = (Boolean)((ConstDec)v39).val;
        assertEquals(true, v41);
        assertEquals(0, v39.getNest());
        assertEquals("+", String.valueOf(((ExpressionBinary) v37).op.getText()));
        Expression v43 = ((ExpressionBinary) v37).e1;
        Expression v44 = ((ExpressionBinary) v43).e0;
        assertThat("", v44, instanceOf(ExpressionNumLit.class));
        assertEquals("3", String.valueOf(v44.firstToken.getText()));
        assertEquals("-", String.valueOf(((ExpressionBinary) v43).op.getText()));
        Expression v45 = ((ExpressionBinary) v43).e1;
        assertThat("", v45, instanceOf(ExpressionNumLit.class));
        assertEquals("2", String.valueOf(v45.firstToken.getText()));
        Statement v46 = ((StatementWhile) v30).statement;
        assertThat("", v46, instanceOf(StatementBlock.class));
        Statement v47 = ((StatementBlock) v46).statements.get(0);
        assertThat("", v47, instanceOf(StatementOutput.class));
        Expression v48 = ((StatementOutput) v47).expression;
        assertThat("", v48, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v48.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v48).getNest());
        Declaration v49 = ((ExpressionIdent) v48).getDec();
        IToken v50 = ((ConstDec)v49).ident;
        assertEquals("c", String.valueOf(v50.getText()));
        String v51 = (String)((ConstDec)v49).val;
        assertEquals("a", v51);
        assertEquals(0, v49.getNest());
        Statement v53 = ((StatementBlock) v46).statements.get(1);
        assertThat("", v53, instanceOf(StatementAssign.class));
        Ident v54 = ((StatementAssign) v53).ident;
        assertThat("", v54, instanceOf(Ident.class));
        assertEquals("b", String.valueOf(v54.firstToken.getText()));
        assertEquals(2, ((Ident) v54).getNest());
        Declaration v55 = ((Ident) v54).getDec();
        IToken v56 = ((ConstDec)v55).ident;
        assertEquals("b", String.valueOf(v56.getText()));
        Boolean v57 = (Boolean)((ConstDec)v55).val;
        assertEquals(true, v57);
        assertEquals(0, v55.getNest());
        Expression v59 = ((StatementAssign) v53).expression;
        assertThat("", v59, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v59.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v59).getNest());
        Declaration v60 = ((ExpressionIdent) v59).getDec();
        IToken v61 = ((ConstDec)v60).ident;
        assertEquals("c", String.valueOf(v61.getText()));
        String v62 = (String)((ConstDec)v60).val;
        assertEquals("a", v62);
        assertEquals(0, v60.getNest());
        Statement v64 = ((StatementBlock) v46).statements.get(2);
        assertThat("", v64, instanceOf(StatementAssign.class));
        Ident v65 = ((StatementAssign) v64).ident;
        assertThat("", v65, instanceOf(Ident.class));
        assertEquals("c", String.valueOf(v65.firstToken.getText()));
        assertEquals(2, ((Ident) v65).getNest());
        Declaration v66 = ((Ident) v65).getDec();
        IToken v67 = ((ConstDec)v66).ident;
        assertEquals("c", String.valueOf(v67.getText()));
        String v68 = (String)((ConstDec)v66).val;
        assertEquals("a", v68);
        assertEquals(0, v66.getNest());
        Expression v70 = ((StatementAssign) v64).expression;
        Expression v71 = ((ExpressionBinary) v70).e0;
        Expression v72 = ((ExpressionBinary) v71).e0;
        assertThat("", v72, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v72.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v72).getNest());
        Declaration v73 = ((ExpressionIdent) v72).getDec();
        IToken v74 = ((ConstDec)v73).ident;
        assertEquals("b", String.valueOf(v74.getText()));
        Boolean v75 = (Boolean)((ConstDec)v73).val;
        assertEquals(true, v75);
        assertEquals(0, v73.getNest());
        assertEquals("-", String.valueOf(((ExpressionBinary) v71).op.getText()));
        Expression v77 = ((ExpressionBinary) v71).e1;
        assertThat("", v77, instanceOf(ExpressionIdent.class));
        assertEquals("d", String.valueOf(v77.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v77).getNest());
        Declaration v78 = ((ExpressionIdent) v77).getDec();
        IToken v79 = ((VarDec)v78).ident;
        assertEquals("d", String.valueOf(v79.getText()));
        assertEquals(0, v78.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v70).op.getText()));
        Expression v80 = ((ExpressionBinary) v70).e1;
        assertThat("", v80, instanceOf(ExpressionBooleanLit.class));
        assertEquals("TRUE", String.valueOf(v80.firstToken.getText()));
        ProcDec v81 = v23.get(1);
        assertThat("", v81, instanceOf(ProcDec.class));
        IToken v82 = ((ProcDec)v81).ident;
        assertEquals("p3", String.valueOf(v82.getText()));
        assertEquals(1, ((ProcDec)v81).getNest());
        Block v83 = ((ProcDec)v81).block;
        assertThat("", v83, instanceOf(Block.class));
        List<ConstDec> v84 = ((Block) v83).constDecs;
        assertEquals(0, v84.size());
        List<VarDec> v85 = ((Block) v83).varDecs;
        assertEquals(0, v85.size());
        List<ProcDec> v86 = ((Block) v83).procedureDecs;
        assertEquals(0, v86.size());
        Statement v87 = ((Block) v83).statement;
        Expression v88 = ((StatementWhile) v87).expression;
        Expression v89 = ((ExpressionBinary) v88).e0;
        assertThat("", v89, instanceOf(ExpressionIdent.class));
        assertEquals("a", String.valueOf(v89.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v89).getNest());
        Declaration v90 = ((ExpressionIdent) v89).getDec();
        IToken v91 = ((ConstDec)v90).ident;
        assertEquals("a", String.valueOf(v91.getText()));
        Integer v92 = (Integer)((ConstDec)v90).val;
        assertEquals(1, v92);
        assertEquals(0, v90.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v88).op.getText()));
        Expression v94 = ((ExpressionBinary) v88).e1;
        Expression v95 = ((ExpressionBinary) v94).e0;
        assertThat("", v95, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v95.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v95).getNest());
        Declaration v96 = ((ExpressionIdent) v95).getDec();
        IToken v97 = ((ConstDec)v96).ident;
        assertEquals("b", String.valueOf(v97.getText()));
        Boolean v98 = (Boolean)((ConstDec)v96).val;
        assertEquals(true, v98);
        assertEquals(0, v96.getNest());
        assertEquals("+", String.valueOf(((ExpressionBinary) v94).op.getText()));
        Expression v100 = ((ExpressionBinary) v94).e1;
        Expression v101 = ((ExpressionBinary) v100).e0;
        assertThat("", v101, instanceOf(ExpressionNumLit.class));
        assertEquals("3", String.valueOf(v101.firstToken.getText()));
        assertEquals("-", String.valueOf(((ExpressionBinary) v100).op.getText()));
        Expression v102 = ((ExpressionBinary) v100).e1;
        assertThat("", v102, instanceOf(ExpressionNumLit.class));
        assertEquals("2", String.valueOf(v102.firstToken.getText()));
        Statement v103 = ((StatementWhile) v87).statement;
        assertThat("", v103, instanceOf(StatementBlock.class));
        Statement v104 = ((StatementBlock) v103).statements.get(0);
        assertThat("", v104, instanceOf(StatementOutput.class));
        Expression v105 = ((StatementOutput) v104).expression;
        assertThat("", v105, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v105.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v105).getNest());
        Declaration v106 = ((ExpressionIdent) v105).getDec();
        IToken v107 = ((ConstDec)v106).ident;
        assertEquals("c", String.valueOf(v107.getText()));
        String v108 = (String)((ConstDec)v106).val;
        assertEquals("a", v108);
        assertEquals(0, v106.getNest());
        Statement v110 = ((StatementBlock) v103).statements.get(1);
        assertThat("", v110, instanceOf(StatementAssign.class));
        Ident v111 = ((StatementAssign) v110).ident;
        assertThat("", v111, instanceOf(Ident.class));
        assertEquals("b", String.valueOf(v111.firstToken.getText()));
        assertEquals(2, ((Ident) v111).getNest());
        Declaration v112 = ((Ident) v111).getDec();
        IToken v113 = ((ConstDec)v112).ident;
        assertEquals("b", String.valueOf(v113.getText()));
        Boolean v114 = (Boolean)((ConstDec)v112).val;
        assertEquals(true, v114);
        assertEquals(0, v112.getNest());
        Expression v116 = ((StatementAssign) v110).expression;
        assertThat("", v116, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v116.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v116).getNest());
        Declaration v117 = ((ExpressionIdent) v116).getDec();
        IToken v118 = ((ConstDec)v117).ident;
        assertEquals("c", String.valueOf(v118.getText()));
        String v119 = (String)((ConstDec)v117).val;
        assertEquals("a", v119);
        assertEquals(0, v117.getNest());
        Statement v121 = ((StatementBlock) v103).statements.get(2);
        assertThat("", v121, instanceOf(StatementAssign.class));
        Ident v122 = ((StatementAssign) v121).ident;
        assertThat("", v122, instanceOf(Ident.class));
        assertEquals("c", String.valueOf(v122.firstToken.getText()));
        assertEquals(2, ((Ident) v122).getNest());
        Declaration v123 = ((Ident) v122).getDec();
        IToken v124 = ((ConstDec)v123).ident;
        assertEquals("c", String.valueOf(v124.getText()));
        String v125 = (String)((ConstDec)v123).val;
        assertEquals("a", v125);
        assertEquals(0, v123.getNest());
        Expression v127 = ((StatementAssign) v121).expression;
        Expression v128 = ((ExpressionBinary) v127).e0;
        Expression v129 = ((ExpressionBinary) v128).e0;
        assertThat("", v129, instanceOf(ExpressionIdent.class));
        assertEquals("b", String.valueOf(v129.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v129).getNest());
        Declaration v130 = ((ExpressionIdent) v129).getDec();
        IToken v131 = ((ConstDec)v130).ident;
        assertEquals("b", String.valueOf(v131.getText()));
        Boolean v132 = (Boolean)((ConstDec)v130).val;
        assertEquals(true, v132);
        assertEquals(0, v130.getNest());
        assertEquals("-", String.valueOf(((ExpressionBinary) v128).op.getText()));
        Expression v134 = ((ExpressionBinary) v128).e1;
        assertThat("", v134, instanceOf(ExpressionIdent.class));
        assertEquals("d", String.valueOf(v134.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v134).getNest());
        Declaration v135 = ((ExpressionIdent) v134).getDec();
        IToken v136 = ((VarDec)v135).ident;
        assertEquals("d", String.valueOf(v136.getText()));
        assertEquals(0, v135.getNest());
        assertEquals(">=", String.valueOf(((ExpressionBinary) v127).op.getText()));
        Expression v137 = ((ExpressionBinary) v127).e1;
        assertThat("", v137, instanceOf(ExpressionBooleanLit.class));
        assertEquals("TRUE", String.valueOf(v137.firstToken.getText()));
        Statement v138 = ((Block) v20).statement;
        assertThat("", v138, instanceOf(StatementAssign.class));
        Ident v139 = ((StatementAssign) v138).ident;
        assertThat("", v139, instanceOf(Ident.class));
        assertEquals("d", String.valueOf(v139.firstToken.getText()));
        assertEquals(1, ((Ident) v139).getNest());
        Declaration v140 = ((Ident) v139).getDec();
        IToken v141 = ((VarDec)v140).ident;
        assertEquals("d", String.valueOf(v141.getText()));
        assertEquals(0, v140.getNest());
        Expression v142 = ((StatementAssign) v138).expression;
        assertThat("", v142, instanceOf(ExpressionNumLit.class));
        assertEquals("6", String.valueOf(v142.firstToken.getText()));
        Statement v143 = ((Block) v0).statement;
        assertThat("", v143, instanceOf(StatementEmpty.class));
    }
    
    @Test
    void paramTest22() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR a;
                        PROCEDURE p1;
                            a := b+c/(a-4)
                        ;
                        .
                        """;
        assertThrows(ScopeException.class, () -> {
            @SuppressWarnings("unused")
            ASTNode ast = getDecoratedAST(input);
        });
    }
    
    @Test
    void paramTest23() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            CALL g
                        ;
                        .
                        """;
        assertThrows(ScopeException.class, () -> {
            @SuppressWarnings("unused")
            ASTNode ast = getDecoratedAST(input);
        });
    }
    
    @Test
    void paramTest24() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            ? g
                        ;
                        .
                        """;
        assertThrows(ScopeException.class, () -> {
            @SuppressWarnings("unused")
            ASTNode ast = getDecoratedAST(input);
        });
    }
    
    @Test
    void paramTest25() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            ! h
                        ;
                        .
                        """;
        assertThrows(ScopeException.class, () -> {
            @SuppressWarnings("unused")
            ASTNode ast = getDecoratedAST(input);
        });
    }
    
    @Test
    void paramTest26() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            BEGIN
                                ! c;
                                b := c;
                                c := b-p>=TRUE
                            END
                        ;
                        .
                        """;
        assertThrows(ScopeException.class, () -> {
            @SuppressWarnings("unused")
            ASTNode ast = getDecoratedAST(input);
        });
    }
    
    @Test
    void paramTest27() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR X;
                        PROCEDURE p1;
                            IF a>=b+(3-2) THEN
                                BEGIN
                                    ! c;
                                    b := c;
                                    c := b-Y>=TRUE
                                END
                        ;
                        .
                        """;
        assertThrows(ScopeException.class, () -> {
            @SuppressWarnings("unused")
            ASTNode ast = getDecoratedAST(input);
        });
    }
    
    @Test
    void paramTest28() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            WHILE a>=b+(3-2) DO
                                BEGIN
                                    ! c;
                                    b := c;
                                    q := b-d>=TRUE
                                END
                        ;
                        .
                        """;
        assertThrows(ScopeException.class, () -> {
            @SuppressWarnings("unused")
            ASTNode ast = getDecoratedAST(input);
        });
    }
    
    @Test
    void paramTest29() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            CONST d="\\n", e=5;
                            VAR b;
                            PROCEDURE p2;
                                VAR q;
                                a := b+c/(a-4)/e+d
                            ;
                            a := b+c/(a-4)/q
                        ;
                        .
                        """;
        assertThrows(ScopeException.class, () -> {
            @SuppressWarnings("unused")
            ASTNode ast = getDecoratedAST(input);
        });
    }
    
    @Test
    void paramTest30() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            PROCEDURE p2;
                                VAR e;
                                CALL c
                            ;
                        ;
                        ! e
                        .
                        """;
        assertThrows(ScopeException.class, () -> {
            @SuppressWarnings("unused")
            ASTNode ast = getDecoratedAST(input);
        });
    }
    
    @Test
    void paramTest31() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            CONST c = 4;
                            PROCEDURE p2;
                                CONST c = 4;
                                VAR c;
                                PROCEDURE p3;
                                    ? c
                                ;
                            ;
                        ;
                        .
                        """;
        assertThrows(ScopeException.class, () -> {
            @SuppressWarnings("unused")
            ASTNode ast = getDecoratedAST(input);
        });
    }
    
    @Test
    void paramTest32() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            PROCEDURE p2;
                                ! e
                            ;
                            ! p2
                        ;
                        .
                        """;
        assertThrows(ScopeException.class, () -> {
            @SuppressWarnings("unused")
            ASTNode ast = getDecoratedAST(input);
        });
    }
    
    @Test
    void paramTest33() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            VAR a, d;
                            PROCEDURE p2;
                                BEGIN
                                    ! c;
                                    b := c;
                                    c := b-d>=TRUE
                                END
                            ;
                            BEGIN
                                    ! c;
                                    b := c;
                                    c := b-e>=TRUE
                            END
                        ;
                        .
                        """;
        assertThrows(ScopeException.class, () -> {
            @SuppressWarnings("unused")
            ASTNode ast = getDecoratedAST(input);
        });
    }
    
    @Test
    void paramTest34() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            IF a>=b+(3-2) THEN
                                BEGIN
                                    ! e;
                                    b := c;
                                    c := b-d>=TRUE
                                END
                        ;
                        .
                        """;
        assertThrows(ScopeException.class, () -> {
            @SuppressWarnings("unused")
            ASTNode ast = getDecoratedAST(input);
        });
    }
    
    @Test
    void paramTest35() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            PROCEDURE p2;
                                WHILE a>=b+(3-2) DO
                                    BEGIN
                                        ! c;
                                        b := c;
                                        c := b-d>=TRUE
                                    END
                            ;
                            e := 6
                        ;
                        .
                        """;
        assertThrows(ScopeException.class, () -> {
            @SuppressWarnings("unused")
            ASTNode ast = getDecoratedAST(input);
        });
    }
    
    @Test
    void paramTest36() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            CONST e="\\n", e=5;
                            VAR b;
                            PROCEDURE p2;
                                a := b+c/(a-4)/e+d
                            ;
                            PROCEDURE p3;
                                a := b+c/(a-4)/e+d
                            ;
                            a := b+c/(a-4)/d
                            
                        ;
                        PROCEDURE p4;
                            e := 6
                        ;
                        .
                        """;
        assertThrows(ScopeException.class, () -> {
            @SuppressWarnings("unused")
            ASTNode ast = getDecoratedAST(input);
        });
    }
    
    @Test
    void paramTest37() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            PROCEDURE p2;
                                VAR e;
                                CALL p3
                            ;
                            PROCEDURE p3;
                                VAR c;
                                CALL e
                            ;
                        ;
                        PROCEDURE p4;
                        ;
                        .
                        """;
        assertThrows(ScopeException.class, () -> {
            @SuppressWarnings("unused")
            ASTNode ast = getDecoratedAST(input);
        });
    }
    
    @Test
    void paramTest38() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            CONST c = 4;
                            PROCEDURE p2;
                                VAR c;
                                VAR q;
                                PROCEDURE p3;
                                    VAR e;
                                        ? c
                                    ;
                                PROCEDURE p4;
                                    e := e + c
                                ;
                            ;
                        ;
                        .
                        """;
        assertThrows(ScopeException.class, () -> {
            @SuppressWarnings("unused")
            ASTNode ast = getDecoratedAST(input);
        });
    }
    
    @Test
    void paramTest39() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            PROCEDURE p2;
                                ! c
                            ;
                            PROCEDURE p1;
                                ! d
                            ;
                            ! p2
                        ;
                        PROCEDURE p4;
                        ;
                        .
                        """;
        ASTNode ast = getDecoratedAST(input);
        assertThat("", ast, instanceOf(Program.class));
        Block v0 = ((Program) ast).block;
        assertThat("", v0, instanceOf(Block.class));
        List<ConstDec> v1 = ((Block) v0).constDecs;
        assertEquals(3, v1.size());
        ConstDec v2 = v1.get(0);
        assertThat("", v2, instanceOf(ConstDec.class));
        IToken v3 = ((ConstDec)v2).ident;
        assertEquals("a", String.valueOf(v3.getText()));
        Integer v4 = (Integer)((ConstDec)v2).val;
        assertEquals(1, v4);
        assertEquals(0, v2.getNest());
        ConstDec v6 = v1.get(1);
        assertThat("", v6, instanceOf(ConstDec.class));
        IToken v7 = ((ConstDec)v6).ident;
        assertEquals("b", String.valueOf(v7.getText()));
        Boolean v8 = (Boolean)((ConstDec)v6).val;
        assertEquals(true, v8);
        assertEquals(0, v6.getNest());
        ConstDec v10 = v1.get(2);
        assertThat("", v10, instanceOf(ConstDec.class));
        IToken v11 = ((ConstDec)v10).ident;
        assertEquals("c", String.valueOf(v11.getText()));
        String v12 = (String)((ConstDec)v10).val;
        assertEquals("a", v12);
        assertEquals(0, v10.getNest());
        List<VarDec> v14 = ((Block) v0).varDecs;
        assertEquals(1, v14.size());
        VarDec v15 = v14.get(0);
        assertThat("", v15, instanceOf(VarDec.class));
        IToken v16 = ((VarDec)v15).ident;
        assertEquals("d", String.valueOf(v16.getText()));
        assertEquals(0, v15.getNest());
        List<ProcDec> v17 = ((Block) v0).procedureDecs;
        assertEquals(2, v17.size());
        ProcDec v18 = v17.get(0);
        assertThat("", v18, instanceOf(ProcDec.class));
        IToken v19 = ((ProcDec)v18).ident;
        assertEquals("p1", String.valueOf(v19.getText()));
        assertEquals(0, ((ProcDec)v18).getNest());
        Block v20 = ((ProcDec)v18).block;
        assertThat("", v20, instanceOf(Block.class));
        List<ConstDec> v21 = ((Block) v20).constDecs;
        assertEquals(0, v21.size());
        List<VarDec> v22 = ((Block) v20).varDecs;
        assertEquals(0, v22.size());
        List<ProcDec> v23 = ((Block) v20).procedureDecs;
        assertEquals(2, v23.size());
        ProcDec v24 = v23.get(0);
        assertThat("", v24, instanceOf(ProcDec.class));
        IToken v25 = ((ProcDec)v24).ident;
        assertEquals("p2", String.valueOf(v25.getText()));
        assertEquals(1, ((ProcDec)v24).getNest());
        Block v26 = ((ProcDec)v24).block;
        assertThat("", v26, instanceOf(Block.class));
        List<ConstDec> v27 = ((Block) v26).constDecs;
        assertEquals(0, v27.size());
        List<VarDec> v28 = ((Block) v26).varDecs;
        assertEquals(0, v28.size());
        List<ProcDec> v29 = ((Block) v26).procedureDecs;
        assertEquals(0, v29.size());
        Statement v30 = ((Block) v26).statement;
        assertThat("", v30, instanceOf(StatementOutput.class));
        Expression v31 = ((StatementOutput) v30).expression;
        assertThat("", v31, instanceOf(ExpressionIdent.class));
        assertEquals("c", String.valueOf(v31.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v31).getNest());
        Declaration v32 = ((ExpressionIdent) v31).getDec();
        IToken v33 = ((ConstDec)v32).ident;
        assertEquals("c", String.valueOf(v33.getText()));
        String v34 = (String)((ConstDec)v32).val;
        assertEquals("a", v34);
        assertEquals(0, v32.getNest());
        ProcDec v36 = v23.get(1);
        assertThat("", v36, instanceOf(ProcDec.class));
        IToken v37 = ((ProcDec)v36).ident;
        assertEquals("p1", String.valueOf(v37.getText()));
        assertEquals(1, ((ProcDec)v36).getNest());
        Block v38 = ((ProcDec)v36).block;
        assertThat("", v38, instanceOf(Block.class));
        List<ConstDec> v39 = ((Block) v38).constDecs;
        assertEquals(0, v39.size());
        List<VarDec> v40 = ((Block) v38).varDecs;
        assertEquals(0, v40.size());
        List<ProcDec> v41 = ((Block) v38).procedureDecs;
        assertEquals(0, v41.size());
        Statement v42 = ((Block) v38).statement;
        assertThat("", v42, instanceOf(StatementOutput.class));
        Expression v43 = ((StatementOutput) v42).expression;
        assertThat("", v43, instanceOf(ExpressionIdent.class));
        assertEquals("d", String.valueOf(v43.firstToken.getText()));
        assertEquals(2, ((ExpressionIdent) v43).getNest());
        Declaration v44 = ((ExpressionIdent) v43).getDec();
        IToken v45 = ((VarDec)v44).ident;
        assertEquals("d", String.valueOf(v45.getText()));
        assertEquals(0, v44.getNest());
        Statement v46 = ((Block) v20).statement;
        assertThat("", v46, instanceOf(StatementOutput.class));
        Expression v47 = ((StatementOutput) v46).expression;
        assertThat("", v47, instanceOf(ExpressionIdent.class));
        assertEquals("p2", String.valueOf(v47.firstToken.getText()));
        assertEquals(1, ((ExpressionIdent) v47).getNest());
        Declaration v48 = ((ExpressionIdent) v47).getDec();
        IToken v49 = ((ProcDec)v48).ident;
        assertEquals("p2", String.valueOf(v49.getText()));
        assertEquals(1, ((ProcDec)v48).getNest());
        ProcDec v50 = v17.get(1);
        assertThat("", v50, instanceOf(ProcDec.class));
        IToken v51 = ((ProcDec)v50).ident;
        assertEquals("p4", String.valueOf(v51.getText()));
        assertEquals(0, ((ProcDec)v50).getNest());
        Block v52 = ((ProcDec)v50).block;
        assertThat("", v52, instanceOf(Block.class));
        List<ConstDec> v53 = ((Block) v52).constDecs;
        assertEquals(0, v53.size());
        List<VarDec> v54 = ((Block) v52).varDecs;
        assertEquals(0, v54.size());
        List<ProcDec> v55 = ((Block) v52).procedureDecs;
        assertEquals(0, v55.size());
        Statement v56 = ((Block) v52).statement;
        assertThat("", v56, instanceOf(StatementEmpty.class));
        Statement v57 = ((Block) v0).statement;
        assertThat("", v57, instanceOf(StatementEmpty.class));

    }
    
    @Test
    void paramTest40() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            VAR a, d, q;
                            PROCEDURE p2;
                                BEGIN
                                    ! c;
                                    b := c;
                                    c := b-d>=TRUE
                                END
                            ;
                            BEGIN
                                    ! c;
                                    b := c;
                                    c := b-d>=TRUE
                            END
                        ;
                        PROCEDURE p3;
                            VAR a, d;
                            PROCEDURE p2;
                                BEGIN
                                    ! c;
                                    b := c;
                                    c := b-d>=TRUE
                                END
                            ;
                            BEGIN
                                    ! c;
                                    b := c;
                                    c := b-q>=TRUE
                            END
                        ;
                        .
                        """;
        assertThrows(ScopeException.class, () -> {
            @SuppressWarnings("unused")
            ASTNode ast = getDecoratedAST(input);
        });
    }
    
    @Test
    void paramTest41() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            CONST q=9;
                            IF a>=b+(3-2) THEN
                                BEGIN
                                    ! c;
                                    b := c;
                                    c := b-d>=TRUE
                                END
                        ;
                        PROCEDURE p2;
                            IF a>=b+(3-2) THEN
                                BEGIN
                                    ! c;
                                    b := c;
                                    q := b-d>=TRUE
                                END
                        ;
                        .
                        """;
        assertThrows(ScopeException.class, () -> {
            @SuppressWarnings("unused")
            ASTNode ast = getDecoratedAST(input);
        });
    }
    
    @Test
    void paramTest42() throws PLPException {
        String input = """
                        CONST a=1, b=TRUE;
                        CONST c="a";
                        VAR d;
                        PROCEDURE p1;
                            PROCEDURE p2;
                                WHILE a>=b+(3-2) DO
                                    BEGIN
                                        ! c;
                                        b := c;
                                        c := b-d>=TRUE
                                    END
                            ;
                            PROCEDURE p2;
                                WHILE a>=b+(3-2) DO
                                    BEGIN
                                        ! c;
                                        b := c;
                                        c := b-d>=TRUE
                                    END
                            ;
                            d := 6
                        ;
                        .
                        """;
        assertThrows(ScopeException.class, () -> {            @SuppressWarnings("unused")
            ASTNode ast = getDecoratedAST(input);
        });
    }



}
