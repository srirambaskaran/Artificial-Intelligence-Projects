package org.usc.homework2;

public class Game {
	private String algorithm;
	private GameBoard current;
	private GameBoard nextMove;
	private int maxDepth;
	private double timeRem;
	private char nextPlay;
	public Game(String algorithm, GameBoard current, String depthString, char nextPlay) {
		super();
		this.algorithm = algorithm;
		this.current = current;
		if(algorithm.equals("COMPETITION"))
			timeRem = Double.parseDouble(depthString);
		else
			this.maxDepth = Integer.parseInt(depthString);
		this.nextPlay = nextPlay;
	}
	public GameBoard getNextMove() {
		return nextMove;
	}
	public void play() {
		if(algorithm.equals("MINIMAX")){
			Minimax minimax = new Minimax(current, nextPlay, maxDepth);
			nextMove = createBoard(minimax.minimaxDecision());
		}else if(algorithm.equals("ALPHABETA")){
			AlphaBetaPruning alphaBetaPruning = new AlphaBetaPruning(current, nextPlay, maxDepth);
			nextMove = createBoard(alphaBetaPruning.alphaBetaSearch());
		}else if(algorithm.equals("COMPETITION")){
			Negascout negascout = new Negascout(current, nextPlay, timeRem);
			nextMove = createBoard(negascout.negascoutSearch());
		}
	}
	private GameBoard createBoard(Move m) {
		if(m == null) return new GameBoard();
		Cell cellChanged = m.cell;
		current.removeFromEmpty(cellChanged);
		if(nextPlay == 'X'){
			current.addToXs(cellChanged);
			for(Cell otherCell: m.otherCellsUpdated){
				current.removeFromOs(otherCell);
				current.addToXs(otherCell);
			}
		}else{
			current.addToOs(cellChanged);
			for(Cell otherCell: m.otherCellsUpdated){
				current.removeFromXs(otherCell);
				current.addToOs(otherCell);
			}
		}
		current.setMove(m);
		return current;
	}
}
