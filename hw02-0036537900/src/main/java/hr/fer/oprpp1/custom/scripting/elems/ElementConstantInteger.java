package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

public class ElementConstantInteger extends Element{
    private int value;

    public ElementConstantInteger(int value){
        this.value = value;
    }

    /**
     * Getter for the  value
     * @return returns the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Method returns the value of the property value
     * @return returns the String value of the property value
     */
    @Override
    public String asText(){
        return String.valueOf(value);
    }

    /**
     * Method compares this with given Object
     * @param o Object to be compared with
     * @return returns true if this and given Object are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElementConstantInteger)) return false;
        ElementConstantInteger that = (ElementConstantInteger) o;
        return value == that.value;
    }


}
