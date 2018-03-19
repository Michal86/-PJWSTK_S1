/*
* BinarySearch.java wielkosc_tablicu szukana 
* WYWOLANIE: java BinarySearch 11 15
*
* WYNIK DZIALANIA:
*
* Losowanie
* [20][36][2][11][100][82][32][19][21][43][5][57][80][45][47]
* Sortowanie
* [2][5][11][19][20][21][32][36][43][45][47][57][80][82][100]
*
* ---------------------
* Wyszukiwanie Binarne
* Element x:11, pozycja w tablicy:3
* Znalazlem element.
*
*/

public class BinarySearch{
	
	//posortowana tablica(arr) i x (szukana)
	public static boolean binaryS(int arr[], int x){
		int left = 0;
		int right = arr.length-1;
		int mid = 0;
		
		while(left <= right){
			mid = left + ((right - left)/2);
			
			if(x == arr[mid]) {
				System.out.println("Element x:" + x + ", pozycja w tablicy:"+ (mid+1));
				return true;
			}
			else if(x < arr[mid])
				right = mid - 1;
			else
				left = mid + 1;
		}
		return false;
	}
	
	//losuj Elementy tablicy (bez powtorzen)
	static void losujTab(int[] arr){
		System.out.println("Losowanie");
		arr[0] = (int)(1+ Math.random() * 100);
		for(int i =1; i<arr.length; i++){
			boolean exist = true;
			while(exist){
				int r = (int)(1+ Math.random() * 100);
				exist = false;
				for(int j=0; j<i; j++){
					if(r == arr[j]) exist=true;
					if(j==(i-1) && !exist)
						arr[i] = r;
				}
			}
		}
		wyswietl(arr);
	}
	
	//sortuj tablice/ BubbleSort (dosc powiedziec, ze slabe sortowanie) O(n^2) bo, O(n)*O(n) = [przejdz n razy]*[zamien n razy]
	static void sortuj(int[] arr){
		System.out.println("Sortowanie");
		boolean posortowana = false;
		int ostatni = arr.length-1;
		int tmp;
		
		while(!posortowana){
			posortowana = true;
			for(int i=0; i<ostatni; i++){
				if(arr[i] > arr[i+1]){
					tmp = arr[i];
					arr[i] = arr[i+1];
					arr[i+1] = tmp;
					posortowana = false;
				}
			}
			ostatni--;
		}
		wyswietl(arr);
	}
		
	//wizualizacja
	static void wyswietl(int[] arr){
		for(int i=0; i<arr.length; i++)
			System.out.print("[" + arr[i]+ "]");
		System.out.println();
	}
	
	//wywolanie przez podanie szukanej liczby + wielkosc tablicy
	public static void main(String[] args){
		int szukana = Integer.parseInt(args[0]);
		int n = Integer.parseInt(args[1]);
		int[] tab = new int[n];
		//---
		losujTab(tab);
		sortuj(tab);
		//---
		System.out.println("\n---------------------");
		System.out.println("Wyszukiwanie Binarne");
		if(binaryS(tab, szukana)) System.out.println("Znalazlem element.");
		else 					  System.out.println("Brak elementu w tablicy.");
	
	
	}//main
}