package org.usc.homework3;

import java.util.LinkedHashMap;
import java.util.List;

public class Unifier {
	public LinkedHashMap<Variable, Operand> unify(Operand a, Operand b){
		return unify(a, b, new LinkedHashMap<Variable, Operand>());
	}

	public LinkedHashMap<Variable, Operand> unify(Operand a, Operand b, LinkedHashMap<Variable, Operand> theta){
		if(theta == null) return null;
		if(a.equals(b)) return theta;
		if(a instanceof Variable) return unifyVars((Variable) a, b, theta);
		if(b instanceof Variable) return unifyVars((Variable)b, a, theta);
		if(a instanceof Predicate && b instanceof Predicate){
			Predicate aPredicate = (Predicate)a;
			Predicate bPredicate = (Predicate)b;
			//Unifies every argument in the predicate
			return unify(aPredicate.parameters,bPredicate.parameters, unifyOps(aPredicate.predicateName, bPredicate.predicateName, theta));
		}
		
		return null;
	}
	
	private LinkedHashMap<Variable, Operand> unify(List<Operand> aParameters, List<Operand> bParameters,
			LinkedHashMap<Variable, Operand> theta) {
		if(theta == null || aParameters.size() != bParameters.size()) return null;
		if(aParameters.size() == 1 && bParameters.size() == 1) return unify(aParameters.get(0), bParameters.get(0), theta);
		return unify(aParameters.subList(1, aParameters.size()), bParameters.subList(1, bParameters.size()), unify(aParameters.get(0), bParameters.get(0), theta));
	}

	public LinkedHashMap<Variable, Operand> unifyVars(Variable a, Operand b, LinkedHashMap<Variable, Operand> theta){
		if (theta.keySet().contains(a))  return unify(theta.get(a), b, theta);
		if (theta.keySet().contains(b))  return unify(a, theta.get(b), theta);
		if (occurCheck(a, b, theta))  return null; 		
		return substitute(theta,a, b);
	}
	
	private LinkedHashMap<Variable, Operand> substitute(LinkedHashMap<Variable, Operand> theta, Variable a, Operand b) {
//		if(b instanceof Variable){
//			return null;
//		}
		theta.put((Variable)a, b);
		for(Variable v: theta.keySet()){
			theta.put(v, theta.get(v));
		}
		return theta;
	}

	private boolean occurCheck(Variable a, Operand b, LinkedHashMap<Variable, Operand> theta) {
		if (a.equals(b)) return true;
		if (theta.containsKey(b)) return occurCheck(a, theta.get(b), theta);
		return false;
	}

	public LinkedHashMap<Variable, Operand> unifyOps(String a, String b, LinkedHashMap<Variable, Operand> theta){		
		if (theta == null) return null;
		if (a.equals(b)) return theta;
		return null;
	}
}

