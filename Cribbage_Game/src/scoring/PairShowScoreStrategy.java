// W05 Team 05 [Wed 05:15PM]
package scoring;
import cribbage.Cribbage.Segment;
import cribbage.IPlayer;
import ch.aplu.jcardgame.*;
import java.util.ArrayList;
import java.util.Collections;

import cribbage.MyCardValues;
import cribbage.Rank;
import cribbage.Suit;
import logging.EventLogger;

public class PairShowScoreStrategy implements IScoringStrategy{
    public int score(Segment s, IPlayer player) {
//        Use newHand to avoid removing cards from segment with library methods
        Hand newHand = new Hand(new Deck(Suit.values(), Rank.values(), "cover", new MyCardValues()));
        for (Card card : s.segment.getCardList()) {
            newHand.insert(card.clone(), false);
        }

        newHand.sort(Hand.SortType.POINTPRIORITY, false);

        int sum = 0;
        ArrayList<Card[]> pairs = newHand.getPairs();
        ArrayList<Card[]> trips = newHand.getTrips();
        ArrayList<Card[]> quads = newHand.getQuads();

//        Reverse order of sorting to fulfill canonical requirements
        Collections.reverse(pairs);
        Collections.reverse(trips);
        Collections.reverse(trips);

//        Log the matching cards
        for(Card[] cardList: pairs) {
            sum += 2;
            player.setScore(player.getScore() + 2);
            EventLogger.getInstance().logPairShowScoreStrat(2, player.getId(), player.getScore(), 2, cardList);
        }
        for(Card[] cardList: trips) {
            sum += 6;
            player.setScore(player.getScore() + 6);
            EventLogger.getInstance().logPairShowScoreStrat(3, player.getId(), player.getScore(), 6, cardList);
        }
        for(Card[] cardList: quads) {
            sum += 12;
            player.setScore(player.getScore() + 12);
            EventLogger.getInstance().logPairShowScoreStrat(4, player.getId(), player.getScore(), 12, cardList);
        }
        return sum;
    }
    
}
