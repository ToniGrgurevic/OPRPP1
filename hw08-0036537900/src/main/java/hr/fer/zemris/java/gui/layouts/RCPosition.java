package hr.fer.zemris.java.gui.layouts;


import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Class representing restrictions of CalcLayout menager
 * has 2 read-only attributes
 * web of 5 rows and 7 columns
 */
public class RCPosition {

    private final int column;
    private final int row;

    public RCPosition(int row, int column) {
        this.column = column;
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }

    /**
     * Factory for objects of type RCPosition
     * @param text that when paresed results in RCPosition object
     * @return RCPosition object
     */
    public static RCPosition parse(String text){
        String[] arguments = text.split(",");
        List<Integer> values = Arrays.stream(arguments)
                .map(Integer::parseInt).toList();
        return new RCPosition(values.get(0), values.get(1));

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RCPosition)) return false;
        RCPosition that = (RCPosition) o;
        return column == that.column && row == that.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, row);
    }
}
