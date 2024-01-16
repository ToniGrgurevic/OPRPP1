package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class LinkedListIndexedCollectionTest {

    @Test
    void testConstructor(){
        LinkedListIndexedCollection col = new LinkedListIndexedCollection();
        assertEquals(0 , col.size());
        assertTrue(col.isEmpty());
        col.add("1");
        col.add("2");
        col.add("3");
        LinkedListIndexedCollection col2 = new LinkedListIndexedCollection(col);
        assertEquals(3, col2.size());
        assertEquals(Arrays.toString(col.toArray()), Arrays.toString(col2.toArray()));

    }

    @Test
    void testRemove() {

        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.add("1");
        collection.add("2");
        collection.add("3");
        //removing middle element
        collection.remove(1);
        assertEquals(2, collection.size());
        assertEquals("1", collection.get(0));
        assertEquals("3", collection.get(1));
        //removing ifirst element
        collection.remove(0);
        assertEquals("3", collection.get(0));
        //removing last element
        collection.add("1");
        collection.add("2");
        collection.remove(collection.size() - 1);
        assertEquals("1", collection.get(collection.size() - 1));

        try {
            collection.remove(-1);
            fail("Expected IndexOutOfBoundsException");
        }catch (IndexOutOfBoundsException ignored){}
        catch (Exception e){
            fail("Expected IndexOutOfBoundsException");
        }

        try {
            collection.remove(2);
            fail("Expected IndexOutOfBoundsException");
        }catch (IndexOutOfBoundsException ignored){}
        catch (Exception e){
            fail("Expected IndexOutOfBoundsException");
        }
    }

    @Test
    void testIndexOf() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.add("1");
        collection.add("2");
        collection.add("3");
        assertEquals(0, collection.indexOf("1"));
        assertEquals(1, collection.indexOf("2"));
        assertEquals(2, collection.indexOf("3"));
        assertEquals(-1, collection.indexOf("4"));
        assertEquals(-1, collection.indexOf(null));
    }

    @Test
    void testInsert() {
        String one = "1";
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.add(one);
        collection.add("2");
        collection.insert("3", 1);
        collection.insert("4", collection.size());
        assertEquals("1", collection.get(0));
        assertEquals("3", collection.get(1));
        assertEquals("2", collection.get(2));
        assertEquals("4", collection.get(3));

        collection.insert("0",0);
        assertEquals("0",collection.get(0));


        collection.insert("5",1);
        collection.insert("6",5);
        assertEquals("[0, 5, 1, 3, 2, 6, 4]",Arrays.toString(collection.toArray()));



        try {
            collection.insert(null, 1);
            fail("Expected NullPointerException");
        }catch(NullPointerException ignored){}
        catch (Exception e){
            fail("Expected NullPointerException");
        }

        try {
            collection.insert("4", -1);
            fail("Expected IndexOutOfBoundsException");
        }catch(IndexOutOfBoundsException ignored){}
        catch (Exception e){
            fail("Expected IndexOutOfBoundsException");
        }

        try {
            collection.insert("4", 8);
            fail("Expected IndexOutOfBoundsException");
        }catch(IndexOutOfBoundsException ignored){}
        catch (Exception e){
            fail("Expected IndexOutOfBoundsException");
        }

        //can I add duplicates?
        collection.insert(one,0);
        assertEquals("1", collection.get(0));
    }

    @Test
    void testGet() {
        LinkedListIndexedCollection collection = new LinkedListIndexedCollection();
        collection.add("1");
        collection.add("2");
        collection.add("3");
        collection.add("4");
        collection.add("5");

        assertEquals("1", collection.get(0));
        assertEquals("2", collection.get(1));
        assertEquals("3", collection.get(2));
        assertEquals("4", collection.get(3));
        assertEquals("5", collection.get(4));
        try {
            collection.get(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ignored) {
        } catch (Exception e) {
            fail("Expected IndexOutOfBoundsException");
        }

        try {
            collection.get(5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ignored) {
        } catch (Exception e) {
            fail("Expected IndexOutOfBoundsException");

        }
    }
}