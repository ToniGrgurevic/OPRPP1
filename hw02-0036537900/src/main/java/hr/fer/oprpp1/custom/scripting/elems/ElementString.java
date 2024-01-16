package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

public class ElementString extends Element{
    private String value;


    public ElementString(String value) {
        this.value = value;
    }

    /**
     * Getter for value.
     * @return value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Returns value as text.
     * @return return value property
     */
    @Override
    public String asText() {
        return "\""+this.value.replace("\"","\\\"")+"\"";
    }


    /**
     * Method compares this with given Object
     * @param o Object to be compared with
     * @return returns true if this and given Object are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElementString)) return false;
        ElementString that = (ElementString) o;
        return Objects.equals(value, that.value);
    }

}
