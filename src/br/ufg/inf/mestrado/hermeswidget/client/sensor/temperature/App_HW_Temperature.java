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
		
		/* TESTE DESEMPENHO */
		System.out.println("INICIO POOL THREADS: "+System.currentTimeMillis());

		// Pasta com os registros MIMIC utilizados pelo HW
		File diretorioMimic = new File("./mimic/paciente-teste/");

		// Preparação do pool de threads de acordo com a quantidade de arquivos que contém os sinais vitais
		ScheduledExecutorService poolWidgets = Executors.newScheduledThreadPool(diretorioMimic.listFiles().length);

		for (File registroAtual : diretorioMimic.listFiles()){	
			HWSensorTemperature widget = new HWSensorTemperature(registroAtual, args);

			poolWidgets.schedule(widget, 2, TimeUnit.SECONDS);
			
		}
		
		while(true) {}

	}
	
}
