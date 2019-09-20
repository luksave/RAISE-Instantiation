package br.ufg.inf.mestrado.hermeswidget.client.sensor.oxygenSaturation;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import br.ufg.inf.mestrado.hermesbase.HermesBaseManager;
import br.ufg.inf.mestrado.hermeswidget.client.sensor.general.HermesWidgetSensorClient;
import br.ufg.inf.mestrado.hermeswidget.client.services.HWRepresentationServiceSensor;
import br.ufg.inf.mestrado.hermeswidget.client.utils.HWLog;
import br.ufg.inf.mestrado.hermeswidget.client.utils.ReaderCSV;
import br.ufg.inf.mestrado.hermeswidget.manager.transferObject.HWTransferObject;

/**
 * 
 * @author Ernesto
 * 
 */

public class HWSensorOxygenSaturation extends HermesWidgetSensorClient
		implements Runnable {

	private HermesBaseManager hermesBaseManager;
	private HWRepresentationServiceSensor representationService;
	
	//private HashMap<String, String> objects;

	private ScheduledExecutorService threadPoolMedidas = null;

	private File registroMimic;
	private int tempoTotalMedida = 0;

	private int intervalos = 0;

	public HWSensorOxygenSaturation(File registroAtual, String tempo[]) {
		this.registroMimic = registroAtual;
		this.startConfigurationService("./settings/topics_oxygenSaturation.json");
		this.hermesBaseManager = this.getCommunicationService();
		this.representationService = this.getRepresentationService();
		this.tempoTotalMedida = Integer.parseInt(tempo[0]);
		this.intervalos = Integer.parseInt(tempo[1]);
		//this.objects = objects;
	}

	@Override
	public void run() {

		ReaderCSV reader = new ReaderCSV(this.registroMimic);

		// int totalLinhas = reader.getLinhas().size();
		List<String[]> listaComSinaisVitais = reader.getLinhas().subList(4,
				tempoTotalMedida);
		int totalThreads = (listaComSinaisVitais.size()) / intervalos;

		System.out.println("Total threads: " + totalThreads);

		// Prepara o pool de threads
		threadPoolMedidas = Executors.newScheduledThreadPool(totalThreads);

		int posicaoSinalVital = 0;
		String[] cabecalho = reader.getLinhas().get(0);
		int contador = 0;
		for (String colunaCabecalho : cabecalho) {
			if (colunaCabecalho.equals("'SpO2'")) {
				posicaoSinalVital = contador;
			}
			contador++;
		}
		System.out.println("...SpO2 = " + posicaoSinalVital);

		if (posicaoSinalVital != 0) {

			String log = "Hermes Widget Sensor Oxigen Saturation for patient ---> "
					+ this.registroMimic.getName()
					+ " started! Date: "
					+ new Date().toString();
			HWLog.recordLog(log);
			System.out.println(log + "\n");

			int posicaoExtensao = registroMimic.getName().lastIndexOf('.');

			String recordIdAtual = registroMimic.getName().substring(0,
					posicaoExtensao);

			System.out.println("Paciente: "+recordIdAtual);

			// Laço para verificar os metadados de cada paciente e as
			// informações de leitura dos sinais vitais
			int contadorOxygenSaturation = 0;
			int contadorLinhas = 0;
			int contadorThreads = 1;
			for (String[] medicaoAtual : listaComSinaisVitais) {
				// && Float.parseFloat(medicaoAtual[posicaoSinalVital]) > 0
				if (contadorLinhas % intervalos == 0) {
					float segfloat = Float.valueOf(medicaoAtual[0]);
					int segundos = Math.round(segfloat);

					int contadorOS = contadorOxygenSaturation++;

					//System.out.println(medicaoAtual[posicaoSinalVital] + " - " + contadorOS);

					
					HWTransferObject hermesWidgetTO = representationService.startRepresentationSensor(
							"saturacao_oxigenio.ttl",
							Integer.toString(segundos), 
							"SatOxig",
							contadorOS, 
							"OxygenSaturationMeasurementDatum",
							medicaoAtual[posicaoSinalVital].substring(0, medicaoAtual[posicaoSinalVital].lastIndexOf('.')), 
							null,
							"%",
							recordIdAtual
					);

					hermesWidgetTO.setThreadAtual(contadorThreads);
					hermesWidgetTO.setTotalThreads(totalThreads);

					threadPoolMedidas.schedule(this.getNotificationService(
							hermesBaseManager, hermesWidgetTO), segundos,
							TimeUnit.SECONDS);

					// if (contadorLinhas==0)
					// representationService.modeloMedicaoSinalVital.write(System.out,
					// "TURTLE");
					//
					representationService.setModeloMedicaoSinalVital(null);
					
					contadorThreads++;
				}
				contadorLinhas++;
			}
		}

	}

}
