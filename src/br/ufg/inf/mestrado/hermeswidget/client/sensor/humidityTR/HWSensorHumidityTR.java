package br.ufg.inf.mestrado.hermeswidget.client.sensor.humidityTR;

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


public class HWSensorHumidityTR extends HermesWidgetSensorClient implements Runnable {
	
	private HWRepresentationServiceSensorIoTStream representationService;
	
	
	public HWSensorHumidityTR(String tempo[]) {
		this.startConfigurationService("./settings/topics_humidity.json");
		this.representationService = this.getRepresentationService();

	}

	
	@Override
	public void run(){
	
		String url       = "https://api.thingspeak.com/channels/1153475/feeds/last.json?api_key=G75ZY80ZLPN9OXEQ";
		String uriBase   = "http://www.inf.ufg.br/Air-Pure/";
		String sensorIRI = uriBase + "RelativeHumiditySensor-" + UUID.randomUUID().toString();
		
		try {
			JSONObject json = ReaderJSon.readJsonFromUrl(url);
			int count = 0;
			
			//Enquanto meu objeto json não for nulo, estou tendo medidas do Air-Pure, logo:
			if(json != null){
			
				System.out.println("Humidade Relativa no instante " + json.get("created_at") + ": " + json.get("field2") +" %");
				
				String log = "Hermes Widget Sensor Relative Humidity for environment ---> Paço Municipal"
						   + " started! Date: " + new Date().toString();
			
				HWLog.recordLog(log);
				
				System.out.println(log + "\n");
				
				System.out.println("Ambiente: " + json.get("entry_id") + " [Humidity]");
				
				String dataTempo    = json.get("created_at").toString();
				String medicaoAtual = json.get("field2").toString();
				
				int   countRHum = count++;
		
				// O DTO muda de acordo com os dados que precisam ser passados
				representationService.startRepresentationSensor(
						sensorIRI, "relative_humidity.ttl", "RelHum", countRHum, 
						"RelativeHumidity",medicaoAtual, null, 
						"%", dataTempo, Unit.Percent, QuantityKind.RelativeHumidity);
				
				System.out.println(dataTempo+ "   ----   Relative Humidity: " +medicaoAtual + " %");
				
				representationService.setModeloMedicaoDadoAmbiental(null);
				
			}
			
		} catch (JSONException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		}	
		
	}
	
}