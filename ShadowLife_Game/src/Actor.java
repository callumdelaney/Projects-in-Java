import bagel.Font;
import bagel.Image;

/**
 * The actor class is the parent class to all characters in the game
 */
public abstract class Actor {
    protected Image image;
    private final String character;
    protected Tile tilePos;
    protected boolean hasFruit;
    private int fruitCount;
//  directions are associated with positions in a circle, for ease of changing directions
    public static final int RIGHT = 0, UP = 1, LEFT = 2, DOWN = 3;

    /**
     * abstract template for characters in project 2
     * @param character name of character
     * @param xPos x position
     * @param yPos y position
     * @param hasFruit checks if actor can store fruit
     * @param fruitCount number of fruit associated with actor
     */
    public Actor(String character, int xPos, int yPos, boolean hasFruit, int fruitCount) {
        this.character = character;
        this.image = new Image("res/images/"+character+".png");
        this.hasFruit = hasFruit;
        this.fruitCount = fruitCount;
        this.tilePos = new Tile(xPos,yPos);
    }

    /** gets actor name
     * @return character
     */
    public String getCharacter() {
        return character;
    }

    protected void drawFruit() {
        Font font = new Font("res/VeraMono.ttf",18);
        assert (hasFruit);
//      draw the number to the top left of the tile, which sits at about (x-30,y-20), then add 32 as a fitting constant
        font.drawString(String.format("%d",fruitCount),tilePos.getXPos()+2,tilePos.getYPos()+12);
    }

    protected void decrementFruit() {
        fruitCount--;
        if (fruitCount < 0) {
            fruitCount = 0;
        }
    }
    protected void incrementFruit() {
        fruitCount++;
    }

    /**
     * gets the number of fruit actor is carrying
     * @return fruitCount
     */
    public int getFruitCount() {
        return fruitCount;
    }


    @Override
    public String toString() {
        return "Actor{" +
                "character=" + character +
                ", tilePos=" + tilePos +
                '}';
    }
    public boolean isActor(String name) {
        return character.equals(name);
    }

    /**
     * method isSign to avoid complicated logical equality having four directions
     * @return
     */
    public boolean isSign() {
        return character.equals("Left") || character.equals("Right") ||
                character.equals("Up") || character.equals("Down");
    }
    /**
     * check for logical equality by comparing tilePos and name
     * @param other
     * @return boolean
     */
    public boolean equals(Actor other) {
        return character.equals(other.character) && tilePos.equals(other.tilePos);
    }

}
