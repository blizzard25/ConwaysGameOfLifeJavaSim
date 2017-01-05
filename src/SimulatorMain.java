import java.util.*;

public class SimulatorMain {

	//scanner for user input
	static Scanner in = new Scanner(System.in);
	
	public static void main(String[] args) {
		//get user population input
	    int[][] population = fillPopulation();
	    //get user generation cycle input
	    int generations = fillGenerationCycles();
	    //print the result
	    System.out.print("\nSimulated population matrix = " + Arrays.deepToString(getGeneration(population, generations)));
	}
	
    static int[][] getGeneration(int[][] cells, int generations) {
    	//cells for the current generation
        Cells generation = new Cells(cells);
        //get all generations using an input of the previous generation
        for (int i = 0; i < generations; i++) generation = generation.getNextGeneration();
        return generation.cells;
    }
    
    static int[][] fillPopulation() { 
    	//3x3 matrix of user input
        int[][] population = new int[3][3];
        //instructions
        System.out.println("Conway's Game of Life is a 0 player simulation game. The game involves a matrix of" +
        		"\ncells which are either 'alive' or 'dead'. You, the user, will provide the population input for this matrix," +
        		"\nas well as the number of generation cycles the program will run. Since the game is 0 player, you only have to provide initial" +
        		"\ninput and the program does the rest. For an explanation of the rules of Conway's Game of Life, see the link below:" +
        		"\nhttps://en.wikipedia.org/wiki/Conway's_Game_of_Life\n\nThe population will be limited to a 3 row, 3 column matrix" +
        		"Enter your 3 by 3 population matrax for population[row][column]\nPlease only enter a value of '0' (dead cell) or '1' (live cell)\n");
        //get user input for the 2D population matrix
        for(int row = 0; row < 3; row++) { 
        	for(int col = 0 ;col < 3; col++) { 
        		if(col == 0) {
        			System.out.print(row == 0 ? "population[0][0] = " : row == 1 ? "population[1][0] = " : "population[2][0] = "); 
        		} else if(col == 1) {
        			System.out.print(row == 0 ? "population[0][1] = " : row == 1 ? "population[1][1] = " : "population[2][1] = ");
        		} else {
        			System.out.print(row == 0 ? "population[0][2] = " : row == 1 ? "population[1][2] = " : "population[2][2] = ");
        		}
        		double enteredValue = in.nextDouble();
        		//check if user input isn't 0 or 1
        		if(enteredValue != 0 && enteredValue != 1 && enteredValue != (int)enteredValue) {
        			while(true) {
        				System.out.println("\nError in entered value : Please only enter a value of '0' or '1'");
        				enteredValue = in.nextDouble();
        				if((enteredValue == 0 || enteredValue == 1) & enteredValue == (int)enteredValue) break;
        			}
        		}
        		population[row][col] = (int)enteredValue;
        		//print newline at the end of column 2
        		System.out.print(col == 2 ? "\n" : "");
        	} 
        } 
        return population; 
	}
    
    static int fillGenerationCycles() {
    	System.out.println("Enter the number of generation cycles\nPlease enter a value greater than or equal to '0'");
    	double gen = in.nextDouble();
    	//check if user input is not an integer or isn't greater than or equal to 0
    	if(gen < 0 && gen != (int)gen) {
    		while(true) {
    			System.out.println("Error in entered value : Please only enter a value greater than or equal to '0'");
    			gen = in.nextDouble();
    			if(gen >= 0 && gen == (int)gen) break;
    		}
    	}
    	//close the scanner
    	in.close();
    	return (int)gen;
    }
    
	private static class Cells {
		
		//2D array for the 2D population of a specific generation
        int [][] cells;

        //constructor for the Cells inner class
        Cells(int[][] cells) { this.cells = cells; }
        
        //set the cell for a given row and column
        void setCell(int x, int y, int value) {
            cells[y][x] = value;
        }

        int getCell(int x, int y) {
            return (y >= 0 && y < cells.length) && (x >= 0 && x < cells[y].length) ? cells[y][x] : 0;
        }

        //get the number of live neighbors
        int getNeighborCount(int x, int y, int result) {
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    //if (!(i == x && j == y)) result +=  getCell(i, j);
                	result = (!(i == x && j == y)) ? result += getCell(i, j) : result;
                }
            }
            return result;
        }

        private int[][] getCellArea(int x0, int y0, int x1, int y1) {
            int height = y1 - y0 + 1, width = x1 - x0 + 1;
            Cells cellsArea = new Cells(new int[height][width]);
            for (int x = x0; x <= x1; x++) {
                for (int y = y0; y <= y1; y++) cellsArea.setCell(x - x0, y - y0, getCell(x, y));
            }
            return cellsArea.cells;
        }

        void crop() {
            int minX = cells.length, maxX = 0, minY = cells[0].length, maxY = 0;
            for(int i = 0; i < cells.length; i++) {
                for(int j = 0; j < cells[0].length; j++) {
                    if (cells[i][j] == 1) {
                        if(minX > j) minX = j;
                        if(minY > i) minY = i;
                        if(maxX < j) maxX = j;
                        if(maxY < i) maxY = i;
                    }
                }
            }
            this.cells = (minX > 0 || maxX < cells.length - 1 || minY > 0 || maxY < cells[0].length - 1) ? this.cells = getCellArea(minX, minY, maxX, maxY) : this.cells;
        }

        //get the next generation of cells based on the Game of Life rules for generation output
        Cells getNextGeneration() {
            Cells nextGeneration = new Cells(new int[cells.length + 2][cells[0].length + 2]);
            for(int y = 0; y < nextGeneration.cells.length; y++) {
                for(int x = 0; x < nextGeneration.cells[0].length; x++) {
                	nextGeneration.setCell(x, y, getNeighborCount(x - 1, y - 1, 0) == 2 ? getCell(x - 1, y - 1) : getNeighborCount(x - 1, y - 1, 0) == 3 ? 1 : 0);
                }
            }
            nextGeneration.crop();
            return nextGeneration;
        }
        
    }
    
}
