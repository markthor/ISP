package connectFour;

import java.util.List;

public class XIcoetusThirdGameLogic implements IGameLogic {
	private int columns = 0;
	private int rows = 0;
	private int playerID;
	private final static int maxDepth = 7;
	private int[][] gameBoard;
	private int[] nextCoinPos;
	private IUtility utility;

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
		utility = new Utility(columns, rows);

		diaLength = (columns - 3 + rows - 3) - 1;
		leftAnchorPointer = rows - 4;
		rightAnchorPointer = columns - 4;
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
				minValue(a, 1, -Double.MAX_VALUE, Double.MAX_VALUE);
				if (bestAction == null || bestAction.getUtility() < a.getUtility()) {
					bestAction = a;
				}
			} else {
				maxValue(a, 1, -Double.MAX_VALUE, Double.MAX_VALUE);
				if (bestAction == null || a.getUtility() < bestAction.getUtility()) {
					bestAction = a;
				}
			}
			gameBoard = a.undo(gameBoard);
		}
		System.out.println("Utility: " + bestAction.getUtility());
		return bestAction.getColumn();
	}
	
	public void maxValue(Action appliedAction, int depth, double alpha, double beta) {
		if (depth <= maxDepth && gameFinished() == Winner.NOT_FINISHED) {
			Action bestAction = null;
			
			List<Action> actions = Action.getActions(columns, rows, 1, gameBoard);
			for (Action a : actions) {
				gameBoard = a.apply(gameBoard);
				minValue(a, depth + 1, alpha, beta);
				if (bestAction == null || bestAction.getUtility() < a.getUtility()) {
					bestAction = a;
					if(bestAction.getUtility() >= beta){
						gameBoard = a.undo(gameBoard);
						break;
					}
					else{
						alpha = Math.max(alpha, bestAction.getUtility());
					}
				}
				gameBoard = a.undo(gameBoard);
			}
			appliedAction.setUtility(bestAction.getUtility());
		} else {
			appliedAction.setUtility(utility.utility(gameBoard));
		}
	}

	private void minValue(Action appliedAction, int depth, double alpha, double beta) {
		if (depth <= maxDepth && gameFinished() == Winner.NOT_FINISHED) {
			Action bestAction = null;
			
			List<Action> actions = Action.getActions(columns, rows, 2, gameBoard);
			for (Action a : actions) {
				gameBoard = a.apply(gameBoard);
				maxValue(a, depth + 1, alpha, beta);
				if (bestAction == null || a.getUtility() < bestAction.getUtility()) {
					bestAction = a;
					if(bestAction.getUtility() <= alpha){
						gameBoard = a.undo(gameBoard);
						break;
					}
					else{
						beta = Math.min(beta, bestAction.getUtility());
					}
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
}
