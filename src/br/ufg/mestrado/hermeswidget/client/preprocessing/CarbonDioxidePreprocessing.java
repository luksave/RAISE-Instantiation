package br.ufg.mestrado.hermeswidget.client.preprocessing;

public class CarbonDioxidePreprocessing {

	public static String IQACarbonDioxide(double CPdioxido){

		int BPdioxidoI = 0, BPdioxidoF = 0,				//Concentra��o inicial e final da faixa de CO2										
			INdioxidoI = 0,	INdioxidoF = 0;				//�ndice inicial para a faixa de CO2 						
		
		if(CPdioxido > 0 && CPdioxido <= 500){
			BPdioxidoI = 0; BPdioxidoF = 500;			//Concentra��o inicial e final da faixa de CO2
			INdioxidoI = 0;	INdioxidoF = 50;            //�ndice inicial e final para a faixa de CO2 	
		} 
	
		if(CPdioxido > 501 && CPdioxido <= 1000){
			BPdioxidoI = 501; BPdioxidoF = 1000;		//Concentra��o inicial e final da faixa de CO2
			INdioxidoI = 51;  INdioxidoF = 100;         //�ndice inicial e final para a faixa de CO2 	
		} 
	
		if(CPdioxido > 1001 && CPdioxido <= 1500){
			BPdioxidoI = 1001; BPdioxidoF = 1500;		//Concentra��o inicial e final da faixa de CO2
			INdioxidoI = 101;  INdioxidoF = 150;        //�ndice inicial e final para a faixa de CO2 	
		} 
		
		if(CPdioxido > 1501 && CPdioxido <= 2000){
			BPdioxidoI = 1501; BPdioxidoF = 2000;		//Concentra��o inicial e final da faixa de CO2
			INdioxidoI = 151;	INdioxidoF = 250;       //�ndice inicial e final para a faixa de CO2 	
		} 
	
		if(CPdioxido > 2001 && CPdioxido <= 3000){
			BPdioxidoI = 2001; BPdioxidoF = 3000;		//Concentra��o inicial e final da faixa de CO2
			INdioxidoI = 251;	INdioxidoF = 350;       //�ndice inicial e final para a faixa de CO2 	
		} 
	
		if(CPdioxido > 3001 && CPdioxido <= 5000){
			BPdioxidoI = 3001; BPdioxidoF = 5000;		//Concentra��o inicial e final da faixa de CO2
			INdioxidoI = 351;	INdioxidoF = 500;       //�ndice inicial e final para a faixa de CO2 	
		} 
	
		int deltaConcentration = BPdioxidoF - BPdioxidoI;
		int         deltaRange = INdioxidoF - INdioxidoI;
		
		double IQA = (deltaRange/deltaConcentration)*(CPdioxido - BPdioxidoI) + INdioxidoI; 
		
		String airQuality = null;
		
		if(IQA >   0 && IQA <= 50)  airQuality = "Qualidade do ar BOA - VERDE";
		if(IQA >  50 && IQA <= 100) airQuality = "Qualidade do ar REGULAR - AMARELO";
		if(IQA > 101 && IQA <= 199) airQuality = "Qualidade do ar M� - LARANJA";			
		if(IQA > 200 && IQA <= 299) airQuality = "Qualidade do ar P�SSIMA - VERMELHO";
		if(IQA > 300 && IQA <= 399) airQuality = "Qualidade do ar CR�TICA - ROXO";	
		
		return airQuality;
		
	}
		
}
