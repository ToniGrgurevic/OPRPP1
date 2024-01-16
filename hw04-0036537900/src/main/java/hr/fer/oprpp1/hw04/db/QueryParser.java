package hr.fer.oprpp1.hw04.db;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * represents a parser of query statement and it gets query string through
 * constructor(without query keyword) and parses it into list of conditional expressions
 */
public class QueryParser {

    private String query;
    private boolean direct;

    IFieldValueGetter[] showing = null;

    List<ConditionalExpression> expressions;

    public IFieldValueGetter[] getShowing() {
        return showing;
    }

    /**
     * Constructor for QueryParser
     * @param query query string
     */
    public QueryParser(String query) {
        this.query = query.strip();
        this.direct = false;
        this.expressions = new LinkedList<>();
        parse();
    }

    /**
     * Method that parses query string into list of conditional expressions
     *
     * <conditions> -> <condition> AND <conditions> | <condition>
     * <condition> -> <field> <operator> <value>
     * <field> -> firstName | lastName | jmbag
     * <operators> -> = | != | > | >= | < | <= | LIKE
     * <value> -> string
     * @throws IllegalArgumentException if query is invalid
     */
    private void parse() {
        int index=0;
        String expression= query.substring(index).strip();

       while(expression.strip().length() != 0){

         IFieldValueGetter field= null;
          expression= expression.substring(index).strip();

           if(expression.startsWith("jmbag")){
               field = FieldValueGetters.JMBAG;
               index+="jmbag".length();
           }
           else if(expression.startsWith("firstName")){
               field = FieldValueGetters.FIRST_NAME;
               index+="firstName".length();
           }
           else if(expression.startsWith("lastName")){
               field = FieldValueGetters.LAST_NAME;
               index+="lastName".length();
           }
           else
               throw new IllegalArgumentException("Invalid field name");


              expression = expression.substring(index).strip();
                index=0;
                IComparisonOperator operator = null;

                if(expression.startsWith("=")){
                    operator = ComparisonOperators.EQUALS;
                    index+="=".length();
                }
                else if(expression.startsWith("!=")){
                    operator = ComparisonOperators.NOT_EQUALS;
                    index+="!=".length();
                }
                else if(expression.startsWith(">=")){
                    operator = ComparisonOperators.GREATER_OR_EQUALS;
                    index+=">=".length();
                }
                else if(expression.startsWith(">")){
                    operator = ComparisonOperators.GREATER;
                    index+=">".length();
                }
                else if(expression.startsWith("<=")){
                    operator = ComparisonOperators.LESS_OR_EQUALS;
                    index+="<=".length();
                }
                else if(expression.startsWith("<")){
                    operator = ComparisonOperators.LESS;
                    index+="<".length();
                }
                else if(expression.startsWith("LIKE")){
                    operator = ComparisonOperators.LIKE;
                    index+="LIKE".length();
                }
                else
                    throw new IllegalArgumentException("Invalid operator");




            int stringStart = expression.indexOf('"', index);
            int stringEnd = expression.indexOf('"', stringStart+1);
            if(stringEnd == -1)
                throw new IllegalArgumentException("Invalid string literal");


            String stringLiterals = expression.substring(stringStart+1, stringEnd);
            index = stringEnd+1;

           expression = expression.substring(index).strip();
            index=0;

            expressions.add(new ConditionalExpression(field, stringLiterals, operator));


            if(expression.strip().length() == 0){ //kraj uvjeta
                break;
            }else if(expression.substring(0,3).toUpperCase().equals("AND")) //break condition
                expression = expression.substring(3).strip();
           else if(expression.substring(0,7).toUpperCase().equals("SHOWING")){ //break condition
                expression = expression.substring(7).strip();
                showing = Arrays.stream(expression.split(", "))
                        .map((v) -> {
                            if (v.equals("jmbag")) return FieldValueGetters.JMBAG;
                            else if (v.equals("firstName")) return FieldValueGetters.FIRST_NAME;
                            else if (v.equals("lastName")) return FieldValueGetters.LAST_NAME;
                            else throw new IllegalArgumentException("Invalid showing");
                        }).toArray(IFieldValueGetter[]::new);
                break;
           }else
                throw new IllegalArgumentException("Invalid query"); //greska u parsiranju
       }

       if(expressions.size() == 1 && expressions.get(0).getComparisonOperator() == ComparisonOperators.EQUALS
            && expressions.get(0).getFieldGetter() == FieldValueGetters.JMBAG)
           direct=true;


    }


    /**
     * Checks if query is direct query
     * @return true if query is direct query, false otherwise
     */
    public boolean isDirectQuery() {
        return direct;
    }

    /**
     * Returns queried jmbag if query is direct query
     * @return queried jmbag
     * @throws IllegalStateException if query is not direct query
     */
    public String getQueriedJMBAG() {
        if (!direct) throw new IllegalStateException("Query is not direct query");
        return expressions.get(0).getStringLiteral();
    }

    /**
     * Returns list of conditional expressions
     * @return list of conditional expressions
     */
    public List<ConditionalExpression> getQuery() {
        return expressions;
    }
}
