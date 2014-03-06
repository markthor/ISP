package connectFour;

import connectFour.IGameLogic.Winner;

public class UtilityTwo {
	private int columns, rows, diaLength, leftAnchorPointer, rightAnchorPointer;

	// General arrays
	private int[] colCount, rowCount, rowSndCount, leftDiaCount, rightDiaCount;

	// Arrays used to count consecutive zeroes
	private int[] colZeroCount, rowZeroCount, rowSndZeroCount, leftDiaZeroCount,
			rightDiaZeroCount;
	
	private final double oneRow = 5, twoRow = 30, threeRow = 75, fourRow = 1000;

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

		for (int i = 0; i < columns; i++) { // husk at tjekke at de sidste
											// rækker giver mening at lægge i
			for (int j = 0; j < rows; j++) {

				// Columns
				/*
				 * if(colCount[i] == 0 && colZeroCount[i] == 0){
				 * switch(gameBoard[i][j]){ case 0: colZeroCount[i]++;
				 * colCount[i] = 0; break; case 1: colCount[i] = 1; break; case
				 * 2: colCount[i] = -1; break; } }
				 */
				// else if(colCount[i] != 0){

				// Columns
				switch (gameBoard[i][j]) {
				case 0:
					colZeroCount[i]++;
					break;
				case 1: // blue coin
					if (0 <= colCount[i]) { // same value
						colCount[i]++;
					} else {
						colCount[i] = 1;
					}
					break;
				case 2: // red coin
					if (colCount[i] <= 0) { // same value
						colCount[i]--;
					} else {
						colCount[i] = -1;
					}
					break;
				}

				// rows
				switch (gameBoard[i][j]) {
				case 0: // TODO: NOT DONE
					if (rowCount[j] == 0) { // consecutive zeroes
						rowZeroCount[j]++;
					} else if (0 < rowCount[j]) { // 1 values before
						if (rowCount[j] + rowZeroCount[j] + 1 >= 4) {
							utility += value(rowCount[j], 1);
							rowCount[j] = 0;
						} else if (rowCount[j] < 3) { // 101 situation 010
							int need = 4 - rowZeroCount[j] - rowCount[j] - 1;
							int left = need;
							int c = 0;
							while (c < need && i + need < columns) {
								if (gameBoard[i + left][j] == 1
										|| gameBoard[i + left][j] == 0) {
									left--;
								} else {
									break;
								}
								c++;
							}
							if (left == 0) {
								utility += value(rowCount[j], 1);
							}
						}
					}
					rowCount[j] = 0;
					break;
					
				case 1: rowCount[j]++;
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
}