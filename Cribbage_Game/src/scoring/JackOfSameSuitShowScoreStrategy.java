// W05 Team 05 [Wed 05:15PM]
package scoring;
import ch.aplu.jcardgame.*;
import cribbage.Cribbage;
import cribbage.Cribbage.Segment;
import cribbage.IPlayer;
import cribbage.Rank;
import logging.EventLogger;

import java.util.ArrayList;


public class JackOfSameSuitShowScoreStrategy implements IScoringStrategy {

    public int score(Segment s, IPlayer player) {
        int sum = 0;
        Card starter = Cribbage.getStarterCard();
        ArrayList<Card> cards = s.segment.getCardsWithRank(Rank.JACK);
//        Loop over all cards in hand with rank jack
        for(Card x : cards) {
//             Make sure it's not the starter card
            if(!starter.equals(x)) {
//                Starter card have same suit with jack in hand
                if(starter.getSuit() == x.getSuit()) {
                    player.setScore(player.getScore()+1);
                    EventLogger.getInstance().logJackScoreStrat(player.getId(), player.getScore(), x);
                    sum += 1;
                }
            }
        }
        return sum;
    }

}
