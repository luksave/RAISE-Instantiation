package br.ufg.inf.mestrado.hermeswidget.client.sensor.volatileOrganicCompounds;

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

public class HWSensorVolatileOrganicCompounds extends HermesWidgetSensorClient implements Runnable {

	private HermesBaseManager             hermesBaseManager;
	private HWRepresentationServiceSensor representationService;

	private File registroAirPure;
	
	private ScheduledExecutorService threadPoolMedidas = null;

	private int tempoTotalMedida = 0;
	private int intervalos = 0;
	
	
	// Construtor recebe o registro e um vetor com o tempo total[0] e de intervalos[1]
	// O parametro de tempo vem do input args.
	public HWSensorVolatileOrganicCompounds(File registroAtual, String tempo[]) {
		this.registroAirPure = registroAtual;
		this.startConfigurationService("./settings/topics_vocs.json");
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

		int posicaoTVOC = 0;
		
		String[] cabecalho = reader.getLinhas().get(0);
		int contador = 0;
		
		for (String colunaCabecalho : cabecalho) {
			// A identificacao deste cabecalho vai mudar de acordo com o novo CSV
			// Mudou para 'TVOC'
			if (colunaCabecalho.equals("'TVOC'")) posicaoTVOC = contador;
			
			contador++;
			
		}

		// Sp02 vai mudar de acordo com o novo CSV: identificador para a coluna do VOC
		// Mudou para TVOC
		System.out.println("...TVOC = " + posicaoTVOC);

		if (posicaoTVOC != 0) {
			String log = "Hermes Widget Sensor Volatile Organic Compounds for environment ---> "
					+ this.registroAirPure.getName() //nome do ambiente
					+ " started! Date: "
					+ new Date().toString();
		
			HWLog.recordLog(log);
			
			System.out.println(log + "\n");

			int posicaoExtensao = registroAirPure.getName().lastIndexOf('.');

			String recordIdAtual = registroAirPure.getName().substring(0, posicaoExtensao);

			System.out.println("Ambiente: "+recordIdAtual+" [TVOC]");

			// Laco para verificar os metadados de cada ambiente e as
			// informacoes de leitura dos dados ambientais
			int contadorVolatileOrganicCompound = 0;
			int contadorLinhas = 0;
			int contadorThreads = 1;
			
			for (String[] medicaoAtual : listaComDadosAmbientais) {
				if (contadorLinhas % intervalos == 0) {
					float segfloat = Float.valueOf(medicaoAtual[0]);
					int   segundos = Math.round(segfloat);
					int contadorTVOC = contadorVolatileOrganicCompound++;
					
					// O DTO vai mudar de acordo com os dados de VOC que precisam ser passados
					HWTransferObject hermesWidgetTO = representationService.startRepresentationSensor(
							"tvoc.ttl", Integer.toString(segundos), 
							"TVOC", contadorTVOC, 
							"VolatileOrganicCompounds", // Nome do topico no arquivo topics_vocs
							medicaoAtual[posicaoTVOC], 
							null, "ppb", recordIdAtual);

					System.out.println("Counter: "+ contadorTVOC + "   ----   Measure: " +medicaoAtual[posicaoTVOC] + " ppb");
					
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
