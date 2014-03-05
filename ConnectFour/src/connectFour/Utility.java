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
	
	
	public double utility(int[][] gameBoard){
		colCount = new int [columns];
    	rowCount = new int[rows];
    	rowSndCount = new int[rows];
    	leftDiaCount = new int[diaLength];
    	rightDiaCount = new int[diaLength];
    	int diaArrayPos;
    	
    	double utility = 0;
    	final double twoRow = 30;
    	final double threeRow = 75;
    	final double fourRow = 1000;
    	
    	
    	//Zero stuff
    	colZeroCount = new int[columns];
		rowZeroCount = new int[rows];
		rowSndZeroCount = new int[columns];
		leftDiaZeroCount = new int[diaLength];
		rightDiaZeroCount = new int[diaLength];
    	
		
		
        for(int i = 0; i < columns; i++) {	//husk at tjekke at de sidste rækker giver mening at lægge i
        	for(int j = 0; j < rows; j++) {
        		
        		//Columns
        		/*if(colCount[i] == 0 && colZeroCount[i] == 0){
        			switch(gameBoard[i][j]){
        				case 0: colZeroCount[i]++;
        						colCount[i] = 0;
        						break;
        				case 1: colCount[i] = 1;
        						break;
        				case 2: colCount[i] = -1;
        						break;
        			}
        		}*/
        		//else if(colCount[i] != 0){
        		
        		//Columns
        			switch(gameBoard[i][j]){
        				case 0: colZeroCount[i]++;
        						break;
        				case 1:	//blue coin
        						if(0 <= colCount[i]){ //same value
        							colCount[i]++;
        						}
        						else {
        							colCount[i] = 1;
        						}
        						break;
        				case 2:	//red coin
        						if(colCount[i] <= 0){ //same value
        							colCount[i]--;
        						}
        						else {
        							colCount[i] = -1;
        						}
        						break;
        			}
        			
        			//rows
        			switch(gameBoard[i][j]){
        				case 0: 
        						//check
        						if(0 < rowCount[j]) {
        							if(rowCount[j] + rowZeroCount[j] == 4){
        								rowCount[j] = 0;
        								//increment utility
        							} else {
        								rowSndCount[j] = rowCount[j];
        								rowCount[j] = 0;
        							}
        							
        						} else if(rowCount[j] < 0) {
        							if(rowCount[j] - rowZeroCount[j] == -4) {
        								rowCount[j] = 0;
        								//decrement utility
        							} else {
        								rowSndCount[j] = rowCount[j];
        								rowCount[j] = 0;
        							}
        						}
        						break;
        				case 1:
        						if(0 <= rowCount[j]) {
        							rowCount[j]++;
        							if(rowCount[j]+rowZeroCount[j] == 3){
        								
        							}
        						}
        						else if(rowCount[j] < 0) { //Switch in coin color
        							if()
        						}
        						else if() {
        							
        						}
        						
        						
        			}
        						
        						
        						
        						
        						
        						
        						
        						
        						//tjek om det er legit
    							if(colCount[i] + colZeroCount[i] >= 4 || legitColumn(gameBoard, i,j,colCount[i],1,4-colCount[i]-colZeroCount[i])){
    								//points based on how good
    								switch(colCount[i]){
									case 1: utility += 1;
											break;
									case 2: utility += 10; // træk værdi for 1 fra
											break;
									case 3: utility += 100; // træk værdi for 2 fra
											break;
									case 4: utility += 1000;
											break;
    								}
    							} else{
    								//træk værdi for colCount[i] fra
    							}
    							colZeroCount[i] = 0;
        						break;
        						
        						
        				case 2: colCount[i] = -1;
		        				if(colCount[i] <= -1){ //same value
									colCount[i]--;
								}
								else {
									colCount[i] = -1;
								}
								
								//tjek om det er legit
								if((colCount[i]*-1) + colZeroCount[i] >= 4 || legitColumn(gameBoard, i,j,(colCount[i]*-1),1,4-(colCount[i]*-1)-colZeroCount[i])){
									//points based on how good
									switch(colCount[i]*-1){
										case 1: utility -= 1;
												break;
										case 2: utility -= 10;
												break;
										case 3: utility -= 100;
												break;
										case 4: utility -= 1000;
												break;
									}
								}
								colZeroCount[i] = 0;
        						break;
        			}
        		//}
        		
        		/*
        		colCount[i] = updateCellCount(gameBoard[i][j], colCount[i], i);
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
        }*/
        /*if(tieCheck()){
        	return Winner.TIE;
        }*/
		
		
		
		
		
        	}
        }//ude af for loops
        return 0;
	}
	
	
	private boolean legitColumn(int[][] gameBoard, int column, int row, int current, int value, int need){
		boolean result = false;
		
		//what was before zeroes before current position? KAN IKKE SKE AT KOLONNER HAR NOGLE ZEROES FØR!!! Men det her kan bruges til at tjekke rækker da det er muligt der
		for(int i = 1; i < 3; i++){//gør så den her kører det antal gange der er realistisk plads
			//check space below
			if(row-i > 0){
				if(gameBoard[column][row-colZeroCount[column]-1] == value){
					need--;
					
					if(need == 0){
						result = true;
						break;
					}
				} 
			}
		}
		
		
		/*if(row-1 >= 0){
			if(need >= 1){
				if(gameBoard[column][row-colZeroCount[column]-1] == value){
					need--;
				}
			}
		}
		if(need > 0 && row-2 >= 0){
			if (need >= 1){
				if(gameBoard[column][row-2] == value){
					need--;
				}
			}
		}
		if(need > 0 && row-3 >= 0){
			if (need >= 1){
				if(gameBoard[column][row-3] == value){
					need--;
				}
			}
		}*/
		
		
		//After current position
		if(need > 0 && row+1 + need <= rows){
			if(need >= 1){
				if(gameBoard[column][row+1] == 0 || gameBoard[column][row+1] == value){
					result = true;
				}
			}
			if (need >= 2){
				if(gameBoard[column][row+2] != 0 && gameBoard[column][row+2] != value){
					result = false;
				}
			}
			if (need == 3){
				if(gameBoard[column][row+3] != 0 && gameBoard[column][row+3] != value){
					result = false;
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param current The value in the current cell in the gameBoard
	 * @param count The current amount of consecutive values
	 * @param zeroArrayPos The position which should be modified in the Zero arrays
	 * @return
	 */
	/*
	private int updateCellCount(int current, int count, int zeroArrayPos) {
		if(count == 0){
			switch(current){
			case 0: 
				
				break;
			case 1: break;
			case 2: break;
		}
		}
		
		
		
		
		
		
		
		
		
		
		switch(count){
			case 0: break;
			case 1: break;
			case 2: break;
			case 3: break;
			default: break;
		}
		return count;
		
		
		
		
		
		
		
		
		
		
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
    }*/
	
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
    	if(count == freeConstant) {
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
    	return freeConstant;
    }
}
