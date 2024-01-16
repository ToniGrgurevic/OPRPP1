package hr.fer.oprpp1.custom.scripting.nodes;

/**
 * A node representing a piece of textual data. It inherits from Node class
 */
public class TextNode extends Node{
    private String text;


    public TextNode(String text) {
        super();
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text.replace("\\", "\\\\").replace("{", "\\{");
    }

    /**
     * Method checks if this TextNode is equal to the given object
     * @param obj Object to be compared with
     * @return true if this equals to obj, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof TextNode other)) {
            return false;
        }
        return this.text.equals(other.text);
    }

}

