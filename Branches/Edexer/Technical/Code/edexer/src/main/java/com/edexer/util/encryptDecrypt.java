package com.edexer.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.mysql.jdbc.util.Base64Decoder;

public class encryptDecrypt {

	private static final String ALGO = "AES";
	private static final byte[] keyValue =  new byte[] { 'e', 'd', 'e', 'x',
			'e', 'r', 'f', 'i', 'n', 'd', 'o', 'n', 'k', 'e', 'e', 'y' };
	private static final byte[] OwnkeyValue=PasswordHash.DEFAULT_SALT.getBytes();
	public String encrypt(String Pass) {
		try {
			if (Pass != null && !Pass.equals("")) {
				Key key = generateKey();
				Cipher c = Cipher.getInstance(ALGO);				
				c.init(Cipher.ENCRYPT_MODE, key);
				byte[] encVal = c.doFinal(Pass.getBytes());
				String encryptedValue = new Base64EncoderUtil().encode(encVal);
				return encryptedValue;
			}
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public String decrypt(String encryptedData) {
		try {
			if (encryptedData != null && !encryptedData.equals("")) {
				Key key = generateKey();
				Cipher c = Cipher.getInstance(ALGO);
				c.init(Cipher.DECRYPT_MODE, key);
				byte[] decordedValue = new Base64Decoder().decode(
						encryptedData.getBytes(), 0, encryptedData.length());
				byte[] decValue = c.doFinal(decordedValue);
				String decryptedValue = new String(decValue);
				return decryptedValue;
			}
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private Key generateKey() throws Exception {
		Key key = new SecretKeySpec(keyValue, ALGO);
		return key;
	}
}
