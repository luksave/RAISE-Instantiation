package br.ufg.inf.mestrado.hermeswidget.manager.facade;

import br.ufg.inf.mestrado.hermesbase.HermesBaseManager;
import br.ufg.inf.mestrado.hermeswidget.manager.services.HWCommunicationService;
import br.ufg.inf.mestrado.hermeswidget.manager.services.HWConfigurationService;
import br.ufg.inf.mestrado.hermeswidget.manager.services.HWManagerServiceFactory;
import br.ufg.inf.mestrado.hermeswidget.manager.services.HWNotificationService;
import br.ufg.inf.mestrado.hermeswidget.manager.transferObject.HWTransferObject;

/**
 * @author Ernesto
 * 
 *         Classe que realiza a inicializa��o de um HermesWidget, juntamente com
 *         o servi�o de comunica��o do mesmo com a infraestrutura.
 * 
 *         Session Facade: Simplificar a interface do cliente e controlar o
 *         acesso e a comunica��o. Session Facade representa uma fun��o ou
 *         v�rias fun��es exercidas por um sistema.
 * 
 *         Remove dos clientes o acesso direto �s camadas de neg�cio do
 *         componente por meio de uma interface com m�todos para registro,
 *         publica��o e assinatura de t�picos.
 */

public class HWManagerFacade {

	public HWManagerFacade() {}

	/**
	 * Inicializa o servi�o de configura��o, obtendo ou criando uma inst�ncia de
	 * HermesWidgetConfigurationService
	 */
	public void startHermesWidgetConfigurationService(String configuration) {
		HWConfigurationService configurationService = HWManagerServiceFactory.getConfigurationService();

		/**
		 * Invoca��o dos m�todos do servi�o de comunica��o para cria��o dos
		 * t�picos, registro de publicadores e assinatura de t�picos
		 */
		configurationService.createTopics(configuration);

	}
	
	public HWCommunicationService startHermesWidgetCommunicationService() {
		return HWManagerServiceFactory.getCommunicationService();
		
	}

	public HWNotificationService getHermesWidgetNotificationService(HermesBaseManager hermesBase, HWTransferObject hermesWidgetTO) {
		return HWManagerServiceFactory.getNotificationService(hermesBase, hermesWidgetTO);
		
	}

}