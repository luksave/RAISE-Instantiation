package br.ufg.inf.mestrado.hermeswidget.client.sensor.humidityTR;

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


public class HWSensorHumidityTR extends HermesWidgetSensorClient implements Runnable {
	
	private HermesBaseManager                      hermesBaseManager;
	private HWRepresentationServiceSensorIoTStream representationService;
	private ScheduledExecutorService               threadPoolMedidas = null;

	
	public HWSensorHumidityTR(String tempo[]) {
		this.startConfigurationService("./settings/topics_humidity.json");
		this.hermesBaseManager = this.getCommunicationService();
		this.representationService = this.getRepresentationService();

	}

	@Override
	public void run(){
	
		String url = "https://api.thingspeak.com/channels/869608/feeds/last.json?api_key=GQK8L914F22F2B1H";
		
		try {
			JSONObject json = ReaderJSon.readJsonFromUrl(url);
			System.out.println("Humidade Relativa no instante " + json.get("created_at") + ": " + json.get("field2") +" %");
			
			String log = "Hermes Widget Sensor Relative Humidity for environment ---> Paço Municipal"
					   + " started! Date: " + new Date().toString();
		
			HWLog.recordLog(log);
			
			System.out.println(log + "\n");
			
			System.out.println("Ambiente: " + json.get("entry_id") + " [Humidity]");
			
			String dataTempo = json.get("created_at").toString();
			
			String medicaoAtual = json.get("field2").toString();
			
			//Enquanto meu objeto json não for nulo, estou tendo medidas do Air-Pure, logo:
			while(json != null){
				
				int countRHum = Integer.parseInt(json.get("created_at").toString());
				
				float segfloat = Float.valueOf(dataTempo);
				int   segundos = Math.round(segfloat);
			
				// O DTO muda de acordo com os dados que precisam ser passados
				HWTransferObject hermesWidgetTO = representationService.startRepresentationSensor(
						"relative_humidity.ttl", Integer.toString(segundos), 
						"RelHum", countRHum, 
						"RelativeHumidity", 
						medicaoAtual, 
						null, "%", dataTempo, Unit.Percent, Quantitykind.RelativeHumidity);
				
				System.out.println(dataTempo+ "   ----   Relative Humidity: " +medicaoAtual + " %");
				
				threadPoolMedidas.schedule(this.getNotificationService(hermesBaseManager, hermesWidgetTO), segundos, TimeUnit.SECONDS);

				representationService.setModeloMedicaoDadoAmbiental(null);
				
			}
			
			
		} catch (JSONException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
	}
}
