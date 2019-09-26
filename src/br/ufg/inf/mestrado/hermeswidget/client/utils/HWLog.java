package br.ufg.inf.mestrado.hermeswidget.client.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HWLog {

	public static void recordLog(String log) {

		File arquivo = new File("./logs/logs.txt");

		try {

			// cria um arquivo (vazio)
			if (!arquivo.exists()) arquivo.createNewFile();
			

			// Caso seja um diretório, é possível listar seus arquivos e diretórios:
			//File[] arquivos = arquivo.listFiles();

			// escreve no arquivo
			FileWriter fw = new FileWriter(arquivo, true);

			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(log);

			bw.newLine();

			bw.close();
			fw.close();


		} catch (IOException ex) {
			ex.printStackTrace();

		}

	}

}
