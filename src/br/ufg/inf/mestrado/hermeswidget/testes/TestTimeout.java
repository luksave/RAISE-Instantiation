package br.ufg.inf.mestrado.hermeswidget.testes;

import java.text.MessageFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.tdb.TDB;
import com.hp.hpl.jena.tdb.TDBFactory;

public class TestTimeout {

	private static final String location = "C:\\Users\\Ernesto\\Documents\\WorkspaceMestrado\\DatasetTest"; //$NON-NLS-1$
	//private static final String sparql = "SELECT * WHERE { ?a ?b ?c . ?c ?d ?e }"; //$NON-NLS-1$
	private static final String sparql = "SELECT * WHERE { ?a ?b ?c }"; //$NON-NLS-1$
	private static final int limit = 1000;
	private static final int timeout1_sec = 20;
	private static final int timeout2_sec = 20;

	private static final boolean CREATE = true;
	private static final int RESOURCES = 10000;
	private static final int COMMIT_EVERY = 1000;
	private static final int TRIPLES_PER_RESOURCE = 100;
	private static final String RES_NS = "http://example.com/"; //$NON-NLS-1$
	private static final String PROP_NS = "http://example.org/ns/1.0/"; //$NON-NLS-1$

	public static void main(String[] args) {

		
		TDB.getContext().set(TDB.symUnionDefaultGraph, true);
		Dataset ds = TDBFactory.createDataset(location);

		if (CREATE) {
			
			for (int iR = 0; iR < RESOURCES; iR++) {
				if (iR % COMMIT_EVERY == 0) {
					if (ds.isInTransaction()) {
						ds.commit();
						ds.end();
					}
					ds.begin(ReadWrite.WRITE);
				}

				Model model = ModelFactory.createDefaultModel();
				Resource res = model.createResource(RES_NS + "resource" + iR); //$NON-NLS-1$
				for (int iP = 0; iP < TRIPLES_PER_RESOURCE; iP++) {
					Property prop = ResourceFactory.createProperty(PROP_NS, "property" + iP); //$NON-NLS-1$
					model.add(res, prop, model.createTypedLiteral("Property value " + iP)); //$NON-NLS-1$
				}
				ds.addNamedModel(res.getURI(), model);
				System.out.println("Created " + res.getURI()); //$NON-NLS-1$
			}
			ds.commit();
			ds.end();
			
		}

		Query query = QueryFactory.create(sparql, Syntax.syntaxSPARQL_11);
		query.setLimit(limit);

		ds.begin(ReadWrite.READ);
		QueryExecution qexec = null;
		try {
			System.out.println(MessageFormat.format(
				"{0,date} {0,time} Executing query [limit={1} timeout1={2}s timeout2={3}s]: {4}", //$NON-NLS-1$
				new Date(System.currentTimeMillis()), limit, timeout1_sec, timeout2_sec, sparql));
			qexec = QueryExecutionFactory.create(query, ds);
			qexec.setTimeout(timeout1_sec, TimeUnit.SECONDS, timeout2_sec, TimeUnit.SECONDS);

			ResultSet rs = qexec.execSelect();
			//ResultSetFormatter.outputAsXML(System.out, rs);
			ResultSetFormatter.out(rs);
		} catch (Throwable t) {
			t.printStackTrace(); // OOME
		} finally {
			if (qexec != null)
				qexec.close();
			ds.end();
			ds.close();
			System.out.println(MessageFormat.format("{0,date} {0,time} Finished", //$NON-NLS-1$
				new Date(System.currentTimeMillis())));
		}
	}

}
