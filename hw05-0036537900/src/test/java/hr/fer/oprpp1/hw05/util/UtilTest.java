package hr.fer.oprpp1.hw05.util;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

    @Test
    public void testHexToBythe(){
        //System.out.println(Arrays.toString(Util.hextobyte("01aE22")));
        assertArrayEquals(new byte[]{1, -82, 34}, Util.hextobyte("01aE22"));

        assertArrayEquals(new byte[0] , Util.hextobyte(""));
        assertThrows(IllegalArgumentException.class , () ->Util.hextobyte("0154l7"));
        assertThrows(IllegalArgumentException.class , ()-> Util.hextobyte("0154A74"));
    }

    @Test
    public void testBytetohex(){
        assertEquals("01ae22" , Util.bytetohex(new byte[]{1, -82, 34}));
        assertEquals("" , Util.bytetohex(new byte[0]));
    }

}