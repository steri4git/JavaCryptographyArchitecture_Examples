package de.kkh.test.security.fileexample;

import java.io.IOException;

public class FileExample {
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {
		String input = FileHelper.leseKlartextInput();
		FileHelper.schreibeSecret(input.getBytes());
		byte[] secret = FileHelper.leseSecret();
		String output = new String(secret);
		FileHelper.schreibeKlartextOutput(output);
	}
}
