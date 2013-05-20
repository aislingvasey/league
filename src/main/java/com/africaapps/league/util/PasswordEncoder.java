package com.africaapps.league.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasswordEncoder {
	
	public final static String ALGORITHM_MD5 = "MD5";
	
	private static Logger logger = LoggerFactory.getLogger(PasswordEncoder.class);
	
	/**
	 * Encodes a given plain text password
	 * @param clearTextPassword
	 * @return
	 */
	public static String getEncodedPassword(String clearTextPassword) {
		String encodedPassword = null;

		MessageDigest md;
		try {
			md = MessageDigest.getInstance(ALGORITHM_MD5);
			md.update(clearTextPassword.getBytes());
			encodedPassword = new String(md.digest());
		} catch (NoSuchAlgorithmException e) {
			logger.error("No MD5 algorithm available: ", e);
		}

		return encodedPassword;
	}
	
	/**
	 * compares a plain text password with an existing encrypted password
	 */
	public static boolean validatePassword(String clearTextPassword, String encodedPassword) {
		if (clearTextPassword != null && clearTextPassword.trim().equals("")) {
		  //special case - a blank password
		  if (encodedPassword != null && encodedPassword.trim().equals("")) {
		    return true;
		  } else {
		    return false;
		  }
		} else {
		  String encodedClearTextPassword = getEncodedPassword(clearTextPassword);	
		  return encodedClearTextPassword.equals(encodedPassword);
		}
	}
}
