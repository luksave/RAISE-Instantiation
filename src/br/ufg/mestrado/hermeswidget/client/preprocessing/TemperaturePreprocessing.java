package br.ufg.mestrado.hermeswidget.client.preprocessing;

public class TemperaturePreprocessing {

	public static String ThermalComfort(double temperature){
		
		if(temperature < 23.00) return "Sala muito fria";
		if(temperature > 26.00) return "Sala muito quente";
		else 				    return "Temperatura ideal";
		
	}
	
	public static double AverageTemperature(double temperature){
		
		//TODO - iMPLEMENTAR L�GICA DE RECUPERA��O DE DADOS DE HIST�RICO E M�DIA
		
		//Retornar aqui a m�dia de temperatura
		return 0.0;
		
	}
	
	
	
}
