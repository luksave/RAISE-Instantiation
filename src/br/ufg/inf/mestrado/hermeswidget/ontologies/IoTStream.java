package br.ufg.inf.mestrado.hermeswidget.ontologies;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.ontology.*;
 
/**
 * Vocabulary definitions from IoTStream.owl 
 * @author Auto-generated by schemagen on 22 jul 2020 00:50 
 */
public class IoTStream {
	/** <p>The ontology model that holds the vocabulary terms</p> */
    private static OntModel M_MODEL = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM, null );
    
    /** <p>The namespace of the vocabulary as a string</p> */
    public static final String NS = "http://purl.org/iot/ontology/iot-stream#";
    
    /** <p>The namespace of the vocabulary as a string</p>
     * @return namespace as String
     * @see #NS */
    public static String getURI() {return NS;}
    
    /** <p>The namespace of the vocabulary as a resource</p> */
    public static final Resource NAMESPACE = M_MODEL.createResource( NS );
    
    /** <p>The ontology's owl:versionInfo as a string</p> */
    public static final String VERSION_INFO = "1.0";
    
    /** <p>The data analytics that has been applied to the IoT data stream.</p> */
    public static final Resource Analytics = M_MODEL.createResource( "http://purl.org/iot/ontology/iot-stream#Analytics" );
    
    /** <p>The event that has been detected from an IoT data steam, and generated by 
     *  an analytics process</p>
     */
    public static final Resource Event = M_MODEL.createResource( "http://purl.org/iot/ontology/iot-stream#Event" );
    
    public static final Resource IotStream = M_MODEL.createResource( "http://purl.org/iot/ontology/iot-stream#IotStream" );
    
    /** <p>A observation made by a sensor device captured as a data point over a time 
     *  instant, or as a subset of data points over a defined time interval.</p>
     */
    public static final Resource StreamObservation = M_MODEL.createResource( "http://purl.org/iot/ontology/iot-stream#StreamObservation" );

    /* 
     * CLASSES
     *  public static final OntClass NOME = M_MODEL.createClass( "url" );
     * */
    
    public static final OntClass Service = M_MODEL.createClass( "http://purl.oclc.org/NET/UNIS/fiware/iot-lite#Service" );
    
    public static final OntClass Coverage = M_MODEL.createClass( "http://www.opengis.net/ont/geosparql#Coverage" );
    
    public static final OntClass Point = M_MODEL.createClass( "http://www.w3.org/2003/01/geo/wgs84_pos#Point" );
    
    public static final OntClass SpatialThing = M_MODEL.createClass( "http://www.w3.org/2003/01/geo/wgs84_pos#SpatialThing" );
    
    public static final OntClass Observation = M_MODEL.createClass( "http://www.w3.org/ns/sosa/Observation " );
    
    public static final OntClass Platform = M_MODEL.createClass( "http://www.w3.org/ns/sosa/Platform" );
    
    public static final OntClass Sensor = M_MODEL.createClass( "http://www.w3.org/ns/sosa/Sensor" );
    
    public static final OntClass Age = M_MODEL.createClass( "https://w3id.org/iot/qoi#Age" );
    
    public static final OntClass Artificiality = M_MODEL.createClass( "https://w3id.org/iot/qoi#Artificiality " );
    
    public static final OntClass Completeness = M_MODEL.createClass( "https://w3id.org/iot/qoi#Completeness" );
    
    public static final OntClass Concordance = M_MODEL.createClass( "https://w3id.org/iot/qoi#Concordance" );
    
    public static final OntClass Frequency = M_MODEL.createClass( "https://w3id.org/iot/qoi#Frequency" );
    
    public static final OntClass Plausibility = M_MODEL.createClass( "https://w3id.org/iot/qoi#Plausibility" );
    
    public static final OntClass Quality = M_MODEL.createClass( "https://w3id.org/iot/qoi#Quality" );
    
    public static final OntClass Timeliness = M_MODEL.createClass( "https://w3id.org/iot/qoi#Timeliness" );

    
    /* 
     * Object Properties
     *  public static final ObjectProperty NOME = M_MODEL.createObjectProperty( "url" );
     * */
    
    public static final ObjectProperty hasQuantityKind = M_MODEL.createObjectProperty( "http://purl.oclc.org/NET/UNIS/fiware/iot-lite#hasQuantityKind " );
    
    public static final ObjectProperty hasUnit = M_MODEL.createObjectProperty( "http://purl.oclc.org/NET/UNIS/fiware/iot-lite#hasUnit" );
    
    public static final ObjectProperty analysedBy = M_MODEL.createObjectProperty( "http://purl.org/iot/ontology/iot-stream#analysedBy" );
    
    public static final ObjectProperty belongsTo = M_MODEL.createObjectProperty( "http://purl.org/iot/ontology/iot-stream#belongsTo" );
    
    public static final ObjectProperty derivedFrom = M_MODEL.createObjectProperty( "http://purl.org/iot/ontology/iot-stream#derivedFrom" );
    
    public static final ObjectProperty detectedFrom = M_MODEL.createObjectProperty( "http://purl.org/iot/ontology/iot-stream#detectedFrom" );
    
    public static final ObjectProperty generatedBy = M_MODEL.createObjectProperty( "http://purl.org/iot/ontology/iot-stream#generatedBy" );
    
    public static final ObjectProperty madeStreamObservation = M_MODEL.createObjectProperty( "http://purl.org/iot/ontology/iot-stream#madeStreamObservation" );
    
    public static final ObjectProperty providedBy = M_MODEL.createObjectProperty( "http://purl.org/iot/ontology/iot-stream#providedBy" );
    
    public static final ObjectProperty location = M_MODEL.createObjectProperty( "http://www.w3.org/2003/01/geo/wgs84_pos#location" );
    
    public static final ObjectProperty madeBySensor = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/madeBySensor" );
    
    public static final ObjectProperty madeObservation = M_MODEL.createObjectProperty( "http://www.w3.org/ns/sosa/madeObservation" );
    
    public static final ObjectProperty hasQuality = M_MODEL.createObjectProperty( "https://w3id.org/iot/qoi#hasQuality" );
    
    
    /* 
     * DATATYPE PROPERTIES
     *  public static final DatatypeProperty NOME = M_MODEL.createDatatypeProperty( "URL" );
     * */
     
    public static final DatatypeProperty endpoint = M_MODEL.createDatatypeProperty( "http://purl.oclc.org/NET/UNIS/fiware/iot-lite#endpoint" );
    
    public static final DatatypeProperty interfaceDescription = M_MODEL.createDatatypeProperty( "http://purl.oclc.org/NET/UNIS/fiware/iot-lite#interfaceDescription" );
   
    public static final DatatypeProperty interfaceType = M_MODEL.createDatatypeProperty( "http://purl.oclc.org/NET/UNIS/fiware/iot-lite#interfaceType" );
    
    public static final DatatypeProperty label = M_MODEL.createDatatypeProperty( "http://purl.org/iot/ontology/iot-stream#label" );
    
    public static final DatatypeProperty methods = M_MODEL.createDatatypeProperty( "http://purl.org/iot/ontology/iot-stream#methods" );
    
    public static final DatatypeProperty paramValues = M_MODEL.createDatatypeProperty( "http://purl.org/iot/ontology/iot-stream#paramValues" );
    
    public static final DatatypeProperty parameters = M_MODEL.createDatatypeProperty( "http://purl.org/iot/ontology/iot-stream#parameters" );
    
    public static final DatatypeProperty windowEnd = M_MODEL.createDatatypeProperty( "http://purl.org/iot/ontology/iot-stream#windowEnd" );
    
    public static final DatatypeProperty windowStart = M_MODEL.createDatatypeProperty( "http://purl.org/iot/ontology/iot-stream#windowStart" );
    
    public static final DatatypeProperty hasSimpleResult = M_MODEL.createDatatypeProperty( "http://www.w3.org/ns/sosa/hasSimpleResult" );
    
    public static final DatatypeProperty resultTime = M_MODEL.createDatatypeProperty( "http://www.w3.org/ns/sosa/resultTime" );
    
    public static final DatatypeProperty hasAbsoluteValue = M_MODEL.createDatatypeProperty( "https://w3id.org/iot/qoi#hasAbsoluteValue" );
    					
    					 
    						
}
