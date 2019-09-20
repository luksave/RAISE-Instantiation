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
 *         Classe que realiza a inicializaçãoo de um HermesWidget, juntamente com
 *         o serviço de comunicação do mesmo com a infraestrutura.
 * 
 *         Session Facade: Simplificar a interface do cliente e controlar o
 *         acesso e a comunicação. Session Facade representa uma função ou
 *         várias funções exercidas por um sistema.
 * 
 *         Remove dos clientes o acesso direto às camadas de negócio do
 *         componente por meio de uma interface com métodos para registro,
 *         publicação e assinatura de tópicos.
 */

public class HWManagerFacade {

	public HWManagerFacade() {
	}

	/**
	 * Inicializa o servi�o de configura��o, obtendo ou criando uma inst�ncia de
	 * HermesWidgetConfigurationService
	 */
	public void startHermesWidgetConfigurationService(String configuration) {
		HWConfigurationService configurationService = HWManagerServiceFactory.getConfigurationService();

		/**
		 * Invoca��o dos m�todos do servi�o de comunica��o para cria��o dos
		 * t�picos, registro de publicadores e assinatura de t�pcos
		 */
		configurationService.createTopics(configuration);

		/*
		 * comunicationService.registerPublishers();
		 * comunicationService.subscribeTopics();
		 */
	}
	
	public HWCommunicationService startHermesWidgetCommunicationService() {
		return HWManagerServiceFactory.getCommunicationService();
	}

	public HWNotificationService getHermesWidgetNotificationService(HermesBaseManager hermesBase, HWTransferObject hermesWidgetTO) {
		return HWManagerServiceFactory.getNotificationService(hermesBase, hermesWidgetTO);
	}

}
