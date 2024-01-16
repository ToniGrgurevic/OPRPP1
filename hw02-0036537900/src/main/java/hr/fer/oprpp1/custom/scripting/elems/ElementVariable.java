package hr.fer.oprpp1.custom.scripting.elems;

import java.util.Objects;

public class ElementVariable extends Element{

    /*  read-only String property */
    private String name;

    public ElementVariable(String name) {
        this.name = name;
    }

    /**
     * Getter for name.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Method retuns the name of the variable
     * @return  the value of name property
     */
    @Override
    public String asText() {
        return this.name;
    }


    /**
     * Method compares this with given Object
     * @param o Object to be compared with
     * @return returns true if this and given Object are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ElementVariable)) return false;
        ElementVariable that = (ElementVariable) o;
        return Objects.equals(name, that.name);
    }

}
