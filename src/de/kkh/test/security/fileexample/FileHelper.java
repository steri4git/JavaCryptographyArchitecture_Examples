package de.kkh.test.security.fileexample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileHelper {
	
	static String KLARTEXTDATEI_INPUT_PFAD = "C:/Entwicklung/Test_Verschluesselung/etc/Klartext_Input.txt";
	static String SECRETDATEI_PFAD = "C:/Entwicklung/Test_Verschluesselung/etc/Secret.txt";
	static String KLARTEXTDATEI_OUTPUT_PFAD = "C:/Entwicklung/Test_Verschluesselung/etc/Klartext_Output.txt";
	
	public static String leseKlartextInput() {
		
		String klartext = "";
		try {
			FileInputStream fstream = new FileInputStream(KLARTEXTDATEI_INPUT_PFAD);
			
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String zeile;
			while ((zeile = br.readLine()) != null) {
				klartext += zeile;
			}
			in.close();
		} catch (Exception e) {
			System.err.println("Error beim einlesen der Datei " + KLARTEXTDATEI_INPUT_PFAD + " ! Message:"
					+ e.getMessage());
		}
		
		return klartext;
	}
	
	public static void schreibeKlartextOutput(final String parameter) throws IOException {
		
		FileWriter fileWriter = new FileWriter(KLARTEXTDATEI_OUTPUT_PFAD, false);
		BufferedWriter out = new BufferedWriter(fileWriter);
		out.write(parameter);
		out.newLine();
		out.close();
	}
	
	public static void schreibeSecret(final byte[] parameter) throws IOException {
		
		FileOutputStream fos = new FileOutputStream(SECRETDATEI_PFAD);
		fos.write(parameter);
		fos.close();
	}
	
	public static byte[] leseSecret() throws IOException {
		
		File file = new File(SECRETDATEI_PFAD);
		FileInputStream fin = new FileInputStream(file);
		byte[] secret = new byte[(int) file.length()];
		fin.read(secret);
		return secret;
	}
}