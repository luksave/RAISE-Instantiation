package br.ufg.inf.mestrado.hermeswidget.client.facade;

import br.ufg.inf.mestrado.hermeswidget.client.services.HWRepresentationServiceSensorIoTStream;
import br.ufg.inf.mestrado.hermeswidget.client.services.HWSensorServiceFactory;
import br.ufg.inf.mestrado.hermeswidget.manager.facade.HWManagerFacade;

/**
 * 
 * @author Ernesto
 * 
 * Classe herda demais m�todos da classe extendida
 *
 */

public class HWSensorFacade extends HWManagerFacade {

	public HWRepresentationServiceSensorIoTStream getHermesWidgetRepresentationService() {
		return HWSensorServiceFactory.getRepresentationService();
		
	}
	
}