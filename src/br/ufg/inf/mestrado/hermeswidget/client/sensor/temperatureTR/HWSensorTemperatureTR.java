package br.ufg.inf.mestrado.hermeswidget.client.sensor.temperatureTR;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;
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
import br.ufg.inf.mestrado.hermeswidget.ontologies.QuantityKind;
import br.ufg.inf.mestrado.hermeswidget.ontologies.Unit;


public class HWSensorTemperatureTR extends HermesWidgetSensorClient implements Runnable {
	
	private HermesBaseManager                      hermesBaseManager;
	private HWRepresentationServiceSensorIoTStream representationService;
	private ScheduledExecutorService               threadPoolMedidas = null;

	
	public HWSensorTemperatureTR(String tempo[]) {
		this.startConfigurationService("./settings/topics_temperature.json");
		this.hermesBaseManager = this.getCommunicationService();
		this.representationService = this.getRepresentationService();		
		
	}

	
	@Override
	public void run(){
	
		String url = "https://api.thingspeak.com/channels/869608/feeds/last.json?api_key=GQK8L914F22F2B1H";
		
		try {
			JSONObject json = ReaderJSon.readJsonFromUrl(url);
			
			//Enquanto meu objeto json não for nulo, estou tendo medidas do Air-Pure, logo:
			while(json != null){

				System.out.println("Temperatura no instante " + json.get("created_at") + ": " + json.get("field1") +" °C");
			
				String log = "Hermes Widget Sensor Temperature for environment ---> Paço Municipal"
						   + " started! Date: " + new Date().toString();
			
				HWLog.recordLog(log);
				
				System.out.println(log + "\n");
				
				System.out.println("Ambiente: " + json.get("entry_id") + " [Temperature]"+ "\n");
				
				
				String dataTempo    = json.get("created_at").toString();
				String medicaoAtual = json.get("field1").toString();
				String uriBase      = "http://www.inf.ufg.br/Air-Pure/";
				String sensorIRI    = uriBase + "TemperatureSensor-" + UUID.randomUUID().toString();
					
				int    countTEMP = Integer.parseInt(json.get("created_at").toString());
				float   segfloat = Float.valueOf(dataTempo);
				int     segundos = Math.round(segfloat);
			
				// O DTO muda de acordo com os dados que precisam ser passados
				HWTransferObject hermesWidgetTO = representationService.startRepresentationSensor(
						sensorIRI, "temperature.ttl", Integer.toString(segundos), 
						"Temp", countTEMP, 
						"Temperature", // 
						medicaoAtual, 
						null, "°C", dataTempo, Unit.DEG_C, QuantityKind.CelsiusTemperature);
				
				System.out.println(dataTempo+ "   ----   Temperature: " +medicaoAtual + " °C");
				
				threadPoolMedidas.schedule(this.getNotificationService(hermesBaseManager, hermesWidgetTO), segundos, TimeUnit.SECONDS);

				representationService.setModeloMedicaoDadoAmbiental(null);
				
				json = ReaderJSon.readJsonFromUrl(url);
				
			}
			
		} catch (JSONException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
}