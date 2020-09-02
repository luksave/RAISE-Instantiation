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
	  //File registroAirPure  = new File("./airPure/medidas.csv");          //Para esse o parâmetro é: 30000 300
		File registroAirPure  = new File("./airPure/medidas_reduzido.csv"); //Para esse o parâmetro é: 1437 1
		
		// Preparacao da pool de threads de acordo com a quantidade de arquivos que contem os dados ambientais
		ScheduledExecutorService poolWidgets = Executors.newScheduledThreadPool(diretorioAirPure.listFiles().length - 1);
		
		//Enviar os dados adquiridos 
		HWSensorCarbonDioxide widget = new HWSensorCarbonDioxide(registroAirPure, args);	
		
		//A cada 6 segundos agenda uma leitura
		poolWidgets.schedule(widget, 1, TimeUnit.SECONDS);
		
		//Finaliza a pool do widget
		poolWidgets.shutdown();
		
		//while(true) {}

	}

}
