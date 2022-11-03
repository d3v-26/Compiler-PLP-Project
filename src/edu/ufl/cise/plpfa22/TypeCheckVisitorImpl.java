package edu.ufl.cise.plpfa22;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

public class TypeCheckVisitorImpl implements ASTVisitor {
	
	SymbolTable symbolTable;
	HashMap<Integer, List<String>> h;
	
	public TypeCheckVisitorImpl() {
		super();
		symbolTable = new SymbolTable();
		h = new HashMap<Integer, List<String>>();
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
		int currScope = this.symbolTable.scopeStack.peek();
		List<String> vars = new ArrayList<String>();
		h.put(currScope, vars);
		
		int changes = 0;
		
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
			int c;
			do {
				c = (Integer) p.block.visit(this, arg);
				changes += c;
			}
			while(c > 0);
		}
		int c;
		do {
			c = (Integer) block.statement.visit(this, arg);
			changes += c;
		}
		while(c > 0);
		
		for(String s : h.get(currScope)) {
			Declaration d = this.symbolTable.lookUp(s);
			if(d.getType() == null) {
				throw new TypeCheckException();
			}
		}
		h.remove(currScope);		
		this.symbolTable.leaveScope();
		return changes;
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
		int changes = 0;
		changes += (Integer)statementAssign.ident.visit(this, arg);
		changes += (Integer)statementAssign.expression.visit(this, arg);
		Type ident = statementAssign.ident.getDec().getType();
		Type expr = statementAssign.expression.getType();
		if(ident != null && expr != null) {
			if(ident != expr) throw new TypeCheckException();
			if(ident == Type.PROCEDURE) throw new TypeCheckException();
			if(statementAssign.ident.getDec() instanceof ConstDec) throw new TypeCheckException();
		}
		else if(ident == null && expr == null) {
			changes += 0;
		}
		else if(ident != null && expr == null) {
			if(statementAssign.ident.getDec() instanceof ConstDec) throw new TypeCheckException();
			statementAssign.expression.setType(ident);
			changes += 1;
		}
		else if(ident == null && expr != null) {
			statementAssign.ident.getDec().setType(expr);
			Declaration d = this.symbolTable.lookUp(String.valueOf(statementAssign.ident.getText()));
			if(d != null) this.symbolTable.updateDec(String.valueOf(statementAssign.ident.getText()), expr);			
			changes += 1;
		}
		return changes;
	}

	@Override
	public Object visitVarDec(VarDec varDec, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		this.symbolTable.insert(String.valueOf(varDec.ident.getText()), varDec);
		return 0;
	}

	@Override
	public Object visitStatementCall(StatementCall statementCall, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		int changes = (Integer)statementCall.ident.visit(this, arg);
		if(statementCall.ident.getDec().getType() != Type.PROCEDURE) {
			throw new TypeCheckException();
		}
		return changes;
	}

	@Override
	public Object visitStatementInput(StatementInput statementInput, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		int changes = (Integer)statementInput.ident.visit(this, arg);
		Type inptype = statementInput.ident.getDec().getType();	
		Type inpstype = this.symbolTable.lookUp(String.valueOf(statementInput.ident.getText())).getType();
		if((statementInput.ident.getDec() instanceof ConstDec)) throw new TypeCheckException();
		if(inptype == Type.PROCEDURE || inpstype == Type.PROCEDURE) {
			throw new TypeCheckException();
		}
		return changes;
	}

	@Override
	public Object visitStatementOutput(StatementOutput statementOutput, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		int changes = (Integer)statementOutput.expression.visit(this, arg);
		Type exptype = statementOutput.expression.getType();		
		if(exptype == Type.PROCEDURE) {
			throw new TypeCheckException();
		}
		return changes;
	}

	@Override
	public Object visitStatementBlock(StatementBlock statementBlock, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		int changes = 0;
		for(Statement s : statementBlock.statements) {
			changes += (Integer) s.visit(this, arg);
		}
		return changes;
	}

	@Override
	public Object visitStatementIf(StatementIf statementIf, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		int changes1 = (Integer)statementIf.expression.visit(this, arg);
		if(statementIf.expression.getType() != Type.BOOLEAN) {
			throw new TypeCheckException();
		}
		int changes2 = (Integer)statementIf.statement.visit(this, arg);
		return changes1+changes2;
	}

	@Override
	public Object visitStatementWhile(StatementWhile statementWhile, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		int changes1 = (Integer)statementWhile.expression.visit(this, arg);
		if(statementWhile.expression.getType() != Type.BOOLEAN) {
			throw new TypeCheckException();
		}
		int changes2 = (Integer)statementWhile.statement.visit(this, arg);
		return changes1 + changes2;
	}

	@Override
	public Object visitExpressionBinary(ExpressionBinary expressionBinary, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		int changes = 0;
		changes += (Integer)expressionBinary.e0.visit(this, arg);
		Type e0 = expressionBinary.e0.getType();
		
		changes += (Integer)expressionBinary.e1.visit(this, arg);
		Type e1 = expressionBinary.e1.getType();
		
		Kind op = expressionBinary.op.getKind();
		
		switch(op) {
			case PLUS -> {
				if(expressionBinary.getType() != null) {
					if(expressionBinary.getType() == Type.PROCEDURE) throw new TypeCheckException();
					if ( e1 != null && e0 != null) {
						if(expressionBinary.getType() != e0 && e0 != e1) throw new TypeCheckException();
						changes += 0;
					}
					else if(e0 != null && e1 == null) {
						if( e0 != expressionBinary.getType()) throw new TypeCheckException();
						expressionBinary.e1.setType(expressionBinary.getType());
						changes += 1;
					}
					else if(e0 == null && e1 != null) {
						if( e1 != expressionBinary.getType()) throw new TypeCheckException();
						expressionBinary.e0.setType(expressionBinary.getType());
						changes += 1;
					}
					else {
						expressionBinary.e0.setType(expressionBinary.getType());
						expressionBinary.e1.setType(expressionBinary.getType());
						changes += 2;
					}
				}
				else {
					if ( e1 != null && e0 != null) {
						if(e1 != e0) throw new TypeCheckException();
						if(e0 == Type.PROCEDURE) throw new TypeCheckException();
						expressionBinary.setType(e1);
						changes += 1;
					}
					else if(e0 != null && e1 == null) {
						if(e0 == Type.PROCEDURE) throw new TypeCheckException();
						expressionBinary.setType(e0);
						expressionBinary.e1.setType(e0);
						changes += 2;
					}
					else if(e0 == null && e1 != null) {
						if(e1 == Type.PROCEDURE) throw new TypeCheckException();
						expressionBinary.setType(e1);
						expressionBinary.e0.setType(e1);
						changes += 2;
					}
					else {
						changes += 0;
					}
				}
			}
			case MINUS, DIV, MOD -> {
				if(expressionBinary.getType() != null) {
					if(expressionBinary.getType() != Type.NUMBER) throw new TypeCheckException();
					if ( e1 != null && e0 != null) {
						if(expressionBinary.getType() != e0 && e0 != e1) throw new TypeCheckException();
						changes += 0;
					}
					else if(e0 != null && e1 == null) {
						if( e0 != expressionBinary.getType()) throw new TypeCheckException();
						expressionBinary.e1.setType(expressionBinary.getType());
						changes += 1;
					}
					else if(e0 == null && e1 != null) {
						if( e1 != expressionBinary.getType()) throw new TypeCheckException();
						expressionBinary.e0.setType(expressionBinary.getType());
						changes += 1;
					}
					else {
						expressionBinary.e0.setType(expressionBinary.getType());
						expressionBinary.e1.setType(expressionBinary.getType());
						changes += 2;
					}
				}
				else {
					if ( e1 != null && e0 != null) {
						if(e1 != e0) throw new TypeCheckException();
						if(e0 != Type.NUMBER) throw new TypeCheckException();
						expressionBinary.setType(e1);
						changes += 1;
					}
					else if(e0 != null && e1 == null) {
						if(e0 != Type.NUMBER) throw new TypeCheckException();
						expressionBinary.setType(e0);
						expressionBinary.e1.setType(e0);
						changes += 2;
					}
					else if(e0 == null && e1 != null) {
						if(e1 != Type.NUMBER) throw new TypeCheckException();
						expressionBinary.setType(e1);
						expressionBinary.e0.setType(e1);
						changes += 2;
					}
					else {
						changes += 0;
					}
				}
			}
			case TIMES -> {
				if(expressionBinary.getType() != null) {
					if(expressionBinary.getType() != Type.NUMBER && expressionBinary.getType() != Type.BOOLEAN) throw new TypeCheckException();
					if ( e1 != null && e0 != null) {
						if(expressionBinary.getType() != e0 && e0 != e1) throw new TypeCheckException();
						changes += 0;
					}
					else if(e0 != null && e1 == null) {
						if( e0 != expressionBinary.getType()) throw new TypeCheckException();
						expressionBinary.e1.setType(expressionBinary.getType());
						changes += 1;
					}
					else if(e0 == null && e1 != null) {
						if( e1 != expressionBinary.getType()) throw new TypeCheckException();
						expressionBinary.e0.setType(expressionBinary.getType());
						changes += 1;
					}
					else {
						expressionBinary.e0.setType(expressionBinary.getType());
						expressionBinary.e1.setType(expressionBinary.getType());
						changes += 2;
					}
				}
				else {
					if ( e1 != null && e0 != null) {
						if(e1 != e0) throw new TypeCheckException();
						if(e0 != Type.NUMBER && e0 != Type.BOOLEAN) throw new TypeCheckException();
						expressionBinary.setType(e1);
						changes += 1;
					}
					else if(e0 != null && e1 == null) {
						if(e0 != Type.NUMBER && e0 != Type.BOOLEAN) throw new TypeCheckException();
						expressionBinary.setType(e0);
						expressionBinary.e1.setType(e0);
						changes += 2;
					}
					else if(e0 == null && e1 != null) {
						if(e1 != Type.NUMBER && e1 != Type.BOOLEAN) throw new TypeCheckException();
						expressionBinary.setType(e1);
						expressionBinary.e0.setType(e1);
						changes += 2;
					}
					else {
						changes += 0;
					}
				}
			}
			case EQ, NEQ, LT, LE, GT, GE -> {
				if(expressionBinary.getType() != null) {
					if(expressionBinary.getType() != Type.BOOLEAN) throw new TypeCheckException();
					if ( e1 != null && e0 != null) {
						if(e1 != e0) throw new TypeCheckException();
						if(e0 == Type.PROCEDURE) throw new TypeCheckException();						
						changes += 0;
					}
					else if(e0 != null && e1 == null) {
						if(e0 == Type.PROCEDURE) throw new TypeCheckException();
						expressionBinary.e1.setType(e0);
						changes += 1;
					}
					else if(e0 == null && e1 != null) {
						if(e1 == Type.PROCEDURE) throw new TypeCheckException();
						expressionBinary.e0.setType(e1);
						changes += 1;
					}
					else {
						changes += 0;
					}
				}
				else {
					if ( e1 != null && e0 != null) {						
						if(e1 != e0) throw new TypeCheckException();
						if(e0 == Type.PROCEDURE) throw new TypeCheckException();						
						changes += 0;
					}
					else if(e0 != null && e1 == null) {
						if(e0 == Type.PROCEDURE) throw new TypeCheckException();
						expressionBinary.e1.setType(e0);
						changes += 1;
					}
					else if(e0 == null && e1 != null) {
						if(e1 == Type.PROCEDURE) throw new TypeCheckException();
						expressionBinary.e0.setType(e1);
						changes += 1;
					}
					else {
						changes += 0;
					}
					expressionBinary.setType(Type.BOOLEAN);
					changes += 1;
				}
			}
			default->throw new SyntaxException();
			
		}
		return changes;
	}

	@Override
	public Object visitExpressionIdent(ExpressionIdent expressionIdent, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		int changes = 0;
		String var = String.valueOf(expressionIdent.firstToken.getText());
		Declaration d = this.symbolTable.lookUp(var);
		if(d == null) return 0;
		Type id = d.getType();
		Type exp = expressionIdent.getType();
		if(id == null && exp == null) {
			changes += 0;
		}
		else if(id != null && exp == null) {
			changes += 1;
			expressionIdent.setType(id);
			expressionIdent.setDec(d);
		}
		else if(id == null && exp != null) {
			changes += 1;
			boolean t = this.symbolTable.updateDec(var, exp);
		}
		else {
			if (id != exp) throw new TypeCheckException();
			changes += 0;
		}
		h.get(this.symbolTable.scopeStack.peek()).add(var);
		return changes;
	}

	@Override
	public Object visitExpressionNumLit(ExpressionNumLit expressionNumLit, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		if(expressionNumLit.getType() != Type.NUMBER) {
			throw new TypeCheckException();
		}
		return 0;
	}

	@Override
	public Object visitExpressionStringLit(ExpressionStringLit expressionStringLit, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		if(expressionStringLit.getType() != Type.STRING) {
			throw new TypeCheckException();
		}
		return 0;
	}

	@Override
	public Object visitExpressionBooleanLit(ExpressionBooleanLit expressionBooleanLit, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		if(expressionBooleanLit.getType() != Type.BOOLEAN) {
			throw new TypeCheckException();
		}
		return 0;
	}

	@Override
	public Object visitProcedure(ProcDec procDec, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		if(procDec.getType() != Type.PROCEDURE) {
			throw new TypeCheckException();
		}
		Declaration d = new ProcDec(null, null, null);
		d.setType(Type.PROCEDURE);
		d.setNest(procDec.getNest());
		this.symbolTable.insert(String.valueOf(procDec.ident.getText()), d);
		return 0;
	}

	@Override
	public Object visitConstDec(ConstDec constDec, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		Type c = constDec.getType();
		Type v = null;
		Object val = constDec.val;
		if(val instanceof String) v = Type.STRING;
		else {
			String ibval = String.valueOf(val);
			if(ibval == "true" || ibval == "false") {
				v = Type.BOOLEAN;
			}
			else {
				v = Type.NUMBER;
			}
		}
		if(v != c) {
			throw new TypeCheckException();
		}
		this.symbolTable.insert(String.valueOf(constDec.ident.getText()), constDec);
		return 0;
	}

	@Override
	public Object visitStatementEmpty(StatementEmpty statementEmpty, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object visitIdent(Ident ident, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		int changes = 0;
		Type id = ident.getDec().getType();
		String var = String.valueOf(ident.getText());
		Declaration d = this.symbolTable.lookUp(var);
		if(d.getType() == null && id == null) {
			changes += 0;
		}
		else if(d.getType() != null && id == null) {
			ident.setDec(d);
			changes += 1;
		}
		else if(d.getType() == null && id != null) {
			System.out.println("update: "+this.symbolTable.updateDec(var, id));
			changes += 1;
		}
		else {
			if(d.getType() != id) throw new TypeCheckException();
			changes += 0;
		}
		h.get(this.symbolTable.scopeStack.peek()).add(var);
		return changes;
	}

}
