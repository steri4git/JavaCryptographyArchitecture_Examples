package de.kkh.test.security.fileexample;

import java.io.IOException;

public class SecureFileExample {
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(final String[] args) throws Exception {
		String klartextInput = FileHelper.leseKlartextInput();
		byte[] secureOutput = SecurityHelper.verschluesselnString(klartextInput, "AES/CTR/NoPadding", "BC");
		FileHelper.schreibeSecret(secureOutput);
		byte[] secureInput = FileHelper.leseSecret();
		String klartextOutput = SecurityHelper.entschluesselnString(secureInput, "AES/CTR/NoPadding", "IBMJCE");
		FileHelper.schreibeKlartextOutput(klartextOutput);
		System.out.println("Beispiel durchgelaufen!");
	}
}
