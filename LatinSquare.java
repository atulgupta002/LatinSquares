//HW4_Atul Gupta


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.FileReader;

/*
 * Utility class to return a pair of values from a function
 * 
 */

class Pair<K, V> {
	K k;
	V v;

	Pair(K key, V val) {
		k = key;
		v = val;
	}
	
	//Function to print the pair.
	public String toString() {
		return k + "," + v;
	}
}

/*
 * Note: an entry of 0 at a certain location (i, j) in the underlying matrix
 * means that the location is empty
 */
public class LatinSquare {

	int dim;
	int[][] content;

	public LatinSquare(int n) {
		dim = n;
		content = new int[n][n];
	}
	/*
	 * Given a pair of (0-based) indices r, c (for row and column) and an entry e,
	 * determine whether placing e >= 1 in the location (r, c) makes the row r
	 * contain two same entries which are >= 1 (which would be a violation of games'
	 * rules). We consider entries e >= 1 because a 0 means that an entry is blank.
	 */
	
	//Function to check if the entry e is already present in the row r.
	boolean rowViolation(int e, int r, int c) {
		
		//Checking each column in the same row
		for(int column = 0; column < dim; column++) {
			if (content[r][column] == e && column != c) {
				return true;
			}
		}
		return false;
	}

	// Analogous definition
	
	//Function to check if the entry e is already present in the column c.
	boolean colViolation(int e, int r, int c) {
		
		//Checking each row for the same column
		for(int row = 0; row < dim; row++) {
			if(content[row][c] == e && row != r) {
				return true;
			}
		}
		return false;
	}

	// Return true when placing e >= 1 in the location (r,c)
	// causes violation either in the column or in the row
	boolean violation(int e, int r, int c) {
		return rowViolation(e, r, c) || colViolation(e, r, c);
	}
	
	//Function to print the Matrix.
	public String toString() {

		String outStr = "";
		
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				outStr += content[i][j] + " ";
			}
			outStr += "\n";
		}
		return outStr;
	}

	/*
	 * Search row-wise from the given coordinates (r, c) rightward and downward for
	 * the next 0 (excluding the entry (r,c). To search the whole matrix use (r, c)
	 * = (-1, -1)
	 */
	//Function to return the next available zero (if any) in the Latin Square object
	public Pair<Integer, Integer> nextZero(int r, int c) {
		
		//When row and/or column is negative, it means the user wants us to start from 0,0
		if (r < 0) {
			r = 0;
		}
		if(c < 0) {
			c = 0;
		}
		
		//Checking current row i.e. changing columns for the same row
		for(int column = c; column < dim; column++) {
			if (content[r][column] == 0) {
				return new Pair<Integer, Integer>(r,column);
			}
		}
		
		//Checking remaining matrix i.e. moving to the next row and checking each cell one by one until a zero is found
		for (int row = r+1; row < dim; row++) {
			for (int column = 0; column < dim; column++) {
				if (content[row][column] == 0) {
					return new Pair<Integer, Integer>(row,column);
				}
			}
		}
		//If no zero is available, return null
		return null;
	}

        
	 //Use recursion to solve the puzzle and print the solution
	//Function to solve the Latin Square object. Uses recursion and backtracking.
	public boolean solve(int row, int col) {
		
		Pair<Integer,Integer> nextZero = nextZero(row,col);
		
		//If there is no 0 to work on, the problem is solved. This is the base case
		if (nextZero == null)
			return true;
		
		//Inserting values from 1 to dim and checking if a viable solution is possible. Else, backtrack.
		for (int value = 1; value <= dim; value++) {
			
			//If the current value does not violate, try to solve the rest of the matrix recursively. If solved, we have the correct value for this
			//	position. If no viable solution is possible (i.e. solve boolean is false), change the value back to 0 and try another number.
			if (!violation(value,nextZero.k,nextZero.v)) {
				content[nextZero.k][nextZero.v] = value;
				
				if (solve(nextZero.k,nextZero.v)) {
					return true;
				}
				//backtracking the current value
				content[nextZero.k][nextZero.v] = 0;
			}
		}
		return false;
	}

        // Read the initial unfilled square from a file
    public static LatinSquare readInputFromFile(String file) throws Exception { // Throws exception when the file is not found
    	String data = "";
    	File fileHandle = new File(file);
    	
    	//Checking if file exists
    	if(!fileHandle.exists()) {
    		throw new Exception("File path is invalid.");
    	}
    	
    	//Reading the file. dim = number of lines in the file
    	int numberOfLines = 0;
    	BufferedReader br = new BufferedReader(new FileReader(fileHandle));
    	
    	while(br.ready()) {
    		data += br.readLine() + " ";
    		numberOfLines++;
    	}
    	br.close();
    	
    	//Creating and populating a new LatinSquare object
    	LatinSquare ls = new LatinSquare(numberOfLines);
    	
    	char[] newData = data.toCharArray();
    	
    	//Ignoring additional spaces from the read data and populating the matrix with numbers. 
    	int s = 0;
    	for (int i = 0; i < numberOfLines; i++) {
    		for (int j = 0; j < numberOfLines; j++) {
    			ls.content[i][j] = Integer.parseInt(String.valueOf(newData[s]));
    			s += 2; //omitting spaces
    		}
    	}
    	
    	//returning the final object
    	return ls;
	}
    
    //Driver code
	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter filepath: ");
		String fileName = scan.next();
		scan.close();
		
		LatinSquare ls;
		
		try {
			ls = readInputFromFile(fileName);
		}catch (Exception e){
			System.out.print(e.getMessage());
			ls = null;
			System.exit(-1);
		}
		System.out.println(ls + "\nSolving: \n");
		ls.solve(-1,-1);
		System.out.println(ls);
	}
}

