package hr.fer.oprpp1.hw04.db;


/**
 * Interface that represents a filter.
 */
public interface IFilter {

    /**
     * If record sataisfies the filter returns true, otherwise false.
     * @param record studentRecord to be checked
     * @return true if record satisfies the filter, otherwise false
     */
    public boolean accepts(StudentRecord record);
}

