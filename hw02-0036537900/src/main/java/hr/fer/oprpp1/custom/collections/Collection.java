package hr.fer.oprpp1.custom.collections;

/**
 *  Represents some general collection of objects
  */
public interface Collection {

    /**
     * Returns true if collection contains no objects and false otherwise. Implemented using method size()
     * @return true if size of collection is 0, false otherwise
     */
     default public boolean isEmpty(){

         return this.size() == 0;
    }

    /**
     * Adds all elements from the given collection into this collection. Other collection remains unchanged.
     * @param col collection whose elements are to be added to this collection
     * @param tester Tester object that checks if the given object satisfies some condition
     */
    default void addAllSatisfying(Collection col, Tester tester){

        ElementsGetter iterator = col.createElementsGetter();
        while(iterator.hasNextElement()){
            Object element = iterator.getNextElement();
            if(tester.test(element)){
                this.add(element);
            }
        }

    }






    /**
     * Returns the number of currently stored objects in this collections.
     * @return number of currently stored objects in this collections
     */
    public int size();


    /** Returns true only if the collection contains given value, as determined by equals method. It is OK to ask if collection contains null.Implemented here to always return false.
     * @param value of the object to be checked
     * @return  true only if the collection contains given value, otherwise false
     */
    public boolean contains(Object value);

    /**
     * Removes one occurrence of the given value from the collection if it exists and returns true if the removing
     * was successful.
     * @param value object to be removed
     * @return true if the object was removed, false otherwise
     */
     boolean remove(Object value);

    /**
     * Adds the given object into this collection.
     * @param value object to be added
     */
    void add(Object value);

    /**
    *Allocates new array with size equals to the size of this collections.
    *This method never returns null
    * @return new array with size equals to the size of this collections
    * @throws UnsupportedOperationException if the collection is empty
     */
     Object[] toArray();

    /**
     * Method calls processor.process(...) for each element of this collection.Uses
     * ElementsGetter to iterate collection
     */
     default void forEach(Processor processor){
        ElementsGetter iterator = this.createElementsGetter();
        while(iterator.hasNextElement()){
            processor.process(iterator.getNextElement());
        }
     }


    /**
     * Crates new ElementsGetter for this collection and returns it.
     */
    ElementsGetter createElementsGetter();


    /**
     * Method adds into the current collection all elements from the given collection. This other collection
     * remains unchanged.
     * @param other collection whose elements are to be added to this collection
     */
    default void addAll(Collection other){

        var reference = this;
        class Adder implements   Processor{
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
     * Removes all elements from this collection.
     */
    public void clear();
}
