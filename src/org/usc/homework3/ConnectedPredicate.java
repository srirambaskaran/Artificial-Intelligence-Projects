package org.usc.homework3;

public class ConnectedPredicate extends Operand {
	public Operand left;
	public Operator operator;
	public Operand right;
	
	public boolean isConnectedPredicate(){
		return true;
	}
	
	public static boolean isSimple(ConnectedPredicate predicate){
		return !(predicate.left instanceof ConnectedPredicate) && !(predicate.right instanceof ConnectedPredicate);
	}
	
	public static boolean isImplication(ConnectedPredicate predicate){
		return predicate.operator.operator.equals(Operators.IMPLICATION);
	}
	
	public static boolean isAnd(ConnectedPredicate predicate){
		return predicate.operator.operator.equals(Operators.AND);
	}
	
	public static boolean isOr(ConnectedPredicate predicate){
		return predicate.operator.operator.equals(Operators.OR);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((operator == null) ? 0 : operator.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
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
		ConnectedPredicate other = (ConnectedPredicate) obj;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		if (operator == null) {
			if (other.operator != null)
				return false;
		} else if (!operator.equals(other.operator))
			return false;
		if (right == null) {
			if (other.right != null)
				return false;
		} else if (!right.equals(other.right))
			return false;
		return true;
	}
	
	public String toString(){
		return (this.truthValue?"":"~")+"("+left+""+operator+""+right+")";
	}
	
	public ConnectedPredicate clone(){
		ConnectedPredicate predicate = new ConnectedPredicate();
		predicate.left = this.left.clone();
		predicate.right = this.right.clone();
		predicate.operator = this.operator;
		return predicate;
	}
}
