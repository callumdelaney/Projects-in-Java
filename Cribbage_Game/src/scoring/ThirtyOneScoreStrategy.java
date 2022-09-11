// W05 Team 05 [Wed 05:15PM]
package scoring;

import cribbage.Cribbage;
import cribbage.IPlayer;
import logging.EventLogger;

public class ThirtyOneScoreStrategy implements IScoringStrategy {
    public int score(Cribbage.Segment s, IPlayer player) {
        if (Cribbage.total(s.segment) == 31) {
            //+2 for player total scores
            player.setScore(player.getScore() + 2);
            EventLogger.getInstance().logThirtyOneScoreStrat(player.getId(), player.getScore(), 2);
            return 2;
        }
        return 0;
    }
}
