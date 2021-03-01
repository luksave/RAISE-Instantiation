package br.ufg.inf.mestrado.hermeswidget.client.preprocessing;

import br.ufg.inf.mestrado.hermeswidget.client.utils.PersistenceTDB;

public class HumidityPreprocessing {

	public static String HumidityComfort(double humidity){
		
		if(humidity < 40.00) return "Sala muito seca";
		if(humidity > 65.00) return "Sala muito umida";
		else 			     return "Umidade ideal";
		
	}
	
	public static String AverageRelativeHumidity(PersistenceTDB dataset){
		
		//TODO - iMPLEMENTAR LÓGICA DE RECUPERAÇÃO DE DADOS DE HISTÓRICO E MÉDIA
		
		String query = "SELECT * WHERE {?x ?r ?y}";
		
		dataset.consultar(query);
		
		//Retornar aqui a média de temperatura
		return "0.0";
		
	}
	
	
	
}
