package org.usc.homework2;


import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class Negascout {
	private char yourPlay;
	private GameBoard currentState;
	private int maxDepth;
//	private double timeRemaining;
	
	public Negascout(GameBoard currentState, char yourPlay, double timeRemaining){
		this.currentState = currentState;
		this.yourPlay = yourPlay;
		this.maxDepth = findDepth(currentState.getEmpty().size());
//		this.timeRemaining = timeRemaining;
		
	}
	
	private int findDepth(int size) {
//		return size/10 - (int)timeRemaining;
		return 8;
	}

	public Move negascoutSearch(){
		int depth = 0;
		Move dummyMove = new Move();
		dummyMove.moveType="Dummy";
		dummyMove.utlityAfterMove = getUtility(currentState);
		LinkedList<Move> initialMoves = new LinkedList<>();
		initialMoves.add(dummyMove);
		LinkedList<Move> possibleMoves = action(currentState, initialMoves , yourPlay, 0);
		int v = Integer.MIN_VALUE;
		Move chosen = null;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		for(Move m : possibleMoves){
			initialMoves.add(m);
			int utilityFromAction = pvs(currentState,initialMoves,depth+1,switchPlay(yourPlay), alpha, beta);
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
		System.out.println("Depth: "+maxDepth);
		return chosen;
	}
	
	public int pvs(GameBoard currentState, LinkedList<Move> previousMoves, int depth, char play, int alpha, int beta){
		if(depth >= maxDepth)
			return previousMoves.get(previousMoves.size()-1).utlityAfterMove;
		int v = Integer.MIN_VALUE;
		LinkedList<Move> moves =action(currentState,previousMoves,play, depth); 
		for(Move m:moves){
			previousMoves.add(m);
			if(moves.indexOf(m) !=0){
				v = - pvs(currentState,previousMoves, depth+1,switchPlay(play),-alpha-1,-alpha);
				if(alpha < v && v < beta){
					v = - pvs(currentState,previousMoves, depth+1,switchPlay(play),beta, v);
				}
			}else{
				v = - pvs(currentState,previousMoves, depth+1,switchPlay(play),-beta,-alpha);
			}
			
			alpha = Math.max(alpha, v);
			previousMoves.remove(m);
			if(alpha>=beta) break;
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
		Collections.sort(moves,new Comparator<Move>(){
			public int compare(Move m1, Move m2){
				if(!m1.moveType.equals(m2.moveType) && m1.moveType.equals("Stake"))
					return -1;
				else if(!m1.moveType.equals(m2.moveType) && m1.moveType.equals("Raid")) 
					return 1;
				else if(m1.utlityAfterMove > m2.utlityAfterMove)
					return -1;
				else if(m1.utlityAfterMove < m2.utlityAfterMove)
					return 1;
				else{
					if(m1.cell.rowIndex < m2.cell.rowIndex)
						return -1;
					else if(m1.cell.rowIndex > m2.cell.rowIndex)
						return 1;
					else{
						if(m1.cell.colIndex < m2.cell.colIndex)
							return -1;
						else return 1;
						
					}
				}
			}
		});
		return moves;
	}
	private char switchPlay(char yourPlay){
		return yourPlay == 'X'?'O':'X';
	}
}
