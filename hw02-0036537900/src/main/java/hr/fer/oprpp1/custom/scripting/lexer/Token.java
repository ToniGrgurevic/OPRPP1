package hr.fer.oprpp1.custom.scripting.lexer;

import java.util.Objects;

/**
 * Token represents a token.
 */
public class Token {

    private TokenType type;
    private Object value;

    public Token(TokenType type, Object value) {
        this.type = type;
        this.value = value;

    }
    public Object getValue() {return value;}
    public TokenType getType() {return  type;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        Token token = (Token) o;
        return type == token.type && Objects.equals(value, token.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }
}
