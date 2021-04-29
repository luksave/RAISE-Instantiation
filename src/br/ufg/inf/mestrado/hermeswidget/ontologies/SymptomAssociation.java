package br.ufg.inf.mestrado.hermeswidget.ontologies;


import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;


public class SymptomAssociation {

	/** <p>The RDF model that holds the vocabulary terms</p> */
    public static final OntModel m_model = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM, null );
    
    /** <p>The namespace of the vocabulary as a string</p> */
    public static final String NS = "http://br.ufg.inf/mestrado/hermes/SymptomAssociation.owl/";
    
    /** <p>The namespace of the vocabulary as a string</p>
     * @return namespace as String
     * @see #NS */
    public static String getURI() {return NS;}
    
    /** <p>The namespace of the vocabulary as a resource</p> */
    public static final Resource NAMESPACE = m_model.createResource( NS );
    
    
    public static final Property hasSymptom = m_model.createProperty(NS + "hasSymptom");
    
    public static final Property hasSymptomsSet = m_model.createProperty(NS + "hasSymptomsSet");
    
    public static final Property SymptomsOf = m_model.createProperty(NS + "symptomsOf");
    
    
    
    public static final Property hasTimeWindow = m_model.createProperty(NS + "hasTimeWindow");
    
    public static final Property hasMinimumIndicatorValue = m_model.createProperty(NS + "hasMinimumIndicatorValue");
    
    
    public static final Property hasCUI = m_model.createProperty(NS + "hasCUI");
    

    public static final OntClass seriousSymptomsSet = m_model.createClass( NS + "seriousSymptomsSet" );
    
    public static final OntClass acuteSymptomsSet = m_model.createClass( NS + "acuteSymptomsSet" );
    
    public static final OntClass lightAndBriefSymptomsSet = m_model.createClass( NS + "lightAndBriefSymptomsSet" );
    
    public static final OntClass lightAndSuddenSymptomsSet = m_model.createClass( NS + "lightAndSuddenSymptomsSet" );
    
    
    @SuppressWarnings("unused")
	public static OntModel addSymptoms(OntModel modeloComSintomas) {
    	
    	//Sintomas que adiciono aos conjuntos

        //-------------------------------Serious
        Resource inflammationResource = modeloComSintomas.createResource("http://purl.obolibrary.org/obo/inflammation")
    			.addProperty(RDF.type, symp.inflammation)
    			.addProperty(symp.ID, "SYMP:0000061", XSDDatatype.XSDstring)
    			.addProperty(symp.Definition, "Inflammation is a general symptom where there is a local response to cellular injury that is marked by capillary dilatation, leukocytic infiltration, redness, heat, pain, swelling, and often loss of function and that serves as a mechanism initiating the elimination of noxious agents and of damaged tissue.")
    			.addProperty(symp.prefLabel, "inflammation")
    			.addProperty(RDFS.subClassOf, symp.generalSymptom);
        //SEM MAPEAMENTO NA ICD10
        
        //-------------------------------Acute
        Resource hypoventilationResource = modeloComSintomas.createResource("http://purl.obolibrary.org/obo/hypoventilation")
    			.addProperty(RDF.type, symp.hypoventilation)
    			.addProperty(symp.ID, "SYMP:0000428", XSDDatatype.XSDstring)
    			.addProperty(symp.Definition, "Hypoventilation is a respiratory abnormality characterized by a deficient ventilation of the lungs that results in reduction in the oxygen content or increase in the carbon dioxide content of the blood or both.")
    			.addProperty(symp.prefLabel, "hypoventilation")
    			.addProperty(RDFS.subClassOf, symp.respiratoryAbnormality);
        //SEM MAPEAMENTO NA ICD10
        
        //-------------------------------Light and Sudden
        Resource headacheResource = modeloComSintomas.createResource("http://purl.obolibrary.org/obo/headache")
    			.addProperty(RDF.type, symp.headache)
    			.addProperty(symp.ID, "SYMP:0000504", XSDDatatype.XSDstring)
    			.addProperty(symp.Definition, "Headache is a pain characterized by a pain in the head.")
    			.addProperty(symp.hasExactSynonym, "cephalalgia")
    			.addProperty(symp.prefLabel, "headache")
    			.addProperty(RDFS.subClassOf, symp.pain)
    			.addProperty(hasCUI, "C0018681");
    	
        Resource fatigueResource = modeloComSintomas.createResource("http://purl.obolibrary.org/obo/fatigue")
    			.addProperty(RDF.type, symp.fatigue)
    			.addProperty(symp.ID, "SYMP:0019177", XSDDatatype.XSDstring)
    			.addProperty(symp.Definition, "Fatigue is a neurological and physiological symptom characterized by a weariness or exhaustion from labor, exertion, or stress.")
    			.addProperty(symp.prefLabel, "fatigue")
    			.addProperty(RDFS.subClassOf, symp.neurologicalAndPhysiologicalSymptom);
    	
        Resource drowsinessResource = modeloComSintomas.createResource("http://purl.obolibrary.org/obo/drowsiness")
    			.addProperty(RDF.type, symp.drowsiness)
    			.addProperty(symp.ID, "SYMP:0000024", XSDDatatype.XSDstring)
    			.addProperty(symp.hasExactSynonym, "somnolence, drowsy")
    			.addProperty(symp.prefLabel, "drowsiness")
    			.addProperty(RDFS.subClassOf, symp.neurologicalAndPhysiologicalSymptom);
    	
        Resource irritabilityResource = modeloComSintomas.createResource("http://purl.obolibrary.org/obo/irritability")
    			.addProperty(RDF.type, symp.irritability)
    			.addProperty(symp.ID, "SYMP:0000654", XSDDatatype.XSDstring)
    			.addProperty(symp.Definition, "A behavioral symptom characterized by a quick excitability to annoyance, impatience, or anger.")
    			.addProperty(symp.hasExactSynonym, "inconsolability")
    			.addProperty(symp.prefLabel, "irritability")
    			.addProperty(RDFS.subClassOf, symp.behavioralSymptom);
    	
        //-------------------------------Light and Brief
        Resource inflamedEyesResource = modeloComSintomas.createResource("http://purl.obolibrary.org/obo/inflamedEye")
    			.addProperty(RDF.type, symp.inflamedEye)
    			.addProperty(symp.ID, "SYMP:0000310", XSDDatatype.XSDstring)
    			.addProperty(symp.prefLabel, "inflamed eyes")
    			.addProperty(RDFS.subClassOf, symp.eyeSymptom);
    	
        Resource strainEyesResource = modeloComSintomas.createResource("http://purl.obolibrary.org/obo/strainEye")
    			.addProperty(RDF.type, symp.strainEye)
    			.addProperty(symp.ID, "SYMP:0000212", XSDDatatype.XSDstring)
    			.addProperty(symp.prefLabel, "eye strain")
    			.addProperty(RDFS.subClassOf, symp.eyeSymptom);
    	
        Resource throatPainResource = modeloComSintomas.createResource("http://purl.obolibrary.org/obo/throatPain")
    			.addProperty(RDF.type, symp.throatPain)
    			.addProperty(symp.ID, "SYMP:0000505", XSDDatatype.XSDstring)
    			.addProperty(symp.Definition, "Throat pain is a pain that is characterized by a painful throat due to inflammation of the fauces and pharynx.")
    			.addProperty(symp.hasExactSynonym, "sore throat")
    			.addProperty(symp.prefLabel, "throat pain")
    			.addProperty(RDFS.subClassOf, symp.pain);
    	
        Resource runnyNoseResource = modeloComSintomas.createResource("http://purl.obolibrary.org/obo/runnyNose")
    			.addProperty(RDF.type, symp.runnyNose)
    			.addProperty(symp.ID, "SYMP:0000372", XSDDatatype.XSDstring)
    			.addProperty(symp.Definition, "Runny nose is a nose symptom characterized by an excessive mucous secretion from the nose.")
    			.addProperty(symp.hasExactSynonym, "sore throat")
    			.addProperty(RDFS.subClassOf, symp.noseSymptom);

        Resource coughResource = modeloComSintomas.createResource("http://purl.obolibrary.org/obo/cough")
    			.addProperty(RDF.type, symp.cough)
    			.addProperty(symp.ID, "SYMP:0000614", XSDDatatype.XSDstring)
    			.addProperty(symp.Definition, "Cough is a respiratory system and chest symptom characterized by an explosive expulsion of air from the lungs acting as a protective mechanism to clear the air passages or as a symptom of pulmonary disturbance.")
    			.addProperty(symp.prefLabel, "cough")
    			.addProperty(RDFS.subClassOf, symp.respiratorySystemAndChestSymptom);
    	
        Resource sneezingResource = modeloComSintomas.createResource("http://purl.obolibrary.org/obo/sneezing")
    			.addProperty(RDF.type, symp.sneezing)
    			.addProperty(symp.ID, "SYMP:0000139", XSDDatatype.XSDstring)
    			.addProperty(symp.Definition, "Sneezing is a respiratory system and chest symptom characterized by a sudden violent spasmodic audible expiration of breath through the nose and mouth especially as a reflex act following irritation of the nasal mucous membrane.")
    			.addProperty(symp.prefLabel, "sneezing")
    			.addProperty(RDFS.subClassOf, symp.respiratorySystemAndChestSymptom);
    	
        Resource wheezingResource = modeloComSintomas.createResource("http://purl.obolibrary.org/obo/wheezing")
    			.addProperty(RDF.type, symp.wheezing)
    			.addProperty(symp.ID, "SYMP:0000604", XSDDatatype.XSDstring)
    			.addProperty(symp.Definition, "Wheezing is a respiratory abnormality characterized by breathing with difficulty usually with a whistling sound.")
    			.addProperty(symp.prefLabel, "wheezing")
    			.addProperty(RDFS.subClassOf, symp.respiratoryAbnormality);
    	
        Resource chestPainResource = modeloComSintomas.createResource("http://purl.obolibrary.org/obo/chestPain")
    			.addProperty(RDF.type, symp.chestPain)
    			.addProperty(symp.ID, "SYMP:0000576", XSDDatatype.XSDstring)
    			.addProperty(symp.prefLabel, "chest pain")
    			.addProperty(RDFS.subClassOf, symp.pain);
    	
        Resource dyspneaResource = modeloComSintomas.createResource("http://purl.obolibrary.org/obo/dyspnea")
    			.addProperty(RDF.type, symp.dyspnea)
    			.addProperty(symp.ID, "SYMP:0019153", XSDDatatype.XSDstring)
    			.addProperty(symp.Definition, "Dyspnea is a respiratory abnormality characterized by difficult or labored respiration.")
    			.addProperty(symp.hasExactSynonym, "shortness of breath, breathing difficulty, breathlessness, labored respiration, difficulty breathing")
    			.addProperty(symp.prefLabel, "throat pain")
    			.addProperty(RDFS.subClassOf, symp.respiratoryAbnormality);

    
	    //Recursos dos conjuntos de Sintomas
	    
	    //EQUIVALENT TO: (isIndicatorOf CO2) AND (hasIndexValue > ) AND (hasTimeWindow = )
	    //					-> hasSetOfSymptoms seriousSymptomsSetResource
	    Resource seriousSymptomsSetResource = modeloComSintomas
				.createResource(SymptomAssociation.NS + "seriousSymptomsSetResource")
					.addProperty(RDF.type, SymptomAssociation.seriousSymptomsSet)
					.addProperty(SymptomAssociation.hasTimeWindow, "3", XSDDatatype.XSDinteger)
					.addProperty(SymptomAssociation.hasSymptom, inflammationResource);
	
	    
	    //EQUIVALENT TO: (isIndicatorOf CO2) AND (hasIndexValue > ) AND (hasTimeWindow = ) 
	    //					-> hasSetOfSymptoms acuteSymptomsSetResource
	    Resource acuteSymptomsSetResource = modeloComSintomas
				.createResource(SymptomAssociation.NS + "acuteSymptomsSetResource")
					.addProperty(RDF.type, SymptomAssociation.seriousSymptomsSet)
					.addProperty(SymptomAssociation.hasTimeWindow, "1", XSDDatatype.XSDinteger)
					.addProperty(SymptomAssociation.hasSymptom, hypoventilationResource);
	    
	    
	    //EQUIVALENT TO: (isIndicatorOf CO2) AND (hasIndexValue > ) AND (hasTimeWindow = ) 
	    //					-> hasSetOfSymptoms lightAndBriefSymptomsSetResource
	    Resource lightAndBriefSymptomsSetResource = modeloComSintomas
				.createResource(SymptomAssociation.NS + "lightAndBriefSymptomsSetResource")
					.addProperty(RDF.type, SymptomAssociation.lightAndBriefSymptomsSet)
					.addProperty(SymptomAssociation.hasTimeWindow, "3", XSDDatatype.XSDinteger)
					.addProperty(SymptomAssociation.hasSymptom, inflamedEyesResource)
					.addProperty(SymptomAssociation.hasSymptom, strainEyesResource)
					.addProperty(SymptomAssociation.hasSymptom, throatPainResource)
					.addProperty(SymptomAssociation.hasSymptom, runnyNoseResource)
					.addProperty(SymptomAssociation.hasSymptom, coughResource)
					.addProperty(SymptomAssociation.hasSymptom, sneezingResource)
					.addProperty(SymptomAssociation.hasSymptom, wheezingResource)
					.addProperty(SymptomAssociation.hasSymptom, chestPainResource)
					.addProperty(SymptomAssociation.hasSymptom, dyspneaResource);
	    
	    
	    //EQUIVALENT TO: (isIndicatorOf CO2) AND (hasIndexValue > ) AND (hasTimeWindow = ) 
	    //					-> hasSetOfSymptoms lightAndSuddenSymptomsSetResource
	    Resource lightAndSuddenSymptomsSetResource = modeloComSintomas
				.createResource(SymptomAssociation.NS + "lightAndSuddenSymptomsSetResource")
					.addProperty(RDF.type, SymptomAssociation.lightAndSuddenSymptomsSet)
					.addProperty(SymptomAssociation.hasTimeWindow, "1", XSDDatatype.XSDinteger)
					.addProperty(SymptomAssociation.hasSymptom, headacheResource)
					.addProperty(SymptomAssociation.hasSymptom, fatigueResource)
					.addProperty(SymptomAssociation.hasSymptom, drowsinessResource)
					.addProperty(SymptomAssociation.hasSymptom, irritabilityResource);
	    
	    //EQUIVALENT TO: (isIndicatorOf CO2) AND (hasIndexValue > ) AND (hasTimeWindow = ) 
	    //					-> hasSetOfSymptoms lightAndSuddenSymptomsSetResource
	    Resource noSymptomsSetResource = modeloComSintomas
				.createResource(SymptomAssociation.NS + "noSymptomsSetResource")
					.addProperty(SymptomAssociation.hasTimeWindow, "xxx", XSDDatatype.XSDstring);
	 
	    return modeloComSintomas;
	    
    }
    
}
