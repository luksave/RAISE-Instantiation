package br.ufg.inf.mestrado.hermeswidget.testes;

import br.ufg.inf.mestrado.hermeswidget.client.sensor.carbonDioxide.App_HW_CarbonDioxide;
import br.ufg.inf.mestrado.hermeswidget.client.sensor.humidity.App_HW_Humidity;
import br.ufg.inf.mestrado.hermeswidget.client.sensor.vocs.App_HW_VolatileOrganicCompounds;
import br.ufg.inf.mestrado.hermeswidget.client.sensor.temperature.App_HW_Temperature;


public class InicializarHermesWidgets {
	
	public static void main(String[] args) {
		
		App_HW_CarbonDioxide.main(args);
		App_HW_Humidity.main(args);
		App_HW_VolatileOrganicCompounds.main(args);
		App_HW_Temperature.main(args);

	}

}