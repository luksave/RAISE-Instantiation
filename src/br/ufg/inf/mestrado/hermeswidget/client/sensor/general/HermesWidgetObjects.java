package br.ufg.inf.mestrado.hermeswidget.client.sensor.general;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import br.ufg.inf.mestrado.hermeswidget.ontologies.SSN;

public class HermesWidgetObjects {
	
	private ConcurrentHashMap<String, String> objects;
	
	public HermesWidgetObjects() {
		this.objects = new ConcurrentHashMap<String, String>();
	}

	public ConcurrentHashMap<String, String> getObjects() {
		return this.objects;
	}

	public void setObjects(ConcurrentHashMap<String, String> objects) {
		this.objects = objects;
	}
	
	public void insertObject(String object) {
		if (!this.objects.containsKey(object)) 
			this.objects.put(object, SSN.NS + object +"-"+ UUID.randomUUID().toString());
	}
}
