package chapter1;

import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.List;

/**
 * Basic class to confirm the Bouncy Castle provider is installed.
 */
public class SimpleProviderTest {
	public static void main(final String[] args) {
		String providerName = "BC";
		
		if (Security.getProvider(providerName) == null) {
			System.out.println(providerName + " provider not installed");
		} else {
			System.out.println(providerName + " is installed.");
		}
		System.out.println("----");
		System.out.println("Installed Security-Providers");
		
		Provider[] prov = Security.getProviders();
		List<Provider> list = Arrays.asList(prov);
		for (Provider provider : list) {
			System.out.println(provider.getName());
		}
		
	}
}
