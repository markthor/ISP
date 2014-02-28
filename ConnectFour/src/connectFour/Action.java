package connectFour;

import java.util.ArrayList;
import java.util.List;

public class Action {
	int column, row, playerID;
	double utility = 0d;
	
	public Action(int column, int row, int playerID){
		this.column = column;
		this.row = row;
		this.playerID = playerID;
	}
	
	/**
	 * Method for getting all legal actions from a given gameboard
	 * @param columns The number of columns
	 * @param rows The number of rows
	 * @param playerID The id of the player making the move
	 * @param gameBoard The current gameboard
	 * @return A list of all legal actions
	 */
	public static List<Action> getActions(int columns, int rows, int playerID, int[][] gameBoard) {
		List<Action> actions = new ArrayList<Action>();
		for(int i = 0; i < columns; i++) {
			for(int j = rows-1; 0 <= j; j--) {
				if(gameBoard[i][j] == 0) {
					actions.add(new Action(i, j, playerID));
				}
			}
		}
		return actions;
	}
	
	public int[][] apply(int[][] gameBoard){
		gameBoard[column][row] = playerID;
		return gameBoard;
	}
	
	public int[][] undo(int[][] gameBoard){
		gameBoard[column][row] = 0;
		return gameBoard;
	}

	/**
	 * @return the utility
	 */
	public double getUtility() {
		return utility;
	}

	/**
	 * @param utility the utility to set
	 */
	public void setUtility(double utility) {
		this.utility = utility;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @return the playerID
	 */
	public int getPlayerID() {
		return playerID;
	}
}