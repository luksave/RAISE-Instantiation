package br.ufg.inf.mestrado.hermeswidget.client.sensor.carbonDioxideTR;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import br.ufg.inf.mestrado.hermeswidget.client.sensor.general.HermesWidgetObjects;
import br.ufg.inf.mestrado.hermeswidget.client.sensor.carbonDioxideTR.HWSensorCarbonDioxideTR;

public class App_HW_CarbonDioxideTR extends HermesWidgetObjects{

	public static void main(String[] args) {
		
		// Preparacao do pool de threads de acordo com a quantidade de dispositivos 
		// Air-Pure sendo lidos...
		ScheduledExecutorService poolWidgets = Executors.newScheduledThreadPool(1);

		//A leitura e modelagem dos dados adquiridos 
		HWSensorCarbonDioxideTR widget = new HWSensorCarbonDioxideTR(args);
		
		//A cada 4 segundos agenda uma leitura e modelagem de dado ambiental
		poolWidgets.schedule(widget, 4, TimeUnit.SECONDS);
		
		//Finaliza o pool do widget
		poolWidgets.shutdown();

		
	}
}
