package Learn.CheckComparable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args){
        Hand hand = new Hand();

        hand.add( new Card(3, Card.CLUBS) );
        hand.add( new Card(2, Card.DIAMONDS)  );
        hand.add( new Card(14, Card.CLUBS) );
        hand.add( new Card(12, Card.HEARTS) );
        hand.add( new Card(2, Card.CLUBS) );

        //hand.sortAgainstSuit();
        hand.print();
        System.out.println("-------------------");
        System.out.println("MIN: "+ Collections.min(hand.getCards(), new SortAgainstSuitAndValue()) );
        System.out.println("MAX: "+ Collections.max(hand.getCards(), new SortAgainstSuitAndValue()) );
        Collections.sort(hand.getHand());
        //ArrayList<Card> cards = hand.getCards();
        Card wanted = new Card(12, Card.HEARTS);
        if ( Collections.binarySearch( hand.getCards(), wanted) >=0 )
            System.out.println("Card->" + wanted);
    }
    //====================================
}
