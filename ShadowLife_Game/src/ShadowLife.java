import bagel.*;

/**
 * Project 2
 * Bagel made by
 * @author Eleanor McMurtry
 */
public class ShadowLife extends AbstractGame {
    public static final int WINDOW_WIDTH = 1024;
    public static final int WINDOW_HEIGHT = 768;
    public final int TICK_LENGTH;
    public final int MAX_TICKS;
    private final Image background;
    GameBoard gameBoard = new GameBoard(WINDOW_WIDTH,WINDOW_HEIGHT);
    Tick tick;
    
    public ShadowLife(int tickRate, int maxTicks, String worldFile) {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, "PROJECT 2!");
        TICK_LENGTH = tickRate;
        background = new Image("res/images/background.png");
        gameBoard.parseWorldFile(worldFile);
        tick = new Tick(TICK_LENGTH);
        MAX_TICKS = maxTicks;
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
//      cmd ln args must be correct
        if ((args.length == 3) && isValidInt(args[0]) && isValidInt(args[1])) {
            ShadowLife game = new ShadowLife(Integer.parseInt(args[0]),Integer.parseInt(args[1]),args[2]);
            game.run();
        }
        else {
            System.out.println("usage: ShadowLife <tick rate> <max ticks> <world file>");
            System.exit(-1);
        }
    }

    /**
     * Performs a game state update.
     */
    @Override
    public void update(Input input) {
//      render the background centred in the middle of the window
        background.draw(Window.getWidth()/2.0,Window.getHeight()/2.0);
//      Draw the current state of the game board
        gameBoard.drawBoard();

//      every game tick, update the position of the gatherers, tickActive is reset to true
        if (tick.tickUpdate()) {
            gameBoard.updateGatherers();
            gameBoard.updateThieves();
        }

//      time out if max ticks is reached
        if (tick.currentTick > MAX_TICKS) {
            System.out.println("Timed out");
            System.exit(-1);
        }

        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
    }

    /**
     *
     * @param strNum string
     * @return true if string is a valid integer
     */
    public static boolean isValidInt(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Integer.parseInt(strNum);
            if (d < 0) {
                return false;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
