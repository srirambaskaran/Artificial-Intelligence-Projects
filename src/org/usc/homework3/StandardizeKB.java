package org.usc.homework3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;

public class StandardizeKB {
	private ArrayList<Operand> kb;
	private HashSet<Variable> variables;
	private int i;
	private char prefix = 'v';
	public StandardizeKB(ArrayList<Operand> kb) {
		this.kb = kb;
		this.variables = new HashSet<>();
		i=0;
	}
	
	public StandardizeKB(char prefix){
		this.prefix = prefix;
		this.variables = new HashSet<>();
	}

	private String generateUniqueVarName(){
		return prefix+""+(i++)+"";
	}

	public ArrayList<Operand> standardize(){
		for(Operand sentence: kb){
			if(sentence instanceof ConnectedPredicate){
				standardize((ConnectedPredicate)sentence, new LinkedHashMap<>());
			}
			if(sentence instanceof Predicate){
				standardize((Predicate)sentence, new LinkedHashMap<>());
			}
		}
		return kb;
	}

	public void standardize(ConnectedPredicate connectedPredicate, LinkedHashMap<String,String> sentenceSubst){
		Operand left = connectedPredicate.left;
		Operand right = connectedPredicate.right;
		if(left instanceof ConnectedPredicate)
			standardize((ConnectedPredicate)left,sentenceSubst);
		else if(left instanceof Predicate)
			standardize((Predicate)left,sentenceSubst);
		if(right instanceof ConnectedPredicate)
			standardize((ConnectedPredicate)right,sentenceSubst);
		else if(right instanceof Predicate)
			standardize((Predicate)right,sentenceSubst);

	}

	public void standardize(Predicate predicate, LinkedHashMap<String,String> sentenceSubst){

		ArrayList<Operand> params = predicate.parameters;
		for(Operand operand: params){
			if(operand instanceof Variable){
				Variable param = (Variable) operand;  
				if(variables.contains(param)){
					if(sentenceSubst.containsKey(param.variable)){
						param.variable = sentenceSubst.get(param.variable);
					}else{
						String varName = generateUniqueVarName();
						sentenceSubst.put(param.variable,varName);
						param.variable = varName;
					}
				}else{
					variables.add(param);
					sentenceSubst.put(param.variable,param.variable);
				}
			}
		}
	}
}