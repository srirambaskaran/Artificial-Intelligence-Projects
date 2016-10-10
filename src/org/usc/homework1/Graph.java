package org.usc.homework1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Graph{
	private Set<Node> nodes;
	private Set<Edge> edges;
	private HashMap<Node,HashMap<Node,Integer>> adjacencyList;
	private HashMap<Node,HashMap<Node,Edge>> edgeList;

	private boolean isDirected;
	private ArrayList<ArrayList<Integer>> adjacencyMatrix;

	public Graph(boolean isDirected){
		this.isDirected = isDirected;
		this.nodes = new HashSet<>();
		this.edges = new HashSet<>();
		this.adjacencyMatrix = new ArrayList<>();
		this.edgeList = new HashMap<>();
		this.adjacencyList = new HashMap<>();
	}
	public Node getNode(String id){
		for(Node node: nodes){
			if(node.getNode().equals(id))
				return node;
		}
		Node temp = new Node(id);
		nodes.add(temp);
		return temp;
	}
	
	public Edge getEdge(Node a, Node b){
		return this.edgeList.get(a).get(b);
	}

	public ArrayList<ArrayList<Integer>> getAdjacencyMatrix() {
		return adjacencyMatrix;
	}
	public Node createNode(String id){
		return getNode(id);
	}
	/**
	 * Returns the weight of the edge a, b
	 * @param a
	 * @param b
	 * @return -1 if the edge doesn't exist, else returns the weight
	 */
	public int getWeight(Node a, Node b){
		int weight = -1;
		if(adjacencyList.containsKey(a)){
			HashMap<Node, Integer> neighbours = adjacencyList.get(a);
			if(neighbours.containsKey(b)){
				weight = neighbours.get(b);
			}
		}
		return weight;
	}
	public Edge createEdge(Node a, Node b, int weight){

		if(!this.adjacencyList.containsKey(a)){
			HashMap<Node, Integer> neighbours = new HashMap<>();
			this.adjacencyList.put(a, neighbours);
			this.edgeList.put(a, new HashMap<>());
		}
		HashMap<Node, Integer> neighbours = adjacencyList.get(a);
		neighbours.put(b, weight);
		Edge e = new Edge(a, b, weight);
		this.edgeList.get(a).put(b, e);
		edges.add(e);
		return e;
	}

	public Set<Node> getAllNeighbours(Node v){
		if(!adjacencyList.containsKey(v) || adjacencyList.get(v) == null)
			return new HashSet<>();
		return adjacencyList.get(v).keySet();
		
	}

	public Set<Node> getNodes() {
		return nodes;
	}
	public Set<Edge> getEdges() {
		return edges;
	}
	@Override
	public String toString() {
		return "Graph [nodes=" + nodes + ", edges=" + edges + ", isDirected=" + isDirected + "]";
	}
}