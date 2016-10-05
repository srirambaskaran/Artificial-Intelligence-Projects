package org.usc.homework1;

import java.util.Calendar;
import java.util.Random;

public class ErdosRenyiGraphModel {
	public static void main(String[] args) {
		int n = 100;
		double p = 0.6;
		int weightMin = 1;
		int weightMax = 15;
		
		Graph g = new Graph(true);
		for(int i=0;i<n;i++){
			g.createNode("N"+i);
		}
		Random r = new Random(Calendar.getInstance().getTimeInMillis());
		
		for(Node v1: g.getNodes()){
			for(Node v2: g.getNodes()){
				if(v1.equals(v2))
					continue;
				if(Math.random() < p){
					int weight = r.nextInt(weightMax);
					weight+=weightMin;
					g.createEdge(v1, v2, weight);
				}
			}
		}
		int n1 = r.nextInt(n);
		int n2 = r.nextInt(n);
		while(n1==n2)
			n2=r.nextInt(n);
		
		Node start = g.getNode("N"+n1);
		Node end = g.getNode("N"+n2);
		System.out.println(start.getNode());
		System.out.println(end.getNode());
		System.out.println(g.getEdges().size());
		for(Edge e: g.getEdges())
			System.out.println(e.getA().getNode()+" "+e.getB().getNode()+" "+(int)e.getWeight());
		System.out.println("0");
	}
}

