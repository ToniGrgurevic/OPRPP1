package hr.fer.oprpp1.hw02;
import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.parser.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class SmartScriptTester {

    public static void main(String[] args) throws IOException {


        if(args.length != 1) {
            System.out.println("Invalid number of arguments!");
            System.exit(-1);
        }
        //String path = "src/main/resources/examples/ doc1.txt";
        String path = args[0];
        String docBody = new String(
                Files.readAllBytes(Paths.get(path)),
                StandardCharsets.UTF_8
        );

        SmartScriptParser parser = null;
        try {
            parser = new SmartScriptParser(docBody);
        } catch(SmartScriptParserException e) {
            System.out.println("Unable to parse document!");
            System.exit(-1);
        } catch(Exception e) {
            System.out.println("If this line ever executes, you have failed this class!");
            System.exit(-1);
        }
        DocumentNode document = parser.getDocumentNode();
        String originalDocumentBody = document.toString();
        SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
        DocumentNode document2 = parser2.getDocumentNode();
        System.out.println(docBody);
        System.out.println(document2.toString());
// now document and document2 should be structurally identical trees
        boolean same = document.equals(document2); // ==> "same" must be true
        System.out.println(same);

    }
}
