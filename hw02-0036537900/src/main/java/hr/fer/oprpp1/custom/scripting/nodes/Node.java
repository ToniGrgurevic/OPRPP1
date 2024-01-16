package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

/**
 *  used for representation of structured documents.
 *   Base class for all graph nodes
 */
public class Node {

    ArrayIndexedCollection children;

    public Node() {

    }

    /**
        * Adds given child to an internally managed collection of children.
        * @param child child to add
        */
        public void addChildNode(Node child) {
            if(children == null) {
                children = new ArrayIndexedCollection();
            }
            children.add(child);
        }

        /**
        * Returns a number of (direct) children.
        * @return number of (direct) children
        */
        public int numberOfChildren() {
            if (children == null) {
                return 0;
            }
            return children.size();

        }

        /**
        * Returns selected child or throws an exception if the index is invalid.
        * @param index index of the child to return
        * @return selected child
        */
        public Node getChild(int index){
            if(index < 0 || index > children.size() - 1) {
                throw new ArrayIndexOutOfBoundsException();
            }
            else  return (Node) children.get(index);

        }

        /**
        * Returns a string representation of the node and all its children.
        * @return a string representation of the node and all its children
        */
        public String toString() {
            throw new UnsupportedOperationException();
        }
}
