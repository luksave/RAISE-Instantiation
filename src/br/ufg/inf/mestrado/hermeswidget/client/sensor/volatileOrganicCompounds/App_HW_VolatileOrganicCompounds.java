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
		// TODO Implementar a aquisição e registro dos dados do sensor em um arquivo
		// Adicionar o arquivo com os dados do sensor na pasta

		// Pasta com os registros MIMIC utilizados pelo HW
		File diretorioAirPure = new File("./airPure/");

		// Preparação do pool de threads de acordo com a quantidade de arquivos que contém os dados ambientais
		ScheduledExecutorService poolWidgets = Executors.newScheduledThreadPool(diretorioAirPure.listFiles().length);

		for (File registroAtual : diretorioAirPure.listFiles()){	
			//Enviar os dados adquiridos 
			HWSensorVolatileOrganicCompounds widget = new HWSensorVolatileOrganicCompounds(registroAtual, args);	
			
			// A cada 5 segundos agenda uma leitura
			poolWidgets.schedule(widget, 5, TimeUnit.SECONDS);
			
		}
		
		while(true) {}
		
	}

}
