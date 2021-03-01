package br.ufg.inf.mestrado.hermeswidget.testes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;



public class InicializarLeiturasAirPure {

	public static void main(String[] args) throws JSONException, IOException {
		
		String url = "https://api.thingspeak.com/channels/869608/feeds/last.json?api_key=I1ROU4UHAC0AWDPL";
		
		JSONObject json = readJsonFromUrl(url);
		
		while(json != null){
			System.out.println("Instante " + json.get("created_at"));
			
			System.out.println("Concentração de CO2: " + json.get("field5") +" PPM");
			System.out.println("Humidade Relativa: " + json.get("field2") +" %");
			System.out.println("Temperatura: " + json.get("field1") +" °C");
			System.out.println("TVOC: " + json.get("field4") +" ppb");
	
			json = readJsonFromUrl(url);
			
			if(json == null) System.out.println("LASQUERA");
			
		}

	}

	
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	
	    int cp;
	    
	    while ((cp = rd.read()) != -1) sb.append((char) cp);
	    
	    return sb.toString();
	    
	  }
	
	
	
	public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			
			return json;
		      
		} finally {is.close();}
		
	}
	
	
}
