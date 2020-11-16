package com.dancers.service.system.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class PasswordSecureHash {

    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    // The following constants may be changed without breaking existing hashes.
    public static final int SALT_BYTE_SIZE = 24;
    public static final int HASH_BYTE_SIZE = 24;
    public static final int PBKDF2_ITERATIONS = 1000;

    public static final int ITERATION_INDEX = 0;
    public static final int SALT_INDEX = 1;
    public static final int PBKDF2_INDEX = 2;

    public static String hashEncrypt(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {

        byte[] hash = pbkdf2(password.toCharArray(), salt.getBytes(), PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
        System.err.println(PBKDF2_ITERATIONS + ":" + toHex(salt.getBytes()) + ":" + toHex(hash));
        return toHex(hash);
    }

    public static String createRandom() {
        SecureRandom random = new SecureRandom();
        return String.valueOf(random.nextLong());
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String pass = PasswordSecureHash.hashEncrypt("C2t0i2m0!123qwe", "ctim");//luohailing
        System.err.println(pass);
    }

    /**
     * Computes the PBKDF2 hash of a password.
     *
     * @param password
     *            the password to hash.
     * @param salt
     *            the salt
     * @param iterations
     *            the iteration count (slowness factor)
     * @param bytes
     *            the length of the hash to compute in bytes
     * @return the PBDKF2 hash of the password
     */
    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }

    /**
     * Converts a string of hexadecimal characters into a byte array.
     *
     * @param hex
     *            the hex string
     * @return the hex string decoded into a byte array
     */
    @SuppressWarnings("unused")
    private static byte[] fromHex(String hex) {
        byte[] binary = new byte[hex.length() / 2];
        for (int i = 0; i < binary.length; i++) {
            binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return binary;
    }

    /**
     * Converts a byte array into a hexadecimal string.
     *
     * @param array
     *            the byte array to convert
     * @return a length*2 character string encoding the byte array
     */
    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0)
            return String.format("%0" + paddingLength + "d", 0) + hex;
        else
            return hex;
    }
}