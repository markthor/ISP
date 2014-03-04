package connectFour;

public class Utility {
	int columns, rows, diaLength, leftAnchorPoint, rightAnchorPoint;
	
	//Arrays used to count consecutive zeroes
	int[] colZeroCount, rowZeroCount, leftDiaZeroCount, rightDiaZeroCount;
	
	public Utility(int columns, int rows){
		this.columns = columns;
		this.rows = rows;
		diaLength = columns-3+rows-3-1;
		leftAnchorPoint = rows-4;
		rightAnchorPoint = columns -4;
		
		
		colZeroCount = new int [columns];
		rowZeroCount = new int[rows];
		leftDiaZeroCount = new int[diaLength];
		rightDiaZeroCount = new int[diaLength];
	}
	
	
	public double utility(int[][] gameBoard){
		int[] colCount = new int [columns];
    	int[] rowCount = new int[rows];
    	int[] leftDiaCount = new int[diaLength];
    	int[] rightDiaCount = new int[diaLength];
    	int diaArrayPos;
    	
    	double utility = 0;
    	final double twoRow = 30;
    	final double threeRow = 75;
    	final double fourRow = 1000;
    	
    	
        for(int i = 0; i < columns; i++) {
        	for(int j = 0; j < rows; j++) {
        		colCount[i] = updateCellCount(gameBoard[i][j], colCount[i]);
        		if(colCount[i] == 4) {
        			utility += fourRow;
        		}
        		if(colCount[i] == -4) {
        			utility -= fourRow;
        		}
        		rowCount[j] = updateCellCount(gameBoard[i][j], rowCount[j]);
        		if(rowCount[j] == 4) {
        			utility += fourRow;
        		}
        		if(rowCount[j] == -4) {
        			utility -= fourRow;
        		}
        		diaArrayPos = leftAnchorPointer-j+i;
        		if(diaArrayPos >= 0 && diaArrayPos < diaLength) {
        			leftDiaCount[diaArrayPos] = updateCellCount(gameBoard[i][j], leftDiaCount[diaArrayPos]);
            		if(leftDiaCount[diaArrayPos] == 4) {
            			utility += fourRow;
            		}
            		if(leftDiaCount[diaArrayPos] == -4) {
            			utility -= fourRow;
            		}
        		}
        		diaArrayPos = rightAnchorPointer-((columns-1)-i)+j;
        		if(diaArrayPos >= 0 && diaArrayPos < diaLength) {
        			rightDiaCount[diaArrayPos] = updateCellCount(gameBoard[i][j], rightDiaCount[diaArrayPos]);
            		if(rightDiaCount[diaArrayPos] == +4) {
            			utility += fourRow;
            		}
            		if(rightDiaCount[diaArrayPos] == -4) {
            			utility -= fourRow;
            		}
        		}
        	}
        }
        /*if(tieCheck()){
        	return Winner.TIE;
        }*/
		
		
		
		
		return 0;
	}
	
	private int updateCellCount(int current, int count) {
    	if(count == 0) {
    		if(current == 1) {
    			return 1;
    		}
    		else if(current == 2) {
    			return -1;
    		}
    		//count zero arrays
    	} else {
    		if(count < 0) {
        		if(current == 1) {
        			return 1;        			
        		}
        		else if(current == 2) {
        			return count-1;
        		}
    		}
    		if(count > 0) {
        		if(current == 1) {
        			return count+1;
        		}
        		else if(current == 2) {
        			return -1;
        		}
    		}
    		
    	}
    	return 0;
    }
}
