package de.kkh.test.security.fileexample;

import java.io.IOException;

public class SecurePPKeyFileExample {
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(final String[] args) throws Exception {
		String klartextInput = FileHelper.leseKlartextInput();
		byte[] secureOutput = PPKeySecurityHelper.verschluesselnString(klartextInput, "RSA/NONE/OAEPPADDING", "BC");
		FileHelper.schreibeSecret(secureOutput);
		byte[] secureInput = FileHelper.leseSecret();
		String klartextOutput = PPKeySecurityHelper.entschluesselnString(secureInput, "RSA/ /OAEPPaddingSHA-1",
				"IBMJCE");
		FileHelper.schreibeKlartextOutput(klartextOutput);
		System.out.println("Beispiel durchgelaufen!");
	}
}
