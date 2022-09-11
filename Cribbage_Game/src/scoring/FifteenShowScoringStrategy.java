// W05 Team 05 [Wed 05:15PM]
package scoring;

import java.util.ArrayList;
import java.util.List;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import cribbage.Cribbage;
import cribbage.IPlayer;
import cribbage.MyCardValues;
import cribbage.Rank;
import cribbage.Suit;
import cribbage.Cribbage.Segment;
import logging.EventLogger;

public class FifteenShowScoringStrategy implements IScoringStrategy {
	private int sum;
	private IPlayer player;

	public int score(Segment s, IPlayer player) {
		this.sum = 0;
		this.player = player;

//		pre sort hand to achieve proper output ordering
		Hand testHand = new Hand(new Deck(Suit.values(), Rank.values(), "cover", new MyCardValues()));
		for (Card card : s.segment.getCardList()) {
			testHand.insert(card.clone(), false);
		}
		testHand.sort(Hand.SortType.POINTPRIORITY, false);

		ArrayList<Card> cardList = new ArrayList<>(testHand.getCardList());
//		Loop through all potential combination sizes that can make up 15
		for (int combinationSize=2; combinationSize<=cardList.size(); combinationSize++) {
			Hand accumulatedHand = new Hand(new Deck(Suit.values(), Rank.values(), "cover", new MyCardValues()));
			combination(cardList, combinationSize, accumulatedHand);
		}

		return this.sum;
    }

//		Check if sums to 15
	public void checkFifteen(Hand accumulatedHand) {
		if (Cribbage.total(accumulatedHand) == 15) {
			this.sum+=2;
			// Log the 15 and the combination
			this.player.setScore(this.player.getScore() + 2);
			EventLogger.getInstance().logFifteenShowScoreStrat(player.getId(), player.getScore(), 2, accumulatedHand);
		}
	}

	// algorithm based from https://hmkcode.com/calculate-find-all-possible-combinations-of-an-array-using-java/
	public void combination(List<Card> cardList, int combinationLength, Hand accumulatedHand) {
		// 1. stop
		if(cardList.size() < combinationLength)
			return;

		// 2. add every card to accumulatedHand to test if 15
		if(combinationLength == 1)
			for(Card card:cardList) {
				accumulatedHand.insert(card.clone(), false);
				checkFifteen(accumulatedHand);
				accumulatedHand.remove(card.clone(), false);
			}

		// 3. add all cards to accumulatedHand then test if 15
		else if(cardList.size() == combinationLength){
			for(Card card:cardList) {
				accumulatedHand.insert(card.clone(), false);
			}
			checkFifteen(accumulatedHand);
		}

		// 4. for each card, call combination
		else if(cardList.size() > combinationLength)
			for(int i = 0 ; i < cardList.size() ; i++) {
				accumulatedHand.insert((cardList.get(i)).clone(), false);
				combination(cardList.subList(i + 1, cardList.size()), combinationLength - 1, accumulatedHand);
				accumulatedHand.remove(cardList.get(i).clone(), false);
			}
	}
}
