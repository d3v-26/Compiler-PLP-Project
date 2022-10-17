package edu.ufl.cise.plpfa22;
import edu.ufl.cise.plpfa22.ast.Declaration;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Stack;

public class SymbolTable {
	Stack<Integer> scopeStack = new Stack<Integer>();
	HashMap<String, HashMap<Integer, Declaration>> symbolTable = new HashMap<String, HashMap<Integer, Declaration>>();
	
	int scopeCounter = -1;

	public void enterScope(){
		// Push the new scope counter
		scopeStack.push(scopeCounter++);		
	}
	
	public void leaveScope(){
		// Decrement scope counter and remove the variables for the scope that we left.
		scopeCounter--;
		for(String k : symbolTable.keySet()) {
			HashMap<Integer, Declaration> h = symbolTable.get(k);
			if(h.containsKey(scopeCounter)) {
				h.remove(scopeCounter);
			}
		}
		scopeStack.pop();		
	}


	public boolean insert(String ident, Declaration dec){
		// Insert ident into HashMap and throw error if its already declared in the current scope.
		int currentScope = scopeStack.peek();
		if(symbolTable.containsKey(ident)) {
			HashMap<Integer, Declaration> map = symbolTable.get(ident);
			if(map.containsKey(currentScope))
				return false;
			map.put(currentScope, dec);
		}
		else {
			HashMap<Integer, Declaration> map = new HashMap<Integer, Declaration>();
			map.put(currentScope, dec);
			symbolTable.put(ident, map);
		}
		return true;
	}
	
	public Declaration lookUp(String ident){
		if(!symbolTable.containsKey(ident))
			return null;
		HashMap<Integer, Declaration> map = symbolTable.get(ident);
		ListIterator<Integer> listIterator = scopeStack.listIterator(scopeStack.size());
		while(listIterator.hasPrevious()) {
			int key = listIterator.previous();
			if(map.containsKey(key)) {
				return map.get(key);
			}
		}
		return null;
	}
	
	public Declaration lookupinscope(String ident, int scope) {
		if(!symbolTable.containsKey(ident))
			return null;
		HashMap<Integer, Declaration> map = symbolTable.get(ident);
		if(map.containsKey(scope)) {
			return map.get(scope);
		}
		return null;
	}
	
	public SymbolTable() {
		scopeStack.push(scopeCounter++);
	}


	@Override
	public String toString() {
		return "Scope Stack top: "+this.scopeStack.peek();
	}
	
	
}
