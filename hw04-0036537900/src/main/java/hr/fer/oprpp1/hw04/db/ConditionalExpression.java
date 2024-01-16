package hr.fer.oprpp1.hw04.db;

/**
 * Class that represents a conditional expression.Object of this class contains getter for a field value,
 * string literal(with wich we are comparing feild value) and comparison operator.
 */

public class ConditionalExpression {


    IFieldValueGetter feildValue;
    String literal;
    IComparisonOperator comparison;


    /**
     * Constructor for a conditional expression.
     * @param feildValue getter for a field value
     * @param literal string literal
     * @param comparison comparison operator
     */
    public ConditionalExpression(IFieldValueGetter feildValue, String literal, IComparisonOperator comparison) {
        this.feildValue = feildValue;
        this.literal = literal;
        this.comparison = comparison;
    }

    /**
     * Getter for a field value.
     * @return field value
     */
    public IFieldValueGetter getFieldGetter() {
        return feildValue;
    }

    /**
     * Getter for a string literal.
     * @return string literal
     */
    public String getStringLiteral() {
        return literal;
    }

    /**
     * Getter for a comparison operator.
     * @return comparison operator
     */
    public IComparisonOperator getComparisonOperator() {
        return comparison;
    }
}
