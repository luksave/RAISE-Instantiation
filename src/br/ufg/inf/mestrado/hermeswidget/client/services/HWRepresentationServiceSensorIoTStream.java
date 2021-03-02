package br.ufg.inf.mestrado.hermeswidget.client.services;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ListIterator;

import org.json.JSONException;

import br.ufg.inf.mestrado.hermeswidget.client.utils.ConsultaMedia;
import br.ufg.inf.mestrado.hermeswidget.client.utils.ConsultaSintomas;
import br.ufg.inf.mestrado.hermeswidget.client.utils.PersistenceTDB;
import br.ufg.inf.mestrado.hermeswidget.manager.transferObject.HWTransferObject;
import br.ufg.inf.mestrado.hermeswidget.ontologies.AQO3;
import br.ufg.inf.mestrado.hermeswidget.ontologies.Geo;
import br.ufg.inf.mestrado.hermeswidget.ontologies.IoTStream;
import br.ufg.inf.mestrado.hermeswidget.ontologies.IoT_Lite;
import br.ufg.inf.mestrado.hermeswidget.ontologies.QUDT;
import br.ufg.inf.mestrado.hermeswidget.ontologies.SOSA;
import br.ufg.inf.mestrado.hermeswidget.ontologies.SSN;
import br.ufg.inf.mestrado.hermeswidget.ontologies.Unit;
import br.ufg.inf.mestrado.hermeswidget.ontologies.symp;
import br.ufg.inf.mestrado.hermeswidget.testes.InicializarHWairpure;


import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;


public class HWRepresentationServiceSensorIoTStream extends HWRepresentationService{

	private HWTransferObject hermesWidgetTO = null;
	
	public static PersistenceTDB tdb = new PersistenceTDB();
	
	public static int checkLastTime = 0;

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
	
	private OntModel representObservation(String sinal, String property, String sensorIRI, String entity, String sensorOutput, String observationValue, Object[] values, 
			                              String feature, String dateTimeID, Individual unit, Individual quantity) throws JSONException, IOException {
		
		
		String propertyIRI = IoTStream.NS + property;
		String uriData     = dateTimeID.substring(00, 10) + "-" + dateTimeID.substring(11, 13) + "-" 
		                   + dateTimeID.substring(14, 16) + "-" + dateTimeID.substring(17, 19);
		
		
		LocalDateTime actualTime = LocalDateTime.parse(dateTimeID.substring(0, dateTimeID.length() - 1));
		
		//Aqui a localização onde a stream foi observada é capturada (geo:Point)
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
		
		//O conjunto de todos Air-Pures em um prédio é o sistema de AIR-PURE
		Resource airPureINF = modeloMedicaoDadoAmbiental
				.createResource("http://www.inf.ufg.br/Air-Pure/service/" + "SALASINF")
				.addProperty(RDF.type, SSN.System)
				.addProperty(IoT_Lite.hasSubSystem, AirPureSala1);		
		
		//O Air-Pure de cada sala é um subsistema do sistema AIR-PURE
		AirPureSala1.addProperty(IoT_Lite.isSubSystemOf, airPureINF);
		
		
		/**
		 * Uma IoTStream é um fluxo de dados
		 * Cada Fluxo de dados é gerado por um sensor <Sensor>
		 * Além de ser provida por um Serviço 
		 */
		Resource streamResource = modeloMedicaoDadoAmbiental
				.createResource(propertyIRI + uriData)
					.addProperty(RDF.type, IoTStream.IotStream);
		
		
		/**
		 * Uma StreamObservation é um conjunto de valores correspondentes a uma observação. 
		 * Resultados são capturados por <sensores> e 
		 * 
		 */
		Resource StreamObservation = modeloMedicaoDadoAmbiental
				.createResource(IoTStream.NS + sensorOutput +"-"+ uriData)
					.addProperty(RDF.type, IoTStream.StreamObservation);
		
		
		/**Aqui um recurso sensor é criado de acordo com o que temos na ontologia IoT-Stream
		 * Ele é do tipo sosa.Sensor
		 * possui um tipo de quantidade e unidade, definidos logo abaixo de acordo com o tipo de dado ambiental
		 * Importante: Esse sensor realiza uma StreamObservation que pertence a uma IotStream
		 */
		Resource sensorResource = modeloMedicaoDadoAmbiental
				.createResource(sensorIRI)
					.addProperty(RDF.type, SOSA.Sensor)
					.addProperty(SOSA.madeObservation, StreamObservation)
					.addProperty(IoT_Lite.hasSensingDevice, AirPureSala1)//Isso deve ser mudado pelo identificador único do dispositivo. Verificando com a Bruna
					.addProperty(QUDT.hasUnit, unit)
					.addProperty(QUDT.hasQuantityKind, quantity)
					.addProperty(IoT_Lite.hasSensingDevice, AirPureSala1);	
		
		
		/**
		 * Um sensor dá como saída um pedaço de informação (valor observado), o valor
		 * é representado por uma StreamObservation. Aqui é realizada a atribuição do 
		 * valor observado e do momento em que foi observado
		 */
		//Aqui o serviço que provê a IotStream é instanciado
		Resource serviceResource = modeloMedicaoDadoAmbiental
				.createResource("http://www.inf.ufg.br/Air-Pure/service/" + sinal)
					.addProperty(RDF.type, IoT_Lite.Service)
					.addProperty(IoT_Lite.endpoint, "http://www.ebserh.gov.br/web/hc-ufg/sensors/measures/room1"+"^^xsd:anyURI"); 
		
		
		// Aqui uma IotStream é criada com a atribuição das classes relacionadas
		streamResource
			.addProperty(IoTStream.generatedBy, sensorResource)
			.addProperty(IoTStream.providedBy, serviceResource);
		
		//Aqui uma StreamObservation é atribuída ao sensor que a gera e à IotStream à qual pertence
		StreamObservation
			.addProperty(SOSA.madeBySensor, sensorResource)
			.addProperty(IoTStream.belongsTo, streamResource)
			.addProperty(SOSA.hasSimpleResult, values[0].toString(), XSDDatatype.XSDdouble)
			.addProperty(SOSA.resultTime, dateTimeID, XSDDatatype.XSDdateTime);
		

		
		/**
		 * A concentração de Dióxido de Carbono é o valor utilizado para aferir a qualidade do ar interno,
		 * pois não temos outros poluentes...
		 * Normalmente devemos nos preocupar se essa concentração ultrapassa 1000ppm
		 * 
		 * Aqui crio o poluente CO2 relacionado à observação
		 * Com essa informação posso resgatar as observações desejadas, calcular a média de acordo com a periodicidade e 
		 * por fim usar essa informação para calcular uma qualidade do ar.
		 * 
		 */
		
		if(sinal == "ConCO2") {
			
			/**
			 * Um poluente possui uma periodicidade <hasPeriodicity> e a informação de tipo, natural ou antropogênico
			 * <subClassOf> 
			 */
			Resource PollutantResource = modeloMedicaoDadoAmbiental
					.createResource(AQO3.NS + sinal)
						.addProperty(RDF.type, AQO3.CO2)
						.addProperty(AQO3.hasPeriodicity, "1")
						.addProperty(RDFS.subClassOf, AQO3.Natural_pollutant);
						
					
			/**
			 * Cada medida de qualidade do ar <AQ_Measurement_Resource> de um determinado poluente é o seu valor de qualidade do ar
			 * calculado de acordo com a fórmula da regulamentação (ou mesmo a criada) usada em <IndicatorCategorization>.
			 * 
			 * Assim, para cada medida de concentração de CO2, usamos o processamento abaixo para calcular o valor da faixa de 
			 * qualidade do ar para uso no recurso AQ_Measurement_Resource.
			 * 
			 */
			
			//Recupero o valor aferido como string e faço parsing em double para cálculo da faixa de qualidade do ar
			//String IQA = CarbonDioxidePreprocessing.IQACarbonDioxide(Double.parseDouble(values[0].toString()));
			
			/** 
			 * Measurement é o ponto de ligação com a ontologia IoT-Stream.
			 * Os dados capturados por esta classe estão de acordo com o proposto na ontologia SOSA para definir observações de fluxos. 
			 * 
			 */
			Resource measureResource = modeloMedicaoDadoAmbiental
					.createResource(IoTStream.NS + sensorOutput +"-Measurement-"+ uriData)
						.addProperty(RDF.type, AQO3.AQ_Measurement)
						.addProperty(AQO3.hasMeasurementOf, PollutantResource)
						.addProperty(AQO3.takesPlaceAt, locationResource)
						.addProperty(AQO3.hasHappenedOn, dateTimeID, XSDDatatype.XSDdateTime)
						.addProperty(SOSA.hasSimpleResult, values[0].toString(), XSDDatatype.XSDdouble)
						.addProperty(IoTStream.hasUnit, Unit.PPM);
			
			StreamObservation.addProperty(OWL.sameAs, measureResource); 
			
			
			/**
			 * A classe que representa o indicator de qualidade do ar dos poluentes é a AQ_User_Indicator.
			 * 
			 * Cada instância da classe AQ_User_Indicator é um indicador para um poluente, possuindo as informações de:
			 * 
			 * Poluente relacionado   <isIndicatorFor>
			 * Periodicidade 		  <hasPeriodicity>
			 * Forma de cálculo 	  <isDerivedBy>
			 * Categoria de qualidade <isCategorizedBy>
			 * 
			 * Assim, cada indicator calculado é do tipo AQ_User_Indicator, calculado através de uma forma de derivação <isDerivedBy> 
			 * em um período de tempo determinado <hasPeriodicity> e é categorizado por faixas de categorias
			 * 
			 * Exemplo: Se a média das medidas de concentração de CO2 na última hora é equivalente a um valor de 65.0, após o cálculo da faixa,
			 * a instância de indicador de qualidade do ar para CO2 <AQ_User_Indicator> possui as seguintes informações:
			 * 
			 * isIndicatorFor: CO2
			 * hasPeriodicity: 1
			 * isDerivedBy: Average
			 * isCategorizedBy: IC_Regular
			 * 
			 * Isto é, a qualidade do ar de acordo com o poluente CO2 é a média das medidas da faixa no período de uma hora.
			 * 
			 * A regra de associação entre um indicador e sua categoria é dada por: 
			 * Equivalent To (isIndicatorFor value <Poluente>) and (hasAvgConc some (xsd:decimal >= <lowerLimit> and xsd:decimal <= <upperLimit>))
			 * 
			 * Essa classe somente é instanciada a cada hora, isto é, cada vêz que a média de concentração é recalculada.
			 * 
			 */
			
			
			/**
			 * A ontologia AQOnto não tem uma questão de competência que traz a necessidade de armazenar o instante de tempo em que uma qualidade
			 * do ar foi calculada. Aparentemente, a única questão de competência ligada a informação de tempo é 
			 * (Q5) Qual a categorização para o indicador <R> na periodicidade <H>?  
			 * 
			 * A  sequência  para  o  cálculo  do  índice  utilizando esta ontologia executa os seguintes passos: 
			 * 
			 */
			
			
			if(actualTime.isAfter(InicializarHWairpure.lastConcAvgTime.plusHours(1))) {//Um novo indicador é requisitado a cada hora.
				InicializarHWairpure.lastConcAvgTime = actualTime;
			
				
				/**
				 *  1 - A consulta das médias é feita e a categoria certa é adicionada ao recurso
				 *  2 - Um novo indicador é requisitado
				 *  3 - Definição de Periodicidade, Poluente e Forma de Derivação
				 */
				Resource AQIndicatorResource = modeloMedicaoDadoAmbiental
						.createResource(AQO3.NS + "IndicatorFor" + sinal +"-"+ uriData)
							.addProperty(RDF.type, AQO3.AQ_Indicator)
							.addProperty(AQO3.hasPeriodicity, "1", XSDDatatype.XSDinteger)
							.addProperty(AQO3.isIndicatorFor, AQO3.CO2)
							.addProperty(AQO3.isDerivedBy, AQO3.Average);
				
				/**	4 - Consulta levando em conta periodicidade, selecionando medidas do poluente, e aplicando a forma de agregação definida.
				 * 	Aqui posso aumentar ou diminuir a janela de tempo conforme necessário. 
				 * 	Basta trocar o parâmetro da consulta
				 */
				
				int janelaTempo = 1;
				
				Double AverageCO2 = ConsultaMedia.getMedia(actualTime.minusHours(janelaTempo).toString(), actualTime.toString());
		
				
				/** 5 - Aplicação recebe o valor da concentração para o indicador e enquadra-o na categoria que o abrange.*/
				AQIndicatorResource.addProperty(AQO3.hasAvgConc, AverageCO2.toString(), XSDDatatype.XSDdouble);
				
				//Leitura das regras para enquadramento da categoria
				BufferedReader rulesFileBR = new BufferedReader(new FileReader("./rules/regrasCategorias.txt"));
				List<Rule> rulesFile = Rule.parseRules( Rule.rulesParserFromReader(rulesFileBR) );
				
				//Reasoning com as regras carregadas.
				Reasoner reasoner = new GenericRuleReasoner(rulesFile);
				reasoner.setDerivationLogging(true);
				InfModel inference = ModelFactory.createInfModel(reasoner, modeloMedicaoDadoAmbiental);
						 
				//Inferência da categoria de qualidade do ar e busca por sintomas relacionados na janela de tempo.
				Resource categoriaResource = inference.getResource(AQO3.NS + "IndicatorFor" + sinal +"-2020-02-12-12-06-10");
				List<Resource> symptomsList = ConsultaSintomas.listaSintomas(modeloMedicaoDadoAmbiental, categoriaResource.getProperty(AQO3.isCategorizedBy).getObject(), janelaTempo);
				
				/**
				 * Recurso <setSymtomsResource>
				 * WindowStart : actualTime.minusHours(janelaTempo)
				 * WindowEnd   : actualTime
				 *  
				 */
				
				Resource setSymptomsResource = modeloMedicaoDadoAmbiental
						.createResource(AQO3.NS + "SymptomsOf" +AQIndicatorResource.getLocalName())
							.addProperty(IoTStream.windowStart, actualTime.minusHours(janelaTempo).toString(), XSDDatatype.XSDdateTime)
							.addProperty(IoTStream.windowStart, actualTime.toString(), XSDDatatype.XSDdateTime);
				
				//Iterador sob sintomas para adicionar ao recurso setSymptoms
				ListIterator<Resource> iterator = symptomsList.listIterator();
				
				while(iterator.hasNext()) {
					Resource symptom = iterator.next();
					setSymptomsResource.addProperty(symp.hasSymptom, symptom);
					
				}
				
				//Adição da inferência isCategorizedBy e de sintomas ao modelo
				modeloMedicaoDadoAmbiental.add(categoriaResource.getProperty(AQO3.isCategorizedBy));
				
				//Impressão do novo modelo
				modeloMedicaoDadoAmbiental.write(System.out, "N-TRIPLES");
				
			}
			
		}	
		
		/**
		 * Caso seja necessário imprimir o modelo
		 * IMPORTANTE: Comentar após uso, ou a saída fica impraticável
		 * Opções: TURTLE, N-TRIPLES, RDF/XML
		 */
		modeloMedicaoDadoAmbiental.write(System.out, "N-TRIPLES");
		
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
