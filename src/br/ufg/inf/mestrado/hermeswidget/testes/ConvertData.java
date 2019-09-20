package br.ufg.inf.mestrado.hermeswidget.testes;

import java.io.InputStream;
import java.util.UUID;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;

public class ConvertData {
	
	public static void main(String[] args) {
		convert();
	}
	
	public static void convert() {
		
		Model modelSSN = ModelFactory.createDefaultModel();
		
		InputStream in = FileManager.get().open("./src/testes/ssn.ttl");
		if (in == null) {
			throw new IllegalArgumentException("Arquivo nao encontrado");
		}
		
		modelSSN.read(in, null, "TURTLE");
		modelSSN.write(System.out, "TURTLE");
		
		Resource ENTITY_TYPE = modelSSN.getResource("http://purl.oclc.org/NET/ssnx/ssn#FeatureOfInterest");
		StmtIterator iter = modelSSN.listStatements(null, RDF.type, ENTITY_TYPE);
		String entityID = null;
		while (iter.hasNext()) {
		    entityID = iter.next().getSubject().getURI();
		    System.out.println(entityID);
		}
		
		int aux = entityID.indexOf("#");
		//System.out.println(aux);
		
		String entity = entityID.substring(aux+1, entityID.length());
		//entity = "http://www.semanticweb.org/ontologies/2013/1/Ontology1361391792831.owl#"+entity;
		//entity = "msvh:"+entity;
		System.out.println(entity);
		
		
		System.out.println("------------------------------");
		
		
		String monitoring = "MonitoringTemperature-"+UUID.randomUUID().toString();
		String instance = "InstanceTemperature-"+UUID.randomUUID().toString();
		String dateTime = "DateTime-"+UUID.randomUUID().toString();
		
		/*
		String queryPerson = 
				" PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> "
				+ " SELECT ?patient "
				+ " WHERE "
				+ " { "
				+ " ?sensing ssn:featureOfInterest ?patient . "
				+ " }";
		*/
		
		String constructConvert = 
			"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> "
			+ " PREFIX msvh: <http://www.semanticweb.org/ontologies/2013/1/Ontology1361391792831.owl#> "
			+ " PREFIX actor: <http://linkserver.icmc.usp.br/ckonto/actor#> "
			+ " PREFIX acti: <http://linkserver.icmc.usp.br/ckonto/activity#> "
			+ " PREFIX tEvent: <http://linkserver.icmc.usp.br/ckonto/tEvent#> "
			+ " PREFIX time: <http://linkserver.icmc.usp.br/ckonto/time#> "
			+ " PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> "
			//+ " BASE <http://www.semanticweb.org/ontologies/2013/1/Ontology1361391792831.owl#> "
			
			+ " CONSTRUCT "
			+ " { "
			+ " msvh:"+monitoring+" acti:hasParticipant msvh:"+entity+" . "
			+ " msvh:"+dateTime+" time:instantCalendarClockDataType ?dateTime . "
			+ " msvh:"+instance+" msvh:valueTemperature ?value ; "
			+ " 		  		  msvh:unitTemperature ?unit . "
			+ " } "
			+ " WHERE "
			+ " { "
			+ " ?sensing ssn:featureOfInterest ?patient . "
			+ " ?sensing ssn:observationResultTime ?dateTime . "
			+ " ?instance ssn:hasOutputValue ?value ; "
			+ " 	      ssn:hasOutputUnit ?unit . "
			+ " } ";
		
		
		
		/*
		String queryTeste = 
				"BASE <http://www.semanticweb.org/ontologies/2013/1/Ontology1361391792831.owl#> "
				+ " CONSTRUCT {?s ?p ?testURI.} "
				+ " WHERE"
				+ " { "
				+ " ?s ?p ?o . "
				+ " BIND (URI(?o) AS ?testURI) "
				+ " } ";
		*/
		
		
		Query queryConstruct = QueryFactory.create(constructConvert);
		QueryExecution qec = QueryExecutionFactory.create(queryConstruct, modelSSN);
		Model resultModel = qec.execConstruct();
		qec.close();
		
		System.out.println("\n\n");
		//resultModel.write(System.out, "TURTLE");
		
		Property p = resultModel.getProperty("http://linkserver.icmc.usp.br/ckonto/activity#hasParticipant");
		ResIterator iterator = resultModel.listSubjectsWithProperty(p);
		entityID = null;
		while (iterator.hasNext()) {
		    entityID = iterator.next().getURI();
		    //System.out.println(entityID);
		    //System.out.println("Teste");
		}
		
		String axillary = "axillary_temperature-"+UUID.randomUUID().toString();
		Statement tipoMonitoramento = ResourceFactory.createStatement(
				ResourceFactory.createResource(entityID),
				ResourceFactory.createProperty("http://www.semanticweb.org/ontologies/2013/1/Ontology1361391792831.owl#hasMonitoringTemperature"),
				ResourceFactory.createResource("http://www.semanticweb.org/ontologies/2013/1/Ontology1361391792831.owl#"+axillary));
		
		resultModel.add(tipoMonitoramento);
		
		
		Statement paciente = ResourceFactory.createStatement(
				ResourceFactory.createResource(entityID),
				ResourceFactory.createProperty("http://linkserver.icmc.usp.br/ckonto/activity#hasParticipant"),
				ResourceFactory.createResource("http://www.semanticweb.org/ontologies/2013/1/Ontology1361391792831.owl#"+entity));
		
		resultModel.add(paciente);
		
		String eventMedida = "EventSensingTemperature-"+UUID.randomUUID().toString();
		Statement event = ResourceFactory.createStatement(
				ResourceFactory.createResource(entityID),
				ResourceFactory.createProperty("http://linkserver.icmc.usp.br/ckonto/tEvent#startDateTime"),
				ResourceFactory.createResource("http://www.semanticweb.org/ontologies/2013/1/Ontology1361391792831.owl#"+eventMedida));
		
		resultModel.add(event);		
		
		
		p = resultModel.getProperty("http://www.semanticweb.org/ontologies/2013/1/Ontology1361391792831.owl#unitTemperature");
		iterator = resultModel.listSubjectsWithProperty(p);
		entityID = null;
		while (iterator.hasNext()) {
		    entityID = iterator.next().getURI();
		    //System.out.println(entityID);
		    //System.out.println("Teste");
		}
		
		
		
		Statement medicao = ResourceFactory.createStatement(
				ResourceFactory.createResource(entityID),
				ResourceFactory.createProperty("http://www.semanticweb.org/ontologies/2013/1/Ontology1361391792831.owl#isMeasurementTemperature"),
				ResourceFactory.createResource("http://www.semanticweb.org/ontologies/2013/1/Ontology1361391792831.owl#"+axillary));
		
		resultModel.add(medicao);
		
		
		resultModel.write(System.out, "TURTLE");
		
		// SELECT
		/*
		Query querySelect = QueryFactory.create(queryPerson);
		QueryExecution qes = QueryExecutionFactory.create(querySelect, modelSSN);
		ResultSet results = qes.execSelect();
		//ResultSetFormatter.out(System.out, results, querySelect);
		String result = ResultSetFormatter.asText(results);
		System.out.println(result);
		//System.out.println(ResultSetFormatter.asText(results));
		qes.close();
		*/
		
		//Resource resource = ResourceFactory.createResource();
		//Statement tripla = ResourceFactory.createStatement(subject, predicate, object)
		
		
	}

}
