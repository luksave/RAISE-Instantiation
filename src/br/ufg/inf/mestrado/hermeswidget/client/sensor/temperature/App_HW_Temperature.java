package br.ufg.inf.mestrado.hermeswidget.client.sensor.temperature;

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

public class App_HW_Temperature extends HermesWidgetObjects {
	
	public static void main(String[] args) {
		File diretorioAirPure = new File("./airPure/");
	  //File registroAirPure  = new File("./airPure/medidas.csv");          //Para esse o par�metro �: 30000 300
		File registroAirPure  = new File("./airPure/medidas_reduzido.csv"); //Para esse o par�metro �: 1437 1
						
		// Preparacao do pool de threads de acordo com a quantidade de arquivos que contem os dados ambientais
		ScheduledExecutorService poolWidgets = Executors.newScheduledThreadPool(diretorioAirPure.listFiles().length - 1);

		//Enviar os dados adquiridos 
		HWSensorTemperature widget = new HWSensorTemperature(registroAirPure, args);
		
		//A cada 6 segundos agenda uma leitura
		poolWidgets.schedule(widget, 22, TimeUnit.SECONDS);
		
		//Finaliza o pool do widget
		poolWidgets.shutdown();
		
		//while(true) {}

	}
	
}
