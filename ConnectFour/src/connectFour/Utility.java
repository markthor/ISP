package connectFour;

import connectFour.IGameLogic.Winner;

public class Utility {
	int columns, rows, diaLength, leftAnchorPointer, rightAnchorPointer;
	
	//General arrays
	int[] colCount, rowCount, rowSndCount, leftDiaCount, rightDiaCount;
	
	//Arrays used to count consecutive zeroes
	int[] colZeroCount, rowZeroCount, rowSndZeroCount, leftDiaZeroCount, rightDiaZeroCount;
	
	public Utility(int columns, int rows){
		this.columns = columns;
		this.rows = rows;
		diaLength = columns-3+rows-3-1;
		leftAnchorPointer = rows-4;
		rightAnchorPointer = columns -4;
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
    	
    	int[] colRecentDisjointCount = new int [columns];
    	int[] rowRecentDisjointCount = new int[rows];
    	int[] leftDiaRecentDisjointCount = new int[diaLength];
    	int[] rightDiaRecentDisjointCount = new int[diaLength];  	
    	
    	double utility = 0;
    	final double oneRow = 5;
    	final double twoRow = 30;
    	final double threeRow = 75;
    	final double fourRow = 1000;
    	int[] updatedCounts;

        for(int i = 0; i < columns; i++) {
        	for(int j = 0; j < rows; j++) {
        		updatedCounts = updateCounts(gameBoard[i][j], colCount[i], colZeroCount[i], colRecentZeroCount[i], colRecentDisjointCount[i]);
        		colCount[i] = updatedCounts[0]; 
        		colZeroCount[i] = updatedCounts[1]; 
        		colRecentZeroCount[i] = updatedCounts[2]; 
        		colRecentDisjointCount[i] = updatedCounts[3];
        		
        		if(3 < Math.abs(colCount[i]) + colZeroCount[i] + Math.abs(colRecentDisjointCount[i]) && colRecentZeroCount[i] < 4) {
        			switch(colCount[i]) {
        				case 1: utility += oneRow; break;
        				case 2: utility += twoRow; break;
        				case 3: utility += threeRow; break;
        				case 4: utility += fourRow; break;
        				case -1: utility -= oneRow; break;
        				case -2: utility -= twoRow; break;
        				case -3: utility -= threeRow; break;
        				case -4: utility -= fourRow; break;
        			}
        			if(colRecentDisjointCount[i] != 0) {
	        			switch(colRecentDisjointCount[i]) {
	    					case 1: utility += oneRow; break;
	    					case 2: utility += twoRow; break;
	    					case 3: utility += threeRow; break;
	    					case 4: utility += fourRow; break;
	    					case -1: utility -= oneRow; break;
	    					case -2: utility -= twoRow; break;
	    					case -3: utility -= threeRow; break;
	    					case -4: utility -= fourRow; break;
	        			}
	        			colZeroCount[i] = Math.abs(colRecentDisjointCount[i]);
	        			colRecentDisjointCount[i] = 0;
        			}
        		}
        	}
        }
        
        return utility;
    }
    
    private int[] updateCounts(int current, int count, int zeroCount, int recentZeroCount, int recentDisjointCount) {
    	int[] result = new int[4];
    	if(current == 0) {
    		zeroCount = zeroCount + 1;
    		recentZeroCount = recentZeroCount + 1;
    	}
    	
    	if(current == 1) {
    		if(count < 0) {
    			//Finds a coin from same player
    			if(0 < recentZeroCount) {
    				if(recentZeroCount < 3) {
    					recentDisjointCount = count;
    				}
    				count = -1;
    			} else {
    				count = count - 1;
    			}
				recentZeroCount = 0;
    		} else if(0 < count) {
    			//Finds a coin from other player
    			count = -1;
    			zeroCount = recentZeroCount;
    			recentZeroCount = 0;
    			recentDisjointCount = 0;
    		} else {
    			recentZeroCount = 0;
    			count = -1;
    		}
    	}
    	
    	if(current == 2) {
    		if(0 < count) {
    			//Finds a coin from same player
    			if(0 < recentZeroCount) {
    				recentDisjointCount = count;
    				count = 1;
    			} else {
    				count = count + 1;
    			}
				recentZeroCount = 0;
    		} else if(count < 0) {
    			//Finds a coin from other player
    			count = 1;
    			zeroCount = recentZeroCount;
    			recentZeroCount = 0;
    			recentDisjointCount = 0;
    		} else {
    			recentZeroCount = 0;
    			count = 1;
    		}
    	}
    	result[0] = count;
    	result[1] = zeroCount;
    	result[2] = recentZeroCount;
    	result[3] = recentDisjointCount;
    	return result;
    	
    }
}
