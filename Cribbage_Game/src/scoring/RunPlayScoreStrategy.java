// W05 Team 05 [Wed 05:15PM]
package scoring;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import cribbage.*;
import cribbage.Cribbage.Segment;
import logging.EventLogger;

import java.util.ArrayList;
import java.util.List;


public class RunPlayScoreStrategy implements IScoringStrategy {

    public boolean increasing(Card card, Card nextCard) {
        Rank cardRank = (Rank) card.getRank();
        Rank nextCardRank = (Rank) nextCard.getRank();
        return nextCardRank.order - cardRank.order == 1;
    }

    public boolean checkRun(List<Card> potentialRun) {
        Hand testHand = new Hand(new Deck(Suit.values(), Rank.values(), "cover", new MyCardValues()));

        for (Card card : potentialRun) {
            testHand.insert(card, false);
        }

        testHand.sort(Hand.SortType.POINTPRIORITY, false);
        ArrayList<Card> sortedPotRun = testHand.getCardList();

//        Check if strictly increasing
        for (int i=0; i<sortedPotRun.size()-1; i++) {
            if (!increasing(sortedPotRun.get(i), sortedPotRun.get(i+1))) {
                return false;
            }
        }
        return true;
    }


    public int score(Segment s, IPlayer player) {

        int sum = 0;

        ArrayList<Card> cardList = new ArrayList<>(s.segment.getCardList());
        List<Card> potentialRun;

        //    if size > 6, sort last 7 cards, then check if run
        //    else take last 5 cards, sort then check if run
        //    else take last 4 cards, sort then check if run
        //    else Take last 3 cards, sort then check if run

        if (cardList.size() > 6) {
//            Check for 7 run
            potentialRun = cardList.subList(cardList.size() - 7, cardList.size());

//          current run is a valid run
            if (checkRun(potentialRun)) {
                sum = 7;
                EventLogger.getInstance().logRunPlayScoreStrat(7, player.getId(), sum, player.getScore() + sum);
            }
        }
        else if (cardList.size() > 5) {
            potentialRun = cardList.subList(0, cardList.size());
            //          current run is a valid run
            if (checkRun(potentialRun)) {
                sum = 6;
                EventLogger.getInstance().logRunPlayScoreStrat(6, player.getId(), sum, player.getScore() + sum);
            }
        }
        else if (cardList.size() > 4) {
            potentialRun = cardList.subList(0, cardList.size());
            //          current run is a valid run
            if (checkRun(potentialRun)) {
                sum = 5;
                EventLogger.getInstance().logRunPlayScoreStrat(5, player.getId(), sum, player.getScore() + sum);
            }
        }
        else if (cardList.size() > 3) {
            potentialRun = cardList.subList(0, cardList.size());
            //          current run is a valid run
            if (checkRun(potentialRun)) {
                sum = 4;
                EventLogger.getInstance().logRunPlayScoreStrat(4, player.getId(), sum, player.getScore() + sum);
            }
        }
        else if (cardList.size() > 2) {
            potentialRun = cardList.subList(0, cardList.size());
            //          current run is a valid run
            if (checkRun(potentialRun)) {
                sum = 3;
                EventLogger.getInstance().logRunPlayScoreStrat(3, player.getId(), sum, player.getScore() + sum);
            }
        }

        player.setScore(player.getScore() + sum);
        return sum;
    }
}
