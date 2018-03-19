/*
Execution:    java Minesweeper m n p

 *  Sample execution:
 *
 *      % java Minesweeper  5 10 0.3
 *      * . . . . . . . . * 
 *      . . . . . . * . . . 
 *      . . . . . . . . * * 
 *      . . . * * * . . * . 
 *      . . . * . . . . . . 
 *
 *      * 1 0 0 0 1 1 1 1 * 
 *      1 1 0 0 0 1 * 2 3 3 
 *      0 0 1 2 3 3 2 3 * * 
 *      0 0 2 * * * 1 2 * 3 
 *      0 0 2 * 4 2 1 1 1 1 
 
*/
public class Minesweeper{
	public static void main(String[] args){
		//zmienne
		int m = Integer.parseInt(args[0]);
		int n = Integer.parseInt(args[1]);
		double p = Double.parseDouble(args[2]);
		
		//losowanie bomb [1..m][1..n]
		boolean[][] bombs= new boolean[m+2][n+2]; //byla podpowiedz - border is used to handle boundary cases 
		for(int i=1; i<=m; i++)
			for(int j=1; j<=n; j++)
				bombs[i][j] = (Math.random() <p);
	
		//wyswietl
		for(int i=1; i<=m; i++){
			for(int j=1; j<=n; j++){
				if(bombs[i][j]) System.out.print("* ");
				else			System.out.print(". ");
			}
			System.out.println();
		}
		System.out.println("-------------------");
		//in[] sol = solution / zamiana . na cyfre (oznacza ile bomb jest obok tego pola)	
		int[][] sol = new int[m+2][n+2];
		
		for(int i=1; i<=m; i++)
			for(int j=1; j<=n; j++)
			// (ii, jj) - indexy pol sasiadujacych
				for(int ii = i-1; ii<= i+1; ii++)
					for(int jj = j-1; jj<= j+1; jj++)
						if(bombs[ii][jj]) sol[i][j]++;
	
		// wyswietl rozwiazanie	
		for(int i=1; i<=m; i++){
			for(int j=1; j<=n; j++){
				if(bombs[i][j]) System.out.print("* ");
				else			System.out.print(sol[i][j] + " ");
			}
			System.out.println();
		}
			
		
	}//main
	
}