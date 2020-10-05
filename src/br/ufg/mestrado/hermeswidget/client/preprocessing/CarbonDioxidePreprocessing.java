package br.ufg.mestrado.hermeswidget.client.preprocessing;

public class CarbonDioxidePreprocessing {

	public static String IQACarbonDioxide(double CPdioxido){

		int BPdioxidoI = 0, BPdioxidoF = 0,				//Concentração inicial e final da faixa de CO2										
			INdioxidoI = 0,	INdioxidoF = 0;				//Índice inicial para a faixa de CO2 						
		
		if(CPdioxido > 0 && CPdioxido <= 500){
			BPdioxidoI = 0; BPdioxidoF = 500;			//Concentração inicial e final da faixa de CO2
			INdioxidoI = 0;	INdioxidoF = 50;            //Índice inicial e final para a faixa de CO2 	
		} 
	
		if(CPdioxido > 501 && CPdioxido <= 1000){
			BPdioxidoI = 501; BPdioxidoF = 1000;		//Concentração inicial e final da faixa de CO2
			INdioxidoI = 51;  INdioxidoF = 100;         //Índice inicial e final para a faixa de CO2 	
		} 
	
		if(CPdioxido > 1001 && CPdioxido <= 1500){
			BPdioxidoI = 1001; BPdioxidoF = 1500;		//Concentração inicial e final da faixa de CO2
			INdioxidoI = 101;  INdioxidoF = 150;        //Índice inicial e final para a faixa de CO2 	
		} 
		
		if(CPdioxido > 1501 && CPdioxido <= 2000){
			BPdioxidoI = 1501; BPdioxidoF = 2000;		//Concentração inicial e final da faixa de CO2
			INdioxidoI = 151;	INdioxidoF = 250;       //Índice inicial e final para a faixa de CO2 	
		} 
	
		if(CPdioxido > 2001 && CPdioxido <= 3000){
			BPdioxidoI = 2001; BPdioxidoF = 3000;		//Concentração inicial e final da faixa de CO2
			INdioxidoI = 251;	INdioxidoF = 350;       //Índice inicial e final para a faixa de CO2 	
		} 
	
		if(CPdioxido > 3001 && CPdioxido <= 5000){
			BPdioxidoI = 3001; BPdioxidoF = 5000;		//Concentração inicial e final da faixa de CO2
			INdioxidoI = 351;	INdioxidoF = 500;       //Índice inicial e final para a faixa de CO2 	
		} 
	
		int deltaConcentration = BPdioxidoF - BPdioxidoI;
		int         deltaRange = INdioxidoF - INdioxidoI;
		
		double IQA = (deltaRange/deltaConcentration)*(CPdioxido - BPdioxidoI) + INdioxidoI; 
		
		String airQuality = null;
		
		if(IQA >   0 && IQA <= 50)  airQuality = "Qualidade do ar BOA - VERDE";
		if(IQA >  50 && IQA <= 100) airQuality = "Qualidade do ar REGULAR - AMARELO";
		if(IQA > 101 && IQA <= 199) airQuality = "Qualidade do ar MÁ - LARANJA";			
		if(IQA > 200 && IQA <= 299) airQuality = "Qualidade do ar PÉSSIMA - VERMELHO";
		if(IQA > 300 && IQA <= 399) airQuality = "Qualidade do ar CRÍTICA - ROXO";	
		
		return airQuality;
		
	}
		
}
