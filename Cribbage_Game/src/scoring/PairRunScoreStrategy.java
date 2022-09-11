// W05 Team 05 [Wed 05:15PM]
package scoring;
import cribbage.Cribbage.Segment;
import cribbage.IPlayer;
import ch.aplu.jcardgame.*;
import logging.EventLogger;

import java.util.ArrayList;
import java.util.List;

public class PairRunScoreStrategy implements IScoringStrategy{
    public boolean allCardsSameRank(List<Card> potentialTripList) {
        for (Card card : potentialTripList) {
            if (!card.getRank().equals(potentialTripList.get(0).getRank())) {
                return false;
            }
        }
        return true;
    }

    public int score(Segment s, IPlayer player) {
        int sum = 0;

//        Check if card to be placed and previous card make pair,
//        if so check if prev prev card makes trip
//        if so check if prev prev prev card makes quad
        ArrayList<Card> cardList = s.segment.getCardList();
        boolean pair = false;
        boolean trip = false;
        boolean quad = false;

//        Maybe pair
        if (cardList.size() > 1) {
            List<Card> potentialPairList = cardList.subList(cardList.size() - 2, cardList.size());
            if (allCardsSameRank(potentialPairList)) {
                pair = true;
                sum += 2;
            }
        }
//        Maybe trip
        if (pair) {
            if (cardList.size() > 2) {
                List<Card> potentialTripList = cardList.subList(cardList.size() - 3, cardList.size());
                if (allCardsSameRank(potentialTripList)) {
                    trip = true;
                    sum += 4;
                }
            }
        }
//        Maybe quad
        if (trip) {
            if (cardList.size() > 3) {
                List<Card> potentialQuadList = cardList.subList(cardList.size() - 4, cardList.size());
                if (allCardsSameRank(potentialQuadList)) {
                	quad = true;
                    sum += 6;
                }
            }
        }

//        Log the matching cards
        if (quad) {
        	player.setScore(player.getScore()+12);
        	EventLogger.getInstance().logPairRunScoreStrat(4, 12, player.getId(), player.getScore());
        }
        else if (trip) {
        	player.setScore(player.getScore()+6);
        	EventLogger.getInstance().logPairRunScoreStrat(3, 6, player.getId(), player.getScore());
        }
        else if (pair) {
        	player.setScore(player.getScore()+2);
        	EventLogger.getInstance().logPairRunScoreStrat(2, 2, player.getId(), player.getScore());
        }
        return sum;
    }
}
