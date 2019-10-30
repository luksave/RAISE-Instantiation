package br.ufg.inf.mestrado.hermeswidget.manager.services;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;

/**
 * 
 * @author Ernesto
 *
 */

public class HWPersistenceService {
	
	Dataset dataset = null;

	public HWPersistenceService() {}

	public void createConection() {
		String diretorio = "HermesDatabases/DatasetHW";
		dataset = TDBFactory.createDataset(diretorio);

	}

	public void update(Model model) {
		try {
			dataset.begin(ReadWrite.WRITE);
			Model modelTDB = dataset.getDefaultModel();
			modelTDB.add(model);
			dataset.commit();
			
		} finally {
			dataset.end();
			
		}
		
	}

	public void consultar(String query) {
		dataset.begin(ReadWrite.READ);
		try {
			QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
			ResultSet rs = qexec.execSelect();
			
			try {
				ResultSetFormatter.out(rs);
				
			} finally {
				qexec.close();
				
			}
		} finally {
			
			dataset.end();
		}
	}

	public void fecharConexao() {
		dataset.close();
		
	}	

}