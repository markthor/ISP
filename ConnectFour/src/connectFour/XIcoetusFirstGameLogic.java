package connectFour;

import java.io.Console;
import java.util.List;

public class XIcoetusFirstGameLogic implements IGameLogic {
	private int x = 0;
    private int y = 0;
    private int playerID;
    private int[][] gameBoard;
    private int[] nextCoinPos;
    //The maximum amount of adjacent 4 connects in one diagonal. 
    private int diaLength;
    //Base pointers for diagonal arrays.
    private int leftAnchorPointer;
    private int rightAnchorPointer;
    
    //test pointer for easy bot
    private int nextMove;
    
    public XIcoetusFirstGameLogic() {
        //TODO Write your implementation for this method
    }

    public void initializeGame(int x, int y, int playerID) {
        this.x = x;
        this.y = y;
        this.playerID = playerID;
        gameBoard = new int[x][y];
        nextCoinPos = new int[x];
        diaLength = (x-3+y-3)-1;
        leftAnchorPointer = y - 4;
        rightAnchorPointer = x - 4;
        //test pointer for easy bot
        nextMove = x-1;
        //TODO Write your implementation for this method
    }

    public Winner gameFinished() {
    	int[] colCount = new int [x];
    	int[] rowCount = new int[y];
    	int[] leftDiaCount = new int[diaLength];
    	int[] rightDiaCount = new int[diaLength];
    	int diaArrayPos;
        for(int i = 0; i < x; i++) {
        	for(int j = 0; j < y; j++) {
        		colCount[i] = updateCellCount(gameBoard[i][j], colCount[i]);
        		if(colCount[i] == -4) {
        			return Winner.PLAYER1;
        		}
        		if(colCount[i] == 4) {
        			return Winner.PLAYER2;
        		}
        		rowCount[j] = updateCellCount(gameBoard[i][j], rowCount[j]);
        		if(rowCount[j] == -4) {
        			return Winner.PLAYER1;
        		}
        		if(rowCount[j] == 4) {
        			return Winner.PLAYER2;
        		}
        		diaArrayPos = leftAnchorPointer-j+i;
        		if(diaArrayPos >= 0 && diaArrayPos < diaLength) {
        			leftDiaCount[diaArrayPos] = updateCellCount(gameBoard[i][j], leftDiaCount[diaArrayPos]);
            		if(leftDiaCount[diaArrayPos] == -4) {
            			return Winner.PLAYER1;
            		}
            		if(leftDiaCount[diaArrayPos] == 4) {
            			return Winner.PLAYER2;
            		}
        		}
        		diaArrayPos = rightAnchorPointer-((x-1)-i)+j;
        		if(diaArrayPos >= 0 && diaArrayPos < diaLength) {
        			rightDiaCount[diaArrayPos] = updateCellCount(gameBoard[i][j], rightDiaCount[diaArrayPos]);
            		if(rightDiaCount[diaArrayPos] == -4) {
            			return Winner.PLAYER1;
            		}
            		if(rightDiaCount[diaArrayPos] == 4) {
            			return Winner.PLAYER2;
            		}
        		}

        	}
        }
        return Winner.NOT_FINISHED;
    }
    
    private int updateCellCount(int current, int count) {
    	if(count == 0) {
    		if(current == 1) {
    			return -1;
    		}
    		if(current == 2) {
    			return 1;
    		}
    	} else {
    		if(count < 0) {
        		if(current == 1) {
        			return count-1;
        		}
        		if(current == 2) {
        			return 1;
        		}
    		}
    		if(count > 0) {
        		if(current == 1) {
        			return -1;
        		}
        		if(current == 2) {
        			return count+1;
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
		Action bestAction = null;
		// AI is blue who wants to maximize the utility
		List<Action> actions = Action.getActions(x, y, playerID, gameBoard);
		for (Action a : actions) {
			gameBoard = a.apply(gameBoard);
			if (playerID == 1) {
				maxValue(a);
			} else {
				minValue(a);
			}
			if (bestAction == null || bestAction.getUtility() > a.getUtility()) {
				bestAction = a;
			}
			gameBoard = a.undo(gameBoard);
		}
		return bestAction.getColumn();
	}

	private void minValue(Action appliedAction) {
		if (gameFinished() == Winner.NOT_FINISHED) {
			Action bestAction = null;

			List<Action> actions = Action.getActions(x, y, 1, gameBoard);
			for (Action a : actions) {
				gameBoard = a.apply(gameBoard);
				minValue(a);
				if (bestAction == null
						|| bestAction.getUtility() < a.getUtility()) {
					bestAction = a;
				}
				gameBoard = a.undo(gameBoard);
			}

		} else if (gameFinished() == Winner.PLAYER1) {
			appliedAction.setUtility(1d);
			;
		} else if (gameFinished() == Winner.PLAYER2) {
			appliedAction.setUtility(-1d);
		} else {
			appliedAction.setUtility(0d);
		}
	}

	public void maxValue(Action appliedAction) {
		if (gameFinished() == Winner.NOT_FINISHED) {
			Action bestAction = null;

			List<Action> actions = Action.getActions(x, y, 1, gameBoard);
			for (Action a : actions) {
				gameBoard = a.apply(gameBoard);
				maxValue(a);
				if (bestAction == null
						|| bestAction.getUtility() < a.getUtility()) {
					bestAction = a;
				}
				gameBoard = a.undo(gameBoard);
			}

		} else if (gameFinished() == Winner.PLAYER1) {
			appliedAction.setUtility(1d);
			;
		} else if (gameFinished() == Winner.PLAYER2) {
			appliedAction.setUtility(-1d);
		} else {
			appliedAction.setUtility(0d);
		}
	}
	
	private double utility(){
		
		
		
		
		
		return 0d;
	}

	private void printGameboard() {
		for (int i = y - 1; 0 <= i; i--) {
			for (int j = 0; j < x; j++) {
				System.out.print(gameBoard[j][i] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

}
