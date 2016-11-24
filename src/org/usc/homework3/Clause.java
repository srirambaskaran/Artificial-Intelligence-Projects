package org.usc.homework3;

import java.util.HashSet;
import java.util.LinkedHashMap;

public class Clause{
	public LinkedHashMap<Variable, Operand> substitutions;
	private HashSet<Predicate> predicates;
	private int clauseId;
	
	public Clause(){
		this.predicates = new HashSet<>();
		this.substitutions = new LinkedHashMap<>();
	}
	
	public void addPredicate(Predicate p){
		this.predicates.add(p);
	}
	
	public HashSet<Predicate> getPredicates(){
		return this.predicates;
	}
	
	public void setPredicates(HashSet<Predicate> predicates){
		this.predicates = predicates;
	}
	
	public void subst(Variable key, Operand value){
		this.substitutions.put(key, value);
	}
	
	public Operand getSubst(Variable key){
		return this.substitutions.get(key);
	}
	
	
	
	public int getClauseId() {
		return this.clauseId;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		int sum = 0;
		for(Predicate p: this.predicates){
			sum+=p.hashCode();
		}
		result = prime * result + ((predicates == null) ? 0 : sum);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Clause))
			return false;
		Clause other = (Clause) obj;
		if (predicates == null) {
			if (other.predicates != null)
				return false;
		} 
		for(Predicate p : this.predicates){
			if(!other.predicates.contains(p))
				return false;
		}
		return true;
	}

	public void setClauseId(int id) {
		this.clauseId = id;
	}

	@Override
	public String toString() {
		return predicates.toString();
	}
}
