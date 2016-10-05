package org.usc.homework1;

import java.util.HashMap;

class Heuristic{
	private HashMap<Node, Integer> heuristic;
	public Heuristic(){
		this.heuristic = new HashMap<>();
	}
	public Heuristic(Node node, Integer value){
		if(heuristic == null)
			this.heuristic = new HashMap<>();
		heuristic.put(node, value);
	}
	public Integer getHeuristic(Node node){
		return this.heuristic.get(node);
	}

	public Integer getHeuristic(String node){
		Node temp = new Node(node);
		return this.heuristic.get(temp);
	}
	public HashMap<Node, Integer> getHeuristic() {
		return heuristic;
	}
	public void setHeuristic(HashMap<Node, Integer> heuristic) {
		this.heuristic = heuristic;
	}

	public void addHeuristic(Node node, Integer value){
		this.heuristic.put(node, value);
	}
	
	public boolean containsKey(Node node){
		return this.heuristic.containsKey(node);
	}
}