package org.usc.homework1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

class AStarSearch{
	private Node start;
	private Node goal;
	private Graph g;
	private Set<Node> explored;
	private Queue<Node> frontier;
	private OutputPath path;
	private Heuristic heuristic;
	public AStarSearch(Node start, Node goal, Graph g, HashMap<Edge,Integer> orderOfPreference, Heuristic heuristic){
		this.start = start;
		this.goal = goal;
		this.explored = new HashSet<>();
		this.heuristic = heuristic;
		this.frontier = new PriorityQueue<Node>(new Comparator<Node>(){
			public int compare(Node a, Node b){
				Integer aPathCost = Integer.parseInt(a.getProperty("estimated-path-cost").toString());
				Integer bPathCost = Integer.parseInt(b.getProperty("estimated-path-cost").toString());

				if(aPathCost == bPathCost){
					Node aParent = (Node) a.getProperty("parent");
					Node bParent = (Node) b.getProperty("parent");
					if(aParent.equals(bParent)){ //Tie Breaker 1: In the order of traffic information
						Edge aEdge = g.getEdge(aParent, a);
						Edge bEdge = g.getEdge(bParent, b);
						int index1 = orderOfPreference.get(aEdge);
						int index2 = orderOfPreference.get(bEdge);
						return (index1 - index2);
					}else{//Tie Breaker 2: when parents aren't the same but still the cost is same.
						return ((Long)a.getProperty("age")).compareTo((Long)b.getProperty("age"));
					}
				}else{
					return aPathCost.compareTo(bPathCost);

				}
			}
		});
		this.path = new OutputPath();
		this.g = g;
	}
	public OutputPath getPath(){
		return this.path;
	}
	public void runAlgorithm(){
		//Defining start node and adding it to frontier
		start.addProperty("parent", null);
		start.addProperty("depth",0);
		start.addProperty("estimated-path-cost", 0);
		start.addProperty("path-cost", 0);
		start.addProperty("cost-from-parent", 0);
		start.addProperty("age", Calendar.getInstance().getTimeInMillis());
		this.frontier.add(start);
		while(frontier.size() > 0){
			Node node = frontier.poll();
			if(node.equals(goal)){
				Node parent = (Node)node.getProperty("parent");
				node.addProperty("path-cost", (Integer)parent.getProperty("path-cost") + (Integer)node.getProperty("cost-from-parent"));
				path.createPath(node);
				break;
			}
			explored.add(node);
			Node parent = (Node)node.getProperty("parent");
			if(parent != null)
				node.addProperty("path-cost", (Integer)parent.getProperty("path-cost") + (Integer)node.getProperty("cost-from-parent"));
			ArrayList<Node> neighbours = new ArrayList<Node>(g.getAllNeighbours(node));

			for(Node neighbour: neighbours){
				Integer weightOnEdge = g.getWeight(node, neighbour);
				Integer fValue = (Integer)(node.getProperty("path-cost"))+weightOnEdge+heuristic.getHeuristic(neighbour);
				Integer newPathCost = (Integer)(node.getProperty("path-cost"))+weightOnEdge;
				if(!frontier.contains(neighbour) && !explored.contains(neighbour)){
					neighbour.addProperty("parent",node);
					neighbour.addProperty("estimated-path-cost",fValue);
					neighbour.addProperty("depth",(Integer)(node.getProperty("depth"))+1);
					neighbour.addProperty("cost-from-parent", weightOnEdge);
					neighbour.addProperty("path-cost", newPathCost);
					neighbour.addProperty("age", Calendar.getInstance().getTimeInMillis());
					frontier.add(neighbour);
				}
				else if(frontier.contains(neighbour)){
					if(((Integer)neighbour.getProperty("path-cost")) > newPathCost){
						frontier.remove(neighbour);
						neighbour.addProperty("estimated-path-cost", fValue);
						neighbour.addProperty("parent", node);
						neighbour.addProperty("cost-from-parent", weightOnEdge);
						neighbour.addProperty("depth",(Integer)(node.getProperty("depth"))+1);
						neighbour.addProperty("path-cost", newPathCost);
						neighbour.addProperty("age", Calendar.getInstance().getTimeInMillis());
						frontier.add(neighbour);
					}
				}else if(explored.contains(neighbour)){
					if(((Integer)neighbour.getProperty("path-cost")) > newPathCost){
						explored.remove(neighbour);
						neighbour.addProperty("estimated-path-cost", fValue);
						neighbour.addProperty("cost-from-parent", weightOnEdge);
						neighbour.addProperty("path-cost", newPathCost);
						neighbour.addProperty("parent", node);
						neighbour.addProperty("depth",(Integer)(node.getProperty("depth"))+1);
						neighbour.addProperty("age", Calendar.getInstance().getTimeInMillis());
						frontier.add(neighbour);
					}
				}
			}
		}
	}
}