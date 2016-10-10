package org.usc.homework2;

import org.usc.homework1.Graph;

public class Game {
	private String algorithm;
	private GameBoard current;
	private Graph g;
	private GameBoard nextMove;
	private int maxDepth;
	private char nextPlay;
	public Game(String algorithm, GameBoard current, int depth, char nextPlay) {
		super();
		this.algorithm = algorithm;
		this.current = current;
		this.maxDepth = depth;
		this.nextPlay = nextPlay;
	}
	public Graph getG() {
		return g;
	}
	public GameBoard getNextMove() {
		return nextMove;
	}
	public void play() {
		if(algorithm.equals("MINIMAX")){
			Minimax minimax = new Minimax(current, nextPlay, maxDepth);
			nextMove = minimax.minimaxDecision();
		}else if(algorithm.equals("ALPHABETA")){
			AlphaBetaPruning alphaBetaPruning = new AlphaBetaPruning(current, nextPlay, maxDepth);
			nextMove = alphaBetaPruning.alphaBetaSearch();
		}else if(algorithm.equals("COMPETITION")){
			
		}
	}
}
