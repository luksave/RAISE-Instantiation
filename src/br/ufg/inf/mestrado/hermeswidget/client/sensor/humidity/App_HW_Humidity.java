package br.ufg.inf.mestrado.hermeswidget.client.sensor.humidity;

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

public class App_HW_Humidity extends HermesWidgetObjects {

	public static void main(String[] args) {
		File diretorioAirPure = new File("./airPure/");
		File registroAirPure  = new File("./airPure/medidas.csv");
		
		// Preparacao do pool de threads de acordo com a quantidade de arquivos que contém os dados ambientais
		ScheduledExecutorService poolWidgets = Executors.newScheduledThreadPool(diretorioAirPure.listFiles().length);

		/*for (File registroAtual : diretorioAirPure.listFiles()){	
			//Enviar os dados adquiridos 
			HWSensorHumidity widget = new HWSensorHumidity(registroAtual, args);	
			
			// A cada 6 segundos agenda uma leitura
			poolWidgets.schedule(widget, 6, TimeUnit.SECONDS);
			
		}*/
		
		//Enviar os dados adquiridos 
		HWSensorHumidity widget = new HWSensorHumidity(registroAirPure, args);	
		
		// A cada 6 segundos agenda uma leitura
		poolWidgets.schedule(widget, 6, TimeUnit.SECONDS);
		
		//Finaliza a pool do widget
		//poolWidgets.shutdown();
		
		//while(true) {}
		
	}

}
