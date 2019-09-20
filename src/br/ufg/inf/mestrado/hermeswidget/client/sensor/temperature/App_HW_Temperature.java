package br.ufg.inf.mestrado.hermeswidget.client.sensor.temperature;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//import br.ufg.inf.mestrado.hermeswidget.client.sensor.temperature.deprecated.HWSensorTemperature;

/**
 * 
 * @author Ernesto
 *
 */

public class App_HW_Temperature {
	
	public static void main(String[] args) {
		
		/* TESTE DESEMPENHO */
		System.out.println("INICIO POOL THREADS: "+System.currentTimeMillis());
		long tInicial = System.currentTimeMillis();

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
			
			HWSensorTemperature widget = new HWSensorTemperature(registroAtual, args);
			
			/* TESTE DESEMPENHO */
			long tFinal = System.currentTimeMillis();
			
			System.out.print("TF: "+tFinal+" - TI: "+tInicial+" =  ");
			System.out.println((tFinal-tInicial));
			/* FIM TESTE */
			
			// 15 - delay para montar o modelo
			//poolWidgets.schedule(widget, 15, TimeUnit.SECONDS);
			poolWidgets.schedule(widget, 2, TimeUnit.SECONDS);
			
		}
		
		/* TESTE DESEMPENHO */
		System.out.println("FIM POOL THREADS: "+System.currentTimeMillis()+"\n");
		
		
		
		while(true) {}

	}
	
}
