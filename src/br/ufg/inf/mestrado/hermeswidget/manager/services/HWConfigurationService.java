package br.ufg.inf.mestrado.hermeswidget.manager.services;

import java.util.ArrayList;

import br.ufg.inf.mestrado.hermesbase.HermesBaseManager;
import br.ufg.inf.mestrado.hermeswidget.manager.configurator.HWManagerConfigurator;

/**
 * @author Ernesto
 * 
 *         Classe responsável pela configuração do HermesWidget, no que diz
 *         respeito a criação do tópicos para publicaçao do contexto obtido e
 *         representado.
 * 
 *         Realiza interface com o componente Hermes Base.
 */

public class HWConfigurationService {

	/**
	 * Instância do componente Hermes Base, responsável pela comunicação da
	 * infraestrutura Hermes.
	 */
	private HermesBaseManager hermesBaseManager = HWManagerServiceFactory.getCommunicationService().getHermesBaseManager();

	/**
	 * Realiza a criação dos tópicos especificados no arquivo topicos.json
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
			System.out.println("Tópico criado: " + nomeTopicoNotificacao);
			*/
		}
	}

	/*
	 * public void notifyContext(HermesWidgetTransferObject hermesWidgetTO) {
	 * 
	 * }
	 */
}
