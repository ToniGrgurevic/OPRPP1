package hr.fer.oprpp1.hw05.crypto;


import hr.fer.oprpp1.hw05.util.Util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;

import static hr.fer.oprpp1.hw05.util.Util.hextobyte;

public class Crypto<T> {




    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IOException {


        String append= "src/main/resources/";
        if(args.length == 2 && args[0].equals("checksha"))
            Sazetak("src/main/resources/" + args[1]);
       else if(args.length == 3 && (args[0].equals("encrypt") || args[0].equals("decrypt")))
           EnxrptDexript(args[0], append+args[1], append+args[2]);


         


    }

    /**
     * Method is used for calculating the SHA-256 digest and compating it with user input.
     */
    private static void Sazetak(String file) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please provide expected sha-256 digest for " + file + ":");
        String expected = sc.next();
        byte[] result  = null;
        try (InputStream br = Files.newInputStream(Path.of(file))){
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[4096];
            while(true){
                int read = br.read(buffer);
                if(read == -1) break;
                sha.update(buffer,0,read);
            }
            result = sha.digest();

        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        StringBuilder sb = new StringBuilder();

        sb.append("Digesting completed. Digest of ")
                .append(Path.of(file).getFileName())
                .append(" ");
        if(Util.bytetohex(result).equals(expected)) sb.append("matches expected digest.");
        else sb.append("does not match the expected digest. Digest was: ")
                .append(Util.bytetohex(result));
        System.out.println(sb);

    }

    /**
     * Method is used for encrypting or decrypting file based on user input.
     * @param operation encription or decryption
     * @param input file to be encrypted or decrypted
     * @param output    file to be created based on action and input file
     */
    private static void EnxrptDexript(String operation, String input, String output) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IOException {

        Path inputFile = Path.of(input);
        Path outputFile = Path.of(output);
        var encrypt = operation.equals("encrypt");
        Scanner sc = new Scanner(System.in);

        System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):");
        String keyText = sc.next();// what user provided for password …
        System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits):");
        String ivText = sc.next(); //what user provided for initialization vector …

        SecretKeySpec keySpec = new SecretKeySpec(hextobyte(keyText), "AES");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(hextobyte(ivText));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);



        try (InputStream br = Files.newInputStream(inputFile) ;
             OutputStream bw = Files.newOutputStream(outputFile) ){
            int sizeOfBuffer = 4096;
            while(true){
                byte[] buffer= new byte[sizeOfBuffer];

                int read = br.read(buffer,0,4096);

                if(read == -1) {
                    byte[] resultBytes = cipher.doFinal();
                    bw.write(resultBytes,0,resultBytes.length);
                    break;
                };

                byte[] resultBytes = cipher.update(buffer,0,read);
                bw.write(resultBytes,0,resultBytes.length);


            }
        }catch (IOException e) {
            throw new RuntimeException("Given path file is unvalid");
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }

        StringBuilder sb = new StringBuilder();

        if (encrypt) sb.append("Encryption");
        else sb.append("Decryption");

        sb.append(" completed. Generated file ")
                .append(inputFile.getFileName())
                .append(" based on file ")
                .append(outputFile.getFileName())
                .append(".");
        System.out.println(sb);
    }



}
