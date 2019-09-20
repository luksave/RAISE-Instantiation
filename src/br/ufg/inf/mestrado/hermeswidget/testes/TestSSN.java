package br.ufg.inf.mestrado.hermeswidget.testes;

import br.ufg.inf.mestrado.hermeswidget.ontologies.SSN;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.AllDifferent;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;

public class TestSSN {
	
	public static OntModel bloodPressureObservation = ModelFactory.createOntologyModel();
	public static AllDifferent allDiff = bloodPressureObservation.createAllDifferent();
	
	public static void main(String[] args) {

		String observation1IRI = "BloodPressureObservation001";
		String observation2IRI = "BloodPressureObservation002";
		
		String sensor1IRI = "BloodPressureSensor001";
		String sensor2IRI = "BloodPressureSensor002";
		
		String propertyIRI = "BloodPressure";
		
		String sensorOutput1IRI = "SystolicBloodPressureSensorOutput";
		String sensorOutput2IRI = "DiastolicBloodPressureSensorOutput";
		
		String observationValue1IRI = "SystolicBloodPressureObservationValue";
		String observationValue2IRI = "DiastolicBloodPressureObservationValue";
		
		
		//String stimulusIRI = "BloodPressure_SensorInput";
		//String sensingIRI = "BloodPressure_Sensing";
		//String featureOfInterestIRI = "BloodPressure";

		//Model bloodPressureObservation = ModelFactory.createDefaultModel();

		/**
		 * Sensing is a process that results in the estimation, or calculation,
		 * of the value of a phenomenon.
		 */
		/*
		Resource sensing = bloodPressureObservation
				.createResource(SSN.NS + sensingIRI)
					.addProperty(RDF.type, SSN.Sensing);
		*/
		
		/**
		 * A feature is an abstraction of real world phenomena (thing, person,
		 * event, etc).
		 */
		/*
		Resource featureOfInterest = bloodPressureObservation
				.createResource(SSN.NS + featureOfInterestIRI)
					.addProperty(RDF.type, SSN.FeatureOfInterest);
		*/	
		
		/**
		 * An Event in the real world that 'triggers' the sensor. The properties
		 * associated to the stimulus may be different to eventual observed
		 * property. It is the event, not the object that triggers the sensor.
		 */
		// Simulus is equivalents with 'Sensor Input'
		/*
		Resource stimulus = bloodPressureObservation
				.createResource(SSN.NS + stimulusIRI)
					.addProperty(RDF.type, SSN.Stimulus)
					.addProperty(SSN.isProxyFor, property);
		*/
		

		
		/**
		 * An observable Quality of an Event or Object. That is, not a quality
		 * of an abstract entity as is also allowed by DUL's Quality, but rather
		 * an aspect of an entity that is intrinsic to and cannot exist without
		 * the entity and is observable by a sensor.
		 */
		Resource property = bloodPressureObservation
				.createResource(SSN.NS + propertyIRI)
					.addProperty(RDF.type, SSN.Property);

		/**
		 * A sensor can do (implements) sensing: that is, a sensor is any entity
		 * that can follow a sensing method and thus observe some Property of a
		 * FeatureOfInterest. Sensors may be physical devices, computational
		 * methods, a laboratory setup with a person following a method, or any
		 * other thing that can follow a Sensing Method to observe a Property.
		 */
		Resource sensor1 = bloodPressureObservation
				.createResource(SSN.NS + sensor1IRI)
					.addProperty(RDF.type, SSN.SensingDevice)
					.addProperty(SSN.observes, property);
					//.addProperty(SSN.detects, stimulus)
					//.addProperty(SSN.identifier, bloodPressureObservation.createTypedLiteral(new String("VSO_0000005_001")));

		allDiff.addDistinctMember(sensor1);
		
		Resource sensor2 = bloodPressureObservation
				.createResource(SSN.NS + sensor2IRI)
					.addProperty(RDF.type, SSN.SensingDevice)
					.addProperty(SSN.observes, property);
					//.addProperty(SSN.detects, stimulus)
					//.addProperty(SSN.identifier, bloodPressureObservation.createTypedLiteral(new String("VSO_0000005_002")));
		
		allDiff.addDistinctMember(sensor2);
		
		//TESTE CONSISTÊNCIA
		//sensor1.addProperty(OWL.sameAs, sensor2);
		
		/**
		 * A sensor outputs a piece of information (an observed value), the
		 * value itself being represented by an ObservationValue.
		 * 
		 * Cada sensorOutput está ligado a um sensor pela propriedade 
		 * ssn:isProducedBy
		 */
		
		Resource sensorOutput1 = bloodPressureObservation
				.createResource(SSN.NS + sensorOutput1IRI +"_"+ sensor1IRI)
					.addProperty(RDF.type, SSN.SensorOutput)
					.addProperty(SSN.isProducedBy, sensor1);
		
		Resource sensorOutput2 = bloodPressureObservation
				.createResource(SSN.NS + sensorOutput2IRI +"_"+ sensor1IRI)
					.addProperty(RDF.type, SSN.SensorOutput)
					.addProperty(SSN.isProducedBy, sensor1);
		
		Resource sensorOutput3 = bloodPressureObservation
				.createResource(SSN.NS + sensorOutput1IRI +"_"+ sensor2IRI)
					.addProperty(RDF.type, SSN.SensorOutput)
					.addProperty(SSN.isProducedBy, sensor2);
		
		Resource sensorOutput4 = bloodPressureObservation
				.createResource(SSN.NS + sensorOutput2IRI +"_"+ sensor2IRI)
					.addProperty(RDF.type, SSN.SensorOutput)
					.addProperty(SSN.isProducedBy, sensor2);
					

		/**
		 * An Observation is a Situation in which a Sensing method has been used
		 * to estimate or calculate a value of a Property of a
		 * FeatureOfInterest. Links to Sensing and Sensor describe what made the
		 * Observation and how; links to Property and Feature detail what was
		 * sensed; the result is the output of a Sensor; other metadata details
		 * times etc.
		 * 
		 * Cada observation está ligada a um sensor pela propriedade
		 * ssn:observedBy
		 */
		bloodPressureObservation.createResource(SSN.NS + observation1IRI)
				.addProperty(RDF.type, SSN.Observation)
				.addProperty(SSN.observedBy, sensor1)
				.addProperty(SSN.observationResult, sensorOutput1)
		        .addProperty(SSN.observationResult, sensorOutput2);
				//.addProperty(SSN.observedProperty, property);
				//.addProperty(SSN.sensingMethodUsed, sensing);
				//.addProperty(SSN.featureOfInterest, featureOfInterest);
		
		bloodPressureObservation.createResource(SSN.NS + observation2IRI)
		.addProperty(RDF.type, SSN.Observation)
		.addProperty(SSN.observedBy, sensor2)
		.addProperty(SSN.observationResult, sensorOutput3)
		.addProperty(SSN.observationResult, sensorOutput4);
		//.addProperty(SSN.observedProperty, property);
		//.addProperty(SSN.sensingMethodUsed, sensing);
		//.addProperty(SSN.featureOfInterest, featureOfInterest);
		
		sensorOutput1.addProperty(SSN.hasValue, 
				bloodPressureObservation.createResource(SSN.NS + observationValue1IRI+"_"+sensor1IRI)
					.addProperty(RDF.type, SSN.ObservationValue)
					.addProperty(SSN.hasQuantityValue, bloodPressureObservation.createTypedLiteral(133, XSDDatatype.XSDnonNegativeInteger))
				);
		
		sensorOutput2.addProperty(SSN.hasValue, 
				bloodPressureObservation.createResource(SSN.NS + observationValue2IRI+"_"+sensor1IRI)
					.addProperty(RDF.type, SSN.ObservationValue)
					.addProperty(SSN.hasQuantityValue, bloodPressureObservation.createTypedLiteral(84, XSDDatatype.XSDnonNegativeInteger))
				);
		
		sensorOutput3.addProperty(SSN.hasValue, 
				bloodPressureObservation.createResource(SSN.NS + observationValue1IRI+"_"+sensor2IRI)
					.addProperty(RDF.type, SSN.ObservationValue)
					.addProperty(SSN.hasQuantityValue, bloodPressureObservation.createTypedLiteral(120, XSDDatatype.XSDnonNegativeInteger))
				);
		
		sensorOutput4.addProperty(SSN.hasValue, 
				bloodPressureObservation.createResource(SSN.NS + observationValue2IRI+"_"+sensor2IRI)
					.addProperty(RDF.type, SSN.ObservationValue)
					.addProperty(SSN.hasQuantityValue, bloodPressureObservation.createTypedLiteral(80, XSDDatatype.XSDnonNegativeInteger))
				);
		
		bloodPressureObservation.write(System.out);
		
		
		Model schema = FileManager.get().loadModel("src/ontologia/ssn/ssn.owl");
		Reasoner reasoner = ReasonerRegistry.getOWLMicroReasoner();
		reasoner = reasoner.bindSchema(schema);
		InfModel infModel = ModelFactory.createInfModel(reasoner, bloodPressureObservation);
		ValidityReport validity = infModel.validate();
		
		System.out.println("\n\n\n"+validity.isValid());
		
		
	}
	
}
