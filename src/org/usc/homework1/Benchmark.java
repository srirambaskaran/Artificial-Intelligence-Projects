package org.usc.homework1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Calendar;

public class Benchmark {

	public static void main(String[] args) throws Exception {		
		String testCasesFolder="cases";
		String userOutputFolder="results";

		File programInputFile = new File("input.txt");
		File programOutputFile = new File("output.txt");
		File logFile = new File("log");
		File testCases = new File(testCasesFolder);
		for(File testCase: testCases.listFiles()){
			if(!testCase.getName().startsWith("input")) continue;

			copyFile(testCase,programInputFile);
			
			long startTime = Calendar.getInstance().getTimeInMillis();
			Process p = Runtime.getRuntime().exec("cmd /C java homework");
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
			String line = null;
			while ((line = in.readLine()) != null) {
				writer.write(line);
			}
			writer.close();
			p.waitFor();
			System.out.println("++ "+testCase.getName()+" ++");
			System.out.println("Time taken: "+((double)(Calendar.getInstance().getTimeInMillis()) - startTime) / 1000.0);
			String diff = difference(programOutputFile,new File(testCasesFolder+File.separator+testCase.getName().replace("input","output")));
			System.out.println(diff);
			copyFile(programOutputFile, new File(userOutputFolder+File.separator+testCase.getName().replace("input","output")));
			
		}
	}

	public static void copyFile(File sourceFile, File destinationFile) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(destinationFile));
		String line = "";
		while((line = reader.readLine())!=null){
			writer.write(line+"\r\n");
		}
		reader.close();
		writer.close();
	}
	
	public static String difference(File oneFile, File secondFile) throws Exception{
		BufferedReader reader = new BufferedReader(new FileReader(oneFile));
		BufferedReader reader2 = new BufferedReader(new FileReader(secondFile));
		StringBuilder changes = new StringBuilder();
		String line1 = "";
		String line2 = "";
		int i1=0;
		int i2=0;
		boolean diff = false;
		while(true){
			if((line1 = reader.readLine())==null){
				break;
			}
			if((line2 = reader2.readLine())==null){
				break;
			}
			if(!line1.equals(line2)){
				changes.append("("+i1+")"+line1+" ==== "+line2+"("+i2+")\r\n");
				diff = true;
				i1++;
				i2++;
			}
			else{
				i1++;
				i2++;
			}
		}
		
		if(i1 == i2 && !diff){
			changes.append("No difference in the two files");
		}
		
		reader.close();
		reader2.close();
		return changes.toString();
		
	}
}
