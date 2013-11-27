package chapter1;

import javax.crypto.Cipher;

/**
 * Basic demonstration of precedence in action.
 */
public class PrecedenceTest {
	public static void main(final String[] args) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
		
		System.out.println(cipher.getProvider());
		
		cipher = Cipher.getInstance("AES/CTR/NoPadding", "IBMJCE");
		
		System.out.println(cipher.getProvider());
	}
}
