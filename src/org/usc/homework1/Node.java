package org.usc.homework1;

import java.util.HashMap;

class Node{
	private String node;
	private HashMap<String, Object> otherProperties;
	public Node(String node){
		this.node = node;
		this.otherProperties = new HashMap<>();
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public void addProperty(String property, Object value){
		this.otherProperties.put(property, value);
	}
	public Object getProperty(String property){
		return this.otherProperties.get(property);
	}

	@Override
	public String toString() {
		return "Node [node=" + node + ", parent="+(otherProperties.get("parent")!=null?((Node)otherProperties.get("parent")).node:"null")+", path-cost= "+otherProperties.get("path-cost")+"]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		if (otherProperties == null) {
			if (other.otherProperties != null)
				return false;
		} else if (!otherProperties.equals(other.otherProperties))
			return false;
		return true;
	}
	
}