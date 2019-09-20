package br.ufg.inf.mestrado.hermeswidget.client.services;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class HWRepresentationService {
	
	/*
	 * Verificar a utilização destes atributos privados, para evitar acesso direto.
	 * Verificar impacto nas classes que estendem esse objeto de representação.
	 */
	
	//public String caminhoSchemaOntologico = "./src/ontologia/ssnSchema.ttl";
	public String caminhoSchemaOntologico = "./src/ontologia/iotliteSchema.ttl";
	public String tipoSerializacao = "Turtle";
	public OntModel modeloMedicaoSinalVital = null;
	
	public void criarModeloRDFDeArquivo(String caminhoArquivoRDF)
	{
		
		if (modeloMedicaoSinalVital == null)
		{
			modeloMedicaoSinalVital = ModelFactory.createOntologyModel();	
		}
		
		
		/*
		try {
			FileInputStream arquivoRDF = new FileInputStream(caminhoArquivoRDF);
			modeloMedicaoSinalVital.read(arquivoRDF, caminhoSchemaOntologico, tipoSerializacao);
			arquivoRDF.close();
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		*/
	}
	
	//abstract public HermesWidgetTransferObject getDataTransferObject(String idEntity, String observations, String complementoTopico, byte[] medidaByteArray, String valorColetado, String instanteMedidaColetada);
	
}
