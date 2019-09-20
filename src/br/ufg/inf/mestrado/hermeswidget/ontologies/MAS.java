package br.ufg.inf.mestrado.hermeswidget.ontologies;

import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class MAS {
	
	/** <p>The ontology model that holds the vocabulary terms</p> */
    private static OntModel m_model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM, null );
	
	/** <p>Relation from a monitoring to your observation </p>
     */
    public static final ObjectProperty hasObservation = m_model.createObjectProperty( "http://www.semanticweb.org/ernesto/ontologies/2015/2/MonitoringAndSensing#hasObservation" );

}
