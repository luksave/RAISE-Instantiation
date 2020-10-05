package br.ufg.mestrado.hermeswidget.client.preprocessing;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import br.ufg.inf.mestrado.hermeswidget.client.utils.ReaderJSon;

public class ITUPreprocessing {

	public static double THIndex(double temperature) throws JSONException, IOException{
		
		String url = "https://api.thingspeak.com/channels/1153475/feeds/last.json?api_key=G75ZY80ZLPN9OXEQ";
		JSONObject json = ReaderJSon.readJsonFromUrl(url);
		String umidade = json.get("field2").toString();
		
		//TA = Temperatura do Ar
		//UR = Umidade Relativa
			   //Precisamos aqui consultar no modelo quanto era o 
			   //valor da umidade no momento dessa temperatura.
			   //Por enquanto consulto pelo json...
		double TA = temperature;
		double UR = Double.parseDouble(umidade); 
		double ITU;
		
		// THOM (1959)
		ITU = (0.8 * TA + (UR/100)*(TA - 14.4) + 46.4);
		
		return ITU;
		
	}
	
}
