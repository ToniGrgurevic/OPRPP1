package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.nodes.EchoNode;
import hr.fer.oprpp1.custom.scripting.nodes.TextNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class SmartScriptParserTest {

    @Test
    public void testParser1() throws IOException {

        for(int i = 1;i<=3;i++) {
            String path = "src/test/resources/primjer"+ i+".txt";
            String docBody = Files.readString(Paths.get(path));

            SmartScriptParser parser = null;
            try {
                parser = new SmartScriptParser(docBody);
            } catch (SmartScriptParserException e) {
                fail("Exception thrown");
            } catch (Exception e) {
                fail("Thrown illegal exception");
            }
            DocumentNode document = parser.getDocumentNode();
            assertEquals(1, document.numberOfChildren());
            if (!(document.getChild(0) instanceof TextNode))
                fail("wrong node type");

            String originalDocumentBody = document.toString();
            SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
            DocumentNode document2 = parser2.getDocumentNode();
            System.out.println(document2.toString());
            boolean same = document.equals(document2); // ==> "same" must be true
            assertTrue(same);
        }

    }

    @Test
    public void testSlashN_inText() throws IOException {

        for (int i = 4; i <= 5; i++) {
            String path = "src/test/resources/primjer" + i+".txt";
            String docBody = Files.readString(Paths.get(path));

            SmartScriptParser parser = null;
            try {
                parser = new SmartScriptParser(docBody);
                fail();
            } catch (SmartScriptParserException ignored) {

            } catch (Exception e) {
                fail("Thrown illegal exception");
            }



        }
    }

    @Test
    public void testStringInTag() throws IOException {

        for(int i = 6;i<=7;i++) {
            String path = "src/test/resources/primjer"+ i+".txt";
            String docBody = Files.readString(Paths.get(path));

            SmartScriptParser parser = null;
            try {
                parser = new SmartScriptParser(docBody);
            } catch (SmartScriptParserException e) {
                fail("Exception thrown");
            } catch (Exception e) {
                fail("Thrown illegal exception");
            }
            DocumentNode document = parser.getDocumentNode();
            assertEquals(2, document.numberOfChildren());
            if (!(document.getChild(0) instanceof TextNode))
                fail("wrong node type");
            if (!(document.getChild(1) instanceof EchoNode))
                fail("wrong node type");

            String originalDocumentBody = document.toString();
            SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
            DocumentNode document2 = parser2.getDocumentNode();
            System.out.println(document2.toString());
            boolean same = document.equals(document2); // ==> "same" must be true
            assertTrue(same);
            System.out.println("");
            System.out.println("##################################");

        }

    }
    @Test
    void testShoudFailParsing() throws IOException {

        for (int i = 8; i <= 9; i++) {
            String path = "src/test/resources/primjer" + i+".txt";
            String docBody = Files.readString(Paths.get(path));

            SmartScriptParser parser = null;
            try {
                parser = new SmartScriptParser(docBody);
                fail();
            } catch (SmartScriptParserException ignored) {

            } catch (Exception e) {
                fail("Thrown illegal exception");
            }



        }
    }

}