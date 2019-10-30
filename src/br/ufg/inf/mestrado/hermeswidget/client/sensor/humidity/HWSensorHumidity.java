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
	// O parametro de tempo vem do input args.
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

		int posicaoRelativeHumidity = 0;
		String[] cabecalho = reader.getLinhas().get(0);
		int contador = 0;
		
		for (String colunaCabecalho : cabecalho) {
			// A identificaÃ§Ã£o deste cabecalho vai mudar de acordo com o novo CSV
			// MUDOU PARA: 'RHum'
			if (colunaCabecalho.equals("'RHum'")) posicaoRelativeHumidity = contador;
			
			contador++;
			
		}
		
		System.out.println("...RHum = " + posicaoRelativeHumidity);

		if (posicaoRelativeHumidity != 0) {
			String log = "Hermes Widget Sensor Volatile Organic Compounds for environment ---> "
					+ this.registroAirPure.getName() //nome do ambiente
					+ " started! Date: "
					+ new Date().toString();
		
			HWLog.recordLog(log);
			
			System.out.println(log + "\n");

			int posicaoExtensao = registroAirPure.getName().lastIndexOf('.');

			String recordIdAtual = registroAirPure.getName().substring(0, posicaoExtensao);

			System.out.println("Ambiente: "+recordIdAtual);

			// Laco para verificar os metadados de cada ambiente e as
			// informacoes de leitura dos dados ambientais
			int contadorRelativeHumidity = 0;
			int contadorLinhas = 0;
			int contadorThreads = 1;
			
			for (String[] medicaoAtual : listaComDadosAmbientais) {
				if (contadorLinhas % intervalos == 0) {
					float segfloat = Float.valueOf(medicaoAtual[0]);
					int   segundos = Math.round(segfloat);
					int contadorRH = contadorRelativeHumidity++;
					
					// O DTO vai mudar de acordo com os dados de Umidade que precisam ser passados
					HWTransferObject hermesWidgetTO = representationService.startRepresentationSensor(
							"relative_humidity.ttl", Integer.toString(segundos), 
							"RelHum", contadorRH, 
							"RelativeHUmidity", // Nome do topico no arquivo topics_humidity
							medicaoAtual[posicaoRelativeHumidity], 
							null, "%", recordIdAtual);

					System.out.println("Contador: "+ contadorRH + "   ----   Medicao: " +medicaoAtual[posicaoRelativeHumidity] + "%");
					
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
