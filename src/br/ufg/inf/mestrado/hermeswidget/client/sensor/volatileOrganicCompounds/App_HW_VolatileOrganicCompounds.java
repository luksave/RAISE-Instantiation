package br.ufg.inf.mestrado.hermeswidget.client.sensor.volatileOrganicCompounds;

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

public class App_HW_VolatileOrganicCompounds extends HermesWidgetObjects {

	public static void main(String[] args) {
		File diretorioAirPure = new File("./airPure/");
		File registroAirPure  = new File("./airPure/medidas.csv");
		
		// Preparacao do pool de threads de acordo com a quantidade de arquivos que contem os dados ambientais
		ScheduledExecutorService poolWidgets = Executors.newScheduledThreadPool(diretorioAirPure.listFiles().length - 1);

		/*for (File registroAtual : diretorioAirPure.listFiles()){	
			//Enviar os dados adquiridos 
			HWSensorVolatileOrganicCompounds widget = new HWSensorVolatileOrganicCompounds(registroAtual, args);	
			
			// A cada 5 segundos agenda uma leitura
			poolWidgets.schedule(widget, 6, TimeUnit.SECONDS);
			
		}*/
		
		//Enviar os dados adquiridos 
		HWSensorVolatileOrganicCompounds widget = new HWSensorVolatileOrganicCompounds(registroAirPure, args);	
		
		//A cada 6 segundos agenda uma leitura
		poolWidgets.schedule(widget, 6, TimeUnit.SECONDS);
		
		//Finaliza a pool do widget
		poolWidgets.shutdown();
		//while(true) {}
		
	}

}
