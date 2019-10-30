package br.ufg.inf.mestrado.hermeswidget.manager.configurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * 
 * @author Ernesto
 * 
 *         Classe que acessa o arquivo de configuração json para verificar os
 *         tópicos onde o Hermes Widget deverá publicar.
 * 
 */

public class HWManagerConfigurator {

	private static JSONObject jsonConfig = null;

	private static void setJsonConfiguration(String configuration) {

		File arquivo = new File(configuration);
		
		try {
			FileInputStream finput = new FileInputStream(arquivo);
			JSONTokener jsonTokener = new JSONTokener(finput);
			jsonConfig = new JSONObject(jsonTokener);
		
		} catch (FileNotFoundException foex) {}
		
	}

	public static ArrayList<String> getNotificationTopicsForRegistry(String configuration) {
		ArrayList<String> topicosParaNotificacao = new ArrayList<String>();
		setJsonConfiguration(configuration);
		JSONArray topicos = jsonConfig.getJSONArray("topicos");
		
		for (int indiceTopico = 0; indiceTopico < topicos.length(); indiceTopico++) {
			String defaultnamespace   = topicos.getJSONObject(indiceTopico).getString("defaultnamespace");
			String nomeTopico         = topicos.getJSONObject(indiceTopico).getString("registrar");
			String nomeCompletoTopico = defaultnamespace + nomeTopico;

			topicosParaNotificacao.add(nomeCompletoTopico);

		}
		
		return topicosParaNotificacao;
	
	}

}