package br.ufg.inf.mestrado.hermeswidget.manager.client;

import br.ufg.inf.mestrado.hermesbase.HermesBaseManager;
import br.ufg.inf.mestrado.hermeswidget.manager.services.HWNotificationService;
import br.ufg.inf.mestrado.hermeswidget.manager.transferObject.HWTransferObject;

/**
 * @author Ernesto
 * 
 *         Classe que invoca a inicializa��o de um Hermes Widget.
 * 
 */

public abstract class HWManagerClient {

	/**
	 * @param configuration
	 * 
	 *            M�todo abstrato cuja implementa��o inicia o Servi�o de
	 *            Configura��o do Hermes Widget.
	 * 
	 *            O servi�o de configura��o utiliza o componente Hermes Base
	 *            para criar os t�picos que receber�o as publica��es de cada
	 *            Hermes Widget, ou seja, os t�picos de notifica��o.
	 * 
	 */
	public abstract void startConfigurationService(String configuration);

	/**
	 * @return
	 * 
	 *         Retorna uma inst�ncia de HermesBaseManager, que realiza o servi�o
	 *         de comunica��o do Hermes.
	 */
	public abstract HermesBaseManager getCommunicationService();

	
	public abstract HWNotificationService getNotificationService(HermesBaseManager hermesBase, HWTransferObject hermesWidgetTO);

}