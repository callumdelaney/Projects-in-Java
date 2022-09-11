// W05 Team 05 [Wed 05:15PM]
package scoring;

import java.util.ArrayList;


import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import cribbage.*;
import logging.*;

public class RunShowScoreStrategy implements IScoringStrategy {

//    Returns the difference between the next card and the current card
    public int difference(Card card, Card nextCard) {
        Rank cardRank = (Rank) card.getRank();
        Rank nextCardRank = (Rank) nextCard.getRank();
        return nextCardRank.order - cardRank.order;
    }

	public int score(Cribbage.Segment s, IPlayer player) {
//	    segment includes crib already

//        pre sort hand
        Hand newHand = new Hand(new Deck(Suit.values(), Rank.values(), "cover", new MyCardValues()));
        for (Card card : s.segment.getCardList()) {
            newHand.insert(card.clone(), false);
        }
        newHand.sort(Hand.SortType.POINTPRIORITY, false);

        ArrayList<Card> cardList = new ArrayList<>(newHand.getCardList());

        int sum = 0;
//        potential runs stores a list of runs
//        each run is composed of same rank lists

//        Store all potential runs in lists of lists of cards of the same rank
        ArrayList<ArrayList<ArrayList<Card>>> potentialRuns = new ArrayList<>();
//        initialise first element of potential runs with an empty potential run
        potentialRuns.add(0, new ArrayList<>());
        ArrayList<ArrayList<Card>> emptyPotentialRun = potentialRuns.get(0);
        emptyPotentialRun.add(new ArrayList<>());
//        initialise first element of potential run's same rank list with first element of sorted card list
        ArrayList<Card> emptySameRankList = emptyPotentialRun.get(0);
        emptySameRankList.add(cardList.get(0));



        for (int i=1; i<cardList.size(); i++) {
            ArrayList<ArrayList<Card>> potentialRun = potentialRuns.get(0);

//            next card is same rank as current card
            if (difference(cardList.get(i-1), cardList.get(i)) == 0) {
//                add to current same rank list
                ArrayList<Card> sameRankList = potentialRun.get(0);
                sameRankList.add(cardList.get(i));
            }
//            next card is exactly 1 rank higher
            else if (difference(cardList.get(i-1), cardList.get(i)) == 1) {
//                Make new same rank list and add card to that
                potentialRun.add(0, new ArrayList<>());
                ArrayList<Card> newSameRankList = potentialRun.get(0);
                newSameRankList.add(cardList.get(i));
            }
//            end of potential run
            else {
//                Start new potential run
                potentialRuns.add(0, new ArrayList<>());
                emptyPotentialRun = potentialRuns.get(0);
                emptyPotentialRun.add(new ArrayList<>());
                ArrayList<Card> newSameRankList = emptyPotentialRun.get(0);
                newSameRankList.add(cardList.get(i));
            }
        }


//  Check the ordering of the runs
        ArrayList<ArrayList<Card>> runs = new ArrayList<>();
        runs.add(new ArrayList<>());
        for (ArrayList<ArrayList<Card>> run : potentialRuns) {
//            Is run
            if (run.size() > 2) {
                for (ArrayList<Card> sameRankList : run) {
                    ArrayList<ArrayList<Card>> tmpruns = new ArrayList<>();
//                    For every card in same rank list, affix to end of new run
                    for (Card card : sameRankList) {
                        for (ArrayList<Card> refRun : runs) {
                            ArrayList<Card> newRun = new ArrayList<>(refRun);
                            newRun.add(0, card);
                            tmpruns.add(newRun);
                        }
                    }
                    runs = tmpruns;
                }
            }
        }


        // logging step
        for (ArrayList<Card> run : runs) {
            if (run.size() == 5) {
                sum += 5;
                player.setScore(player.getScore()+5);
                EventLogger.getInstance().logRunShowScoreStrat(5, run, 5, player.getId(),player.getScore());
            }
            else if (run.size() == 4) {
                sum += 4;
                player.setScore(player.getScore()+4);
                EventLogger.getInstance().logRunShowScoreStrat(4, run, 4, player.getId(),player.getScore());
            }
            else if (run.size() == 3){
                sum += 3;
                player.setScore(player.getScore()+3);
                EventLogger.getInstance().logRunShowScoreStrat(3, run, 3, player.getId(),player.getScore());
            }
        }

        return sum;
    }
        
}
