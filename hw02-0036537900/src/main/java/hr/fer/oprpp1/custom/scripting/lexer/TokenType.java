package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * TokenType represents a posible types of a token.
 */
public enum TokenType {
    EOF, // end of file
    TEXT, // one or more letter
    TAG_INTEGER,
    TAG_DOUBLE,
    TAG_VARIABLE,
    TAG_OPEN,
    TAG_CLOSE,
    TAG_NAME,  // =,for, end
    TAG_STRING,
    TAG_FUNCTION,
    TAG_OPERATOR,

}
