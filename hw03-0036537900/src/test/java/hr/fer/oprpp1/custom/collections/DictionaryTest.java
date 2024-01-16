package hr.fer.oprpp1.custom.collections;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class DictionaryTest {

    @Test
    void testIsEmptyAndSize() {
        Dictionary<Integer,String> dict = new Dictionary<Integer,String>();
        dict.put(3,"Iva");
        dict.put(4,"Ivan");
        assertFalse(dict.isEmpty());
        assertEquals(2, dict.size());

        dict.put(3,"Frane");
        assertEquals(2, dict.size());

        dict.clear();
        assertTrue(dict.isEmpty());
        assertEquals(0,dict.size());



    }


    @Test
    void testPut() {
        Dictionary<Integer,String> dict = new Dictionary<Integer,String>();
        dict.put(3,"Iva");
        dict.put(4,"Ivan");
        assertEquals(2 , dict.size());
        assertEquals("Ivan", dict.put(4,"Stipe"));

        assertThrows( IllegalArgumentException.class, () ->dict.put(null,"ivo"));
        dict.put(6,null);
        assertEquals(3 , dict.size());
        assertNull( dict.put(6,"Josip"));
        assertEquals("Josip",dict.get(6));

    }

    @Test
    void testGet() {

        Dictionary<Integer,String> dict = new Dictionary<Integer,String>();
        dict.put(3,"Iva");
        dict.put(4,null);

         assertNull(dict.get("nije integer"));

        dict.put(6,"Josip");
        assertEquals("Josip",dict.get(6));
        assertNull(dict.get(4));
        assertNull(dict.get(9));

        assertThrows( IllegalArgumentException.class, () ->dict.get(null));
    }

    @Test
    void testRemove() {

        Dictionary<Integer,String> dict = new Dictionary<Integer,String>();
        dict.put(3,"Iva");
        dict.put(4,"Ivan");
        dict.put(6,null);

        assertThrows( IllegalArgumentException.class, () ->dict.remove(null));


        assertNull( dict.remove(9));
        assertEquals("Iva" , dict.remove(3));
        assertEquals(dict.size() , 2);
        assertNull(dict.get(3));
        assertNull(dict.remove(6));

    }
}