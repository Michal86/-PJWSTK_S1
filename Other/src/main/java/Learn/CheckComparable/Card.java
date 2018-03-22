package Learn.CheckComparable;

public class Card implements Comparable<Card>{
    public static final int SPADES   = 0;
    public static final int DIAMONDS = 1;
    public static final int HEARTS   = 2;
    public static final int CLUBS    = 3;
    private int value;
    private int suit;

    public Card(int value, int suit){
        this.value = value;
        this.suit  = suit;
    }

    public int getValue() {
        return value;
    }
    public int getSuit() {
        return suit;
    }

    @Override
    public int compareTo(Card card) {
        if( this.getValue() - card.getValue() != 0 )
            return this.getValue() - card.getValue();
        else{
            return this.getSuit() - card.getSuit();
        }
    }

    @Override
    public String toString() {
        return StringValueOfCard(getValue())+ " of " + getSuitOfCard(getSuit());
    }

    public String getSuitOfCard(int suit){
        if( suit==0 ) return "Spades";
        if( suit==1 ) return "Diamonds";
        if( suit==2 ) return "Hearts";
        if( suit==3 ) return "Clubs";
        return "Can't get suit.";
    }

    public String StringValueOfCard(int value){
        if ( value <11) return ""+value;
        else {
            if (value == 11) return "J";
            if (value == 12) return "Q";
            if (value == 13) return "K";
            if (value == 14) return "A";
        }
        return null;
    }

    //====================================
    public static void main(String[] args){
        Card first = new Card(2, Card.DIAMONDS);
        Card second = new Card(14, Card.CLUBS);
        Card third = new Card(12, Card.HEARTS);
        Card fourth = new Card(12, Card.CLUBS);

        System.out.println(first);
        System.out.println(second);
        System.out.println(third);
        System.out.println(fourth);

        int comparison = third.compareTo(fourth);

        if ( comparison < 0 ) {
            System.out.println("the most valuable hand contains the cards");
            System.out.println(fourth);
        } else if ( comparison > 0 ){
            System.out.println("the most valuable hand contains the cards");
            System.out.println(third);
        } else {
            System.out.println("the hands are equally valuable");
        }
    }
    //====================================
}
