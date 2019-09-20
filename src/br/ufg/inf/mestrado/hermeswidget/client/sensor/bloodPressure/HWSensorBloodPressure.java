package br.ufg.inf.mestrado.hermeswidget.client.sensor.bloodPressure;

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

public class HWSensorBloodPressure extends HermesWidgetSensorClient implements
		Runnable {

	private HermesBaseManager hermesBaseManager;
	private HWRepresentationServiceSensor representationService;
	
	//private HashMap<String, String> objects;

	private ScheduledExecutorService threadPoolMedidas = null;

	private File registroMimic;

	private int tempoTotalMedida = 0;

	private int intervalos = 0;

	long tTotalRepresentation;

	public HWSensorBloodPressure(File registroAtual, String tempo[]) {
		this.registroMimic = registroAtual;
		this.startConfigurationService("./settings/topics_bloodPressure.json");
		this.hermesBaseManager = this.getCommunicationService();
		this.representationService = this.getRepresentationService();
		this.tempoTotalMedida = Integer.parseInt(tempo[0]);
		this.intervalos = Integer.parseInt(tempo[1]);
	}

	@Override
	public void run() {
		ReaderCSV reader = new ReaderCSV(this.registroMimic);

		// int totalLinhas = reader.getLinhas().size();
		List<String[]> listaComSinaisVitais = reader.getLinhas().subList(4,
				tempoTotalMedida);

		int totalThreads = (listaComSinaisVitais.size()) / this.intervalos;

		// Prepara o pool de threads
		// threadPoolMedidas =
		// Executors.newScheduledThreadPool(reader.getLinhas().size());
		threadPoolMedidas = Executors.newScheduledThreadPool(totalThreads);
		
		System.out.println("Total threads: "+totalThreads);

		int posicaoSistolica = 0;
		int posicaoDiastolica = 0;
		int posicaoMedia = 0;
		String[] cabecalho = reader.getLinhas().get(0);
		int contador = 0;
		for (String colunaCabecalho : cabecalho) {
			if (colunaCabecalho.equals("'ABPsys'")) {
				posicaoSistolica = contador;
			} else if (colunaCabecalho.equals("'ABPdias'")) {
				posicaoDiastolica = contador;
			} else if (colunaCabecalho.equals("'ABPmean'")) {
				posicaoMedia = contador;
			}
			contador++;
		}
		System.out.println("...ABPsys = " + posicaoSistolica);
		System.out.println("...ABPdias = " + posicaoDiastolica);
		System.out.println("...ABPmean = " + posicaoMedia);

		if (posicaoSistolica != 0) {

			String log = "Hermes Widget Sensor Blood Pressure for patient ---> "
					+ this.registroMimic.getName()
					+ " started! Date: "
					+ new Date().toString();
			HWLog.recordLog(log);

			System.out.println(log + "\n");

			int posicaoExtensao = registroMimic.getName().lastIndexOf('.');

			String recordIdAtual = registroMimic.getName().substring(0,	posicaoExtensao);
			
			System.out.println(recordIdAtual);

			// System.out.println("Início do monitoramento do paciente...");

			// Laço para verificar os metadados de cada paciente e as
			// informações de leitura dos sinais vitais

			int contadorPres = 0;
			int contadorLinhas = 0;
			int contadorThreads = 1;
			for (String[] medicaoAtual : listaComSinaisVitais) {
				if (contadorLinhas % this.intervalos == 0) {

					// int segundos = Integer.valueOf(medicaoAtual[0]);
					float segfloat = Float.valueOf(medicaoAtual[0]);
					int segundos = Math.round(segfloat);

					int contadorP = contadorPres++;
					
					//System.out.println(medicaoAtual[posicaoSistolica] +" - "+ contadorP);

					// Diastolica e Sistolica
					String medicaoComposta[] = {medicaoAtual[posicaoDiastolica].substring(0, medicaoAtual[posicaoDiastolica].lastIndexOf('.')), 
												medicaoAtual[posicaoSistolica].substring(0, medicaoAtual[posicaoSistolica].lastIndexOf('.'))};
					
					HWTransferObject hermesWidgetTO = representationService.startRepresentationSensor(
							"temperatura_corporea.ttl",
							Integer.toString(segundos), 
							"PresSang",
							contadorP, 
							"VSO_0000005",
							null, 
							medicaoComposta,
							"mmHg",
							recordIdAtual
					);
					
					/*
					// Distolica
					representationService.representMonitoringVitalSign(
							"pressao_sanguinea.ttl", "MonitoringBloodPressure",
							Integer.toString(segundos), "PresSang", contadorP,
							"VSO_0000005", "hasMonitoringBloodPressure",
							"isMeasurementBloodPressure",
							"valueDiastolicBloodPressure",
							medicaoAtual[posicaoDiastolica],
							"unitBloodPressure", "mmHg", recordIdAtual,
							medicaoAtual[posicaoMedia]);
					*/

					/*
					// Sistolica.
					int posicaoDecimal = medicaoAtual[posicaoSistolica]
							.lastIndexOf('.');
					String medidaInteira = medicaoAtual[posicaoSistolica]
							.substring(0, posicaoDecimal);
					representationService
							.getModeloMedicaoSinalVital()
							.add(representationService.getNovaMedicaoAferida(),
									ResourceFactory.createProperty(HWRepresentationServiceVitalSign
											.getDefaultNamespace()
											+ "valueSystolicBloodPressure"),
									ResourceFactory.createTypedLiteral(new Integer(
											Integer.parseInt(medidaInteira))));
					*/


					hermesWidgetTO.setThreadAtual(contadorThreads);
					hermesWidgetTO.setTotalThreads(totalThreads);

					threadPoolMedidas.schedule(this.getNotificationService(hermesBaseManager, hermesWidgetTO), segundos, TimeUnit.SECONDS);

					}

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
