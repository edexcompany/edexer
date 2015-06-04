package com.edexer.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.UUID;

public class TokenGenerator {

	private static SecureRandom random = new SecureRandom();
	private static final int DEFAULT_LENGHT = 15;

	/**
	 * Generates random password with lenght 16
	 * 
	 * @return string containing the password token
	 */
	public static String generatePassword() {
		return new BigInteger(130, random).toString(32)
				.subSequence(0, DEFAULT_LENGHT - 1).toString();
	}

	/**
	 * Generate random password with certain lenght
	 * 
	 * @param lenght
	 *            the lenght of returned password
	 * @return
	 */
	public static String generatePassword(int lenght) {
		return new BigInteger(130, random).toString(32).subSequence(0, lenght)
				.toString();
	}

	/**
	 * Generate random token with certain lenght
	 * 
	 * @param lenght
	 *            the lenght of returned token
	 * @return
	 */
	public static String generateToken(int lenght) {
		return new BigInteger(130, random).toString(32).subSequence(0, lenght)
				.toString();
	}

	public static String generateUUID(boolean omitDelimiter) {
		if (omitDelimiter)
			return UUID.randomUUID().toString().replace("-", "");
		return UUID.randomUUID().toString();
	}
}
