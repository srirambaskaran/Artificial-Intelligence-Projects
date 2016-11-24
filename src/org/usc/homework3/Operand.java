package org.usc.homework3;

public class Operand extends Token{
	protected int id;
	protected String expression;
	public boolean truthValue;
	protected int size;
	public Operand(){
		truthValue = true;
		size = 0;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expression == null) ? 0 : expression.hashCode());
		result = prime * result + id;
		result = prime * result + (truthValue ? 1231 : 1237);
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
		Operand other = (Operand) obj;
		if (expression == null) {
			if (other.expression != null)
				return false;
		} else if (!expression.equals(other.expression))
			return false;
		if (id != other.id)
			return false;
		if (truthValue != other.truthValue)
			return false;
		return true;
	}
	public int size() {
		return expression.length();
	}
	public String toString(){
		if(this instanceof Predicate)
			return ((Predicate)this).toString();
		if(this instanceof ConnectedPredicate)
			return ((ConnectedPredicate)this).toString();
		if(this instanceof Variable)
			return ((Variable)this).toString();
		if(this instanceof Constant)
			return ((Constant)this).toString();
		return super.toString();
	}
	public Operand clone(){
		return new Operand();
	}
}
