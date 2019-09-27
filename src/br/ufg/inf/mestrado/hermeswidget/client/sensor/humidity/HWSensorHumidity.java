package br.ufg.inf.mestrado.hermeswidget.client.sensor.humidity;

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

public class HWSensorHumidity extends HermesWidgetSensorClient implements Runnable {

	private HermesBaseManager hermesBaseManager;

	private HWRepresentationServiceSensor representationService;

	private File registroAirPure;
	
	private ScheduledExecutorService threadPoolMedidas = null;

	private int tempoTotalMedida = 0;

	private int intervalos = 0;
	
	
	// Construtor recebe o registro e um vetor com o tempo total[0] e de intervalos[1]
	// O parâmetro de tempo vem do input args.
	public HWSensorHumidity(File registroAtual, String tempo[]) {
		this.registroAirPure = registroAtual;
		this.startConfigurationService("./settings/topics_humidity.json");
		this.hermesBaseManager = this.getCommunicationService();
		this.representationService = this.getRepresentationService();
		this.tempoTotalMedida = Integer.parseInt(tempo[0]);
		this.intervalos = Integer.parseInt(tempo[1]);
		
	}

	@Override
	public void run() {
		ReaderCSV reader = new ReaderCSV(this.registroAirPure);

		List<String[]> listaComDadosAmbientais = reader.getLinhas().subList(4, tempoTotalMedida);
		
		int totalThreads = (listaComDadosAmbientais.size()) / intervalos;

		System.out.println("Total threads: " + totalThreads);

		// Prepara o pool de threads
		threadPoolMedidas = Executors.newScheduledThreadPool(totalThreads);

		int posicaoDadoAmbiental = 0;
		String[] cabecalho = reader.getLinhas().get(0);
		int contador = 0;
		
		for (String colunaCabecalho : cabecalho) {
			// A identificação deste cabeçalho vai mudar de acordo com o novo CSV
			// MUDOU PARA: 'RHum'
			if (colunaCabecalho.equals("'RHum'")) posicaoDadoAmbiental = contador;
			
			contador++;
			
		}
		
		// Sp02 vai mudar de acordo com o novo CSV: identificador para a coluna do VOC
		// MUDOU PARA: RHum
		System.out.println("...RHum = " + posicaoDadoAmbiental);

		if (posicaoDadoAmbiental != 0) {

			String log = "Hermes Widget Sensor Volatile Organic Compounds for environment ---> "
					+ this.registroAirPure.getName() //nome do ambiente
					+ " started! Date: "
					+ new Date().toString();
		
			HWLog.recordLog(log);
			
			System.out.println(log + "\n");

			int posicaoExtensao = registroAirPure.getName().lastIndexOf('.');

			String recordIdAtual = registroAirPure.getName().substring(0, posicaoExtensao);

			System.out.println("Ambiente: "+recordIdAtual);

			// Laço para verificar os metadados de cada ambiente e as
			// informações de leitura dos dados ambientais
			int contadorHumidity = 0;
			int contadorLinhas = 0;
			int contadorThreads = 1;
			
			for (String[] medicaoAtual : listaComDadosAmbientais) {
				if (contadorLinhas % intervalos == 0) {
					float segfloat = Float.valueOf(medicaoAtual[0]);
					int   segundos = Math.round(segfloat);
					int contadorRH = contadorHumidity++;
					
					// O DTO vai mudar de acordo com os dados de VOC que precisam ser passados
					HWTransferObject hermesWidgetTO = representationService.startRepresentationSensor(
							"relative_humidity.ttl", Integer.toString(segundos), 
							"RelHum", contadorRH, 
							"RelativeHUmidity", // Nome do tópico no arquivo topics_humidity
							medicaoAtual[posicaoDadoAmbiental].substring(0, medicaoAtual[posicaoDadoAmbiental].lastIndexOf('.')), 
							null, "%", recordIdAtual);

					hermesWidgetTO.setThreadAtual(contadorThreads);
					hermesWidgetTO.setTotalThreads(totalThreads);

					threadPoolMedidas.schedule(this.getNotificationService(hermesBaseManager, hermesWidgetTO), segundos, TimeUnit.SECONDS);

					representationService.setModeloMedicaoDadoAmbiental(null);
					
					contadorThreads++;
				
				}
				
				contadorLinhas++;
				
			}
			
		}
		
	}

}
