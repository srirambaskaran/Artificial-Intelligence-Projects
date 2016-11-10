package org.usc.homework2;


import java.util.LinkedList;

public class Minimax {
	private char yourPlay;
	private GameBoard currentState;
	private int maxDepth;
	
	public Minimax(GameBoard currentState, char yourPlay, int maxDepth){
		this.currentState = currentState;
		this.yourPlay = yourPlay;
		this.maxDepth = maxDepth;
	}
	
	private char switchPlay(char yourPlay){
		return yourPlay == 'X'?'O':'X';
	}
	
	public Move minimaxDecision(){
		int depth = 0;
		Move dummyMove = new Move();
		dummyMove.moveType="Dummy";
		dummyMove.utlityAfterMove = getUtility(currentState);
		LinkedList<Move> initialMoves = new LinkedList<>();
		initialMoves.add(dummyMove);
		LinkedList<Move> possibleMoves = action(currentState, initialMoves , yourPlay, depth);
		
		int v = Integer.MIN_VALUE;
		Move chosen = null;
		for(Move m : possibleMoves){
			initialMoves.add(m);
			int utilityFromAction = minValue(currentState,initialMoves,depth+1,switchPlay(yourPlay));;
//			System.out.println(m+"\t"+utilityFromAction);
			if(v < utilityFromAction){
				v = utilityFromAction;
				chosen = m;
			}else if(v == utilityFromAction){
				if(!m.moveType.equals(chosen.moveType) && m.moveType.equals("Stake"))
					chosen = m;
				else if(m.moveType.equals(chosen.moveType)){
					if(m.cell.rowIndex < chosen.cell.rowIndex)
						chosen = m;
					else if(m.cell.rowIndex == chosen.cell.rowIndex)
						if(m.cell.colIndex < chosen.cell.colIndex)
							chosen = m;
				}
			}
			initialMoves.remove(m);
		}
		
		return chosen;
	}
	
	public int minValue(GameBoard currentState, LinkedList<Move> previousMoves, int depth, char play){
		if(depth == maxDepth)
			return previousMoves.get(previousMoves.size()-1).utlityAfterMove;
		int v = Integer.MAX_VALUE;
		LinkedList<Move> moves =action(currentState,previousMoves,play, depth);
		if(moves.size() == 0){
			return previousMoves.get(previousMoves.size()-1).utlityAfterMove;
		}
		for(Move m:moves){
			previousMoves.add(m);
			v = Math.min(v, maxValue(currentState,previousMoves,depth+1, switchPlay(play)));
			previousMoves.remove(m);
		}
		return v;
	}

	private int maxValue(GameBoard currentState,LinkedList<Move> previousMoves, int depth, char play) {
		if(depth == maxDepth)
			return previousMoves.get(previousMoves.size()-1).utlityAfterMove;
		int v = Integer.MIN_VALUE;
		LinkedList<Move> moves = action(currentState,previousMoves,play, depth);
		if(moves.size() == 0){
			return previousMoves.get(previousMoves.size()-1).utlityAfterMove;
		}
		for(Move m: moves){
			previousMoves.add(m);
			v = Math.max(v, minValue(currentState,previousMoves,depth+1, switchPlay(play)));
			previousMoves.remove(m);
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

	private LinkedList<Move> action(GameBoard currentState, LinkedList<Move> previousMoves, char play, int depth) {
		LinkedList<Move> moves = currentState.generateMoves(currentState, previousMoves, play);
		return moves;
	}
}
