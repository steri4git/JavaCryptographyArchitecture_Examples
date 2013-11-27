package de.kkh.test.security.fileexample;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class SecurityHelper {
	
	public static String entschluesselnString(final byte[] secret, final String algortihmus, final String provider)
			throws Exception {
		
		Cipher cipher = Cipher.getInstance(algortihmus, provider);
		SecretKey encryptionKey = null;
		
		if (provider.equals("BC")) {
			encryptionKey = getSecretKeyFromBCKeyStore();
		} else {
			encryptionKey = getSecretKeyFromIBMKeyStore();
		}
		
		cipher.init(Cipher.DECRYPT_MODE, encryptionKey, (getIV()));
		byte[] klartextByteArray = new byte[secret.length];
		
		int ctLength = cipher.update(secret, 0, klartextByteArray.length, klartextByteArray, 0);
		
		ctLength += cipher.doFinal(klartextByteArray, ctLength);
		
		String klartext = new String(klartextByteArray);
		System.out.println("Entschluesseln fertig! " + provider + " " + algortihmus);
		return klartext;
	}
	
	public static byte[] verschluesselnString(final String klartext, final String algortihmus, final String provider)
			throws Exception {
		
		Cipher cipher = Cipher.getInstance(algortihmus, provider);
		
		SecretKey encryptionKey = null;
		
		if (provider.equals("BC")) {
			encryptionKey = getSecretKeyFromBCKeyStore();
		} else {
			encryptionKey = getSecretKeyFromIBMKeyStore();
		}
		
		byte[] klartextByteArray = klartext.getBytes();
		byte[] secretByteArray = new byte[klartextByteArray.length];
		cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, getIV());
		int ctLength = cipher.update(klartextByteArray, 0, klartextByteArray.length, secretByteArray, 0);
		
		ctLength += cipher.doFinal(secretByteArray, ctLength);
		System.out.println("Verschluesseln fertig! " + provider + " " + algortihmus);
		return secretByteArray;
	}
	
	private static SecretKey getSecretKeyFromBCKeyStore() throws Exception {
		
		KeyStore store = KeyStore.getInstance("UBER", "BC");
		
		// get user password and file input stream
		char[] password = "storePassword".toCharArray();
		
		java.io.FileInputStream fis = null;
		try {
			fis = new java.io.FileInputStream("C:/Entwicklung/Test_Verschluesselung/etc/keystore.ubr");
			store.load(fis, password);
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
		SecretKey key = (SecretKey) store.getKey("secretKeyAlias", password);
		
		return key;
	}
	
	private static SecretKey getSecretKeyFromIBMKeyStore() throws Exception {
		
		KeyStore store = KeyStore.getInstance("JCEKS", "IBMJCE");
		
		// get user password and file input stream
		char[] password = "storePassword".toCharArray();
		
		java.io.FileInputStream fis = null;
		try {
			fis = new java.io.FileInputStream("C:/Entwicklung/Test_Verschluesselung/etc/keystore.jce");
			store.load(fis, password);
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
		SecretKey key = (SecretKey) store.getKey("secretKeyAlias", password);
		
		return key;
	}
	
	private static IvParameterSpec getIV() {
		
		byte[] ivBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x00, 0x01, 0x02, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, 0x01 };
		
		return new IvParameterSpec(ivBytes);
	}
}