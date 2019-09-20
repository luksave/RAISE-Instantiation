package br.ufg.inf.mestrado.hermeswidget.client.sensor.bloodPressure;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Ernesto
 *
 */

public class App_HW_BloodPressure {
	
	public static void main(String[] args) {

		// Pasta com os registros MIMIC utilizados pelo HW
		/*
		File diretorioMimic = new File("./mimic/registros_utilizados/");
		*/
		File diretorioMimic = new File("./mimic/paciente-teste/");
		
		// Preparação do pool de threads de acordo com a quantidade de arquivos
		// que contém os sinais vitais
		ScheduledExecutorService poolWidgets = Executors.newScheduledThreadPool(diretorioMimic.listFiles().length);

		for (File registroAtual : diretorioMimic.listFiles())
		{
			//System.out.println("Hermes Widget Sensor: "+ registroAtual.getName() +" inicializado às "+ new Date());			
			
			HWSensorBloodPressure widget = new HWSensorBloodPressure(registroAtual, args);
			
			poolWidgets.schedule(widget, 2, TimeUnit.SECONDS);
			
		}
		
		//poolWidgets.shutdown();
		
		//while(true) {}

	}

}
