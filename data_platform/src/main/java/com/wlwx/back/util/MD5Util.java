package com.wlwx.back.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;

public class MD5Util {
	private static final String HEX_NUMS_STR = "0123456789ABCDEF";
	private static final Integer SALT_LENGTH = Integer.valueOf(12);

	private MD5Util(){
		super();
	}
	
	public static byte[] hexStringToByte(String hex) {
		int len = hex.length() / 2;
		byte[] result = new byte[len];
		char[] hexChars = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (HEX_NUMS_STR.indexOf(hexChars[pos]) << 4 | HEX_NUMS_STR
					.indexOf(hexChars[(pos + 1)]));
		}
		return result;
	}

	public static String byteToHexString(byte[] salt) {
		StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < salt.length; i++) {
			String hex = Integer.toHexString(salt[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString.append(hex.toUpperCase());
		}
		return hexString.toString();
	}

	public static boolean validPasswd(String passwd, String dbPasswd)
			throws Exception {
		byte[] pwIndb = hexStringToByte(dbPasswd);

		byte[] salt = new byte[SALT_LENGTH.intValue()];
		System.arraycopy(pwIndb, 0, salt, 0, SALT_LENGTH.intValue());

		MessageDigest md = MessageDigest.getInstance("MD5");

		md.update(salt);
		md.update(passwd.getBytes("UTF-8"));
		byte[] digest = md.digest();

		byte[] digestIndb = new byte[pwIndb.length - SALT_LENGTH.intValue()];

		System.arraycopy(pwIndb, SALT_LENGTH.intValue(), digestIndb, 0,
				digestIndb.length);

		if (Arrays.equals(digest, digestIndb)) {
			return true;
		}
		return false;
	}

	public static String getEncryptedPwd(String passwd)
			throws Exception {
		byte[] pwd;
		SecureRandom sc = new SecureRandom();
		byte[] salt = new byte[SALT_LENGTH.intValue()];
		sc.nextBytes(salt);

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(salt);
		md.update(passwd.getBytes("UTF-8"));
		byte[] digest = md.digest();

		pwd = new byte[salt.length + digest.length];
		System.arraycopy(salt, 0, pwd, 0, SALT_LENGTH.intValue());
		System.arraycopy(digest, 0, pwd, SALT_LENGTH.intValue(), digest.length);
		return byteToHexString(pwd);
	}

}