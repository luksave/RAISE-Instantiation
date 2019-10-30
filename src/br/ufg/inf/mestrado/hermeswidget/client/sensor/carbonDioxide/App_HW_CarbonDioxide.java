package br.ufg.inf.mestrado.hermeswidget.client.sensor.carbonDioxide;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import br.ufg.inf.mestrado.hermeswidget.client.sensor.general.HermesWidgetObjects;

/**
 * 
 * @author Lucas Felipe
 *
 */

public class App_HW_CarbonDioxide extends HermesWidgetObjects {

	public static void main(String[] args) {
		File diretorioAirPure = new File("./airPure/");
		File registroAirPure  = new File("./airPure/medidas.csv");
		
		//Por qu� tem 2 arquivos no diret�rio se s� tem 1?????
		//System.out.println("Arquivos no diretorio: " +diretorioAirPure.listFiles().length);
		
		// Preparação do pool de threads de acordo com a quantidade de arquivos que contém os dados ambientais
		ScheduledExecutorService poolWidgets = Executors.newScheduledThreadPool(diretorioAirPure.listFiles().length - 1);

		/*for (File registroAtual : diretorioAirPure.listFiles()){	
			//Enviar os dados adquiridos 
			HWSensorCarbonDioxide widget = new HWSensorCarbonDioxide(registroAtual, args);	
			
			// A cada 6 segundos agenda uma leitura
			poolWidgets.schedule(widget, 6, TimeUnit.SECONDS);
			
		}*/
		
		//Enviar os dados adquiridos 
		HWSensorCarbonDioxide widget = new HWSensorCarbonDioxide(registroAirPure, args);	
		
		//A cada 6 segundos agenda uma leitura
		poolWidgets.schedule(widget, 6, TimeUnit.SECONDS);
		
		//Finaliza a pool do widget
		poolWidgets.shutdown();
		
		//while(true) {}

	}

}
