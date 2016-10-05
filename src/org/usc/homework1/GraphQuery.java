package org.usc.homework1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.util.Set;
/**
 * This allows the user to query the graph. This reads the input as given in the input specifications. 
 * </br>
 * Read usage comments to know how to use it. 
 * 
 * @author Sriram
 * 
 */
public class GraphQuery {
	public static void main(String[] args) throws Exception{
		Scanner in = new Scanner(System.in);
		
		System.out.print("Enter the graph file: ");
		String graphFile = in.nextLine();
		Graph g = new Graph(true);
		readInput(graphFile, g);
		String query="";
		while(!query.equals("quit")){
			System.out.print("> ");
			query = in.nextLine();
			if(query.equals("help"))
				printAllCommands();
			else
				query(query,g);
		}
		in.close();
	}
	private static void printAllCommands() {
		System.out.println("1. neighbours <node>");
		System.out.println("2. edgeweight <node-1> <node-2>");
		System.out.println("3. quit");
	}
	private static void query(String query, Graph g){
		if(query.startsWith("neighbours")){
			String node = query.split(" ")[1];
			Node temp = new Node(node);
			Set<Node> neighbours = g.getAllNeighbours(temp);
			for(Node neighbour: neighbours){				
				double weight = g.getWeight(temp, neighbour);
				System.out.println(neighbour.getNode()+" - "+weight);
			}
		}else if(query.startsWith("edgeweight")){
			String[] nodes = query.split(" ");
			String node1 = nodes[1];
			String node2 = nodes[2];
			double weight = g.getWeight(new Node(node1),new Node(node2));
			System.out.println(node1+"-"+node2+" -  "+ ((weight == -1)?"No Edge":weight));
		}else{
			System.out.println("Use one of the following commands");
			printAllCommands();
		}
	}
	
	public static void readInput(String inputFile, Graph g) throws Exception{
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		reader.readLine();
		reader.readLine();

		int noLiveData = Integer.parseInt(reader.readLine());
		
		for(int i=0;i<noLiveData;i++){
			String[] tokens = reader.readLine().split(" ");
			Node a = g.getNode(tokens[0]);
			Node b = g.getNode(tokens[1]);
			int weight = Integer.parseInt(tokens[2]);
			g.createEdge(a, b, weight);
		}

		reader.close();
	}
}
