/**
 * actor that represents a gatherer
 */
public class Gatherer extends Actor implements Movable {
    private int directionOfMovement;
    private boolean carrying;
    private boolean active;
//  if a gatherer is declared obsolete it can be removed from the board
    public boolean obsolete;

    /**
     * Initialise gatherer according to algorithm 1
     * @param xPos x position of gatherer
     * @param yPos y position of gatherer
     */
    public Gatherer(int xPos, int yPos) {
        super("gatherer",xPos,yPos,false,0);
        directionOfMovement = LEFT;
        carrying = false;
        active = true;
        obsolete = false;
    }

//  gatherers have the ability to walk in one of four directions

    /**
     * set gatherer direction
     * @param direction as an integer
     */
    public void setDirection(int direction) {
        directionOfMovement = direction;
    }
    /**
     * set gatherer direction
     * @param direction as a string
     */
    public void setDirection(String direction) {
        switch (direction) {
            case "Left":
                directionOfMovement = LEFT;
                break;
            case "Right":
                directionOfMovement = RIGHT;
                break;
            case "Up":
                directionOfMovement = UP;
                break;
            case "Down":
                directionOfMovement = DOWN;
                break;
        }
    }

    /**
     * returns direction of travel
     * @return directionOfMovement variable
     */
    public int getDirection() {
        return directionOfMovement;
    }

    /**
     * make gatherer walk one tile in its direction
     */
    public void updatePosition() {
        switch(directionOfMovement) {
            case LEFT:
                super.tilePos.setColumn(super.tilePos.getColumn()-1);
                break;
            case RIGHT:
                super.tilePos.setColumn(super.tilePos.getColumn()+1);
                break;
            case UP:
                super.tilePos.setRow(super.tilePos.getRow()-1);
                break;
            case DOWN:
                super.tilePos.setRow(super.tilePos.getRow()+1);
                break;
        }
    }

    /**
     * gets activity of gatherer
     * @return
     */
    public boolean isActive() {
        return active;
    }
    /**
     * returns true if gatherer is carrying
     */
    public boolean isCarrying() {
        return carrying;
    }
    /**
     * sets activity of gatherer
     */
    public void setActive(boolean result) {
        active = result;
    }
    /**
     * sets the carry status of gatherer
     */
    public void setCarrying(boolean result) {
        carrying = result;
    }

    public String toString() {
        return super.toString();
    }

}
