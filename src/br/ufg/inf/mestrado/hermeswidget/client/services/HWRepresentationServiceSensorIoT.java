package br.ufg.inf.mestrado.hermeswidget.client.services;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import br.ufg.inf.mestrado.hermeswidget.manager.transferObject.HWTransferObject;
import br.ufg.inf.mestrado.hermeswidget.ontologies.SSN;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

public class HWRepresentationServiceSensorIoT extends HWRepresentationService {
	
	private HWTransferObject hermesWidgetTO = null;
	
	public HWRepresentationServiceSensorIoT() {}
	
	public HWTransferObject startRepresentationSensor(String nomeModelo, String instanteMedidaColetada, 
													  String abreviaturaDadoAmbiental, int contadorDadoAmbiental, 
													  String nomeClasseDadoAmbiental, String medidaColetada, 
													  String[] medidaComposta, String unidadeMedida, 
													  String idAmbiente) {
		
		criarModeloRDFDeArquivo("./mimic/modelos/"+nomeModelo); //São os modelos para representar as informações
		
		
		modeloMedicaoDadoAmbiental = ModelFactory.createOntologyModel();
		
		String sensorOutput = "sensorOutput-"+nomeClasseDadoAmbiental;
		String observationValue = "observationValue";
		
		Object[] values; 
		
		// Não se aplica aos dados Ambientais coletados.
		/*if (abreviaturaDadoAmbiental == "PresSang") {
			Object[] v = medidaComposta;
			values = v;
			
		} else {*/
			Object[] v = {medidaColetada};
			values = v;
			
		//}
		
		modeloMedicaoDadoAmbiental = representObservation(abreviaturaDadoAmbiental, "property-"+abreviaturaDadoAmbiental, 
													   						        "sensor-"  +nomeClasseDadoAmbiental, sensorOutput, 
													   						        "observation-"+abreviaturaDadoAmbiental, observationValue, 
													   						         values, unidadeMedida, idAmbiente);
		
		
		
		if (contadorDadoAmbiental == 0)	modeloMedicaoDadoAmbiental.write(System.out, "TURTLE");
		
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
		//Como todo os dados são de medidas simples, o valor do sensor será dado por medidaColetada
		hermesWidgetTO.setSensorValue(medidaColetada); 
		
		//if (abreviaturaDadoAmbiental == "Temp") hermesWidgetTO.setSensorValue(medidaColetada);
		//else 									  hermesWidgetTO.setSensorValue(medidaComposta[0]+" e "+medidaComposta[1]);
				
		return hermesWidgetTO;
	
	}
	
	private OntModel representObservation(String sinal, String property, String sensor,  String sensorOutput, 
			                              String observation, String observationValue, Object[] values, 
			                              String unidadeMedida, String feature) {
				
		String featureIRI; // +"-"+ UUID.randomUUID().toString();
		
		featureIRI = SSN.NS + feature;
		
		String propertyIRI; // +"-"+ UUID.randomUUID().toString();
		
		propertyIRI = SSN.NS + property;
		
		String sensorIRI = SSN.NS + sensor +"-"+ UUID.randomUUID().toString();
		
		Resource featureOfInterestResource = modeloMedicaoDadoAmbiental
				.createResource(featureIRI)
					.addProperty(RDF.type, SSN.FeatureOfInterest);
		
		/**
		 * An observable Quality of an Event or Object. That is, not a quality
		 * of an abstract entity as is also allowed by DUL's Quality, but rather
		 * an aspect of an entity that is intrinsic to and cannot exist without
		 * the entity and is observable by a sensor.
		 */
		Resource propertyResource = modeloMedicaoDadoAmbiental
				.createResource(propertyIRI)
					.addProperty(RDF.type, SSN.Property);

		/**
		 * A sensor can do (implements) sensing: that is, a sensor is any entity
		 * that can follow a sensing method and thus observe some Property of a
		 * FeatureOfInterest. Sensors may be physical devices, computational
		 * methods, a laboratory setup with a person following a method, or any
		 * other thing that can follow a Sensing Method to observe a Property.
		 */
		Resource sensorResource = modeloMedicaoDadoAmbiental
				.createResource(sensorIRI)
					.addProperty(RDF.type, SSN.SensingDevice)
					.addProperty(SSN.observes, propertyResource);

		/**
		 * A sensor outputs a piece of information (an observed value), the
		 * value itself being represented by an ObservationValue.
		 */
		Resource sensorOutputResource;	
		
		sensorOutputResource = modeloMedicaoDadoAmbiental
				.createResource(SSN.NS + sensorOutput +"-"+ UUID.randomUUID().toString())
					.addProperty(RDF.type, SSN.SensorOutput)
					.addProperty(SSN.isProducedBy, sensorResource);
		

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
		
		
		modeloMedicaoDadoAmbiental.createResource(SSN.NS + observation +"-"+ UUID.randomUUID().toString())
			.addProperty(RDF.type, SSN.Observation)
			.addProperty(SSN.observedBy, sensorResource)
			.addProperty(SSN.observedProperty, propertyResource)
			.addProperty(SSN.observationResult, sensorOutputResource)
			.addProperty(SSN.featureOfInterest, featureOfInterestResource)
			.addProperty(SSN.observationResultTime, modeloMedicaoDadoAmbiental.createTypedLiteral(dateTime.toString(), XSDDatatype.XSDdateTime));
		
		
		if (sinal == "Temp") {
			sensorOutputResource.addProperty(SSN.hasValue, 
				modeloMedicaoDadoAmbiental.createResource(SSN.NS + observationValue +"-"+ UUID.randomUUID().toString())
					.addProperty(RDF.type, SSN.ObservationValue)
					.addProperty(SSN.hasOutputValue, modeloMedicaoDadoAmbiental.createTypedLiteral(values[0], XSDDatatype.XSDfloat))
					.addProperty(SSN.hasOutputUnit, modeloMedicaoDadoAmbiental.createTypedLiteral(unidadeMedida, XSDDatatype.XSDstring))
			);
			
		} else {
			sensorOutputResource.addProperty(SSN.hasValue, 
				modeloMedicaoDadoAmbiental.createResource(SSN.NS + observationValue +"-"+ UUID.randomUUID().toString())
					.addProperty(RDF.type, SSN.ObservationValue)
					.addProperty(SSN.hasOutputValue, modeloMedicaoDadoAmbiental.createTypedLiteral(values[0], XSDDatatype.XSDnonNegativeInteger))
					.addProperty(SSN.hasOutputUnit, modeloMedicaoDadoAmbiental.createTypedLiteral(unidadeMedida, XSDDatatype.XSDstring))
			);
			
		}
		
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
