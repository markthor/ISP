package connectFour;

import java.util.List;

public class XIcoetusFirstGameLogic implements IGameLogic {
	private int columns = 0;
    private int rows = 0;
    private int playerID;
    private int[][] gameBoard;
    private int[] nextCoinPos;
    
    //The maximum amount of adjacent 4 connects in one diagonal. 
    private int diaLength;
    //Base pointers for diagonal arrays.
    private int leftAnchorPointer;
    private int rightAnchorPointer;
    
    //test pointer for easy bot
    //private int nextMove;
    
    public XIcoetusFirstGameLogic() {
        //TODO Write your implementation for this method
    }

    public void initializeGame(int columns, int rows, int playerID) {
        this.columns = columns;
        this.rows = rows;
        this.playerID = playerID;
        gameBoard = new int[columns][rows];
        nextCoinPos = new int[columns];
        /*for (int i = 0; i < x; i++){
        	nextCoinPos[i] = y-1;
        }*/
        
        diaLength = (columns-3+rows-3)-1;
        leftAnchorPointer = rows - 4;
        rightAnchorPointer = columns - 4;
        //test pointer for easy bot
        //nextMove = columns-1;
        //TODO Write your implementation for this method
    }

    public Winner gameFinished() {
    	int[] colCount = new int [columns];
    	int[] rowCount = new int[rows];
    	int[] leftDiaCount = new int[diaLength];
    	int[] rightDiaCount = new int[diaLength];
    	int diaArrayPos;
        for(int i = 0; i < columns; i++) {
        	for(int j = 0; j < rows; j++) {
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
        		diaArrayPos = rightAnchorPointer-((columns-1)-i)+j;
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
        if(tieCheck()){
        	return Winner.TIE;
        }
        
        return Winner.NOT_FINISHED;
    }
    
    private boolean tieCheck(){
    	int filled = 0;
    	for(int i = 0; i < columns; i++){
    		if(gameBoard[i][rows-1] != 0) filled++;
    	}
    	return filled == columns;
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
		return bestAction.getColumn();
	}

	private void minValue(Action appliedAction, int depth) {
		if (gameFinished() == Winner.NOT_FINISHED) {
			Action bestAction = null;

			List<Action> actions = Action.getActions(columns, rows, 2, gameBoard);
			for (Action a : actions) {
				gameBoard = a.apply(gameBoard);
				maxValue(a, depth+1);
				if (bestAction == null
						|| a.getUtility() < bestAction.getUtility()) {
					bestAction = a;
				}
				gameBoard = a.undo(gameBoard);
			}
			appliedAction.setUtility(bestAction.getUtility());
		} else if (gameFinished() == Winner.PLAYER1) {
			appliedAction.setUtility(1d/depth);
		} else if (gameFinished() == Winner.PLAYER2) {
			appliedAction.setUtility(-1d/depth);
		} else {
			appliedAction.setUtility(0d);
		}
		
	}

	public void maxValue(Action appliedAction, int depth) {
		if (gameFinished() == Winner.NOT_FINISHED) {
			Action bestAction = null;

			List<Action> actions = Action.getActions(columns, rows, 1, gameBoard);
			for (Action a : actions) {
				gameBoard = a.apply(gameBoard);
				minValue(a, depth+1);
				if (bestAction == null
						|| bestAction.getUtility() < a.getUtility()) {
					bestAction = a;
				}
				gameBoard = a.undo(gameBoard);
			}
			appliedAction.setUtility(bestAction.getUtility());
		} else if (gameFinished() == Winner.PLAYER1) {
			appliedAction.setUtility(1d/depth);
		} else if (gameFinished() == Winner.PLAYER2) {
			appliedAction.setUtility(-1d/depth);
		} else {
			appliedAction.setUtility(0d);
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
