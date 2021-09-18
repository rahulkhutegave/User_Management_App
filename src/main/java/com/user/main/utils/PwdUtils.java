package com.user.main.utils;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.user.main.exception.PasswordEncryptDecryptException;
import com.user.main.exception.UserAppException;


public class PwdUtils {

	private static final String SECRET_KEY = "abc123xyz123abcd";
	private static final String INIT_VECTOR = "abc123xyz123abcd";
	
	private PwdUtils() {
		
	}

	public static String encryptMsg(String msg) throws UserAppException {
		IvParameterSpec ivParamSpec = new IvParameterSpec(INIT_VECTOR.getBytes());
		byte[] encrypted=null;
		SecretKeySpec secretKeySpec;
		try {
			secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParamSpec);

			 encrypted = cipher.doFinal(msg.getBytes());
		} catch (Exception e) {
			throw new PasswordEncryptDecryptException(e.getMessage());
		}

		return Base64.getEncoder().encodeToString(encrypted);
	}

	public static String decryptMsg(String msg) throws PasswordEncryptDecryptException {
		IvParameterSpec ivParamSpec = new IvParameterSpec(INIT_VECTOR.getBytes());
		byte[] decrypted=null;
		SecretKeySpec secretKeySpec;
		try {
			secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParamSpec);

			byte[] decodedMsg = Base64.getDecoder().decode(msg);

			 decrypted = cipher.doFinal(decodedMsg);
		} catch (Exception e) {
			throw new PasswordEncryptDecryptException(e.getMessage());
		}
		return new String(decrypted);

	}

}