package hr.fer.oprpp1.hw05.util;

import java.util.List;
import java.util.stream.Stream;

/**
 * This class is used for utility methods concerning the {@link hr.fer.oprpp1.hw05.crypto.Cipher} class.
 * Mainly used for converting hexadecimal strings to byte arrays and vice versa.
 */
public class Util {

    /**
     * This method converts a hexadecimal string to a byte array .
     * supports both uppercase letters and lowercase letters.For empty String, an empty array is returned.
     *
     * @param keyText hexadecimal string
     * @return byte array representing the hexadecimal string keyText in Big Endian format
     * @throws IllegalArgumentException If string keytext not valid (odd-sized, has invalid characters, …
     */
    public static byte[] hextobyte(String keyText) {
        if (keyText.length() == 0) return new byte[0];
        if (keyText.length() % 2 != 0) throw new IllegalArgumentException("String keyText must be even-sized.");

        byte[] bytes = new byte[keyText.length() / 2];

        List<Character> chars = Stream.of('a', 'b', 'c', 'd', 'e', 'f').toList();

        for (int i = 0; i < keyText.length(); i++) {
            char c = keyText.charAt(i);
            if (!Character.isDigit(c) && !chars.contains(Character.toLowerCase(c)))
                throw new IllegalArgumentException("String keyText must contain only digits and letters a-f (A-F).");

            byte bajt = bytes[i / 2];
            bajt = (byte) (bajt << 4);
            if (Character.isDigit(c))
                bajt = (byte) (bajt | (c - '0'));
            else
                bajt = (byte) (bajt | (Character.toLowerCase(c) - 'a' + 10));

            bytes[i / 2] = bajt;
        }

        return bytes;
    }

    /**
     * Method takes a byte array and creates its hex-encoding: for each byte of given
     * array, two characters are returned in string, in big-endian notation. For zero-length array an empty string
     * must be returned. Method should use lowercase letters for creating encoding
     * @param bytearray byte array to be converted to hex-encoding
     * @return hex-encoding of given byte array in String
     */
    public static String bytetohex(byte[] bytearray) {
        if(bytearray.length == 0) return "";

        StringBuilder sb = new StringBuilder();

        for(var bajt : bytearray){
            int first = (bajt >> 4) & 0x0F;
            int second = bajt & 0x0F;

            if(first < 10) sb.append((char) (first + '0'));
            else sb.append((char) (first - 10 + 'a'));

            if(second < 10) sb.append((char) (second + '0'));
            else sb.append((char) (second - 10 + 'a'));
        }

        return sb.toString();
    }


}