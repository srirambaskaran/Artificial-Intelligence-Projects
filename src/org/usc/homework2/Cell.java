package org.usc.homework2;

/**
 * Represents one cell in the board
 * @author Sriram
 *
 */
public class Cell implements Cloneable{
	public int row;
	public char col;
	public int rowIndex;
	public int colIndex;
	public int utilityValue;
	public char cellValue;
	public Cell(int rowIndex,int colIndex){
		this.rowIndex = rowIndex;
		this.colIndex = colIndex;
		this.row = rowIndex+1;
		this.col = (char)(colIndex + 65);
	}
	public String toFormattedString(){
		return col+""+row+"";
	}
	@Override
	public String toString() {
		return col+""+row+" - "+cellValue+" ("+rowIndex+","+colIndex+") = "+utilityValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + colIndex;
		result = prime * result + rowIndex;
		result = prime * result + cellValue;
		result = prime * result + utilityValue;
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
		Cell other = (Cell) obj;
		if (colIndex != other.colIndex)
			return false;
		if (rowIndex != other.rowIndex)
			return false;
		if (cellValue != other.cellValue)
			return false;
		return true;
	}
	@Override
	protected Cell clone(){
		Cell newCell = new Cell(this.rowIndex, this.colIndex);
		newCell.utilityValue = this.utilityValue;
		newCell.cellValue = this.cellValue;
		return newCell;
	}
}
