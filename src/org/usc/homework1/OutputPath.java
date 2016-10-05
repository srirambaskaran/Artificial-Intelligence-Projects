package org.usc.homework1;

import java.util.ArrayList;

/**
/** This class will store the output path returned by the algorithm   
/** @author Sriram Baskaran											  
/**																	  		
 **/

class OutputPath{
	private ArrayList<Node> path;
	public OutputPath(){
		this.path = new ArrayList<>();
	}
	public void addToPath(Node node){
		this.path.add(node);
	}
	public void removeFromPath(Node node){
		this.path.remove(node);
	}
	public ArrayList<Node> getPath(){
		return this.path;
	}
	public void createPath(Node goal) {
		path.add(goal);
		if(goal.getProperty("parent") == null)
			return;
		createPath((Node)goal.getProperty("parent"));
	}
}