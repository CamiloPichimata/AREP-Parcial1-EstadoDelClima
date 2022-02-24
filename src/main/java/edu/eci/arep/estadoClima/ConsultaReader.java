package main.java.edu.eci.arep.estadoClima;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class ConsultaReader {
	
	public static String consultaURL(String URL) {
		String rtaAPI = null;
		URL consulta = null;
		try {
			consulta = new URL(URL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}	
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(consulta.openStream()))) {
			String inputLine = null;
			while ((inputLine = reader.readLine()) != null) {
				rtaAPI = rtaAPI + inputLine;
			}
			System.out.println(inputLine);
		} catch (IOException x) {
			System.err.println(x);
		}
		return rtaAPI;
	}
}
