package hr.fer.oprpp1.custom.collections;

/**
 * Object of this class  knows how to process some other object
 */
public interface Processor {

    /**
     * Method that processes the given value
     * @param value to be processed
     */
     void process(Object value);
}


