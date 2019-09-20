package br.ufg.inf.mestrado.hermeswidget.client.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HWLog {

	public static void recordLog(String log) {

		File arquivo = new File("./logs/logs.txt");

		try {

			if (!arquivo.exists()) {
				// cria um arquivo (vazio)
				arquivo.createNewFile();
			}

			// caso seja um diret�rio, � poss�vel listar seus arquivos e
			// diret�rios
			//File[] arquivos = arquivo.listFiles();

			// escreve no arquivo
			FileWriter fw = new FileWriter(arquivo, true);

			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(log);

			bw.newLine();

			bw.close();
			fw.close();

			/*
			 * //faz a leitura do arquivo FileReader fr = new
			 * FileReader(arquivo);
			 * 
			 * BufferedReader br = new BufferedReader(fr);
			 * 
			 * //equanto houver mais linhas while (br.ready()) { //l� a proxima
			 * linha String linha = br.readLine();
			 * 
			 * //faz algo com a linha System.out.println(linha); }
			 * 
			 * br.close(); fr.close();
			 */

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

}
