package hr.fer.zemris.java.gui.charts;

/**
 * Class representing a pair of values (x,y)
 * Used in BarChart class.Class has only 2 read-only properties x and y
 */
public class XYValue {
    /**
     * read-only property y
     */
    private final int x;

    /**
     * read-only property x
     */
    private final int y;

    public XYValue(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public XYValue(String x, String y) {
        this(Integer.parseInt(x), Integer.parseInt(y));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
