package org.usc.homework3;

public class Constant extends Operand{
	public String constant;
	public Constant(String constant){
		this.constant = constant;
	}
	
	@Override
	public String toString() {
		return constant;
	}
	
	public Operand clone(){
		return new Constant(this.constant);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((constant == null) ? 0 : constant.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Constant))
			return false;
		Constant other = (Constant) obj;
		if (constant == null) {
			if (other.constant != null)
				return false;
		} else if (!constant.equals(other.constant))
			return false;
		return true;
	}
	
	
}
