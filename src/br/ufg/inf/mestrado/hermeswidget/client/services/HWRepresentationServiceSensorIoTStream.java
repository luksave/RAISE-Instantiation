package br.ufg.inf.mestrado.hermeswidget.client.services;


import java.io.ByteArrayOutputStream;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONException;

import br.ufg.inf.mestrado.hemeswidget.client.semanticEnrichment.SemanticEnrichment;
import br.ufg.inf.mestrado.hermeswidget.client.preprocessing.CarbonDioxidePreprocessing;
import br.ufg.inf.mestrado.hermeswidget.client.utils.EnvironmentQuery;
import br.ufg.inf.mestrado.hermeswidget.client.utils.PersistenceTDB;
import br.ufg.inf.mestrado.hermeswidget.manager.transferObject.HWTransferObject;
import br.ufg.inf.mestrado.hermeswidget.ontologies.AQO3;
import br.ufg.inf.mestrado.hermeswidget.ontologies.Geo;
import br.ufg.inf.mestrado.hermeswidget.ontologies.IoTStream;
import br.ufg.inf.mestrado.hermeswidget.ontologies.IoT_Lite;
import br.ufg.inf.mestrado.hermeswidget.ontologies.QUDT;
import br.ufg.inf.mestrado.hermeswidget.ontologies.SOSA;
import br.ufg.inf.mestrado.hermeswidget.ontologies.SSN;
import br.ufg.inf.mestrado.hermeswidget.ontologies.SymptomAssociation;
import br.ufg.inf.mestrado.hermeswidget.ontologies.Unit;
import br.ufg.inf.mestrado.hermeswidget.testes.InicializarHWairpure;


import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;


public class HWRepresentationServiceSensorIoTStream extends HWRepresentationService{

	private HWTransferObject hermesWidgetTO = null;
	
	public static PersistenceTDB tdb = new PersistenceTDB();

	public HWRepresentationServiceSensorIoTStream() {}
	
	
	public static PersistenceTDB getTDB(){
		return HWRepresentationServiceSensorIoTStream.tdb;
		
	}
	
	
	public HWTransferObject startRepresentationSensor(
			String sensorIRI, String nomeModelo, String abreviaturaDadoAmbiental, 
			int contador, String nomeClasseDadoAmbiental, 
			String medidaColetada, String[] medidaComposta, 
			String idAmbiente, String instanteColeta, Individual unit, Individual quantity) throws JSONException, IOException {
		
		
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
	
	
	private OntModel representObservation
	(String sinal, String property, String sensorIRI, String entity, String sensorOutput, String observationValue, Object[] values, 
	 String feature, String dateTimeID, Individual unit, Individual quantity) throws JSONException, IOException {
		
		
		String propertyIRI = IoTStream.NS + property;
		String uriData     = dateTimeID.substring(00, 10) + "-" + dateTimeID.substring(11, 13) + "-" 
		                   + dateTimeID.substring(14, 16) + "-" + dateTimeID.substring(17, 19);
		
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
		LocalDateTime actualTime = LocalDateTime.parse(uriData, format);
		
		
		//Aqui a localiza��o onde a stream foi observada � capturada (geo:Point)
		Resource locationResource = modeloMedicaoDadoAmbiental
				.createResource("http://www.inf.ufg.br/Air-Pure/location")
					.addProperty(RDF.type, Geo.point)
					.addProperty(Geo.long_, "-49.255") 
					.addProperty(Geo.lat, "-16.6799"); 
		
		//Levando em conta que cada ambiente tem seu sensing device
		Resource AirPureSala1 = modeloMedicaoDadoAmbiental
				.createResource("http://www.inf.ufg.br/Air-Pure/service/" + "INF0001")
				.addProperty(RDF.type, SSN.SensingDevice)
				.addProperty(Geo.location, locationResource);
		
		//O conjunto de todos Air-Pures em um pr�dio � o sistema de AIR-PURE
		Resource airPureINF = modeloMedicaoDadoAmbiental
				.createResource("http://www.inf.ufg.br/Air-Pure/service/" + "SALASINF")
				.addProperty(RDF.type, SSN.System)
				.addProperty(IoT_Lite.hasSubSystem, AirPureSala1);		
		
		//O Air-Pure de cada sala � um subsistema do sistema AIR-PURE
		AirPureSala1.addProperty(IoT_Lite.isSubSystemOf, airPureINF);
		
		
		/**
		 * Uma IoTStream � um fluxo de dados
		 * Cada Fluxo de dados � gerado por um sensor <Sensor>
		 * Al�m de ser provida por um Servi�o 
		 */
		Resource streamResource = modeloMedicaoDadoAmbiental
				.createResource(propertyIRI + uriData)
					.addProperty(RDF.type, IoTStream.IotStream);
		
		
		/**
		 * Uma StreamObservation � um conjunto de valores correspondentes a uma observa��o. 
		 * Resultados s�o capturados por <sensores> e 
		 * 
		 */
		Resource StreamObservation = modeloMedicaoDadoAmbiental
				.createResource(IoTStream.NS + sensorOutput +"-"+ uriData)
					.addProperty(RDF.type, IoTStream.StreamObservation);
		
		
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
					.addProperty(QUDT.hasQuantityKind, quantity)
					.addProperty(IoT_Lite.hasSensingDevice, AirPureSala1);	
		
		
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
			.addProperty(SOSA.hasSimpleResult, values[0].toString(), XSDDatatype.XSDdouble)
			.addProperty(SOSA.resultTime, dateTimeID, XSDDatatype.XSDdateTime);
		

		
		/**
		 * A concentra��o de Di�xido de Carbono � o valor utilizado para aferir a qualidade do ar interno,
		 * pois n�o temos outros poluentes...
		 * Normalmente devemos nos preocupar se essa concentra��o ultrapassa 1000ppm
		 * 
		 * Aqui crio o poluente CO2 relacionado � observa��o
		 * Com essa informa��o posso resgatar as observa��es desejadas, calcular a m�dia de acordo com a periodicidade e 
		 * por fim usar essa informa��o para calcular uma qualidade do ar.
		 * 
		 */
		
		if(sinal == "ConCO2") {
			
			/**
			 * Um poluente possui uma periodicidade <hasPeriodicity> e a informa��o de tipo, natural ou antropog�nico
			 * <subClassOf> 
			 */
			Resource PollutantResource = modeloMedicaoDadoAmbiental
					.createResource(AQO3.NS + sinal)
						.addProperty(RDF.type, AQO3.CO2)
						.addProperty(AQO3.hasPeriodicity, "1")
						.addProperty(RDFS.subClassOf, AQO3.Natural_pollutant);
						
					
			/**
			 * Cada medida de qualidade do ar <AQ_Measurement_Resource> de um determinado poluente � o seu valor de qualidade do ar
			 * calculado de acordo com a f�rmula da regulamenta��o (ou mesmo a criada) usada em <IndicatorCategorization>.
			 * 
			 * Assim, para cada medida de concentra��o de CO2, usamos o processamento abaixo para calcular o valor da faixa de 
			 * qualidade do ar para uso no recurso AQ_Measurement_Resource.
			 * 
			 */
			
			//Recupero o valor aferido como string e fa�o parsing em double para c�lculo da faixa de qualidade do ar
			//String IQA = CarbonDioxidePreprocessing.IQACarbonDioxide(Double.parseDouble(values[0].toString()));
			
			/** 
			 * Measurement � o ponto de liga��o com a ontologia IoT-Stream.
			 * Os dados capturados por esta classe est�o de acordo com o proposto na ontologia SOSA para definir observa��es de fluxos. 
			 *
			 * Usado apenas para poluentes!
			 * 
			 * Dados como temperatura e umidade n�o s�o representados aqui.
			 */
			@SuppressWarnings("unused")
			Resource measureResource = modeloMedicaoDadoAmbiental
					.createResource(IoTStream.NS + sensorOutput +"-Measurement-"+ uriData)
						.addProperty(RDF.type, AQO3.AQ_Measurement)
						.addProperty(AQO3.hasMeasurementOf, PollutantResource)
						.addProperty(AQO3.takesPlaceAt, locationResource)
						.addProperty(AQO3.hasHappenedOn, dateTimeID, XSDDatatype.XSDdateTime)
						.addProperty(SOSA.hasSimpleResult, values[0].toString(), XSDDatatype.XSDdouble)
						.addProperty(IoTStream.hasUnit, Unit.PPM);
			
	
			/**
			 * A classe que representa o indicator de qualidade do ar dos poluentes � a AQ_User_Indicator.
			 * 
			 * Cada inst�ncia da classe AQ_User_Indicator � um indicador para um poluente, possuindo as informa��es de:
			 * 
			 * Poluente relacionado   <isIndicatorFor>
			 * Periodicidade 		  <hasPeriodicity>
			 * Forma de agrega��o 	  <isDerivedBy>
			 * Categoria de qualidade <isCategorizedBy>
			 * 
			 * Assim, cada indicator � do tipo AQ_User_Indicator, calculado atrav�s de uma forma de deriva��o <isDerivedBy> 
			 * em um per�odo de tempo determinado <hasPeriodicity> e � categorizado por faixas de categorias
			 * 
			 * Exemplo: Se a m�dia das medidas de concentra��o de CO2 na �ltima hora � equivalente a um valor de 65.0, ap�s o c�lculo da faixa,
			 * a inst�ncia de indicador de qualidade do ar para CO2 <AQ_User_Indicator> possui as seguintes informa��es:
			 * 
			 * isIndicatorFor: CO2
			 * hasPeriodicity: 1
			 * isDerivedBy: Average
			 * isCategorizedBy: IC_Regular
			 * 
			 * Isto �, a qualidade do ar de acordo com o poluente CO2 � a m�dia das medidas da faixa no per�odo de uma hora.
			 * 
			 * A regra de associa��o entre um indicador e sua categoria � dada por: 
			 * Equivalent To (isIndicatorFor value <Poluente>) and (hasAvgConc some (xsd:decimal >= <lowerLimit> and xsd:decimal <= <upperLimit>))
			 * 
			 * Essa classe somente � instanciada a cada hora, isto �, cada v�z que a m�dia de concentra��o � recalculada.
			 * 
			 */
			
			
			/**
			 * A ontologia AQOnto n�o tem uma quest�o de compet�ncia que traz a necessidade de armazenar o instante de tempo em que uma qualidade
			 * do ar foi calculada. Aparentemente, a �nica quest�o de compet�ncia ligada a informa��o de tempo � 
			 * (Q5) Qual a categoriza��o para o indicador <R> na periodicidade <H>?  
			 * 
			 * A  sequ�ncia  para  o  c�lculo  do  �ndice  utilizando esta ontologia executa os seguintes passos: 
			 * 
			 */
			
			int Periodicity=1;
			
			//Um novo indicador � requisitado a cada hora.
			if(actualTime.isAfter(InicializarHWairpure.lastConcAvgTime.plusHours(Periodicity))) {
				InicializarHWairpure.lastConcAvgTime = actualTime;
				
				/**
				 *  1 - A consulta das m�dias � feita e a categoria certa � adicionada ao recurso
				 *  2 - Um novo indicador � requisitado
				 *  3 - Defini��o de Periodicidade, Poluente e Forma de Deriva��o
				 */
				Resource AQIndicatorResource = modeloMedicaoDadoAmbiental
						.createResource(AQO3.NS + "IndicatorFor" + sinal + "-" + actualTime)
							.addProperty(RDF.type, AQO3.AQ_Indicator)
							.addProperty(AQO3.hasPeriodicity, "1", XSDDatatype.XSDinteger)
							.addProperty(AQO3.isIndicatorFor, AQO3.CO2)
							.addProperty(AQO3.isDerivedBy, AQO3.Average);
				
				tdb.update(modeloMedicaoDadoAmbiental);
				
				/**	4 - Consulta levando em conta periodicidade, selecionando medidas do poluente, e aplicando a forma de agrega��o definida.
				 * 	Aqui posso aumentar ou diminuir a janela de tempo conforme necess�rio. 
				 * 	Basta trocar o par�metro da consulta
				 */
				
				int janelaTempo = 1;
				AQIndicatorResource.addProperty(SymptomAssociation.hasTimeWindow, Integer.toString(janelaTempo), XSDDatatype.XSDinteger);
				
				Double AverageCO2 = EnvironmentQuery.getMedia(actualTime.minusHours(janelaTempo).toString(), actualTime.toString());
		
				
				/** 5 - Aplica��o recebe o valor da concentra��o para o indicador e enquadra-o na categoria que o abrange.*/
				
				AQIndicatorResource
					.addProperty(AQO3.hasAvgConc, AverageCO2.toString(), XSDDatatype.XSDdouble)
					.addProperty(IoTStream.windowStart, actualTime.minusHours(janelaTempo).toString(), XSDDatatype.XSDtime)
					.addProperty(IoTStream.windowEnd, actualTime.toString(), XSDDatatype.XSDtime);
				
				
				String enrichmentURI = sinal + "-" + actualTime;
							
	
				
				/**
				 * 6 - Aplica��o  utiliza  o  mesmo  valor  para  enquadr�-lo  nas  categorias  do  �ndice, obtendo assim sua categoriza��o:
				 */
				
				//Recupero o valor aferido como string e fa�o parsing em double para c�lculo da faixa de qualidade do ar
				int IQA = CarbonDioxidePreprocessing.IQACarbonDioxide(AverageCO2);
				
				
				@SuppressWarnings("unused")
				Resource AQIndexResource = modeloMedicaoDadoAmbiental
						.createResource(AQO3.NS + "AQIndex/" + sinal + "-" + actualTime)
							.addProperty(RDF.type, AQO3.AQ_Index)
							.addProperty(AQO3.isIndexFor, AQO3.CO2)
							.addProperty(AQO3.isDerivedBy, AQO3.Average)
							.addProperty(AQO3.hasIndexValue, Integer.toString(IQA), XSDDatatype.XSDdouble)
							.addProperty(SymptomAssociation.hasTimeWindow, Integer.toString(janelaTempo), XSDDatatype.XSDinteger);				

				//Chamo 
				modeloMedicaoDadoAmbiental = SymptomAssociation.addSymptoms(modeloMedicaoDadoAmbiental);
				
				modeloMedicaoDadoAmbiental = SemanticEnrichment.enrichModelWithIQA(modeloMedicaoDadoAmbiental, enrichmentURI);
				
				//modeloMedicaoDadoAmbiental = 
				//SemanticEnrichment.enrichModelSWRL(modeloMedicaoDadoAmbiental, enrichmentURI);
				modeloMedicaoDadoAmbiental = SemanticEnrichment.enrichModelWithMTCS(modeloMedicaoDadoAmbiental, enrichmentURI);
				
				
				//Impress�o do novo modelo
				modeloMedicaoDadoAmbiental.write(System.out, "TURTLE");
				
				
			}
			
		}	
		
		/**
		 * Caso seja necess�rio imprimir o modelo
		 * IMPORTANTE: Comentar ap�s uso, ou a sa�da fica impratic�vel
		 * Op��es: TURTLE, N-TRIPLES, RDF/XML
		 */
		//modeloMedicaoDadoAmbiental.write(System.out, "N-TRIPLES");
		
		tdb.update(modeloMedicaoDadoAmbiental);
		
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
