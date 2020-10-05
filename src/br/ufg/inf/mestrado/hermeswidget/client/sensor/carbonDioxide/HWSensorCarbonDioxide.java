package br.ufg.inf.mestrado.hermeswidget.client.sensor.carbonDioxide;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;

import br.ufg.inf.mestrado.hermesbase.HermesBaseManager;
import br.ufg.inf.mestrado.hermeswidget.client.sensor.general.HermesWidgetSensorClient;
import br.ufg.inf.mestrado.hermeswidget.client.services.HWRepresentationServiceSensorIoTStream;
import br.ufg.inf.mestrado.hermeswidget.client.utils.HWLog;
import br.ufg.inf.mestrado.hermeswidget.client.utils.ReaderCSV;
import br.ufg.inf.mestrado.hermeswidget.manager.transferObject.HWTransferObject;
import br.ufg.inf.mestrado.hermeswidget.ontologies.QuantityKind;
import br.ufg.inf.mestrado.hermeswidget.ontologies.Unit;

/**
 * 
 * @author Lucas Felipe
 *
 */

public class HWSensorCarbonDioxide extends HermesWidgetSensorClient implements Runnable {
	
	private HermesBaseManager                      hermesBaseManager;
	private HWRepresentationServiceSensorIoTStream representationService;
	
	private int tempoTotalMedida = 0;
	private int intervalos = 0;
	
	private File registroAirPure;
	
	private ScheduledExecutorService threadPoolMedidas = null;
	
	
	// Construtor recebe o registro e um vetor com o tempo total[0] e de intervalos[1]
	// O parametro de tempo vem do input args.
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
			if (colunaCabecalho.equals("'CO2'")) posicaoCarbonDioxide = contador;
			
			contador++;
			
		}
		
		System.out.println("...CO2 = " + posicaoCarbonDioxide);

		if (posicaoCarbonDioxide != 0) {
			String log = "Hermes Widget Sensor Carbon Dioxide for environment ---> "
					+ this.registroAirPure.getName()
					+ " started! Date: "
					+ new Date().toString();
		
			HWLog.recordLog(log);
			System.out.println(log + "\n");

			int  posicaoExtensao = registroAirPure.getName().lastIndexOf('.');
			String recordIdAtual = registroAirPure.getName().substring(0, posicaoExtensao);

			System.out.println("Ambiente: "+recordIdAtual+ " [Carbon Dioxide]");

	
			int contadorCarbonDioxide = 0;
			int contadorLinhas = 0;
			int contadorThreads = 1;
			
			String uriBase   = "http://www.inf.ufg.br/Air-Pure/";
			String sensorIRI = uriBase + "CarbonDioxideSensor-" + UUID.randomUUID().toString();
	
			// Laco para verificar os metadados de cada ambiente e as
			// informacoes de leitura dos dados ambientais
			for (String[] medicaoAtual : listaComDadosAmbientais) {
						
				if (contadorLinhas % intervalos == 0) {
					float segfloat    = Float.valueOf(medicaoAtual[0]);
					int   segundos    = Math.round(segfloat);
					int   contadorCO2 = contadorCarbonDioxide++;
					
					
					String dataTempo = medicaoAtual[0].substring(0, 4)  + "-" + medicaoAtual[0].substring(4, 6)   +"-"+ medicaoAtual[0].substring(6, 8)
				             +" " +medicaoAtual[0].substring(8, 10) + ":" + medicaoAtual[0].substring(10, 12) +":"+ medicaoAtual[0].substring(12, 14);
									
					
					// O DTO vai mudar de acordo com os dados de CO2 que precisam ser passados
					HWTransferObject hermesWidgetTO = null;
					try {
						hermesWidgetTO = representationService.startRepresentationSensor(
															sensorIRI, "co2_concentration.ttl",
															"ConCO2", contadorCO2, 
															"CarbonDioxide",
															medicaoAtual[posicaoCarbonDioxide], 
															null, recordIdAtual, dataTempo, Unit.PPM, QuantityKind.CO2Concentration);
					
					} catch (JSONException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
					
					
					SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
					Date  data = null;
					try { data = formato.parse(dataTempo);} catch (ParseException e) {e.printStackTrace();}
					
					
					System.out.println(data+ "   ----   CARBON DIOXIDE: " +medicaoAtual[posicaoCarbonDioxide] + " ppm");
					
			
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
