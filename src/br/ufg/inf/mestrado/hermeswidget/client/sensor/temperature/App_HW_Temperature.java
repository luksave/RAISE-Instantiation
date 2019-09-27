package br.ufg.inf.mestrado.hermeswidget.client.sensor.temperature;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import br.ufg.inf.mestrado.hermeswidget.client.sensor.general.HermesWidgetObjects;

/**
 * 
 * @author Ernesto
 *
 */

public class App_HW_Temperature extends HermesWidgetObjects {
	
	public static void main(String[] args) {
		// TODO Implementar a aquisição e registro dos dados do sensor no arquivo medidas.csv
		// Pasta com os registros MIMIC, agora AirPure, utilizados pelo HW
		File diretorioAirPure = new File("./airPure/");

		// Preparação do pool de threads de acordo com a quantidade de arquivos que contém os sinais vitais
		ScheduledExecutorService poolWidgets = Executors.newScheduledThreadPool(diretorioAirPure.listFiles().length);

		for (File registroAtual : diretorioAirPure.listFiles()){	
			HWSensorTemperature widget = new HWSensorTemperature(registroAtual, args);

			poolWidgets.schedule(widget, 2, TimeUnit.SECONDS);
			
		}
		
		while(true) {}

	}
	
}
