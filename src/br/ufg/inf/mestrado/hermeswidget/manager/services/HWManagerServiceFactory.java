package br.ufg.inf.mestrado.hermeswidget.manager.services;

import br.ufg.inf.mestrado.hermesbase.HermesBaseManager;
import br.ufg.inf.mestrado.hermeswidget.manager.transferObject.HWTransferObject;

/**
 * @author Ernesto
 * 
 *         Fornece objetos singleton para os servi�os do Hermes Widget
 *         (comuncia��o, persist�ncia, representa��o)
 */

public class HWManagerServiceFactory {

	private static HWConfigurationService hermesWidgetConfiguration;
	private static HWCommunicationService hermesWidgetCommunication;
	private static HWNotificationService hermesWidgetNotification;
	private static HWPersistenceService hermesWidgetPersistence;
	
	

	/**
	 * @return
	 * 
	 *         M�todo que retorna uma inst�ncia do objeto de comunica��o, o qual
	 *         possui uma inst�ncia de HermesBaseObject
	 */
	public static HWConfigurationService getConfigurationService() {
		if (hermesWidgetConfiguration == null) {
			hermesWidgetConfiguration = new HWConfigurationService();
		}
		return hermesWidgetConfiguration;
	}
	
	/**
	 * @return
	 * 
	 *         ?
	 */
	public static HWCommunicationService getCommunicationService() {
		if (hermesWidgetCommunication == null) {
			hermesWidgetCommunication = new HWCommunicationService();
		}
		return hermesWidgetCommunication;
	}
	
	/**
	 * @return
	 * 
	 *         ?
	 */
	public static HWNotificationService getNotificationService(HermesBaseManager hermesBase, HWTransferObject hermesWidgetTO) {
		hermesWidgetNotification = new HWNotificationService(hermesBase, hermesWidgetTO);
		return hermesWidgetNotification;
	}

	/**
	 * @return
	 * 
	 *         ?
	 */
	public static HWPersistenceService getPersistenceService() {
		if (hermesWidgetPersistence == null) {
			hermesWidgetPersistence = new HWPersistenceService();
		}
		return hermesWidgetPersistence;
	}

}
