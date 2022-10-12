package edu.ufl.cise.plpfa22;

import edu.ufl.cise.plpfa22.IToken.Kind;
import edu.ufl.cise.plpfa22.ast.ASTVisitor;
import edu.ufl.cise.plpfa22.ast.Block;
import edu.ufl.cise.plpfa22.ast.ConstDec;
import edu.ufl.cise.plpfa22.ast.ExpressionBinary;
import edu.ufl.cise.plpfa22.ast.ExpressionBooleanLit;
import edu.ufl.cise.plpfa22.ast.ExpressionIdent;
import edu.ufl.cise.plpfa22.ast.ExpressionNumLit;
import edu.ufl.cise.plpfa22.ast.ExpressionStringLit;
import edu.ufl.cise.plpfa22.ast.Ident;
import edu.ufl.cise.plpfa22.ast.ProcDec;
import edu.ufl.cise.plpfa22.ast.Program;
import edu.ufl.cise.plpfa22.ast.StatementAssign;
import edu.ufl.cise.plpfa22.ast.StatementBlock;
import edu.ufl.cise.plpfa22.ast.StatementCall;
import edu.ufl.cise.plpfa22.ast.StatementEmpty;
import edu.ufl.cise.plpfa22.ast.StatementIf;
import edu.ufl.cise.plpfa22.ast.StatementInput;
import edu.ufl.cise.plpfa22.ast.StatementOutput;
import edu.ufl.cise.plpfa22.ast.StatementWhile;
import edu.ufl.cise.plpfa22.ast.VarDec;

public class ASTVisitorImpl implements ASTVisitor {

	@Override
	public Object visitBlock(Block block, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitProgram(Program program, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		return program.block.visit(this, arg);
	}

	@Override
	public Object visitStatementAssign(StatementAssign statementAssign, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitVarDec(VarDec varDec, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitStatementCall(StatementCall statementCall, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitStatementInput(StatementInput statementInput, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitStatementOutput(StatementOutput statementOutput, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitStatementBlock(StatementBlock statementBlock, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitStatementIf(StatementIf statementIf, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitStatementWhile(StatementWhile statementWhile, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpressionBinary(ExpressionBinary expressionBinary, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		int left = (Integer) expressionBinary.e0.visit(this, arg);
		int right = (Integer) expressionBinary.e1.visit(this, arg);
		IToken op = expressionBinary.op;
		switch(op.getKind()) {
			case PLUS -> {
				return left + right;
			}
			case MINUS -> {
				return left - right;
			}
			case TIMES -> {
				return left * right;
			}
			case DIV -> {
				if(right == 0) {
					throw new SyntaxException();
				}
				return left / right;
			}
			case MOD -> {
				return left % right;
			}
			case LT -> {
				return left < right;
			}
			case GT -> {
				return left > right;
			}
			case LE -> {
				return left <= right;
			}
			case GE -> {
				return left >= right;
			}
			case EQ -> {
				return left == right;
			}
			case NEQ -> {
				return left != right;
			}
			default -> {
				throw new SyntaxException();
			}
		}
	}

	@Override
	public Object visitExpressionIdent(ExpressionIdent expressionIdent, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		return expressionIdent.firstToken.getStringValue();
	}

	@Override
	public Object visitExpressionNumLit(ExpressionNumLit expressionNumLit, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		return expressionNumLit.firstToken.getIntValue();
	}

	@Override
	public Object visitExpressionStringLit(ExpressionStringLit expressionStringLit, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		return expressionStringLit.firstToken.getStringValue();
	}

	@Override
	public Object visitExpressionBooleanLit(ExpressionBooleanLit expressionBooleanLit, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		return expressionBooleanLit.firstToken.getBooleanValue();
	}

	@Override
	public Object visitProcedure(ProcDec procDec, Object arg) throws PLPException {
		// TODO Auto-generated method stub
		return procDec.block.visit(this, arg);
	}

	@Override
	public Object visitConstDec(ConstDec constDec, Object arg) throws PLPException {
		// TODO Auto-generated method stub
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
		return null;
	}

}
