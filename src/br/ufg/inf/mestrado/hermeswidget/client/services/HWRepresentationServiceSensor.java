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
import br.ufg.inf.mestrado.hermeswidget.ontologies.QuantityKind;
import br.ufg.inf.mestrado.hermeswidget.ontologies.SSN;
import br.ufg.inf.mestrado.hermeswidget.ontologies.QUDT;
import br.ufg.inf.mestrado.hermeswidget.ontologies.Unit;

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
			                                          String idAmbiente,
			                                          String dataTempo) {
		
		criarModeloRDFDeArquivo("./mimic/modelos/"+nomeModelo);
		modeloMedicaoDadoAmbiental = ModelFactory.createOntologyModel();
		String sensorOutput = "sensorOutput-"+nomeClasseDadoAmbiental;
		String observationValue = "observationValue"+medidaColetada;
		
		Object[] values; 
	
		Object[] v = {medidaColetada};
		values = v;
			
		modeloMedicaoDadoAmbiental = representObservation(abreviaturaDadoAmbiental, 
				                                          "property-"+abreviaturaDadoAmbiental, 
				                                          "sensor-"  +nomeClasseDadoAmbiental, sensorOutput, 
				                                          "entity-"  +abreviaturaDadoAmbiental, observationValue, 
				                                                      values, unidadeMedida, idAmbiente);
	
		
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
	
	private OntModel representObservation(String sinal,  String property, String sensor, String sensorOutput, 
			                              String entity, String observationValue, Object[] values, String unidadeMedida, String feature) {
		
		String sensorIRI = sensor +"-"+ UUID.randomUUID().toString();
		
		String propertyIRI = IoT_Lite.NS + property;
		
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
		
		
		//allDiff.addDistinctMember(sensorResource);
		
		/* Cálculo do IQA
		 * 1 - Os valores coletados  - e modelados nos widgets - que compõem o IQA são
		 * aqueles que são inaláveis. (VOCS e CO2)
		 * 2 - O cálculo da concentração média é feita levando em conta períodos de 24hrs, 
		 * 8hrs ou 1hr, de acordo com o poluente.
		 * 3 - O índice é calculado individualmente para cada um dos poluentes. O resultado 
		 * final do IQA é o pior valor calculado dentre os poluente (aquele com o maior resultado)
		 */
		
		//índice (inicial) = valor do índice de qualidade do ar mínimo que corresponde à concentração inicial da faixa;
		int inicialCO2 = 0;
		int inicialVOC = 0;
		
		//índice (final) = valor do índice de qualidade do ar maximo que corresponde à concentração final da faixa;
		int finalCO2 = 0;
		int finalVOC = 0;		
		
		//Conc. (medida) = concentração medida;
		//Valor atualizado dentro do if (sinal)
		int medidaCO2 = 0;//Atualizado em if(sinal == "ConCO2")
		int medidaVOC = 0;//Atualizado em if(sinal == "TVOC")
		
		//Conc. (inicial e final) = concentração inicial/final da faixa onde se localiza a concentração medida.
		//*Cada poluente tem uma
		//VALORES OBTIDOS DA REGULAÇÃO ANVISA, 2003
		
		//CO2 medido em ppm
		int inicialFaixaCO2 = 400;
		int finalFaixaCO2   = 1000;
		
		//TVOCS medido em ppb
		int inicialFaixaVOC = 100;
		int finalFaixaVOC   = 250;
		
		//O valor do IQA final é o maior entre IQACO2 e IQAVOC
		double IQACO2;
		double IQAVOC;
	
		//if(IQACO2 > IQA) IQA = IQACO2;
		//if(IQAVOC > IQA) IQA = IQAVOC;
		
		if(unidadeMedida == "ppm"){
			/** Sensor */
			sensorResource = modeloMedicaoDadoAmbiental
				.createResource(sensorIRI)
					.addProperty(RDF.type, SSN.Sensor)
					.addProperty(QUDT.hasUnit, Unit.PPM)
					.addProperty(QUDT.hasQuantityKind, QuantityKind.CO2Concentration);
		}
		if(unidadeMedida == "ppb"){
			/** Sensor */
			sensorResource = modeloMedicaoDadoAmbiental
				.createResource(sensorIRI)
					.addProperty(RDF.type, SSN.Sensor)
					.addProperty(QUDT.hasUnit, Unit.PPB)
					.addProperty(QUDT.hasQuantityKind, QuantityKind.TVOC);
		}
		if(unidadeMedida == "%"){
			/** Sensor */
			sensorResource = modeloMedicaoDadoAmbiental
				.createResource(sensorIRI)
					.addProperty(RDF.type, SSN.Sensor)
					.addProperty(QUDT.hasUnit, Unit.Percent)
					.addProperty(QUDT.hasQuantityKind, QuantityKind.RelativeHumidity);
		}
		if(unidadeMedida == "Celsius"){
			/** Sensor */
				sensorResource = modeloMedicaoDadoAmbiental
					.createResource(sensorIRI)
						.addProperty(RDF.type, SSN.Sensor)
						.addProperty(QUDT.hasUnit, Unit.DEG_C)
						.addProperty(QUDT.hasQuantityKind, QuantityKind.CelsiusTemperature);
			
		}
	
		
		
		/**
		 * Um sensor dá como saída um pedaço de informação (valor observado), o valor
		 * é representado por um ObservationValue.
		 */
		
		Resource sensorOutputResource;
		
		sensorOutputResource = modeloMedicaoDadoAmbiental
				.createResource(SSN.NS + sensorOutput +"-"+ UUID.randomUUID().toString())
					.addProperty(RDF.type, SSN.SensorOutput)
					.addProperty(SSN.isProducedBy, sensorResource);
	
		
		 
		
		
		/** Sensing Device INCLUIR */
		Resource sensingDeviceResource = modeloMedicaoDadoAmbiental
				.createResource("device"+sinal)
					.addProperty(RDF.type, SSN.SensingDevice)
					.addProperty(IoT_Lite.exposedBy, sensorResource);
		
		/** Point INCLUIR */
		Resource locationResource = modeloMedicaoDadoAmbiental
				.createResource("location")
					.addProperty(RDF.type, Geo.point)
					.addProperty(Geo.long_, "-49.255") 
					.addProperty(Geo.lat, "-16.6799"); 
					
		
		/** Service INCLUIR */
		Resource serviceResource = modeloMedicaoDadoAmbiental
				.createResource("service"+sinal)
					.addProperty(RDF.type, IoT_Lite.Service)
					.addProperty(IoT_Lite.endpoint, "http://www.ebserh.gov.br/web/hc-ufg/sensors/measures/room1"+"^^xsd:anyURI"); 
	
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

		
		
		if (sinal == "ConCO2") {
			medidaCO2 = Integer.parseInt(observationValue);
			IQACO2    = inicialCO2 + ((finalCO2-inicialCO2)/(finalFaixaCO2-inicialFaixaCO2)) * (medidaCO2 - inicialFaixaCO2);
	
			sensorOutputResource.addProperty(SSN.hasValue, 
				modeloMedicaoDadoAmbiental.createResource(SSN.NS + observationValue +"-"+ UUID.randomUUID().toString())
					.addProperty(RDF.type, SSN.ObservationValue)
					.addProperty(SSN.hasOutputValue, modeloMedicaoDadoAmbiental.createTypedLiteral(values[0], XSDDatatype.XSDfloat))
					.addProperty(SSN.hasOutputUnit, modeloMedicaoDadoAmbiental.createTypedLiteral(unidadeMedida, XSDDatatype.XSDstring))
			);
			
			sensorOutputResource.addProperty(SSN.hasValue, 
					modeloMedicaoDadoAmbiental.createResource(SSN.NS + observationValue +"-IQACO2-"+ UUID.randomUUID().toString())
						.addProperty(RDF.type, SSN.ObservationValue)
						.addProperty(SSN.hasOutputValueAux, modeloMedicaoDadoAmbiental.createTypedLiteral(IQACO2, XSDDatatype.XSDfloat))
						.addProperty(SSN.hasOutputUnit, modeloMedicaoDadoAmbiental.createTypedLiteral("integer", XSDDatatype.XSDstring))
				);
			
		}else if (sinal == "TVOC") {
			medidaVOC = Integer.parseInt(observationValue);
			IQAVOC    = inicialVOC + ((finalVOC-inicialVOC)/(finalFaixaVOC-inicialFaixaVOC)) * (medidaVOC - inicialFaixaVOC);
			
			sensorOutputResource.addProperty(SSN.hasValue, 
				modeloMedicaoDadoAmbiental.createResource(SSN.NS + observationValue +"-"+ UUID.randomUUID().toString())
					.addProperty(RDF.type, SSN.ObservationValue)
					.addProperty(SSN.hasOutputValue, modeloMedicaoDadoAmbiental.createTypedLiteral(values[0], XSDDatatype.XSDfloat))
					.addProperty(SSN.hasOutputUnit, modeloMedicaoDadoAmbiental.createTypedLiteral(unidadeMedida, XSDDatatype.XSDstring))
			);
			
			sensorOutputResource.addProperty(SSN.hasValue, 
					modeloMedicaoDadoAmbiental.createResource(SSN.NS + observationValue +"-IQAVOC-"+ UUID.randomUUID().toString())
						.addProperty(RDF.type, SSN.ObservationValue)
						.addProperty(SSN.hasOutputValueAux, modeloMedicaoDadoAmbiental.createTypedLiteral(IQAVOC, XSDDatatype.XSDfloat))
						.addProperty(SSN.hasOutputUnit, modeloMedicaoDadoAmbiental.createTypedLiteral("integer", XSDDatatype.XSDstring))
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