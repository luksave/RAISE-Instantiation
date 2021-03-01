package br.ufg.inf.mestrado.hermeswidget.client.utils;

import br.ufg.inf.mestrado.hermeswidget.client.services.HWRepresentationServiceSensorIoTStream;

public class ConsultaMedia{

	
	public static Double getMedia(String windowStart, String windowEnd) {

		PersistenceTDB tdb = HWRepresentationServiceSensorIoTStream.getTDB();
		
		System.out.println(windowStart);
		System.out.println(windowEnd);
		
		String 
		queryCO2AvgInt  = "PREFIX xsd:        <http://www.w3.org/2001/XMLSchema#> "
						+ "PREFIX rdfs:       <http://www.w3.org/2000/01/rdf-schema#> "
						+ "PREFIX owl:        <http://www.w3.org/2002/07/owl#> "
						+ "PREFIX rdf:        <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
						+ "PREFIX ssn:        <http://purl.oclc.org/NET/ssnx/ssn#> "
						+ "PREFIX iot-lite:   <http://purl.oclc.org/NET/UNIS/fiware/iot-lite#> "
						+ "PREFIX qu:    	  <http://qudt.org/2.1/schema/qudt#> "
						+ "PREFIX qu-unit: 	  <http://qudt.org/2.1/vocab/unit#> "
						+ "PREFIX sosa:       <http://www.w3.org/ns/sosa/> "
						+ "PREFIX iot-stream: <http://purl.org/iot/ontology/iot-stream#> "
						+ "PREFIX geo:        <http://www.w3.org/2003/01/geo/wgs84_pos#> "
						
						+ "SELECT (ROUND(AVG(?Resultado)) AS ?AVERAGE) "
						//+ "SELECT ?Resultado ?Tempo "
						
					    + "WHERE {"
					    + "?Observacao a iot-stream:StreamObservation ;"
					    + "		sosa:hasSimpleResult ?Resultado ;"
					    + "		sosa:isSampleOf ?Tempo ; "
					    + "		sosa:madeBySensor ?Sensor ."
					    + "?Sensor qu:hasUnit ?Unit ."
					      
					    + "FILTER (?Tempo >= '" + windowStart + "Z'^^xsd:dateTime &&"
					    + "        ?Tempo <= '" + windowEnd   + "Z'^^xsd:dateTime &&"
			    		+ "        datatype(?Resultado) = xsd:double &&"
			    		+ "		 ?Unit = qu-unit:PPM)"
			    		
					    + "}";		
		
		return tdb.getAverageConcentration(queryCO2AvgInt); 
				
	}
	
}
