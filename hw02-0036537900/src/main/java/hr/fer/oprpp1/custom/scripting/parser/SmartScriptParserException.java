package hr.fer.oprpp1.custom.scripting.parser;

/**
 * If any exception occurs during parsing in SmartScriptParser,
 * parser should catch it and rethrow an instance of this exception
 */
public class SmartScriptParserException extends RuntimeException{
    public SmartScriptParserException(String s) {
    }

    public SmartScriptParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public SmartScriptParserException(Throwable cause) {
        super(cause);
    }

    public SmartScriptParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SmartScriptParserException() {
    }
}
