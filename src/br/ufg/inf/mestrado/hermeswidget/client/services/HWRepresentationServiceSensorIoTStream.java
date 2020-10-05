package br.ufg.inf.mestrado.hermeswidget.client.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.json.JSONException;

import br.ufg.inf.mestrado.hermeswidget.manager.transferObject.HWTransferObject;
import br.ufg.inf.mestrado.hermeswidget.ontologies.Geo;
import br.ufg.inf.mestrado.hermeswidget.ontologies.IoTStream;
import br.ufg.inf.mestrado.hermeswidget.ontologies.IoT_Lite;
import br.ufg.inf.mestrado.hermeswidget.ontologies.QUDT;
import br.ufg.inf.mestrado.hermeswidget.ontologies.SOSA;
import br.ufg.inf.mestrado.hermeswidget.ontologies.SSN;
import br.ufg.inf.mestrado.hermeswidget.ontologies.Unit;
import br.ufg.mestrado.hermeswidget.client.preprocessing.CarbonDioxidePreprocessing;
import br.ufg.mestrado.hermeswidget.client.preprocessing.ITUPreprocessing;
import br.ufg.mestrado.hermeswidget.client.preprocessing.TemperaturePreprocessing;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;


public class HWRepresentationServiceSensorIoTStream extends HWRepresentationService{

	private HWTransferObject hermesWidgetTO = null;
	
	public HWRepresentationServiceSensorIoTStream() {}
	
	public HWTransferObject startRepresentationSensor(
			String sensorIRI, String nomeModelo, String abreviaturaDadoAmbiental, int contador, String nomeClasseDadoAmbiental, 
			String medidaColetada, String[] medidaComposta, String idAmbiente, String instanteColeta, Individual unit, Individual quantity) throws JSONException, IOException {
		
		
		criarModeloRDFDeArquivo("./mimic/modelos/"+nomeModelo); //Cria o modelo RDF de acordo com o expressado no arquivo 
		modeloMedicaoDadoAmbiental = ModelFactory.createOntologyModel();
		String sensorOutput = "sensorOutput-"+nomeClasseDadoAmbiental;
		String observationValue = "observationValue"+medidaColetada;
		
		
		Object[] values; 
		Object[] v = {medidaColetada};
		
		values = v;
			
		
		modeloMedicaoDadoAmbiental = representObservation(abreviaturaDadoAmbiental, "property-"+abreviaturaDadoAmbiental, sensorIRI, "entity-"  +abreviaturaDadoAmbiental, 
														  sensorOutput, observationValue, values, idAmbiente, instanteColeta, unit, quantity);
	
		
		ByteArrayOutputStream baosContextoFiltrado = new ByteArrayOutputStream();
		modeloMedicaoDadoAmbiental.write(baosContextoFiltrado, tipoSerializacao, caminhoSchemaOntologico);
		byte[] byteArray = baosContextoFiltrado.toByteArray();
			
		// Configura transfer object
		hermesWidgetTO = new HWTransferObject();
		hermesWidgetTO.setIdEntidade("place"+idAmbiente);
		hermesWidgetTO.setNomeTopico(nomeClasseDadoAmbiental);
		hermesWidgetTO.setComplementoTopico("");
		hermesWidgetTO.setContexto(byteArray);
		hermesWidgetTO.setCaminhoOntologia(caminhoSchemaOntologico);
		hermesWidgetTO.setTipoSerializacao(tipoSerializacao);
		hermesWidgetTO.setSensorValue(medidaColetada); 
		
		return hermesWidgetTO;
		
	}
	
	private OntModel representObservation(String sinal, String property, String sensorIRI, String entity, String sensorOutput, String observationValue, Object[] values, 
			                              String feature, String dateTimeID, Individual unit, Individual quantity) throws JSONException, IOException {
		
		
		String propertyIRI = IoTStream.NS + property;
		String uriData     = dateTimeID.substring(00, 10) + "-" + dateTimeID.substring(11, 13) + "-" 
		                   + dateTimeID.substring(14, 16) + "-" + dateTimeID.substring(17, 19);
		
		
		//Aqui a localiza��o onde a stream foi observada � capturada (geo:Point)
		Resource locationResource = modeloMedicaoDadoAmbiental
				.createResource("http://www.inf.ufg.br/Air-Pure/location")
					.addProperty(RDF.type, Geo.point)
					.addProperty(Geo.long_, "-49.255") 
					.addProperty(Geo.lat, "-16.6799"); 
		
		Resource AirPureSala1 = modeloMedicaoDadoAmbiental
				.createResource("http://www.inf.ufg.br/Air-Pure/service/" + "INF0001")
				.addProperty(RDF.type, SSN.SensingDevice)
				.addProperty(Geo.location, locationResource);
		
		Resource airPureINF = modeloMedicaoDadoAmbiental
				.createResource("http://www.inf.ufg.br/Air-Pure/service/" + "SALASINF")
				.addProperty(RDF.type, SSN.Device)
				.addProperty(IoT_Lite.hasSubSystem, AirPureSala1);		
		
		
		//Aqui uma IoTStream � criada
		Resource streamResource = modeloMedicaoDadoAmbiental
				.createResource(propertyIRI + uriData)
					.addProperty(RDF.type, IoTStream.IotStream);
		
		
		//Aqui a classe StreamObservation � criada. Essa classe cont�m o resultado observado pelo sensor e o momento desta observa��o
		Resource StreamObservation = modeloMedicaoDadoAmbiental
				.createResource(IoTStream.NS + sensorOutput +"-"+ uriData);
		
		
		/**Aqui um recurso sensor � criado de acordo com o que temos na ontologia IoT-Stream
		 * Ele � do tipo sosa.Sensor
		 * possui um tipo de quantidade e unidade, definidos logo abaixo de acordo com o tipo de dado ambiental
		 * Importante: Esse sensor realiza uma StreamObservation que pertence a uma IotStream
		 */
		Resource sensorResource = modeloMedicaoDadoAmbiental
				.createResource(sensorIRI)
					.addProperty(RDF.type, SOSA.Sensor)
					.addProperty(SOSA.madeObservation, StreamObservation)
					.addProperty(IoT_Lite.hasSensingDevice, AirPureSala1)//Isso deve ser mudado pelo identificador �nico do dispositivo. Verificando com a Bruna
					.addProperty(QUDT.hasUnit, unit)
					.addProperty(QUDT.hasQuantityKind, quantity);	
		
		
		
		/**
		 * Um sensor d� como sa�da um peda�o de informa��o (valor observado), o valor
		 * � representado por uma StreamObservation. Aqui � realizada a atribui��o do 
		 * valor observado e do momento em que foi observado
		 */
		
		//Aqui o servi�o que prov� a IotStream � instanciado
		Resource serviceResource = modeloMedicaoDadoAmbiental
				.createResource("http://www.inf.ufg.br/Air-Pure/service/" + sinal)
					.addProperty(RDF.type, IoT_Lite.Service)
					.addProperty(IoT_Lite.endpoint, "http://www.ebserh.gov.br/web/hc-ufg/sensors/measures/room1"+"^^xsd:anyURI"); 
		
		// Aqui uma IotStream � criada com a atribui��o das classes relacionadas
		streamResource
			.addProperty(IoTStream.generatedBy, sensorResource)
			.addProperty(IoTStream.providedBy, serviceResource);
		
		
		//Aqui uma StreamObservation � atribu�da ao sensor que a gera e � IotStream � qual pertence
		StreamObservation
			.addProperty(SOSA.madeBySensor, sensorResource)
			.addProperty(IoTStream.belongsTo, streamResource)
			.addProperty(SOSA.hasSimpleResult, values[0].toString())
			.addProperty(SOSA.resultTime, dateTimeID);
		
		
		/**
		 * A concentra��o de Di�xido de Carbono � o valor utilizado para aferir a qualidade do ar interno,
		 * pois n�o temos outros poluentes...
		 * Normalmente devemos nos preocupar se essa concentra��o ultrapassa 1000ppm
		 */

		
		/**
		 * Pr�-processamento do dado de Concentra��o de CO2
		 * Se a concentra��o passa do limite de 1000 ppm, calcule a qualidade do ar e insira no modelo
		 */
		
		String medida = values[0].toString();
		
		if(unit == Unit.PPM ){
			
			double CPdioxido = Double.parseDouble(medida);
			
			if( CPdioxido >= 1000){
				String airQuality = CarbonDioxidePreprocessing.IQACarbonDioxide(CPdioxido);
				Resource eventResource = modeloMedicaoDadoAmbiental
						.createResource("http://www.inf.ufg.br/Air-Pure/event/" + StreamObservation)
							.addProperty(IoTStream.label, airQuality)
							.addProperty(IoTStream.detectedFrom, streamResource)
							.addProperty(IoTStream.windowStart, dateTimeID)
							.addProperty(IoTStream.windowEnd, dateTimeID);

			}		
		
		}

		if(unit == Unit.DEG_C){
			double temperature = Double.parseDouble(medida);
			
			if(temperature < 23 || temperature > 26){
				String thermalComfort = TemperaturePreprocessing.ThermalComfort(temperature);
				
				Resource eventResource = modeloMedicaoDadoAmbiental
						.createResource("http://www.inf.ufg.br/Air-Pure/event/" + StreamObservation)
							.addProperty(IoTStream.label, thermalComfort)
							.addProperty(IoTStream.detectedFrom, streamResource)
							.addProperty(IoTStream.windowStart, dateTimeID)
							.addProperty(IoTStream.windowEnd, dateTimeID);

			
			
				if(thermalComfort == "Sala muito quente"){
					double ITU = ITUPreprocessing.THIndex(temperature);
					//Agora o que fazer com o �ndice de temperatura?
					//A ICD-10 apresenta informa��es sobre os riscos da exposi��o a temperaturas extremas
					//Mas o que s�o as temperaturas extremas?
					//Por enquanto podemos pensar apenas em quest�es de conforto, produtividade, etc...
					
				}
				
			}
			
		}
		
		
		/**
		 * Caso seja necess�rio imprimir o modelo
		 * IMPORTANTE: Comentar ap�s uso, ou a sa�da fica impratic�vel
		 * Op��es: TURTLE, N-TRIPLES, RDF/XML
		 */
		//modeloMedicaoDadoAmbiental.write(System.out, "TURTLE");
		
		return modeloMedicaoDadoAmbiental;
		
	}
	
	
	public void setModeloMedicaoDadoAmbiental(OntModel modeloMedicaoDadoAmbiental) {
		this.modeloMedicaoDadoAmbiental = modeloMedicaoDadoAmbiental;
		
	}
	
	
	public HWTransferObject getDataTransferObject(String idAmbiente, String nomeClasseDadoAmbiental, 
			                                      String complementoTopico, byte[] medidaByteArray, 
			                                      String valorMedidaColetada, String instanteMedidaColetada) {
		
		hermesWidgetTO = new HWTransferObject();
		
		hermesWidgetTO.setIdEntidade("person"+idAmbiente);
		hermesWidgetTO.setNomeTopico(nomeClasseDadoAmbiental);
		hermesWidgetTO.setComplementoTopico(complementoTopico);
		hermesWidgetTO.setContexto(medidaByteArray);
		hermesWidgetTO.setCaminhoOntologia(caminhoSchemaOntologico);
		hermesWidgetTO.setTipoSerializacao(tipoSerializacao);
		hermesWidgetTO.setSensorValue(valorMedidaColetada);
		
		return hermesWidgetTO;
		
	}
		
	

}
