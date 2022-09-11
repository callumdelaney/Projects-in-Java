// W05 Team 05 [Wed 05:15PM]
package scoring;

import cribbage.Cribbage.Segment;
import cribbage.IPlayer;


public class CompositeNormalPlayScoringStrategy extends CompositeScoringStrategy {

    public CompositeNormalPlayScoringStrategy() {
//        Applicable to normal play rules strategies
        this.addStrategy(new RunPlayScoreStrategy());
        this.addStrategy(new PairRunScoreStrategy());
        this.addStrategy(new FifteenRunScoreStrategy());
        this.addStrategy(new ThirtyOneScoreStrategy());
    }

    public int score(Segment s, IPlayer player) {
        //        Go over all applicable strategies and apply their scoring method to add to this turns score
        int sum = 0;
        for (IScoringStrategy scoringStrategy : scoringStrategies) {
            sum += scoringStrategy.score(s,player);
        }
        return sum;
    }
}
