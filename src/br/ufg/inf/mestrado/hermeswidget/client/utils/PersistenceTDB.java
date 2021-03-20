package br.ufg.inf.mestrado.hermeswidget.client.utils;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
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

public class PersistenceTDB {
	
	Dataset dataset = null;

	public PersistenceTDB() {
		String diretorio = "./Storage/DatasetAmbiental";
		dataset = TDBFactory.createDataset(diretorio);

	}
	

	public void update(Model model) {
		try {
			dataset.begin(ReadWrite.WRITE);
			
			Model modelTDB = dataset.getDefaultModel();
			modelTDB.add(model);
			dataset.commit();
			
			System.out.println("Modelo armazenado com sucesso!\n");
			
		} finally {
			dataset.end();
			
		}
		
	}


	public Double getConcentration(String query, String aggregation) {
		
		dataset.begin(ReadWrite.READ);
		
		ResultSet rs = null;
		Double resultado = 0.0;
		
		try {
			QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
			rs = qexec.execSelect();
		
			while (rs.hasNext()) {
				QuerySolution qs = rs.next();
				resultado = qs.getLiteral(aggregation).getDouble();
			}
			
			qexec.close();
			
		}finally {dataset.end();}
		
		return resultado;
		
	}
	
	
	public ResultSet consultar(String query) {
		dataset.begin(ReadWrite.READ);
		ResultSet rs = null;
		
		QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
		rs = qexec.execSelect();
		
		
		try {
			ResultSetFormatter.out(rs);
			
		} finally {
			qexec.close();
			
		}
		
		return rs;
		
	}
	
	public void removerDados(Model model) {
		try {
			dataset.begin(ReadWrite.WRITE);
			
			Model modelTDB = dataset.getDefaultModel();
			modelTDB.remove(model);
			dataset.commit();
			
			System.out.println("Modelo deletado com sucesso!\n");
		} finally {
			dataset.end();
			
		}
	}

	public void fecharConexao() {
		dataset.close();
		
	}	

}
