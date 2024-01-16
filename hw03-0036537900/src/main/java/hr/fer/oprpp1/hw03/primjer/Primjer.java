package hr.fer.oprpp1.hw03.primjer;

import hr.fer.oprpp1.custom.collections.*;

public class Primjer {
    public static void main(String[] args) {
// create collection:
        SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);
// fill data:
        examMarks.put("Ivana", 2);
        examMarks.put("Ante", 2);
        examMarks.put("Jasna", 2);
        examMarks.put("Kristina", 5);
        examMarks.put("Ivana", 5); // overwrites old grade for Ivana
        for(SimpleHashtable.TableEntry<String,Integer> pair : examMarks) {
            System.out.printf("%s => %d%n", pair.getKey(), pair.getValue());
        }
    }
}
