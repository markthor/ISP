package connectFour;

import connectFour.IGameLogic.Winner;

public class UtilityTwo implements Utility {
	private int columns, rows, diaLength, leftAnchorPointer, rightAnchorPointer;

	// General arrays
	private int[] colCount, rowCount, rowSndCount, leftDiaCount, rightDiaCount;

	// Arrays used to count consecutive zeroes
	private int[] colZeroCount, rowZeroCount, rowSndZeroCount, leftDiaZeroCount,
			rightDiaZeroCount;
	
	private final double oneRow = 1, twoRow = 10, threeRow = 25, fourRow = 1000;

	public UtilityTwo(int columns, int rows) {
		this.columns = columns;
		this.rows = rows;
		diaLength = columns - 3 + rows - 3 - 1;
		leftAnchorPointer = rows - 4;
		rightAnchorPointer = columns - 4;
	}

	public double utility(int[][] gameBoard) {
		colCount = new int[columns];
		rowCount = new int[rows];
		rowSndCount = new int[rows];
		leftDiaCount = new int[diaLength];
		rightDiaCount = new int[diaLength];
		int diaArrayPos;

		double utility = 0;

		// Zero stuff
		colZeroCount = new int[columns];
		rowZeroCount = new int[rows];
		rowSndZeroCount = new int[columns];
		leftDiaZeroCount = new int[diaLength];
		rightDiaZeroCount = new int[diaLength];

		for (int i = 0; i < columns; i++) { // husk at tjekke at de sidste rækker giver mening at lægge i
			for (int j = 0; j < rows; j++) {
				// COLUMNS
				
				switch (gameBoard[i][j]) {
				case 0:
					colZeroCount[i]++;
					
					if(colCount[i] > 0){//1's before
						utility += columnCounter(colCount[i], j, 1);
					}
					if(colCount[i] < 0){//2's before
						utility += columnCounter(-colCount[i], j, 2);
					}
					colCount[i] = 0;
					break;
				case 1: // blue coin
					if (0 <= colCount[i]) { // same value
						colCount[i]++;
						//TODO: We can check here if it is the last row instead of running through all at the end. Would that be more efficient?
					} else { //2's before
						utility += columnCounter(-colCount[i], j, 2);
						colCount[i] = 1;
					}
					break;
				case 2: // red coin
					if (colCount[i] <= 0) { // same value
						colCount[i]--;
						//TODO: We can check here if it is the last row instead of running through all at the end. Would that be more efficient? CAN ALSO BE DONE IN OTHER PLACES
					} else {//1's before
						utility += columnCounter(colCount[i], j, 1);
						colCount[i] = -1;
					}
					break;
				}
				
				
				
				
				
				// ROWS
				switch (gameBoard[i][j]) {
				case 0: // TODO: NOT DONE
					if (rowCount[j] == 0) { // consecutive zeroes
						rowZeroCount[j]++;
					} 
					else if (0 < rowCount[j]) { // 1 values before
						if (rowCount[j] + rowZeroCount[j] + 1 >= 4) {
							utility += value(rowCount[j], 1);
						}
						else{
							utility += rowChecker(rowCount[j], rowZeroCount[j], 1, i, j, gameBoard);
						}
					}
					else if (rowCount[j] < 0) { // 2 values before
						if (-rowCount[j] + rowZeroCount[j] + 1 >= 4) {
							utility += value(-rowCount[j], 2);
						}
						else{
							utility += rowChecker(-rowCount[j], rowZeroCount[j], 2, i, j, gameBoard);
						}
					}
					rowCount[j] = 0;
					rowZeroCount[j] = 1;
					break;
					
				case 1: 
					if(rowCount[j] >= 0){
						rowCount[j]++;
						//TODO: We can check here if it is the last column instead of running through all at the end. Would that be more efficient?
					}
					else if(rowCount[j] < 0){//2's before
						if (-rowCount[j] + rowZeroCount[j] >= 4) {
							utility += value(-rowCount[j], 2);
						}
						else{
							utility += rowChecker(-rowCount[j], rowZeroCount[j], 2, i, j, gameBoard);
						}
						rowCount[j] = 1;
						rowZeroCount[j] = 0;
					}
					break;
				case 2:
					if(rowCount[j] <= 0){//2's before
						rowCount[j]--;
						//TODO: We can check here if it is the last column instead of running through all at the end. Would that be more efficient?
					}
					else if(0 < rowCount[j]){//1's before
						if (rowCount[j] + rowZeroCount[j] >= 4) {
							utility += value(rowCount[j], 1);
						}
						else{
							utility += rowChecker(rowCount[j], rowZeroCount[j], 1, i, j, gameBoard);
						}
						rowCount[j] = -1;
						rowZeroCount[j] = 0;
					}
					break;
				}
				
				
				
				//LEFTDIAGONAL
				diaArrayPos = leftAnchorPointer-j+i;
				switch (gameBoard[i][j]) {
				case 0:
					if(diaArrayPos >= 0 && diaArrayPos < diaLength){ //valid diagonal
						if(leftDiaCount[diaArrayPos] == 0){//zeroes before
							leftDiaZeroCount[diaArrayPos]++;
						}
						else if(0 < leftDiaCount[diaArrayPos]){//1's before
							if(leftDiaCount[diaArrayPos] + 1 + leftDiaZeroCount[diaArrayPos] >= 4){//check if eligible
								utility += value(leftDiaCount[diaArrayPos], 1);
							}
							else
							{
							 utility += leftDiagonalChecker(leftDiaCount[diaArrayPos], leftDiaZeroCount[diaArrayPos], 1, i, j, gameBoard);
							}
						}
						else if(leftDiaCount[diaArrayPos] < 0){//2's before
							if(-leftDiaCount[diaArrayPos] + 1 + leftDiaZeroCount[diaArrayPos] >= 4){//check if eligible
								utility += value(-leftDiaCount[diaArrayPos], 2);
							}
							else
							{
							 utility += leftDiagonalChecker(-leftDiaCount[diaArrayPos], leftDiaZeroCount[diaArrayPos], 2, i, j, gameBoard);
							}	
						}
						leftDiaCount[diaArrayPos] = 0;
						leftDiaZeroCount[diaArrayPos] = 1;
					break;
					}
				case 1:
					if(diaArrayPos >= 0 && diaArrayPos < diaLength){ //valid diagonal
						if(leftDiaCount[diaArrayPos] >= 0){//1's before
							leftDiaCount[diaArrayPos]++;
						}
						else if(leftDiaCount[diaArrayPos] < 0){//2's before
							if(-leftDiaCount[diaArrayPos] + leftDiaZeroCount[diaArrayPos] >= 4){//check if eligible
								utility += value(-leftDiaCount[diaArrayPos], 2);
							}
							else
							{
							 utility += leftDiagonalChecker(-leftDiaCount[diaArrayPos], leftDiaZeroCount[diaArrayPos], 2, i, j, gameBoard);
							}	
							leftDiaCount[diaArrayPos] = 1;
							leftDiaZeroCount[diaArrayPos] = 0;
						}
					}
					break;
				case 2:
					if(diaArrayPos >= 0 && diaArrayPos < diaLength){ //valid diagonal
						if(leftDiaCount[diaArrayPos] <= 0){//2's before
							leftDiaCount[diaArrayPos]--;
						}
						else if(0 < leftDiaCount[diaArrayPos]){//1's before
							if(leftDiaCount[diaArrayPos] + 1 + leftDiaZeroCount[diaArrayPos] >= 4){//check if eligible
								utility += value(leftDiaCount[diaArrayPos], 1);
							}
							else{
								utility += leftDiagonalChecker(leftDiaCount[diaArrayPos], leftDiaZeroCount[diaArrayPos], 1, i, j, gameBoard);
							}
							leftDiaCount[diaArrayPos] = -1;
							leftDiaZeroCount[diaArrayPos] = 0;
						}
					}
					break;
        		}
				
				
				
				
				//RIGHTDIAGONAL
				diaArrayPos = rightAnchorPointer-((columns-1)-i)+j;
				switch (gameBoard[i][j]) {
				case 0:
					if(diaArrayPos >= 0 && diaArrayPos < diaLength) { //valid diagonal, move before switch
						if(rightDiaCount[diaArrayPos] == 0){//zeroes before
							rightDiaZeroCount[diaArrayPos]++;
						}
						else if(0 < rightDiaCount[diaArrayPos]){//1's before
							if(rightDiaCount[diaArrayPos] + 1 + rightDiaZeroCount[diaArrayPos] >= 4){//check if eligible
								utility += value(rightDiaCount[diaArrayPos], 1);
							}
							else 
							{
								utility += rightDiagonalChecker(rightDiaCount[diaArrayPos], rightDiaCount[diaArrayPos], 1, i, j, gameBoard);
							}	
						}
						else if(rightDiaCount[diaArrayPos] < 0){//2's before
							if(-rightDiaCount[diaArrayPos] + 1 + rightDiaZeroCount[diaArrayPos] >= 4){//check if eligible
								utility += value(-rightDiaCount[diaArrayPos], 2);
							}
							else 
							{
								utility += rightDiagonalChecker(-rightDiaCount[diaArrayPos], rightDiaCount[diaArrayPos], 2, i, j, gameBoard);
							}	
						}
						rightDiaCount[diaArrayPos] = 0;
						rightDiaZeroCount[diaArrayPos] = 1;
					break;
					}
				case 1:
					if(diaArrayPos >= 0 && diaArrayPos < diaLength){ //valid diagonal
						if(rightDiaCount[diaArrayPos] >= 0){//1's before
							rightDiaCount[diaArrayPos]++;
						}
						else if(rightDiaCount[diaArrayPos] < 0){//2's before
							if(-rightDiaCount[diaArrayPos] + rightDiaZeroCount[diaArrayPos] >= 4){//check if eligible
								utility += value(-rightDiaCount[diaArrayPos], 2);
							}
							else 
							{
								utility += rightDiagonalChecker(-rightDiaCount[diaArrayPos], rightDiaCount[diaArrayPos], 2, i, j, gameBoard);
							}
							rightDiaCount[diaArrayPos] = 1;
							rightDiaZeroCount[diaArrayPos] = 0;
						}
					}
					break;
				case 2:
					if(diaArrayPos >= 0 && diaArrayPos < diaLength){ //valid diagonal
						if(rightDiaCount[diaArrayPos] <= 0){//2's before
							rightDiaCount[diaArrayPos]--;
						}
						else if(0 < rightDiaCount[diaArrayPos]){//1's before
							if(rightDiaCount[diaArrayPos] + 1 + rightDiaZeroCount[diaArrayPos] >= 4){//check if eligible
								utility += value(rightDiaCount[diaArrayPos], 1);
							}
							else 
							{
								utility += rightDiagonalChecker(rightDiaCount[diaArrayPos], rightDiaCount[diaArrayPos], 1, i, j, gameBoard);
							}	
							rightDiaCount[diaArrayPos] = -1;
							rightDiaZeroCount[diaArrayPos] = 0;
						}
					}
					break;
				}
			}
		}
		
		for(int i = 0; i < columns; i++){
			if(colZeroCount[i] == 0){
				if(4 <= colCount[i]){
					utility += value(4,1);
				}
				else if(colCount[i] <= -4){
					utility += value(4,2);
				}
			}
		}
		for(int i = 0; i < rows; i ++){
			if(rowZeroCount[i] == 0){
				if(4 <= rowCount[i]){
					utility += value(4,1);
				}
				else if(rowCount[i] <= -4){
					utility += value(4,2);
				}
			}
		}
		for(int i = 0; i < diaLength; i++){
			if(leftDiaZeroCount[i] == 0){
				if(4 <= leftDiaCount[i]){
					utility += value(4,1);
				}
				else if(leftDiaCount[i] <= -4){
					utility += value(4,2);
				}
			}
			if(rightDiaZeroCount[i] == 0){
				if(4 <= rightDiaCount[i]){
					utility += value(4,1);
				}
				else if(rightDiaCount[i] <= -4){
					utility += value(4,2);
				}
			}
		}
		
		return utility;
	}
	
	
	private double value(int consecutive, int playerID){
		switch (consecutive){
			case 1: if(playerID == 1) { return oneRow;} else { return -oneRow; } 
			case 2: if(playerID == 1) { return twoRow;} else { return -twoRow; } 
			case 3: if(playerID == 1) { return threeRow;} else { return -threeRow; } 
			default: if(playerID == 1) { return fourRow;} else { return -fourRow; } 
		}
	}
	
	//colC = the positive colCount value.
	private double columnCounter(int colC, int row, int playerID)
	{
		double util = 0d;
		if(colC > 0){//1's before
			if(colC + rows-row >= 4){//there is space to get 4 in a row
				util= value(colC, playerID);
			}
		}
		return util;
	}
	
	
	//rowC = the positive rowCount value.
	private double rowChecker(int rowC, int rowZeroC, int playerID, int column, int row, int[][] gameBoard)
	{
		int need = 4 - rowZeroC - rowC - 1;
		int left = need;
		
		//Checks all possible locations
		if(left > 0 && column+1 < columns){//If there is space 2 to the left
			if(gameBoard[column+1][row] == 0 || gameBoard[column+1][row] == 1){
				left--;
			}
			
			if(left > 0 && column+2 < columns){//If there is space 2 to the left
				if(gameBoard[column+2][row] == 0 || gameBoard[column+2][row] == 1){
					left--;
				}
			}
		}
		if (left == 0) {
			return value(rowC, playerID);
		}
		return 0d;
	}
	
	//leftDiaC = the positive leftDiaCount
	private double leftDiagonalChecker(int leftDiaC, int leftZeroDiaC, int playerID, int column, int row, int[][] gameBoard){
		if(column+1 < columns && row+1 < rows){//check for space
			int need = 4 - leftZeroDiaC - leftDiaC - 1;
			int left = need;
			if(gameBoard[column+1][row+1] == 0 || gameBoard[column+1][row+1] == 1){
				left--;
				if(left > 0 && column+2 < columns && row+2 < rows && (gameBoard[column+2][row+2] == 0 || gameBoard[column+2][row+2] == 1)){
					left--;
				}
			}
			else if(left == 0){
				return value(leftDiaC, playerID);
			}
		}
		return 0d;
	}
	
	//rightDiaC = the positive tightDiaCount
	private double rightDiagonalChecker(int rightDiaC, int rightZeroDiaC, int playerID, int column, int row, int[][] gameBoard){
		if(column+1 < columns && row-1 > 0){//check for space
			int need = 4 - rightZeroDiaC - rightDiaC - 1;
			int left = need;
			if(gameBoard[column+1][row-1] == 0 || gameBoard[column+1][row-1] == 1){
				left--;
				if(left > 0 && column+2 < columns && row-2 > 0 && (gameBoard[column+2][row-2] == 0 || gameBoard[column+2][row-2] == 1)){
					left--;
				}
			}
			else if(left == 0){
				return value(rightDiaC, playerID);
			}
		}
		return 0d;
	}
}