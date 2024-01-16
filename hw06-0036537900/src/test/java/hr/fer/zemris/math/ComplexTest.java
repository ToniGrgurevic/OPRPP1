package hr.fer.zemris.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComplexTest {


    @Test
    public void testParse(){

        Complex c = Complex.parse("i");
        assertEquals(0,c.getRe());
        assertEquals(1,c.getIm());

        c = Complex.parse("-i");
        assertEquals(0,c.getRe());
        assertEquals(-1,c.getIm());

        c = Complex.parse("i5");
        assertEquals(0,c.getRe());
        assertEquals(5,c.getIm());

        c = Complex.parse("-i5");
        assertEquals(0,c.getRe());
        assertEquals(-5,c.getIm());

        c = Complex.parse("1");
        assertEquals(1,c.getRe());
        assertEquals(0,c.getIm());



        c = Complex.parse("-2.71-i3.15");
        assertEquals(-2.71,c.getRe());
        assertEquals(-3.15,c.getIm());

        c = Complex.parse("-2.71-i3.15");
        assertEquals(-2.71,c.getRe());
        assertEquals(-3.15,c.getIm());

        c = Complex.parse("-2.71-i3.15");
        assertEquals(-2.71,c.getRe());
        assertEquals(-3.15,c.getIm());

        c = Complex.parse("-2.71-i3.15");
        assertEquals(-2.71,c.getRe());
        assertEquals(-3.15,c.getIm());

        c = Complex.parse("-1-i0");
        assertEquals(-1,c.getRe());
        assertEquals(0,c.getIm());

        c = Complex.parse("0-i1");
        assertEquals(0,c.getRe());
        assertEquals(-1,c.getIm());



    }

}