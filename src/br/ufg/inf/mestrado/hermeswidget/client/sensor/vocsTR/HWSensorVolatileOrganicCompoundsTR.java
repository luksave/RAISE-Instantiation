package br.ufg.inf.mestrado.hermeswidget.client.sensor.vocsTR;

import java.io.IOException;
import java.util.Date;

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
	
		String url       = "https://api.thingspeak.com/channels/869608/feeds/last.json?api_key=I1ROU4UHAC0AWDPL";
		String uriBase   = "http://www.inf.ufg.br/Air-Pure-";
		
		try {
			JSONObject json = ReaderJSon.readJsonFromUrl(url);
			int count = 0;
			
			//Se o objeto json n�o for nulo, estou tendo medidas do Air-Pure, logo:
			if(json != null){
			
				String log = "Hermes Widget Sensor Volatile Organic Compounds for environment ---> Pa�o Municipal"
						   + " started! Date: " + new Date().toString();
			
				HWLog.recordLog(log);
				
				System.out.println(log + "\n");
				
				System.out.println("Ambiente: " + json.get("entry_id") + " [TVOC]");
				
				String dataTempo    = json.get("created_at").toString();
				String medicaoAtual = json.get("field4").toString();
				
				String sensorIRI = uriBase + json.get("entry_id") + "/TVOCSensor";
				
				int countTVOC = count++;
			
				representationService.startRepresentationSensor(
						sensorIRI, "tvoc.ttl", "TVOC", countTVOC, 
						"VolatileOrganicCompounds", medicaoAtual, 
						null, "PPB", dataTempo, Unit.PPB, QuantityKind.TVOC);
				
				System.out.println(dataTempo+ "   ----   TVOCS: " +medicaoAtual + " PPB");
				
				representationService.setModeloMedicaoDadoAmbiental(null);

			}
			
		} catch (JSONException | IOException e1) {
			e1.printStackTrace();
			System.out.println("Erro de Leitura");
			
		}	
		
	}

}