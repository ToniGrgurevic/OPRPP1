package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollectionTest {

    @Test
    void test() {
        Collection collectionArray = new ArrayIndexedCollection();
        Collection collectionList = new LinkedListIndexedCollection();
        Collection[] collections = {collectionArray, collectionList};
        try {
            for (int i = 0; i < 2; i++) {
                testClear(collections[i]);
                collections[i].clear();
                System.out.println("clear() good");
                testAdd(collections[i]);
                collections[i].clear();
                System.out.println("add() good");
               /*
                testContains(collections[i]);
                System.out.println("contains() good");
                collections[i].clear();
                testIsEmpty(collections[i]);
                System.out.println("isEmpty() good");
                collections[i].clear();
                testSize(collections[i]);
                System.out.println("size() good");
                collections[i].clear();
                testToArray(collections[i]);
                System.out.println("toArray() good");

                */

            }
        } catch (Exception e) {
            fail("Expected no exception");
        }
    }


    void testAdd(Collection collection) {

        collection.add("1");
        assertEquals("1", collection.toArray()[0]);
        collection.add("2");
        assertEquals("2", collection.toArray()[1]);

        //can I add duplicates?
        collection.add("1");
        assertEquals("1", collection.toArray()[2]);

        try {
            collection.add(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException ignored) {
        } catch (Exception e) {
            fail("Expected NullPointerException");
        }

    }


    void testClear(Collection collection) {
        collection.add("1");
        collection.add("2");
        collection.clear();

        assertEquals(0, collection.size());
        try {
            var array = collection.toArray();
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException ignored) {
        } catch (Exception e) {
            fail("Expected UnsupportedOperationException");
        }
    }





    void testIsEmpty(Collection collection) {
        assertTrue(collection.isEmpty());
        collection.add(1);
        assertFalse(collection.isEmpty());
    }


    void testSize(Collection collection) {
        assertEquals(0, collection.size());
        collection.add(1);
        assertEquals(1, collection.size());
    }



    void testContains(Collection collection) {
        collection.add(1);
        assertTrue(collection.contains(1));
        assertFalse(collection.contains(2));
    }


    void testToArray(Collection collection) {
        collection.add(1);
        collection.add(2);
        collection.add(3);
        Object[] array = collection.toArray();
        assertEquals(1, array[0]);
        assertEquals(2, array[1]);
        assertEquals(3, array[2]);
    }




    void testAddAll(Collection collection) {
        collection.add(1);
        collection.add(2);
        collection.add(3);
        ArrayIndexedCollection collection2 = new ArrayIndexedCollection();
        collection2.add(4);
    }

}