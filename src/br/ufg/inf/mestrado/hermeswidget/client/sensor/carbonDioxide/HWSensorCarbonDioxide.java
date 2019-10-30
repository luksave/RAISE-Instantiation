package br.ufg.inf.mestrado.hermeswidget.client.sensor.carbonDioxide;

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

public class HWSensorCarbonDioxide extends HermesWidgetSensorClient implements Runnable {
	
	private HermesBaseManager hermesBaseManager;
	
	private HWRepresentationServiceSensor representationService;
	
	private File registroAirPure;
	
	private ScheduledExecutorService threadPoolMedidas = null;
	
	private int tempoTotalMedida = 0;
	
	private int intervalos = 0;
	
	// Construtor recebe o registro e um vetor com o tempo total[0] e de intervalos[1]
	// O par√¢metro de tempo vem do input args.
	public HWSensorCarbonDioxide(File registroAtual, String tempo[]) {
		this.registroAirPure = registroAtual;
		this.startConfigurationService("./settings/topics_carbonDioxide.json");
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

		System.out.println("Total threads: " + totalThreads + ". Intervalos: " +intervalos);

		// Prepara o pool de threads
		threadPoolMedidas = Executors.newScheduledThreadPool(totalThreads);

		int posicaoCarbonDioxide = 0;
		
		String[] cabecalho = reader.getLinhas().get(0);
		int contador = 0;
		
		for (String colunaCabecalho : cabecalho) {
			// A identifica√ß√£o deste cabe√ßalho vai mudar de acordo com o novo CSV
			// MUDOU PARA: 'CO2'
			if (colunaCabecalho.equals("'CO2'")) posicaoCarbonDioxide = contador;
			
			contador++;
			
		}
		
		System.out.println("...CO2 = " + posicaoCarbonDioxide);

		if (posicaoCarbonDioxide != 0) {
			String log = "Hermes Widget Sensor Carbon Dioxide for environment ---> "
					+ this.registroAirPure.getName() //nome do ambiente
					+ " started! Date: "
					+ new Date().toString();
		
			HWLog.recordLog(log);
			
			System.out.println(log + "\n");

			int posicaoExtensao = registroAirPure.getName().lastIndexOf('.');

			String recordIdAtual = registroAirPure.getName().substring(0, posicaoExtensao);

			System.out.println("Ambiente: "+recordIdAtual);

			// La√ßo para verificar os metadados de cada ambiente e as
			// informa√ß√µes de leitura dos dados ambientais
			int contadorCarbonDioxide = 0;
			int contadorLinhas = 0;
			int contadorThreads = 1;
			
			//O PROGRAMA N√O EST¡ SAINDO DESSE LA«O!
			for (String[] medicaoAtual : listaComDadosAmbientais) {
				if (contadorLinhas % intervalos == 0) {
					float segfloat    = Float.valueOf(medicaoAtual[0]);
					int   segundos    = Math.round(segfloat);
					int   contadorCO2 = contadorCarbonDioxide++;
					
					// O DTO vai mudar de acordo com os dados de CO2 que precisam ser passados
					HWTransferObject hermesWidgetTO = representationService.startRepresentationSensor(
														"co2_concentration.ttl", Integer.toString(segundos), "ConCO2", 
														contadorCO2, "CarbonDioxideConcentration",
														medicaoAtual[posicaoCarbonDioxide], 
														null, "ppm", recordIdAtual);
				
					System.out.println("Contador: "+ contadorCO2 + "   ----   MediÁ„o: " +medicaoAtual[posicaoCarbonDioxide] + " ppm");

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
