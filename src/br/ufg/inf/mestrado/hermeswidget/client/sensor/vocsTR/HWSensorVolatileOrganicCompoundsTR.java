package br.ufg.inf.mestrado.hermeswidget.client.sensor.vocsTR;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import br.ufg.inf.mestrado.hermeswidget.client.sensor.general.HermesWidgetSensorClient;
import br.ufg.inf.mestrado.hermeswidget.client.services.HWRepresentationServiceSensorIoTStream;
import br.ufg.inf.mestrado.hermeswidget.client.utils.HWLog;
import br.ufg.inf.mestrado.hermeswidget.client.utils.ReaderJSon;
import br.ufg.inf.mestrado.hermeswidget.ontologies.QuantityKind;
import br.ufg.inf.mestrado.hermeswidget.ontologies.Unit;


public class HWSensorVolatileOrganicCompoundsTR extends HermesWidgetSensorClient implements Runnable {
	
	private HWRepresentationServiceSensorIoTStream representationService;

	
	public HWSensorVolatileOrganicCompoundsTR(String tempo[]) {
		this.startConfigurationService("./settings/topics_vocs.json");
		this.representationService = this.getRepresentationService();		
		
	}

	
	@Override
	public void run(){
	
		String url       = "https://api.thingspeak.com/channels/1153475/feeds/last.json?api_key=G75ZY80ZLPN9OXEQ";
		String uriBase   = "http://www.inf.ufg.br/Air-Pure/";
		String sensorIRI = uriBase + "TVOCSensor-" + UUID.randomUUID().toString();
		
		try {
			JSONObject json = ReaderJSon.readJsonFromUrl(url);
			int count = 0;
			
			//Se o objeto json não for nulo, estou tendo medidas do Air-Pure, logo:
			if(json != null){
				
				System.out.println("TVOC no instante " + json.get("created_at") + ": " + json.get("field4") +" ppb");
				
				String log = "Hermes Widget Sensor Volatile Organic Compounds for environment ---> Paço Municipal"
						   + " started! Date: " + new Date().toString();
			
				HWLog.recordLog(log);
				
				System.out.println(log + "\n");
				
				System.out.println("Ambiente: " + json.get("entry_id") + " [TVOC]");
				
				String dataTempo    = json.get("created_at").toString();
				String medicaoAtual = json.get("field4").toString();

				int   countTVOC = count++;
			
				// O DTO muda de acordo com os dados que precisam ser passados
				representationService.startRepresentationSensor(
						sensorIRI, "tvoc.ttl", "TVOC", countTVOC, 
						"VolatileOrganicCompounds", medicaoAtual, 
						null, "PPB", dataTempo, Unit.PPB, QuantityKind.TVOC);
				
				System.out.println(dataTempo+ "   ----   TVOCS: " +medicaoAtual + " PPB");
				
				representationService.setModeloMedicaoDadoAmbiental(null);

			}
			
			
		} catch (JSONException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		}	
		
	}

}

