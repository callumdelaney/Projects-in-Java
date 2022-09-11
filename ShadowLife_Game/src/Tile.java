/**
 * The tile class separates the window into a grid of squares
 */
public class Tile {
    public static final int SIZE = 64;
    private int row;
    private int column;

    public Tile(int x, int y) {
        this.row = y/SIZE;
        this.column = x/SIZE;
    }

    /**
     * get the scaled row
     * @return row
     */
    public int getRow() {
        return row;
    }

    /**
     * get the scaled column
     * @return column
     */
    public int getColumn() {
        return column;
    }

    /**
     * get actual Y position
     * @return y position on window
     */
    public int getYPos() {
        return row*SIZE;
    }

    /**
     * get actual X position
     * @return x position in window
     */
    public int getXPos() { return column*SIZE; }

    /**
     * sets scaled row
     * @param toRow this is the destination row
     */
    public void setRow(int toRow) { row=toRow; }

    /**
     * sets scaled column
     * @param toColumn this is the destination column
     */
    public void setColumn(int toColumn) {
        column=toColumn;
    }

    public String toString() {
        return "Tile{row=" + row + ", column=" + column + "}";
    }

    public boolean equals(Tile other) {
        return row == other.row && column == other.column;
    }
}
