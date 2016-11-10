package org.usc.homework2;


import java.util.HashSet;

public class Move {
	Cell cell;
	String moveType;
	int utlityAfterMove;
	HashSet<Cell> otherCellsUpdated;
	int numCellsUpdated;
	char moveBy;
	public Move(){
		this.otherCellsUpdated = new HashSet<>();
	}
	public void addOtherCellsUpdated(Cell cell){
		this.otherCellsUpdated.add(cell);
		this.numCellsUpdated++;
	}
	@Override
	public String toString() {
		return "["+cell.toFormattedString()+" "+moveType+"("+moveBy+") - ("+utlityAfterMove+")]";
	}
}
