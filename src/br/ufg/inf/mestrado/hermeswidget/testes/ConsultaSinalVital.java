package br.ufg.inf.mestrado.hermeswidget.testes;

import br.ufg.inf.mestrado.hermeswidget.manager.services.HWPersistenceService;

public class ConsultaSinalVital {

	public static void main(String[] args) {

		// Consulta SPARQL sobre os indivíduos armazenados
		HWPersistenceService h = new HWPersistenceService();
		h.createConection();

		String queryPulseRate = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
				+ "PREFIX var: <http://www.semanticweb.org/ontologies/2013/1/Ontology1361391792831.owl#> "
				+ "PREFIX actor: <http://linkserver.icmc.usp.br/ckonto/actor#> "
				+ "PREFIX acti: <http://linkserver.icmc.usp.br/ckonto/activity#> "
				+ "PREFIX tEvent: <http://linkserver.icmc.usp.br/ckonto/tEvent#> "
				+ "PREFIX time: <http://linkserver.icmc.usp.br/ckonto/time#> "
				+ "SELECT ?valor ?pulso ?unidade "
				+ "WHERE {?monitoring acti:hasParticipant var:person01 . "
				+ "?monitoring var:hasMonitoringPulseRate ?pulseRate . "
				+ "?monitoring tEvent:startDateTime ?date . "
				+ "?pulso var:isMeasurementPulseRate ?pulseRate . "
				+ "?pulso var:valuePulseRate ?valor . "
				+ "?pulso var:unitPulseRate ?unidade " +
				// "?date time:instantCalendarClockDataType ?valorData . "+
				// "FILTER (?valorData >= '2013-02-19T10:00:00'^^xsd:dateTime && ?valorData <= '2013-02-20T11:00:00'^^xsd:dateTime)"
				// +
				"}";

		String query = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
				+ "PREFIX var: <http://www.semanticweb.org/ontologies/2013/1/Ontology1361391792831.owl#> "
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX acti: <http://linkserver.icmc.usp.br/ckonto/activity#>"
				+ "SELECT ?person ?tipo "
				+ "WHERE {?person acti:hasParticipant ?tipo}";

		String queryTemperataure = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ "PREFIX var: <http://www.semanticweb.org/ontologies/2013/1/Ontology1361391792831.owl#> "
				+ "PREFIX actor: <http://linkserver.icmc.usp.br/ckonto/actor#> "
				+ "PREFIX acti: <http://linkserver.icmc.usp.br/ckonto/activity#> "
				+ "PREFIX tEvent: <http://linkserver.icmc.usp.br/ckonto/tEvent#> "
				+ "PREFIX time: <http://linkserver.icmc.usp.br/ckonto/time#> "
				+ "SELECT ?monitoring ?date ?axiliary ?valor ?temperature ?unidade "
				+ "WHERE {?monitoring acti:hasParticipant var:person01 . "
				+ "?monitoring var:hasMonitoringTemperature ?axiliary . "
				+ "?monitoring tEvent:startDateTime ?date . "
				+ "?temperature var:isMeasurementTemperature ?axiliary . "
				+ "?temperature var:valueTemperature ?valor . "
				+ "?temperature var:unitTemperature ?unidade " + "}";

		String queryBloodPressure = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
				+ "PREFIX var: <http://www.semanticweb.org/ontologies/2013/1/Ontology1361391792831.owl#> "
				+ "PREFIX actor: <http://linkserver.icmc.usp.br/ckonto/actor#> "
				+ "PREFIX acti: <http://linkserver.icmc.usp.br/ckonto/activity#> "
				+ "PREFIX tEvent: <http://linkserver.icmc.usp.br/ckonto/tEvent#> "
				+ "PREFIX time: <http://linkserver.icmc.usp.br/ckonto/time#> "
				+ "SELECT ?person ?valorSistolica ?valorDiastolica ?pressure ?unidade ?valorData "
				+ "WHERE {?monitoring acti:hasParticipant ?person . "
				+ "?person var:hasRole var:patient . "
				+ "?monitoring var:hasMonitoringBloodPressure ?bloodPressure . "
				+ "?monitoring tEvent:startDateTime ?date . "
				+ "?pressure var:isMeasurementBloodPressure ?bloodPressure . "
				+ "?pressure var:valueSystolicBloodPressure ?valorSistolica . "
				+ "?pressure var:valueDiastolicBloodPressure ?valorDiastolica . "
				+ "?pressure var:unitBloodPressure ?unidade . "
				+ "?date time:instantCalendarClockDataType ?valorData . " + "}";

		String queryRespiratoryRate = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
				+ "PREFIX var: <http://www.semanticweb.org/ontologies/2013/1/Ontology1361391792831.owl#> "
				+ "PREFIX actor: <http://linkserver.icmc.usp.br/ckonto/actor#> "
				+ "PREFIX acti: <http://linkserver.icmc.usp.br/ckonto/activity#> "
				+ "PREFIX tEvent: <http://linkserver.icmc.usp.br/ckonto/tEvent#> "
				+ "PREFIX time: <http://linkserver.icmc.usp.br/ckonto/time#> "
				+ "SELECT ?valor ?respiratory ?unidade ?valorData "
				+ "WHERE {?monitoring acti:hasParticipant var:person01 . "
				+ "?monitoring var:hasMonitoringRespiratoryRate ?respRate . "
				+ "?monitoring tEvent:startDateTime ?date . "
				+ "?respiratory var:isMeasurementRespiratoryRate ?respRate . "
				+ "?respiratory var:valueRespiratoryRate ?valor . "
				+ "?respiratory var:unitRespiratoryRate ?unidade . "
				+ "?date time:instantCalendarClockDataType ?valorData " + "}";

		String queryOxygenSaturation = "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
				+ "PREFIX var:	<http://www.semanticweb.org/ontologies/2013/1/Ontology1361391792831.owl#> "
				+ "PREFIX actor: <http://linkserver.icmc.usp.br/ckonto/actor#> "
				+ "PREFIX acti: <http://linkserver.icmc.usp.br/ckonto/activity#> "
				+ "PREFIX tEvent: <http://linkserver.icmc.usp.br/ckonto/tEvent#> "
				+ "PREFIX time: <http://linkserver.icmc.usp.br/ckonto/time#> "
				+ "SELECT ?valor ?oxygen ?unidade ?valorData "
				+ "WHERE {?monitoring acti:hasParticipant var:person01 . "
				+ "?monitoring var:hasMonitoringOxygenSaturation ?oxygenSaturation . "
				+ "?monitoring tEvent:startDateTime ?date . "
				+ "?oxygen var:isMeasurementOxygenSaturation ?oxygenSaturation . "
				+ "?oxygen var:valueOxygenSaturation ?valor . "
				+ "?oxygen var:unitOxygenSaturation ?unidade . "
				+ "?date time:instantCalendarClockDataType ?valorData " + "}";

		h.consultar("SELECT ?s ?p ?o where {?s ?p ?o}");
		h.consultar(query);
		
		System.out.println("\n\n\n################################################################  "
				+ "MEDIÇÕES DE TEMPERATURA DE UM DETERMINADO PACIENTE  "
				+ "#############################################################################");
		h.consultar(queryTemperataure);
		System.out.println("\n\n\n################################################################  "
				+ "MEDIÇÕES DE FREQUÊNCIA DE PULSO DE UM DETERMINADO PACIENTE  "
				+ "#############################################################################");
		h.consultar(queryPulseRate);
		System.out.println("\n\n\n################################################################  "
				+ "MEDIÇÕES DE PRESSÂO ARTERIAL DE UM DETERMINADO PACIENTE  "
				+ "#############################################################################");
		h.consultar(queryBloodPressure);
		System.out.println("\n\n\n################################################################  "
				+ "MEDIÇÕES DE FREQUÊNCIA RESPIRATÓRIA DE UM DETERMINADO PACIENTE  "
				+ "#############################################################################");
		h.consultar(queryRespiratoryRate);
		System.out.println("\n\n\n################################################################  "
				+ "MEDIÇÕES DE SATURAÇÃO DE OXIGÊNIO DE UM DETERMINADO PACIENTE  "
				+ "#############################################################################");
		h.consultar(queryOxygenSaturation);
	}

}
