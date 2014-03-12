package connectFour;

import connectFour.IGameLogic.Winner;

public class UtilityTwo implements IUtility {
	private int columns, rows, diaLength, leftAnchorPointer, rightAnchorPointer;

	// General arrays
	private int[] colCount, rowCount, rowSndCount, leftDiaCount, rightDiaCount;

	// Arrays used to count consecutive zeroes
	private int[] colZeroCount, rowZeroCount, rowSndZeroCount, leftDiaZeroCount, rightDiaZeroCount;

	private final double oneRow = 1, twoRow = 10, threeRow = 35, fourRow = 1000;

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
				int cC = colCount[i];
				int rC = rowCount[j];
				int rZC = rowZeroCount[j];

				// COLUMNS
				switch (gameBoard[i][j]) {
				case 0:
					colZeroCount[i]++;

					if (cC > 0) {// 1's before
						utility += columnCounter(cC, j, 1);
					}
					if (colCount[i] < 0) {// 2's before
						utility += columnCounter(-cC, j, 2);
					}
					colCount[i] = 0;
					break;
				case 1: // blue coin
					if (0 <= cC) { // same value
						colCount[i]++; // TODO: We can check here if it is the last row instead of running through all at the end. Would that be more
										// efficient?
					} else { // 2's before
						utility += columnCounter(-cC, j, 2);
						colCount[i] = 1;
					}
					break;
				case 2: // red coin
					if (cC <= 0) { // same value
						colCount[i]--; // TODO: We can check here if it is the last row instead of running through all at the end. Would that be more
										// efficient? CAN ALSO BE DONE IN OTHER PLACES
					} else {// 1's before
						utility += columnCounter(cC, j, 1);
						colCount[i] = -1;
					}
					break;
				}

				// ROWS
				switch (gameBoard[i][j]) {
				case 0: // TODO: NOT DONE
					if (rC == 0) { // consecutive zeroes
						rowZeroCount[j]++;
					} else if (0 < rC) { // 1 values before
						if (rC + rZC + 1 >= 4) {
							utility += value(rC, 1);
						} else {
							utility += rowChecker(rC, rZC, 1, i, j, gameBoard);
						}
					} else if (rC < 0) { // 2 values before
						if (-rC + rZC + 1 >= 4) {
							utility += value(-rC, 2);
						} else {
							utility += rowChecker(-rC, rZC, 2, i, j, gameBoard);
						}
					}
					rowCount[j] = 0;
					rowZeroCount[j] = 1;
					break;

				case 1:
					if (rC >= 0) {
						rowCount[j]++; // TODO: We can check here if it is the last column instead of running through all at the end. Would that be
										// more efficient?
					} else if (rC < 0) {// 2's before
						if (-rC + rZC >= 4) {
							utility += value(-rC, 2);
						} else {
							utility += rowChecker(-rC, rZC, 2, i, j, gameBoard);
						}
						rowCount[j] = 1;
						rowZeroCount[j] = 0;
					}
					break;
				case 2:
					if (rC <= 0) {// 2's before
						rowCount[j]--; // TODO: We can check here if it is the last column instead of running through all at the end. Would that be
										// more efficient?
					} else if (0 < rC) {// 1's before
						if (rC + rZC >= 4) {
							utility += value(rC, 1);
						} else {
							utility += rowChecker(rC, rZC, 1, i, j, gameBoard);
						}
						rowCount[j] = -1;
						rowZeroCount[j] = 0;
					}
					break;
				}

				// LEFTDIAGONAL
				diaArrayPos = leftAnchorPointer - j + i;
				if (diaArrayPos >= 0 && diaArrayPos < diaLength) {
					int lDC = leftDiaCount[diaArrayPos];
					int lZDC = leftDiaZeroCount[diaArrayPos];
					switch (gameBoard[i][j]) {
					case 0:
						// valid diagonal //TODO: Fix it
						if (lDC == 0) {// zeroes before
							leftDiaZeroCount[diaArrayPos]++;
						} else if (0 < lDC) {// 1's before
							if (lDC + 1 + lZDC >= 4) {// check if eligible
								utility += value(lDC, 1);
							} else {
								utility += leftDiagonalChecker(lDC, lZDC, 1, i, j, gameBoard);
							}
						} else if (lDC < 0) {// 2's before
							if (-lDC + 1 + lZDC >= 4) {// check if eligible
								utility += value(-lDC, 2);
							} else {
								utility += leftDiagonalChecker(-lDC, lZDC, 2, i, j, gameBoard);
							}
						}
						leftDiaCount[diaArrayPos] = 0;
						leftDiaZeroCount[diaArrayPos] = 1;
						break;

					case 1:
						if (lDC >= 0) {// 1's before
							leftDiaCount[diaArrayPos]++;
						} else if (lDC < 0) {// 2's before
							if (-lDC + lZDC >= 4) {// check if eligible
								utility += value(-lDC, 2);
							} else {
								utility += leftDiagonalChecker(-lDC, lZDC, 2, i, j, gameBoard);
							}
							leftDiaCount[diaArrayPos] = 1;
							leftDiaZeroCount[diaArrayPos] = 0;
						}

						break;
					case 2:

						if (lDC <= 0) {// 2's before
							leftDiaCount[diaArrayPos]--;
						} else if (0 < lDC) {// 1's before
							if (lDC + 1 + lZDC >= 4) {// check if eligible
								utility += value(lDC, 1);
							} else {
								utility += leftDiagonalChecker(lDC, lZDC, 1, i, j, gameBoard);
							}
							leftDiaCount[diaArrayPos] = -1;
							leftDiaZeroCount[diaArrayPos] = 0;
						}

						break;
					}
				}

				// RIGHTDIAGONAL
				diaArrayPos = rightAnchorPointer - ((columns - 1) - i) + j;
				if (diaArrayPos >= 0 && diaArrayPos < diaLength) {
					int rDC = rightDiaCount[diaArrayPos];
					int rZDC = rightDiaZeroCount[diaArrayPos];
					switch (gameBoard[i][j]) {
					case 0:
						if (rDC == 0) {// zeroes before
							rightDiaZeroCount[diaArrayPos]++;
						} else if (0 < rDC) {// 1's before
							if (rDC + 1 + rZDC >= 4) {// check if eligible
								utility += value(rDC, 1);
							} else {
								utility += rightDiagonalChecker(rDC, rZDC, 1, i, j, gameBoard);// TODO:skal det være right dia eller rightdiazero
							}
						} else if (rDC < 0) {// 2's before
							if (-rDC + 1 + rZDC >= 4) {// check if eligible
								utility += value(-rDC, 2);
							} else {
								utility += rightDiagonalChecker(-rDC, rZDC, 2, i, j, gameBoard);// TODO:skal det være right dia eller rightdiazero
							}
						}
						rightDiaCount[diaArrayPos] = 0;
						rightDiaZeroCount[diaArrayPos] = 1;
						break;
					case 1:
						if (rDC >= 0) {// 1's before
							rightDiaCount[diaArrayPos]++;
						} else if (rDC < 0) {// 2's before
							if (-rDC + rZDC >= 4) {// check if eligible
								utility += value(-rDC, 2);
							} else {
								utility += rightDiagonalChecker(-rDC, rZDC, 2, i, j, gameBoard);
							}
							rightDiaCount[diaArrayPos] = 1;
							rightDiaZeroCount[diaArrayPos] = 0;
						}
						break;
					case 2:
						if (rDC <= 0) {// 2's before
							rightDiaCount[diaArrayPos]--;
						} else if (0 < rDC) {// 1's before
							if (rDC + 1 + rZDC >= 4) {// check if eligible
								utility += value(rZDC, 1);
							} else {
								utility += rightDiagonalChecker(rDC, rZDC, 1, i, j, gameBoard);
							}
							rightDiaCount[diaArrayPos] = -1;
							rightDiaZeroCount[diaArrayPos] = 0;
						}
						break;
					}
				}
			}
		}

		for (int i = 0; i < columns; i++) {
			//if (colZeroCount[i] == 0) {
				if (4 <= colCount[i]) {
					utility += value(4, 1);
				} else if (colCount[i] <= -4) {
					utility += value(4, 2);
				}
			//}
		}
		for (int i = 0; i < rows; i++) {
			//if (rowZeroCount[i] == 0) {
				if (4 <= rowCount[i]) {
					utility += value(4, 1);
				} else if (rowCount[i] <= -4) {
					utility += value(4, 2);
				}
			//}
		}
		for (int i = 0; i < diaLength; i++) {
			//if (leftDiaZeroCount[i] == 0) {
				if (4 <= leftDiaCount[i]) {
					utility += value(4, 1);
				} else if (leftDiaCount[i] <= -4) {
					utility += value(4, 2);
				}
			//}
			//if (rightDiaZeroCount[i] == 0) {
				if (4 <= rightDiaCount[i]) {
					utility += value(4, 1);
				} else if (rightDiaCount[i] <= -4) {
					utility += value(4, 2);
				}
			//}
		}

		return utility;
	}

	private double value(int consecutive, int playerID) {
		switch (consecutive) {
		case 1:
			if (playerID == 1) {
				return oneRow;
			} else {
				return -oneRow;
			}
		case 2:
			if (playerID == 1) {
				return twoRow;
			} else {
				return -twoRow;
			}
		case 3:
			if (playerID == 1) {
				return threeRow;
			} else {
				return -threeRow;
			}
		default:
			if (playerID == 1) {
				return fourRow;
			} else {
				return -fourRow;
			}
		}
	}

	// colC = the positive colCount value.
	private double columnCounter(int colC, int row, int playerID) {
		double util = 0d;
		if (colC > 0) {// 1's before
			if (colC + rows - row >= 4) {// there is space to get 4 in a row
				util = value(colC, playerID);
			}
		}
		return util;
	}

	// rowC = the positive rowCount value.
	private double rowChecker(int rowC, int rowZeroC, int playerID, int column, int row, int[][] gameBoard) {
		int need = 4 - rowZeroC - rowC - 1;
		int left = need;

		// Checks all possible locations
		if (left > 0 && column + 1 < columns) {// If there is space 2 to the left
			if (gameBoard[column + 1][row] == 0 || gameBoard[column + 1][row] == 1) {
				left--;
			}

			if (left > 0 && column + 2 < columns) {// If there is space 2 to the left
				if (gameBoard[column + 2][row] == 0 || gameBoard[column + 2][row] == 1) {
					left--;
				}
			}
		}
		if (left == 0) {
			return value(rowC, playerID);
		}
		return 0d;
	}

	// leftDiaC = the positive leftDiaCount
	private double leftDiagonalChecker(int leftDiaC, int leftZeroDiaC, int playerID, int column, int row, int[][] gameBoard) {
		if (column + 1 < columns && row + 1 < rows) {// check for space
			int need = 4 - leftZeroDiaC - leftDiaC - 1;
			int left = need;
			if (gameBoard[column + 1][row + 1] == 0 || gameBoard[column + 1][row + 1] == 1) {
				left--;
				if (left > 0 && column + 2 < columns && row + 2 < rows && (gameBoard[column + 2][row + 2] == 0 || gameBoard[column + 2][row + 2] == 1)) {
					left--;
				}
			} else if (left == 0) {
				return value(leftDiaC, playerID);
			}
		}
		return 0d;
	}

	// rightDiaC = the positive rightDiaCount
	private double rightDiagonalChecker(int rightDiaC, int rightZeroDiaC, int playerID, int column, int row, int[][] gameBoard) {
		if (column + 1 < columns && row - 1 > 0) {// check for space
			int need = 4 - rightZeroDiaC - rightDiaC - 1;
			int left = need;
			if (gameBoard[column + 1][row - 1] == 0 || gameBoard[column + 1][row - 1] == 1) {
				left--;
				if (left > 0 && column + 2 < columns && row - 2 > 0 && (gameBoard[column + 2][row - 2] == 0 || gameBoard[column + 2][row - 2] == 1)) {
					left--;
				}
			} else if (left == 0) {
				return value(rightDiaC, playerID);
			}
		}
		return 0d;
	}
}