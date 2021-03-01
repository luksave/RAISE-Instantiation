package br.ufg.inf.mestrado.hermeswidget.client.sensor.vocsTR;

import java.util.Timer;
import java.util.TimerTask;

import br.ufg.inf.mestrado.hermeswidget.client.sensor.general.HermesWidgetObjects;

/**
 * 
 * @author Lucas Felipe
 *
 */

public class App_HW_VolatileOrganicCompoundsTR extends HermesWidgetObjects{

	public static void main(String[] args) {
			
		int    delay = 8000;  // delay de 8 seg.
		int interval = 15000;  // intervalo de 15 seg.
		
		Timer  timer = new Timer();
	
		timer.scheduleAtFixedRate(new TimerTask() {
	        public void run() {
	    		HWSensorVolatileOrganicCompoundsTR widget = new HWSensorVolatileOrganicCompoundsTR(args);
	    		widget.run();
	    		
	        }
		
		}, delay, interval);
		
	}

}