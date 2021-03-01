package br.ufg.inf.mestrado.hermeswidget.client.sensor.temperatureTR;

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


public class HWSensorTemperatureTR extends HermesWidgetSensorClient implements Runnable {
	
	private HWRepresentationServiceSensorIoTStream representationService;

	public HWSensorTemperatureTR(String tempo[]) {
		this.startConfigurationService("./settings/topics_temperature.json");
		this.representationService = this.getRepresentationService();		
		
	}

	
	@Override
	public void run(){
	
		String url       = "https://api.thingspeak.com/channels/869608/feeds/last.json?api_key=I1ROU4UHAC0AWDPL";
		String uriBase   = "http://www.inf.ufg.br/Air-Pure-";
		
		try {
			JSONObject json = ReaderJSon.readJsonFromUrl(url);
			int count = 0;
			
			//Se o objeto json não for nulo, estou tendo medidas do Air-Pure, logo:
			if(json != null){

				String log = "Hermes Widget Sensor Temperature for environment ---> Paço Municipal"
						   + " started! Date: " + new Date().toString();
			
				HWLog.recordLog(log);
				
				System.out.println(log + "\n");
				
				System.out.println("Ambiente: " + json.get("entry_id") + " [Temperature]");
				
				
				String dataTempo    = json.get("created_at").toString();
				String medicaoAtual = json.get("field1").toString();
					
				String sensorIRI = uriBase + json.get("entry_id") + "/TemperatureSensor";
				
				int  countTEMP = count++;

				representationService.startRepresentationSensor(
						sensorIRI, "temperature.ttl", "Temp", countTEMP, 
						"Temperature", medicaoAtual, null, 
						"°C", dataTempo, Unit.DEG_C, QuantityKind.CelsiusTemperature);
							
				System.out.println(dataTempo+ "   ----   Temperature: " +medicaoAtual + " °C");
				
				representationService.setModeloMedicaoDadoAmbiental(null);
				
			}
			
		} catch (JSONException | IOException e1) {
			e1.printStackTrace();
			System.out.println("Erro de Leitura");
			
		}
		
	}
	
}