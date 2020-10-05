package br.ufg.inf.mestrado.hermeswidget.client.sensor.carbonDioxideTR;

import java.util.Timer;
import java.util.TimerTask;

import br.ufg.inf.mestrado.hermeswidget.client.sensor.general.HermesWidgetObjects;
import br.ufg.inf.mestrado.hermeswidget.client.sensor.carbonDioxideTR.HWSensorCarbonDioxideTR;

public class App_HW_CarbonDioxideTR extends HermesWidgetObjects{

	public static void main(String[] args) {
		
		int    delay = 1000;  // delay de 1 seg.
		int interval = 15000;  // intervalo de 15 seg.
		
		Timer  timer = new Timer();
	
		timer.scheduleAtFixedRate(new TimerTask() {
	        public void run() {
	    		HWSensorCarbonDioxideTR widget = new HWSensorCarbonDioxideTR(args);
	    		widget.run();
	    		
	        }
		
		}, delay, interval);
			
	}
	
}
