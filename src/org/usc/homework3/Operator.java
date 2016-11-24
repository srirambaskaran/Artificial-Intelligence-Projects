package org.usc.homework3;

public class Operator extends Token {
	public String operator;
	public int precedence;

	public Operator(String operator, int precedence) {
		this.operator = operator;
		this.precedence = precedence;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((operator == null) ? 0 : operator.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Operator other = (Operator) obj;
		if (operator == null) {
			if (other.operator != null)
				return false;
		} else if (!operator.equals(other.operator))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return operator; 
	}
	
}
