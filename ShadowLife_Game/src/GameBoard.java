import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;


// GameBoard splits the area into a grid of tiles for placement

/**
 * holds the state of the world as an array of Actors
 */
public class GameBoard {
    private final int MAX_ACTORS = 100;
    private ArrayList<Actor> actors = new ArrayList<>();
    private final int width;
    private final int height;

    /**
     * initialises the board to specified dimensions
     * @param width
     * @param height
     */
    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
    }

    protected void parseWorldFile(String fileName) {
        int row, col,i = 0,j = 0;
        try {
            File worldFile = new File(fileName);
            Scanner myReader = new Scanner(worldFile);

            while (myReader.hasNextLine()) {
                String[] data = myReader.nextLine().split(",");
                row = Integer.parseInt(data[1]); col = Integer.parseInt(data[2]);
//  Invalid input can't be accepted
                if (row>width || col>height || row<0 || col<0) {
                    System.out.println("Position is invalid.");
                }
//  Add the actor to our array of actors
                initialise_actor(row,col,data[0],i);
                i++;
                if (i > MAX_ACTORS) {break;}
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            e.printStackTrace();
        }
    }

    protected void printActors() {
        for (Actor actor:actors) {
            System.out.println(actor);
        }
    }

    /**
     * draws the current state of the board
     */
    public void drawBoard() {
        for (Actor actor:actors) {
            actor.image.draw(actor.tilePos.getXPos()+32, actor.tilePos.getYPos()+32);

            if (actor.hasFruit && !actor.getCharacter().equals("gold-tree")) {
                actor.drawFruit();
            }
        }
    }

    /**
     * update game state of gatherers according to algorithm 2
     */
    public void updateGatherers() {
        ArrayList<Gatherer> newGatherers = new ArrayList<>();
        ArrayList<Gatherer> gatherers = new ArrayList<>();
        for (Actor actor: actors) {
            if (actor.getCharacter().equals("gatherer")) {
                gatherers.add((Gatherer) actor);
            }
        }

        for (Gatherer gatherer: gatherers) {
//          step 1
            if (gatherer.isActive()) {
                gatherer.updatePosition();
            }
//          create instances of objects at the gatherer's location to compare with actors on the board
            Fence fence = new Fence(gatherer.tilePos.getXPos(),gatherer.tilePos.getYPos());
            Pool pool = new Pool(gatherer.tilePos.getXPos(),gatherer.tilePos.getYPos());
            Tree tree = new Tree(gatherer.tilePos.getXPos(),gatherer.tilePos.getYPos());
            Hoard hoard = new Hoard(gatherer.tilePos.getXPos(),gatherer.tilePos.getYPos());
            Stockpile stockpile = new Stockpile(gatherer.tilePos.getXPos(),gatherer.tilePos.getYPos());

            for (Actor actor: actors) {
//              step 3, applies if there is a fence on the gatherers position
                if (actor.equals(fence)) {
                    gatherer.setActive(false);
//                  reverse direction to retrace step
                    gatherer.setDirection((gatherer.getDirection()+2)%4);
                    gatherer.updatePosition();
//                  reverse direction again to avoid switching directions
                    gatherer.setDirection((gatherer.getDirection()+2)%4);
                    break;
                }
//              step 6 if gatherer is on a pool
                if (actor.equals(pool)) {
//                  create gatherer at its position
                    Gatherer newGatherer = new Gatherer(gatherer.tilePos.getXPos(),gatherer.tilePos.getYPos());
//                  rotate newGatherer's direction 90 counter-clockwise
                    newGatherer.setDirection((gatherer.getDirection()+1)%4);
//                  do same thing but set direction 90 degrees clockwise
                    Gatherer newGatherer2 = new Gatherer(gatherer.tilePos.getXPos(),gatherer.tilePos.getYPos());
                    newGatherer2.setDirection((gatherer.getDirection()+3)%4);
//                  add cloned gatherers to separate list to later be merged with arraylists;
                    newGatherers.add(newGatherer);newGatherers.add(newGatherer2);
                    newGatherer.updatePosition(); newGatherer2.updatePosition();
//                  remove selected gatherer from board and do nothing else with it
                    gatherer.obsolete = true;
                    continue;
                }
//              step 11 if gatherer is on sign
                if (actor.isSign() && actor.tilePos.equals(gatherer.tilePos)) {
//                  set direction of gatherer to be the direction of the sign
                    gatherer.setDirection(actor.getCharacter());
                }
//              step 13 if gatherer is on tree and not carrying
                if (actor.equals(tree) && !gatherer.isCarrying()) {
                    if (actor.getFruitCount()>0) {
                        actor.decrementFruit();
                        gatherer.setCarrying(true);
//                      rotate 180 degrees
                        gatherer.setDirection((gatherer.getDirection()+2)%4);
                    }
                }
//              step 18 if gatherer is at hoard/stockpile
                if (actor.equals(hoard) || actor.equals(stockpile)) {
                    if (gatherer.isCarrying()) {
                        gatherer.setCarrying(false);
//                      increase storage count
                        actor.incrementFruit();
                    }
//                  rotate 180 degrees
                    gatherer.setDirection((gatherer.getDirection()+2)%4);
                }

            }
        }
//      delete gatherers from the board here to avoid altering the list while it is being iterated over
        for (Gatherer gatherer: gatherers) {
            if (gatherer.obsolete) {
                actors.remove(gatherer);
            }
        }
//      add the newly created gatherers to the board
        actors.addAll(newGatherers);
    }
    /**
     * update game state of thieves according to algorithm 4
     */
    public void updateThieves() {
        ArrayList<Thief> newThieves = new ArrayList<>();
        ArrayList<Thief> thieves = new ArrayList<>();
        for (Actor actor: actors) {
            if (actor.getCharacter().equals("thief")) {
                thieves.add((Thief) actor);
            }
        }

        for (Thief thief: thieves) {
//          step 1
            if (thief.isActive()) {
                thief.updatePosition();
            }
//          create instances of objects at the thief's location to compare with actors on the board
            Fence fence = new Fence(thief.tilePos.getXPos(),thief.tilePos.getYPos());
            Pool pool = new Pool(thief.tilePos.getXPos(),thief.tilePos.getYPos());
            Pad pad = new Pad(thief.tilePos.getXPos(),thief.tilePos.getYPos());
            Gatherer gatherer = new Gatherer(thief.tilePos.getXPos(),thief.tilePos.getYPos());
            Tree tree = new Tree(thief.tilePos.getXPos(),thief.tilePos.getYPos());
            GoldenTree goldTree = new GoldenTree(thief.tilePos.getXPos(),thief.tilePos.getYPos());
            Hoard hoard = new Hoard(thief.tilePos.getXPos(),thief.tilePos.getYPos());
            Stockpile stockpile = new Stockpile(thief.tilePos.getXPos(),thief.tilePos.getYPos());

            for (Actor actor: actors) {
//              step 3, applies if there is a fence on the thieves position
                if (actor.equals(fence)) {
                    thief.setActive(false);
//                  reverse direction to retrace step
                    thief.setDirection((thief.getDirection()+2)%4);
                    thief.updatePosition();
//                  reverse direction again to avoid switching directions
                    thief.setDirection((thief.getDirection()+2)%4);
                    break;
                }
//              step 6 if thief is on a pool
                if (actor.equals(pool)) {
//                  create thief at its position
                    Thief newThief = new Thief(thief.tilePos.getXPos(),thief.tilePos.getYPos());
//                  rotate newThief's direction 90 counter-clockwise
                    newThief.setDirection((thief.getDirection()+1)%4);
//                  do same thing but set direction 90 degrees clockwise
                    Thief newThief2 = new Thief(thief.tilePos.getXPos(),thief.tilePos.getYPos());
                    newThief2.setDirection((thief.getDirection()+3)%4);
//                  add cloned thieves to separate list to later be merged with arraylists;
                    newThieves.add(newThief);newThieves.add(newThief2);
                    newThief.updatePosition(); newThief2.updatePosition();
//                  remove selected thief from board and do nothing else with it
                    thief.obsolete = true;
                    continue;
                }
//              step 11 if thief is on sign
                if (actor.isSign() && actor.tilePos.equals(thief.tilePos)) {
//                  set direction of thief to be the direction of the sign
                    thief.setDirection(actor.getCharacter());
//                    System.out.println(thief.getDirection());
                }
//              step 13 if thief is on pad
                if (actor.equals(pad)) {
                    thief.setConsuming(true);
                }
//              step 15 if thief is on a gatherer
                if (actor.equals(gatherer)) {
//                  turn thief 270 degrees clockwise = 90 degrees anti clockwise
                    thief.setDirection((thief.getDirection()+1)%4);
                }
//              step 17 if thief is on tree and not carrying
                if ((actor.equals(tree) || actor.equals(goldTree)) && !thief.isCarrying()) {
                    if (actor.equals(tree)) {
                        if (actor.getFruitCount()>0) {
                            actor.decrementFruit();
                            thief.setCarrying(true);
                        }
                    }
                    else {
                        thief.setCarrying(true);
                    }

                }
//              step 21 if thief is at hoard
                if (actor.equals(hoard)) {
                    if (thief.isConsuming()) {
                        thief.setConsuming(false);
                        if (!thief.isCarrying()) {
                            if (actor.getFruitCount()>0) {
                                thief.setCarrying(true);
//                              decrease hoard count by 1
                                actor.decrementFruit();
                            }
                            else {
//                              rotate 90 degrees clockwise
                                thief.setDirection((thief.getDirection()+3)%4);
                            }
                        }
                    }
                    else if (thief.isCarrying()) {
                        thief.setCarrying(false);
                        actor.incrementFruit();
//                      rotate 90 degrees clockwise
                        thief.setDirection((thief.getDirection()+3)%4);
                    }
                }
//              step 34 if thief is on stockpile
                if (actor.equals(stockpile)) {
                    if (!thief.isCarrying()) {
                        if (actor.getFruitCount()>0) {
                            thief.setCarrying(true);
                            thief.setConsuming(false);
//                          decrement stockpile count
                            actor.decrementFruit();
//                          rotate 90 degrees clockwise
                            thief.setDirection((thief.getDirection()+3)%4);
                        }
                    }
                    else {
//                      rotate clockwise by 1
                        thief.setDirection((thief.getDirection()+3)%4);
                    }
                }
            }
        }
//      delete thieves from the board here to avoid altering the list while it is being iterated over
        for (Thief thief: thieves) {
            if (thief.obsolete) {
                actors.remove(thief);
            }
        }
//      add the newly created thieves to the board
        actors.addAll(newThieves);
    }

    /**
     * adds an actor to the actors array based on its type
     * @param row
     * @param column
     * @param character
     * @param index
     */
    private void initialise_actor(int row,int column, String character, int index) {

        switch (character) {
            case "Tree":
                actors.add(new Tree(row,column));
                break;
            case "Gatherer":
                actors.add(new Gatherer(row,column));
                break;
            case "Fence":
                actors.add(new Fence(row,column));
                break;
            case "GoldenTree":
                actors.add(new GoldenTree(row,column));
                break;
            case "Hoard":
                actors.add(new Hoard(row,column));
                break;
            case "Pad":
                actors.add(new Pad(row,column));
                break;
            case "Pool":
                actors.add(new Pool(row,column));
                break;
            case "Stockpile":
                actors.add(new Stockpile(row,column));
                break;
            case "Thief":
                actors.add(new Thief(row,column));
                break;
            default:
                assert character.substring(0,4).equals("Sign");
//              pass the direction as the second word of the string: left,down,up,right
                actors.add(new Sign(row, column, character.substring(4)));
        }
    }
}
