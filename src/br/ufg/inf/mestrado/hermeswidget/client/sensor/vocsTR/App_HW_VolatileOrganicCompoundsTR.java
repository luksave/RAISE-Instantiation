package br.ufg.inf.mestrado.hermeswidget.client.sensor.vocsTR;

import java.util.Timer;
import java.util.TimerTask;
import br.ufg.inf.mestrado.hermeswidget.client.sensor.vocsTR.HWSensorVolatileOrganicCompoundsTR;

/**
 * 
 * @author Lucas Felipe
 *
 */

public class App_HW_VolatileOrganicCompoundsTR {

	public static void main(String[] args) {
			
		int    delay = 4000;  // delay de 1 seg.
		int interval = 15000;  // intervalo de 5 seg.
		
		Timer  timer = new Timer();
	
		timer.scheduleAtFixedRate(new TimerTask() {
	        public void run() {
	    		HWSensorVolatileOrganicCompoundsTR widget = new HWSensorVolatileOrganicCompoundsTR(args);
	    		widget.run();
	    		
	        }
		
		}, delay, interval);
		
	}

}