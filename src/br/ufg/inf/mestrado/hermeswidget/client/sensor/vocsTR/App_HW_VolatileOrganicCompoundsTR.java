package br.ufg.inf.mestrado.hermeswidget.client.sensor.vocsTR;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import br.ufg.inf.mestrado.hermeswidget.client.sensor.vocsTR.HWSensorVolatileOrganicCompoundsTR;

/**
 * 
 * @author Lucas Felipe
 *
 */

public class App_HW_VolatileOrganicCompoundsTR {

	public static void main(String[] args) {
		
		// Preparacao do pool de threads de acordo com a quantidade de dispositivos 
		//Air-Pure sendo lidos...
		ScheduledExecutorService poolWidgets = Executors.newScheduledThreadPool(1);

		//A leitura e modelagem dos dados adquiridos 
		HWSensorVolatileOrganicCompoundsTR widget = new HWSensorVolatileOrganicCompoundsTR(args);
		
		//A cada 4 segundos agenda uma leitura e modelagem de dado ambiental
		poolWidgets.schedule(widget, 4, TimeUnit.SECONDS);
		
		//Finaliza o pool do widget
		poolWidgets.shutdown();
		
	}

}