package hr.fer.oprpp1.hw04.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentDB {


    public static void main(String[] args) throws IOException {

        var list = Files.readAllLines(Paths.get("src/main/resources/database.txt")
                                                , StandardCharsets.UTF_8);

        StudentDatabase db = new StudentDatabase(list.toArray(new String[0]));

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print(">");
            String input = sc.nextLine();
            if (input.strip().startsWith("exit"))
                break;

            if (input.strip().startsWith("query")) {
                input = input.strip().substring("query".length());


                QueryParser parser;
                try{
                    parser = new QueryParser(input);
                }catch (IllegalArgumentException e){
                    System.out.println("Invalid query,try again."+e.getMessage());
                    continue;
                }

                List<StudentRecord> filtered;
                if(parser.isDirectQuery()){
                    filtered = new ArrayList<>();
                    filtered.add(db.forJMBAG(parser.getQueriedJMBAG()));
                    if(filtered.get(0) == null)
                        filtered.remove(0);
                    else
                        System.out.println("Using index for record retrieval.");
                }else
                    filtered = filter(input, db,parser);

                List<String> formated = null;
                if(parser.getShowing() == null)
                     formated = StudentDB.formatOutput(filtered, null);
                else{
                    formated = StudentDB.formatOutput(filtered, parser.getShowing());
                }

                for (var s : formated) {
                        System.out.println(s);
                    }
                System.out.println("Records selected: " + filtered.size());
                System.out.println();

            } else {
                System.out.println("Invalid command,try again.");
            }
        }
        System.out.println("Goodbye!");
    }

    private static List<String> formatOutput(List<StudentRecord> filtered, IFieldValueGetter[] showing) {
        if(filtered.isEmpty()) return new ArrayList<>();

        if(showing == null){
            showing = new IFieldValueGetter[]{FieldValueGetters.JMBAG,FieldValueGetters.LAST_NAME,FieldValueGetters.FIRST_NAME};
        }

        List<String> output = new ArrayList<String>();
        int maxSizeFirstName = 10;
        int maxSizeLastName = 10;
        for(var student : filtered){
            if(student.getFirstName().length() > maxSizeFirstName)
                maxSizeFirstName = student.getFirstName().length();
            if(student.getLastName().length() > maxSizeLastName)
                maxSizeLastName = student.getLastName().length();
        }

        StringBuilder okvir = new StringBuilder();
        for (var element : showing){
            int size = 0;
            if(element == FieldValueGetters.JMBAG)
                size = 12;
            else if(element == FieldValueGetters.LAST_NAME)
                size = maxSizeLastName + 2;
            else if(element == FieldValueGetters.FIRST_NAME)
                size = maxSizeFirstName + 2;

            okvir.append("+")
                    .append("=".repeat(size))
                    .append("+");

        }
        output.add(okvir.toString());

        for(var student : filtered){
            StringBuilder sb = new StringBuilder();

            for(var element : showing){
                int size = 0;
                String value = "";
                if(element == FieldValueGetters.JMBAG){
                    value = student.getJmbag();
                    size = 12;
                }
                else if(element == FieldValueGetters.LAST_NAME){
                    value = student.getLastName();
                    size = maxSizeLastName + 2;
                }
                else if(element == FieldValueGetters.FIRST_NAME){
                    value = student.getFirstName();
                    size = maxSizeFirstName + 2;
                }
                sb.append(" ".repeat(Math.max(0, size - value.length()))).append(value).append(" | ");
            }
            output.add(sb.toString());
        }
        output.add(okvir.toString());
        return  output;

    }



    /**
     * Method that filters students from database with given filter
     * @param input input from user that contains conditions
     * @param db database
     * @return list of students that satisfy user conditions
     */
    private static List<StudentRecord> filter(String input, StudentDatabase db,QueryParser parser) {

        IFilter filter = new QueryFilter(parser.getQuery());

        return db.filter(filter);
    }



}