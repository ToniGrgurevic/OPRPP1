package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ElementsGetter;

/*
*A node representing an entire document. It inherits from Node class.
 */
public class DocumentNode extends Node{

    public DocumentNode() {
    }

    /**
        * Method returns the String representation of the DocumentNode
        * @return returns the String representation of the DocumentNode
        */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        ElementsGetter iterator = children.createElementsGetter();
        while(iterator.hasNextElement()) {
            Node node = (Node) iterator.getNextElement();
            sb.append(node.toString());
        }
        return sb.toString();
    }

    /**
        * Method checks if two DocumentNodes are equal
        * @param obj Object to be compared with
        * @return returns true if two DocumentNodes are equal, false otherwise
        */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof DocumentNode other)) {
            return false;
        }
        if(this.numberOfChildren() != other.numberOfChildren()) {
            return false;
        }
        for(int i = 0; i < this.numberOfChildren(); i++) {
            if(!this.getChild(i).equals(other.getChild(i))) {
                return false;
            }
        }
        return true;
    }
}
