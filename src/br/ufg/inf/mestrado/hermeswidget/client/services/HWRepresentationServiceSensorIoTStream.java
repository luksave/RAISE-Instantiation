package br.ufg.inf.mestrado.hermeswidget.client.services;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import br.ufg.inf.mestrado.hermeswidget.manager.transferObject.HWTransferObject;
import br.ufg.inf.mestrado.hermeswidget.ontologies.Geo;
import br.ufg.inf.mestrado.hermeswidget.ontologies.IoTStream;
import br.ufg.inf.mestrado.hermeswidget.ontologies.IoT_Lite;
import br.ufg.inf.mestrado.hermeswidget.ontologies.Qudt;
import br.ufg.inf.mestrado.hermeswidget.ontologies.sosa;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

public class HWRepresentationServiceSensorIoTStream extends HWRepresentationService{

private HWTransferObject hermesWidgetTO = null;
	
	public HWRepresentationServiceSensorIoTStream() {}
	
	public HWTransferObject startRepresentationSensor(String nomeModelo, String instanteMedidaColetada, String abreviaturaDadoAmbiental, int contadorDadoAmbiental, String nomeClasseDadoAmbiental, 
													  String medidaColetada, String[] medidaComposta, String idAmbiente, String dataTempo, Individual unit, Individual quantity) {
		
		
		criarModeloRDFDeArquivo("./mimic/modelos/"+nomeModelo); //Cria o modelo RDF de acordo com o expressado no arquivo 
		modeloMedicaoDadoAmbiental = ModelFactory.createOntologyModel();
		String sensorOutput = "sensorOutput-"+nomeClasseDadoAmbiental;
		String observationValue = "observationValue"+medidaColetada;
		
		
		Object[] values; 
		Object[] v = {medidaColetada};
		
		values = v;
			
		
		
		modeloMedicaoDadoAmbiental = representObservation(abreviaturaDadoAmbiental, "property-"+abreviaturaDadoAmbiental, "sensor-"  +nomeClasseDadoAmbiental, "entity-"  +abreviaturaDadoAmbiental, 
														  sensorOutput, observationValue, values, idAmbiente, dataTempo, unit, quantity);
	
		
		if (contadorDadoAmbiental == 0) modeloMedicaoDadoAmbiental.write(System.out, "TURTLE");
		
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
	
	private OntModel representObservation(String sinal, String property, String sensor, String entity, String sensorOutput, String observationValue, Object[] values, String feature, String dateTimeID, Individual unit, Individual quantity) {
		
		
		String sensorIRI   = sensor + "-" + UUID.randomUUID().toString();
		String propertyIRI = IoTStream.NS + property;
		
	
		
		
		//Aqui uma IoTStream é criada
		Resource streamResource = modeloMedicaoDadoAmbiental
				.createResource(propertyIRI + dateTimeID)
					.addProperty(RDF.type, IoTStream.IotStream);
		
		
		//Aqui a classe StreamObservation é criada. Essa classe contém o resultado observado pelo sensor e o momento desta observação
		Resource StreamObservation = modeloMedicaoDadoAmbiental
				.createResource(IoTStream.NS + sensorOutput +"-"+ dateTimeID);
		
		
		/**Aqui um recurso sensor é criado de acordo com o que temos na ontologia IoT-Stream
		 * Ele é do tipo sosa.Sensor
		 * possui um tipo de quantidade e unidade, definidos logo abaixo de acordo com o tipo de dado ambiental
		 * Importante: Esse sensor realiza uma StreamObservation que pertence a uma IotStream
		 */
		Resource sensorResource = modeloMedicaoDadoAmbiental
				.createResource(sensorIRI)
					.addProperty(RDF.type, sosa.Sensor)
					.addProperty(sosa.madeObservation, StreamObservation)
					.addProperty(Qudt.hasUnit, unit)
					.addProperty(Qudt.hasQuantityKind, quantity);	
		
		
		/**
		 * Um sensor dá como saída um pedaço de informação (valor observado), o valor
		 * é representado por uma StreamObservation. Aqui é realizada a atribuição do 
		 * valor observado e do momento em que foi observado
		 */
		
		//Aqui o serviço que provê a IotStream é instanciado
		Resource serviceResource = modeloMedicaoDadoAmbiental
				.createResource("service"+sinal)
					.addProperty(RDF.type, IoT_Lite.Service)
					.addProperty(IoT_Lite.endpoint, "http://www.ebserh.gov.br/web/hc-ufg/sensors/measures/room1"+"^^xsd:anyURI"); 
		
		//Aqui a localização onde a stream foi observada é capturada (geo:Point)
		Resource locationResource = modeloMedicaoDadoAmbiental
				.createResource("location")
					.addProperty(RDF.type, Geo.point)
					.addProperty(Geo.long_, "-49.255") 
					.addProperty(Geo.lat, "-16.6799"); 
		
		// Aqui uma IotStream é criada com a atribuição das classes relacionadas
		streamResource
			.addProperty(IoTStream.generatedBy, sensorResource)
			.addProperty(Geo.location, locationResource)
			.addProperty(IoTStream.providedBy, serviceResource);
		
		
		//Aqui uma StreamObservation é atribuída ao sensor que a gera e à IotStream à qual pertence
		StreamObservation
			.addProperty(sosa.madeBySensor, sensorResource)
			.addProperty(IoTStream.belongsTo, streamResource)
			.addProperty(sosa.hasSimpleResult, values[0].toString())
			.addProperty(sosa.resultTime, dateTimeID);
		
		
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
