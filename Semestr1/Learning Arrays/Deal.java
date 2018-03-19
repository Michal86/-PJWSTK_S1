public class Deal{
	public static void main(String[] args){
		int CARDS_PER_PLAYER = 5;
		
		//number of players
		int PLAYERS = Integer.parseInt(args[0]);
		
	    String[] SUITS = {
            "Clubs", "Diamonds", "Hearts", "Spades"
        };

        String[] RANKS = {
            "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "Jack", "Queen", "King", "Ace"
        };
		
		int n = RANKS.length * SUITS.length; //wielkosc tablicy deck- laczna ilosc kart
		
		if(CARDS_PER_PLAYER * PLAYERS > n)
			throw new RuntimeException("Too many players");
	
		//inicjuje talie
		String[] deck = new String[n];
		for(int i =0; i<RANKS.length; i++)
			for(int j=0; j<SUITS.length; j++){
				deck[SUITS.length*i + j] = RANKS[i] + " of " + SUITS[j];
		}
		//tasuje karty
		for(int i =0; i<n; i++){
			int r = i + (int)(Math.random()* (n-i));
			String tmp = deck[r];
			deck[r] = deck[i];
			deck[i] = tmp;
		}
		//wyswietl wylosowane karty graczy
		for(int i=0; i<PLAYERS * CARDS_PER_PLAYER; i++){
			if(i % CARDS_PER_PLAYER == 0) System.out.println("PLAYER " + (i/CARDS_PER_PLAYER+1));
			System.out.println(deck[i]);
			if(i % CARDS_PER_PLAYER == CARDS_PER_PLAYER - 1)
				System.out.println();
		}
		
		/*   HowMany      */		
	    // number of command-line arguments
        n = args.length;

        // output message
        System.out.print("You entered " + n + " command-line argument");
        if (n == 1) System.out.println(".");
        else        System.out.println("s.");
    		
		
	}//main
}