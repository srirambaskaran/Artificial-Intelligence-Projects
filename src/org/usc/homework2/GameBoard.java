package org.usc.homework2;


import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
/**
 * This represents a board. This contains three {@link HashSet} of {@link Cell} type.<br/>
 * empty = contains all the empty cells in the node<br/>
 * xs = contains all the cells containing a "X"<br/>
 * os = contains all the cells containing a "O"<br/>
 * 
 * This also stores the utility and depth of the board in the game tree.<br/>
 * The toString() method prints the board as required.
 * @author Sriram
 *
 */
public class GameBoard{
	private HashSet<Cell> empty;
	private HashSet<Cell> xs;
	private HashSet<Cell> os;
	private int sumX;
	private int sumO;
	private int depth;
	private Move move;
	private char nextPlay;
	private char myPlay;
	public enum Direction {NORTH, SOUTH, EAST, WEST};
	public HashMap<String,Cell> cells;
	
	public GameBoard(){
		this.sumX = 0;
		this.sumO = 0;
		this.depth = -1;
		this.empty = new HashSet<>();
		this.xs = new HashSet<>();
		this.os = new HashSet<>();
		this.cells = new HashMap<>();
	}
	public char getMyPlay() {
		return myPlay;
	}
	public void setMyPlay(char myPlay) {
		this.myPlay = myPlay;
	}
	public char getNextPlay() {
		return nextPlay;
	}
	public void setNextPlay(char nextPlay) {
		this.nextPlay = nextPlay;
	}
	public HashSet<Cell> getEmpty() {
		return empty;
	}
	public HashSet<Cell> getXs() {
		return xs;
	}
	public HashSet<Cell> getOs() {
		return os;
	}
	public int getSumX() {
		return sumX;
	}
	public int getSumO() {
		return sumO;
	}
	public int getDepth() {
		return depth;
	}
	public Move getMove() {
		return move;
	}
	public void setMove(Move move) {
		this.move = move;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public void removeFromEmpty(Cell cell){
		this.empty.remove(cell);
	}
	public void removeFromXs(Cell cell){
		
		this.xs.remove(cell);
	}
	public void removeFromOs(Cell cell){
		this.os.remove(cell);
	}
	public void addToEmpty(Cell cell){
		this.empty.add(cell);
		this.cells.put(cell.rowIndex+","+cell.colIndex,cell);
	}
	public void addToXs(Cell cell){
		this.xs.add(cell);
		this.cells.put(cell.rowIndex+","+cell.colIndex,cell);
	}
	public void addToOs(Cell cell){
		this.os.add(cell);
		this.cells.put(cell.rowIndex+","+cell.colIndex,cell);
	}
	
	public LinkedList<Move> generateMoves(GameBoard inputBoard, LinkedList<Move> previousMoves, char nextPlay){
		if(xs.size() + os.size() + previousMoves.size() == cells.size()+1){
			return new LinkedList<Move>();
		}
		LinkedList<Move> moves = new LinkedList<>();
		HashSet<Cell> myCells = nextPlay == 'X'?xs:os;
		HashSet<Cell> opponentCells = nextPlay == 'X'?os:xs;
		HashSet<Cell> emptyCells = new HashSet<>(empty);
		HashSet<Cell> myCellsUpdated = new HashSet<>();
		HashSet<Cell> opponentCellsUpdated = new HashSet<>();
		myCellsUpdated.addAll(myCells);
		opponentCellsUpdated.addAll(opponentCells);
		Move previousMove = previousMoves.get(previousMoves.size()-1);
		for(Move m: previousMoves){
			if(m.moveType.equals("Dummy")) continue;
			if(m.moveType.equals("Stake")){
				if(m.moveBy == nextPlay)
					myCellsUpdated.add(m.cell);
				else
					opponentCellsUpdated.add(m.cell);
			}
			else{
				if(m.moveBy == nextPlay)
					myCellsUpdated.add(m.cell);
				else
					opponentCellsUpdated.add(m.cell);
				for(Cell cell: m.otherCellsUpdated){
					if(m.moveBy == nextPlay){
						opponentCellsUpdated.remove(cell);
						myCellsUpdated.add(cell);
					}
					else{
						myCellsUpdated.remove(cell);
						opponentCellsUpdated.add(cell);
					}
				}
			}
		}
				
		emptyCells.removeAll(myCellsUpdated);
		emptyCells.removeAll(opponentCellsUpdated);	
		
		
		HashSet<Cell> raidCells = new HashSet<>();
		for(Cell cell : emptyCells){
			Move stakeMove = new Move();
			stakeMove.cell = cell;
			stakeMove.moveType = "Stake";
			if(myPlay == nextPlay)
				stakeMove.utlityAfterMove = previousMove.utlityAfterMove + cell.utilityValue;
			else
				stakeMove.utlityAfterMove = previousMove.utlityAfterMove - cell.utilityValue;
			stakeMove.numCellsUpdated = 1;
			stakeMove.moveBy=nextPlay;
			moves.add(stakeMove);
		}

		for(Cell cell:myCellsUpdated){
			for(Direction direction: Direction.values()){
				Cell neighbour = cells.get(rowIndex(cell.rowIndex, direction)+","+colIndex(cell.colIndex, direction));
				if(emptyCells.contains(neighbour)){
					int conqueredPoints = 0;
					int opponentLostPoints = 0;
					Move raidMove = new Move();
					int numCellsUpdated = 0;
					boolean raid = false;
					for(Direction direction2: Direction.values()){
						Cell neighbour2 = cells.get(rowIndex(neighbour.rowIndex, direction2)+","+colIndex(neighbour.colIndex, direction2));
						if(opponentCellsUpdated.contains(neighbour2)){
							raid = true;
							conqueredPoints+=neighbour2.utilityValue;
							opponentLostPoints+=neighbour2.utilityValue;
							raidMove.addOtherCellsUpdated(neighbour2);
							numCellsUpdated++;
						}
					}
					raidMove.cell = neighbour;
					raidMove.moveType = "Raid";
					if(myPlay == nextPlay)
						raidMove.utlityAfterMove = previousMove.utlityAfterMove + neighbour.utilityValue + conqueredPoints + opponentLostPoints;
					else
						raidMove.utlityAfterMove = previousMove.utlityAfterMove - neighbour.utilityValue - conqueredPoints - opponentLostPoints;
					raidMove.numCellsUpdated = numCellsUpdated + 1;
					raidMove.moveBy = nextPlay;
					if(raid){
						moves.add(raidMove);
						raidCells.add(neighbour);
					}
				}
			}
		}
		

		return moves;
	}
	
	public boolean neigbourContainsOpposite(Cell cell, HashSet<Cell> opponentCells){
		Cell dummyEastCell = new Cell(cell.rowIndex,cell.colIndex+1);
		Cell dummyWestCell = new Cell(cell.rowIndex,cell.colIndex-1);
		Cell dummyNorthCell = new Cell(cell.rowIndex-1,cell.colIndex);
		Cell dummySouthCell = new Cell(cell.rowIndex+1,cell.colIndex);
		return opponentCells.contains(dummyEastCell) 
				|| opponentCells.contains(dummyWestCell) 
				|| opponentCells.contains(dummyNorthCell) 
				|| opponentCells.contains(dummySouthCell); 
	}

	
	public void calculateUtilitiesOfPlayer(){
		sumX = 0;
		sumO = 0;
		for(String index:cells.keySet()){
			Cell cell = cells.get(index);
			char cellValue = cell.cellValue;
			int utilityValue = cell.utilityValue;
			switch(cellValue){
			case 'X': sumX+=utilityValue;break;
			case 'O': sumO+=utilityValue;break;
			}
		}
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder();
		int numCells = empty.size() + xs.size() + os.size();
		int boardDimension = (int)Math.sqrt(numCells);
		char[][] outputMatrix = new char[boardDimension][boardDimension];
		for(Cell cell : xs){
			outputMatrix[cell.rowIndex][cell.colIndex] = 'X';
		}
		for(Cell cell : os){
			outputMatrix[cell.rowIndex][cell.colIndex] = 'O';
		}
		for(Cell cell : empty){
			outputMatrix[cell.rowIndex][cell.colIndex] = '.';
		}
		for(int i=0;i<boardDimension;i++){
			for(int j=0;j<boardDimension;j++){
				builder.append(outputMatrix[i][j]+"");
			}
			builder.append("\n");
		}
		String outputString = builder.toString();
		if(outputString.endsWith("\n")){
			outputString = outputString.substring(0,outputString.length()-1);
		}
		return outputString;
	}
	
	
	public int rowIndex(int old, Direction direction){
		switch(direction){
			case NORTH: return old-1;
			case SOUTH: return old+1;
			case EAST: 
			case WEST: 
			default: return old;
		}
	}
	
	public int colIndex(int old, Direction direction){
		switch(direction){
			case EAST: return old+1;
			case WEST: return old-1;
			case NORTH: 
			case SOUTH: 
			default: return old;
		}
	}
}

