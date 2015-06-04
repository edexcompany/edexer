package com.edexer.util;

import org.apache.commons.codec.binary.Base64;

public class Base64EncoderUtil {

	public static String encode(String data) {
		return encode(data.getBytes());
	}
	
	public static String encode(byte[] b){
		return Base64.encodeBase64String(b);
	}
	
	public static String decode(String data){
		return decode(data.getBytes());
	}
	
	public static String decode(byte[] b){
		return new String(Base64.decodeBase64(b));
	}
}
