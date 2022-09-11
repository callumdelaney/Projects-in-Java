/**
 * Tick represents a portion of time in game
 */
public class Tick {
    protected int currentTick;
    private long currentTime;
//  time between ticks
    private final int tickSeparation;
    public boolean tickActive;

    /**
     * create a tick
     * @param tickSeparation time between ticks
     */
    public Tick(int tickSeparation) {
        currentTick = 0;
        currentTime = System.currentTimeMillis();
        this.tickSeparation = tickSeparation;
        tickActive = true;
    }

    /**
     * update currentTick after a time specified by tickSeparation
     */
    public boolean tickUpdate() {
        if (System.currentTimeMillis()-currentTime >= tickSeparation) {
            currentTick++;
            currentTime = System.currentTimeMillis();
            tickActive = true;
            return true;
        }
        if (currentTick >= 1000000) {
            currentTick = 0;
            System.out.println("tick reset to 0");
        }
        return false;
    }
    public String toString() {
        return "{currentTick = " + currentTick + "}";
    }
}
