package br.ufg.inf.mestrado.hermeswidget.testes;

import com.hp.hpl.jena.query.ResultSet;

import br.ufg.inf.mestrado.hermeswidget.client.utils.EnvironmentQuery;

public class RealizaConsultas {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/*Double AverageCO2 = EnvironmentQuery.getMedia("2021-03-04T06:04:42", "2021-03-04T07:04:42");
		System.out.println("\n Média de concentração: " +AverageCO2);

		
		Double instConc = EnvironmentQuery.getConcPolut("ConCO2", "2021-03-04-07:04:42");
		System.out.println("\n Concentração do poluente no tempo escolhido:" +instConc);


		Double lastConc = EnvironmentQuery.getLastConcPolut("ConCO2");
		System.out.println("\n Última concentração do poluente:" +lastConc);
		
		
		Double intervalConc = EnvironmentQuery.getIntervalConcPolut("ConCO2", "2021-03-04-07:04:42", "2021-03-04-07:06:42");
		System.out.println("\n Concentração do poluente no intervalo escolhido:" +intervalConc);		
		
		
		Double intervalConcAVG = EnvironmentQuery.getIntervalConcPolutAVG("ConCO2", "2021-03-04-07:04:42", "2021-03-04-07:06:42");
		System.out.println("\n Concentração média do poluente no tempo escolhido:" +intervalConcAVG);

		
		String qualityByPolut = EnvironmentQuery.getQualityByPolut("CO2", "2021-03-04-07:04:42", "2021-03-04-07:06:42");
		System.out.println("\n A qualidade do ar no ambiente de acordo com o poluente é:" +qualityByPolut);

		
		String instQualityByPolut = EnvironmentQuery.getInstQualityByPolut("CO2", "2021-03-04-07:06:42");
		System.out.println("\n A qualidade do ar instantânea de acordo com o poluente é:" +instQualityByPolut);

		
		String actualQualityByPolut = EnvironmentQuery.getActualQualityByPolut("CO2");
		System.out.println("\n A qualidade do ar atual de acordo com o poluente é:" +actualQualityByPolut);
		
		ResultSet symptomsSetByPolut = EnvironmentQuery.getSymptomsByPolut("CO2");
		System.out.println("\n O conjunto de sintomas para o poluente é:" +symptomsSetByPolut);*/

		ResultSet symptomsByPolut = EnvironmentQuery.getSymptomsSetByPolut("CO2");
		System.out.println("\n O conjunto de sintomas para o poluente é:" +symptomsByPolut);
		
		
	}

}
