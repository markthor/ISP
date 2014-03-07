package connectFour;

import connectFour.IGameLogic.Winner;

public class Utility {
	private int columns, rows, diaLength, leftAnchorPointer, rightAnchorPointer;

	// General arrays
	private int[] colCount, rowCount, rowSndCount, leftDiaCount, rightDiaCount;

	// Arrays used to count consecutive zeroes
	private int[] colZeroCount, rowZeroCount, rowSndZeroCount, leftDiaZeroCount,
			rightDiaZeroCount;
	
	private final double oneRow = 5, twoRow = 30, threeRow = 75, fourRow = 1000;

	public Utility(int columns, int rows) {
		this.columns = columns;
		this.rows = rows;
		diaLength = columns - 3 + rows - 3 - 1;
		leftAnchorPointer = rows - 4;
		rightAnchorPointer = columns - 4;
	}
	
    public double getUtility(int[][] gameBoard) {
    	int[] colCount = new int [columns];
    	int[] rowCount = new int[rows];
    	int[] leftDiaCount = new int[diaLength];
    	int[] rightDiaCount = new int[diaLength];
    	int diaArrayPos;
    	
    	int[] colZeroCount = new int [columns];
    	int[] rowZeroCount = new int[rows];
    	int[] leftDiaZeroCount = new int[diaLength];
    	int[] rightDiaZeroCount = new int[diaLength];
    	
    	int[] colRecentZeroCount = new int [columns];
    	int[] rowRecentZeroCount = new int[rows];
    	int[] leftDiaRecentZeroCount = new int[diaLength];
    	int[] rightDiaRecentZeroCount = new int[diaLength];
    	
    	double utility = 0;
    	final double twoRow = 30;
    	final double threeRow = 75;
    	final double fourRow = 1000;
    	int[] updatedCounts;

        for(int i = 0; i < columns; i++) {
        	for(int j = 0; j < rows; j++) {
        		updatedCounts = updateCounts(gameBoard[i][j], colCount[i], colZeroCount[i], colRecentZeroCount[i]);
        		colCount[i] = updatedCounts[0]; 
        		colZeroCount[i] = updatedCounts[1]; 
        		colRecentZeroCount[i] = updatedCounts[2]; 
        		
        		if(3 < colCount[i] + colZeroCount[i]) {
        			if(colCount[i] == 1) {
        				
        			}
        		}
        			
        		updatedCounts = updateCounts(gameBoard[i][j], rowCount[j], rowZeroCount[j], rowRecentZeroCount[j]);
        		rowCount[j] = updatedCounts[0]; 
        		rowZeroCount[j] = updatedCounts[1]; 
        		rowRecentZeroCount[j] = updatedCounts[2];
        		
        		
        		
        		diaArrayPos = leftAnchorPointer-j+i;
        		if(diaArrayPos >= 0 && diaArrayPos < diaLength) {

        		}
        		diaArrayPos = rightAnchorPointer-((columns-1)-i)+j;
        		if(diaArrayPos >= 0 && diaArrayPos < diaLength) {

        	}
        	}
        }
        
        return utility;
    }
    
    private int[] updateCounts(int current, int count, int zeroCount, int recentZeroCount) {
    	int[] result = new int[3];
    	if(current == 0) {
    		zeroCount = zeroCount + 1;
    		recentZeroCount = zeroCount + 1;
    	}
    	
    	if(current == 1) {
    		if(count < 0) {
    			//Finds a coin from same player
    			if(0 < recentZeroCount) {
    				count = -1;
    			} else {
    				count = count - 1;
    			}
				recentZeroCount = 0;
    		} else if(0 < count) {
    			//Finds a coin from other player
    			count = 1;
    			zeroCount = recentZeroCount;
    			recentZeroCount = 0;
    		} else {
    			recentZeroCount = 0;
    			count = -1;
    		}
    	}
    	
    	if(current == 2) {
    		if(0 < count) {
    			//Finds a coin from same player
    			if(0 < recentZeroCount) {
    				count = 1;
    			} else {
    				count = count + 1;
    			}
				recentZeroCount = 0;
    		} else if(count < 0) {
    			//Finds a coin from other player
    			count = -1;
    			zeroCount = recentZeroCount;
    			recentZeroCount = 0;
    		} else {
    			recentZeroCount = 0;
    			count = 1;
    		}
    	}
    	result[0] = count;
    	result[1] = zeroCount;
    	result[2] = recentZeroCount;
    	return result;
    	
    }
    
    private int updateCellCount(int current, int count, int zeroCount) {
    	/*if(count == freeConstant) {
    		if(current == 1) {
    			return count - 1;
    		}
    		if(current == 2) {
    			return count + 1;
    		}
    		if(current == 0) {
    			return freeConstant + count;
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
    	return freeConstant;*/
    	return 0;
    }
}
