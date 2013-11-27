package de.kkh.test.security.fileexample;

import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class CreateKeyStore {
	
	/**
	 * @param args
	 * @throws NoSuchProviderException
	 * @throws KeyStoreException
	 */
	public static void main(final String[] args) throws Exception {
		
		// Erstelle Passwort für Schlüssel
		KeyStore.PasswordProtection kspp = new KeyStore.PasswordProtection("storePassword".toCharArray());
		
		// Erstelle SecretKey
		KeyGenerator generator = KeyGenerator.getInstance("AES", "BC");
		generator.init(256);
		SecretKey encryptionKey = generator.generateKey();
		KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(encryptionKey);
		
		// Erstelle P-/P-Key
		KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA", "BC");
		kpGen.initialize(2048, new SecureRandom());
		KeyPair keyPair = kpGen.generateKeyPair();
		PrivateKey privateKey = keyPair.getPrivate();
		PublicKey publicKey = keyPair.getPublic();
		
		// Erstelle BC-KeyStore
		KeyStore bcStore = KeyStore.getInstance("UBER", "BC");
		bcStore.load(null);
		bcStore.setEntry("secretKeyAlias", skEntry, kspp);
		bcStore.setCertificateEntry("publicKeyCert", PPKeySecurityHelper.erstelleSelbstsigniertesZertifikat(keyPair));
		
		bcStore.store(new FileOutputStream("C:/Entwicklung/Test_Verschluesselung/etc/keystore.ubr"),
				"storePassword".toCharArray());
		System.out.println("BC-KeyStore angelegt!");
		
		// Erstelle IBM-Key-Store
		KeyStore ibmStore = KeyStore.getInstance("JCEKS", "IBMJCE");
		ibmStore.load(null);
		Certificate[] chain = new Certificate[1];
		chain[0] = PPKeySecurityHelper.erstelleSelbstsigniertesZertifikat(keyPair);
		PrivateKeyEntry pke = new KeyStore.PrivateKeyEntry(privateKey, chain);
		
		ibmStore.setEntry("privateKeyAlias", pke, kspp);
		
		ibmStore.setEntry("secretKeyAlias", skEntry, kspp);
		ibmStore.store(new FileOutputStream("C:/Entwicklung/Test_Verschluesselung/etc/keystore.jce"),
				"storePassword".toCharArray());
		System.out.println("IBM-KeyStore angelegt!");
	}
}
