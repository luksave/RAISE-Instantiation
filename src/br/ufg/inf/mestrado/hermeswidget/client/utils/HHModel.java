package br.ufg.inf.mestrado.hermeswidget.client.utils;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetAccessor;
import com.hp.hpl.jena.query.DatasetAccessorFactory;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;


public class HHModel {
	
	public static void create(String uri) {
		
		Dataset dataset = DatasetFactory.create(uri);
		DatasetAccessorFactory.create(dataset);
		
	}
	
	public static void update(Model model) {
		
		String serviceURI = "http://localhost:3030/test/data";
		DatasetAccessor accessor = DatasetAccessorFactory.createHTTP(serviceURI);
		
		accessor.putModel(model);
	
	}
	
	public static void query() {
		
		QueryExecution qe = QueryExecutionFactory.sparqlService(
                "http://localhost:3030/test/query", "SELECT * WHERE {?x ?r ?y}");
        
		ResultSet results = qe.execSelect();
        ResultSetFormatter.out(System.out, results);
        
        qe.close();
        
	}

}
