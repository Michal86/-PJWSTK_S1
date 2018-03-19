/* *****************************************************************************
 *  Compilation:  javac RandomWalkers.java
 *  Execution:    java RandomWalker n
 *
 *  Simulates how long it takes n random walkers starting at the center
 *  of an n-by-n grid to visit every cell in the grid.
 *  https://introcs.cs.princeton.edu/java/14array/RandomWalkers.java.html
 ***************************************************************************** */

public class Walkers{
	
	static int n;
	static int i;
	static int j;
	static int krokow;
	
	public static void main(String[] args){
			n = Integer.parseInt(args[0]);
			boolean[][] board = new boolean[n][n];
			i = n/2;
			j = n/2;
			board[i][j] = true; //startowa pozycja
			int odwiedzonych = 1;
			krokow = 0;			
			//wyswietl stan poczatkowy
			System.out.println("Pozycja startowa [" +i+ "][" +j+ "], odwiedzonych: " + odwiedzonych + ", krokow:" + krokow);
			System.out.println("---------------------");
			for(int x=0; x<n; x++){
				for(int y=0; y<n; y++){
					if(board[x][y]) System.out.print("X ");
					else			System.out.print("o ");
				}
				System.out.println();
			}
			System.out.println("**********************");
			
			//poruszaj sie po polach, az odwiedzisz wszystkie
			while(odwiedzonych< n*n){
				System.out.println("---");
				odwiedzonych = pickDirection(board, odwiedzonych);
				krokow++;
				//--- za kazdym krokiem
				System.out.println("Krok: " + krokow + ", pozycja [" +i+ "][" +j+ "]");
				for(int x=0; x<n; x++){
					for(int y=0; y<n; y++){
						if(board[x][y]) System.out.print("X ");
						else			System.out.print("o ");
					}
					System.out.println();
				}
			}
			
			//wyswietl po zakonczeniu 
			System.out.println("Pozycja koncowa [" +i+ "][" +j+ "], odwiedzonych: " + odwiedzonych + ", krokow:" + krokow);
			System.out.println("---------------------");

	}//main
	
	/* Metody */
	//nazwa slaba, nie wybiera a idzie w wylosowanym kierunku, ale jeszcze nie umiem klas
	static int pickDirection(boolean[][] moveOnBoard, int check){
		int r = (int)(Math.random() * 4); //4 kierunki
		System.out.println("r=" + r);
		switch(r){
			case 0: //prawo
				if(j+1 <n){
					j++;
					System.out.println("Idz w prawo");
				}
				if(!moveOnBoard[i][j]) {
					check++;
					moveOnBoard[i][j] = true;
				}
				break;
			
			case 1: //dol
				if(i+1 <n){
					System.out.println("Idz w dol");
					i++;
				}
				if(!moveOnBoard[i][j]) {
					check++;
					moveOnBoard[i][j] = true;
				}
				break;			
			
			case 2: //lewo
				if(j-1 >= 0){
					System.out.println("Idz w lewo"); 
					j--;
				}
				if(!moveOnBoard[i][j]) {
					check++;
					moveOnBoard[i][j] = true;
				}
				break;			
			case 3: //gora
				if(i-1 >= 0){
					System.out.println("Idz w gore"); 
					i--;
				}
				if(!moveOnBoard[i][j]) {
					check++;
					moveOnBoard[i][j] = true;
				}
				break;				
		}
		return check;
	}
	//---
}