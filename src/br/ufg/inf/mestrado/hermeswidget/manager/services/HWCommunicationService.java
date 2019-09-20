package br.ufg.inf.mestrado.hermeswidget.manager.services;

import br.ufg.inf.mestrado.hermesbase.HermesBaseManager;

public class HWCommunicationService {
	
	private HermesBaseManager hermesBaseManager = null;
	
	public HWCommunicationService() {
		// --> MEXI AQUI
		//this.hermesBaseManager = new HermesBaseManager();
		// --> MEXI AQUI
	}
	
	public HermesBaseManager getHermesBaseManager() {
		return this.hermesBaseManager;
	}

}
