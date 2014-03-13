package connectFour;

public class UtilityTwo implements IUtility {
	private int columns, rows, diaLength, leftAnchorPointer, rightAnchorPointer, maxColZones, maxRowZones;
	
	public UtilityTwo(int columns, int rows) {
		this.columns = columns;
		this.rows = rows;
		diaLength = columns - 3 + rows - 3 - 1;
		leftAnchorPointer = rows - 4;
		rightAnchorPointer = columns - 4;
		maxColZones = (columns/4 + 1) * 3;
		maxRowZones = (rows/4 + 1) * 3;
	}
	
	public double utility(int[][] gameBoard) {
		int[][] colLegalZone = new int[columns][maxColZones];
		int[] colNextZonePointer = new int[columns];
		int[] colRecentZeroCount = new int[columns];
		int[] colZoneCount = new int[columns];
		int[] colPlayerZone = new int[columns];
		int[] colPreviousCount = new int[columns];
		
		// Find legal zones.
		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < rows; j++) {
				updateCounts(i, gameBoard[i][j], colRecentZeroCount, colZoneCount, colPlayerZone, colPreviousCount);
				setLegalZones(i, colLegalZone, colRecentZeroCount, colZoneCount, colPlayerZone, colPreviousCount, colNextZonePointer);
			}
		}
		// Set utility
		for (int i = 0; i < columns; i++) {
			
		}
		return 0;
	}
	
	private void updateCounts(int index, int cellValue, int[] recentZeroCount, int[] zoneCount, int[] playerZone, int[] colPreviousCount) {
		if(cellValue == 0) {
			recentZeroCount[index] += 1;
		} else if(cellValue == 1) {
			if(playerZone[index] == 2) {
				colPreviousCount[index] += zoneCount[index];
				zoneCount[index] = 1 + recentZeroCount[index];
			} else {
				zoneCount[index] += 1 + recentZeroCount[index];
			}
			playerZone[index] = 1;
			recentZeroCount[index] = 0;
		} else if(cellValue == 2) {
			
			if(playerZone[index] == 1) {
				zoneCount[index] = 1 + recentZeroCount[index];
			} else {
				zoneCount[index] += 1 + recentZeroCount[index];
			}			
			playerZone[index] = 2;
			recentZeroCount[index] = 0;
		}
	}
	
	private void setLegalZones(int index, int[][] legalZone, int[] recentZeroCount, int[] zoneCount, int[] playerZone, int[] previousCount, int[] nextZonePointer) {
		// If zone is larger than 3 register it as legal zone.
		if(3 < zoneCount[index] + recentZeroCount[index] && 0 < zoneCount[index]) {
			if(nextZonePointer[index] == 0 || legalZone[index][nextZonePointer[index]-3] != playerZone[index]) {
				// Create a new zone
				legalZone[index][nextZonePointer[index]] = playerZone[index];
				legalZone[index][nextZonePointer[index]+1] = previousCount[index];
				legalZone[index][nextZonePointer[index]+2] = previousCount[index] + zoneCount[index] + recentZeroCount[index] - 1;
				nextZonePointer[index] += nextZonePointer[index] + 3;				
			} else {
				legalZone[index][nextZonePointer[index]-1] += 1;
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
