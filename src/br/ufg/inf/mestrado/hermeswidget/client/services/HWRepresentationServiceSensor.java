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
	
	//private OntModel ontModelObservation;
	//private AllDifferent allDiff;
	
	public HWRepresentationServiceSensor() {
		
	}
	
	/*
	public HWRepresentationServiceSensor(OntModel ontModel) {
		ontModelObservation = ontModel;
	}
	*/
	
	public HWTransferObject startRepresentationSensor(String nomeModelo, String instanteMedidaColetada, String abreveaturaSinalVital, 
			int contadorSinalVital, String nomeClasseSinalVital, String medidaColetada, String[] medidaComposta, String unidadeMedida, String idPaciente) {
		
		criarModeloRDFDeArquivo("./mimic/modelos/"+nomeModelo);
		
		/*
		if (allDiff == null) {
			allDiff = modeloMedicaoSinalVital.createAllDifferent();
		}
		*/
		
		modeloMedicaoSinalVital = ModelFactory.createOntologyModel();
		
		//String[] sensorOutput = {"sensorOutput_"+nomeClasseSinalVital};
		String sensorOutput = "sensorOutput-"+nomeClasseSinalVital;
		String observationValue = "observationValue";
		
		Object[] values; 
		if (abreveaturaSinalVital == "PresSang") {
			Object[] v = medidaComposta;
			values = v;
		} else {
			Object[] v = {medidaColetada};
			values = v;
		}
		
		
		modeloMedicaoSinalVital = representObservation(abreveaturaSinalVital, "property-"+abreveaturaSinalVital, "sensor-"+nomeClasseSinalVital, sensorOutput, "entity-"+abreveaturaSinalVital, observationValue, values, unidadeMedida, idPaciente);
		
		
		
		if (contadorSinalVital == 0)
			modeloMedicaoSinalVital.write(System.out, "TURTLE");
		
		ByteArrayOutputStream baosContextoFiltrado = new ByteArrayOutputStream();
		modeloMedicaoSinalVital.write(baosContextoFiltrado, tipoSerializacao, caminhoSchemaOntologico);
		byte[] byteArray = baosContextoFiltrado.toByteArray();
		
		
		// Configura transfer object
		
		hermesWidgetTO = new HWTransferObject();
		
		// ALTERAÇÃO
		// hermesWidgetTO.setIdEntidade(idPaciente+"_"+nomeClasseSinalVital);
		hermesWidgetTO.setIdEntidade("person"+idPaciente);
		
		hermesWidgetTO.setNomeTopico(nomeClasseSinalVital);
		hermesWidgetTO.setComplementoTopico("");
		hermesWidgetTO.setContexto(byteArray);
		hermesWidgetTO.setCaminhoOntologia(caminhoSchemaOntologico);
		hermesWidgetTO.setTipoSerializacao(tipoSerializacao);
		if (abreveaturaSinalVital == "Temp") {
			hermesWidgetTO.setSensorValue(medidaColetada);
		} else {
			hermesWidgetTO.setSensorValue(medidaComposta[0]+" e "+medidaComposta[1]);
		}
		
		/* Depreciado
		Calendar calendarMedida = Calendar.getInstance();
		calendarMedida.setTime(instanteMedida);
		int segundosMinuto = calendarMedida.get(Calendar.MINUTE) * 60; 
		int instanteFinal = segundosMinuto + calendarMedida.get(Calendar.SECOND);
		*/
		return hermesWidgetTO;
	}
	
	private OntModel representObservation(String sinal, String property, String sensor,  String sensorOutput, String entity, String observationValue, Object[] values, String unidadeMedida, String feature) {
		/*
		Set<String> chaves = HermesWidgetObjects.getPacientes().keySet();
		for (String chave : chaves)
		{
			if(chave != null)
				System.out.println(chave + HermesWidgetObjects.getPacientes().get(chave));
		}
		*/
		
		//HermesWidgetObjects object = new HermesWidgetObjects();
		
		String featureIRI; // +"-"+ UUID.randomUUID().toString();
		/*
		if (!object.getObjects().isEmpty() && object.getObjects().containsKey(property)) {
			featureIRI = object.getObjects().get(property);
		} else {
			featureIRI = SSN.NS + feature +"-"+ UUID.randomUUID().toString();
			object.getObjects().put(feature, featureIRI);
		}*/
		// NOVO
		//featureIRI = SSN.NS + feature;
		featureIRI = IoT_Lite.NS + feature;
		
		String propertyIRI; // +"-"+ UUID.randomUUID().toString();
		/*
		if (!object.getObjects().isEmpty() && object.getObjects().containsKey(property)) {
			propertyIRI = object.getObjects().get(property);
		} else {
			propertyIRI = SSN.NS + property +"-"+ UUID.randomUUID().toString();
			object.getObjects().put(property, propertyIRI);
		}
		*/
		// NOVO
		//propertyIRI = SSN.NS + property;
		propertyIRI = IoT_Lite.NS + property;
		
		//String sensorIRI = SSN.NS + sensor +"-"+ UUID.randomUUID().toString();
		String sensorIRI = sensor +"-"+ UUID.randomUUID().toString();
		//String observationIRI = SSN.NS + observation +"-"+ UUID.randomUUID().toString();
		
		
		// IoT-Lite: COMENTAR DEPOIS DE FAZER AS ALTERAÇÕES -> 27/03
		/*Resource featureOfInterestResource = modeloMedicaoSinalVital
				.createResource(featureIRI)
					.addProperty(RDF.type, SSN.FeatureOfInterest);*/
					
		
		// IoT-Lite: ATUALIZAR A VARIÁVEL featureIRI -> 27/03
		
		/** Sistema */
		/*Resource systemResource = modeloMedicaoSinalVital
				.createResource(featureIRI)
					.addProperty(RDF.type, SSN.System);
					//.addProperty(RDF.type, SSN.FeatureOfInterest);*/
		
		
		/** Device */
		/*Resource deviceResource = modeloMedicaoSinalVital
				.createResource(featureIRI)
					.addProperty(RDF.type, SSN.Device);*/
		
		
		// MANTIDO
		/**
		 * An observable Quality of an Event or Object. That is, not a quality
		 * of an abstract entity as is also allowed by DUL's Quality, but rather
		 * an aspect of an entity that is intrinsic to and cannot exist without
		 * the entity and is observable by a sensor.
		 */
		/*Resource propertyResource = modeloMedicaoSinalVital
				.createResource(propertyIRI)
					.addProperty(RDF.type, SSN.Property);*/
		
		
		/** Sensor */
		Resource sensorResource = modeloMedicaoSinalVital
				.createResource(sensorIRI)
					.addProperty(RDF.type, SSN.Sensor)
					//.addProperty(SSN.observes, modeloMedicaoSinalVital
					//		.createResource(propertyIRI))
					//.addProperty(IoT_Lite.hasMetadata, "propertyIRI")
					//.addProperty(IoT_Lite.hasSensingDevice, featureIRI)
					.addProperty(IoT_Lite.hasUnit, "qu:degree_Celcius") // importar de qu
					.addProperty(IoT_Lite.hasQuantityKind, "qu:temperature"); // importar de qu
		
		/** Sensing Device INCLUIR */
		Resource sensingDeviceResource = modeloMedicaoSinalVital
				.createResource("device"+sinal)
					.addProperty(RDF.type, SSN.SensingDevice)
					.addProperty(IoT_Lite.exposedBy, sensorResource);
					//.addProperty(SSN.hasDeployment, "");
		
		
		/** Deployment */
		/*Resource deploymentResource = modeloMedicaoSinalVital
				.createResource(sensorIRI)
					.addProperty(RDF.type, SSN.Deployment)
					.addProperty(null, ""); // geo:hasLocation */
		
		
		/** Attribute INCLUIR */
		/*Resource attributeResource = modeloMedicaoSinalVital
				.createResource(sensorIRI)
					.addProperty(RDF.type, IoT_Lite.Attribute)
					.addProperty(IoT_Lite.isAssociatedWith, sensorResource)
					.addProperty(IoT_Lite.hasQuantityKind, "qu:temperature");*/
		
		
		//LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); 
		
		/** Point INCLUIR */
		Resource locationResource = modeloMedicaoSinalVital
				.createResource("location")
					.addProperty(RDF.type, "geo:Point")
					.addProperty(Geo.long_, "-49.255") // geo:long (usar LocationManager)
					.addProperty(Geo.lat, "-16.6799"); // geo:lat (usar LocationManager)
					//.addProperty(IoT_Lite.altRelative, "");
		
		
		/** Service INCLUIR */
		Resource serviceResource = modeloMedicaoSinalVital
				.createResource("service"+sinal)
					.addProperty(RDF.type, IoT_Lite.Service)
					.addProperty(IoT_Lite.endpoint, "http://www.ebserh.gov.br/web/hc-ufg/sensors/measures/room1"+"^^xsd:anyURI"); 
					//.addProperty(IoT_Lite.interfaceDescription, "");
					//.addProperty(RDF.type, ""); // ver aqui
		
		
		
		
		

		/**
		 * A sensor can do (implements) sensing: that is, a sensor is any entity
		 * that can follow a sensing method and thus observe some Property of a
		 * FeatureOfInterest. Sensors may be physical devices, computational
		 * methods, a laboratory setup with a person following a method, or any
		 * other thing that can follow a Sensing Method to observe a Property.
		 */
		/*Resource sensorResource = modeloMedicaoSinalVital
				.createResource(sensorIRI)
					.addProperty(RDF.type, SSN.SensingDevice)
					.addProperty(SSN.observes, propertyResource);
		*/
		
		//allDiff.addDistinctMember(sensorResource);

		/**
		 * A sensor outputs a piece of information (an observed value), the
		 * value itself being represented by an ObservationValue.
		 */
		
		//Resource[] sensorOutputResource = new Resource[sensorOutput.length];
		/*Resource sensorOutputResource;
		
		//for (int i = 0; i < values.length; i++) {
		sensorOutputResource = modeloMedicaoSinalVital
				.createResource(SSN.NS + sensorOutput +"-"+ UUID.randomUUID().toString())
					.addProperty(RDF.type, SSN.SensorOutput)
					.addProperty(SSN.isProducedBy, sensorResource);
		//}
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
		
		
		// Daqui pra baixo tem que ver!!
		
		
		//for (int i = 0; i < values.length; i++) {
		modeloMedicaoSinalVital.createResource(entity +"-"+ UUID.randomUUID().toString())
			.addProperty(RDF.type, IoT_Lite.Entity)
			.addProperty(IoT_Lite.hasSensingDevice, sensingDeviceResource)
			//.addProperty(IoT_Lite.has, attributeResource)
			.addProperty(Geo.location, locationResource)
			.addProperty(IoT_Lite.hasMetadata, serviceResource)
			.addProperty(SSN.observationResultTime, modeloMedicaoSinalVital.createTypedLiteral(dateTime.toString(), XSDDatatype.XSDdateTime));
		//}
		 
		
		/*
		if (sinal == "Temp") {
			sensorOutputResource.addProperty(SSN.hasValue, 
				modeloMedicaoSinalVital.createResource(SSN.NS + observationValue +"-"+ UUID.randomUUID().toString())
					.addProperty(RDF.type, SSN.ObservationValue)
					.addProperty(SSN.hasOutputValue, modeloMedicaoSinalVital.createTypedLiteral(values[0], XSDDatatype.XSDfloat))
					.addProperty(SSN.hasOutputUnit, modeloMedicaoSinalVital.createTypedLiteral(unidadeMedida, XSDDatatype.XSDstring))
			);
		} else if (sinal == "PresSang") {
			
			String uri = SSN.NS + observationValue +"-"+ UUID.randomUUID().toString();
			for (int i = 0; i < values.length; i++) {
				if (i == 0) {
					sensorOutputResource.addProperty(SSN.hasValue, 
						modeloMedicaoSinalVital.createResource(uri)
							.addProperty(RDF.type, SSN.ObservationValue)
							.addProperty(SSN.hasOutputValueAux, modeloMedicaoSinalVital.createTypedLiteral(values[i], XSDDatatype.XSDnonNegativeInteger))
							.addProperty(SSN.hasOutputUnit, modeloMedicaoSinalVital.createTypedLiteral(unidadeMedida, XSDDatatype.XSDstring))
					);
					
				}
				else {
					sensorOutputResource.addProperty(SSN.hasValue, 
						modeloMedicaoSinalVital.createResource(uri)
							.addProperty(RDF.type, SSN.ObservationValue)
							.addProperty(SSN.hasOutputValue, modeloMedicaoSinalVital.createTypedLiteral(values[i], XSDDatatype.XSDnonNegativeInteger))
							.addProperty(SSN.hasOutputUnit, modeloMedicaoSinalVital.createTypedLiteral(unidadeMedida, XSDDatatype.XSDstring))
					);
				}
				
			}
		} 	else {
//				String aux = String.valueOf(values[i]);
//				int aux2 = Integer.parseUnsignedInt(aux);
			
			//int aux = Integer.parseInt((String) values[i]);
			sensorOutputResource.addProperty(SSN.hasValue, 
				modeloMedicaoSinalVital.createResource(SSN.NS + observationValue +"-"+ UUID.randomUUID().toString())
					.addProperty(RDF.type, SSN.ObservationValue)
					.addProperty(SSN.hasOutputValue, modeloMedicaoSinalVital.createTypedLiteral(values[0], XSDDatatype.XSDnonNegativeInteger))
					.addProperty(SSN.hasOutputUnit, modeloMedicaoSinalVital.createTypedLiteral(unidadeMedida, XSDDatatype.XSDstring))
			);
			
		}
		*/
		
		return modeloMedicaoSinalVital;
	}
	
	/*
	public Resource getObservation() {
		return this.observationResource;
	}
	*/
	
	/*
	public static void main(String[] args) {
		
		OntModel ontModel = ModelFactory.createOntologyModel();
		HWRepresentationServiceSensor rs = new HWRepresentationServiceSensor(ontModel);
		
		String[] sensorOutputBloodPressure = {"SystolicBloodPressureSensorOutput", "DiastolicBloodPressureSensorOutput"};
		String[] observationValueBloodPressure = {"SystolicBloodPressureObservationValue", "DiastolicBloodPressureObservationValue"};
		
		
		Object[] valuesBloodPressure1 = {133, 84};
		ontModel = rs.representObservation("BloodPressure", "BloodPressureSensor001", sensorOutputBloodPressure, "BloodPressureObservation001", observationValueBloodPressure, valuesBloodPressure1, "teste");
		
		//ontModel.write(System.out, "TURTLE");
		
		//System.out.println("\n\n");

		
		rs = new HWRepresentationServiceSensor(ontModel);
		
		
		Object[] valuesBloodPressure2 = {120, 80};
		ontModel = rs.representObservation("BloodPressure", "BloodPressureSensor002", sensorOutputBloodPressure, "BloodPressureObservation002", observationValueBloodPressure, valuesBloodPressure2, "teste");
		
		ontModel.write(System.out, "TURTLE");
		
		System.out.println("\n\n");
		
	}
	*/
	
	public void setModeloMedicaoSinalVital(OntModel modeloMedicaoSinalVital) {
		this.modeloMedicaoSinalVital = modeloMedicaoSinalVital;
	}
	
	
	public HWTransferObject getDataTransferObject(String idPaciente, String nomeClasseSinalVital, String complementoTopico, byte[] medidaByteArray, String valorMedidaColetada, String instanteMedidaColetada) {
		
		hermesWidgetTO = new HWTransferObject();
		
		// ALTERAÇÃO
		// hermesWidgetTO.setIdEntidade(idPaciente+"_"+nomeClasseSinalVital);
		hermesWidgetTO.setIdEntidade("person"+idPaciente);
		
		hermesWidgetTO.setNomeTopico(nomeClasseSinalVital);
		hermesWidgetTO.setComplementoTopico(complementoTopico);
		hermesWidgetTO.setContexto(medidaByteArray);
		hermesWidgetTO.setCaminhoOntologia(caminhoSchemaOntologico);
		hermesWidgetTO.setTipoSerializacao(tipoSerializacao);
		hermesWidgetTO.setSensorValue(valorMedidaColetada);
		
		/* Depreciado
		Calendar calendarMedida = Calendar.getInstance();
		calendarMedida.setTime(instanteMedida);
		int segundosMinuto = calendarMedida.get(Calendar.MINUTE) * 60; 
		int instanteFinal = segundosMinuto + calendarMedida.get(Calendar.SECOND);
		*/
		return hermesWidgetTO;
		
//		threadPoolMedidas.schedule(new HermesWidgetThreadMeasurementNotification(hermesBaseManager, hermesWidgetTO), instanteFinal, TimeUnit.MINUTES);
	}

}
