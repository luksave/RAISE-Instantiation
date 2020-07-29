package br.ufg.inf.mestrado.hermeswidget.ontologies;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.ontology.*;


public class sosa {

	static String dc     =  "http://purl.org/dc/elements/1.1/";
	static String owl    =  "http://www.w3.org/2002/07/owl#";
	static String rdf    =  "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	static String rdfs   =  "http://www.w3.org/2000/01/rdf-schema#";
	static String schema =  "http://schema.org/";
	static String skos   =  "http://www.w3.org/2004/02/skos/core#";
	static String sosa   =  "http://www.w3.org/ns/sosa/";
	static String terms  =  "http://purl.org/dc/terms/";
	static String vann   =  "http://purl.org/vocab/vann/";
	static String voaf   =  "http://purl.org/vocommons/voaf#";
	static String xsd    =  "http://www.w3.org/2001/XMLSchema#";
	static String time   =  "http://www.w3.org/2006/time#";
	static String foaf   =  "http://xmlns.com/foaf/0.1/";

	
	/** <p>The ontology model that holds the vocabulary terms</p> */
    private static OntModel m_model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM, null );
    
    /** <p>The namespace of the vocabulary as a string</p> */
    public static final String NS = "http://www.w3.org/ns/sosa/#";
    
    /** <p>The namespace of the vocabulary as a string</p>
     *  @see #NS */
    public static String getURI() {return NS;}
    
    /** <p>The namespace of the vocabulary as a resource</p> */
    public static final Resource NAMESPACE = m_model.createResource( NS );
    
    /** <p>The ontology's owl:versionInfo as a string</p> */
    public static final String VERSION_INFO = "0.4";
	
    
    public static final AnnotationProperty date = m_model.createAnnotationProperty( dc + "date" );
    
    public static final AnnotationProperty created = m_model.createAnnotationProperty( terms + "created" );
    
    public static final AnnotationProperty modified = m_model.createAnnotationProperty( terms + "modified" );
    
    public static final AnnotationProperty domainIncludes= m_model.createAnnotationProperty( schema + "domainIncludes" );
    
    public static final AnnotationProperty rangeIncludes= m_model.createAnnotationProperty( schema+ "rangeIncludes" );
 
    
    /**
     * CLASSES DA SOSA usada na IoT-Stream
     */
   
    
    public static final OntClass Sensor = m_model.createClass( sosa + "Sensor" );
    
	
    /**
     * PROPRIEDADES DE OBJETO DA SOSA usadas na IoT-Stream
     */
       
    public static final ObjectProperty madeObservation = m_model.createObjectProperty( sosa + "madeObservation");
    
    public static final ObjectProperty madeBySensor = m_model.createObjectProperty( sosa + "madeBySensor");
    
    public static final ObjectProperty hasSimpleResult = m_model.createObjectProperty( sosa + "hasSimpleResult");
    
    public static final ObjectProperty resultTime = m_model.createObjectProperty( sosa + "isSampleOf");
    
    
}
