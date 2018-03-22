package Learn.CheckComparable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hand implements Comparable<Hand>{
    private List<Card> hand;

    public Hand(){
        hand = new ArrayList<Card>();
    }
    //===============================

    public void add(Card card){
        if (card == null) return;

        if( !hand.contains(card))
            hand.add(card);
        else
            System.out.println("Karta jest już w ręce, oszukujesz? :D");
    }

    public void print() {
        for (Card currentCard : hand)
            System.out.println(currentCard);
    }

    public List<Card> getHand() {
        return hand;
    }

    //---------------------------------
    public ArrayList<Card> getCards(){
        return new ArrayList<>(this.hand);
        //return this.hand;
    }

    public void sortAgainstSuit(){
        Collections.sort(hand, new SortAgainstSuitAndValue());
    }

    @Override
    public int compareTo(Hand hand) {
        ArrayList<Card> againstHand = hand.getCards();
        int total1 = 0, total2 = 0;
        // Getting card points from this hand
        for (Card currentCard : getCards()){
            total1 += ( currentCard.getValue() + currentCard.getSuit() );
        }
        // Getting card points from comparing hand
        for (Card currentCard : againstHand){
            total2 += ( currentCard.getValue() + currentCard.getSuit() );
        }
        return total1-total2;
    }
}
