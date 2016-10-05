package org.usc.homework1;

class Edge{
	private Node a;
	private Node b;
	private int weight;
	public Edge(Node a, Node b, int weight) {
		super();
		this.a = a;
		this.b = b;
		this.weight = weight;
	}
	public Node getA() {
		return a;
	}
	public void setA(Node a) {
		this.a = a;
	}
	public Node getB() {
		return b;
	}
	public void setB(Node b) {
		this.b = b;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	@Override
	public String toString() {
		return "Edge [a=" + a + ", b=" + b + ", weight=" + weight + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
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
		Edge other = (Edge) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}
	
}