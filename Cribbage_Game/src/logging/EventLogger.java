// W05 Team 05 [Wed 05:15PM]
package logging;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ch.aplu.jcardgame.*;
import cribbage.Cribbage;
import cribbage.MyCardValues;
import cribbage.Rank;
import cribbage.Suit;



public class EventLogger {
    private static EventLogger instance = new EventLogger();

//    Get instance of singleton class
    public static EventLogger getInstance() {
        return instance;
    }

//    Log text into log file
    public void log(String text) {
        try {
            FileWriter writer = new FileWriter("cribbage.log", true);
            writer.write(text);
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

//    Resets previous log
    public void reset() {
        try {
            new FileWriter("cribbage.log", false).close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static String handToString(Hand hand) {
        boolean first = true;
        String result = "[";
        for(Card x : hand.getCardList()) {
            if(!first) {
                result = result + ",";
            }
            result = result + Cribbage.cardRank(x);
            result = result + Cribbage.cardSuit(x);
            first = false;
        }
        result = result + "]\n";
        return result;
    }

    public static String arrayListToString(ArrayList<Card> cards) {
        boolean first = true;
        String result = "[";
        for(Card x : cards) {
            if(!first) {
                result = result + ",";
            }
            result = result + Cribbage.cardRank(x);
            result = result + Cribbage.cardSuit(x);
            first = false;
        }
        result = result + "]\n";
        return result;
    }

    public static String arrayToString(Card[] cards) {
        boolean first = true;
        String result = "[";
        for(Card x : cards) {
            if(!first) {
                result = result + ",";
            }
            result = result + Cribbage.cardRank(x);
            result = result + Cribbage.cardSuit(x);
            first = false;
        }
        result = result + "]\n";
        return result;
    }
    
    public void logDeal(int nPlayers, Hand[] playerHands) {
    	for (int i=0; i<nPlayers; i++) {
    		this.log("deal,P"+i+",");
    		this.log(EventLogger.handToString(playerHands[i]));
    	}
    }
    
    public void logRunShowScoreStrat(int length, ArrayList<Card> cards, int value, int player, int playerScore) {
    	String log;
    	Hand testHand = new Hand(new Deck(Suit.values(), Rank.values(), "cover", new MyCardValues()));
    	for (Card card: cards) {
    		testHand.insert(card.clone(), false);
    	}
    	testHand.sort(Hand.SortType.POINTPRIORITY, false);
    	
//    	logs the run based on length of run, cards associated with run, value of run, player and total player score
        log = "score,P"+player+","+playerScore+","+value+",run"+length+","+arrayListToString(testHand.getCardList());
    	System.out.print(log);
    	this.log(log);
    }

    public void logFlushShowScoreStrat(int length, ArrayList<Card> cards, int value, int player, int playerScore) {
//        Put all cards into a new hand to be sorted and logged
        String log;
        Hand testHand = new Hand(new Deck(Suit.values(), Rank.values(), "cover", new MyCardValues()));
        for (Card card: cards) {
            testHand.insert(card.clone(), false);
        }
//        Sort by point priority
        testHand.sort(Hand.SortType.POINTPRIORITY, false);

//        Need to confirm this works first
        log = "score,P"+player+","+playerScore+","+value+",flush"+length+","+arrayListToString(testHand.getCardList());
        System.out.println(log);
        this.log(log);
    }
    
    public void logPairRunScoreStrat(int length, int value, int player, int playerScore) {
    	String log;
    	log = "score,P"+player+","+playerScore+","+value+",pair"+length+"\n";
    	System.out.print(log);
    	this.log(log);
    }
    
    public void logGoScoreStrat(int value, int player, int playerScore) {
    	String log;
    	log = "score,P"+player+","+playerScore+","+value+",go\n";
    	System.out.print(log);
    	this.log(log);
    }

    public void logJackScoreStrat(int player, int playerScore, Card card) {
        String log;
        log = "score,P"+player+","+playerScore+",1,jack,["+Cribbage.cardRank(card)+Cribbage.cardSuit(card)+"]\n";
        System.out.print(log);
        this.log(log);
    }

    public void logStarterJackScore(int player, int playerScore, Card card) {
        String log;
        log = "score,P"+player+","+playerScore+",2,starter,["+Cribbage.cardRank(card)+Cribbage.cardSuit(card)+"]\n";
        System.out.print(log);
        this.log(log);
    }

    public void logDiscard(int player, Hand discard) {
        String log;
        log = "discard,P" + player + "," + EventLogger.handToString(discard);
        this.log(log);
    }

    public void logStarter(Card card) {
        String log;
        log = "starter," + Cribbage.cardRank(card) + Cribbage.cardSuit(card) + "\n";
        this.log(log);
    }

    public void logPlay(int player, int total, Card card) {
        String log;
        log = "play,P" + player + "," + total + "," + Cribbage.cardRank(card) + Cribbage.cardSuit(card) + "\n";
        this.log(log);
    }
    
    public void logFifteenRunScoreStrat(int player, int playerScore, int value) {
    	String log = "score,P" + player + "," + playerScore + "," + value + ",fifteen\n";
		this.log(log);
    }
    
    public void logFifteenShowScoreStrat(int player, int playerScore, int value, Hand fifteenHand) {
//      Sort by point priority
        fifteenHand.sort(Hand.SortType.POINTPRIORITY, false);
    	String log = "score,P" + player + "," + playerScore + ",2,fifteen," + EventLogger.handToString(fifteenHand);
    	this.log(log);
    }
    
    public void logPairShowScoreStrat(int length,int player, int playerScore, int value, Card[] cardList) {
    	String log = "score,P" + player + "," + playerScore + ","+value+"," + "pair"+length+"," + arrayToString(cardList);
    	System.out.print(log);
    	this.log(log);
    }
    
    public void logRunPlayScoreStrat(int length, int player, int value, int playerScore) {
    	String log = "score,P" + player + "," + playerScore + "," + value + ",run"+length+"\n";
    	System.out.print(log);
    	this.log(log);
    }
    
    public void logThirtyOneScoreStrat(int player, int playerScore, int value) {
    	String log = "score,P" + player + "," + playerScore + "," + value + ",thirtyone\n";
    	this.log(log);
    }

}
