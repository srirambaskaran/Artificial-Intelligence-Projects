package org.usc.homework1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

class BFS{
	private Node start;
	private Node goal;
	private Graph g;
	private Set<Node> explored;
	private Queue<Node> frontier;
	private OutputPath path;
	private HashMap<Edge,Integer> orderOfPreference;
	public BFS(Node start, Node goal, Graph g, HashMap<Edge,Integer> orderOfPreference){
		this.start = start;
		this.goal = goal;
		this.explored = new HashSet<>();
		this.frontier = new LinkedList<>();
		this.path = new OutputPath();
		this.g = g;
		this.orderOfPreference = orderOfPreference;
	}
	public OutputPath getPath(){
		return this.path;
	}
	public void runAlgorithm(){
		//Defining start node and adding it to frontier
		start.addProperty("parent", null);
		start.addProperty("depth",0);
		start.addProperty("path-cost", 0);
		this.frontier.add(start);
		while(frontier.size() > 0){
			Node node = frontier.poll();
			if(node.equals(goal)){
				path.createPath(node);
				break;
			}
			explored.add(node);
			/*
			 * Sorting the neighbors so that we pull out the ones that are
			 * occurring the first in our problem definition.
			 */
			ArrayList<Node> neighbours = new ArrayList<Node>(g.getAllNeighbours(node));
			Collections.sort(neighbours, new Comparator<Node>() {
				public int compare(Node a, Node b){
					Edge aEdge = g.getEdge(node, a);
					Edge bEdge = g.getEdge(node, b);
					int index1 = orderOfPreference.get(aEdge);
					int index2 = orderOfPreference.get(bEdge);
					return (index1 - index2);
				}
			});

			for(Node neighbour: neighbours){

				if(!frontier.contains(neighbour) && !explored.contains(neighbour)){
					neighbour.addProperty("parent",node);
					neighbour.addProperty("path-cost",(Integer)(node.getProperty("path-cost"))+1);
					neighbour.addProperty("depth",(Integer)(node.getProperty("depth"))+1);		
					frontier.add(neighbour);
				}
			}
		}
	}
}