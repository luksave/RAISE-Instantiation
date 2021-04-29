package br.ufg.inf.mestrado.hemeswidget.client.semanticEnrichment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.List;


import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;
import com.hp.hpl.jena.util.FileManager;

import br.ufg.inf.mestrado.hermeswidget.ontologies.AQO3;
import br.ufg.inf.mestrado.hermeswidget.ontologies.SymptomAssociation;

public class SemanticEnrichment {

	static final String inputFileName = "./src/br/ufg/inf/mestrado/hermeswidget/ontologies/Dione.owl";
	
	public static OntModel enrichModelWithMTCS(OntModel modeloEnriquecido, String uriEnriquecida) throws FileNotFoundException {
		
		//Leitura das regras para associação de sintomas e indicadores
		BufferedReader rulesFileBR = new BufferedReader(new FileReader("./rules/regrasSintomasIndicador.txt"));
		List<Rule> rulesFile1 = Rule.parseRules( Rule.rulesParserFromReader(rulesFileBR) );
		
		Model ICD10CModel = ModelFactory.createDefaultModel();
		InputStream inputModel = FileManager.get().open(inputFileName);
		
		if (inputModel == null) throw new IllegalArgumentException( "File: " + inputFileName + " not found");
		
		ICD10CModel.read(inputModel, "");
		
		modeloEnriquecido.add(ICD10CModel);
		
		//Reasoning com as regras carregadas.
		Reasoner reasoner = new GenericRuleReasoner(rulesFile1);
		reasoner.setDerivationLogging(true);
		
		InfModel inference = ModelFactory.createInfModel(reasoner, modeloEnriquecido);
		
		//Inferência da categoria de indicador de qualidade do ar e busca por sintomas relacionados na janela de tempo.
		Resource sintomasResource = inference.getResource(AQO3.NS + "AQIndex/" + uriEnriquecida);
		
		//Adição da inferência hasIndexLevel índice ao modelo. 
		modeloEnriquecido.add(sintomasResource.getProperty(SymptomAssociation.hasSymptomsSet));
		
		return modeloEnriquecido;
		
	}
	
	
	/*public static void enrichModelSWRL(OntModel modeloEnriquecido, String uriEnriquecida) throws FileNotFoundException {	
		//A - Arquivo de regras
		String caminho = "./rules/regrasSWRL.owl";
		
		//B - Ler o .owl de regras e adicioná-lo ao modelo a ser enriquecido
		modeloEnriquecido.read(caminho, "RDF/XML");
		
		modeloEnriquecido.write(System.out, "N-TRIPLES");
		
		//C - Fazer o reasoning do modelo enriquecido
		Reasoner reasoner = modeloEnriquecido.getReasoner();
		         reasoner.setDerivationLogging(true);
		
		modeloEnriquecido.prepare();
		
		InfModel inference = ModelFactory.createInfModel(reasoner, modeloEnriquecido);
		
		//Inferência dos sintomas relacionados ao indice de qualidade do ar e janela de Tempo
		Resource sintomaResource = inference.getResource(AQO3.NS + "AQIndex/" + uriEnriquecida);
		System.out.println("\nRecurso de sintomas:" +sintomaResource);
		System.out.println("Index Level:" +sintomaResource.getPropertyResourceValue(AQO3.hasIndexLevel));
		System.out.println("Index Value:" +sintomaResource.getProperty(AQO3.hasIndexValue));
		System.out.println("Time Window:" +sintomaResource.getProperty(SymptomAssociation.hasTimeWindow));
		System.out.println("Derived By:" +sintomaResource.getPropertyResourceValue(AQO3.isDerivedBy));
		System.out.println("Index For:" +sintomaResource.getPropertyResourceValue(AQO3.isIndexFor));
		System.out.println("Conjunto de sintomas:" +sintomaResource.getPropertyResourceValue(SymptomAssociation.hasSymptomsSet)+"\n");
	
		
		//Adição da inferência hasSymptomsSet de conjunto de sintomas ao modelo original
		//modeloEnriquecido.add(sintomaResource.getProperty(SymptomAssociation.hasSymptomsSet));
		
		
		//return modeloEnriquecido;
		
	}*/
	
	
	public static OntModel enrichModelWithIQA (OntModel modeloEnriquecido, String uriEnriquecida) throws FileNotFoundException {
		
		//Leitura das regras para enquadramento da categoria de INDICADOR
		BufferedReader rulesFileBR = new BufferedReader(new FileReader("./rules/regrasCategoriasIndicador.txt"));
		List<Rule> rulesFile1 = Rule.parseRules( Rule.rulesParserFromReader(rulesFileBR) );
		
		//Reasoning com as regras carregadas.
		Reasoner reasoner = new GenericRuleReasoner(rulesFile1);
				 reasoner.setDerivationLogging(true);
		
	
		InfModel inference1 = ModelFactory.createInfModel(reasoner, modeloEnriquecido);
	
		
		//Inferência da categoria de indicador de qualidade do ar e busca por sintomas relacionados na janela de tempo.
		Resource categoriaResource1 = inference1.getResource(AQO3.NS + "IndicatorFor" + uriEnriquecida);
		
		
		//Leitura das regras para enquadramento da categoria de índice
		rulesFileBR = new BufferedReader(new FileReader("./rules/regrasCategoriasIndice.txt"));
		List<Rule> rulesFile2 = Rule.parseRules( Rule.rulesParserFromReader(rulesFileBR) );
		
		//Reasoning com as regras carregadas.
		reasoner = new GenericRuleReasoner(rulesFile2);
		reasoner.setDerivationLogging(true);
		InfModel inference2 = ModelFactory.createInfModel(reasoner, modeloEnriquecido);
				 
		
		//Inferência da categoria de índice de qualidade do ar.
		Resource categoriaResource2 = inference2.getResource(AQO3.NS + "AQIndex/" + uriEnriquecida);
		
				
		//Adição da inferência isCategorizedBy indicator e de sintomas ao modelo
		modeloEnriquecido.add(categoriaResource1.getProperty(AQO3.isCategorizedBy));
		
		//Adição da inferência hasIndexLevel índice ao modelo. 
		modeloEnriquecido.add(categoriaResource2.getProperty(AQO3.hasIndexLevel));
		
		
		return modeloEnriquecido;
		
	}
	
}
