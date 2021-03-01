package br.ufg.inf.mestrado.hermeswidget.client.sensor.humidityTR;

import java.util.Timer;
import java.util.TimerTask;

import br.ufg.inf.mestrado.hermeswidget.client.sensor.general.HermesWidgetObjects;

/**
 * 
 * @author Lucas Felipe
 *
 */

public class App_HW_HumidityTR extends HermesWidgetObjects{
	
	public static void main(String[] args) {
			
		int    delay = 4000;  // delay de 4 seg.
		int interval = 15000; // intervalo de 15 seg.
		
		Timer  timer = new Timer();

		timer.scheduleAtFixedRate(new TimerTask() {
	        public void run() {
	    		HWSensorHumidityTR widget = new HWSensorHumidityTR(args);
	    		widget.run();
	    		
	        }
		
		}, delay, interval);
		
	}
	
}
