package hr.fer.oprpp1.hw02.prob1;

/**
 * Lexer class represents a lexical analyzer.
 */
public class Lexer {
    private char[] data; // ulazni tekst
    private Token token; // trenutni token
    private int currentIndex; // indeks prvog neobrađenog znaka
    // konstruktor prima ulazni tekst koji se tokenizira

    private LexerState state;

    /**
     * Creates a new Lexer with the given text that converts to char array.
     * @param text that lexer analyzes
     * @throws NullPointerException if text is null
     */
    public Lexer(String text) {
        if(text == null) throw new NullPointerException("Text can't be null.");
        data = text.toCharArray();
        currentIndex = 0;
        state = LexerState.BASIC;


    }

    /**
     * method to check if the given char is a some type of space
     * @param znak to be checked
     * @return true if znak is a space, false otherwise
     */
    private boolean isSpace(char znak){
        if(znak == ' ' || znak == '\t' || znak== '\n' || znak== '\r')
            return true;
        return false;
    }

    /**
     * Sets the state of the lexer.
     * @param state to be set
     * @throws NullPointerException if state is null
     */
    public void setState(LexerState state){
        if(state == null) throw new NullPointerException("State can't be null.");
        this.state = state;
    }

    /**
     * Generates and returns the next token.
     * @return next token
     * @throws LexerException if an error occurs
     */
    public Token nextToken() {
        if(currentIndex == data.length+1) throw new LexerException("There are no more tokens to generate.");

        if(currentIndex == data.length) {
            currentIndex++;
            return token = new Token(TokenType.EOF, null);
        }

        char znak = data[currentIndex];
        while (  isSpace(znak) ) {
            if(currentIndex+1 == data.length) {
                currentIndex++;
                break;
            }
            currentIndex++;
            znak = data[currentIndex];

        }

        if (currentIndex == data.length) {
            return token = new Token(TokenType.EOF, null);
        }


        if(this.state == LexerState.EXTENDED){

            if (znak == '#') {
                currentIndex++;
                setState(LexerState.BASIC);
                return token = new Token(TokenType.SYMBOL, znak);
            }
            StringBuilder word = new StringBuilder();
            while (!isSpace(znak) && znak != '#') {
                word.append(znak);
                if (currentIndex == data.length - 1) {//procitao sve znakove
                    break;
                }
                znak = data[++currentIndex];
            }
            return token = new Token(TokenType.WORD, word.toString());

        }else {


            if (Character.isLetter(znak) || znak == '\\') {
                StringBuilder word = new StringBuilder();
                //dok je znak char ili  "/"
                while ((Character.isLetter(znak) || znak == '\\') && currentIndex < data.length) {

                    if (znak == '\\') { //must be followed by a number or another \
                        ++currentIndex;
                        if (currentIndex < data.length &&
                                (Character.isDigit(data[currentIndex]) || data[currentIndex] == '\\'))
                            znak = data[currentIndex];
                        else throw new LexerException("Invalid sequence.");
                    }
                    word.append(znak);
                    if (currentIndex == data.length - 1) {
                        currentIndex++;
                        break;
                    }
                    znak = data[++currentIndex];

                }

                return token = new Token(TokenType.WORD, word.toString());
            } else if (Character.isDigit(znak)) {
                StringBuilder number = new StringBuilder();
                number.append(znak);
                currentIndex++;
                while (currentIndex < data.length && Character.isDigit(data[currentIndex])) {
                    number.append(data[currentIndex]);
                    currentIndex++;
                }

                try {
                    return token = new Token(TokenType.NUMBER, Long.parseLong(number.toString()));
                } catch (NumberFormatException ex) {
                    throw new LexerException("Number is too big to be stored in Long.");
                }
            } else {
                if(znak == '#') setState(LexerState.EXTENDED);
                currentIndex++;
                return token = new Token(TokenType.SYMBOL, znak);
            }
        }

    }


    /**
     * Returns the last generated token.Doesn't start generating of the next token.
     * @return last generated token
     */
    public Token getToken() {
        return token;}
}