package org.usc.homework2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
public class GameBoard implements Cloneable {
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
	
	public void addAllToEmpty(HashSet<Cell> cells){
		for(Cell cell:cells)
			addToEmpty(cell.clone());
	}
	public void addAllToXs(HashSet<Cell> cells){
		for(Cell cell:cells)
			addToXs(cell.clone());
	}
	public void addAllToOs(HashSet<Cell> cells){
		for(Cell cell:cells)
			addToOs(cell.clone());
	}
	
	public ArrayList<GameBoard> generateNextMoves(int depth){
		HashSet<Cell> myCells = nextPlay == 'X'?xs:os;
		HashSet<Cell> raidCells = new HashSet<>();
		for(Cell cell:myCells){
			Cell eastCell = neighbour(cell, Direction.EAST);
			Cell westCell = neighbour(cell, Direction.WEST);
			Cell northCell = neighbour(cell, Direction.NORTH);
			Cell southCell = neighbour(cell, Direction.SOUTH);
			
			if(eastCell != null && eastCell.cellValue == '.' && neigbourContainsOpposite(eastCell))
				raidCells.add(eastCell);
			if(westCell != null && westCell.cellValue == '.' && neigbourContainsOpposite(westCell))
				raidCells.add(westCell);
			if(northCell != null && northCell.cellValue == '.' && neigbourContainsOpposite(northCell))
				raidCells.add(northCell);
			if(southCell != null && southCell.cellValue == '.' && neigbourContainsOpposite(southCell))
				raidCells.add(southCell);
		}

		HashSet<Cell> stakeCells = new HashSet<>();
		for(Cell cell : empty){
			if(!raidCells.contains(cell)){
				stakeCells.add(cell);
			}
		}
		
		ArrayList<GameBoard> neighbours = new ArrayList<>();
		for(Cell raidCell: raidCells){
			Cell newRaidCell = raidCell.clone();
			
			GameBoard neighbour = this.clone();
			neighbour.removeFromEmpty(newRaidCell);
			newRaidCell.cellValue = this.nextPlay;
			if(this.nextPlay == 'X')
				neighbour.addToXs(newRaidCell); 
			else
				neighbour.addToOs(newRaidCell);
			
			raidNeighbours(neighbour, newRaidCell);
			neighbour.setDepth(depth);
			neighbour.calculateUtilitiesOfPlayer();
			neighbour.nextPlay = this.nextPlay == 'X'?'O':'X';
			neighbour.getMove().cell = newRaidCell;
			neighbour.getMove().moveType = "Raid";
			
			neighbours.add(neighbour);
		}
		for(Cell stakeCell: stakeCells){
			Cell newStakeCell = stakeCell.clone();
			
			GameBoard neighbour = this.clone();
			neighbour.removeFromEmpty(newStakeCell);
			newStakeCell.cellValue = this.nextPlay;
			if(this.nextPlay == 'X')
				neighbour.addToXs(newStakeCell); 
			else
				neighbour.addToOs(newStakeCell);
			neighbour.setDepth(depth);
			neighbour.calculateUtilitiesOfPlayer();
			neighbour.nextPlay = this.nextPlay == 'X'?'O':'X';
			neighbour.getMove().cell = newStakeCell;
			neighbour.getMove().moveType = "Stake";
			
			neighbours.add(neighbour);
		}
		return neighbours;
	}
	private boolean neigbourContainsOpposite(Cell cell) {
		Cell eastCell = neighbour(cell, Direction.EAST);
		Cell westCell = neighbour(cell, Direction.WEST);
		Cell northCell = neighbour(cell, Direction.NORTH);
		Cell southCell = neighbour(cell, Direction.SOUTH);
		if(eastCell != null && eastCell.cellValue != '.' && eastCell.cellValue != nextPlay)
			return true;
		if(westCell != null && westCell.cellValue != '.' && westCell.cellValue != nextPlay)
			return true;
		if(northCell != null && northCell.cellValue != '.' && northCell.cellValue != nextPlay)
			return true;
		if(southCell != null && southCell.cellValue != '.' && southCell.cellValue != nextPlay)
			return true;
		return false;
	}
	private void raidNeighbours(GameBoard neighbour, Cell newRaidCell) {
		char raidPlay = newRaidCell.cellValue;
		Cell eastCell = neighbour.neighbour(newRaidCell, Direction.EAST);
		Cell westCell = neighbour.neighbour(newRaidCell, Direction.WEST);
		Cell northCell = neighbour.neighbour(newRaidCell, Direction.NORTH);
		Cell southCell = neighbour.neighbour(newRaidCell, Direction.SOUTH);
		
		if(eastCell != null && eastCell.cellValue != '.' && eastCell.cellValue != raidPlay)
			eastCell.cellValue = raidPlay;
		if(westCell != null && westCell.cellValue != '.' && westCell.cellValue != raidPlay)
			westCell.cellValue = raidPlay;
		if(northCell != null && northCell.cellValue != '.' && northCell.cellValue != raidPlay)
			northCell.cellValue =raidPlay;
		if(southCell != null && southCell.cellValue != '.' && southCell.cellValue != raidPlay)
			southCell.cellValue = raidPlay;
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
	
	public Cell neighbour(Cell cell, Direction direction){
		int i=cell.rowIndex;
		int j=cell.colIndex;
		switch(direction){
			case NORTH: i--; break;
			case SOUTH: i++; break;
			case EAST: j++; break;
			case WEST: j--; break;
		}
		Cell neighbouringCell = cells.containsKey(i+","+j)?cells.get(i+","+j):null;
		return neighbouringCell;
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder();
		int numCells = empty.size() + xs.size() + os.size();
		int boardDimension = (int)Math.sqrt(numCells);
		char[][] outputMatrix = new char[boardDimension][boardDimension];
		for(String cellIndices:cells.keySet()){
			String[] cellIndicesToken = cellIndices.split(",");
			int i = Integer.parseInt(cellIndicesToken[0]);
			int j = Integer.parseInt(cellIndicesToken[1]);
			outputMatrix[i][j] = cells.get(cellIndices).cellValue;
		}
		for(int i=0;i<boardDimension;i++){
			for(int j=0;j<boardDimension;j++){
				builder.append(outputMatrix[i][j]+"");
			}
			builder.append("\n");
		}
		return builder.toString();
	}
	
	public GameBoard clone(){
		GameBoard newBoard = new GameBoard();
		newBoard.addAllToEmpty(this.empty);
		newBoard.addAllToXs(this.xs);
		newBoard.addAllToOs(this.os);
		newBoard.sumX = this.sumX;
		newBoard.sumO = this.sumO;
		newBoard.depth = this.depth;
		newBoard.move = new Move();
		move.cell = this.move.cell.clone();
		move.moveType = this.move.moveType;
		newBoard.nextPlay = this.nextPlay;
		newBoard.cells = new HashMap<>(this.cells);
		newBoard.myPlay = this.myPlay;
		return newBoard;
	}
}
