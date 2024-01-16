package hr.fer.oprpp1.custom.collections;



/**
 * Objects of this class represent stack of objects
 */
public class ObjectStack<T> {

    /**private  collection wich holds the elements of the stack*/
    private ArrayIndexedCollection<T> collection;


    /**
     * Constructs new ObjectStack.At beginning the stack is empty
     */
    public ObjectStack() {
        collection = new ArrayIndexedCollection<>();
    }

    /**
     * Checks if the stack is empty
     * @return true if the stack is empty, false otherwise
     */
    public boolean isEmpty(){
        return collection.isEmpty();
    }

    /**
     * Returns the size of the stack
     * @return size of the stack
     */
    public int size(){
        return collection.size();
    }

    /**
     * Pushes the given value on the stack if it is not null
     * @param value value to be pushed on the stack
     * @throws NullPointerException if the given value is null
     */
    public void push(T value){
        if (value == null) throw new NullPointerException("Given value is null witch is not allowed");
        collection.add(value);
    }

    /**
     * Returns the value on the top of the stack
     * @return value on the top of the stack
     * @throws EmptyStackException if the stack is empty
     */
    public Object pop(){
        if (collection.isEmpty()) throw new EmptyStackException("Stack is empty");
        T value = collection.get(collection.size()-1);
        collection.remove(value);
        return value;
    }

    /**
     * Returns the value on the top of the stack without removing it
     * @return value on the top of the stack
     * @throws EmptyStackException if the stack is empty
     */
    public T peek(){
        if (collection.isEmpty()) throw new EmptyStackException("Stack is empty");
        return collection.get(collection.size()-1);
    }

    /**
     * Removes all elements from the stack
     */
    public void clear(){
        collection.clear();
    }



}
