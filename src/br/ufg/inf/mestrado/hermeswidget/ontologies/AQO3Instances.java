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
		AQO3.CO2_Good.addProperty(AQO3.hasUpperLimit, "50");
		AQO3.CO2_Good.addProperty(RDFS.subClassOf, AQO3.IC_Good);
		
		AQO3.CO2_Regular.addProperty(AQO3.hasLowerLimit, "51");
		AQO3.CO2_Regular.addProperty(AQO3.hasUpperLimit, "100");
		AQO3.CO2_Regular.addProperty(RDFS.subClassOf, AQO3.IC_Regular);
		
		AQO3.CO2_Poor.addProperty(AQO3.hasLowerLimit, "101");
		AQO3.CO2_Poor.addProperty(AQO3.hasUpperLimit, "150");
		AQO3.CO2_Poor.addProperty(RDFS.subClassOf, AQO3.IC_Poor);
		
		AQO3.CO2_Bad.addProperty(AQO3.hasLowerLimit, "151");
		AQO3.CO2_Bad.addProperty(AQO3.hasUpperLimit, "200");
		AQO3.CO2_Bad.addProperty(RDFS.subClassOf, AQO3.IC_Bad);
		
		AQO3.CO2_Lousy.addProperty(AQO3.hasLowerLimit, "201");
		AQO3.CO2_Lousy.addProperty(AQO3.hasUpperLimit, "400");
		AQO3.CO2_Lousy.addProperty(RDFS.subClassOf, AQO3.IC_Lousy);
		
		AQO3.CO2_Critical.addProperty(AQO3.hasLowerLimit, "401");
		AQO3.CO2_Critical.addProperty(AQO3.hasUpperLimit, "500");
		AQO3.CO2_Critical.addProperty(RDFS.subClassOf, AQO3.IC_Critical);
		
		
		/** Cada IndicatorCategorization é, por sua vez, uma subclasse de AQ_Categorizations */
		
		AQO3.IC_Good.addProperty(RDFS.subClassOf, AQO3.AQ_Categorizations);
		AQO3.IC_Regular.addProperty(RDFS.subClassOf, AQO3.AQ_Categorizations);
		AQO3.IC_Poor.addProperty(RDFS.subClassOf, AQO3.AQ_Categorizations);
		AQO3.IC_Bad.addProperty(RDFS.subClassOf, AQO3.AQ_Categorizations);
		AQO3.IC_Lousy.addProperty(RDFS.subClassOf, AQO3.AQ_Categorizations);
		AQO3.IC_Critical.addProperty(RDFS.subClassOf, AQO3.AQ_Categorizations);
		
		
		/** 
		 * O mesmo é feito para os índices.
		 */
		AQO3.ANVISA_Good.addProperty(AQO3.hasLowerLimit, "0");
		AQO3.ANVISA_Good.addProperty(AQO3.hasUpperLimit, "50");
		AQO3.ANVISA_Good.addProperty(RDFS.subClassOf, AQO3.REG_ANVISA);
	
		AQO3.ANVISA_Regular.addProperty(AQO3.hasLowerLimit, "50");
		AQO3.ANVISA_Regular.addProperty(AQO3.hasUpperLimit, "100");
		AQO3.ANVISA_Regular.addProperty(RDFS.subClassOf, AQO3.REG_ANVISA);
		
		AQO3.ANVISA_Poor.addProperty(AQO3.hasLowerLimit, "101");
		AQO3.ANVISA_Poor.addProperty(AQO3.hasUpperLimit, "150");
		AQO3.ANVISA_Poor.addProperty(RDFS.subClassOf, AQO3.REG_ANVISA);
		
		AQO3.ANVISA_Bad.addProperty(AQO3.hasLowerLimit, "151");
		AQO3.ANVISA_Bad.addProperty(AQO3.hasUpperLimit, "200");
		AQO3.ANVISA_Bad.addProperty(RDFS.subClassOf, AQO3.REG_ANVISA);
		
		AQO3.ANVISA_Lousy.addProperty(AQO3.hasLowerLimit, "201");
		AQO3.ANVISA_Lousy.addProperty(AQO3.hasUpperLimit, "300");
		AQO3.ANVISA_Lousy.addProperty(RDFS.subClassOf, AQO3.REG_ANVISA);
		
		AQO3.ANVISA_Critical.addProperty(AQO3.hasLowerLimit, "300");
		AQO3.ANVISA_Critical.addProperty(AQO3.hasUpperLimit, "500");
		AQO3.ANVISA_Critical.addProperty(RDFS.subClassOf, AQO3.REG_ANVISA);
		
		
		/**
		 * Além disso, instancia-se a fórmula utilizada para conversão entre índice e 
		 */
		AQO3.ANVISA_FORMULA.addProperty(AQO3.hasFormula, "(INpoluenteF - INpoluenteI) / (BPpoluenteF - BPpoluenteI)"
				  									   + "* (CPpoluente - BPpoluenteI) + INpoluenteI");
		
		
	}
	
}
