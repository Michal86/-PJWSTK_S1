public class QuickSort{
	static int counter = 0;
	public static void main(String[] args){
		System.out.println("Test QuicSort na podanej tablicy "+ args[0] +" elementowej.");
		int n = Integer.parseInt(args[0]);
		int[] array = new int[n];
		
		System.out.println("Tworze tablice");
		CreateRandArray(array);
		
		DisplayArr(array);
		System.out.println("Uruchamiam metode QuicSrt");
		QuickSrt(array);
		System.out.println("Tablica po sortowaniu");
		DisplayArr(array);
		System.out.println("\n Counter = " + counter);
	}
	/* ------------------------------------ */
	
	public static void QuickSrt(int[] array){
		QuickSrt(array, 0, array.length-1);
	}
	
	public static void QuickSrt(int[] array, int left, int right){
		counter++;
		if(left >= right)
			return;
		int pivot = array[(left + right)/2]; //najlepiej losowac
		int index = Partition(array, left, right, pivot); //wyznacz punkt podzialu sortowanej tablicy
		
		QuickSrt(array, left, index-1); //sortuje lewy przedzial
		QuickSrt(array, index, right);  //sortuj prawy przedzial
	}
	/* ------------------------------------ */
	
	public static int Partition(int[] array, int left, int right, int pivot){
		while(left <= right){  // (right-left == 0)
			
			while(array[left] < pivot)	left++;
			while(array[right] > pivot)	right--;
			
			if(left <= right){
				Swap(array, left, right);
				left++;
				right--;
			}
		}
		return left;
	}
	/* ------------------------------------ */	
	
	public static void Swap(int[] array, int a, int b){
		int tmp = array[a];
		array[a] = array[b];
		array[b] = tmp;
	}
	/* ------------------------------------ */
	
	public static void CreateRandArray(int[] array){
		int r = 0;
		for(int i=0; i<array.length; i++)
			array[i] = i + (int)(Math.random()* (array.length+i));
		
		/* DisplayArr(array);
		for(int j=0; j<array.length; j++){
			r = j + (int)(Math.random() * (array.length-j));
			Swap(array, j, r);
		 }
		 */
	}
	/* ------------------------------------ */
	
	public static void DisplayArr(int[] array){
		for(int i=0; i<array.length; i++){
			System.out.print("[" + array[i] + "]");
		}
		System.out.println();
	}
	
	
	
	
	
}