package hr.fer.oprpp1.jmbag0036537900.collections;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;




class ArrayIndexedCollectionTest {


//make a test for constructors
    @Test
    void testConstruct(){
        try{
            Collection collection1 = new ArrayIndexedCollection();
             Collection collection2 = new ArrayIndexedCollection(10);
             collection1.add("1");
             Collection collection3 = new ArrayIndexedCollection(collection1, 10);
             Collection collection4 = new ArrayIndexedCollection(collection1);
            assertEquals("1", ((ArrayIndexedCollection) collection3).get(0));
            assertEquals("1", ((ArrayIndexedCollection) collection4).get(0));

        }catch (Exception e){
            fail("Expected no exception");
        }

        try {
            Collection collection5 = new ArrayIndexedCollection(null, 10);
            fail("Expected NullPointerException");
        }catch (NullPointerException ignored){}
        catch (Exception e){
            fail("Expected NullPointerException");
        }
        try {
            Collection collection5 = new ArrayIndexedCollection(null);
            fail("Expected NullPointerException");
        }catch (NullPointerException ignored){}
        catch (Exception e){
            fail("Expected NullPointerException");
        }

        try {
            Collection collection = new ArrayIndexedCollection( -1);
            fail("Expected IllegalArgumentException");
        }catch (IllegalArgumentException ignored){}
        catch (Exception e){
            fail("Expected IllegalArgumentException");
        }
        try {
            Collection collection = new ArrayIndexedCollection();
            Collection collection1 = new ArrayIndexedCollection(collection, 0);
            fail("Expected IllegalArgumentException");
        }catch (IllegalArgumentException ignored){}
        catch (Exception e){
            fail("Expected IllegalArgumentException");
        }

    }






    @Test
    void testGet() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
        collection.add("1");
        collection.add("2");


        assertEquals("1", collection.get(0));
        assertEquals("2", collection.get(1));

        try {
            collection.get(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ignored) {
        } catch (Exception e) {
            fail("Expected IndexOutOfBoundsException");
        }

        try {
            collection.get(2);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ignored) {
        } catch (Exception e) {
            fail("Expected IndexOutOfBoundsException");

        }
    }





    @Test
    void testInsert() {
        String one = "1";
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
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
            collection.insert("4", 6);
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
    void testIndexOf() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
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
    void testRemove() {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
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






}