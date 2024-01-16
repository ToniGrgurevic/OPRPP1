package hr.fer.oprpp1.hw04.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class StudentDatabaseTest {

    @Test
    public void testDB() throws IOException {
        var list = Files.readAllLines(Paths.get("src/main/resources/database.txt"));

        StudentDatabase db = new StudentDatabase(list.toArray(new String[0]));

        assertEquals(63, db.filter(new TrueFilter()).size());
        assertEquals(0, db.filter(new FalseFilter()).size());

        var student = db.forJMBAG("0000000029");
        if (!Objects.equals(student.getLastName(), "Kos-Grabar") || !Objects.equals(student.getFirstName(), "Ivo"))
            fail("Wrong student");

        assertNull(db.forJMBAG("0009000000"));

        var list2 = Files.readAllLines(Paths.get("src/main/resources/sameJmbag.txt"));
        assertThrows( IllegalArgumentException.class, () -> new StudentDatabase(list2.toArray(new String[0])));

     var list3 = Files.readAllLines(Paths.get("src/main/resources/invalidGrade.txt"));
      assertThrows( IllegalArgumentException.class,()->new StudentDatabase(list3 .toArray(new String[0])));
    }


    @Test
    public void testComparisonOperators() {

        assertTrue(ComparisonOperators.LESS.satisfied("Ana", "Jasna"));
        assertFalse(ComparisonOperators.LESS.satisfied("Jasna", "Ana"));

        assertFalse(ComparisonOperators.GREATER.satisfied("Ana", "Jasna"));
        assertTrue(ComparisonOperators.GREATER.satisfied("Jasna", "Ana"));

        assertTrue(ComparisonOperators.EQUALS.satisfied("Ana", "Ana"));
        assertFalse(ComparisonOperators.EQUALS.satisfied("Ana", "Jasna"));

        assertFalse(ComparisonOperators.NOT_EQUALS.satisfied("Ana", "Ana"));
        assertTrue(ComparisonOperators.NOT_EQUALS.satisfied("Ana", "Jasna"));

        assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied("Ana", "Jasna"));
        assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied("Ana", "Ana"));
        assertFalse(ComparisonOperators.LESS_OR_EQUALS.satisfied("Jasna", "Ana"));

        assertFalse(ComparisonOperators.GREATER_OR_EQUALS.satisfied("Ana", "Jasna"));
        assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied("Ana", "Ana"));
        assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied("Jasna", "Ana"));

        assertTrue(ComparisonOperators.LIKE.satisfied("Zagreb", "Zagreb"));
        assertFalse(ComparisonOperators.LIKE.satisfied("AAA", "AA*AA"));
        assertTrue(ComparisonOperators.LIKE.satisfied("AAhjghgchjgchfchAA", "AA*AA"));
        assertFalse(ComparisonOperators.LIKE.satisfied("AAAB", "AA*AA"));
        assertTrue(ComparisonOperators.LIKE.satisfied("AAAB", "AAA*"));
        assertTrue(ComparisonOperators.LIKE.satisfied("8AAA", "*AAA"));
        assertFalse(ComparisonOperators.LIKE.satisfied("AAA8", "AA*A"));

    }


    @Test
    public void testFieldValueGetters() {

        StudentRecord record = new StudentRecord("0000000001", "Ivan", "Hobit", 2);

        assertEquals("0000000001", FieldValueGetters.JMBAG.get(record));
        assertEquals("Hobit", FieldValueGetters.LAST_NAME.get(record));
        assertEquals("Ivan", FieldValueGetters.FIRST_NAME.get(record));

    }

    @Test
    public void testConditionalExpresion() {

        StudentRecord record = new StudentRecord("0000000001", "Ivan", "Hobit", 2);

        ConditionalExpression expr = new ConditionalExpression(FieldValueGetters.JMBAG, "0000000001", ComparisonOperators.EQUALS);

        assertTrue(expr.getComparisonOperator().satisfied(expr.getFieldGetter().get(record), expr.getStringLiteral()));
    }


    @Test
    public void testQueryParser(){
        QueryParser qp1 = new QueryParser(" jmbag =\"0123456789\" ");
        assertTrue( qp1.isDirectQuery() );
       assertEquals("0123456789",qp1.getQueriedJMBAG() ); // 0123456789
        assertEquals(1,qp1.getQuery().size());

        QueryParser qp2 = new QueryParser("jmbag=\"0123456789\" and lastName>\"J\"");
        assertFalse(qp2.isDirectQuery()); // false
// System.out.println(qp2.getQueriedJMBAG()); // would throw!
        assertEquals( 2,qp2.getQuery().size());
        assertThrows(IllegalStateException.class, qp2::getQueriedJMBAG);

    }

    @Test
    public void testQueryFilter() throws IOException {
        var list = Files.readAllLines(Paths.get("src/main/resources/database.txt"));

        StudentDatabase db = new StudentDatabase(list.toArray(new String[0]));

        QueryParser qp1 = new QueryParser(" jmbag =\"0000000029\" ");
        QueryParser qp2 = new QueryParser("firstName LIKE \"T*\" and lastName>\"J\"");

        IFilter filter1 = new QueryFilter(qp1.getQuery());
        IFilter filter2 = new QueryFilter(qp2.getQuery());

        assertEquals(1, db.filter(filter1).size());

        int size = 0;
        for(var student : db.filter(new TrueFilter())){
            if(student.getFirstName().startsWith("T") && student.getLastName().compareTo("J") > 0) size++;
        }

        for(var student : db.filter(filter2) ){
            assertTrue(student.getFirstName().startsWith("T") && student.getLastName().compareTo("J") > 0);
        }
        assertEquals(size , db.filter(filter2).size());


    }








    public static class TrueFilter implements IFilter{

        @Override
        public boolean accepts(StudentRecord record) {
             return true;
        }
    }

    public static class FalseFilter implements IFilter{

        @Override
        public boolean accepts(StudentRecord record) {
            return false;
        }
    }



}