package br.ufg.inf.mestrado.hermeswidget.client.services;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import br.ufg.inf.mestrado.hermeswidget.manager.transferObject.HWTransferObject;
import br.ufg.inf.mestrado.hermeswidget.ontologies.Geo;
import br.ufg.inf.mestrado.hermeswidget.ontologies.IoT_Lite;
import br.ufg.inf.mestrado.hermeswidget.ontologies.SSN;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

public class HWRepresentationServiceSensor extends HWRepresentationService {
	
	private HWTransferObject hermesWidgetTO = null;
	
	public HWRepresentationServiceSensor() {}
	
	
	public HWTransferObject startRepresentationSensor(String nomeModelo, String instanteMedidaColetada, 
			                                          String abreviaturaDadoAmbiental, int contadorDadoAmbiental, 
			                                          String nomeClasseDadoAmbiental, String medidaColetada, 
			                                          String[] medidaComposta, String unidadeMedida, 
			                                          String idAmbiente) {
		
		criarModeloRDFDeArquivo("./mimic/modelos/"+nomeModelo);
		
		modeloMedicaoDadoAmbiental = ModelFactory.createOntologyModel();
		String sensorOutput = "sensorOutput-"+nomeClasseDadoAmbiental;
		String observationValue = "observationValue";
		
		Object[] values; 
		
		if (abreviaturaDadoAmbiental == "PresSang") {
			Object[] v = medidaComposta;
			values = v;
			
		} else {
			Object[] v = {medidaColetada};
			values = v;
			
		}
		
		
		modeloMedicaoDadoAmbiental = representObservation(abreviaturaDadoAmbiental, "property-"+abreviaturaDadoAmbiental, "sensor-"+nomeClasseDadoAmbiental, sensorOutput, "entity-"+abreviaturaDadoAmbiental, observationValue, values, unidadeMedida, idAmbiente);
	
		
		if (contadorDadoAmbiental == 0) modeloMedicaoDadoAmbiental.write(System.out, "TURTLE");
		
		ByteArrayOutputStream baosContextoFiltrado = new ByteArrayOutputStream();
		modeloMedicaoDadoAmbiental.write(baosContextoFiltrado, tipoSerializacao, caminhoSchemaOntologico);
		byte[] byteArray = baosContextoFiltrado.toByteArray();
		
		
		// Configura transfer object
		hermesWidgetTO = new HWTransferObject();
		
		hermesWidgetTO.setIdEntidade("person"+idAmbiente);
		hermesWidgetTO.setNomeTopico(nomeClasseDadoAmbiental);
		hermesWidgetTO.setComplementoTopico("");
		hermesWidgetTO.setContexto(byteArray);
		hermesWidgetTO.setCaminhoOntologia(caminhoSchemaOntologico);
		hermesWidgetTO.setTipoSerializacao(tipoSerializacao);
		
		if (abreviaturaDadoAmbiental == "Temp") {
			hermesWidgetTO.setSensorValue(medidaColetada);
			
		} else {
			hermesWidgetTO.setSensorValue(medidaComposta[0]+" e "+medidaComposta[1]);
			
		}
		
		return hermesWidgetTO;
		
	}
	
	private OntModel representObservation(String sinal, String property, String sensor,  
										  String sensorOutput, String entity, String observationValue, 
										  Object[] values, String unidadeMedida, String feature) {
	
		
		String sensorIRI = sensor +"-"+ UUID.randomUUID().toString();
		
		/** Sensor */
		Resource sensorResource = modeloMedicaoDadoAmbiental
				.createResource(sensorIRI)
					.addProperty(RDF.type, SSN.Sensor)
					.addProperty(IoT_Lite.hasUnit, "qu:degree_Celcius") // importar de qu
					.addProperty(IoT_Lite.hasQuantityKind, "qu:temperature"); // importar de qu
		
		/** Sensing Device INCLUIR */
		Resource sensingDeviceResource = modeloMedicaoDadoAmbiental
				.createResource("device"+sinal)
					.addProperty(RDF.type, SSN.SensingDevice)
					.addProperty(IoT_Lite.exposedBy, sensorResource);
		

		
		/** Point INCLUIR */
		Resource locationResource = modeloMedicaoDadoAmbiental
				.createResource("location")
					.addProperty(RDF.type, "geo:Point")
					.addProperty(Geo.long_, "-49.255") 
					.addProperty(Geo.lat, "-16.6799"); 
					
		
		/** Service INCLUIR */
		Resource serviceResource = modeloMedicaoDadoAmbiental
				.createResource("service"+sinal)
					.addProperty(RDF.type, IoT_Lite.Service)
					.addProperty(IoT_Lite.endpoint, "http://www.ebserh.gov.br/web/hc-ufg/sensors/measures/room1"+"^^xsd:anyURI"); 
	
		

		/**
		 * A sensor can do (implements) sensing: that is, a sensor is any entity
		 * that can follow a sensing method and thus observe some Property of a
		 * FeatureOfInterest. Sensors may be physical devices, computational
		 * methods, a laboratory setup with a person following a method, or any
		 * other thing that can follow a Sensing Method to observe a Property.
		 */
	

		/**
		 * A sensor outputs a piece of information (an observed value), the
		 * value itself being represented by an ObservationValue.
		 */
		

		/**
		 * An Observation is a Situation in which a Sensing method has been used
		 * to estimate or calculate a value of a Property of a
		 * FeatureOfInterest. Links to Sensing and Sensor describe what made the
		 * Observation and how; links to Property and Feature detail what was
		 * sensed; the result is the output of a Sensor; other metadata details
		 * times etc.
		 */
		
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		DatatypeFactory df;
		XMLGregorianCalendar dateTime = null;
		
		try {
			df = DatatypeFactory.newInstance();
			dateTime = df.newXMLGregorianCalendar(calendar);
			
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
			
		}
		
		modeloMedicaoDadoAmbiental.createResource(entity +"-"+ UUID.randomUUID().toString())
			.addProperty(RDF.type, IoT_Lite.Entity)
			.addProperty(IoT_Lite.hasSensingDevice, sensingDeviceResource)
			.addProperty(Geo.location, locationResource)
			.addProperty(IoT_Lite.hasMetadata, serviceResource)
			.addProperty(SSN.observationResultTime, modeloMedicaoDadoAmbiental.createTypedLiteral(dateTime.toString(), XSDDatatype.XSDdateTime));

		
		return modeloMedicaoDadoAmbiental;
	}
	
	
	public void setModeloMedicaoSinalVital(OntModel modeloMedicaoSinalVital) {
		this.modeloMedicaoDadoAmbiental = modeloMedicaoSinalVital;
		
	}
	
	
	public HWTransferObject getDataTransferObject(String idPaciente, String nomeClasseSinalVital, String complementoTopico, byte[] medidaByteArray, String valorMedidaColetada, String instanteMedidaColetada) {
		
		hermesWidgetTO = new HWTransferObject();
		
		hermesWidgetTO.setIdEntidade("person"+idPaciente);
		hermesWidgetTO.setNomeTopico(nomeClasseSinalVital);
		hermesWidgetTO.setComplementoTopico(complementoTopico);
		hermesWidgetTO.setContexto(medidaByteArray);
		hermesWidgetTO.setCaminhoOntologia(caminhoSchemaOntologico);
		hermesWidgetTO.setTipoSerializacao(tipoSerializacao);
		hermesWidgetTO.setSensorValue(valorMedidaColetada);
		
		return hermesWidgetTO;
		
	}

}
