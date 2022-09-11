// W05 Team 05 [Wed 05:15PM]

package cribbage;

// Cribbage.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import logging.EventLogger;

import java.awt.Color;
import java.awt.Font;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class Cribbage extends CardGame {
	private static final long serialVersionUID = 1L;
	static Cribbage cribbage;  // Provide access to singleton
	public static FacadeScoring facadeScoring = new FacadeScoring();

	// returns value of the card rank
	public static int cardValue(Card c) {
		Rank rank = (Rank) c.getRank();
		return rank.value;
	}
	
	// returns string value of card rank
	public static String cardRank(Card c) {
		Rank rank = (Rank) c.getRank();
		switch (rank) {
			case ACE:
				return "A";
			case TWO:
				return "2";
			case THREE:
				return "3";
			case FOUR:
				return "4";
			case FIVE:
				return "5";
			case SIX:
				return "6";
			case SEVEN:
				return "7";
			case EIGHT:
				return "8";
			case NINE:
				return "9";
			case TEN:
				return "T";
			case JACK:
				return "J";
			case QUEEN:
				return "Q";
			case KING:
				return "K";
			default:
				return "shouldn't go here";
		}
	}
	public static String cardSuit(Card c) {
		Suit suit = (Suit) c.getSuit();
		switch (suit) {
		case CLUBS:
			return "C";
		case DIAMONDS:
			return "D";
		case HEARTS:
			return "H";
		case SPADES:
			return "S";
		default:
			return "shouldn't go here";
		}
			
	}

	/*
	Canonical String representations of Suit, Rank, Card, and Hand
	*/
	String canonical(Suit s) { return s.toString().substring(0, 1); }

	String canonical(Rank r) {
		switch (r) {
			case ACE:case KING:case QUEEN:case JACK:case TEN:
				return r.toString().substring(0, 1);
			default:
				return String.valueOf(r.value);
		}
	}

    String canonical(Card c) { return canonical((Rank) c.getRank()) + canonical((Suit) c.getSuit()); }

    String canonical(Hand h) {
		Hand h1 = new Hand(deck); // Clone to sort without changing the original hand
		for (Card C: h.getCardList()) h1.insert(C.getSuit(), C.getRank(), false);
		h1.sort(Hand.SortType.POINTPRIORITY, false);
		return "[" + h1.getCardList().stream().map(this::canonical).collect(Collectors.joining(",")) + "]";
    }



	static Random random;

	public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
      int x = random.nextInt(clazz.getEnumConstants().length);
      return clazz.getEnumConstants()[x];
  }

	static boolean ANIMATE;
	// transfers card to another hand
	void transfer(Card c, Hand h) {
		if (ANIMATE) {
			c.transfer(h, true);
		} else {
			c.removeFromHand(true);
			h.insert(c, true);
		}
  }
  // deal cards to players
  private void dealingOut(Hand pack, Hand[] hands) {
	  for (int i = 0; i < nStartCards; i++) {
		  for (int j=0; j < nPlayers; j++) {
			  Card dealt = randomCard(pack);
			  dealt.setVerso(false);  // Show the face
			  transfer(dealt, hands[j]);
		  }
	  }
  }

	static int SEED;

	public static Card randomCard(Hand hand){
      int x = random.nextInt(hand.getNumberOfCards());
      return hand.get(x);
  }

  private final String version = "0.1";
  static public final int nPlayers = 2;
  public final int nStartCards = 6;
  public final int nDiscards = 2;
  private final int handWidth = 400;
  private final int cribWidth = 150;
  private final int segmentWidth = 180;
  private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover", new MyCardValues());
  private final Location[] handLocations = {
			  new Location(360, 75),
			  new Location(360, 625)
	  };
  private final Location[] scoreLocations = {
			  new Location(590, 25),
			  new Location(590, 675)
	  };
  private final Location[] segmentLocations = {  // need at most three as 3x31=93 > 2x4x10=80
			new Location(150, 350),
			new Location(400, 350),
			new Location(650, 350)
	};
  private final Location starterLocation = new Location(50, 625);
  private final Location cribLocation = new Location(700, 625);
  private final Location seedLocation = new Location(5, 25);
  // private final TargetArea cribTarget = new TargetArea(cribLocation, CardOrientation.NORTH, 1, true);
  private final Actor[] scoreActors = {null, null}; //, null, null };
  private final Location textLocation = new Location(350, 450);
  private final Hand[] hands = new Hand[nPlayers];
  private static Hand starter;
  private Hand crib;

  public static void setStatus(String string) { cribbage.setStatusText(string); }

static private final IPlayer[] players = new IPlayer[nPlayers];
private final int[] scores = new int[nPlayers];

final Font normalFont = new Font("Serif", Font.BOLD, 24);
final Font bigFont = new Font("Serif", Font.BOLD, 36);

private void initScore() {
	 for (int i = 0; i < nPlayers; i++) {
		 scores[i] = 0;
		 scoreActors[i] = new TextActor("0", Color.WHITE, bgColor, bigFont);
		 addActor(scoreActors[i], scoreLocations[i]);
	 }
  }
// update scores on the animation
private void updateScore(int player) {
	removeActor(scoreActors[player]);
	scoreActors[player] = new TextActor(String.valueOf(scores[player]), Color.WHITE, bgColor, bigFont);
	addActor(scoreActors[player], scoreLocations[player]);
}

private void deal(Hand pack, Hand[] hands) {
	for (int i = 0; i < nPlayers; i++) {
		hands[i] = new Hand(deck);
		// players[i] = (1 == i ? new HumanPlayer() : new RandomPlayer());
		players[i].setId(i);
		players[i].startSegment(deck, hands[i]);
		// set each player's score to 0
		players[i].setScore(0);
	}
	RowLayout[] layouts = new RowLayout[nPlayers];
	for (int i = 0; i < nPlayers; i++)
	{
		layouts[i] = new RowLayout(handLocations[i], handWidth);
		layouts[i].setRotationAngle(0);
		// layouts[i].setStepDelay(10);
		hands[i].setView(this, layouts[i]);
		hands[i].draw();
	}
	layouts[0].setStepDelay(0);

	dealingOut(pack, hands);
	for (int i = 0; i < nPlayers; i++) {
		hands[i].sort(Hand.SortType.POINTPRIORITY, true);
	}
	layouts[0].setStepDelay(0);
}

private void discardToCrib() {
	int p = 0; //Starting from player 0
	Card card;
	crib = new Hand(deck);
	Hand discard = new Hand(deck);
	RowLayout layout = new RowLayout(cribLocation, cribWidth);
	layout.setRotationAngle(0);
	crib.setView(this, layout);
	// crib.setTargetArea(cribTarget);
	crib.draw();
	for (IPlayer player: players) {
		for (int i = 0; i < nDiscards; i++) {
			card = player.discard();
			transfer(card.clone(), discard);
			transfer(card, crib);
		}
		if(p == 0) {
			discard.sort(Hand.SortType.POINTPRIORITY, false);
			// log the discarding of cards
			EventLogger.getInstance().logDiscard(0,discard);
		} else {
			discard.sort(Hand.SortType.POINTPRIORITY, false);
			EventLogger.getInstance().logDiscard(1,discard);
		}
		discard.removeAll(false);
		p = 1;
		crib.sort(Hand.SortType.POINTPRIORITY, true);
	}
}

private void starter(Hand pack) {
	starter = new Hand(deck);  // if starter is a Jack, the dealer gets 2 points
	RowLayout layout = new RowLayout(starterLocation, 0);
	layout.setRotationAngle(0);
	starter.setView(this, layout);
	starter.draw();
	Card dealt = randomCard(pack);
	dealt.setVerso(false);
	transfer(dealt, starter);
	// log starter card
	EventLogger.getInstance().logStarter(dealt);
	if(starter.get(0).getRank() == Rank.JACK) { //Starter card is jack, 2 points for dealer(Player 1)
		int score = facadeScoring.starterJackScore(players[1], starter.get(0));
		scores[1] += score;
		updateScore(1);
	}
}
// returns Card in the starter hand
public static Card getStarterCard() {
	Hand starterHand = starter;
	assert(starterHand != null);
	return starterHand.get(0);
}
// returns the total value of a hand
public static int total(Hand hand) {
	int total = 0;
	for (Card c: hand.getCardList()) total += cardValue(c);
	return total;
}

// print card details to stdout
public static void printCard(Card card) {
	System.out.print(cardRank(card) + cardSuit(card));
}

//print hand details to stdout
public static void printHand(Hand hand) {
	int numCards = hand.getNumberOfCards(), i=1;
	System.out.print("[");
	for (Card card: hand.getCardList()) {
		printCard(card);
		if (!(i++ == numCards)) {
			System.out.print(",");
		}
	}
	System.out.print("]");
}


public class Segment {
		public Hand segment;
		boolean go;
		int lastPlayer;
		boolean newSegment;

		void reset(final List<Hand> segments) {
			segment = new Hand(deck);
			segment.setView(Cribbage.this, new RowLayout(segmentLocations[segments.size()], segmentWidth));
			segment.draw();
			go = false;        // No-one has said "go" yet
			lastPlayer = -1;   // No-one has played a card yet in this segment
			newSegment = false;  // Not ready for new segment yet
		}
}
// play a round
private void play() {
	final int thirtyone = 31;
	List<Hand> segments = new ArrayList<>();
	int currentPlayer = 0; // Player 1 is dealer
	Segment s = new Segment();
	s.reset(segments);
	while (!(players[0].emptyHand() && players[1].emptyHand())) {
		int turnScore = 0;
		Card nextCard = players[currentPlayer].lay(thirtyone-total(s.segment));
		if (nextCard == null) {
			if (s.go) {
				
				// Another "go" after previous one with no intervening cards
				turnScore = facadeScoring.goScore(players[currentPlayer]);
				scores[currentPlayer] += turnScore;
				updateScore(currentPlayer);
				s.newSegment = true;
			} else {
				// currentPlayer says "go"
				s.go = true;
			}
			// switch player turns
			currentPlayer = (currentPlayer+1) % 2;
		} else {
			s.lastPlayer = currentPlayer; // last Player to play a card in this segment
			transfer(nextCard, s.segment);
			//Log playing of card
			EventLogger.getInstance().logPlay(currentPlayer, total(s.segment), nextCard);

			System.out.println(canonical(s.segment));
			// a call to facadeScoring to score this player based on the current segment
			turnScore = facadeScoring.playScore(s,players[currentPlayer]);
			System.out.println("turnScore: " + turnScore);
			// update scores on game
			scores[currentPlayer] += turnScore;
			updateScore(currentPlayer);

			
			if (total(s.segment) == thirtyone) {
				// lastPlayer scores 2 points for 31 in call to facadeScoring
				s.newSegment = true;
				currentPlayer = (currentPlayer+1) % 2;
			} else {
				// lastPlayer gets 2 points for a 15 (in facadeScoring)
				if (!s.go) { // if it is "go" then same player gets another turn
					currentPlayer = (currentPlayer+1) % 2;
				}
			}
		}
		if (s.newSegment) {
			segments.add(s.segment);
			s.reset(segments);
		}
	}
	// if s.go is true, the currentPlayer will have played the last card
	// so they score for go
	if (s.go) {
		int turnScore = facadeScoring.goScore(players[currentPlayer]);
		scores[currentPlayer] += turnScore;
		updateScore(currentPlayer);
	}
	else {
		// neither player had called go, so the currentPlayer will not have played the last card
		currentPlayer = (currentPlayer+1) % 2;
		int turnScore = facadeScoring.goScore(players[currentPlayer]);
		scores[currentPlayer] += turnScore;
		updateScore(currentPlayer);
	}
}
// the Show stage of the game
void showHandsCrib() {
	String log;
	int showScore = 0;
	System.out.println("\nTHE SHOW:\n");
	System.out.println("Starter card: "+canonical(starter.get(0)));
	// have to construct a segment that includes the players starting hand and the starting card
	for (int i=0;i<nPlayers;i++) {
		
		ArrayList<Card> startingCard = (ArrayList<Card>) starter.getCardList().clone();
		ArrayList<Card> forScoring = new ArrayList<>();

		// combine each player's starting hand with the starter card
		forScoring.addAll(players[i].startingHand);
		// log showing of hands for each player
		log = "show,P" + players[i].getId() + "," + cardRank(startingCard.get(0)) + cardSuit(startingCard.get(0)) + "+" + EventLogger.getInstance().arrayListToString(forScoring);
		forScoring.add(startingCard.get(0));
		
		Segment s = new Segment();
		s.segment = new Hand(deck);
		
		for (Card c: forScoring) {
			s.segment.insert(c, false);
		}

		EventLogger.getInstance().log(log);
		System.out.println("Player "+i+" canonical hand:");
		System.out.println(canonical(s.segment));
		// call showScore from facadeScoring to score each hand
		showScore = facadeScoring.showScore(s,players[i]);
		System.out.println("Player "+i+" scored "+showScore+" points for the show");
		
		// update scores in game
		scores[i] += showScore;
		updateScore(i);
	}
	
	// score the crib (for dealer)
	// Player1 is always the dealer
	ArrayList<Card> startingCard = (ArrayList<Card>) starter.getCardList().clone();
	ArrayList<Card> forScoring = new ArrayList<>();
	forScoring.addAll(crib.getCardList());
	// create log string of starting card and crib
	log = "show,P1," + cardRank(startingCard.get(0)) + cardSuit(startingCard.get(0)) + "+" + EventLogger.getInstance().arrayListToString(forScoring);
	forScoring.add(startingCard.get(0));

	Segment s = new Segment();
	s.segment = new Hand(deck);

	for (Card c: forScoring) {
		s.segment.insert(c, false);
	}
	
	System.out.println("Player 1 has the crib:");
	System.out.println(canonical(s.segment));
	// log the showing of the crib hand
	EventLogger.getInstance().log(log);
	// call showScore from facadeScoring to score the crib + starter card
	showScore = facadeScoring.showScore(s,players[1]);

	System.out.println("Player 1 scored "+showScore+" points for the crib!");
	// update scores in game
	scores[1] += showScore;
	updateScore(1);
}

  public Cribbage()
  {
    super(850, 700, 30);
    setSimulationPeriod(10);
    cribbage = this;
    setTitle("Cribbage (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
    setStatusText("Initializing...");
    initScore();

	  Hand pack = deck.toHand(false);
	  RowLayout layout = new RowLayout(starterLocation, 0);
	  layout.setRotationAngle(0);
	  pack.setView(this, layout);
	  pack.setVerso(true);
	  pack.draw();
	  addActor(new TextActor("Seed: " + SEED, Color.BLACK, bgColor, normalFont), seedLocation);

	  /* Play the round */
	  deal(pack, hands);
	  
//	  log the cards dealt to each player
	  EventLogger.getInstance().logDeal(nPlayers, hands);

	  discardToCrib();

	  starter(pack);
	  
	  for (int i=0;i<nPlayers;i++) {
		  players[i].makeStartingHand();
	  }
	  
	  play();

	  showHandsCrib();


    addActor(new Actor("sprites/gameover.gif"), textLocation);
    setStatusText("Game over.");
    refresh();
  }

  public static void main(String[] args)
		  throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
		  	InstantiationException, IllegalAccessException {
	  /* Handle Properties */
	  // System.out.println("Working Directory = " + System.getProperty("user.dir"));
	  Properties cribbageProperties = new Properties();
	  // Default properties
	  cribbageProperties.setProperty("Animate", "true");
	  cribbageProperties.setProperty("Player0", "cribbage.RandomPlayer");
	  cribbageProperties.setProperty("Player1", "cribbage.HumanPlayer");

	  // Read properties
	  try (FileReader inStream = new FileReader("cribbage.properties")) {
		  cribbageProperties.load(inStream);
	  }

	  // Control Graphics
	  ANIMATE = Boolean.parseBoolean(cribbageProperties.getProperty("Animate"));

	  // Control Randomisation
	  /* Read the first argument and save it as a seed if it exists */
	  if (args.length > 0 ) { // Use arg seed - overrides property
		  SEED = Integer.parseInt(args[0]);
	  } else { // No arg
		  String seedProp = cribbageProperties.getProperty("Seed");  //Seed property
		  if (seedProp != null) { // Use property seed
			  SEED = Integer.parseInt(seedProp);
		  } else { // and no property
			  SEED = new Random().nextInt(); // so randomise
		  }
	  }
	  random = new Random(SEED);

	  // Control Player Types
	  Class<?> clazz;
	  clazz = Class.forName(cribbageProperties.getProperty("Player0"));
	  players[0] = (IPlayer) clazz.getConstructor().newInstance();
	  clazz = Class.forName(cribbageProperties.getProperty("Player1"));
	  players[1] = (IPlayer) clazz.getConstructor().newInstance();
	  // End properties

	  // initial logging step
	  EventLogger.getInstance().reset();
	  String log;
	  log = "seed," + SEED + "\n" + cribbageProperties.getProperty("Player0") + ",P0\n" + cribbageProperties.getProperty("Player1") + ",P1\n";
	  EventLogger.getInstance().log(log);


	  new Cribbage();
  }

}
