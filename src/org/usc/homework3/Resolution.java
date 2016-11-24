package org.usc.homework3;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.PriorityQueue;

public class Resolution {
	private ArrayList<Operand> kb;
	private Predicate negatedQuery;
	private ArrayList<Operand> disjunctions;
	private ArrayList<Clause> clauses;
	private StandardizeKB standardizer;
	private HashMap<String, PriorityQueue<Clause>> predicateMap;

	private final int maxIter = 50;
	private int clauseId;
	public Resolution(ArrayList<Operand> kb, Predicate query, StandardizeKB standardizer){
		this.kb = kb;

		this.negatedQuery = query.clone();
		this.negatedQuery.truthValue = !this.negatedQuery.truthValue;
		this.clauseId = 1;
		this.clauses = new ArrayList<>();
		this.predicateMap = new HashMap<>();
		this.standardizer = standardizer;
		separateClauses();
		fillPredicates();
		for(Clause clause:clauses){
			System.out.println(clause.getClauseId()+"\t"+clause);
		}
	}

	private void separateClauses(){
		disjunctions = new ArrayList<>();

		for(Operand operand: kb){
			disjunctions.addAll(separateClauses(operand));
		}
	}

	public ArrayList<Operand> separateClauses(Operand operand){
		Operand newOperand = operand.clone();
		ArrayList<Operand> clauses = new ArrayList<>();
		if(newOperand instanceof ConnectedPredicate){			
			if(((ConnectedPredicate)operand).operator.equals(Operators.AND)){
				Operand left = ((ConnectedPredicate)newOperand).left;
				Operand right = ((ConnectedPredicate)newOperand).right;
				clauses.addAll(separateClauses(left));
				clauses.addAll(separateClauses(right));

			}else{
				ConnectedPredicate temp = ((ConnectedPredicate)operand).clone();
				clauses.add(temp);
			}
		}else if(operand instanceof Predicate){
			Predicate temp = ((Predicate)operand).clone(); 
			clauses.add(temp);
		}
		return clauses;
	}
	private String getPredicateKey(Predicate p){
		return (p.truthValue ? "":"~")+""+p.predicateName;
	}
	private String getNegatedPredicateKey(Predicate p){
		return (p.truthValue ? "~":"")+""+p.predicateName;
	}
	public boolean resolution(){		
		int iter=0;
		PriorityQueue<Clause> resolvents = new PriorityQueue<>(new LengthComparator());
		Clause queryClause = new Clause();
		queryClause.addPredicate(negatedQuery);
		queryClause.setClauseId(clauseId++);
		resolvents.add(queryClause);
		while(!resolvents.isEmpty() && iter < maxIter){
			iter++;
			Clause c1 = resolvents.poll();
			for(Predicate p1 : c1.getPredicates()){
				PriorityQueue<Clause> possibleClauses = predicateMap.get(getPredicateKey(p1));
				
				if(possibleClauses == null) continue;
				for(Clause c2 : possibleClauses){
					ArrayList<Clause> resolvedClauses = resolve(c1,c2);
					for(Clause resolvedClause: resolvedClauses){
						if(resolvedClause == null || resolvedClause.getPredicates().size() == 0){ 
							System.out.println(resolvedClause.getClauseId()+"\t"+c1.getClauseId()+"-"+c2.getClauseId()+"\t"+resolvedClause);
							return true;
						}
						if(!clauses.contains(resolvedClause)){
							resolvents.add(resolvedClause);
							addToPredicateMap(resolvedClause);
							clauses.add(resolvedClause);
							System.out.println(resolvedClause.getClauseId()+"\t"+c1.getClauseId()+"-"+c2.getClauseId()+"\t"+resolvedClause);
						}
					}
				}
				break; //have to do it only once
			}
		}
		System.out.println(iter);
		return false;
	}
	private void addToPredicateMap(Clause c){
		for(Predicate temp:c.getPredicates()){
			String key = getNegatedPredicateKey(temp);
			if(predicateMap.containsKey(key)){
				predicateMap.get(key).add(c);
			}else{
				predicateMap.put(key, new PriorityQueue<>(new LengthComparator()));
				predicateMap.get(key).add(c);
			}
		}
	}
	private void fillPredicates(){
		for(Operand o : disjunctions){
			HashSet<Predicate> predicates = getPredicates(o);
			Clause c = new Clause();
			c.setClauseId(clauseId++);
			c.setPredicates(predicates);
			clauses.add(c);
			addToPredicateMap(c);
		}
	}

	private HashSet<Predicate> getPredicates(Operand o){
		HashSet<Predicate> predicates = new HashSet<>();
		if(o instanceof ConnectedPredicate){
			ConnectedPredicate connectedPredicate = (ConnectedPredicate) o;
			HashSet<Predicate> left = getPredicates(connectedPredicate.left);
			HashSet<Predicate> right = getPredicates(connectedPredicate.right);
			predicates.addAll(left);
			predicates.addAll(right);
		}else if(o instanceof Predicate){
			Predicate predicate = (Predicate)o;
			predicates.add(predicate);
		}
		return predicates;
	}

	private ArrayList<Clause> resolve(Clause c1, Clause c2) {
		ArrayList<Clause> resolvents = new ArrayList<>();
		HashSet<Predicate> c1Predicates = c2.getPredicates();
		HashSet<Predicate> c2Predicates = c1.getPredicates();
		Unifier unifier = new Unifier();
		HashSet<String> resolvedPredicates = new HashSet<>();
		LinkedHashMap<Variable, Operand> clauseUnify = new LinkedHashMap<>();
		LinkedHashMap<String, String> subst = new LinkedHashMap<>();
		for(Predicate p1 : c1Predicates ){
			for(Predicate p2 : c2Predicates ){
				if(!p1.predicateName.equals(p2.predicateName)) continue;
				if(p1.truthValue == p2.truthValue) continue;
				LinkedHashMap<Variable, Operand> temp= unifier.unify(p1, p2, clauseUnify);
				if(temp != null){
					resolvedPredicates.add(p1.predicateName);
					clauseUnify.putAll(temp);
					Clause resolvent = new Clause();
					resolvent.setClauseId(this.clauseId++);
					resolvents.add(resolvent);
					for(Predicate other: c1Predicates){
						if(other.equals(p1)) continue;
						Predicate otherClone = other.clone();
						resolvent.addPredicate(otherClone);
						ArrayList<Operand> newParams = new ArrayList<>();
						for(Operand param: otherClone.parameters){
							if(param instanceof Variable){
								if(clauseUnify.containsKey(param)){
									Operand c = clauseUnify.get(param);
									newParams.add(c);
								}
								else{
									newParams.add(param);
								}
							}else{
								newParams.add(param);
							}

							otherClone.parameters = newParams;
							standardizer.standardize(otherClone, subst);
						}

					}
					for(Predicate other: c2Predicates){
						if(other.equals(p2)) continue;
						Predicate otherClone = other.clone();
						resolvent.addPredicate(otherClone);
						ArrayList<Operand> newParams = new ArrayList<>();
						for(Operand param: otherClone.parameters){
							if(param instanceof Variable){
								if(clauseUnify.containsKey(param)){
									Operand c = clauseUnify.get(param);
									newParams.add(c);
								}
								else{
									newParams.add(param);
								}
							}else{
								newParams.add(param);
							}
						}

						otherClone.parameters = newParams;
						standardizer.standardize(otherClone, subst);
					}
				}
			}
		}
		return resolvents;
	}
}

class LengthComparator implements Comparator<Clause>{	
	@Override
	public int compare(Clause arg0, Clause arg1) {
		Integer length1 = arg0.getPredicates().size();
		Integer length2 = arg1.getPredicates().size();
		return length1.compareTo(length2);
	}
}