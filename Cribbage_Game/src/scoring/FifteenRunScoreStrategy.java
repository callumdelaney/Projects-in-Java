// W05 Team 05 [Wed 05:15PM]
package scoring;
import cribbage.Cribbage.Segment;
import cribbage.IPlayer;
import cribbage.Cribbage;
import logging.EventLogger;

public class FifteenRunScoreStrategy implements IScoringStrategy {
	public int score(Segment s, IPlayer player) {
//		If cards so far sum to 15
		if (Cribbage.total(s.segment) == 15) {
			//+2 for player total scores
			player.setScore(player.getScore() + 2);
			EventLogger.getInstance().logFifteenRunScoreStrat(player.getId(), player.getScore(), 2);
			return 2;
		}
        return 0;
    }
}
