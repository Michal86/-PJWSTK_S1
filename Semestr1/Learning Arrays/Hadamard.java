public class Hadamard{
	public static void main(String[] args){
		int n = Integer.parseInt(args[0]);
		boolean[][] H = new boolean[n][n];
		
		//inicjalizacja Hadamard matrix
		H[0][0] = true;
		
		for(int k=1; k<n; k += k)
			for(int i=0; i<k;i++)
				for(int j=0; j<k; j++){
					H[i+k][j]   =  H[i][j];
					H[i][j+k]   =  H[i][j];
					H[i+k][j+k] = !H[i][j];
				}
		//wyswietl		
		for(int i=0; i<n;i++){
			for(int j=0; j<n; j++){
				if(H[i][j]) System.out.print("[1]");
				else		System.out.print("[0]");
			}
			System.out.println();
		}
	}
}