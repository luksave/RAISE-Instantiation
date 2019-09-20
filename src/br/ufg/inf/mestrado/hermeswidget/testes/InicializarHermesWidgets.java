package br.ufg.inf.mestrado.hermeswidget.testes;

import br.ufg.inf.mestrado.hermeswidget.client.sensor.bloodPressure.App_HW_BloodPressure;
import br.ufg.inf.mestrado.hermeswidget.client.sensor.heartRate.App_HW_HeartRate;
import br.ufg.inf.mestrado.hermeswidget.client.sensor.oxygenSaturation.App_HW_OxygenSaturation;
import br.ufg.inf.mestrado.hermeswidget.client.sensor.respiratoryRate.App_HW_RespiratoryRate;
import br.ufg.inf.mestrado.hermeswidget.client.sensor.temperature.App_HW_Temperature;

public class InicializarHermesWidgets {
	
	public static void main(String[] args) {
		
		//System.out.println(new Date()+"\n\n");
		
		App_HW_BloodPressure.main(args);
		App_HW_HeartRate.main(args);
		App_HW_OxygenSaturation.main(args);
		App_HW_RespiratoryRate.main(args);
		App_HW_Temperature.main(args);
		
		//System.out.println("\n\n");
		
	}

}
