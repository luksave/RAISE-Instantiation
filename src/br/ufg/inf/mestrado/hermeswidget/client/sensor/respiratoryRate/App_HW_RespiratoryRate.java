package br.ufg.inf.mestrado.hermeswidget.client.sensor.respiratoryRate;

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

public class App_HW_RespiratoryRate extends HermesWidgetObjects {
	
	public static void main(String[] args) {

		// Pasta com os registros MIMIC utilizados pelo HW
		/*
		File diretorioMimic = new File("./mimic/registros_utilizados/");
		*/
		File diretorioMimic = new File("./mimic/paciente-teste/");

		// Prepara��o do pool de threads de acordo com a quantidade de arquivos
		// que cont�m os sinais vitais
		ScheduledExecutorService poolWidgets = Executors.newScheduledThreadPool(diretorioMimic.listFiles().length);

		for (File registroAtual : diretorioMimic.listFiles())
		{	
			//System.out.println("Hermes Widget Sensor: "+ registroAtual.getName() +"inicializado �s "+ new Date());
			
			HWSensorRespiratoryRate widget = new HWSensorRespiratoryRate(registroAtual, args);
			
			poolWidgets.schedule(widget, 5, TimeUnit.SECONDS);
			
		}
		
		while(true) {}

	}
	
}
