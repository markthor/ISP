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
				}
			}
		}
		
		//Place a queen automatically if there is only one tile left in the row
		int lastAvailableSpot = 0;
		int numberOfRestrictions = 0;
		for(int j = 0; j < columns; j++) {
			for(int i = 0; i < rows; i++) {
				if(board[i][j] == -1) {
					numberOfRestrictions = 1 + numberOfRestrictions;
				} else {
					lastAvailableSpot = i;
				}
			}
			if(numberOfRestrictions == columns-1) {
				insertQueen(lastAvailableSpot, j);
			}
			numberOfRestrictions = 0;
		}
		
		return true;
	}
	
	/**
	 * Creates the BDD of the predicate that represents the solution to the N-queen problem.
	 * @return A BDD of the predicate that represents the solution to the N-queen problem.
	 */
    private BDD createCompleteBDD() {
    	BDDFactory bddFactory = JFactory.init(numberOfNodes, cacheSize);
        bddFactory.setVarNum(columns*rows);
        
        BDD lastNode = bddFactory.one();
        BDD currentNode = null;
        
        //For each tile on the NxN board.
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
		        for(int i = -1, j = 1; 0 <= column+i && row+j < rows; i--, j++) {
		        	currentNode.andWith(bddFactory.nithVar(chessBoardIndexToVar(column+i, row+j)));
		        }
		        for(int i = 1, j = -1; column+i < columns && 0 <= row+j; i++, j--) {
		        	currentNode.andWith(bddFactory.nithVar(chessBoardIndexToVar(column+i, row+j)));
		        }
                currentNode = bddFactory.ithVar(chessBoardIndexToVar(column, row)).imp(currentNode);
                lastNode.andWith(currentNode);
        	}
        }
        // The expression that checks that all N queens has been placed.
        BDD eightQueensPlacedLastNode = bddFactory.one();
        for(int column = 0; column < columns; column++) {
            BDD eightQueensPlaced = bddFactory.zero();
        	for(int row = 0; row < rows; row++) {
        		eightQueensPlaced.orWith(bddFactory.ithVar(chessBoardIndexToVar(column, row)));
        	}
            eightQueensPlacedLastNode.andWith(eightQueensPlaced);
        }
        
        lastNode.andWith(eightQueensPlacedLastNode);
        
        return lastNode;
    }
    
    /**
     * Converts a tile coordinate pair to the BDD boolean variable that represents the tile.
     * @param i The row coordinate.
     * @param j The column coordinate.
     * @return The BDD variable number.
     */
    private int chessBoardIndexToVar(int i, int j) {
        return i + j*rows;
    }
}
