package br.ufg.inf.mestrado.hermeswidget.client.sensor.general;

import br.ufg.inf.mestrado.hermesbase.HermesBaseManager;
import br.ufg.inf.mestrado.hermeswidget.client.facade.HWSensorFacade;
import br.ufg.inf.mestrado.hermeswidget.client.services.HWRepresentationServiceSensor;
import br.ufg.inf.mestrado.hermeswidget.manager.client.HWManagerClient;
import br.ufg.inf.mestrado.hermeswidget.manager.services.HWNotificationService;
import br.ufg.inf.mestrado.hermeswidget.manager.transferObject.HWTransferObject;

/**
 * @author Ernesto
 * 
 */

public class HermesWidgetSensorClient extends HWManagerClient {
	
	/*
	private static HashMap<String, String> pacientes = new HashMap<String, String>();
	private static HashMap<String, String> propriedades = new HashMap<String, String>();
	
	public HashMap<String, String> getPacientes() {
		return pacientes;
	}

	public void setPacientes(HashMap<String, String> pacientes) {
		HermesWidgetSensorClient.pacientes = pacientes;
	}

	public HashMap<String, String> getPropriedades() {
		return propriedades;
	}

	public void setPropriedades(HashMap<String, String> propriedades) {
		HermesWidgetSensorClient.propriedades = propriedades;
	}
	*/

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
		//return null;
	}

	/**
	 * Método implementado da classe abstrata HermesWidgetManagerClient
	 */
	public HWNotificationService getNotificationService(HermesBaseManager hermesBase, HWTransferObject hermesWidgetTO) {
		return facade.getHermesWidgetNotificationService(hermesBase,
				hermesWidgetTO);
	}

	public HWRepresentationServiceSensor getRepresentationService() {
		return facade.getHermesWidgetRepresentationService();
	}

}
