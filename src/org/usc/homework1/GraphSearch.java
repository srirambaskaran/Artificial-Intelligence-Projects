package org.usc.homework1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implements 4 search algorithms <br/>
 * Uninformed: Breadth First Search, Depth First Search, Uniform Cost Search<br/>
 * Informed: A*<br/>
 * Input File: input.txt<br/>
 * Output File: output.txt<br/><br/><br/>
 * 
 * input.txt format<br/>
 * ALGORITHM : (DFS, A*, BFS, UCS)<br/>
 * START_NODE<br/>
 * GOAL_NODE<br/>
 * NUMBER_OF_EDGES_IN_GRAPH<br/>
 * EDGE 1 (format: node1 node2 weight)<br/>
 * EDGE 2 (format: node1 node2 weight)<br/>
 * ..<br/>
 * EDGE M (format: node1 node2 weight)<br/><br/>
 * NUMBER_OF_HEURISTICS (will be used only in A*, give it 0 otherwise)<br/>
 * HEURISTIC 1 (format: node1 value)<br/>
 * HEURISTIC 2 (format: node2 value)<br/>
 * ..<br/>
 * HEURISTIC N (format: node3 value)<br/>
 * <br/>
 * <br/>
 * output.txt<br/>
 * NODE PATH_COST (repeated from source to destination)<br/>
 * 
 * 
 * @author Sriram
 *
 */
public class GraphSearch {

	private String algorithm;
	private Node start;
	private Node end;
	private int noLiveData;
	private int noSundayData;
	private Graph g;
	private Heuristic heuristic; //only used for A*
	private String inputFile;
	private String outputFile;
	private HashMap<Edge,Integer> orderOfPreference;

	public GraphSearch(String inputFile, String outputFile){
		this.inputFile = inputFile;
		this.outputFile = outputFile;
	}

	public void aStar() throws Exception{
		if(start.equals(end)){
			OutputPath path =  new OutputPath();
			path.addToPath(start);
			start.addProperty("path-cost", 0);
			writeOutput(path);
		}else{
			AStarSearch aStarSearch = new AStarSearch(start, end, g, orderOfPreference, heuristic);
			aStarSearch.runAlgorithm();
			writeOutput(aStarSearch.getPath());
		}
	}

	public void ucs() throws Exception{
		if(start.equals(end)){
			OutputPath path =  new OutputPath();
			path.addToPath(start);
			start.addProperty("path-cost", 0);
			writeOutput(path);
		} else {
			UniformCostSearch ucs = new UniformCostSearch(start, end, g, orderOfPreference);
			ucs.runAlgorithm();
			writeOutput(ucs.getPath());
		}
	}

	public void bfs() throws Exception{
		if(start.equals(end)){
			OutputPath path =  new OutputPath();
			path.addToPath(start);
			start.addProperty("path-cost", 0);
			writeOutput(path);
		}else{
			BFS bfs = new BFS(start, end, g, orderOfPreference);
			bfs.runAlgorithm();
			writeOutput(bfs.getPath());
		}
	}

	public void dfs() throws Exception{
		if(start.equals(end)){
			OutputPath path =  new OutputPath();
			path.addToPath(start);
			start.addProperty("path-cost", 0);
			writeOutput(path);
		} else {
			DFS dfs = new DFS(start, end, g, orderOfPreference);
			dfs.runAlgorithm();
			writeOutput(dfs.getPath());
		}
	}
	public void writeOutput(OutputPath outputPath) throws Exception{
		int pathLength = outputPath.getPath().size();
		BufferedWriter writer = new BufferedWriter(new FileWriter(this.outputFile));
		ArrayList<Node> path = outputPath.getPath();
		
		//Back traversing the path
		for(int i=pathLength-1;i>=0;i--){
			System.out.println(path.get(i).getNode()+" "+((Integer)path.get(i).getProperty("path-cost")).intValue());
			writer.write(path.get(i).getNode()+" "+((Integer)path.get(i).getProperty("path-cost")).intValue()+"\r\n");
		}
		writer.close();
	}
	
	/**
	 * This function reads the input from the file as specified.
	 * @return if the algorithm should be run or not
	 * @throws Exception
	 */
	public boolean readInput() throws Exception{
		BufferedReader reader = new BufferedReader(new FileReader(this.inputFile));
		this.algorithm = reader.readLine();
		this.g = new Graph(true);
		String startTreeNodeId = reader.readLine();
		String endTreeNodeId = reader.readLine();
		this.start = g.createNode(startTreeNodeId);
		this.end = g.createNode(endTreeNodeId);
		if(startTreeNodeId.equals(endTreeNodeId)){
			OutputPath path =  new OutputPath();
			path.addToPath(start);
			start.addProperty("path-cost", 0);
			writeOutput(path);
			reader.close();
			return false;
		}
		this.orderOfPreference = new HashMap<>();
		this.heuristic = new Heuristic();
		this.noLiveData = Integer.parseInt(reader.readLine());
		
		for(int i=0;i<noLiveData;i++){
			String[] tokens = reader.readLine().split(" ");
			Node a = g.getNode(tokens[0]);
			Node b = g.getNode(tokens[1]);
			//Initializes heuristics to 0. A check to ensure if sunday traffic info is not available.
			if(!this.heuristic.containsKey(a))
				this.heuristic.addHeuristic(a, 0);
			if(!this.heuristic.containsKey(b))
				this.heuristic.addHeuristic(b, 0);
			int weight = Integer.parseInt(tokens[2]);
			Edge e = g.createEdge(a, b, weight);
			if(!orderOfPreference.containsKey(e))
				orderOfPreference.put(e,i+1);
		}

		this.noSundayData = Integer.parseInt(reader.readLine());
		for(int i=0;i<noSundayData;i++){
			String[] tokens = reader.readLine().split(" ");
			Node v = g.getNode(tokens[0]);
			Integer heuristic = Integer.parseInt(tokens[1]);
			this.heuristic.addHeuristic(v, heuristic);
		}
		reader.close();
		return true;
	}
	
	public static void main(String[] args) throws Exception {
		String inputFile = "input.txt";
		String outputFile = "output.txt";
		GraphSearch graphSearch = new GraphSearch(inputFile, outputFile);
		boolean runAlgo = graphSearch.readInput(); //returns true only if the source is different from destination.
		if(runAlgo){
			if(graphSearch.algorithm.equals("DFS")){
				graphSearch.dfs();
			}else if(graphSearch.algorithm.equals("BFS")){
				graphSearch.bfs();
			}else if(graphSearch.algorithm.equals("UCS")){
				graphSearch.ucs();
			}else if(graphSearch.algorithm.equals("A*")){
				graphSearch.aStar();
			}
		}
	}
}
