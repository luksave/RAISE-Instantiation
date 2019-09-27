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

/**
 * 
 * @author Lucas Felipe
 *
 */

public class HWSensorTemperature extends HermesWidgetSensorClient implements Runnable {

	private HermesBaseManager hermesBaseManager;

	private HWRepresentationServiceSensor representationService;

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

		List<String[]> listaComDadosAmbientais = reader.getLinhas().subList(4, tempoTotalMedida);
		
		int totalThreads = (listaComDadosAmbientais.size()) / intervalos;
		
		System.out.println("Total threads: "+totalThreads);

		// Prepara o pool de threads
		threadPoolMedidas = Executors.newScheduledThreadPool(totalThreads);

		int posicaoSinalVital = 0;
		String[] cabecalho = reader.getLinhas().get(0);
		int contador = 0;
		
		for (String colunaCabecalho : cabecalho) {
			// A identificação deste cabeçalho vai mudar de acordo com o novo CSV
			// MUDOU PARA: 'Temp'
			if (colunaCabecalho.equals("'Temp'")) posicaoSinalVital = contador;
			
			contador++;
			
		}

		if (posicaoSinalVital != 0) {
			String log = "Hermes Widget Sensor Temperature for ambient ---> "
					+ this.registroMimic.getName() + " started! In: "
					+ new Date().toString();
			HWLog.recordLog(log);
			
			System.out.println(log + "\n");

			int posicaoExtensao = registroMimic.getName().lastIndexOf('.');

			String recordIdAtual = registroMimic.getName().substring(0,	posicaoExtensao);

			System.out.println("Ambiente:" +recordIdAtual);

			// Laço para verificar os metadados de cada paciente e as
			// informações de leitura dos sinais vitais
			int contadorTemp = 0;
			int contadorLinhas = 0;
			int contadorThreads = 1;
			for (String[] medicaoAtual : listaComDadosAmbientais) {
				if (contadorLinhas % intervalos == 0) {
					float segfloat = Float.valueOf(medicaoAtual[0]);
					int segundos = Math.round(segfloat);
					int contadorT = contadorTemp++;
					
					HWTransferObject hermesWidgetTO = representationService.startRepresentationSensor(
							"temperatura_ambiental.ttl", Integer.toString(segundos), 
							"Temp", contadorT, 
							"Temperature", // Nome do tópico no arquivo topics_temperature 
							medicaoAtual[posicaoSinalVital], 
							null, "Celsius", recordIdAtual
					);
					
					hermesWidgetTO.setThreadAtual(contadorThreads);
					hermesWidgetTO.setTotalThreads(totalThreads);
						
					
					threadPoolMedidas.schedule(this.getNotificationService(hermesBaseManager, hermesWidgetTO), segundos, TimeUnit.SECONDS);
					
					// Limpa o modelo de representação para a próxima instância					
					representationService.setModeloMedicaoDadoAmbiental(null);

					contadorThreads++;
				}

				contadorLinhas++;
				
			}
			
		}

	}

}
