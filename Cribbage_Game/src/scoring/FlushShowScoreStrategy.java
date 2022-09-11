// W05 Team 05 [Wed 05:15PM]
package scoring;
import ch.aplu.jcardgame.*;
import cribbage.Cribbage;
import cribbage.Cribbage.Segment;
import cribbage.IPlayer;
import cribbage.Suit;
import logging.EventLogger;

import java.util.ArrayList;

public class FlushShowScoreStrategy implements IScoringStrategy {

    public int score(Segment s, IPlayer player){
        // segment s already contains starter card
        Card starter = Cribbage.getStarterCard();
        int sum = 0;
        for(Suit suit : Suit.values()) {
            // Current segment have flush of length 5 (must include starter)
            if(s.segment.getNumberOfCardsWithSuit(suit) > 4) {
                sum = 5;
                player.setScore(player.getScore()+sum);
                EventLogger.getInstance().logFlushShowScoreStrat(5, s.segment.getCardList(), 5, player.getId(), player.getScore());
            } else if(s.segment.getNumberOfCardsWithSuit(suit) > 3){
                // Flush of length 4, must not include starter card
                if(starter.getSuit() != suit) {
                    sum = 4;
                    player.setScore(player.getScore()+sum);
                    ArrayList<Card> cardList = new ArrayList<>();
                    for (Card card: s.segment.getCardList()) {
                        cardList.add(card.clone());
                    }
                    cardList.remove(starter.clone());
                    EventLogger.getInstance().logFlushShowScoreStrat(4, cardList, 4, player.getId(), player.getScore());
                }
                // Do nothing if flush of length 4 including starter card
            }
        }
        return sum;
    }
}
