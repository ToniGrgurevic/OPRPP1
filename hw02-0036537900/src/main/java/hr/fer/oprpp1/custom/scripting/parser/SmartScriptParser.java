package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.scripting.elems.*;
import hr.fer.oprpp1.custom.scripting.lexer.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import hr.fer.oprpp1.custom.scripting.nodes.*;
import hr.fer.oprpp1.custom.collections.ObjectStack;

public class SmartScriptParser {

    private SmartScriptLexer lexer;
    private String documentBody;

    DocumentNode documentNode;


    public SmartScriptParser(String documentBody) {

        this.documentBody = documentBody;
        this.lexer = new SmartScriptLexer(documentBody);
        this.parse();
    }






    public static void main(String[] args) {


        for (int i = 1; i < 2; i++) {
            String docBody = "";
            try {
                docBody = Files.readString(Paths.get("src/test/resources/primjer" + 4 + ".txt"));
            } catch (IOException e) {
                System.out.println("Unable to read file.");
            }

                SmartScriptParser parser = null;

                try {
                    parser = new SmartScriptParser(docBody);
                } catch (SmartScriptParserException ex) {
                    System.out.println("Unable to parse document!");
                    System.exit(-1);
                } catch (Exception ex) {
                    System.out.println("If this line ever executes, you have failed this class!");
                    System.exit(-1);
                }
                DocumentNode document = parser.getDocumentNode();
                String originalDocumentBody = document.toString();
                System.out.println(originalDocumentBody);
            }
        }

    /**
     * Parses the document body with the help of lexer that he calls to generate tokens.
     */
    private void parse() {
        ObjectStack stack = new ObjectStack();
         documentNode =  new DocumentNode();
        stack.push(documentNode);
        Token token = lexer.nextToken();

        while(token.getType() != TokenType.EOF){

        if(token.getType() == TokenType.TEXT){
            StringBuilder text = new StringBuilder();

            while(token.getType() == TokenType.TEXT){
                text.append(" ").append(token.getValue());
                token=lexer.nextToken();
            }
            text.append(" ");
            if(token.getType() != TokenType.TAG_OPEN
                && token.getType() != TokenType.EOF)
                throw new SmartScriptParserException("Invalid text.");
            ((Node)stack.peek()).addChildNode(new TextNode(text.toString()));

        }
        else if(token.getType() == TokenType.TAG_OPEN){

            token = lexer.nextToken();

            if(token.getType() == TokenType.TAG_NAME){

                if( token.getValue()  == "FOR" ){

                ElementVariable variable = null;
                Element[] expressions = new Element[3];
                    token= lexer.nextToken();
                    int count= 0;
                    while( token.getType() != TokenType.TAG_CLOSE && count<4){
                        if(count==0 && token.getType() == TokenType.TAG_VARIABLE){

                            variable = new ElementVariable((String) token.getValue());

                        }else if(count>0) {
                            switch (token.getType()) {
                                case TAG_INTEGER ->
                                        expressions[count - 1] = new ElementConstantInteger(Integer.parseInt((String) token.getValue()) );
                                case TAG_DOUBLE ->
                                        expressions[count - 1] = new ElementConstantDouble(Double.parseDouble((String) token.getValue()) );
                                case TAG_STRING ->
                                        expressions[count - 1] = new ElementString((String) token.getValue());
                                case TAG_VARIABLE ->
                                        expressions[count - 1] = new ElementVariable((String) token.getValue());
                                case TAG_OPERATOR ->
                                        expressions[count - 1] = new ElementOperator((String) token.getValue());
                                case TAG_FUNCTION ->
                                        expressions[count - 1] = new ElementFunction((String) token.getValue());
                                default -> throw new SmartScriptParserException("Invalid expression in for loop.");
                            }
                        }else  throw new SmartScriptParserException("Invalid expression in for loop.");

                        token = lexer.nextToken();
                        count++;
                    }

                    ForLoopNode forLoopNode = new ForLoopNode(variable, expressions[0], expressions[1], expressions[2]);
                    ((Node)stack.peek()).addChildNode(forLoopNode);
                    stack.push(forLoopNode);
                    if(token.getType() != TokenType.TAG_CLOSE){
                        throw new SmartScriptParserException("Invalid number of arguments in echo tag.");
                    }
                    token = lexer.nextToken();
                }

                else if(token.getValue()  == "="){
                    token = lexer.nextToken();
                    Element[] elements = new Element[1];
                    int count = 0;
                    while(token.getType() != TokenType.TAG_CLOSE ){
                        //resaize array elements
                        if(count == elements.length){
                            elements= java.util.Arrays.copyOf(elements, elements.length+1);
                        }

                        switch (token.getType()) {
                            case TAG_INTEGER ->
                                    elements[count] = new ElementConstantInteger((int) token.getValue());
                            case TAG_DOUBLE ->
                                    elements[count] = new ElementConstantDouble((double) token.getValue());
                            case TAG_STRING ->
                                    elements[count] = new ElementString((String) token.getValue());
                            case TAG_VARIABLE ->
                                    elements[count] = new ElementVariable((String) token.getValue());
                            case TAG_OPERATOR ->
                                    elements[count] = new ElementOperator((String) token.getValue());
                            case TAG_FUNCTION ->
                                    elements[count] = new ElementFunction((String) token.getValue());

                            default -> throw new SmartScriptParserException("Invalid expression in for loop.");
                        }

                        token = lexer.nextToken();
                        count++;


                    }
                    if(count==0){
                            throw new SmartScriptParserException("Invalid number of arguments in echo tag.");
                    }
                    token=lexer.nextToken();
                    EchoNode echoNode = new EchoNode(elements);
                    ((Node)stack.peek()).addChildNode(echoNode);
                }
                else if(token.getValue()  == "END") {
                    try{
                        stack.pop();
                    }catch (Exception e){
                        throw new SmartScriptParserException("Invalid number of END tags.");
                    }
                    token = lexer.nextToken(); //skip }
                    if(token.getType()!=TokenType.TAG_CLOSE)
                        throw new SmartScriptParserException("tag END is not properly closed.");
                    token=lexer.nextToken();
                }
                else throw new SmartScriptParserException("Tag name is not valid.");


            }else throw new SmartScriptParserException("Tag name is not valid.");


        }
        else if (token.getType() == TokenType.TAG_CLOSE) {
            throw new SmartScriptParserException("Tag is not closed.");
        }

        }

    }




    public DocumentNode getDocumentNode() {
        return documentNode;
    }


}
