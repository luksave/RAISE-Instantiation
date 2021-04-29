package br.ufg.inf.mestrado.hermeswidget.testes;

import java.time.LocalDateTime;

import br.ufg.inf.mestrado.hermeswidget.client.sensor.carbonDioxideTR.App_HW_CarbonDioxideTR;
import br.ufg.inf.mestrado.hermeswidget.client.sensor.humidityTR.App_HW_HumidityTR;
import br.ufg.inf.mestrado.hermeswidget.client.sensor.temperatureTR.App_HW_TemperatureTR;
import br.ufg.inf.mestrado.hermeswidget.client.sensor.vocsTR.App_HW_VolatileOrganicCompoundsTR;

public class InicializarHWairpure {

	//Essa informa��o de tempo � utilizada 
	
	public static LocalDateTime lastConcAvgTime = LocalDateTime.parse("2020-02-12T11:05:10");
	
	public static void main(String[] args) {
				
		
		App_HW_CarbonDioxideTR.main(args);
		//App_HW_HumidityTR.main(args);
		//App_HW_VolatileOrganicCompoundsTR.main(args);
		//App_HW_TemperatureTR.main(args);

	}

}
