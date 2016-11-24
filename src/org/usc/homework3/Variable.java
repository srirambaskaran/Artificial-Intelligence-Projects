package org.usc.homework3;

public class Variable extends Operand {
	public String variable;
	public Variable(String variable){
		this.variable = variable;
	}
	
	@Override
	public String toString() {
		return variable;
	}
	@Override
	public int hashCode() {
		return this.variable.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Variable)) return false;
		Variable other = (Variable)obj;
		return this.variable.equals(other.variable);
	}
	public Operand clone(){
		return new Variable(this.variable);
	}
}
