package hr.fer.oprpp1.hw04.db;

import java.util.List;

/**
 * Class impements IFilter interface and represents a filter that will be used to filter students with
 * conditions given in constructor.
 */
public class QueryFilter implements  IFilter{

    List<ConditionalExpression> conditions;

    /**
     * Constructor for QueryFilter
     * @param conditions list of conditional expressions that will be used to filter students
     */
    public QueryFilter(List<ConditionalExpression> conditions) {
        this.conditions = conditions;
    }


    /**
     * Method that checks if studentRecord satisfies all conditions
     * @param record studentRecord to be checked
     * @return true if record satisfies all conditions, false otherwise
     */
    @Override
    public boolean accepts(StudentRecord record) {
        for(var condition : conditions){
            if(!condition.getComparisonOperator().satisfied(condition.getFieldGetter().get(record),condition.getStringLiteral()))
                return false;
        }
        return true;
    }
}
