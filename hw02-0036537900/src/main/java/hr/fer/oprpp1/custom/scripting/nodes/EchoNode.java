package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;

import java.util.Arrays;

/*
 *a node representing a command which generates some textual output dynamically. It inherits
 *from Node class.
 */
public class EchoNode extends Node{
/* Array that holds elements of EchoNode */
    Element[] elements;




    public EchoNode(Element[] elements) {
        this.elements = elements;
    }

    public Element[] getElements() {
        return elements;
    }

    @Override
    public String toString() {
        StringBuilder sb =new StringBuilder();
        sb.append(" {$= ");
        for(var element : elements) {
            sb.append(element.asText()).append(" ");
        }
        sb.append("$} ");

        return sb.toString();
    }


    /**
     * Method checks if this EchoNode is equal to the given object
     * @param obj Object to be compared with
     * @return true if this equals to obj, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof EchoNode other)) {
            return false;
        }
        if(this.elements.length != other.elements.length) {
            return false;
        }
        for(int i = 0; i < this.elements.length; i++) {
            if(!this.elements[i].equals(other.elements[i])) {
                return false;
            }
        }
        return true;
    }
}
