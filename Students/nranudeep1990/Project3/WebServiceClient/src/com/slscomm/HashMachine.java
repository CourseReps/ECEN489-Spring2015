package com.slscomm;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashMachine {

    /* Method to generate and store user credential in a two element array. First element contains
       32 character (128 bit) randomly generated salt. Second element contains 64 character (256 bit)
       salted hashed password. Each element is stored as a string.
     */
    public static String[] generateSaltedUserHash (String password) throws NoSuchAlgorithmException {
        String salt = getSalt();
        String securePassword = get_SHA_256_SaltedPassword(password, salt);
        String[] credential = new String[2];
        credential[0] = salt;
        credential[1] = securePassword;
        return credential;
    }

    //Method that generates a 64 character (256 bit) unsalted hashed password
    public static String generateUnsaltedUserHash (String password) throws NoSuchAlgorithmException {
        String hash = get_SHA_256_unsaltedPassword(password);
        return hash;
    }

    //Method that generates a 16 character (64 bit) hex token used for validating an authenticated user
    public static String generateSessionID () throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] random = new byte[8];
        sr.nextBytes(random);
        return getHexString(random);

    }

    //Method that generates a 64 character (256 bit) salted hash using the SHA-256 algorithm
    private static String get_SHA_256_SaltedPassword(String passwordToHash, String salt)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());
            generatedPassword = getHexString(bytes);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    //Method used to generate an unsalted 256 bit hash
    private static String get_SHA_256_unsaltedPassword(String passwordToHash)
    {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(passwordToHash.getBytes());
            byte[] bytes = md.digest();
            generatedPassword = getHexString(bytes);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    //Method used to securely generate a random 128 bit salt
    private static String getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return getHexString(salt);
    }
    //Simple method that checks string equality
    public static boolean validatePassword (String passToCheck, String storedHash, String salt) throws NoSuchAlgorithmException
    {
        String testHash = get_SHA_256_SaltedPassword(passToCheck, salt);
        if (testHash.equals(storedHash))
            return true;
        else
            return false;
    }

    //Method to securely verify two salted hashes. Returns true if passwords match
    public static boolean securelyValidatePassword(String passToCheck, String storedPassword, String storedSalt) throws NoSuchAlgorithmException
    {
        byte[] salt = getByteArray(storedSalt);
        byte[] storedHash = getByteArray(storedPassword);
        byte[] testHash = getByteArray(get_SHA_256_SaltedPassword(passToCheck, storedSalt));

        int diff = storedHash.length ^ testHash.length;
        for(int i = 0; i < storedHash.length && i < testHash.length; i++)
        {
            diff |= storedHash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    //Method to securely verify two unsalted hashes. Returns true if passwords match
    public static boolean securelyValidateUnsaltedPassword(String passToCheck, String storedPassword) throws NoSuchAlgorithmException
    {
        byte[] storedHash = getByteArray(storedPassword);
        byte[] testHash = getByteArray(passToCheck);

        int diff = storedHash.length ^ testHash.length;
        for(int i = 0; i < storedHash.length && i < testHash.length; i++)
        {
            diff |= storedHash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    //Method used to convert a byte array into a hex formatted string
    private static String getHexString(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  + paddingLength + "d", 0) + hex;
        }
        else{
            return hex;
        }
    }

    //Method used to convert a hex formatted string into a byte array
    private static byte[] getByteArray(String hex) throws NoSuchAlgorithmException
    {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
}
