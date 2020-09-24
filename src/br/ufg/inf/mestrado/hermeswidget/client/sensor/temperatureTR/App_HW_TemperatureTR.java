package br.ufg.inf.mestrado.hermeswidget.client.sensor.temperatureTR;

import java.util.Timer;
import java.util.TimerTask;

import br.ufg.inf.mestrado.hermeswidget.client.sensor.general.HermesWidgetObjects;
import br.ufg.inf.mestrado.hermeswidget.client.sensor.temperatureTR.HWSensorTemperatureTR;

/**
 * 
 * @author Lucas Felipe
 *
 */

public class App_HW_TemperatureTR extends HermesWidgetObjects{

	public static void main(String[] args) {
					
		int    delay = 3000;  // delay de 1 seg.
		int interval = 15000;  // intervalo de 5 seg.
		
		Timer  timer = new Timer();
	
		timer.scheduleAtFixedRate(new TimerTask() {
	        public void run() {
	    		HWSensorTemperatureTR widget = new HWSensorTemperatureTR(args);
	    		widget.run();
	    		
	        }
		
		}, delay, interval);
		
	}
	
}