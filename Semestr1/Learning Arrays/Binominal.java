public class Binominal{
	
	public static void main(String[] ags){
		int n = Integer.parseInt(args[0]);
		
		int[] permutation = new int[n];
		boolean[][] board = new boolean[n][n];
		
		//r random permutation of the integers 0 through n-1.
		int r = 0;
		for(int=0; i<n; i++)
			permutation[i] = (int)(i + Math.random()* (n-i));
		
		//display permutation
		for(int=0; i<n; i++)
			System.out.print("{" + permutation[i] + "}");
	}
	
}