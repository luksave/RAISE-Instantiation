package br.ufg.inf.mestrado.hermeswidget.client.sensor.general;

import br.ufg.inf.mestrado.hermesbase.HermesBaseManager;
import br.ufg.inf.mestrado.hermeswidget.client.facade.HWSensorFacade;
import br.ufg.inf.mestrado.hermeswidget.client.services.HWRepresentationServiceSensorIoTStream;
import br.ufg.inf.mestrado.hermeswidget.manager.client.HWManagerClient;
import br.ufg.inf.mestrado.hermeswidget.manager.services.HWNotificationService;
import br.ufg.inf.mestrado.hermeswidget.manager.transferObject.HWTransferObject;

/**
 * @author Ernesto
 * 
 */

public class HermesWidgetSensorClient extends HWManagerClient {

	/**
	 * Instância de façade (HermesWidgetFacade) utilizada para inicializar os
	 * serviços de um Hermes Widget.
	 */
	private static HWSensorFacade facade = new HWSensorFacade();

	/**
	 * Método implementado da classe abstrata HermesWidgetManagerClient
	 */
	public void startConfigurationService(String configuration) {
		facade.startHermesWidgetConfigurationService(configuration);
		
	}

	/**
	 * Método implementado da classe abstrata HermesWidgetManagerClient
	 */
	public HermesBaseManager getCommunicationService() {
		return facade.startHermesWidgetCommunicationService().getHermesBaseManager();
		
	}

	/**
	 * Método implementado da classe abstrata HermesWidgetManagerClient
	 */
	public HWNotificationService getNotificationService(HermesBaseManager hermesBase, HWTransferObject hermesWidgetTO) {
		return facade.getHermesWidgetNotificationService(hermesBase, hermesWidgetTO);
		
	}

	public HWRepresentationServiceSensorIoTStream getRepresentationService() {
		return facade.getHermesWidgetRepresentationService();
		
	}

}