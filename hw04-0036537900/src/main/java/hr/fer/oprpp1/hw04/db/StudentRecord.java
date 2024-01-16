package hr.fer.oprpp1.hw04.db;

import java.util.Objects;

/**
 * Class that represents a single student record.Student has jmbag,first name, last name and grade.
 * There can't be two students with the same jmbag.
 */

public class StudentRecord {
    private String jmbag;
    private String firstName;
    private String lastName;
    private int grade;


    public StudentRecord(String jmbag, String firstName, String lastName, int grade) {
        this.jmbag = jmbag;
        this.firstName = firstName;
        this.lastName = lastName;
        this.grade = grade;
    }

    public String getJmbag() {
        return jmbag;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getGrade() {
        return grade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentRecord)) return false;
        StudentRecord that = (StudentRecord) o;
        return Objects.equals(jmbag, that.jmbag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jmbag);
    }
}
