/**
 * This class implements the logic behind the BDD for the n-queens problem
 * You should implement all the missing methods
 * 
 * @author Stavros Amanatidis
 *
 */
import java.util.*;

import net.sf.javabdd.*;

public class QueensLogic {
    private int columns = 0;
    private int rows = 0;
    private int[][] board;
    BDD EightQueenBDD;
    private final int numberOfNodes = 2000000;
    private final int cacheSize = numberOfNodes/10;


    public QueensLogic() {
       //constructor
    }

    public void initializeGame(int size) {
        this.columns = size;
        this.rows = size;
        this.board = new int[columns][rows];
        EightQueenBDD = createCompleteBDD();
        
        if(EightQueenBDD.satCount() > 0) {
        	System.out.println("There exists a solution!" + EightQueenBDD.satCount());
        }
    }

   
    public int[][] getGameBoard() {
        return board;
    }

    public boolean insertQueen(int column, int row) {

        if (board[column][row] == -1 || board[column][row] == 1) {
            return true;
        }
        
        board[column][row] = 1;
        
        // put some logic here..
        
        return true;
    }
    
    private BDD createCompleteBDD() {
    	BDDFactory bddFactory = JFactory.init(numberOfNodes, cacheSize);
        bddFactory.setVarNum(columns*rows);
        
        BDD lastNode = bddFactory.zero();
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
                lastNode = lastNode.or(currentNode);
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
    /*
    private BDD createBDD() {
    	BDDFactory fact = JFactory.init(1000,1000);
    	fact.setVarNum(x*y);
    	int a = 0;
    	int b = 0;
    	int diaStartLeftA, diaStartLeftB, diaStartRightA, diaStartRightB;
    	BDD result = fact.ithVar(chessBoardIndexToVar(a, b));
    	if(a <= b) {
    		diaStartLeftA = 0;
    		diaStartLeftB = b - a;
    	} else {
    		diaStartLeftA = a - b;
    		diaStartLeftB = 0;
    	}
    	
    	
    	
    	//Traversing horizontal direction.
    	int diaI = diaStartLeftA;
    	int diaJ = diaStartLeftB;
    	System.out.println("diaI =" + diaI + "  diaJ =" + diaJ);
    	for(int i = 0; i < x; i++) {
    		if(i != a) {
    			result = result.and(fact.nithVar(chessBoardIndexToVar(i, b)));
    			board[i][b] = -1;
    		}
    		if(i != b) {
    			result = result.and(fact.nithVar(chessBoardIndexToVar(a, i)));
    			board[a][i] = -1;
    		}
    		if(x >= diaI && x >= diaJ && diaI != a && diaJ != b) {
    			result = result.and(fact.nithVar(chessBoardIndexToVar(diaI, diaJ)));
    			board[diaI][diaJ] = -1;
    			System.out.println("diaI =" + diaI + "  diaJ =" + diaJ);
    			diaI = diaI+1;
    			diaJ = diaJ+1;
    		}
    		
    	}
    	return result;
    }
    */
    private int chessBoardIndexToVar(int i, int j) {
        return i + j*rows;
    }
    
    
}
