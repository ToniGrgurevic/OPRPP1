package hr.fer.oprpp1.custom.collections;

import java.util.function.Function;

/**
 *  Represents some general collection of objects
  */
public interface Collection<T> {

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

    default  void addAllSatisfying(Collection<? extends T> col, Tester<? super T> tester){

        ElementsGetter<T> iterator = (ElementsGetter<T>) col.createElementsGetter();
        while(iterator.hasNextElement()){
            T element = (T) iterator.getNextElement();
            if(tester.test(element)){
                this.add(element);
            }
        }

    }

    public default <Z>   void  copyTransformedIfAllowed(Collection<Z> col, Function<? super T , ? extends  Z> processor, Tester<? super T> tester){

        ElementsGetter<T> iterator = (ElementsGetter<T>) this.createElementsGetter();
        while(iterator.hasNextElement()){
            T element =  iterator.getNextElement();
            if(tester.test(element)){
                col.add(processor.apply(element));
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
    public boolean contains(T value);

    /**
     * Removes one occurrence of the given value from the collection if it exists and returns true if the removing
     * was successful.
     * @param value object to be removed
     * @return true if the object was removed, false otherwise
     */
     boolean remove( T value);

    /**
     * Adds the given object into this collection.
     * @param value object to be added
     */
    void add(T value);

    /**
    *Allocates new array with size equals to the size of this collections.
    *This method never returns null
    * @return new array with size equals to the size of this collections
    * @throws UnsupportedOperationException if the collection is empty
     */
     T[] toArray();

    /**
     * Method calls processor.process(...) for each element of this collection.Uses
     * ElementsGetter to iterate collection
     */
     default void forEach(Processor<? super T> processor){
        ElementsGetter<T> iterator = this.createElementsGetter();
        while(iterator.hasNextElement()){
            processor.process(iterator.getNextElement());
        }
     }


    /**
     * Crates new ElementsGetter for this collection and returns it.
     */
    ElementsGetter<T> createElementsGetter();


    /**
     * Method adds into the current collection all elements from the given collection. This other collection
     * remains unchanged.
     * @param other collection whose elements are to be added to this collection
     */
    default void addAll(Collection<? extends T> other){

        var reference = this;
        class Adder implements   Processor<T> {
            @Override
            //adds elements to collection
            public void process(T value) {
                reference.add(value);
            }
        }
        Processor<T> processor = new Adder();
        other.forEach(processor);

    }

    /**
     * Removes all elements from this collection.
     */
    public void clear();
}
