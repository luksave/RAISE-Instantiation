package br.ufg.inf.mestrado.hermeswidget.client.services;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class HWRepresentationService {
	
	/*
	 * Verificar a utilização destes atributos privados, para evitar acesso direto.
	 * Verificar impacto nas classes que estendem esse objeto de representação.
	 */

	public String caminhoSchemaOntologico = "./src/ontologia/iotliteSchema.ttl";
	public String tipoSerializacao = "Turtle";
	public OntModel modeloMedicaoDadoAmbiental = null;
	

	public void criarModeloRDFDeArquivo(String caminhoArquivoRDF){
		if (modeloMedicaoDadoAmbiental == null) modeloMedicaoDadoAmbiental = ModelFactory.createOntologyModel();	
	
	}
	
}