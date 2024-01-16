package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

public class ElementOperator extends Element{
    /*  read-only String property */
    private String symbol;


    public ElementOperator(String name) {
        this.symbol = name;
    }

    /**
     * Getter for symbol.
     * @return symbol
     */
    public String getName() {
        return symbol;
    }

    /**
     * Method retuns the symbol of the variable
     * @return  the value of symbol property
     */
    @Override
    public String asText() {
        return this.symbol;
    }

    /**
     * Method compares this with given Object
     * @param o Object to be compared with
     * @return returns true if this and given Object are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElementOperator)) return false;
        ElementOperator that = (ElementOperator) o;
        return Objects.equals(symbol, that.symbol);
    }


}
