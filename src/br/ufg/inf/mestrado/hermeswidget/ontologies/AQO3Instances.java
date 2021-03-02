package br.ufg.inf.mestrado.hermeswidget.ontologies;

import com.hp.hpl.jena.vocabulary.RDFS;

public class AQO3Instances {
	
	public void createIndividuals() {
	
		/** 	
		 * Indicadores Fazem referência a um poluente P
		 * Temos 6 categorias para o indicador de CO2 (a instância de <AQ_User_Indicator> para CO2)
		 * CO2_Good       0 - 40
		 * CO2_Regular   41 - 100
		 * CO2_Poor     101 - 199
		 * CO2_Bad      200 - 299
		 * CO2_Lousy    300 - 399 (Inadequado)
		 * CO2_Critical 400 ou maior
		 * Essas faixas de valores para cada categoria deve ser instanciadas.
		 * 
		 * Essas faixas são subclasses das classes em IndicatorCategorization, já que cada poluente possui
		 * seu próprio limite superior e inferior.
		 *  
		 */
	
		AQO3.CO2_Good.addProperty(AQO3.hasLowerLimit, "0");
		AQO3.CO2_Good.addProperty(AQO3.hasUpperLimit, "40");
		AQO3.CO2_Good.addProperty(RDFS.subClassOf, AQO3.IC_Good);
		
		AQO3.CO2_Regular.addProperty(AQO3.hasLowerLimit, "41");
		AQO3.CO2_Regular.addProperty(AQO3.hasUpperLimit, "100");
		AQO3.CO2_Regular.addProperty(RDFS.subClassOf, AQO3.IC_Regular);
		
		AQO3.CO2_Poor.addProperty(AQO3.hasLowerLimit, "101");
		AQO3.CO2_Poor.addProperty(AQO3.hasUpperLimit, "299");
		AQO3.CO2_Poor.addProperty(RDFS.subClassOf, AQO3.IC_Poor);
		
		AQO3.CO2_Bad.addProperty(AQO3.hasLowerLimit, "200");
		AQO3.CO2_Bad.addProperty(AQO3.hasUpperLimit, "299");
		AQO3.CO2_Bad.addProperty(RDFS.subClassOf, AQO3.IC_Bad);
		
		AQO3.CO2_Lousy.addProperty(AQO3.hasLowerLimit, "300");
		AQO3.CO2_Lousy.addProperty(AQO3.hasUpperLimit, "399");
		AQO3.CO2_Lousy.addProperty(RDFS.subClassOf, AQO3.IC_Lousy);
		
		AQO3.CO2_Critical.addProperty(AQO3.hasLowerLimit, "400");
		AQO3.CO2_Critical.addProperty(AQO3.hasUpperLimit, "500");
		AQO3.CO2_Critical.addProperty(RDFS.subClassOf, AQO3.IC_Critical);
		
		
		/** Cada IndicatorCategorization é, por sua vez, uma subclasse de AQ_Categorizations */
		
		AQO3.IC_Good.addProperty(RDFS.subClassOf, AQO3.AQ_Categorizations);
		AQO3.IC_Regular.addProperty(RDFS.subClassOf, AQO3.AQ_Categorizations);
		AQO3.IC_Poor.addProperty(RDFS.subClassOf, AQO3.AQ_Categorizations);
		AQO3.IC_Bad.addProperty(RDFS.subClassOf, AQO3.AQ_Categorizations);
		AQO3.IC_Lousy.addProperty(RDFS.subClassOf, AQO3.AQ_Categorizations);
		AQO3.IC_Critical.addProperty(RDFS.subClassOf, AQO3.AQ_Categorizations);
	
		
	}
	
}
