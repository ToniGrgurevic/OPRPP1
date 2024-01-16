package hr.fer.oprpp1.jmbag0036537900.collections;

/**
 *  Represents some general collection of objects
  */
public class Collection {

    /**
     * Returns true if collection contains no objects and false otherwise. Implemented using method size()
     * @return true if size of collection is 0, false otherwise
     */
    public boolean isEmpty(){
        return this.size() == 0;
    }

    /**
     * Returns the number of currently stored objects in this collections.Implemented here to always return 0.
     * @return number of currently stored objects in this collections
     */
    public int size(){
        return 0;
    }


    /** Returns true only if the collection contains given value, as determined by equals method. Implemented here
     *  to always return false. It is OK to ask if collection contains null.Implemented here to always return false.
     * @param value of the object to be checked
     * @return  true only if the collection contains given value, otherwise false
     */
    public boolean contains(Object value){
        return false;
    }


    /**
     * Removes one occurrence of the given value from the collection if it exists and returns true if the removing
     * was successful. Implemented here to always return false.
     * @param value object to be removed
     * @return true if the object was removed, false otherwise
     */
    public boolean remove(Object value){
        return false;
    }

    /**
     * Adds the given object into this collection.Implemented here to do nothing.
     * @param value object to be added
     */
    void add(Object value){
    }

    /**
    *Allocates new array with size equals to the size of this collections.
    *This method never returns null
    * @return new array with size equals to the size of this collections
    * @throws UnsupportedOperationException if the collection is empty
     */
    public Object[] toArray(){
        throw new UnsupportedOperationException("Size of collection is 0");
    }

    /**
     * Method calls processor.process(...) for each element of this collection.
     * Implemented here to do nothing.
     */
    public void forEach(Processor processor){    }


    /**
     * Method adds into the current collection all elements from the given collection. This other collection
     * remains unchanged.
     * @param other collection whose elements are to be added to this collection
     */
    public  void addAll(Collection other){

        var reference = this;
        class Adder extends  Processor{
            @Override
            //adds elements to collection
            public void process(Object value) {
                reference.add(value);
            }
        }
        Processor processor = new Adder();
        other.forEach(processor);

    }

    /**
     * Removes all elements from this collection. Implemented here to do nothing.
     */
    public void clear(){
    }
}
