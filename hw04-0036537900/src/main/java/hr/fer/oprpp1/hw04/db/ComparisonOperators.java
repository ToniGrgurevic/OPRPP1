package hr.fer.oprpp1.hw04.db;


/**
 * class that represents a comparison operator.Contains 7 static types of itself.
 * Each type represents a different comparison operator and has implemented strategy for himself.
 */
public class ComparisonOperators {

    public static final IComparisonOperator LESS = (value1, value2) -> value1.compareTo(value2) < 0;
    public static final IComparisonOperator LESS_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) <= 0;
    public static final IComparisonOperator GREATER = (value1, value2) -> value1.compareTo(value2) > 0;
    public static final IComparisonOperator GREATER_OR_EQUALS = (value1, value2) -> value1.compareTo(value2) >= 0;
    public static final IComparisonOperator EQUALS = (value1, value2) -> value1.compareTo(value2) == 0;
    public static final IComparisonOperator NOT_EQUALS = (value1, value2) -> value1.compareTo(value2) != 0;

    public static final IComparisonOperator LIKE = (value1, value2) -> {

        int dot = value2.indexOf('*');

        if(dot == -1)
            return value1.equals(value2);
         else if (dot ==0)
            return  value1.endsWith(value2.substring(1));
        else if(dot == value2.length()-1)
            return value1.startsWith(value2.substring(0, dot));

        String[] parts = value2.split("\\*");
        return value1.startsWith(parts[0]) && value1.endsWith(parts[1])
                  && value1.length() >= value2.length()-1  ;
    };

}
