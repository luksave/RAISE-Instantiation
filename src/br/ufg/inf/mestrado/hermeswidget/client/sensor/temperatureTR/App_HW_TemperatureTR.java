package br.ufg.inf.mestrado.hermeswidget.client.sensor.temperatureTR;

import java.util.Timer;
import java.util.TimerTask;

import br.ufg.inf.mestrado.hermeswidget.client.sensor.general.HermesWidgetObjects;

/**
 * 
 * @author Lucas Felipe
 *
 */

public class App_HW_TemperatureTR extends HermesWidgetObjects{

	public static void main(String[] args) {
					
		int    delay = 6000;  // delay de 6 seg.
		int interval = 15000;  // intervalo de 15 seg.
		
		Timer  timer = new Timer();
	
		timer.scheduleAtFixedRate(new TimerTask() {
	        public void run() {
	    		HWSensorTemperatureTR widget = new HWSensorTemperatureTR(args);
	    		widget.run();
	    		
	        }
		
		}, delay, interval);
		
	}
	
}