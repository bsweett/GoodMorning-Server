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
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class DatabaseEncryptionManager {

	private SecretKeySpec skeySpec;
	private IvParameterSpec iv;

	/**
	 * Encrypts and Decrypts data using AES-128
	 *
	 * @param clearText The data to be encrypted
	 */
	public DatabaseEncryptionManager()  {

		//Generate the IV and key
		this.iv = new IvParameterSpec(this.generateIv());
		this.skeySpec =  new SecretKeySpec("D865FBCA32A5F&^0".getBytes(), "AES");

	}


	public String encrypt(String textToEncrypt) {

		try {

			byte[] plainText = textToEncrypt.getBytes("UTF-8");

			// initialize the cipher for encrypt mode
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, this.skeySpec, this.iv);

			// encrypt the message
			byte[] encrypted = cipher.doFinal(plainText);

			Base64 encoder = new Base64();

			return new String (encoder.encode(encrypted));

		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}catch(NoSuchPaddingException e){
			e.printStackTrace();
		}catch(InvalidKeyException e){
			e.printStackTrace();
		}catch(IllegalBlockSizeException e){
			e.printStackTrace();
		}catch(BadPaddingException e){
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String decrypt(String textToDecrypt) {

		try {
			Base64 encoder = new Base64();

			byte[] encryptedText = textToDecrypt.getBytes("UTF-8");

			// initialize the cipher for encrypt mode
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, this.skeySpec, this.iv);

			// decrypt the message
			byte[] decrypted = cipher.doFinal(encoder.decode(encryptedText));

			return new String(decrypted);

		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
		}catch(NoSuchPaddingException e){
			e.printStackTrace();
		}catch(InvalidKeyException e){
			e.printStackTrace();
		}catch(IllegalBlockSizeException e){
			
			//TODO: Find the cause of this. It doesn't seem to be effecting the output might be an IV issue
			//e.printStackTrace();
			
		}catch(BadPaddingException e){
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
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

}
