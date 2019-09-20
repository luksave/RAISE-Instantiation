package br.ufg.inf.mestrado.hermeswidget.client.sensor.temperature;

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

public class HWSensorTemperature extends HermesWidgetSensorClient implements Runnable {

	// Inst�ncia do servi�o de comunica��o
	private HermesBaseManager hermesBaseManager;

	private HWRepresentationServiceSensor representationService;
	
	//private HashMap<String, String> objects;

	private ScheduledExecutorService threadPoolMedidas;

	private File registroMimic;
	
	private int tempoTotalMedida = 0;
	private int intervalos = 0;

	long tTotalRepresentation;

	public HWSensorTemperature(File registroAtual, String tempo[]) {
		this.registroMimic = registroAtual;
		this.startConfigurationService("./settings/topics_temperature.json");
		this.hermesBaseManager = this.getCommunicationService();
		this.representationService = this.getRepresentationService();
		this.tempoTotalMedida = Integer.parseInt(tempo[0]);
		this.intervalos = Integer.parseInt(tempo[1]);
	}

	@Override
	public void run() {

		ReaderCSV reader = new ReaderCSV(this.registroMimic);

		// int totalLinhas = reader.getLinhas().size();
		List<String[]> listaComSinaisVitais = reader.getLinhas().subList(4, tempoTotalMedida);
		int totalThreads = (listaComSinaisVitais.size()) / intervalos;
		
		System.out.println("Total threads: "+totalThreads);

		// Prepara o pool de threads
		threadPoolMedidas = Executors.newScheduledThreadPool(totalThreads);

		int posicaoSinalVital = 0;
		String[] cabecalho = reader.getLinhas().get(0);
		int contador = 0;
		for (String colunaCabecalho : cabecalho) 
		{
			if (colunaCabecalho.equals("'Tblood'")) {
				
				posicaoSinalVital = contador;
			}
			contador++;
		}

		if (posicaoSinalVital != 0) {
			
			/*
			 * Log
			 */
			String log = "Hermes Widget Sensor Temperature for patient ---> "
					+ this.registroMimic.getName() + " started! In: "
					+ new Date().toString();
			HWLog.recordLog(log);
			System.out.println(log + "\n");

			int posicaoExtensao = registroMimic.getName().lastIndexOf('.');

			String recordIdAtual = registroMimic.getName().substring(0,	posicaoExtensao);

			System.out.println(recordIdAtual);

			// La�o para verificar os metadados de cada paciente e as
			// informa��es de leitura dos sinais vitais
			int contadorTemp = 0;
			int contadorLinhas = 0;
			int contadorThreads = 1;
			for (String[] medicaoAtual : listaComSinaisVitais) 
			{
				if (contadorLinhas % intervalos == 0) 
				{
					
					// int segundos = Integer.valueOf(medicaoAtual[0]);
					float segfloat = Float.valueOf(medicaoAtual[0]);
					int segundos = Math.round(segfloat);

					int contadorT = contadorTemp++;
					
					//System.out.println(medicaoAtual[posicaoSinalVital] +" - "+ contadorT);

					
					HWTransferObject hermesWidgetTO = representationService.startRepresentationSensor(
							"temperatura_corporea.ttl",
							Integer.toString(segundos), 
							"Temp",
							contadorT, 
							"VSO_0000008",
							medicaoAtual[posicaoSinalVital], 
							null,
							"Celsius",
							recordIdAtual
					);
							
							/*
							representationService
							.representMonitoringVitalSign(
									"temperatura_corporea.ttl",
									"MonitoringTemperature",
									Integer.toString(segundos), "Temperatura",
									contadorT, "VSO_0000008",
									"hasMonitoringTemperature",
									"isMeasurementTemperature",
									"valueTemperature",
									medicaoAtual[posicaoSinalVital],
									"unitTemperature", "Celsius",
									recordIdAtual, null);
									*/
					
					hermesWidgetTO.setThreadAtual(contadorThreads);
					hermesWidgetTO.setTotalThreads(totalThreads);
						
					
					threadPoolMedidas.schedule(this.getNotificationService(hermesBaseManager, hermesWidgetTO), segundos, TimeUnit.SECONDS);
					
					
					// Limpa o modelo de representa��o para a pr�xima inst�ncia
					
					representationService.setModeloMedicaoSinalVital(null);

					contadorThreads++;
				}

				contadorLinhas++;
			}
		}

	}

}
