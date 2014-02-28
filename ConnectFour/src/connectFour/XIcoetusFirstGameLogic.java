package connectFour;

public class XIcoetusFirstGameLogic implements IGameLogic {
    private int x = 0;
    private int y = 0;
    private int playerID;
    private int[][] gameboard;
    private int[] nextCoinPos;
    
    public XIcoetusFirstGameLogic() {
        //TODO Write your implementation for this method
    }
	
    public void initializeGame(int x, int y, int playerID) {
        this.x = x;
        this.y = y;
        this.playerID = playerID;
        gameboard = new int[x][y];
        nextCoinPos = new int[x];
        //TODO Write your implementation for this method
    }
	
    public Winner gameFinished() {
        //TODO Write your implementation for this method
        return Winner.NOT_FINISHED;
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
