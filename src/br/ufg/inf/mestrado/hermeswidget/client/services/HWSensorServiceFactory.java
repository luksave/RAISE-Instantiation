package br.ufg.inf.mestrado.hermeswidget.client.services;

import br.ufg.inf.mestrado.hermeswidget.manager.services.HWManagerServiceFactory;

/**
 * 
 * @author Ernesto
 * 
 */

public class HWSensorServiceFactory extends HWManagerServiceFactory {

	private static HWRepresentationServiceSensorIoTStream hermesWidgetRepresentation;

	/**
	 * @return
	 * 
	 * 
	 */
	public static HWRepresentationServiceSensorIoTStream getRepresentationService() {
		hermesWidgetRepresentation = new HWRepresentationServiceSensorIoTStream();
		return hermesWidgetRepresentation;
		
	}

}
