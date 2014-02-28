package connectFour;

public class XIcoetusFirstGameLogic implements IGameLogic {
    private int x = 0;
    private int y = 0;
    private int playerID;
    private int[][] gameboard;
    private int[] nextCoinPos;
    //The maximum amount of adjacent 4 connects in one diagonal. 
    private int diaLength;
    
    public XIcoetusFirstGameLogic() {
        //TODO Write your implementation for this method
    }
	
    public void initializeGame(int x, int y, int playerID) {
        this.x = x;
        this.y = y;
        this.playerID = playerID;
        gameboard = new int[x][y];
        nextCoinPos = new int[x];
        diaLength = (x-3+y-3)-1;
        //TODO Write your implementation for this method
    }
	
    public Winner gameFinished() {
    	int[] colCount = new int [y];
    	int[] rowCount = new int[x];
    	int[] leftDiaCount = new int[diaLength];
    	int[] rightDiaCount = new int[diaLength];
        for(int i = 0; i < gameboard.length-1; i++) {
        	for(int j = 0; j < gameboard[0].length-1; j++) {
        		colCount[i] = updateCellCount(gameboard[i][j], colCount[i]);
        		if(colCount[i] == -4) {
        			return Winner.PLAYER1;
        		}
        		if(colCount[i] == 4) {
        			return Winner.PLAYER2;
        		}
        		rowCount[j] = updateCellCount(gameboard[i][j], colCount[j]);
        		if(rowCount[j] == -4) {
        			return Winner.PLAYER1;
        		}
        		if(rowCount[j] == 4) {
        			return Winner.PLAYER2;
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
    	gameboard[column][nextCoinPos[column]] = playerID;
    	nextCoinPos[column]++;
    	printGameboard();
    }

    public int decideNextMove() {
        //TODO Write your implementation for this method
        return 0;
    }
    
    private void printGameboard() {
    	for(int i = y-1; 0 <= i; i--) {
    		for(int j = 0; j < x; j++) {
    			System.out.print(gameboard[j][i] + " ");
    		}
    		System.out.println();
    	}
		System.out.println();
    }

}
