// W05 Team 05 [Wed 05:15PM]

package cribbage;

import ch.aplu.jcardgame.Card;
import cribbage.Cribbage.Segment;
import logging.EventLogger;
import scoring.*;

public class FacadeScoring {
    private CompositeScoringStrategy compositePlayScoringStrategy;
    private CompositeScoringStrategy compositeShowScoringStrategy;

//     Initialise the two scoring strategies for play and show
    public FacadeScoring() {
        compositePlayScoringStrategy = new CompositeNormalPlayScoringStrategy();
        compositeShowScoringStrategy = new CompositeNormalShowScoringStrategy();
    }

//    Score current segment for given player using the given rules for play scoring
    public int playScore(Segment segment, IPlayer player) {
        return compositePlayScoringStrategy.score(segment,player);
    }

//    Score current segment for given player using the given rules for show scoring
    public int showScore(Segment segment, IPlayer player) {
        return compositeShowScoringStrategy.score(segment,player);
    }
    
    // logs and updates points for scoring go
    public int goScore(IPlayer player) {
    	int score = 1;
    	player.setScore(player.getScore()+score);
    	EventLogger.getInstance().logGoScoreStrat(score, player.getId(), player.getScore());
    	return score;
    }

    // logs and updates points for dealer if starterCard card is jack
    public int starterJackScore(IPlayer player, Card starterCard) {
        player.setScore(player.getScore()+2);
        EventLogger.getInstance().logStarterJackScore(player.getId(), player.getScore(), starterCard);
        return 2;
    }
}
