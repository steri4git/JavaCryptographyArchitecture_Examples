package de.kkh.test.security.fileexample;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.crypto.Cipher;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.x509.X509V3CertificateGenerator;

public class PPKeySecurityHelper {
	
	public static String entschluesselnString(final byte[] secret, final String algortihmus, final String provider)
			throws Exception {
		
		Cipher cipher = Cipher.getInstance(algortihmus, provider);
		
		PrivateKey privateKey = getPrivateKeyFromIBMKeyStore();
		
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] klartextByteArray = new byte[secret.length];
		
		klartextByteArray = cipher.doFinal(secret);
		
		String klartext = new String(klartextByteArray);
		System.out.println("Entschluesseln fertig! " + provider + " " + algortihmus);
		return klartext;
	}
	
	public static byte[] verschluesselnString(final String klartext, final String algortihmus, final String provider)
			throws Exception {
		
		Cipher cipher = Cipher.getInstance(algortihmus, provider);
		
		PublicKey publicKey = getPublicKeyFromBCKeyStore();
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] klartextByteArray = klartext.getBytes();
		byte[] secretByteArray = cipher.doFinal(klartextByteArray);
		System.out.println("Verschluesseln fertig! " + provider + " " + algortihmus);
		return secretByteArray;
	}
	
	private static PublicKey getPublicKeyFromBCKeyStore() throws Exception {
		
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
		X509Certificate cert = (X509Certificate) store.getCertificate("publicKeyCert");
		System.out.println(cert.getIssuerDN().getName());
		return cert.getPublicKey();
	}
	
	private static PrivateKey getPrivateKeyFromIBMKeyStore() throws Exception {
		
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
		
		PrivateKey privateKey = (PrivateKey) store.getKey("privateKeyAlias", password);
		return privateKey;
	}
	
	public static X509Certificate erstelleSelbstsigniertesZertifikat(final KeyPair keyPair) throws Exception,
			InvalidKeyException, IllegalStateException, NoSuchProviderException, NoSuchAlgorithmException,
			SignatureException {
		
		Date startDate = new Date(System.currentTimeMillis() - 1000000000); // time from which certificate is valid
		Date expiryDate = new Date(System.currentTimeMillis() + 1000000000);
		; // time after which certificate is not valid
		BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis()); // serial number for certificate
		// PrivateKey caKey = ...; // private key of the certifying authority (ca) certificate
		// X509Certificate caCert = ...; // public key certificate of the certifying authority
		// KeyPair keyPair = ...; // public/private key pair that we are creating certificate for
		
		X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();
		X500Principal subjectName = new X500Principal("CN=Test V3 Certificate");
		
		certGen.setSerialNumber(serialNumber);
		certGen.setIssuerDN(new X500Principal("CN=Test V3 Certificate"));
		certGen.setNotBefore(startDate);
		certGen.setNotAfter(expiryDate);
		certGen.setSubjectDN(subjectName);
		certGen.setPublicKey(keyPair.getPublic());
		certGen.setSignatureAlgorithm("SHA256withRSA");
		
		X509Certificate cert = certGen.generate(keyPair.getPrivate(), "BC"); // note: private key of CA
		return cert;
		
	}
}
