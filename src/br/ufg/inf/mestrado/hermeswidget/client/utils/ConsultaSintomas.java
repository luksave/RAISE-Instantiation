package br.ufg.inf.mestrado.hermeswidget.client.utils;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import br.ufg.inf.mestrado.hermeswidget.ontologies.AQO3;
import br.ufg.inf.mestrado.hermeswidget.ontologies.symp;

public class ConsultaSintomas {
	/**
	 * Recebe uma qualidade do ar (por tempo, por poluente - conforme c�lculado)
	 * Relaciona Sintomas �s diferentes qualidades do ar
	 * Descobre os identificadores �nicos dos sintomas na ICD10
	 * Retorna uma lista de recursos, que s�o os sintomas encontrados.
	 * Programa principal cria um recurso conjuntoSintomas que possui,<qualidadeAr>, <janelaTempo>, <WindowStart>, e <WindowEnd>.
	 * O recurso conjuntoSintomas est� ligado a cada sintoma retornado pelo m�todo atrav�s de uma propriedade <hasSymptoms>.
	 * O recurso conjuntoSintomas � ent�o ligado � qualidade do ar atrav�s de uma propriedade <hasAssociatedSymptoms>.
	 * 
	 * N�o preciso fazer uma consulta aqui... � s� montar o recurso e retornar
	 * 
	 */
	public static List<Resource> listaSintomas(Model modelo, RDFNode qualidade, int janelaTempo){
		
		List<Resource> listaSintomas = new ArrayList<Resource>();
		
		/**
		 * Baixa exposi��o ao CO2: Todas abaixo de 1000ppm em qualquer per�odo de tempo
		 * Concentra��es abaixo de 1000 ppm s�o classificadas entre CO2_Good e CO2_Regular
		 * 
		 * Para efeito de adequa��o � maior parte dos estudos, consideramos apenas a qualidade CO2_Regular,
		 * dado que exposi��es abaixo de 500ppm (enquadrado na faixa CO2_Good) raramente provoca sintomas.
		 * 
		 */
		
		if(qualidade.equals(AQO3.CO2_Good)) {
			System.out.println("Qualidade do Ar ideal.");
			return null;
			
		}
		
		//Como os sintomas apenas pioram conforme a concentra��o e exposi��o, consideramo-os acumulativos
		if((qualidade.equals(AQO3.CO2_Regular) || qualidade.equals(AQO3.CO2_Poor) || qualidade.equals(AQO3.CO2_Bad) ||
			qualidade.equals(AQO3.CO2_Lousy)   || qualidade.equals(AQO3.CO2_Critical)) && janelaTempo >= 1) {
			
			System.out.println("SINTOMAS: Mudan�a na amplitude respirat�ria.\n"
					                   + "Aumento no fluxo sangu�neo perif�rico. \n"
					                   + "Mudan�as no estado de funcionamento do c�rtex cerebral. \n"
					                   + "Dor de cabe�a. \n"
					                   + "Fadiga. \n"
					                   + "Dificuldade de concentra��o. \n"
					                   + "Tens�o. \n"
					                   + "Irritabilidade.");
			
			
			Resource headacheResource = modelo.createResource("http://purl.obolibrary.org/obo/headache")
					.addProperty(RDF.type, symp.headache)
					.addProperty(symp.ID, "SYMP:0000504", XSDDatatype.XSDstring)
					.addProperty(symp.Definition, "Headache is a pain characterized by a pain in the head.")
					.addProperty(symp.hasExactSynonym, "cephalalgia")
					.addProperty(symp.prefLabel, "headache")
					.addProperty(RDFS.subClassOf, symp.pain);
			
			listaSintomas.add(headacheResource);
			
			
			Resource fatigueResource = modelo.createResource("http://purl.obolibrary.org/obo/fatigue")
					.addProperty(RDF.type, symp.fatigue)
					.addProperty(symp.ID, "SYMP:0019177", XSDDatatype.XSDstring)
					.addProperty(symp.Definition, "Fatigue is a neurological and physiological symptom characterized by a weariness or exhaustion from labor, exertion, or stress.")
					.addProperty(symp.prefLabel, "fatigue")
					.addProperty(RDFS.subClassOf, symp.neurologicalAndPhysiologicalSymptom);
			
			listaSintomas.add(fatigueResource);
			
			
			Resource drowsinessResource = modelo.createResource("http://purl.obolibrary.org/obo/drowsiness")
					.addProperty(RDF.type, symp.drowsiness)
					.addProperty(symp.ID, "SYMP:0000024", XSDDatatype.XSDstring)
					.addProperty(symp.hasExactSynonym, "somnolence, drowsy")
					.addProperty(symp.prefLabel, "drowsiness")
					.addProperty(RDFS.subClassOf, symp.neurologicalAndPhysiologicalSymptom);
			
			listaSintomas.add(drowsinessResource);
			
			
			Resource irritabilityResource = modelo.createResource("http://purl.obolibrary.org/obo/irritability")
					.addProperty(RDF.type, symp.irritability)
					.addProperty(symp.ID, "SYMP:0000654", XSDDatatype.XSDstring)
					.addProperty(symp.Definition, "A behavioral symptom characterized by a quick excitability to annoyance, impatience, or anger.")
					.addProperty(symp.hasExactSynonym, "inconsolability")
					.addProperty(symp.prefLabel, "irritability")
					.addProperty(RDFS.subClassOf, symp.behavioralSymptom);
			
			listaSintomas.add(irritabilityResource);
			
			
			if(janelaTempo >= 3) {
				System.out.println("Sintomas por exposi��o prolongada:Irrita��o nos olhos e Sintomas do trato respirat�rio. \n"
								  	   + "Olhos secos, co�ando ou irritados. \n"
								  	   + "Olhos cansados ou tensos. \n"
								  	   + "Garganta seca ou dolorida. \n"
								  	   + "Nariz congestionado ou escorrendo. \n"
								  	   + "Tosse. \n"
								  	   + "Espirro. \n"
								  	   + "Chiado. \n"
								  	   + "Peito Apertado. \n"
								  	   + "Respira��o Curta");
				
				
				Resource inflamedEyesResource = modelo.createResource("http://purl.obolibrary.org/obo/inflamedEye")
						.addProperty(RDF.type, symp.inflamedEye)
						.addProperty(symp.ID, "SYMP:0000310", XSDDatatype.XSDstring)
						.addProperty(symp.prefLabel, "inflamed eyes")
						.addProperty(RDFS.subClassOf, symp.eyeSymptom);
				
				listaSintomas.add(inflamedEyesResource);
				
				
				Resource strainEyesResource = modelo.createResource("http://purl.obolibrary.org/obo/strainEye")
						.addProperty(RDF.type, symp.strainEye)
						.addProperty(symp.ID, "SYMP:0000212", XSDDatatype.XSDstring)
						.addProperty(symp.prefLabel, "eye strain")
						.addProperty(RDFS.subClassOf, symp.eyeSymptom);
				
				listaSintomas.add(strainEyesResource);
				
				
				Resource throatPainResource = modelo.createResource("http://purl.obolibrary.org/obo/throatPain")
						.addProperty(RDF.type, symp.throatPain)
						.addProperty(symp.ID, "SYMP:0000505", XSDDatatype.XSDstring)
						.addProperty(symp.Definition, "Throat pain is a pain that is characterized by a painful throat due to inflammation of the fauces and pharynx.")
						.addProperty(symp.hasExactSynonym, "sore throat")
						.addProperty(symp.prefLabel, "throat pain")
						.addProperty(RDFS.subClassOf, symp.pain);
				
				listaSintomas.add(throatPainResource);
				
				
				Resource runnyNoseResource = modelo.createResource("http://purl.obolibrary.org/obo/runnyNose")
						.addProperty(RDF.type, symp.runnyNose)
						.addProperty(symp.ID, "SYMP:0000372", XSDDatatype.XSDstring)
						.addProperty(symp.Definition, "Runny nose is a nose symptom characterized by an excessive mucous secretion from the nose.")
						.addProperty(symp.hasExactSynonym, "sore throat")
						.addProperty(RDFS.subClassOf, symp.noseSymptom);
				
				listaSintomas.add(runnyNoseResource);

				
				Resource coughResource = modelo.createResource("http://purl.obolibrary.org/obo/cough")
						.addProperty(RDF.type, symp.cough)
						.addProperty(symp.ID, "SYMP:0000614", XSDDatatype.XSDstring)
						.addProperty(symp.Definition, "Cough is a respiratory system and chest symptom characterized by an explosive expulsion of air from the lungs acting as a protective mechanism to clear the air passages or as a symptom of pulmonary disturbance.")
						.addProperty(symp.prefLabel, "cough")
						.addProperty(RDFS.subClassOf, symp.respiratorySystemAndChestSymptom);
				
				listaSintomas.add(coughResource);
				
				
				Resource sneezingResource = modelo.createResource("http://purl.obolibrary.org/obo/sneezing")
						.addProperty(RDF.type, symp.sneezing)
						.addProperty(symp.ID, "SYMP:0000139", XSDDatatype.XSDstring)
						.addProperty(symp.Definition, "Sneezing is a respiratory system and chest symptom characterized by a sudden violent spasmodic audible expiration of breath through the nose and mouth especially as a reflex act following irritation of the nasal mucous membrane.")
						.addProperty(symp.prefLabel, "sneezing")
						.addProperty(RDFS.subClassOf, symp.respiratorySystemAndChestSymptom);
				
				listaSintomas.add(sneezingResource);
				
				
				Resource wheezingResource = modelo.createResource("http://purl.obolibrary.org/obo/wheezing")
						.addProperty(RDF.type, symp.wheezing)
						.addProperty(symp.ID, "SYMP:0000604", XSDDatatype.XSDstring)
						.addProperty(symp.Definition, "Wheezing is a respiratory abnormality characterized by breathing with difficulty usually with a whistling sound.")
						.addProperty(symp.prefLabel, "wheezing")
						.addProperty(RDFS.subClassOf, symp.respiratoryAbnormality);
				
				listaSintomas.add(wheezingResource);
				
				
				Resource chestPainResource = modelo.createResource("http://purl.obolibrary.org/obo/chestPain")
						.addProperty(RDF.type, symp.chestPain)
						.addProperty(symp.ID, "SYMP:0000576", XSDDatatype.XSDstring)
						.addProperty(symp.prefLabel, "chest pain")
						.addProperty(RDFS.subClassOf, symp.pain);
				
				listaSintomas.add(chestPainResource);
				
				
				Resource dyspneaResource = modelo.createResource("http://purl.obolibrary.org/obo/dyspnea")
						.addProperty(RDF.type, symp.dyspnea)
						.addProperty(symp.ID, "SYMP:0019153", XSDDatatype.XSDstring)
						.addProperty(symp.Definition, "Dyspnea is a respiratory abnormality characterized by difficult or labored respiration.")
						.addProperty(symp.hasExactSynonym, "shortness of breath, breathing difficulty, breathlessness, labored respiration, difficulty breathing")
						.addProperty(symp.prefLabel, "throat pain")
						.addProperty(RDFS.subClassOf, symp.respiratoryAbnormality);
				
				listaSintomas.add(dyspneaResource);
				
				
			}
			
			System.out.println("Os sintomas apresentados est�o relacionados ao trato respirat�rio superior e inferior, olhos, pele, e outros.");

					
		}
		
		/**
		 * Exposi��o severa entre 1000 e 5000ppm em per�odos de 1 a 6 horas
		 * Isto � CO2_Poor, CO2_Bad, e CO2_Lousy.
		 */
		
		if((qualidade.equals( AQO3.CO2_Poor) || qualidade.equals(AQO3.CO2_Bad) || qualidade.equals(AQO3.CO2_Lousy) ||
			qualidade.equals(AQO3.CO2_Critical)&& (janelaTempo >=1 && janelaTempo <=6))) {
			System.out.println("Sintomas por exposi��o severa e prolongada: Reten��o de CO2. \n"
					                   + "Hypoventila��o. \n"
					                   + "Efeitos cognitivos.");
			
			Resource hypoventilationResource = modelo.createResource("http://purl.obolibrary.org/obo/hypoventilation")
					.addProperty(RDF.type, symp.hypoventilation)
					.addProperty(symp.ID, "SYMP:0000428", XSDDatatype.XSDstring)
					.addProperty(symp.Definition, "Hypoventilation is a respiratory abnormality characterized by a deficient ventilation of the lungs that results in reduction in the oxygen content or increase in the carbon dioxide content of the blood or both.")
					.addProperty(symp.prefLabel, "hypoventilation")
					.addProperty(RDFS.subClassOf, symp.respiratoryAbnormality);
			
			listaSintomas.add(hypoventilationResource);
			
			if((qualidade.equals(AQO3.CO2_Lousy) || qualidade.equals(AQO3.CO2_Critical)) && janelaTempo >=2) {
				System.out.println("Sintomas por exposi��o severa e prolongada: Inflama��es. \n");
				
				Resource inflammationResource = modelo.createResource("http://purl.obolibrary.org/obo/inflammation")
						.addProperty(RDF.type, symp.inflammation)
						.addProperty(symp.ID, "SYMP:0000061", XSDDatatype.XSDstring)
						.addProperty(symp.Definition, "Inflammation is a general symptom where there is a local response to cellular injury that is marked by capillary dilatation, leukocytic infiltration, redness, heat, pain, swelling, and often loss of function and that serves as a mechanism initiating the elimination of noxious agents and of damaged tissue.")
						.addProperty(symp.prefLabel, "inflammation")
						.addProperty(RDFS.subClassOf, symp.generalSymptom);
				
				listaSintomas.add(inflammationResource);
				
			}
			
		}
		
		return listaSintomas;
	
	}
	
}
