package br.ufg.inf.mestrado.hermeswidget.client.sensor.volatileOrganicCompounds;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import br.ufg.inf.mestrado.hermesbase.HermesBaseManager;
import br.ufg.inf.mestrado.hermeswidget.client.sensor.general.HermesWidgetSensorClient;
import br.ufg.inf.mestrado.hermeswidget.client.services.HWRepresentationServiceSensorIoTStream;
import br.ufg.inf.mestrado.hermeswidget.client.utils.HWLog;
import br.ufg.inf.mestrado.hermeswidget.client.utils.ReaderJSon;
import br.ufg.inf.mestrado.hermeswidget.manager.transferObject.HWTransferObject;
import br.ufg.inf.mestrado.hermeswidget.ontologies.Quantitykind;
import br.ufg.inf.mestrado.hermeswidget.ontologies.Unit;


/**
 * 
 * @author Lucas Felipe
 *
 */

public class HWSensorVolatileOrganicCompoundsNew extends HermesWidgetSensorClient implements Runnable {

	private HermesBaseManager                      hermesBaseManager;
	private HWRepresentationServiceSensorIoTStream representationService;
	private ScheduledExecutorService               threadPoolMedidas = null;


	
	// Construtor recebe o registro e um vetor com o tempo total[0] e de intervalos[1]
	// O parametro de tempo vem do input args.
	public HWSensorVolatileOrganicCompoundsNew(File registroAtual, String tempo[]) {
		this.startConfigurationService("./settings/topics_vocs.json");
		this.hermesBaseManager = this.getCommunicationService();
		this.representationService = this.getRepresentationService();
	
		
	}

	@Override
	public void run(){
	
		String url = "https://api.thingspeak.com/channels/869608/feeds/last.json?api_key=GQK8L914F22F2B1H";
		
		try {
			JSONObject json = ReaderJSon.readJsonFromUrl(url);
			System.out.println("TVOC no instante " + json.get("created_at") + ": " + json.get("field4") +" ppb");
			
			String log = "Hermes Widget Sensor Volatile Organic Compounds for environment ---> Paço Municipal"
					   + " started! Date: " + new Date().toString();
		
			HWLog.recordLog(log);
			
			System.out.println(log + "\n");
			
			System.out.println("Ambiente: " + json.get("entry_id") + " [TVOC]");
			
			String dataTempo = json.get("created_at").toString();
			
			String medicaoAtual = json.get("field4").toString();
			
			int countTVOC = Integer.parseInt(json.get("created_at").toString());
			
			float segfloat = Float.valueOf(dataTempo);
			int   segundos = Math.round(segfloat);
		
			// O DTO muda de acordo com os dados que precisam ser passados
			HWTransferObject hermesWidgetTO = representationService.startRepresentationSensor(
					"tvoc.ttl", Integer.toString(segundos), 
					"TVOC", countTVOC, 
					"VolatileOrganicCompounds", // Nome do topico no arquivo topics_vocs
					medicaoAtual, 
					null, "PPB", dataTempo, Unit.PPB, Quantitykind.TVOC);
			
			System.out.println(dataTempo+ "   ----   TVOCS: " +medicaoAtual + " ppb");
			
			threadPoolMedidas.schedule(this.getNotificationService(hermesBaseManager, hermesWidgetTO), segundos, TimeUnit.SECONDS);

			representationService.setModeloMedicaoDadoAmbiental(null);
			
		} catch (JSONException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
	}

}

