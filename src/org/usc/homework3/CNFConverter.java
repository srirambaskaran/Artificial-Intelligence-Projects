package org.usc.homework3;

public class CNFConverter {
	
	public void convert(Operand sentence) {
		implicitElimination(sentence);
		demorgan(sentence);
		distribution(sentence);
		
	}
	
	
	private void distribution(Operand sentence) {
		if(sentence instanceof ConnectedPredicate){
			ConnectedPredicate connectedPredicate = (ConnectedPredicate)sentence;
			
			if(connectedPredicate.operator.equals(Operators.OR)){
				if(connectedPredicate.left instanceof ConnectedPredicate 
						&& connectedPredicate.right instanceof Predicate
						&& ((ConnectedPredicate)connectedPredicate.left).operator.equals(Operators.AND)){
					ConnectedPredicate alphaBeta = (ConnectedPredicate)connectedPredicate.left;
					ConnectedPredicate left = new ConnectedPredicate();
					left.left = alphaBeta.left;
					left.right = connectedPredicate.right;
					left.operator = Operators.OR;
					
					ConnectedPredicate right = new ConnectedPredicate();
					right.left = alphaBeta.right;
					right.right = connectedPredicate.right;
					right.operator = Operators.OR;
					
					connectedPredicate.left = left;
					connectedPredicate.right = right;
					connectedPredicate.operator = Operators.AND;
					
				}else if(connectedPredicate.left instanceof Predicate 
						&& connectedPredicate.right instanceof ConnectedPredicate
						&& ((ConnectedPredicate)connectedPredicate.right).operator.equals(Operators.AND)){
					ConnectedPredicate alphaBeta = (ConnectedPredicate)connectedPredicate.right;
					ConnectedPredicate left = new ConnectedPredicate();
					left.left = connectedPredicate.left;
					left.right= alphaBeta.left;
					left.operator = Operators.OR;
					
					ConnectedPredicate right = new ConnectedPredicate();
					right.left = connectedPredicate.left;
					right.right = alphaBeta.right;
					right.operator = Operators.OR;
					
					connectedPredicate.left = left;
					connectedPredicate.right = right;
					connectedPredicate.operator = Operators.AND;
					
					
				}
			}
			if(connectedPredicate.left instanceof ConnectedPredicate)
				distribution(connectedPredicate.left);
			if(connectedPredicate.right instanceof ConnectedPredicate)
				distribution(connectedPredicate.right);
		}
		
	}

	private void implicitElimination(Operand sentence){
		if(sentence instanceof ConnectedPredicate){
			ConnectedPredicate connectedPredicate = (ConnectedPredicate)sentence;
			if(connectedPredicate.operator.equals(Operators.IMPLICATION)){
				//Implication Elimination
				connectedPredicate.operator = Operators.OR;
				connectedPredicate.left.truthValue = !connectedPredicate.left.truthValue;
			}
			if(connectedPredicate.left instanceof ConnectedPredicate)
				implicitElimination(connectedPredicate.left);
			if(connectedPredicate.right instanceof ConnectedPredicate)
				implicitElimination(connectedPredicate.right);
		}
	}
	
	private void demorgan(Operand sentence){
		if(sentence instanceof ConnectedPredicate){
			ConnectedPredicate connectedPredicate = (ConnectedPredicate)sentence;
			if(connectedPredicate.operator.equals(Operators.AND) && !connectedPredicate.truthValue){
				connectedPredicate.operator = Operators.OR;
				connectedPredicate.truthValue = true;
				connectedPredicate.left.truthValue = !connectedPredicate.left.truthValue;
				connectedPredicate.right.truthValue = !connectedPredicate.right.truthValue;
			}else if(connectedPredicate.operator.equals(Operators.OR) && !connectedPredicate.truthValue){
				connectedPredicate.operator = Operators.AND;
				connectedPredicate.truthValue = true;
				connectedPredicate.left.truthValue = !connectedPredicate.left.truthValue;
				connectedPredicate.right.truthValue = !connectedPredicate.right.truthValue;
			}
			if(connectedPredicate.left instanceof ConnectedPredicate)
				demorgan(connectedPredicate.left);
			if(connectedPredicate.right instanceof ConnectedPredicate)
				demorgan(connectedPredicate.right);
		}
		
	}
}
