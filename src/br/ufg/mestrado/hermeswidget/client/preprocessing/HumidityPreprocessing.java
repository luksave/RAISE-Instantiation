package br.ufg.mestrado.hermeswidget.client.preprocessing;

public class HumidityPreprocessing {

	public static String HumidityComfort(int humidity){
		
		if(humidity < 40) return "Sala muito seca";
		if(humidity > 65) return "Sala muito umida";
		else 			  return "Umidade ideal";
		
	}
	
	public static double AverageRelativeHumidity(double temperature){
		
		//TODO - iMPLEMENTAR LÓGICA DE RECUPERAÇÃO DE DADOS DE HISTÓRICO E MÉDIA
		
		//Retornar aqui a média de temperatura
		return 0.0;
		
	}
	
	
	
}
