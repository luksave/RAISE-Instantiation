package br.ufg.inf.mestrado.hermeswidget.client.sensor.vocs;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import br.ufg.inf.mestrado.hermesbase.HermesBaseManager;
import br.ufg.inf.mestrado.hermeswidget.client.sensor.general.HermesWidgetSensorClient;
import br.ufg.inf.mestrado.hermeswidget.client.services.HWRepresentationServiceSensorIoTStream;
import br.ufg.inf.mestrado.hermeswidget.client.utils.HWLog;
import br.ufg.inf.mestrado.hermeswidget.client.utils.ReaderCSV;
import br.ufg.inf.mestrado.hermeswidget.manager.transferObject.HWTransferObject;
import br.ufg.inf.mestrado.hermeswidget.ontologies.Quantitykind;
import br.ufg.inf.mestrado.hermeswidget.ontologies.Unit;

/**
 * 
 * @author Lucas Felipe
 *
 */

public class HWSensorVolatileOrganicCompounds extends HermesWidgetSensorClient implements Runnable {

	private HermesBaseManager                      hermesBaseManager;
	private HWRepresentationServiceSensorIoTStream representationService;

	private ScheduledExecutorService threadPoolMedidas = null;
	
	private File registroMimic;

	private int tempoTotalMedida = 0;
	private int intervalos = 0;
	
	long tTotalRepresentation;
	
	// Construtor recebe o registro e um vetor com o tempo total[0] e de intervalos[1]
	// O parametro de tempo vem do input args.
	public HWSensorVolatileOrganicCompounds(File registroAtual, String tempo[]) {
		this.registroMimic = registroAtual;
		this.startConfigurationService("./settings/topics_vocs.json");
		this.hermesBaseManager = this.getCommunicationService();
		this.representationService = this.getRepresentationService();
		this.tempoTotalMedida = Integer.parseInt(tempo[0]);
		this.intervalos = Integer.parseInt(tempo[1]);
		
	}

	@Override
	public void run(){
		ReaderCSV reader = new ReaderCSV(this.registroMimic);
		
		List<String[]> listaComDadosAmbientais = reader.getLinhas().subList(4, tempoTotalMedida);
		
		int totalThreads = (listaComDadosAmbientais.size()) / intervalos;

		System.out.println("Total threads: " + totalThreads);
		
		// Prepara o pool de threads
		threadPoolMedidas = Executors.newScheduledThreadPool(totalThreads);

		int posicaoTVOC = 0;
		
		String[] cabecalho = reader.getLinhas().get(0);
		int contador = 0;
		
		for (String colunaCabecalho : cabecalho) {
			if (colunaCabecalho.equals("'TVOC'")) posicaoTVOC = contador;			
			contador++;
			
		}

		System.out.println("...TVOC = " + posicaoTVOC);

		if (posicaoTVOC != 0) {
			String log = "Hermes Widget Sensor Volatile Organic Compounds for environment ---> "
					+ this.registroMimic.getName() + " started! In: "
					+ new Date().toString();
			HWLog.recordLog(log);
			
			System.out.println(log + "\n");

			int posicaoExtensao = registroMimic.getName().lastIndexOf('.');

			String recordIdAtual = registroMimic.getName().substring(0, posicaoExtensao);

			System.out.println("Ambiente: "+recordIdAtual+" [TVOC]");

			// Laco para verificar os metadados de cada ambiente e as
			// informacoes de leitura dos dados ambientais
			int contadorTVOC    = 0;
			int contadorLinhas  = 0;
			int contadorThreads = 1;
			
			for (String[] medicaoAtual : listaComDadosAmbientais) {
				if (contadorLinhas % intervalos == 0) {
					float   segfloat = Float.valueOf(medicaoAtual[0]);
					int     segundos = Math.round(segfloat);
					int contadorTvoc = contadorTVOC++;
					
					String dataTempo = medicaoAtual[0].substring(0, 4)  + "-" + medicaoAtual[0].substring(4, 6) +"-"+ medicaoAtual[0].substring(6, 8)
				             +" " +medicaoAtual[0].substring(8, 10) + ":" + medicaoAtual[0].substring(10, 12) +":"+ medicaoAtual[0].substring(12, 14);
				
					
					// O DTO vai mudar de acordo com os dados de VOC que precisam ser passados
					HWTransferObject hermesWidgetTO = representationService.startRepresentationSensor(
							"tvoc.ttl", Integer.toString(segundos), 
							"TVOC", contadorTvoc, 
							"VolatileOrganicCompounds", // Nome do topico no arquivo topics_vocs
							medicaoAtual[posicaoTVOC], 
							null, recordIdAtual, dataTempo, Unit.PPB, Quantitykind.TVOC);
					
					
					SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date data = null;
					
					try { data = formato.parse(dataTempo);
					} catch (ParseException e) {e.printStackTrace();}
					
					//System.out.println(data+ "   ----   TVOCS: " +medicaoAtual[posicaoTVOC] + " PPB");
			
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
