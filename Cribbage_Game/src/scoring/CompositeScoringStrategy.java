// W05 Team 05 [Wed 05:15PM]
package scoring;
import cribbage.Cribbage.Segment;
import cribbage.IPlayer;
import java.util.ArrayList;


public abstract class CompositeScoringStrategy implements IScoringStrategy {
    public ArrayList<IScoringStrategy> scoringStrategies = new ArrayList<>();

    public abstract int score(Segment s, IPlayer player);

    public void addStrategy(IScoringStrategy scoringStrategy) {
        scoringStrategies.add(scoringStrategy);
    }

    public void removeStrategy(IScoringStrategy scoringStrategy) {
        scoringStrategies.remove(scoringStrategy);
    }
}
