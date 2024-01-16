package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * exeption used when handling lexer errors
 */
public class LexerException extends RuntimeException{

        public LexerException() {
            super();
        }

        public LexerException(String message) {
            super(message);
        }
}
