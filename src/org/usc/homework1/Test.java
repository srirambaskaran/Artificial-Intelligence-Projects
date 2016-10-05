package org.usc.homework1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class Test {
	public static void main(String[] args) throws Exception {
		int i=0;
		String inputFolder = "random_inputs";
		String outputFolder = "random_outputs";
		String changedFolder = "more-test-cases";
		
		HashMap<String, String> algoMap = new HashMap<>();
		algoMap.put("A*","A_");
		algoMap.put("BFS","BFS");
		algoMap.put("DFS","DFS");
		algoMap.put("UCS","UCS");
		
		File input = new File(inputFolder);
		for(File inputFile: input.listFiles()){
			String inputFileContents = readFile(inputFile);
			for(String algo:algoMap.keySet()){
				String writeContents = inputFileContents.replace("CROSSFIRE", algo);
				String inputFileName = changedFolder+File.separator+"input"+i+".txt";
				String outputFileName = changedFolder+File.separator+"output"+i+".txt";
				
				writeFile(new File(inputFileName), writeContents);
				copyFile(new File(outputFolder+File.separator+algoMap.get(algo)+File.separator+inputFile.getName()), new File(outputFileName));
				i++;
			}
		}
		System.out.println("Written "+i+" files");
	}
	public static void writeFile(File destination, String contents) throws Exception{
		BufferedWriter writer = new BufferedWriter(new FileWriter(destination));
		writer.write(contents);
		writer.close();
		
	}
	public static String readFile(File source) throws Exception{
		BufferedReader reader = new BufferedReader(new FileReader(source));
		StringBuilder inputStr = new StringBuilder();
		String line = "";
		while((line = reader.readLine())!=null){
			inputStr.append(line+ "\r\n");
		}
		reader.close();
		return inputStr.toString();
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
	
}

