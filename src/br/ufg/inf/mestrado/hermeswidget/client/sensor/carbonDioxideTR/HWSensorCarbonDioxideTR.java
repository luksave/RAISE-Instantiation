package br.ufg.inf.mestrado.hermeswidget.client.sensor.carbonDioxideTR;

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

public class HWSensorCarbonDioxideTR extends HermesWidgetSensorClient implements Runnable{

	private HWRepresentationServiceSensorIoTStream representationService;

	
	public HWSensorCarbonDioxideTR(String tempo[]){
		this.startConfigurationService("./settings/topics_temperature.json");
		this.representationService = this.getRepresentationService();
		
	}

	
	@Override
	public void run(){
	
		String url       = "https://api.thingspeak.com/channels/1153475/feeds/last.json?api_key=G75ZY80ZLPN9OXEQ";
		String uriBase   = "http://www.inf.ufg.br/Air-Pure/";
		String sensorIRI = uriBase + "CarbonDioxideSensor-" + UUID.randomUUID().toString();
		
		try {
			JSONObject json = ReaderJSon.readJsonFromUrl(url);
			int count = 0;
			
			//Se o objeto json não for nulo, há medida do Air-Pure a ser modelada...
			if(json != null){
			
				System.out.println("CO2 concentration no instante " + json.get("created_at") + ": " + json.get("field5") +" PPM");
				
				String log = "Hermes Widget Sensor Carbon Dioxide Concentration for environment ---> SALA-INF-001"
						   + " started! Date: " + new Date().toString();
			
				HWLog.recordLog(log);
				
				System.out.println(log + "\n");
				
				System.out.println("Ambiente: " + json.get("entry_id") + " [Carbon Dioxide]");
				
				
				String dataTempo    = json.get("created_at").toString();
				String medicaoAtual = json.get("field5").toString();
				
				int   countCO2 = count++;
					
				representationService.startRepresentationSensor(
						sensorIRI, "co2_concentration.ttl", "ConCO2", countCO2, 
						"CarbonDioxideConcentration", medicaoAtual,	null, 
						"PPM", dataTempo, Unit.PPM, QuantityKind.CO2Concentration);
				
				System.out.println(dataTempo+ "   ----   Carbon Dioxide concentration: " +medicaoAtual + " PPM");
				
				representationService.setModeloMedicaoDadoAmbiental(null);
				
			}
				
		} catch (JSONException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		}	
					
	}
		
}