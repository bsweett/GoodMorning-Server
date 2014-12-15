package com.goodmorning.database;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

public class DatabaseEncryptionManager {
		
	private Cipher cipher;
    private SecretKey key;
    private IvParameterSpec iv;

    private byte[] text;
    private byte[] output;
    
    private static DatabaseEncryptionManager instance = null;
    
    public static DatabaseEncryptionManager getInstance() {
    	  if(instance == null)
			try {
				instance = new DatabaseEncryptionManager();
			} catch (NoSuchAlgorithmException e) {
				
				e.printStackTrace();
			}
    	    return instance;
    }

    /**
     * Encrypts and Decrypts data using AES-128
     *
     * @param clearText The data to be encrypted
     * @throws java.security.NoSuchAlgorithmException
     */
    protected DatabaseEncryptionManager() throws NoSuchAlgorithmException {
		    //Generate the IV and key
		    this.iv = new IvParameterSpec(this.generateIv());
		    this.key = this.generateKey();
    }
    
    /**
     * 
     * @param pt
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws UnsupportedEncodingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public String encryptPlainText(String pt) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, 
    InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
    	this.cipher = createCipher(true);
	    this.text = this.convertClearText(pt);
	    this.output = this.encrypt();
		return this.toString();
    }
    
    /**
     * 
     * @param ct
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws UnsupportedEncodingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public String decryptCipherText(String ct) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, 
    InvalidAlgorithmParameterException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
    	this.cipher = createCipher(false);
	    this.text = this.convertClearText(ct);
	    this.output = this.decrypt();
		return this.toString();
    }


    /**
     * Converts the clear text passed by the user to an array of bytes
     *
     * @param clearText The clear text passed by the user
     * @return The byte representation of the clear text
     */
    private byte[] convertClearText(String clearText) throws UnsupportedEncodingException {

        //Convert the clear text passed by the user into bytes
        return clearText.getBytes("UTF-8");
    }

    /**
     * Creates an AES cipher using CBC mode with PKCS5 padding
     *
     * @return The cipher used to encrypt data
     */
    private Cipher createCipher(boolean encrypt) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {

        //Create an AES cipher in CBC mode using PKCS5 padding
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        if(encrypt)
        	cipher.init(Cipher.ENCRYPT_MODE, this.key, this.iv);
        else 
        	cipher.init(Cipher.DECRYPT_MODE, this.key, this.iv);
        return cipher;
    }

    /**
     * Generates a random IV to be used in the encryption process
     *
     * @return The IV's byte representation
     */
    private byte[] generateIv() {
        SecureRandom random = new SecureRandom();
        byte[] ivBytes = new byte[16];
        random.nextBytes(ivBytes);
        return ivBytes;
    }

    /**
     * Generates a secret key to be used in the encryption process
     *
     * @return The secret key
     */
    private SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keygen;
        //Java normally doesn't support 256-bit key sizes without an extra installation so stick with a 128-bit key
        keygen = KeyGenerator.getInstance("AES");
        keygen.init(128);
        SecretKey aesKey = keygen.generateKey();
        return aesKey;

    }

    /**
     * Returns the initialization vector
     *
     * @return The randomly generated IV
     */
    public IvParameterSpec getIv() {
        return this.iv;
    }

    /**
     * Returns the key used with cipher
     *
     * @return The randomly generated secret key
     */
    public SecretKey getKey() {
        return this.key;
    }

    /**
     * Encrypts the data passed from encryptPlainText
     *
     * @return The byte representation of the encrypted data
     */
    private byte[] encrypt() throws IllegalBlockSizeException, BadPaddingException {
        return this.cipher.doFinal(this.text);
    }
    
    /**
     * Decrypts the data passed from decryptCipherText
     *
     * @return The byte representation of the encrypted data
     */
    private byte[] decrypt() throws IllegalBlockSizeException, BadPaddingException {
        return this.cipher.doFinal(this.text);
    }

    /**
     * Returns the output text as a base64 encoded string
     *
     * @return The encrypted base64 encoded string
     */
    @Override
    public String toString() {
        return Base64.encodeBase64String(output);
    }
}
