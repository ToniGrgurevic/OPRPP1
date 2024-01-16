package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class SimpleHashtableTest {


    @Test
    void testConstructor() {
        assertThrows(IllegalArgumentException.class , () -> new SimpleHashtable<>(-1));
        SimpleHashtable<Integer,String> table =new SimpleHashtable<>(16);
        assertEquals(0,table.size());
    }

    /** Test for put() get() size() containsKey() containsValue() methods  of class simpleHashtable */
    @Test
    void testMethods1() {
        SimpleHashtable<Integer,String>  table = new SimpleHashtable<>();
        assertEquals(table.size() , 0);
        table.put(1,"Ivan");
        assertNull( table.put(2,"Stipe"));
        assertEquals( table.put(1,"Franko"), "Ivan");
        assertEquals(2 , table.size());

        assertThrows(NullPointerException.class, () -> table.put(null,"null"));
        table.put(1,null);
        assertNull(table.get(1));
        assertNull(table.get(null));
        assertEquals("Stipe", table.get(2));

        table.put(65,"Biuk");
        assertEquals("Biuk", table.get(65));

        assertTrue(table.containsKey(65));
        assertTrue(table.containsKey(1));
        assertFalse(table.containsKey(52));
        assertFalse(table.containsKey(null));
        assertFalse(table.containsKey("null"));

        assertTrue(table.containsValue(null));
        assertTrue(table.containsValue("Biuk"));
        assertFalse(table.containsValue("Francesco"));
        assertFalse(table.containsValue("Ivan"));
        assertFalse(table.containsValue(1));

        assertNull(table.get("112"));
        assertNull(table.get("Nije Integer"));
    }


    /** Test methods remove() isEmpty() clear() of class simpleHashtable */
    @Test
    void testMethods2() {
        SimpleHashtable<Integer,String>  table = new SimpleHashtable<>();
        assertTrue( table.isEmpty());
        table.put(1,"Ivan");
         table.put(2,"Stipe");
        table.put(65,"Biuk");
        assertFalse(table.isEmpty());
        table.clear();
        assertTrue( table.isEmpty());

        table.put(1,"Ivan");
        table.put(2,"Stipe");
        table.put(65,"Biuk");
        table.put(81,"Horvat");
        assertNull(table.remove(null));
        assertNull(table.remove(49));
        assertNull(table.remove("nije Integer"));
        assertEquals("Biuk",table.remove(65));
        assertNull(table.get(65));
        assertEquals("Ivan",table.remove(1));
        assertNull(table.get(1));
        assertEquals("Horvat",table.remove(81));
        assertNull(table.get(81));
        assertEquals("Stipe",table.remove(2));
        assertTrue( table.isEmpty());

    }

    @Test
    void testToString() {
        SimpleHashtable<Integer,String>  table = new SimpleHashtable<>();
        table.put(1,"Ivan");
        table.put(2,"Stipe");
        table.put(65,"Biuk");
        table.put(81,"Horvat");
        table.put(45,null);
        String ocekivano = "[1=Ivan, 65=Biuk, 81=Horvat, 2=Stipe, 45=null]";
        assertEquals(ocekivano, table.toString());

    }

    @Test
    void toArray() {
        SimpleHashtable<Integer,String>  table = new SimpleHashtable<>();
        table.put(1,"Ivan");
        table.put(2,"Stipe");
        table.put(65,"Biuk");
        table.put(81,"Horvat");
        SimpleHashtable.TableEntry<Integer,String>[]  expexted =(SimpleHashtable.TableEntry<Integer, String>[]) new SimpleHashtable.TableEntry[4];

        expexted[0] = new SimpleHashtable.TableEntry<>(1,"Ivan");
        expexted[1] = new SimpleHashtable.TableEntry<>(65,"Biuk");
        expexted[2] = new SimpleHashtable.TableEntry<>(81,"Horvat");
        expexted[3] = new SimpleHashtable.TableEntry<>(2,"Stipe");

        var array = table.toArray();
        for(int i=0; i<array.length; i++){
            assertEquals(array[i].getKey(), expexted[i].getKey());
            assertEquals(array[i].getValue(), expexted[i].getValue());
        }
    }


    @Test
    void testIterator() {

        SimpleHashtable<Integer,String>  table = new SimpleHashtable<>();
        table.put(1,"Ivan");
        table.put(2,"Stipe");
        table.put(65,"Biuk");
        table.put(81,"Horvat");
        Iterator<SimpleHashtable.TableEntry<Integer, String>> iterator = table.iterator();
        iterator.hasNext();
        iterator.hasNext();
        iterator.hasNext();
        assertEquals("Ivan", iterator.next().getValue());
        assertEquals("Biuk", iterator.next().getValue());
        assertTrue(iterator.hasNext());
        assertEquals("Horvat", iterator.next().getValue());
        assertEquals("Stipe", iterator.next().getValue());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);

        Iterator<SimpleHashtable.TableEntry<Integer, String>> iterator1 = table.iterator();
        Iterator<SimpleHashtable.TableEntry<Integer, String>> iterator2= table.iterator();
        assertEquals("Ivan", iterator1.next().getValue());
        assertEquals("Ivan", iterator2.next().getValue());
        assertEquals("Biuk", iterator1.next().getValue());
        iterator2.remove();
        assertThrows(ConcurrentModificationException.class , iterator1::next);
        assertThrows(ConcurrentModificationException.class , iterator1::hasNext);


        Iterator<SimpleHashtable.TableEntry<Integer, String>> iterator3= table.iterator();
        assertEquals("Biuk", iterator3.next().getValue());
        iterator3.remove();
        assertThrows(IllegalStateException.class , iterator3::remove);


    }

    @Test
    public void testReorganization(){
            SimpleHashtable<String,Integer> examMarks = new SimpleHashtable<>(2);

            examMarks.put("Ivana", 2);
            examMarks.put("Ante", 2);
            examMarks.put("Jasna", 2);
            examMarks.put("Kristina", 5);
            examMarks.put("Ivana", 5); // overwrites old grade for Ivana
            assertEquals(4,examMarks.size());

    }
}