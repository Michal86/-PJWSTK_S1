public class RandPermutation{
	
	public static void main(String[] args){
		int n = Integer.parseInt(args[0]);
		int[] permArr = new int[n];
		boolean[][] board = new boolean[n][n];
		boolean exist = true;
		boolean zero = false;
		int r=0;
		
		//przed losowaniem, inicjalizuje wyzszym numerem, nie chce rozpatrywac wylosowanych zer, z tymi w tablicy
		for(int i=0; i<n; i++)
			permArr[i] = n;
			
		permArr[0] = (int)(Math.random() * n); //pierwszy moze zawierac wszystkie liczby
		//r random permutation of the integers 0 through n-1.
		for(int i=1; i<n; i++){
			exist = true; //aby wejsc do while
			while(exist){
				exist = false; //zakladam, ze nie istnieje wylosowana liczba
				r = (int)(Math.random() * n);
				for(int j=0; j<i; j++){
					if(permArr[j] == r)
						exist = true;
					if(j==(i-1) && !exist) // jezeli przejzy cala tab i nie znajdzie tam tej cyfry+rozne od n(bo wczesniej inicjowalismy te liczbe)
						permArr[i] = r;
				}
			}//while
		}//for(i)

		//inizjalizacja planszy wg wylosowanej permutacji
		for(int i=0; i<n; i++)
			board[permArr[i]][i] = true;
			
		//display permutation
		System.out.println("\n---------");
		for(int i=0; i<n; i++)
			System.out.print(permArr[i]+" ");
		System.out.println();
		for(int i=0; i<n; i++){
			for(int j=0; j<n; j++)
				if(board[i][j]) System.out.print("Q ");
				else			System.out.print("* ");
			System.out.println();
		}
		
		
		
		
		
	}
	
}