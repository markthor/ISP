/**
 * This class implements the logic behind the BDD for the n-queens problem
 * You should implement all the missing methods
 * 
 * @author Stavros Amanatidis
 *
 */

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.JFactory;

public class QueensLogic {
	private int columns = 0;
	private int rows = 0;
	private int[][] board;
	private final int numberOfNodes = 2000000;
	private final int cacheSize = numberOfNodes / 10;
	BDDFactory bddFactory;
    private BDD eightQueenBDD;

	public QueensLogic() {
		// constructor
	}

	public int[][] getGameBoard() {
		return board;
	}
    
	public void initializeGame(int size) {
        this.columns = size;
        this.rows = size;
        this.board = new int[columns][rows];
        
        bddFactory = JFactory.init(numberOfNodes, cacheSize);
        bddFactory.setVarNum(columns*rows);
        
        eightQueenBDD = createCompleteBDD();
        
        System.out.println("There exists " + (int)eightQueenBDD.satCount() + " solution(s).");
    }

	public boolean insertQueen(int column, int row) {

		if (board[column][row] == -1 || board[column][row] == 1) {
			return true;
		}

		board[column][row] = 1;
		// Restrict that nigga
		eightQueenBDD.restrictWith(bddFactory.ithVar(chessBoardIndexToVar(column, row)));
		
		BDD temporaryBDD;
		for(int i = 0; i < columns; i++) {
			for(int j = 0; j < rows; j++) {
				//Restrict the BDD if there is a queen
				if(board[i][j] == 0) {
					temporaryBDD = eightQueenBDD.restrict(bddFactory.ithVar(chessBoardIndexToVar(i, j)));
					if(temporaryBDD.satCount() == 0) {
						board[i][j] = -1;
					}
					temporaryBDD = eightQueenBDD;
				}
			}
		}
		
		
		System.out.println("There exists " + (int)eightQueenBDD.satCount() + " solution(s).");
		
		return true;
	}

    private BDD createCompleteBDD() {
    	BDDFactory bddFactory = JFactory.init(numberOfNodes, cacheSize);
        bddFactory.setVarNum(columns*rows);
        
        BDD lastNode = bddFactory.one();
        BDD currentNode = null;
        
        for(int column = 0; column < columns; column++) {
        	for(int row = 0; row < rows; row++) {
                currentNode = bddFactory.one();
		        //Check the row
		        for(int i = 0; i < columns; i++) {
		        	if(i != column) {
		        		currentNode.andWith(bddFactory.nithVar(chessBoardIndexToVar(i, row)));
		        	}
		        }
		        
		        //Check the column
		        for(int i = 0; i < rows; i++) {
		        	if(i != row) {
		        		currentNode.andWith(bddFactory.nithVar(chessBoardIndexToVar(column, i)));
		        	}
		        }
		        
		        //Check the NW to SE diagonal
		        for(int i = (column - Math.min(column, row)), j = (row - Math.min(column, row)); i < columns && j < rows; i++, j++) {
		        	if(i != column && j != row) {
		        		currentNode.andWith(bddFactory.nithVar(chessBoardIndexToVar(i, j)));
		        	}
		        }
		        
		        //Check the SW to NE diagonal
		        int difference = column;
		        if(((columns-1) / 2) < column || ((rows-1) / 2) < row) {
		        	difference = (rows - 1) - row;
		        }
		        for(int i = -difference, j = difference; column+i < columns && 0 <= row+j; i++, j--) {
		    		if(i != 0 && j != 0) {
		    			currentNode.andWith(bddFactory.nithVar(chessBoardIndexToVar(column+i, row+j)));
		    		}
		    	}
                currentNode = bddFactory.ithVar(chessBoardIndexToVar(column, row)).imp(currentNode);
                lastNode = lastNode.and(currentNode);
        	}
        }
        
        BDD eightQueensPlacedLastNode = bddFactory.one();
        for(int column = 0; column < columns; column++) {
            BDD eightQueensPlaced = bddFactory.zero();
        	for(int row = 0; row < rows; row++) {
        		eightQueensPlaced = eightQueensPlaced.or(bddFactory.ithVar(chessBoardIndexToVar(column, row)));
        	}
            eightQueensPlacedLastNode = eightQueensPlacedLastNode.and(eightQueensPlaced);
        }
        
        currentNode = lastNode.and(eightQueensPlacedLastNode);
        
        return currentNode;
    }
    private int chessBoardIndexToVar(int i, int j) {
        return i + j*rows;
    }
}
