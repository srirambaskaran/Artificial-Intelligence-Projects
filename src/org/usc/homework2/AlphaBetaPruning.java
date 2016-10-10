package org.usc.homework2;

import java.util.ArrayList;

public class AlphaBetaPruning {
	private char yourPlay;
	private GameBoard currentState;
	private int maxDepth;
	
	public AlphaBetaPruning(GameBoard currentState, char yourPlay, int maxDepth){
		this.currentState = currentState;
		this.yourPlay = yourPlay;
		this.maxDepth = maxDepth;
	}
	
	public GameBoard minimaxDecision(){
		int depth = 0;
		ArrayList<GameBoard> actions = action(currentState,depth+1);
		int v = Integer.MIN_VALUE;
		GameBoard chosen = null;
		for(GameBoard a : actions){
			int utilityFromAction = min(a, depth+1);;
			if(v < utilityFromAction){
				v = utilityFromAction;
				chosen = a;
			}
		}
		
		return chosen;
	}
	
	public int min(GameBoard currentState, int depth){
		if(depth == maxDepth)
			return getUtility(currentState);
		int v = Integer.MAX_VALUE;
		ArrayList<GameBoard> actions =action(currentState, depth+1); 
		for(GameBoard a : actions){
			v = Math.min(v, max(a,depth+1));
		}
		return v;
	}

	private int max(GameBoard currentState, int depth) {
		if(depth == maxDepth)
			return getUtility(currentState);
		int v = Integer.MIN_VALUE;
		ArrayList<GameBoard> actions = action(currentState,depth+1);
		for(GameBoard a:actions){
			v = Math.max(v, min(a,depth+1));
		}
		return v;
	}
	
	private int getUtility(GameBoard board){
		if(yourPlay == 'X')
			return board.getSumX() - board.getSumO();
		else if(yourPlay == 'O')
			return board.getSumO() - board.getSumX();
		else
			return 0;
	}

	private ArrayList<GameBoard> action(GameBoard currentState,int depth) {
		//Returns a sorted order of the next moves
		ArrayList<GameBoard> neighbours = currentState.generateNextMoves(depth);
		return neighbours;
//		if(depth%2 == 0){
//			Collections.sort(neighbours,new Comparator<GameBoard>(){
//				public int compare(GameBoard g1, GameBoard g2){
//					Integer utilityValue1 = g1.getNextPlay() == 'X'?g1.getSumX()-g1.getSumO() :g1.getSumO()-g1.getSumX();
//					Integer utilityValue2 = g2.getNextPlay() == 'X'?g2.getSumX()-g2.getSumO() :g2.getSumO()-g2.getSumX();
//					return utilityValue1.compareTo(utilityValue2);
//				}
//			});
//		}else{
//			Collections.sort(neighbours,new Comparator<GameBoard>(){
//				public int compare(GameBoard g1, GameBoard g2){
//					Integer utilityValue1 = g1.getNextPlay() == 'X'?g1.getSumX()-g1.getSumO() :g1.getSumO()-g1.getSumX();
//					Integer utilityValue2 = g2.getNextPlay() == 'X'?g2.getSumX()-g2.getSumO() :g2.getSumO()-g2.getSumX();
//					return -utilityValue1.compareTo(utilityValue2);
//				}
//			});
//		}
//		return neighbours;
	}
}
