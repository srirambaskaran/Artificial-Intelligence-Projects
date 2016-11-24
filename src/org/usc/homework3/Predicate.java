package org.usc.homework3;

import java.util.ArrayList;

public class Predicate extends Operand {
	public String predicateName;
	public ArrayList<Operand> parameters;
	public Predicate(){
		this.parameters = new ArrayList<>();
	}
	public void setSize(int size){
		this.size = size;
	}
	public int size(){
		return this.size;
	}
	
	@Override
	public String toString() {
		StringBuilder returnString = new StringBuilder();
		returnString.append(predicateName+"(");
		for(Operand parameter: parameters){
			returnString.append(parameter+",");
		}
		
		return (this.truthValue?"":"(~")+returnString.substring(0, returnString.length()-1)+")"+(this.truthValue?"":")");
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((predicateName == null) ? 0 : predicateName.hashCode());
		int sum = 0;
		for(Operand o:parameters){
			sum += o.hashCode();
		}
		result = prime * result + ((parameters == null) ? 0 : sum);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Predicate other = (Predicate) obj;
		if (predicateName == null) {
			if (other.predicateName != null)
				return false;
		} else if (!predicateName.equals(other.predicateName))
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} 
		for(Operand o : parameters){
			if(!other.parameters.contains(o))
				return false;
		}
		return true;
	}
	public Predicate clone(){
		Predicate newPredicate = new Predicate();
		newPredicate.predicateName=predicateName;
		newPredicate.truthValue = this.truthValue;
		for(Operand parameter: parameters)
			newPredicate.parameters.add(parameter.clone());
		return newPredicate;
	}
}
