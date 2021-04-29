package br.ufg.inf.mestrado.hermeswidget.client.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import com.hp.hpl.jena.query.ResultSet;

import br.ufg.inf.mestrado.hermeswidget.client.preprocessing.CarbonDioxidePreprocessing;
import br.ufg.inf.mestrado.hermeswidget.client.services.HWRepresentationServiceSensorIoTStream;

public class EnvironmentQuery{

	static PersistenceTDB tdb = HWRepresentationServiceSensorIoTStream.getTDB();
	
	
	static String prefixos  =  "PREFIX xsd:        <http://www.w3.org/2001/XMLSchema#> "
							 + "PREFIX rdfs:       <http://www.w3.org/2000/01/rdf-schema#> "
							 + "PREFIX owl:        <http://www.w3.org/2002/07/owl#> "
							 + "PREFIX rdf:        <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
							 + "PREFIX ssn:        <http://purl.oclc.org/NET/ssnx/ssn#> "
							 + "PREFIX iot-lite:   <http://purl.oclc.org/NET/UNIS/fiware/iot-lite#> "
							 + "PREFIX qu:    	   <http://qudt.org/2.1/schema/qudt#> "
							 + "PREFIX qu-unit:    <http://qudt.org/2.1/vocab/unit#> "
							 + "PREFIX sosa:       <http://www.w3.org/ns/sosa/> "
							 + "PREFIX iot-stream: <http://purl.org/iot/ontology/iot-stream#> "
							 + "PREFIX geo:        <http://www.w3.org/2003/01/geo/wgs84_pos#> "
							 + "PREFIX aqonto:     <http://www.semanticweb.org/ricardo/ontologies/2017/5/AirQualityOnto.owl#> "
							 + "PREFIX sympAssoc:  <http://br.ufg.inf/mestrado/hermes/SymptomAssociation.owl/> ";
	
	
	public static Double getMedia(String windowStart, String windowEnd) {

		System.out.println(windowStart);
		System.out.println(windowEnd);
		
		String 
		queryCO2AvgInt  = prefixos + "SELECT (ROUND(AVG(?Resultado)) AS ?AVERAGE) "
						
					    + "WHERE {"
					    + "?Observacao a iot-stream:StreamObservation ;"
					    + "		sosa:hasSimpleResult ?Resultado ;"
					    + "		sosa:isSampleOf ?Tempo ; "
					    + "		sosa:madeBySensor ?Sensor ."
					    + "?Sensor qu:hasUnit ?Unit ."
					      
					    + "FILTER (?Tempo >= '" + windowStart + "'^^xsd:dateTime &&"
					    + "        ?Tempo <= '" + windowEnd   + "'^^xsd:dateTime &&"
			    		+ "        datatype(?Resultado) = xsd:double &&"
			    		+ "		 ?Unit = qu-unit:PPM)"
			    		
					    + "}";		
		
		return tdb.getConcentration(queryCO2AvgInt, "AVERAGE"); 
				
	}
	
	/**-------------------- Consultas básicas */
	//[1] Qual a concentração do poluente <P> no instante de tempo <T>? 
	public static Double getConcPolut(String poluente, String tempo) {


		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
		LocalDateTime actualTime = LocalDateTime.parse(tempo, format);
		
		String comecoTempo = actualTime.toString();
		String limiteTempo = actualTime.plusSeconds(20).toString();
		
		System.out.println("Poluente: "+ poluente);
		System.out.println("Instante: "+ comecoTempo);
		
		String 
		queryLastConc  = prefixos + "SELECT ?Resultado ?Tempo "
						
					    + "WHERE {"
					    + "?Medida a aqonto:AQ_Measurement ;"
					    + "		sosa:hasSimpleResult ?Resultado ;"
					    + "		aqonto:hasHappenedOn ?Tempo ; "
					    + "		aqonto:hasMeasurementOf aqonto:"+ poluente + " . "
					      
					    + "FILTER (?Tempo >= '" + comecoTempo + "'^^xsd:dateTime &&"
					    + "        ?Tempo <= '" + limiteTempo + "'^^xsd:dateTime &&"
			    		+ "        datatype(?Resultado) = xsd:double)"
			    		
					    + "}";		
		
		
		Double resultado = tdb.getConcentration(queryLastConc, "Resultado");
		
		return resultado; 
				
	}
	
	//[2] Qual a concentração atual do poluente <P>?
	public static Double getLastConcPolut(String poluente) {
		
		System.out.println("Poluente: "+ poluente);
		
		String 
		queryLastConc  = prefixos + "SELECT ?Resultado ?Tempo "
						
					    + "WHERE {"
					    + "?Medida a aqonto:AQ_Measurement ;"
					    + "		sosa:hasSimpleResult ?Resultado ;"
					    + "		aqonto:hasHappenedOn ?Tempo ; "
					    + "		aqonto:hasMeasurementOf aqonto:"+ poluente + " . "
					      
					    + "FILTER (datatype(?Resultado) = xsd:double)"
			    		
					    + "} "
					    + "ORDER BY DESC(?Tempo)"
					    + "Limit 1";		
		
		
		Double resultado = tdb.getConcentration(queryLastConc, "Resultado");
		
		return resultado; 
				
	}

	//[3] Quais as concentrações do poluente <P> no intervalo de tempo <H>?
	public static Double getIntervalConcPolut(String poluente, String tempo1, String tempo2) {


		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
		LocalDateTime time1 = LocalDateTime.parse(tempo1, format);
		LocalDateTime time2 = LocalDateTime.parse(tempo2, format);
		
		String comecoTempo = time1.toString();
		String finalTempo = time2.toString();
		
		System.out.println("Poluente: "+ poluente);
		System.out.println("Intervalo: "+ comecoTempo + " - " +finalTempo);
		
		String 
		queryLastConc  = prefixos + "SELECT ?Resultado ?Tempo "
						
					    + "WHERE {"
					    + "?Medida a aqonto:AQ_Measurement ;"
					    + "		sosa:hasSimpleResult ?Resultado ;"
					    + "		aqonto:hasHappenedOn ?Tempo ; "
					    + "		aqonto:hasMeasurementOf aqonto:"+ poluente + " . "
					      
					    + "FILTER (?Tempo >= '" + comecoTempo + "'^^xsd:dateTime &&"
					    + "        ?Tempo <= '" + finalTempo + "'^^xsd:dateTime &&"
			    		+ "        datatype(?Resultado) = xsd:double)"
			    		
					    + "}";		
		
		
		Double resultado = tdb.getConcentration(queryLastConc, "Resultado");
		
		return resultado; 
				
	}

	
	/**-------------------- Consultas levando em conta pré-processamentos*/
	//[4] Qual a média de concentração para o poluente <P> no intervalo de tempo <H>?
	public static Double getIntervalConcPolutAVG(String poluente, String tempo1, String tempo2) {

		System.out.println("Poluente:" +poluente);
		System.out.println("Tempo inicial:" +tempo1);
		System.out.println("Tempo final:" +tempo2);


		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
		LocalDateTime time1 = LocalDateTime.parse(tempo1, format);
		LocalDateTime time2 = LocalDateTime.parse(tempo2, format);
		
		String comecoTempo = time1.toString();
		String finalTempo = time2.toString();
		
		System.out.println("Poluente: "+ poluente);
		System.out.println("Intervalo: "+ comecoTempo + " - " +finalTempo);
		
		String 
		queryIntervalAVGConc  = prefixos + "(ROUND(AVG(?Resultado)) AS ?AVERAGE) ?Tempo "
						
					    + "WHERE {"
					    + "?Medida a aqonto:AQ_Measurement ;"
					    + "		sosa:hasSimpleResult ?Resultado ;"
					    + "		aqonto:hasHappenedOn ?Tempo ; "
					    + "		aqonto:hasMeasurementOf aqonto:"+ poluente + " . "
					      
					    + "FILTER (?Tempo >= '" + comecoTempo + "'^^xsd:dateTime &&"
					    + "        ?Tempo <= '" + finalTempo + "'^^xsd:dateTime &&"
			    		+ "        datatype(?Resultado) = xsd:double)"
			    		
					    + "}";		
		
		
		Double resultado = tdb.getConcentration(queryIntervalAVGConc, "AVERAGE");
		
		return resultado; 

		
	}
	

	/**-------------------- Consultas levando em conta a modelagem*/
	//[5] Qual a qualidade do ar no ambiente <A> levando em conta o poluente <P>?
	public static String getQualityByPolut(String poluente, String tempo1, String tempo2) {

		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
		LocalDateTime time1 = LocalDateTime.parse(tempo1, format);
		LocalDateTime time2 = LocalDateTime.parse(tempo2, format);
		
		String comecoTempo = time1.toString();
		String finalTempo = time2.toString();
		
		System.out.println("Poluente: "+ poluente);
		System.out.println("Intervalo: "+ comecoTempo + " - " +finalTempo);

		Double media = EnvironmentQuery.getIntervalConcPolutAVG(poluente, tempo1, tempo2);
		
		int resultado = CarbonDioxidePreprocessing.IQACarbonDioxide(media);
		
		String level = CarbonDioxidePreprocessing.IQALevel(resultado);
		
		return level; 
		
	}
	

	//[6] Qual a qualidade do ar para o poluente <P> no instante de tempo <T>?
	public static String getInstQualityByPolut(String poluente, String tempo1) {

		String 
		queryPeriodicity  = prefixos + "SELECT ?Periodicidade "
						
					    + "WHERE {"
					    + "?Poluente a rdf:type aqonto:CO2;"
					    + "		aqonto:hasPeriodicity ?Periodicidade . "			    		
					    + "}";		
		
		
		Double periodicity = tdb.getConcentration(queryPeriodicity, "Periodicidade");
		
		int intPeriodicity = (int)Math.round(periodicity);
		
		
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
		LocalDateTime time1 = LocalDateTime.parse(tempo1, format);
		LocalDateTime time2 = time1.minusHours(intPeriodicity);
		
		String Tempo1 = time1.toString();
		String Tempo2 = time2.toString();
		
		System.out.println("Poluente: "+ poluente);
		System.out.println("Intervalo: "+ Tempo1 + " - " +Tempo2);

		Double media = EnvironmentQuery.getIntervalConcPolutAVG(poluente, Tempo1, Tempo2);
		
		int resultado = CarbonDioxidePreprocessing.IQACarbonDioxide(media);
		
		String level = CarbonDioxidePreprocessing.IQALevel(resultado);
		
		return level; 
		
	}

	
	//[7] Qual a qualidade do ar para o poluente <P> atual?
	public static String getActualQualityByPolut(String poluente) {
		
		String 
		queryPeriodicity  = prefixos + "SELECT ?Periodicidade "
						
					    + "WHERE {"
					    + "?Poluente a rdf:type aqonto:CO2;"
					    + "		aqonto:hasPeriodicity ?Periodicidade . "			    		
					    + "}";		
		
		
		Double periodicity = tdb.getConcentration(queryPeriodicity, "Periodicidade");
		
		int intPeriodicity = (int)Math.round(periodicity);
		
		Calendar calendar = Calendar.getInstance();
		
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm:ss");
		LocalDateTime time1 = LocalDateTime.parse(calendar.getTime().toString(), format);
		LocalDateTime time2 = time1.minusHours(intPeriodicity);
		
		String Tempo1 = time1.toString();
		String Tempo2 = time2.toString();
		
		System.out.println("Poluente: "+ poluente);
		System.out.println("Intervalo: "+ Tempo1 + " - " +Tempo2);

		Double media = EnvironmentQuery.getIntervalConcPolutAVG(poluente, Tempo1, Tempo2);
		
		int resultado = CarbonDioxidePreprocessing.IQACarbonDioxide(media);
		
		String level = CarbonDioxidePreprocessing.IQALevel(resultado);
		
		return level; 
		
	}
	
	
	//[8] Qual a qualidade do ar para geral no instante de tempo <T>?  
	/** TODO */
	
	
	//[9] Qual a qualidade do ar para geral atual?
	/** TODO */
	
	/**-------------------- Consultas levando em conta o enriquecimento realizado */
	//[10] Quais sintomas <S> podem ser encontrados para o IQA do poluente <P>?
	public static ResultSet getSymptomsByPolut(String poluente) {
		
		System.out.println("Poluente: "+ poluente);

		String querySymptoms  = prefixos + "SELECT ?Qualidade ?Conjunto "				
					    + "WHERE {"
					    + "?Qualidade a aqonto:AQ_Index ;"
					    + "		aqonto:isIndexFor aqonto:"+ poluente +" ; " 
					    + "		sympAssoc:hasSymptomsSet ?Conjunto . "
			
					    + "}";		
		
		
		ResultSet resultado = tdb.consultar(querySymptoms);
		
		return resultado; 
			
	}
	
	//[11] Quais são os sintomas <S> do conjunto de sintomas <Cs>?
	public static ResultSet getSymptomsSetByPolut(String poluente) {
		
		System.out.println("Poluente: "+ poluente);

		String querySymptoms  = prefixos + "SELECT ?Sintomas "				
					    + "WHERE {"
					    + "?Qualidade a aqonto:AQ_Index ;"
					    + "		aqonto:isIndexFor aqonto:"+ poluente +" ; " 
					    + "		sympAssoc:hasSymptomsSet ?Conjunto . "
					    + "?Conjunto sympAssoc:hasSymptom ?Sintomas . "
			
					    + "}";		
		
		
		ResultSet resultado = tdb.consultar(querySymptoms);
		
		return resultado; 
			
	}
	

	
	
}
