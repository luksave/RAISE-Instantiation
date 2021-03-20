package br.ufg.inf.mestrado.hemeswidget.client.semanticEnrichment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;


import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.reasoner.rulesys.GenericRuleReasoner;
import com.hp.hpl.jena.reasoner.rulesys.Rule;

import br.ufg.inf.mestrado.hermeswidget.ontologies.AQO3;
import br.ufg.inf.mestrado.hermeswidget.ontologies.SymptomAssociation;

public class SemanticEnrichment {

	public static OntModel enrichModelWithMTCS(OntModel modeloEnriquecido, String uriEnriquecida) throws FileNotFoundException {
		
		//Leitura das regras para associação de sintomas e indicadores
		BufferedReader rulesFileBR = new BufferedReader(new FileReader("./rules/regrasSintomasIndicador.txt"));
		List<Rule> rulesFile1 = Rule.parseRules( Rule.rulesParserFromReader(rulesFileBR) );
		
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
	
	
	public static OntModel enrichModelWithIQA (OntModel modeloEnriquecido, String uriEnriquecida) throws FileNotFoundException {
		
		//Leitura das regras para enquadramento da categoria de INDICADOR
		BufferedReader rulesFileBR = new BufferedReader(new FileReader("./rules/regrasCategoriasIndicador.txt"));
		List<Rule> rulesFile1 = Rule.parseRules( Rule.rulesParserFromReader(rulesFileBR) );
		
		//Reasoning com as regras carregadas.
		Reasoner reasoner = new GenericRuleReasoner(rulesFile1);
		
		//Reasoner pelletReasoner = modeloMedicaoDadoAmbiental.getReasoner();
		reasoner.setDerivationLogging(true);
		
		//pelletReasoner.setDerivationLogging(true);
		//modeloMedicaoDadoAmbiental.prepare();
		InfModel inference1 = ModelFactory.createInfModel(reasoner, modeloEnriquecido);
		
		//Model dedModel = modeloMedicaoDadoAmbiental.getDeductionsModel();
		//if (dedModel == null) System.out.println("DedModel is null"); // it is null ...
		//else dedModel.write(System.out,"TURTLE");
		
		//Model rawModel = modeloMedicaoDadoAmbiental.getRawModel();
		//Model diffModel = modeloMedicaoDadoAmbiental.difference(rawModel); // .difference(dedModel);
		
		//System.out.println("---- DiffModel: ----------------------");
		//diffModel.write(System.out,"TURTLE"); // everything is in the DiffModel
		
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
