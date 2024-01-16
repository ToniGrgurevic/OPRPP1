package hr.fer.oprpp1.custom.scripting.lexer;


import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParserException;

public class SmartScriptLexer {
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
    public SmartScriptLexer(String text) {
        if(text == null) throw new NullPointerException("Text can't be null.");
        text  = text.strip();
        data = text.toCharArray();
        currentIndex = 0;
        state = LexerState.BASIC;
    }

    /**
     * Generates and returns next token.
     * @return next token
     * @SmartScriptParserException if there is no more tokens
     */
    public Token nextToken(){
        if(token != null && token.getType() == TokenType.EOF) throw new SmartScriptParserException("There is no more tokens.");
        if(currentIndex >= data.length) return token = new Token(TokenType.EOF, null);

        if(state == LexerState.BASIC) return token = nextTokenBasic();
        else return token = nextTokenTag();
    }

    /**
     * Returns  generated token.Method is called only if lexer is in TAG.
     * @return next Token
     * @SmartScriptParserException if there is an error while generating token
     */
    private Token nextTokenTag() {
        
        char znak = data[currentIndex];


        //remove all spaces
        while( Character.isWhitespace(znak)) znak = data[++currentIndex];

        if(znak == 'f' || znak=='F'
        && (currentIndex< data.length-2)  &&
                (data[currentIndex+1] == 'o' || data[currentIndex+1] == 'O')
                && (data[currentIndex+2] == 'r' || data[currentIndex+2] == 'R')){
            currentIndex+=3;
            return new Token(TokenType.TAG_NAME, "FOR");

        } else if (znak == 'e' || znak=='E'
                && (currentIndex< data.length-2)  &&
                (data[currentIndex+1] == 'n' || data[currentIndex+1] == 'N')
                && (data[currentIndex+2] == 'd' || data[currentIndex+2] == 'D')) {
            currentIndex+=3;
            return new Token(TokenType.TAG_NAME, "END");
        } else if(znak == '='){
            currentIndex++;
            return new Token(TokenType.TAG_NAME, "=");
        }
        else if(znak == '$' && data[currentIndex+1] == '}') {
            currentIndex += 2;
            this.setState(LexerState.BASIC);
            return token = new Token(TokenType.TAG_CLOSE, "$}");
        } else if (Character.isLetter(znak)) {  //variable
            StringBuilder variable = new StringBuilder();
            variable.append(znak);
            currentIndex++;
            while( currentIndex< data.length
                    &&( Character.isDigit(data[currentIndex]) || Character.isLetter(data[currentIndex]) || data[currentIndex] == '_') ) {
                variable.append(data[currentIndex++]);

            }

            return new Token(TokenType.TAG_VARIABLE , variable.toString());

        }else if( (Character.isDigit(znak) )
                || (znak == '-' && currentIndex< data.length-1 && Character.isDigit(data[currentIndex+1])) ){
            StringBuilder number = new StringBuilder();
            number.append(znak);
            currentIndex++;
            boolean dot =false; //can be only one
            while(currentIndex< data.length
                    && (Character.isDigit(data[currentIndex]) || data[currentIndex++] == '.')) {
                number.append(data[currentIndex++]);
                if(znak == '.' ) {
                    if(dot) throw new SmartScriptParserException("Number can't have more than one dot.");
                    else if ( currentIndex< data.length-1 && !Character.isDigit(data[currentIndex+1])) {
                        throw new SmartScriptParserException("Number must have digits after dot.");
                    }
                    dot = true;
                }
            }
            if(dot) return new Token(TokenType.TAG_DOUBLE, number.toString());
            else
              return new Token(TokenType.TAG_INTEGER, number.toString());
        } else if ( znak =='+' || znak == '-' ||znak=='/' ||znak=='^' ||znak=='*') {
            currentIndex++;
            return new Token(TokenType.TAG_OPERATOR, String.valueOf(znak));
        } else if (znak == '@') {
            StringBuilder function = new StringBuilder();
            function.append(znak);
            currentIndex++;
            while(currentIndex< data.length
                    &&( Character.isDigit(data[currentIndex]) || Character.isLetter(data[currentIndex]) || data[currentIndex] == '_') ) {
                function.append(data[currentIndex++]);
            }
            return new Token(TokenType.TAG_FUNCTION, function.toString());
        }else if(znak == '"') {
            StringBuilder string = new StringBuilder();
            currentIndex++;
            while(currentIndex< data.length && data[currentIndex] != '"') {
                string.append(data[currentIndex++]);

                if(data[currentIndex] == '\\') {
                    currentIndex++;

                      if(data[currentIndex] != '\\'
                      && data[currentIndex] != '"'
                      && data[currentIndex] != 'n'
                      && data[currentIndex] != 't'
                              && data[currentIndex] != 't') {
                          throw new SmartScriptParserException("Escape character can't be followed by anything except \\ or \".");
                      }else {
                          switch (data[currentIndex]) {
                              case '\\' -> string.append('\\');
                              case '"' -> string.append('"');
                              case 'n' -> string.append('\n');
                              case 'r' -> string.append('\r');
                              case 't' -> string.append('\t');
                          }
                          currentIndex++;
                      }
                }
            }
            currentIndex++; //skip "
            return new Token(TokenType.TAG_STRING,  string.toString());
        }

            throw new SmartScriptParserException("Invalid character->" + znak);


    }


    /**
     * Sets the state of lexer.
     * @param state to which lexer is set
     */
    private void setState(LexerState state) {
        this.state = state;
    }



    /**
     * Returns  generated token.Method is called only if lexer is in TAG.
     * @return next Token
     * @SmartScriptParserException if there is an error while generating token
     */
    private Token nextTokenBasic() {
        char znak = data[currentIndex];

        //remove all spaces
        while( Character.isWhitespace(znak)) znak = data[++currentIndex];

        if(znak == '{'
        && (currentIndex<data.length-1 && data[currentIndex+1] == '$' )
        && ( currentIndex == 0 ||  data[currentIndex-1] !='\\' )) {
            currentIndex += 2;
            this.setState(LexerState.TAG);
            return token = new Token(TokenType.TAG_OPEN, "{$");
        } else  {
            if (  znak == '\\'
            && (currentIndex<data.length-1 && (data[currentIndex+1] != '\\' && data[currentIndex+1] != '{'))) {
               throw new SmartScriptParserException("Escape character can't be followed by anything except \\ or {.");
            }
            //boolean escape = false;

            if(znak == '\\') currentIndex++;

            StringBuilder word = new StringBuilder();
            word.append(data[currentIndex]);
            ++currentIndex;
            while(   ( !(data[currentIndex] == '{' && (currentIndex<data.length-1 && data[currentIndex+1] == '$' ) ))
                    && !Character.isWhitespace(data[currentIndex])) {


                if (  data[currentIndex] == '\\'
                        && (currentIndex==data.length-1 || (data[currentIndex+1] != '\\' && data[currentIndex+1] != '{'))) {
                    throw new SmartScriptParserException("Escape character can't be followed by anything except \\ or {.");
                }
                else if(data[currentIndex] == '\\') {
                    currentIndex++;
                }

                word.append(data[currentIndex++]);
                if(currentIndex == data.length) break;
            }

            return new Token(TokenType.TEXT , word.toString());
        }
    }
}
