package br.ufg.inf.mestrado.hermeswidget.client.services;

import br.ufg.inf.mestrado.hermeswidget.manager.services.HWManagerServiceFactory;

/**
 * 
 * @author Ernesto
 * 
 */

public class HWSensorServiceFactory extends HWManagerServiceFactory {

	private static HWRepresentationServiceSensor hermesWidgetRepresentation;

	/**
	 * @return
	 * 
	 * 
	 */

	public static HWRepresentationServiceSensor getRepresentationService() {

		hermesWidgetRepresentation = new HWRepresentationServiceSensor();

		return hermesWidgetRepresentation;
	}

}
