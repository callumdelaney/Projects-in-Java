// W05 Team 05 [Wed 05:15PM]
package scoring;
import cribbage.Cribbage.Segment;
import cribbage.IPlayer;

public interface IScoringStrategy {
    int score(Segment s, IPlayer player);
}
