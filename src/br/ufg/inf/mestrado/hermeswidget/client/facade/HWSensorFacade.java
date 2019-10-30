package br.ufg.inf.mestrado.hermeswidget.client.facade;

import br.ufg.inf.mestrado.hermeswidget.client.services.HWRepresentationServiceSensor;
import br.ufg.inf.mestrado.hermeswidget.client.services.HWSensorServiceFactory;
import br.ufg.inf.mestrado.hermeswidget.manager.facade.HWManagerFacade;

/**
 * 
 * @author Ernesto
 * 
 * Classe herda demais métodos da classe extendida
 *
 */

public class HWSensorFacade extends HWManagerFacade {

	public HWRepresentationServiceSensor getHermesWidgetRepresentationService() {
		return HWSensorServiceFactory.getRepresentationService();
		
	}
	
}