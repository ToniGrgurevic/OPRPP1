package hr.fer.oprpp1.custom.collections;
/**
 * interface witch used to iterate over the elements of the collection.
 *There are no restrictions on the number of ElementsGetters that can be created for one collection.
 */
public interface ElementsGetter {

    /**
     * checks if there is next element in the collection
     * @return true if there is next element, false otherwise
     */
     boolean hasNextElement();

    /**
     * Returns the next element in the collection
     * @return next element in the collection
     */
    Object getNextElement();

    /**
     * Calls the process method of the processor for each element of the collection that has not yet been iterated
     *  by this ElementsGetter.
     * @param p processor
     */
    public default void processRemaining(Processor p){
        	while(hasNextElement()) {
        		p.process(getNextElement());
        	}
    }

}
