package org.usc.homework1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class TestCaseRunner {

	public static void main(String[] args) throws Exception{
		String inputRootFolder="testcases-generated-shawn";
		String algoInputFile="input.txt";
		String algoOutputFile="output.txt";
		String outputRootFolder="testcases-generated-output-shawn";
//		String[] algorithms={"BFS","DFS","UCS","A*"};
		File[] testCases = new File(inputRootFolder).listFiles();
		StringBuilder testCaseData = new StringBuilder();
//		String[] noAStars = {"1","2","3","4","5","6","11","12"};
//		int i=1;
		for(File testCase:testCases){
//			boolean runAStar = true;
//			for(String noAStar:noAStars){
//				if(testCase.getName().contains("testcase"+noAStar+".txt"))
//					runAStar = false;
//			}
			testCaseData.setLength(0);
			BufferedReader reader = new BufferedReader(new FileReader(testCase));
			String line = "";
			while((line=reader.readLine())!=null){
				testCaseData.append(line+"\r\n");
			}
			reader.close();
			
//			for(String algorithm:algorithms){
//				if(!runAStar && algorithm.equals("A*"))
//					continue;
//					
				BufferedWriter inputFileWriter = new BufferedWriter(new FileWriter(algoInputFile));
//				inputFileWriter.write(algorithm+"\r\n");
				inputFileWriter.write(testCaseData.toString());
				inputFileWriter.close();
//				System.out.println("Running: "+testCase.getName()+" Algorithm: "+algorithm);
				System.out.println("Running: "+testCase.getName());
				GraphSearch.main(null);
				//running the algorithm
				BufferedReader outputFileReader = new BufferedReader(new FileReader(algoOutputFile));
				BufferedWriter outputFileWriter = new BufferedWriter(new FileWriter(outputRootFolder+File.separator+testCase.getName().replace("input","output")));
				line = "";
				while((line = outputFileReader.readLine())!=null){
					outputFileWriter.write(line+"\r\n");
				}
				outputFileReader.close();
				outputFileWriter.close();
//			}
		}
	}
}
