package hr.fer.oprpp1.hw04.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Class that represents a database of students.
 */
public class StudentDatabase {

    List<StudentRecord> list = new LinkedList<>();
    Map<String,StudentRecord> map = new HashMap<>();

    /**
     * Expects array of students with wich it will create a database.
     * @param records array of students(one String one student)
     */
    public StudentDatabase(String[] records) {

        for(var line : records){
            var inputs = line.split("\\t");
            if(inputs.length != 4){
                throw new IllegalArgumentException("Invalid input");
            }

            StudentRecord student = new StudentRecord(inputs[0], inputs[2], inputs[1],Integer.parseInt(inputs[3]) );

            if(map.containsKey(student.getJmbag()))
                throw new IllegalArgumentException("There can't be two students with the same jmbag");
            if(student.getGrade() > 5 || student.getGrade() < 1)
                throw new IllegalArgumentException("Grade must be between 1 and 5");

            map.put(student.getJmbag(),student);
            list.add(student);
        }


    }


    /**
     * Returns student with the given jmbag.
     * @param jmbag jmbag of the student that will be returned
     * @return student with the given jmbag
     */
    public StudentRecord forJMBAG(String jmbag){
        return map.get(jmbag);
    }

    /**
     * Returns list of students that satisfy the filter.Method doesn't return copied list,
     * it returns the same list that is in the database only with filtered students.
     * @param filter filter that will be used to filter students
     * @return list of students that satisfy the filter
     */
    public List<StudentRecord> filter(IFilter filter){
        List<StudentRecord> acceptableRecords = new LinkedList<>();
        for(var student : list){
            if(filter.accepts(student)){
                acceptableRecords.add(student);
            }
        }
        return acceptableRecords;

    }





}
