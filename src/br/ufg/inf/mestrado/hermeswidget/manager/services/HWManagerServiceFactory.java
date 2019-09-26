package br.ufg.inf.mestrado.hermeswidget.manager.services;

import br.ufg.inf.mestrado.hermesbase.HermesBaseManager;
import br.ufg.inf.mestrado.hermeswidget.manager.transferObject.HWTransferObject;

/**
 * @author Ernesto
 * 
 *         Fornece objetos singleton para os serviços do Hermes Widget
 *         (comunciação, persistência, representação)
 */

public class HWManagerServiceFactory {

	private static HWConfigurationService hermesWidgetConfiguration;
	private static HWCommunicationService hermesWidgetCommunication;
	private static HWNotificationService  hermesWidgetNotification;
	private static HWPersistenceService   hermesWidgetPersistence;
	
	

	/**
	 * @return
	 * 
	 *         Método que retorna uma instância do objeto de comunicação, o qual
	 *         possui uma instância de HermesBaseObject
	 */
	public static HWConfigurationService getConfigurationService() {
		if (hermesWidgetConfiguration == null) hermesWidgetConfiguration = new HWConfigurationService();
		
		return hermesWidgetConfiguration;
		
	}
	

	public static HWCommunicationService getCommunicationService() {
		if (hermesWidgetCommunication == null) hermesWidgetCommunication = new HWCommunicationService();
		
		return hermesWidgetCommunication;
		
	}
	

	public static HWNotificationService getNotificationService(HermesBaseManager hermesBase, HWTransferObject hermesWidgetTO) {
		hermesWidgetNotification = new HWNotificationService(hermesBase, hermesWidgetTO);
		
		return hermesWidgetNotification;
		
	}


	public static HWPersistenceService getPersistenceService() {
		if (hermesWidgetPersistence == null) hermesWidgetPersistence = new HWPersistenceService();
		
		return hermesWidgetPersistence;
		
	}

}
