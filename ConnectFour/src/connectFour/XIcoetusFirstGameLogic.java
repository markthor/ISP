package connectFour;

public class XIcoetusFirstGameLogic implements IGameLogic {
    private int x = 0;
    private int y = 0;
    private int playerID;
    private int[][] gameboard;
    
    public XIcoetusFirstGameLogic() {
        //TODO Write your implementation for this method
    }
	
    public void initializeGame(int x, int y, int playerID) {
        this.x = x;
        this.y = y;
        this.playerID = playerID;
        gameboard = new int[x][y];
        //TODO Write your implementation for this method
    }
	
    public Winner gameFinished() {
        //TODO Write your implementation for this method
        return Winner.NOT_FINISHED;
    }


    public void insertCoin(int column, int playerID) {
        
    }

    public int decideNextMove() {
        //TODO Write your implementation for this method
        return 0;
    }

}
