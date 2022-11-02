package edu.ufl.cise.plpfa22;

import edu.ufl.cise.plpfa22.IToken.Kind;
import edu.ufl.cise.plpfa22.ast.ASTVisitor;
import edu.ufl.cise.plpfa22.ast.Block;
import edu.ufl.cise.plpfa22.ast.ConstDec;
import edu.ufl.cise.plpfa22.ast.Declaration;
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
import edu.ufl.cise.plpfa22.ast.Types.Type;
import edu.ufl.cise.plpfa22.ast.VarDec;

public class ASTVisitorImpl implements ASTVisitor {
	
	SymbolTable symbolTable;
	
	public ASTVisitorImpl() {
		super();
		this.symbolTable = new SymbolTable();
	}
	
	public void checkDecIdent(String id) throws ScopeException {
		if(this.symbolTable.lookUp(id) == null) {
			throw new ScopeException();
		}
	}
	
	public void checkNotDecIdent(String id) throws ScopeException {
		if(this.symbolTable.lookupinscope(id, this.symbolTable.scopeStack.peek()) != null) {
			throw new ScopeException();
		}
	}
	
	@Override
	public Object visitBlock(Block block, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		this.symbolTable.enterScope();
		System.out.println("In ast");
		for(ConstDec c : block.constDecs) {
			c.visit(this, arg);
		}
		
		for(VarDec v : block.varDecs) {
			v.visit(this, arg);
		}
		
		for(ProcDec p : block.procedureDecs) {
			p.visit(this, arg);
		}
		
		for(ProcDec p : block.procedureDecs) {
			p.block.visit(this, arg);
		}
		
		block.statement.visit(this, arg);
		
		this.symbolTable.leaveScope();
		return null;
	}

	@Override
	public Object visitProgram(Program program, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		program.block.visit(this, arg);
		return null;
	}

	@Override
	public Object visitStatementAssign(StatementAssign statementAssign, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		statementAssign.ident.visit(this, arg);
		statementAssign.expression.visit(this, arg);
		return null;
	}

	@Override
	public Object visitVarDec(VarDec varDec, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		checkNotDecIdent(String.valueOf(varDec.ident.getText()));
		varDec.setNest(this.symbolTable.scopeStack.peek());
		this.symbolTable.insert(String.valueOf(varDec.ident.getText()), varDec);
		return null;
	}

	@Override
	public Object visitStatementCall(StatementCall statementCall, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		statementCall.ident.visit(this, arg);
		return null;
	}

	@Override
	public Object visitStatementInput(StatementInput statementInput, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		statementInput.ident.visit(this, arg);
		return null;
	}

	@Override
	public Object visitStatementOutput(StatementOutput statementOutput, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		statementOutput.expression.visit(this, arg);
		return null;
	}

	@Override
	public Object visitStatementBlock(StatementBlock statementBlock, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		for(Statement s : statementBlock.statements) {
			s.visit(this, arg);
		}
		return null;
	}

	@Override
	public Object visitStatementIf(StatementIf statementIf, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		statementIf.expression.visit(this, arg);
		statementIf.statement.visit(this, arg);
		return null;
	}

	@Override
	public Object visitStatementWhile(StatementWhile statementWhile, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		statementWhile.expression.visit(this, arg);
		statementWhile.statement.visit(this, arg);
		return null;
	}

	@Override
	public Object visitExpressionBinary(ExpressionBinary expressionBinary, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		expressionBinary.e0.visit(this, arg);
		expressionBinary.e1.visit(this, arg);
		return null;
	}

	@Override
	public Object visitExpressionIdent(ExpressionIdent expressionIdent, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		IToken id = expressionIdent.firstToken;
		expressionIdent.setNest(this.symbolTable.scopeStack.peek());
		Declaration d = this.symbolTable.lookUp(String.valueOf(id.getText()));
		if (d == null) {
			throw new ScopeException(String.valueOf(id.getText()));
		}
		expressionIdent.setDec(d);
		expressionIdent.setType(d.getType());
		return null;
	}

	@Override
	public Object visitExpressionNumLit(ExpressionNumLit expressionNumLit, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		expressionNumLit.setType(Type.NUMBER);
		return expressionNumLit.firstToken.getIntValue();
	}

	@Override
	public Object visitExpressionStringLit(ExpressionStringLit expressionStringLit, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		expressionStringLit.setType(Type.STRING);
		return expressionStringLit.firstToken.getStringValue();
	}

	@Override
	public Object visitExpressionBooleanLit(ExpressionBooleanLit expressionBooleanLit, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		expressionBooleanLit.setType(Type.BOOLEAN);
		return expressionBooleanLit.firstToken.getBooleanValue();
	}

	@Override
	public Object visitProcedure(ProcDec procDec, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		checkNotDecIdent(String.valueOf(procDec.ident.getText()));
		procDec.setNest(this.symbolTable.scopeStack.peek());
		procDec.setType(Type.PROCEDURE);
		this.symbolTable.insert(String.valueOf(procDec.ident.getText()), procDec);
		return null;
	}

	@Override
	public Object visitConstDec(ConstDec constDec, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		checkNotDecIdent(String.valueOf(constDec.ident.getText()));
		constDec.setNest(this.symbolTable.scopeStack.peek());
		Object val = constDec.val;
		if((val instanceof String)) {
			constDec.setType(Type.STRING);
		}
		else if(String.valueOf(val) == "true" || String.valueOf(val) == "false") {
			constDec.setType(Type.BOOLEAN);
		}
		else {
			constDec.setType(Type.NUMBER);
		}
		
		this.symbolTable.insert(String.valueOf(constDec.ident.getText()), constDec);
		return null;
	}

	@Override
	public Object visitStatementEmpty(StatementEmpty statementEmpty, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitIdent(Ident ident, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		checkDecIdent(String.valueOf(ident.firstToken.getText()));
		ident.setNest(this.symbolTable.scopeStack.peek());
		Declaration d = this.symbolTable.lookUp(String.valueOf(ident.getText()));
		if (d == null) {
			throw new ScopeException(String.valueOf(ident.getText()));
		}
		ident.setDec(d);
		return null;
	}

}
