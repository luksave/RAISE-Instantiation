package br.ufg.inf.mestrado.hermeswidget.manager.transferObject;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * 
 * @author Ernesto
 * 
 *         Objeto utilizado para transferir dados entre as camadas de negócio,
 *         apresentação e integração.
 * 
 *         Encapsula os dados que serão notificados por um Hermes Widget.
 * 
 */

public class HWTransferObject {

	private String nomeTopico;
	private String complementoTopico;
	private byte[] contexto;
	private Model modelContexto;
	private String idEntidade;
	private String caminhoOntologia;
	private String tipoSerializacao;
	private String sensorValue;

	/* NOVO */
	private final String tipoTopico = "widget";
	
	public String getTipoTopico() {
		return tipoTopico;
		
	}
	/* ----- */

	private int threadAtual;
	private int totalThreads;

	public String getNomeTopico() {
		return nomeTopico;
		
	}

	public void setNomeTopico(String nomeTopico) {
		this.nomeTopico = nomeTopico;
		
	}

	public String getComplementoTopico() {
		return complementoTopico;
		
	}

	public void setComplementoTopico(String complementoTopico) {
		this.complementoTopico = complementoTopico;
		
	}

	public byte[] getContexto() {
		return contexto;
		
	}

	public void setContexto(byte[] contexto) {
		this.contexto = contexto;
		
	}

	public Model getModelContexto() {
		return modelContexto;
		
		
	}

	public void setModelContexto(Model modelContexto) {
		this.modelContexto = modelContexto;
		
	}

	public String getIdEntidade() {
		return idEntidade;
		
	}

	public void setIdEntidade(String idEntidade) {
		this.idEntidade = idEntidade;
		
	}

	public String getCaminhoOntologia() {
		return caminhoOntologia;
		
	}

	public void setCaminhoOntologia(String caminhoOntologia) {
		this.caminhoOntologia = caminhoOntologia;
		
	}

	public String getTipoSerializacao() {
		return tipoSerializacao;
		
	}

	public void setTipoSerializacao(String tipoSerializacao) {
		this.tipoSerializacao = tipoSerializacao;
		
	}

	public String getSensorValue() {
		return sensorValue;
		
	}

	public void setSensorValue(String sensorValue) {
		this.sensorValue = sensorValue;
		
	}

	public int getThreadAtual() {
		return threadAtual;
		
	}

	public void setThreadAtual(int threadAtual) {
		this.threadAtual = threadAtual;
		
	}

	public int getTotalThreads() {
		return totalThreads;
		
	}

	public void setTotalThreads(int totalThreads) {
		this.totalThreads = totalThreads;
		
	}

}
