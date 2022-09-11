/**
 * thief is a subclass of actor
 */
public class Thief extends Actor implements Movable {
    private int directionOfMovement;
    private boolean carrying;
    private boolean consuming;
    private boolean active;
    public boolean obsolete;

    /**
     * create a thief
     * @param xPos
     * @param yPos
     */
    public Thief(int xPos,int yPos) {
        super("thief",xPos,yPos,false,0);
        directionOfMovement = UP;
        carrying = false;
        consuming = false;
        active = true;
        obsolete = false;
    }

    /**
     * set direction of movement
     * @param direction this is the destination direction
     */
    public void setDirection(int direction) {
        directionOfMovement = direction;
    }
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
     * get direction of movement
     * @return directionOfMovement
     */
    public int getDirection() { return directionOfMovement; }
    /**
     * get activity of thief
     * @return active
     */
    public boolean isActive() {
        return active;
    }
    /**
     * get carry status
     * @return carrying
     */
    public boolean isCarrying() {
        return carrying;
    }
    /**
     * set activity
     */
    public void setActive(boolean result) {
        active = result;
    }
    /**
     * set carry status
     */
    public void setCarrying(boolean result) {
        carrying = result;
    }
    /**
     * get consuming status
     */
    public boolean isConsuming() { return consuming; }
    /**
     * set consuming status
     */
    public void setConsuming(boolean result) { consuming = result; }

    /**
     * makes thief walk 1 tile in direction of movement
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
    public String toString() {
        return super.toString();
    }
}
