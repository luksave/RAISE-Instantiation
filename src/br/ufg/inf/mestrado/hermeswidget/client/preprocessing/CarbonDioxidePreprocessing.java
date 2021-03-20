package br.ufg.inf.mestrado.hermeswidget.client.preprocessing;

public class CarbonDioxidePreprocessing {

	
	public static String IQALevel(int IQA) {
		
		String level = "Good";

		if(IQA > 500 && IQA < 1000) level = "Regular";

		if(IQA > 500 && IQA < 1000) level = "Poor";
		
		if(IQA > 500 && IQA < 1000) level = "Bad";
		
		if(IQA > 500 && IQA < 1000) level = "Lousy";
		
		if(IQA > 500 && IQA < 1000) level = "Critical";
		
		
		return level;
		
	}
	
	public static int IQACarbonDioxide(double CPdioxido){

		double BPdioxidoI = 0.0, BPdioxidoF = 0.0,				//Concentração inicial e final da faixa de CO2										
		       INdioxidoI = 0.0, INdioxidoF = 0.0;				//Índice inicial para a faixa de CO2 						
			   
		int IQA = 0;
		
		if(CPdioxido == 0) IQA = 0;
		
		if(CPdioxido > 0) {	
			if(CPdioxido > 0.0 && CPdioxido <= 500.0){
				BPdioxidoI = 0.0; BPdioxidoF = 500.0;			//Concentração inicial e final da faixa de CO2
				INdioxidoI = 0.0;	INdioxidoF = 50.0;            //Índice inicial e final para a faixa de CO2 	
			} 
			
			if(CPdioxido > 500.0 && CPdioxido <= 1000.0){
				BPdioxidoI = 501.0; BPdioxidoF = 1000.0;		//Concentração inicial e final da faixa de CO2
				INdioxidoI = 51.0;  INdioxidoF = 100.0;         //Índice inicial e final para a faixa de CO2 	
			} 
			
			if(CPdioxido > 1000.0 && CPdioxido <= 1500.0){
				BPdioxidoI = 1001.0; BPdioxidoF = 1500.0;		//Concentração inicial e final da faixa de CO2
				INdioxidoI = 101.0;  INdioxidoF = 150.0;        //Índice inicial e final para a faixa de CO2 	
			} 
			
			if(CPdioxido > 1500.0 && CPdioxido <= 2000.0){
				BPdioxidoI = 1501.0; BPdioxidoF = 2000.0;		//Concentração inicial e final da faixa de CO2
				INdioxidoI = 151.0;	INdioxidoF = 250.0;       //Índice inicial e final para a faixa de CO2 	
			} 
			
			if(CPdioxido > 2000.0 && CPdioxido <= 3000.0){
				BPdioxidoI = 2001.0; BPdioxidoF = 3000.0;		//Concentração inicial e final da faixa de CO2
				INdioxidoI = 251.0;	INdioxidoF = 350.0;       //Índice inicial e final para a faixa de CO2 	
			} 
			
			if(CPdioxido > 3000.0 && CPdioxido <= 5000.0){
				BPdioxidoI = 3001.0; BPdioxidoF = 5000.0;		//Concentração inicial e final da faixa de CO2
			INdioxidoI = 351.0;	INdioxidoF = 500.0;       //Índice inicial e final para a faixa de CO2 	
			} 
			
			double deltaConcentration = BPdioxidoF - BPdioxidoI;
			double         deltaRange = INdioxidoF - INdioxidoI;
			
			IQA = (int) ((deltaRange/deltaConcentration)*(CPdioxido - BPdioxidoI) + INdioxidoI); 
		
		}
		
		return IQA;
		
	}
		
}
