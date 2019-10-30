package br.ufg.inf.mestrado.hermeswidget.manager.services;

import java.util.ArrayList;

//import br.ufg.inf.mestrado.hermesbase.HermesBaseManager;
import br.ufg.inf.mestrado.hermeswidget.manager.configurator.HWManagerConfigurator;

/**
 * @author Ernesto
 * 
 *         Classe respons�vel pela configura��o do HermesWidget, no que diz
 *         respeito a cria��o do t�picos para publica�ao do contexto obtido e
 *         representado.
 * 
 *         Realiza interface com o componente Hermes Base.
 */

public class HWConfigurationService {

	/**
	 * Inst�ncia do componente Hermes Base, respons�vel pela comunica��o da
	 * infraestrutura Hermes.
	 */
	//private HermesBaseManager hermesBaseManager = HWManagerServiceFactory.getCommunicationService().getHermesBaseManager();

	/**
	 * Realiza a cria��o dos t�picos especificados no arquivo topicos.json
	 */
	public void createTopics(String configuration) {

		ArrayList<String> topicosParaNotificacao = HWManagerConfigurator
				.getNotificationTopicsForRegistry(configuration);
		for (int i = 0; i < topicosParaNotificacao.size(); i++) {
			String nomeTopicoNotificacao = topicosParaNotificacao.get(i);
			// --> MEXI AQUI
			//hermesBaseManager.createNotificationTopic(nomeTopicoNotificacao);
			// --> MEXI AQUI
			/*
			System.out.println("Created Topic: " + nomeTopicoNotificacao);
			*/
			System.out.println("Created Topic: " + nomeTopicoNotificacao);
		}
	}

	/*
	 * public void notifyContext(HermesWidgetTransferObject hermesWidgetTO) {
	 * 
	 * }
	 */
}
