// W05 Team 05 [Wed 05:15PM]

package cribbage;

import ch.aplu.jcardgame.*;

import java.util.ArrayList;

public abstract class IPlayer {
    int id;
    Deck deck; // Need this since can't get from hand to deck
    Hand hand;
    ArrayList<Card> startingHand;
    int totalScore = 0;

    void setId(int id)  {
        this.id = id;
    }
    void startSegment(Deck deck, Hand hand) {
    	this.deck = deck;
    	this.hand = hand;
    	
    }
    void makeStartingHand() {
    	startingHand = ((ArrayList<Card>) hand.getCardList().clone());
    }
    
    abstract Card discard();
    boolean emptyHand() {return hand.isEmpty();}
    abstract Card selectToLay();
    Card lay(int limit) {
        // System.out.println("lay(" + limit + ")");
        Hand unlayable = new Hand(deck);
        for (Card c: ((ArrayList<Card>) hand.getCardList().clone()))  // Modify list, so need to iterate over clone
            if (Cribbage.cardValue(c) > limit) {
                c.removeFromHand(true);
                // System.out.println("hand = " + hand.toString());
                unlayable.insert(c, false);
            }
        // hand.draw(); Cribbage.delay(1000);
        Card s = selectToLay();
            hand.insert(unlayable, true);
            return s;
    }
    public void setScore(int score) {
    	totalScore = score;
    }
    public int getId() {
    	return id;
    }
    public int getScore() {
    	return totalScore;
    }
}
