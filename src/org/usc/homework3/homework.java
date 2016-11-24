package org.usc.homework3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class homework {

	public static void main(String[] args) throws Exception {
		testUnify();
		String inputFile = "homework3/input.txt";
		String outputFile = "homework3/output.txt";
		ArrayList<Operand> kb = new ArrayList<>();
		ArrayList<Operand> queries = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		
		int nQ = Integer.parseInt(reader.readLine());
		for(int i=0;i<nQ;i++){
			String line = reader.readLine();
			InfixToPosfix infixToPosfix = new InfixToPosfix(line);
			Operand sentence = infixToPosfix.getSentence();
			queries.add(sentence);
		}		

		int nKB = Integer.parseInt(reader.readLine());
		for(int i=0;i<nKB;i++){
			String line = reader.readLine();
			InfixToPosfix infixToPosfix = new InfixToPosfix(line);
			Operand sentence = infixToPosfix.getSentence();
			CNFConverter cnfConverter = new CNFConverter();
			cnfConverter.convert(sentence);
			kb.add(sentence);
		}
		reader.close();
		StandardizeKB standardizeKB = new StandardizeKB(kb);
		kb = standardizeKB.standardize();
		printKB(kb);
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
		
		//Inference
		for(Operand query: queries){
			if(query instanceof Predicate){
				Resolution resolution = new Resolution(kb, (Predicate)query, standardizeKB);
				boolean inference = resolution.resolution();
				System.out.println(result(inference));
				writer.write(result(inference) + "\n");
			}
		}
		writer.close();
		
	}
	
	private static String result(boolean inference){
		return inference?"TRUE":"FALSE";
	}
	
	private static void testUnify(){
		Predicate a = new Predicate();
		a.predicateName = "A";
		a.parameters = new ArrayList<>();
		a.parameters.add(new Constant("Jack"));
		
		Predicate b = new Predicate();
		b.predicateName = "A";
		b.parameters = new ArrayList<>();
		b.parameters.add(new Constant("Mary"));
		
		Unifier unify = new Unifier();
		System.out.println(unify.unify(a, b));
	}
	
	private static void printKB(ArrayList<Operand> kb){
		for(Operand sentence: kb){
			System.out.print(sentence+"\t");
//			if(sentence instanceof ConnectedPredicate){
//				if(((ConnectedPredicate) sentence).operator.equals(Operators.AND))
//					System.out.println(true);
//				else
//					System.out.println(false);
//			}else{
//				System.out.println("Not Connected");
//			}
			System.out.println();
		}
	}
}
