// W05 Team 05 [Wed 05:15PM]
package scoring;

import cribbage.Cribbage.Segment;
import cribbage.IPlayer;

public class CompositeNormalShowScoringStrategy extends CompositeScoringStrategy{

    public CompositeNormalShowScoringStrategy() {
        //        Applicable to normal show rules strategies
        //        Ordered according to specifications
        this.addStrategy(new FifteenShowScoringStrategy());
        this.addStrategy(new RunShowScoreStrategy());
        this.addStrategy(new PairShowScoreStrategy());
        this.addStrategy(new FlushShowScoreStrategy());
        this.addStrategy(new JackOfSameSuitShowScoreStrategy());
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
