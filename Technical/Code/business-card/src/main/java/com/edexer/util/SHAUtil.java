package com.edexer.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAUtil {
	
	private static MessageDigest MESSAGE_DIGEST;
	
	
	static{
		init();
	}
	private static void init(){
		try {
			MESSAGE_DIGEST = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized static String getSHA256Encoded(String data){
		MESSAGE_DIGEST.reset();
		MESSAGE_DIGEST.update(data.getBytes());
		byte[] hashed = MESSAGE_DIGEST.digest();
		return Base64EncoderUtil.encode(hashed);
	}

}
