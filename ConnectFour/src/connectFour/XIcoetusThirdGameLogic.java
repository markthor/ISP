package connectFour;

import java.util.List;

public class XIcoetusThirdGameLogic implements IGameLogic {
	private int columns = 0;
	private int rows = 0;
	private int playerID;
	private final static int maxDepth = 7;
	private int[][] gameBoard;
	private int[] nextCoinPos;
	private Utility utility;

	// The maximum amount of adjacent 4 connects in one diagonal.
	private int diaLength;
	// Base pointers for diagonal arrays.
	private int leftAnchorPointer;
	private int rightAnchorPointer;

	public void initializeGame(int columns, int rows, int playerID) {
		this.columns = columns;
		this.rows = rows;
		this.playerID = playerID;
		gameBoard = new int[columns][rows];
		nextCoinPos = new int[columns];
		utility = new UtilityTwo(columns, rows);

		diaLength = (columns - 3 + rows - 3) - 1;
		leftAnchorPointer = rows - 4;
		rightAnchorPointer = columns - 4;
		// test pointer for easy bot
		// nextMove = columns-1;
		// TODO Write your implementation for this method

		// TESTING
		int[][] gb = new int[][] {{0, 0, 0, 0, 0, 0}, {2, 2, 2, 1, 0, 0}, {1, 2, 1, 0, 0, 0}, {1, 1, 2, 2, 1, 0}, {1, 1, 2, 1, 0, 0}, {2, 1, 2, 2, 1, 0}, {0, 0, 0, 0, 0, 0}};
		/*
		 * Utility: 25000
		 * Columns: 7000
		 * Rows: 6000
		 * LeftDia: 6000
		 * RightDia:
		 */
		System.out.println("1's utility: " + utility.utility(gb));
		//gb = new int[][] { { 2, 2, 2, 2, 2, 2 }, { 2, 2, 2, 2, 2, 2 }, { 2, 2, 2, 2, 2, 2 }, { 2, 2, 2, 2, 2, 2 }, { 2, 2, 2, 2, 2, 2 }, { 2, 2, 2, 2, 2, 2 }, { 2, 2, 2, 2, 2, 2 } };
		/*
		 * Utility: -19000
		 * Columns: -7000
		 * Rows: -6000
		 * LeftDia: -6000
		 * RightDia:
		 */
		//System.out.println("2's utility: " + utility.utility(gb));

		// Utility MAIN
		// UtilityTwo util = new UtilityTwo(columns, rows);
		// int[][] gb = new int[][]{{1,0,0,0}, {0,0,0,0},{1,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
		// gameBoard = gb;
		// printGameboard();
		System.out.println();

		/*
		 * this.columns = 6;
		 * this.rows = 6;
		 * int[][] gb = new int[][]{{1,0,1,1,1,0}, {0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0},{0,0,0,0,0,0}};
		 * System.out.println(getUtility(gb));
		 * 
		 * //Utility MAIN
		 * UtilityTwo util = new UtilityTwo(columns, rows);
		 * int[][] gb = new int[][]{{1,0,0,0}, {0,0,0,0},{1,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
		 * gameBoard = gb;
		 * printGameboard();
		 * System.out.println("Utility " + util.utility(gb));
		 */
		//gb = new int[][]{{0, 0, 0, 0, 0, 0}, {2, 2, 0, 0, 0, 0}, {1, 2, 1, 0, 0, 0}, {1, 1, 2, 2, 1, 0}, {1, 1, 2, 1, 0, 0}, {2, 1, 2, 2, 1, 0}, {0, 0, 0, 0, 0, 0}};
		//gameBoard = gb;
	}

	public Winner gameFinished() {
		int[] colCount = new int[columns];
		int[] rowCount = new int[rows];
		int[] leftDiaCount = new int[diaLength];
		int[] rightDiaCount = new int[diaLength];
		int diaArrayPos;
		Winner winner;

		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < rows; j++) {
				colCount[i] = updateCellCount(gameBoard[i][j], colCount[i]);
				winner = winnerCheck(colCount[i]);
				if (winner != null) {
					return winner;
				}

				rowCount[j] = updateCellCount(gameBoard[i][j], rowCount[j]);
				winner = winnerCheck(rowCount[j]);
				if (winner != null) {
					return winner;
				}

				diaArrayPos = leftAnchorPointer - j + i;
				if (diaArrayPos >= 0 && diaArrayPos < diaLength) {
					leftDiaCount[diaArrayPos] = updateCellCount(gameBoard[i][j], leftDiaCount[diaArrayPos]);
					winner = winnerCheck(leftDiaCount[diaArrayPos]);
					if (winner != null) {
						return winner;
					}
				}
				diaArrayPos = rightAnchorPointer - ((columns - 1) - i) + j;
				if (diaArrayPos >= 0 && diaArrayPos < diaLength) {
					rightDiaCount[diaArrayPos] = updateCellCount(gameBoard[i][j], rightDiaCount[diaArrayPos]);
					winner = winnerCheck(rightDiaCount[diaArrayPos]);
					if (winner != null) {
						return winner;
					}
				}
			}
		}
		if (tieCheck()) {
			return Winner.TIE;
		}

		return Winner.NOT_FINISHED;
	}

	private Winner winnerCheck(int count) {
		if (count == -4) {
			return Winner.PLAYER1;
		}
		if (count == 4) {
			return Winner.PLAYER2;
		}
		return null;
	}

	private boolean tieCheck() {
		int filled = 0;
		for (int i = 0; i < columns; i++) {
			if (gameBoard[i][rows - 1] != 0)
				filled++;
		}
		return filled == columns;
	}

	private int updateCellCount(int current, int count) {
		if (count == 0) {
			if (current == 1) {
				return -1;
			}
			if (current == 2) {
				return 1;
			}
		} else {
			if (count < 0) {
				if (current == 1) {
					return count - 1;
				}
				if (current == 2) {
					return 1;
				}
			}
			if (count > 0) {
				if (current == 1) {
					return -1;
				}
				if (current == 2) {
					return count + 1;
				}
			}

		}
		return 0;
	}

	public void insertCoin(int column, int playerID) {
		gameBoard[column][nextCoinPos[column]] = playerID;
		nextCoinPos[column]++;
		printGameboard();
	}

	/**
	 * 
	 * @return The column in which it is best to place the coin
	 */
	public int decideNextMove() {
		System.out.println("Started");
		Action bestAction = null;
		// AI is blue who wants to maximize the utility
		List<Action> actions = Action.getActions(columns, rows, playerID, gameBoard);
		for (Action a : actions) {
			gameBoard = a.apply(gameBoard);
			if (playerID == 1) {
				minValue(a, 1);
				if (bestAction == null || bestAction.getUtility() < a.getUtility()) {
					bestAction = a;
				}
			} else {
				maxValue(a, 1);
				if (bestAction == null || a.getUtility() < bestAction.getUtility()) {
					bestAction = a;
				}
			}
			gameBoard = a.undo(gameBoard);
		}
		System.out.println("Utility: " + bestAction.getUtility());
		return bestAction.getColumn();
	}

	private void minValue(Action appliedAction, int depth) {
		if (depth <= maxDepth && gameFinished() == Winner.NOT_FINISHED) {
			Action bestAction = null;

			List<Action> actions = Action.getActions(columns, rows, 2, gameBoard);
			for (Action a : actions) {
				gameBoard = a.apply(gameBoard);
				maxValue(a, depth + 1);
				if (bestAction == null || a.getUtility() < bestAction.getUtility()) {
					bestAction = a;
				}
				gameBoard = a.undo(gameBoard);
			}
			appliedAction.setUtility(bestAction.getUtility());
		} else {
			appliedAction.setUtility(utility.utility(gameBoard));
		}

	}

	public void maxValue(Action appliedAction, int depth) {
		if (depth <= maxDepth && gameFinished() == Winner.NOT_FINISHED) {
			Action bestAction = null;

			List<Action> actions = Action.getActions(columns, rows, 1, gameBoard);
			for (Action a : actions) {
				gameBoard = a.apply(gameBoard);
				minValue(a, depth + 1);
				if (bestAction == null || bestAction.getUtility() < a.getUtility()) {
					bestAction = a;
				}
				gameBoard = a.undo(gameBoard);
			}
			appliedAction.setUtility(bestAction.getUtility());
		} else {
			appliedAction.setUtility(utility.utility(gameBoard));
		}
	}

	private void printGameboard() {
		for (int i = rows - 1; 0 <= i; i--) {
			for (int j = 0; j < columns; j++) {
				System.out.print(gameBoard[j][i] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	private double getUtility(int[][] gameBoard) {
		int[] colCount = new int[columns];
		int[] rowCount = new int[rows];
		int[] leftDiaCount = new int[diaLength];
		int[] rightDiaCount = new int[diaLength];
		int diaArrayPos;

		int[] colZeroCount = new int[columns];
		int[] rowZeroCount = new int[rows];
		int[] leftDiaZeroCount = new int[diaLength];
		int[] rightDiaZeroCount = new int[diaLength];

		int[] colRecentZeroCount = new int[columns];
		int[] rowRecentZeroCount = new int[rows];
		int[] leftDiaRecentZeroCount = new int[diaLength];
		int[] rightDiaRecentZeroCount = new int[diaLength];

		int[] colRecentDisjointCount = new int[columns];
		int[] rowRecentDisjointCount = new int[rows];
		int[] leftDiaRecentDisjointCount = new int[diaLength];
		int[] rightDiaRecentDisjointCount = new int[diaLength];

		double utility = 0;
		final double oneRow = 5;
		final double twoRow = 30;
		final double threeRow = 75;
		final double fourRow = 1000;
		int[] updatedCounts;

		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < rows; j++) {
				updatedCounts = updateCounts(gameBoard[i][j], colCount[i], colZeroCount[i], colRecentZeroCount[i], colRecentDisjointCount[i]);
				colCount[i] = updatedCounts[0];
				colZeroCount[i] = updatedCounts[1];
				colRecentZeroCount[i] = updatedCounts[2];
				colRecentDisjointCount[i] = updatedCounts[3];

				if (3 < Math.abs(colCount[i]) + colZeroCount[i] + Math.abs(colRecentDisjointCount[i]) && colRecentZeroCount[i] < 4) {
					switch (colCount[i]) {
					case 1:
						utility += oneRow;
						break;
					case 2:
						utility += twoRow;
						if (2 < colZeroCount[i]) {
							utility -= oneRow;
						}
						break;
					case 3:
						utility += threeRow;
						if (1 < colZeroCount[i]) {
							utility -= twoRow;
						}
						break;
					case 4:
						return fourRow;
					case -1:
						utility -= oneRow;
						break;
					case -2:
						utility -= twoRow;
						if (2 < colZeroCount[i]) {
							utility += oneRow;
						}
						break;
					case -3:
						utility -= threeRow;
						if (1 < colZeroCount[i]) {
							utility += twoRow;
						}
						break;
					case -4:
						return -fourRow;
					}
					if (colRecentDisjointCount[i] != 0) {
						switch (colRecentDisjointCount[i]) {
						case 1:
							utility += oneRow;
							break;
						case 2:
							utility += twoRow;
							break;
						case -1:
							utility -= oneRow;
							break;
						case -2:
							utility -= twoRow;
							break;
						}
						colZeroCount[i] += Math.abs(colRecentDisjointCount[i]);
						colRecentDisjointCount[i] = 0;
					}
				}
			}
		}

		return utility;
	}

	private int[] updateCounts(int current, int count, int zeroCount, int recentZeroCount, int recentDisjointCount) {
		int[] result = new int[4];
		if (current == 0) {
			zeroCount = zeroCount + 1;
			recentZeroCount = recentZeroCount + 1;
		}

		if (current == 1) {
			if (count < 0) {
				// Finds a coin from same player
				if (0 < recentZeroCount) {
					if (zeroCount < 3 && recentZeroCount < 3) {
						recentDisjointCount = count;
					}
					count = -1;
				} else {
					count = count - 1;
				}
				recentZeroCount = 0;
			} else if (0 < count) {
				// Finds a coin from other player
				count = -1;
				zeroCount = recentZeroCount;
				recentZeroCount = 0;
				recentDisjointCount = 0;
			} else {
				recentZeroCount = 0;
				count = -1;
			}
		}

		if (current == 2) {
			if (0 < count) {
				// Finds a coin from same player
				if (0 < recentZeroCount) {
					if (zeroCount < 3 && recentZeroCount < 3) {
						recentDisjointCount = count;
					}
					count = 1;
				} else {
					count = count + 1;
				}
				recentZeroCount = 0;
			} else if (count < 0) {
				// Finds a coin from other player
				count = 1;
				zeroCount = recentZeroCount;
				recentZeroCount = 0;
				recentDisjointCount = 0;
			} else {
				recentZeroCount = 0;
				count = 1;
			}
		}
		result[0] = count;
		result[1] = zeroCount;
		result[2] = recentZeroCount;
		result[3] = recentDisjointCount;
		return result;

	}

}
