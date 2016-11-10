package org.usc.homework2;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Calendar;

public class Benchmark {

	public static void main(String[] args) {		
		String testCasesFolder="hw2-5000/INPUT/";
		String userOutputFolder="homework2-results";

		File programInputFile = new File("homework2/input.txt");
		File programOutputFile = new File("homework2/output.txt");
//		File logFile = new File("log");
		File testCases = new File(testCasesFolder);
		int hits = 0;
		int misses = 0;
		StringBuilder missesList = new StringBuilder();
		for(File testCase: testCases.listFiles()){
			try{
			if(!testCase.getName().endsWith(".in")) continue;

			copyFile(testCase,programInputFile);
			
			long startTime = Calendar.getInstance().getTimeInMillis();
//			Process p = Runtime.getRuntime().exec("cmd /C java homework");
			homework.main(args);
//			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
//			BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
//			String line = null;
//			while ((line = in.readLine()) != null) {
//				writer.write(line);
//			}
//			writer.close();
//			p.waitFor();
			System.out.println("++ "+testCase.getName()+" ++");
			System.out.println("Time taken: "+((double)(Calendar.getInstance().getTimeInMillis()) - startTime) / 1000.0);
			String diff = difference(programOutputFile,new File(testCasesFolder+File.separator+testCase.getName().replace("in","out")));
			System.out.println(diff);
			if(diff.contains("No difference")){
				hits++;
			}else{
				misses++;
				missesList.append(testCase.getName()+",");
			}
			copyFile(programOutputFile, new File(userOutputFolder+File.separator+testCase.getName().replace("in","out")));
			}catch(Exception e){
				System.out.println("++ "+testCase.getName()+" ++");
				System.err.println("Error! ");
			}
		}
		
		System.out.println("Hits: "+hits+"\tMisses "+misses);
		if(missesList.length()>0)
			System.out.println(missesList.substring(0,missesList.length()-1));
		else
			System.out.println("All Test Cases Passed");
	}

	public static void copyFile(File sourceFile, File destinationFile) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(destinationFile));
		String line = "";
		while((line = reader.readLine())!=null){
			writer.write(line+"\n");
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

