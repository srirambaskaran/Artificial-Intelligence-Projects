package org.usc.homework3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfixToPosfix {

	private String infixString;
	private ArrayList<Token> posfix;
	private Operand sentence;

	public InfixToPosfix(String infix){
		this.infixString = infix;
		this.posfix = new ArrayList<Token>();
		infixToPosfix();
		this.sentence = convertToPosfixExpression();
	}
	public Operand getSentence() {
		return sentence;
	}
	private Operand convertToPosfixExpression(){
		Stack<Operand> operands = new Stack<>();
		for(Token token: posfix){
			if(token instanceof Operator){			
				Operator operator = (Operator)token;
				if(operator.equals(Operators.NOT)){
					Operand operand = (Operand)operands.pop();
					operand.truthValue = !operand.truthValue;
					operands.add(operand);
				}else{
					Operand right = (Operand)operands.pop();
					Operand left = (Operand)operands.pop();
					ConnectedPredicate connectedOperand = new ConnectedPredicate();
					connectedOperand.left = left;
					connectedOperand.right = right;
					connectedOperand.operator = operator;
					operands.add(connectedOperand);
				}
			}else if (token instanceof Operand){
				operands.add((Operand)token);
			}
		}
		return operands.pop();
	}

	private HashMap<Integer, Operand> operands(String infixReplaced){
		HashMap<Integer, Operand> operandPositions = new  HashMap<>();
		String regex="(([A-Za-z]+)\\((([A-Za-z]+)(\\,[A-Za-z]+)*)\\))+";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(infixReplaced);
		while(matcher.find()){
			Predicate tempPredicate = new Predicate();
			String predicateName=matcher.group(2);
			String paramsString=matcher.group(3);
			String[] params = paramsString.split(",");
			for(String param: params){
				boolean isVariable = Character.isLowerCase(param.charAt(0));
				if(isVariable)
					tempPredicate.parameters.add(new Variable(param));
				else
					tempPredicate.parameters.add(new Constant(param));
			}
			tempPredicate.predicateName = predicateName;
			tempPredicate.setSize(matcher.end()-matcher.start());
			operandPositions.put(matcher.start(), tempPredicate);
		}
		return operandPositions;
	}

	private void infixToPosfix(){
		String infixReplaced = infixString.replaceAll("\\s+", "");
		HashMap<Integer, Operand> operandPositions = operands(infixReplaced);

		char[] infixArray = infixReplaced.toCharArray();
		Stack<Operator> operatorStack = new Stack<>();

		for(int i = 0;i<infixArray.length;i++){
			char infixElement = infixArray[i];
			if(infixElement == '('){
				operatorStack.push(Operators.OPEN_PARAN);
				continue;
			}else if(infixElement == ')'){
				while(!operatorStack.peek().equals(Operators.OPEN_PARAN)){
					posfix.add(operatorStack.pop());
				}
				operatorStack.pop();
				continue;
			}

			Operator tempOperator = null;
			if(infixArray[i] == '~'){
				//Not Statement follows it.
				//Check if its a predicate or connected predicate
				tempOperator = Operators.NOT;
			}else if(infixArray[i] == '='){
				//implication
				i++;
				tempOperator = Operators.IMPLICATION;
			}else if(infixArray[i] == '&'){
				tempOperator = Operators.AND;
			}else if(infixArray[i] == '|'){
				tempOperator = Operators.OR;
			}

			if(tempOperator != null){
				if(operatorStack.isEmpty() || operatorStack.peek().equals(Operators.OPEN_PARAN)){
					operatorStack.push(tempOperator);
				}else{
					if(operatorStack.peek().precedence > tempOperator.precedence){
						posfix.add(operatorStack.pop());
					}
					operatorStack.push(tempOperator);
				}
			}else{
				Operand predicate = operandPositions.get(i);
				i += predicate.size()-1;
				this.posfix.add(predicate);
			}
		}
		while(!operatorStack.empty()){
			Operator top = operatorStack.pop();
			if(!top.equals(Operators.OPEN_PARAN))
				posfix.add(top);
		}

	}
}