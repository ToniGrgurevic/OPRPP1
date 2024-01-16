package hr.fer.oprpp1.hw02.prob1;

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
