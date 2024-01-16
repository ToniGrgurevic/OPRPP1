package hr.fer.zemris.java.gui.prim;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrimListModelTest {


    @Test
    public void testAdd(){
        PrimListModel model = new PrimListModel();

        assertEquals(1, model.getSize());
        assertEquals(1, model.getElementAt(0));

        model.next();
        assertEquals(2, model.getSize());
        assertEquals(1, model.getElementAt(0));
        assertEquals(2, model.getElementAt(1));

        model.next();
        assertEquals(3, model.getSize());
        assertEquals(1, model.getElementAt(0));
        assertEquals(2, model.getElementAt(1));
        assertEquals(3, model.getElementAt(2));

        model.next();
        assertEquals(4, model.getSize());
        assertEquals(1, model.getElementAt(0));
        assertEquals(2, model.getElementAt(1));
        assertEquals(3, model.getElementAt(2));
        assertEquals(5, model.getElementAt(3));

    }

}