package br.ufg.inf.mestrado.hermeswidget.manager.client;

import br.ufg.inf.mestrado.hermesbase.HermesBaseManager;
import br.ufg.inf.mestrado.hermeswidget.manager.services.HWNotificationService;
import br.ufg.inf.mestrado.hermeswidget.manager.transferObject.HWTransferObject;

/**
 * @author Ernesto
 * 
 *         Classe que invoca a inicialização de um Hermes Widget.
 * 
 */

public abstract class HWManagerClient {

	/**
	 * @param configuration
	 * 
	 *            Método abstrato cuja implementação inicia o Serviço de
	 *            Configuração do Hermes Widget.
	 * 
	 *            O serviço de configuração utiliza o componente Hermes Base
	 *            para criar os tópicos que receberão as publicações de cada
	 *            Hermes Widget, ou seja, os tópicos de notificação.
	 * 
	 */
	public abstract void startConfigurationService(String configuration);

	/**
	 * @return
	 * 
	 *         Retorna uma instância de HermesBaseManager, que realiza o serviço
	 *         de comunicação do Hermes.
	 */
	public abstract HermesBaseManager getCommunicationService();

	
	public abstract HWNotificationService getNotificationService(HermesBaseManager hermesBase, HWTransferObject hermesWidgetTO);

}