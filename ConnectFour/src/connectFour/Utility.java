package connectFour;

public class Utility implements IUtility {
	private int columns, rows, diaLength, leftAnchorPointer, rightAnchorPointer;
	// General arrays
	private int[] colCount, rowCount, leftDiaCount, rightDiaCount;
	// Arrays used to count consecutive zeroes
	private int[] colZeroCount, rowZeroCount, leftDiaZeroCount, rightDiaZeroCount;
	// Constants used for assigning the utility value
	private final double oneRow = 1, twoRow = 10, threeRow = 35, fourRow = 1000;

	/**
	 * @param columns
	 *            Number of columns
	 * @param rows
	 *            Number of rows
	 */
	public Utility(int columns, int rows) {
		this.columns = columns;
		this.rows = rows;
		// The length of the diagonal counting
		diaLength = columns - 3 + rows - 3 - 1;
		// Base pointers for diagonal arrays. The pointer points at the index that represents the diagonal that starts in the gameboard corner.
		leftAnchorPointer = rows - 4;
		rightAnchorPointer = columns - 4;
	}

	/**
	 * Calculates the utility of the given gameboard
	 * 
	 * @param gameBoard
	 *            The gameboard
	 */
	public double utility(int[][] gameBoard) {
		// initialize the counting arrays
		colCount = new int[columns];
		rowCount = new int[rows];
		leftDiaCount = new int[diaLength];
		rightDiaCount = new int[diaLength];
		int diaArrayPos;

		double utility = 0;

		// initialize the zero arrays
		colZeroCount = new int[columns];
		rowZeroCount = new int[rows];
		leftDiaZeroCount = new int[diaLength];
		rightDiaZeroCount = new int[diaLength];

		// Loops through the gameboard
		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < rows; j++) {
				// Local variables to minimize the amount of array lookups
				int cC = colCount[i];
				int rC = rowCount[j];
				int rZC = rowZeroCount[j];

				// Switches on the value of the current field of the gameboard
				switch (gameBoard[i][j]) {
				// There is a 0 in the field
				case 0:
					// COLUMNS
					// Increment the amount of zeroes found in this column
					colZeroCount[i]++;

					// 1's before
					if (cC > 0) {
						// Check if it is a valid column to give you utility
						utility += columnCounter(cC, j, 1);
					}
					// 2's before
					if (colCount[i] < 0) {
						// Check if it is a valid column to give you utility
						utility += columnCounter(-cC, j, 2);
					}
					colCount[i] = 0;

					// ROWS
					// If there have been consecutive zeroes
					if (rC == 0) {
						// Increment the amount of consecutive zeroes in the current row
						rowZeroCount[j]++;
						break;
					}
					// if there has been 1 values before the current zero
					else if (0 < rC) {
						// If there has been 4 or more 1's and 0's in a row
						if (rC + rZC + 1 >= 4) {
							// Add utility
							utility += value(rC, 1);
						} else {
							// Check if it is a valid row to give you utility
							utility += checkRow(rC, rZC, 1, i, j, gameBoard);
						}
					}
					// if there has been 2 values before the current zero
					else if (rC < 0) {
						// If there has been 4 or more 2's and 0's in a row
						if (-rC + rZC + 1 >= 4) {
							// Add utility
							utility += value(-rC, 2);
						} else {
							// Check if it is a valid row to give you utility
							utility += checkRow(-rC, rZC, 2, i, j, gameBoard);
						}
					}
					// Set rowcount for the row to 0
					rowCount[j] = 0;
					// Set the amount of consecutive zeroes to 1
					rowZeroCount[j] = 1;

					break;
				// There is a 1 in the field
				case 1: // blue coin
					// COLUMNS
					// This column have had one or more consecutive 1's
					if (0 <= cC) {
						// Increment the amount of times that 1's has been observed
						colCount[i]++;
					}
					// 2's before
					else {
						if (colCount[i] <= -4) {
							// Check if it is a valid column to give you utility
							utility += value(4, 2);
						}
						// Set that one 1 has been observed
						colCount[i] = 1;
					}

					// ROWS
					// If it has been 1's before
					if (rC >= 0) {
						// increase the amount of times 1's as been observed
						rowCount[j]++;
					}
					// There has been 2's before
					else if (rC < 0) {
						// Check if there has been enough 2's and 0's for the row to give utility
						if (-rC + rZC >= 4) {
							// Add utility
							utility += value(-rC, 2);
						}
						// Set rowcount for the row to 1
						rowCount[j] = 1;
						// Set the amount of consecutive zeroes to 0
						rowZeroCount[j] = 0;
					}

					break;

				// There is a 2 in the field
				case 2: // red coin
					// COLUMNS
					// This column have had one or more consecutive 2's
					if (cC <= 0) {
						// Increment the amount of times that 2's has been observed
						colCount[i]--;
					}
					// 1's before
					else {
						if (colCount[i] >= 4) {
							// Check if it is a valid column to give you utility
							utility += value(4, 1);
						}
						// Set that one 2 has been observed
						colCount[i] = -1;
					}

					// ROWS
					// If it has been 2's before
					if (rC <= 0) {
						// increase the amount of times 1's as been observed
						rowCount[j]--;
					}
					// There has been 1's before
					else if (0 < rC) {
						// Check if there has been enough 1's and 0's for the row to give utility
						if (rC + rZC >= 4) {
							// Add utility
							utility += value(rC, 1);
						}
						// Set rowcount for the row to -1
						rowCount[j] = -1;
						// Set the amount of consecutive zeroes to 0
						rowZeroCount[j] = 0;
					}

					break;
				}

				// LEFTDIAGONAL
				// Calculate the array position for the diagonal arrays
				diaArrayPos = leftAnchorPointer - j + i;
				// If the position is valid
				if (diaArrayPos >= 0 && diaArrayPos < diaLength) {
					// Local variables to limit the amunt of array indexes
					int lDC = leftDiaCount[diaArrayPos];
					int lZDC = leftDiaZeroCount[diaArrayPos];
					// Switch on the board position
					switch (gameBoard[i][j]) {
					// A 0 is in the field
					case 0:
						// There has been zeroes before
						if (lDC == 0) {
							// Increase the amount of consecutive zeroes
							leftDiaZeroCount[diaArrayPos]++;
						}
						// There has been 1's before
						else if (0 < lDC) {
							// Check if there has been 4 or more 1's and 0's in a row
							if (lDC + 1 + lZDC >= 4) {
								// Add utility
								utility += value(lDC, 1);
							} else {
								// Check if the diagonal is valid for utility
								utility += checkLeftDiagonal(lDC, lZDC, 1, i, j, gameBoard);
							}
						}
						// There has been 2's before
						else if (lDC < 0) {
							// Check if there has been 4 or more 2's and 0's in a row
							if (-lDC + 1 + lZDC >= 4) {
								// Add utility
								utility += value(-lDC, 2);
							} else {
								// Check if the diagonal is valid for utility
								utility += checkLeftDiagonal(-lDC, lZDC, 2, i, j, gameBoard);
							}
						}
						// Set the count to 0
						leftDiaCount[diaArrayPos] = 0;
						// Set the amount of consecutive zeroes
						leftDiaZeroCount[diaArrayPos] = 1;
						break;

					// there is a 1 in the field
					case 1:
						// 1's before
						if (lDC >= 0) {
							// Increment amount of 1's
							leftDiaCount[diaArrayPos]++;
						}
						// 2's before
						else if (lDC < 0) {
							// Check if there has been 4 or more 2's and 0's in a row
							if (-lDC + lZDC >= 4) {
								// Add utility
								utility += value(-lDC, 2);
							} else {
								// Check if it is a valid diagonal
								utility += checkLeftDiagonal(-lDC, lZDC, 2, i, j, gameBoard);
							}
							// Set the count to 1
							leftDiaCount[diaArrayPos] = 1;
							// Set the amount of consecutive zeroes to 0
							leftDiaZeroCount[diaArrayPos] = 0;
						}
						break;

					// There is a 2 in the field
					case 2:
						// 2's before
						if (lDC <= 0) {
							leftDiaCount[diaArrayPos]--;
						}
						// 1's before
						else if (0 < lDC) {
							// Check if there has been 4 or more 1's and 0's in a row
							if (lDC + 1 + lZDC >= 4) {
								// Add utility
								utility += value(lDC, 1);
							} else {
								// Check if the diagonal is valid for utility
								utility += checkLeftDiagonal(lDC, lZDC, 1, i, j, gameBoard);
							}
							// set the diagonal count to -1
							leftDiaCount[diaArrayPos] = -1;
							// Set the amount of consecutive zeroes to 0
							leftDiaZeroCount[diaArrayPos] = 0;
						}

						break;
					}
				}

				// RIGHTDIAGONAL
				// Set the new diaarraypointer
				diaArrayPos = rightAnchorPointer - ((columns - 1) - i) + j;
				// Check if oit is a valid diagonal
				if (diaArrayPos >= 0 && diaArrayPos < diaLength) {
					// Local variables to decrease the amount of array looups
					int rDC = rightDiaCount[diaArrayPos];
					int rZDC = rightDiaZeroCount[diaArrayPos];
					// Switch which follows the same procedure as the LEFTDIAGONAL
					switch (gameBoard[i][j]) {
					case 0:
						if (rDC == 0) {// zeroes before
							rightDiaZeroCount[diaArrayPos]++;
						} else if (0 < rDC) {// 1's before
							if (rDC + 1 + rZDC >= 4) {// check if eligible
								utility += value(rDC, 1);
							} else {
								utility += checkRightDiagonal(rDC, rZDC, 1, i, j, gameBoard);
							}
						} else if (rDC < 0) {// 2's before
							if (-rDC + 1 + rZDC >= 4) {// check if eligible
								utility += value(-rDC, 2);
							} else {
								utility += checkRightDiagonal(-rDC, rZDC, 2, i, j, gameBoard);
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
							}
							rightDiaCount[diaArrayPos] = 1;
							rightDiaZeroCount[diaArrayPos] = 0;
						}
						break;
					case 2:
						if (rDC <= 0) {// 2's before
							rightDiaCount[diaArrayPos]--;
						} else if (0 < rDC) {// 1's before
							if (rDC + rZDC >= 4) {// check if eligible
								utility += value(rZDC, 1);
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
			if (4 <= colCount[i]) {
				utility += value(4, 1);
			} else if (colCount[i] <= -4) {
				utility += value(4, 2);
			}
		}

		for (int i = 0; i < rows; i++) {
			if (rowCount[i] > 0) {
				if (rowCount[i] + rowZeroCount[i] >= 4) {
					utility += value(rowCount[i], 1);
				}
			} else if (rowCount[i] < 0) {
				if (-rowCount[i] + rowZeroCount[i] >= 4) {
					utility += value(-rowCount[i], 2);
				}
			}
		}

		for (int i = 0; i < diaLength; i++) {
			if (4 <= leftDiaCount[i]) {
				utility += value(4, 1);
			} else if (leftDiaCount[i] <= -4) {
				utility += value(4, 2);
			}

			if (4 <= rightDiaCount[i]) {
				utility += value(4, 1);
			} else if (rightDiaCount[i] <= -4) {
				utility += value(4, 2);
			}
		}

		return utility;
	}

	/**
	 * Returns the utility value of a given amount of consecutive values for a playerID
	 * 
	 * @param consecutive
	 *            The amount of times a value has been observed consecutively
	 * @param playerID
	 *            The ID of the player which should get the utility
	 * @return the utility value of the amount of consecutive values for the given player ID
	 */
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
	/**
	 * Checks if a column is valid for receiving utility
	 * 
	 * @param colC
	 *            The positive value in the colCount array
	 * @param row
	 *            The row which
	 * @param playerID
	 * @return the utility for the given column state
	 */
	private double columnCounter(int colC, int row, int playerID) {
		double util = 0d;
		// Check if thre is space to get 4 in a row
		if (colC + (rows - row) >= 4) {
			util = value(colC, playerID);
		}
		return util;
	}

	/**
	 * Check if a row is valid for utility
	 * 
	 * @param rowC
	 *            The positive rowCount
	 * @param rowZeroC
	 *            The amount of consecutive zeroes
	 * @param playerID
	 *            The player ID
	 * @param column
	 *            the column from where the method is called
	 * @param row
	 *            the row from where the method is called
	 * @param gameBoard
	 *            The current gameboard
	 * @return The utility for the given row state
	 */
	private double checkRow(int rowC, int rowZeroC, int playerID, int column, int row, int[][] gameBoard) {
		// The amount of pieces needed to get 4 in a row
		int need = 4 - rowZeroC - rowC - 1;
		// The amount of pieces needed to get 4 in a row, updated throughout the method
		int left = need;

		// Check the field to the right contains a 0 or a piece for that player
		if (left > 0 && column + 1 < columns) {
			if (gameBoard[column + 1][row] == 0 || gameBoard[column + 1][row] == playerID) {
				left--;

				// If there is needed more pieces to get 4 in a row, check 2 spaces to the right
				if (left > 0 && column + 2 < columns) {
					if (gameBoard[column + 2][row] == 0 || gameBoard[column + 2][row] == playerID) {
						left--;
					}
				}
			}
		}
		if (left == 0) {
			return value(rowC, playerID);
		}
		return 0d;
	}

	/**
	 * 
	 * @param leftDiaC
	 *            The positive leftDiaCount
	 * @param leftZeroDiaC
	 *            The amount of consecutive zeroes
	 * @param playerID
	 *            The player ID
	 * @param column
	 *            the column from where the method is called
	 * @param row
	 *            the row from where the method is called
	 * @param gameBoard
	 *            The current gameboard
	 * @return The utility for the given left diagonal state
	 */
	private double checkLeftDiagonal(int leftDiaC, int leftZeroDiaC, int playerID, int column, int row, int[][] gameBoard) {
		// Check if there is more space in the diagonal
		if (column + 1 < columns && row + 1 < rows) {
			// The amount of pieces needed to get 4 in a row
			int need = 4 - leftZeroDiaC - leftDiaC - 1;
			// The amount of pieces needed to get 4 in a row, updated troughout the method
			int left = need;

			// check what the next piece is
			if (gameBoard[column + 1][row + 1] == 0 || gameBoard[column + 1][row + 1] == playerID) {
				// Decrease left if it is a 0 or the players piece
				left--;
				// If left is still higher than zero, check the next space
				if (left > 0 && column + 2 < columns && row + 2 < rows && (gameBoard[column + 2][row + 2] == 0 || gameBoard[column + 2][row + 2] == playerID)) {
					left--;
				}
			}
			// If left is 0, then the diagonal is valid
			else if (left == 0) {
				return value(leftDiaC, playerID);
			}
		}
		return 0d;
	}

	/**
	 * 
	 * @param rightDiaC
	 *            The positive rightDiaCount
	 * @param rightZeroDiaC
	 *            The amount of consecutive zeroes
	 * @param playerID
	 *            The player ID
	 * @param column
	 *            the column from where the method is called
	 * @param row
	 *            the row from where the method is called
	 * @param gameBoard
	 *            The current gameboard
	 * @return The utility for the given right diagonal state
	 */
	private double checkRightDiagonal(int rightDiaC, int rightZeroDiaC, int playerID, int column, int row, int[][] gameBoard) {
		// Same procedure as left diagonal
		/*
		 * if (column + 1 < columns && row - 1 > 0) {
		 * int need = 4 - rightZeroDiaC - rightDiaC - 1;
		 * int left = need;
		 * if (gameBoard[column + 1][row - 1] == 0 || gameBoard[column + 1][row - 1] == playerID) {
		 * left--;
		 * if (left > 0 && column + 2 < columns && row - 2 > 0 && (gameBoard[column + 2][row - 2] == 0 || gameBoard[column + 2][row - 2] == playerID))
		 * {
		 * left--;
		 * }
		 * } else if (left == 0) {
		 * return value(rightDiaC, playerID);
		 * }
		 * }
		 * return 0d;
		 */

		if (column + 1 < columns && row - 1 >= 0) {
			// The amount of pieces needed to get 4 in a row
			int need = 4 - rightZeroDiaC - rightDiaC - 1;
			// The amount of pieces needed to get 4 in a row, updated troughout the method
			int left = need;

			// check what the next piece is
			if (gameBoard[column + 1][row - 1] == 0 || gameBoard[column + 1][row - 1] == playerID) {
				// Decrease left if it is a 0 or the players piece
				left--;
				// If left is still higher than zero, check the next space
				if (left > 0 && column + 2 < columns && row - 2 >= 0 && (gameBoard[column + 2][row - 2] == 0 || gameBoard[column + 2][row - 2] == playerID)) {
					left--;
				}
			}
			// If left is 0, then the diagonal is valid
			else if (left == 0) {
				return value(rightDiaC, playerID);
			}
		}
		return 0d;
	}
}