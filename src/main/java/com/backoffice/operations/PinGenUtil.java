//package com.backoffice.operations;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.SecretKeySpec;
//
//import org.apache.commons.codec.DecoderException;
//import org.apache.commons.codec.binary.Hex;
//import org.apache.commons.lang3.StringUtils;
//
//public class PinGenUtil {
//
//	public static String extractPanAccountNumberPart(final String accountNumber) {
//		String accountNumberPart = null;
//		if (accountNumber.length() > 12)
//			accountNumberPart = accountNumber.substring(accountNumber.length() - 13, accountNumber.length() - 1);
//		else
//			accountNumberPart = accountNumber;
//		return accountNumberPart;
//	}
//
//	public static String format0Encode(final String pin, final String pan) {
//		try {
//			final String pinLenHead = StringUtils.leftPad(Integer.toString(pin.length()), 2, '0') + pin;
//			final String pinData = StringUtils.rightPad(pinLenHead, 16, 'F');
//			final byte[] bPin = Hex.decodeHex(pinData.toCharArray());
//			final String panPart = extractPanAccountNumberPart(pan);
//			final String panData = StringUtils.leftPad(panPart, 16, '0');
//			final byte[] bPan = Hex.decodeHex(panData.toCharArray());
//
//			final byte[] pinblock = new byte[8];
//			for (int i = 0; i < 8; i++)
//				pinblock[i] = (byte) (bPin[i] ^ bPan[i]);
//
//			String out = Hex.encodeHexString(pinblock).toUpperCase();
//			System.out.println("Hex ZPIn Block :" + out);
//			return out;
//		} catch (DecoderException e) {
//			throw new RuntimeException("Hex decoder failed!", e);
//		}
//	}
//
//	
//
//	public static String encrypt(String plainText, String key) throws Exception {
//		Cipher cipher = getCipher(Cipher.ENCRYPT_MODE, key);
//		byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
//
//		return org.apache.commons.codec.binary.Base64.encodeBase64String(encryptedBytes);
//	}
//
//
//
//	private static Cipher getCipher(int cipherMode, String key) throws Exception {
//		String encryptionAlgorithm = "AES";
//		SecretKeySpec keySpecification = new SecretKeySpec(key.getBytes("UTF-8"), encryptionAlgorithm);
//		Cipher cipher = Cipher.getInstance(encryptionAlgorithm);
//		cipher.init(cipherMode, keySpecification);
//
//		return cipher;
//	}
////
////	public static void main(String[] args) throws Exception {
////		String setPinKey = "EC2CEC2277BFD9F0102E8241B917C185";
////		String kitNo = "60000543";
////		// Encryption
////		String formattedPinBlock = format0Encode("2323", kitNo);
////		System.out.println("formattedPinBlock Pin: " + formattedPinBlock);
////		String encryptedPin = encrypt(formattedPinBlock, setPinKey);
////		System.out.println("Encrypted Pin: " + encryptedPin);
////	}
//}
