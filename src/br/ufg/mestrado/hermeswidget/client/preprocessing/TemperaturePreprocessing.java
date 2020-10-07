package br.ufg.mestrado.hermeswidget.client.preprocessing;

public class TemperaturePreprocessing {

	public static String ThermalComfort(double temperature){
		
		if(temperature < 23.00) return "Sala muito fria";
		if(temperature > 26.00) return "Sala muito quente";
		else 				    return "Temperatura ideal";
		
	}
	
	public static double AverageTemperature(double temperature){
		
		//TODO - iMPLEMENTAR LÓGICA DE RECUPERAÇÃO DE DADOS DE HISTÓRICO E MÉDIA
		
		//Retornar aqui a média de temperatura
		return 0.0;
		
	}
	
	
	
}
