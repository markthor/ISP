package connectFour;

import connectFour.IGameLogic.Winner;

public class UtilityTwo {
	private int columns, rows, diaLength, leftAnchorPointer, rightAnchorPointer;

	// General arrays
	private int[] colCount, rowCount, rowSndCount, leftDiaCount, rightDiaCount;

	// Arrays used to count consecutive zeroes
	private int[] colZeroCount, rowZeroCount, rowSndZeroCount, leftDiaZeroCount,
			rightDiaZeroCount;
	
	private final double oneRow = 1, twoRow = 10, threeRow = 100, fourRow = 1000;

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
						if(colCount[i] + rows-j >= 4){//there is space to get 4 in a row
							utility += value(colCount[i], 1);
						}
					}
					if(colCount[i] < 0){//2's before
						if(-colCount[i] + rows-j >= 4){//there is space to get 4 in a row
							utility += value(-colCount[i], 2);
						}
					}
					colCount[i] = 0;
					break;
				case 1: // blue coin
					if (0 <= colCount[i]) { // same value
						colCount[i]++;
						//TODO: We can check here if it is the last row instead of running through all at the end. Would that be more efficient?
					} else { //2's before
						if(-colCount[i] >= 4){//there is space to get 4 in a row
							utility += value(-colCount[i], 2);
						}
						colCount[i] = 1;
					}
					break;
				case 2: // red coin
					if (colCount[i] <= 0) { // same value
						colCount[i]--;
						//TODO: We can check here if it is the last row instead of running through all at the end. Would that be more efficient?
					} else {//1's before
						if(colCount[i] >= 4){//there is space to get 4 in a row
							utility += value(colCount[i], 1);
						}
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
						} else if (rowCount[j] < 3) { // 101 situation 010
							int need = 4 - rowZeroCount[j] - rowCount[j] - 1;
							int left = need;
							
							//Checks all possible locations
							/*if(i-1-rowCount[j] > 0){//If there is space 1 to the left
								if(gameBoard[i-1-rowCount[j]][j] == 0 || gameBoard[i-1-rowCount[j]][j] == 1){
									left--;
								}
								
								
								if(left > 0 && i-2-rowCount[j] > 0){//If there is space 2 to the left
									if(gameBoard[i-2-rowCount[j]][j] == 0 || gameBoard[i-2-rowCount[j]][j] == 1){
										left--;
									}
								}
							}*/
							
							if(left > 0 && i+1 < columns){//If there is space 2 to the left
								if(gameBoard[i+1][j] == 0 || gameBoard[i+1][j] == 1){
									left--;
								}
								
								
								if(left > 0 && i+2 < columns){//If there is space 2 to the left
									if(gameBoard[i+2][j] == 0 || gameBoard[i+2][j] == 1){
										left--;
									}
								}
							}
								
							//}
							if (left == 0) {
								utility += value(rowCount[j], 1);
							}
						}
					}
					else if (rowCount[j] < 0) { // 2 values before
						if (-rowCount[j] + rowZeroCount[j] + 1 >= 4) {
							utility += value(-rowCount[j], 2);
						} else if (-rowCount[j] < 3) { // 101 situation 010
							int need = 4 - rowZeroCount[j] - -rowCount[j] - 1;
							int left = need;
							
							//Checks all possible locations
							/*if(i-1-(-rowCount[j]) > 0){//If there is space 1 to the left
								if(gameBoard[i-1-(-rowCount[j])][j] == 0 || gameBoard[i-1-(-rowCount[j])][j] == 2){
									left--;
								}
								
								
								if(left > 0 && i-2-(-rowCount[j]) > 0){//If there is space 2 to the left
									if(gameBoard[i-2-(-rowCount[j])][j] == 0 || gameBoard[i-2-(-rowCount[j])][j] == 2){
										left--;
									}
								}
							}*/
							
							if(left > 0 && i+1 < columns){//If there is space 1 to the right
								if(gameBoard[i+1][j] == 0 || gameBoard[i+1][j] == 2){
									left--;
								}
								
								
								if(left > 0 && i+2 < columns){//If there is space 2 to the right
									if(gameBoard[i+2][j] == 0 || gameBoard[i+2][j] == 2){
										left--;
									}
								}
							}
								
							//}
							if (left == 0) {
								utility += value(-rowCount[j], 2);
							}
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
						} else if (-rowCount[j] < 3) { // 101 situation 010
							int need = 4 - rowZeroCount[j] - -rowCount[j];
							int left = need;
							
							//Checks all possible locations
							/*if(i-1-(-rowCount[j]) > 0){//If there is space 1 to the left
								if(gameBoard[i-1-(-rowCount[j])][j] == 0 || gameBoard[i-1-(-rowCount[j])][j] == 2){
									left--;
								}
								
								
								if(left > 0 && i-2-(-rowCount[j]) > 0){//If there is space 2 to the left
									if(gameBoard[i-2-(-rowCount[j])][j] == 0 || gameBoard[i-2-(-rowCount[j])][j] == 2){
										left--;
									}
								}
							}*/
							
							if(left > 0 && i+1 < columns){//If there is space 1 to the right
								if(gameBoard[i+1][j] == 0 || gameBoard[i+1][j] == 2){
									left--;
								}
								
								
								if(left > 0 && i+2 < columns){//If there is space 2 to the right
									if(gameBoard[i+2][j] == 0 || gameBoard[i+2][j] == 2){
										left--;
									}
								}
							}
								
							//}
							if (left == 0) {
								utility += value(-rowCount[j], 2);
							}
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
						} else if (rowCount[j] < 3) { // 101 situation 010, Er det nu også det? Er det ikke bare altid sådan? Bliver der talt op for 4 på stribe med 11112?
							int need = 4 - rowZeroCount[j] - rowCount[j];
							int left = need;
							
							//Checks all possible locations
							/*if(i-1-rowCount[j] > 0){//If there is space 1 to the left
								if(gameBoard[i-1-rowCount[j]][j] == 0 || gameBoard[i-1-rowCount[j]][j] == 1){
									left--;
								}
								
								
								if(left > 0 && i-2-rowCount[j] > 0){//If there is space 2 to the left
									if(gameBoard[i-2-rowCount[j]][j] == 0 || gameBoard[i-2-rowCount[j]][j] == 1){
										left--;
									}
								}
							}*/
							
							if(left > 0 && i+1 < columns){//If there is space 2 to the left
								if(gameBoard[i+1][j] == 0 || gameBoard[i+1][j] == 1){
									left--;
								}
								
								
								if(left > 0 && i+2 < columns){//If there is space 2 to the left
									if(gameBoard[i+2][j] == 0 || gameBoard[i+2][j] == 1){
										left--;
									}
								}
							}
								
							//}
							if (left == 0) {
								utility += value(rowCount[j], 1);
							}
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
							else if(i+1 < columns && j+1 < rows){//check for space
								int need = 4 - leftDiaZeroCount[j] - leftDiaCount[j] - 1;
								int left = need;
								if(gameBoard[i+1][j+1] == 0 || gameBoard[i+1][j+1] == 1){
									left--;
									if(left > 0 && i+2 < columns && j+2 < rows && gameBoard[i+2][j+2] == 0 || gameBoard[i+2][j+2] == 1){
										left--;
									}
								}
								else if(left == 0){
									utility += value(leftDiaCount[diaArrayPos], 1);
								}
							}	
						}
						else if(leftDiaCount[diaArrayPos] < 0){//2's before
							if(-leftDiaCount[diaArrayPos] + 1 + leftDiaZeroCount[diaArrayPos] >= 4){//check if eligible
								utility += value(-leftDiaCount[diaArrayPos], 2);
							}
							else if(i+1 < columns && j+1 < rows){//check for space
								int need = 4 - leftDiaZeroCount[j] - -leftDiaCount[j] - 1;
								int left = need;
								if(gameBoard[i+1][j+1] == 0 || gameBoard[i+1][j+1] == 2){
									left--;
									if(left > 0 && i+2 < columns && j+2 < rows && gameBoard[i+2][j+2] == 0 || gameBoard[i+2][j+2] == 2){
										left--;
									}
								}
								else if(left == 0){
									utility += value(-leftDiaCount[diaArrayPos], 2);
								}
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
							else if(i+1 < columns && j+1 < rows){//check for space
								int need = 4 - leftDiaZeroCount[j] - -leftDiaCount[j];
								int left = need;
								if(gameBoard[i+1][j+1] == 0 || gameBoard[i+1][j+1] == 2){
									left--;
									if(left > 0 && i+2 < columns && j+2 < rows && gameBoard[i+2][j+2] == 0 || gameBoard[i+2][j+2] == 2){
										left--;
									}
								}
								else if(left == 0){
									utility += value(-leftDiaCount[diaArrayPos], 2);
								}
							}
							leftDiaCount[diaArrayPos] = 1;
							leftDiaZeroCount[diaArrayPos] = 0;
						}
					}
					break;
				case 2:
					if(diaArrayPos >= 0 && diaArrayPos < diaLength){ //valid diagonal
						if(leftDiaCount[diaArrayPos] < 0){//2's before
							leftDiaCount[diaArrayPos]--;
						}
						else if(0 < leftDiaCount[diaArrayPos]){//1's before
							if(leftDiaCount[diaArrayPos] + 1 + leftDiaZeroCount[diaArrayPos] >= 4){//check if eligible
								utility += value(leftDiaCount[diaArrayPos], 1);
							}
							else if(i+1 < columns && j+1 < rows){//check for space
								int need = 4 - leftDiaZeroCount[j] - leftDiaCount[j] - 1;
								int left = need;
								if(gameBoard[i+1][j+1] == 0 || gameBoard[i+1][j+1] == 1){
									left--;
									if(left > 0 && i+2 < columns && j+2 < rows && gameBoard[i+2][j+2] == 0 || gameBoard[i+2][j+2] == 1){
										left--;
									}
								}
								else if(left == 0){
									utility += value(leftDiaCount[diaArrayPos], 1);
								}
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
	        		if(diaArrayPos >= 0 && diaArrayPos < diaLength) { //valid diagonal
						if(rightDiaCount[diaArrayPos] == 0){//zeroes before
							rightDiaZeroCount[diaArrayPos]++;
						}
						else if(0 < rightDiaCount[diaArrayPos]){//1's before
							if(rightDiaCount[diaArrayPos] + 1 + rightDiaZeroCount[diaArrayPos] >= 4){//check if eligible
								utility += value(rightDiaCount[diaArrayPos], 1);
							}
							else if(i+1 < columns && j-1 > 0){//check for space
								int need = 4 - rightDiaZeroCount[diaArrayPos] - rightDiaCount[diaArrayPos] - 1;
								int left = need;
								if(gameBoard[i+1][j-1] == 0 || gameBoard[i+1][j-1] == 1){
									left--;
									if(left > 0 && i+2 < columns && j-2 > 0 && gameBoard[i+2][j-2] == 0 || gameBoard[i+2][j-2] == 1){
										left--;
									}
								}
								else if(left == 0){
									utility += value(rightDiaCount[diaArrayPos], 1);
								}
							}	
						}
						else if(rightDiaCount[diaArrayPos] < 0){//2's before
							if(-rightDiaCount[diaArrayPos] + 1 + rightDiaZeroCount[diaArrayPos] >= 4){//check if eligible
								utility += value(-rightDiaCount[diaArrayPos], 2);
							}
							else if(i+1 < columns && j-1 > 0){//check for space
								int need = 4 - rightDiaZeroCount[j] - -rightDiaCount[j] - 1;
								int left = need;
								if(gameBoard[i+1][j-1] == 0 || gameBoard[i+1][j-1] == 2){
									left--;
									if(left > 0 && i+2 < columns && j-2 > 0 && gameBoard[i+2][j-2] == 0 || gameBoard[i+2][j-2] == 2){
										left--;
									}
								}
								else if(left == 0){
									utility += value(-rightDiaCount[diaArrayPos], 2);
								}
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
							else if(i+1 < columns && j-1 > 0){//check for space
								int need = 4 - rightDiaZeroCount[j] - -rightDiaCount[j];
								int left = need;
								if(gameBoard[i+1][j-1] == 0 || gameBoard[i+1][j-1] == 2){
									left--;
									if(left > 0 && i+2 < columns && j-2 > 0 && gameBoard[i+2][j-2] == 0 || gameBoard[i+2][j-2] == 2){
										left--;
									}
								}
								else if(left == 0){
									utility += value(-rightDiaCount[diaArrayPos], 2);
								}
							}
							rightDiaCount[diaArrayPos] = 1;
							rightDiaZeroCount[diaArrayPos] = 0;
						}
					}
					break;
				case 2:
					if(diaArrayPos >= 0 && diaArrayPos < diaLength){ //valid diagonal
						if(rightDiaCount[diaArrayPos] < 0){//2's before
							rightDiaCount[diaArrayPos]--;
						}
						else if(0 < rightDiaCount[diaArrayPos]){//1's before
							if(rightDiaCount[diaArrayPos] + 1 + rightDiaZeroCount[diaArrayPos] >= 4){//check if eligible
								utility += value(rightDiaCount[diaArrayPos], 1);
							}
							else if(i+1 < columns && j-1 > 0){//check for space
								int need = 4 - rightDiaZeroCount[j] - rightDiaCount[j] - 1;
								int left = need;
								if(gameBoard[i+1][j-1] == 0 || gameBoard[i+1][j-1] == 1){
									left--;
									if(left > 0 && i+2 < columns && j-2 > 0 && gameBoard[i+2][j-2] == 0 || gameBoard[i+2][j-2] == 1){
										left--;
									}
								}
								else if(left == 0){
									utility += value(rightDiaCount[diaArrayPos], 1);
								}
							}	
							rightDiaCount[diaArrayPos] = -1;
							rightDiaZeroCount[diaArrayPos] = 0;
						}
					}
					break;
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