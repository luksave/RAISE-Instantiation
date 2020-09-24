package br.ufg.mestrado.hermeswidget.client.preprocessing;

public class TemperaturePreprocessing {

	public static String ThermalComfort(int temperature){
		
		if(temperature < 23) return "Sala muito fria";
		if(temperature > 26) return "Sala muito quente";
		else 				 return "Temperatura ideal";
		
	}
	
}
