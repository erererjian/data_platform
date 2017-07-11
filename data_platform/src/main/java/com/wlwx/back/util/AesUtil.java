package com.wlwx.back.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AesUtil {
	private static final Logger LOGGER = Logger.getLogger(AesUtil.class);
	
	private static final String ENCODING = "UTF-8";
	private static final String KEY = "wlwx_008@";

	public AesUtil() {
		super();
	}

	public static void main(String[] args) {
//		String data = "123456";
//		System.out.println(encryptAES(data));
//		System.out.println(decrypt(encryptAES(data)));
	}

	public static String encryptAES(String content, String password) {
		byte[] encryptResult = encrypt(content, password);
		String encryptResultStr = parseByte2HexStr(encryptResult);

		encryptResultStr = ebotongEncrypto(encryptResultStr);
		return encryptResultStr;
	}

	public static String encryptAES(String content) {
		byte[] encryptResult = encrypt(content, KEY);
		String encryptResultStr = parseByte2HexStr(encryptResult);

		encryptResultStr = ebotongEncrypto(encryptResultStr);
		return encryptResultStr;
	}

	public static String decrypt(String encryptResultStr, String password) {
		String decrpt = ebotongDecrypto(encryptResultStr);
		byte[] decryptFrom = parseHexStr2Byte(decrpt);
		byte[] decryptResult = decrypt(decryptFrom, password);
		return new String(decryptResult);
	}

	public static String decrypt(String encryptResultStr) {
		String decrpt = ebotongDecrypto(encryptResultStr);
		byte[] decryptFrom = parseHexStr2Byte(decrpt);
		byte[] decryptResult = decrypt(decryptFrom, KEY);
		return new String(decryptResult);
	}

	public static String ebotongEncrypto(String str) {
		BASE64Encoder base64encoder = new BASE64Encoder();
		String result = str;
		if ((str != null) && (str.length() > 0)) {
			try {
				byte[] encodeByte = str.getBytes(ENCODING);
				result = base64encoder.encode(encodeByte);
			} catch (Exception e) {
				LOGGER.info(e);
			}
		}
		if (str != null) 
			return result.replaceAll("\r\n", "").replaceAll("\r", "").replaceAll("\n", "");
		else 
			return str;
	}

	public static String ebotongDecrypto(String str) {
		BASE64Decoder base64decoder = new BASE64Decoder();
		try {
			byte[] encodeByte = base64decoder.decodeBuffer(str);
			return new String(encodeByte);
		} catch (IOException e) {
			LOGGER.info(e);
		}
		return str;
	}

	private static byte[] encrypt(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");

			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(password.getBytes());
			kgen.init(128, secureRandom);

			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			byte[] byteContent = content.getBytes(ENCODING);
			cipher.init(1, key);
			return cipher.doFinal(byteContent);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.info(e);
		} catch (NoSuchPaddingException e) {
			LOGGER.info(e);
		} catch (InvalidKeyException e) {
			LOGGER.info(e);
		} catch (UnsupportedEncodingException e) {
			LOGGER.info(e);
		} catch (IllegalBlockSizeException e) {
			LOGGER.info(e);
		} catch (BadPaddingException e) {
			LOGGER.info(e);
		}
		return null;
	}

	private static byte[] decrypt(byte[] content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");

			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(password.getBytes());
			kgen.init(128, secureRandom);

			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(2, key);
			return cipher.doFinal(content);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.info(e);
		} catch (NoSuchPaddingException e) {
			LOGGER.info(e);
		} catch (InvalidKeyException e) {
			LOGGER.info(e);
		} catch (IllegalBlockSizeException e) {
			LOGGER.info(e);
		} catch (BadPaddingException e) {
			LOGGER.info(e);
		}
		return null;
	}

	public static String parseByte2HexStr(byte[] buf) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = ((byte) (high * 16 + low));
		}
		return result;
	}
}