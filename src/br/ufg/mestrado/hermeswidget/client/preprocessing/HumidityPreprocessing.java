package br.ufg.mestrado.hermeswidget.client.preprocessing;

public class HumidityPreprocessing {

	public static String HumidityComfort(int humidity){
		
		if(humidity < 40) return "Sala muito seca";
		if(humidity > 65) return "Sala muito umida";
		else 			  return "Umidade ideal";
		
	}
	
	public static double AverageRelativeHumidity(double temperature){
		
		//TODO - iMPLEMENTAR L�GICA DE RECUPERA��O DE DADOS DE HIST�RICO E M�DIA
		
		//Retornar aqui a m�dia de temperatura
		return 0.0;
		
	}
	
	
	
}
