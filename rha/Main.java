package rha;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {
	private static int func_hash(int chave) {
		double A = 0.6180339887;
		return (int)(20 * (chave * A % 1));
	}
	public static int charactere(String palavra) {
		int indice = 0;
		int valor = 0;
		for(int i = 0 ; i < palavra.length() ; i++) {
			valor = func_hash(palavra.charAt(i));
			indice += valor;
		}
		return indice;
	}
	
	public static void main(String[] args) {
<<<<<<< HEAD
		Documento d1 = new Documento("/workspaces/Projeto_JAVA_Similaridade/documentos/doc1.txt");
		Hash h1 = d1.wordFrequency( "/workspaces/Projeto_JAVA_Similaridade/documentos/doc1.txt" ) ;
		//System.out.println(h1);

		Documento d2 = new Documento("/workspaces/Projeto_JAVA_Similaridade/documentos/doc2.txt");
		Hash h2 = d2.wordFrequency( "/workspaces/Projeto_JAVA_Similaridade/documentos/doc2.txt" ) ;
		//System.out.println(h2);

		//double similarity = CompareTo.cosineSimilarity(h1, h2);
        //System.out.printf("Similaridade: %.2f%%\n", similarity);

		List<String> word1;
		List<String> word2;
		List<Integer> frequence1;
		List<Integer> frequence2;
		CompareTo.estractWords(h1, word1, frequence1);
		CompareTo.estractWords(h2, word2, frequence2);

		double result = CompareTo.cosineSimilarity(word1,frequence1,word2,frequence2);
		 System.out.printf("Similaridade: %.2f%%\n", result);
=======
		String diretorioDocumentos = "documentos";
		double limiar = 0.75;
>>>>>>> bc9e8ff8ef4fd01962ac062328107b13ea5e95dc
		
		// Lista todos os arquivos .txt no diretório
		File dir = new File(diretorioDocumentos);
		File[] arquivos = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".txt"));
		
		if (arquivos == null || arquivos.length == 0) {
			System.out.println("Nenhum arquivo .txt encontrado no diretório " + diretorioDocumentos);
			return;
		}
		
		// Processa cada documento
		List<Documento> documentos = new ArrayList<>();
		List<Hash> hashes = new ArrayList<>();
		List<String> nomesArquivos = new ArrayList<>();
		
		for (File arquivo : arquivos) {
			String caminho = arquivo.getAbsolutePath();
			Documento doc = new Documento(caminho);
			Hash hash = doc.wordFrequency(caminho);
			documentos.add(doc);
			hashes.add(hash);
			nomesArquivos.add(arquivo.getName());
		}
		
		// Calcula similaridade entre todos os pares
		List<Resultado> resultados = new ArrayList<>();
		int totalPares = 0;
		
		for (int i = 0; i < hashes.size(); i++) {
			for (int j = i + 1; j < hashes.size(); j++) {
				double similaridade = CompareTo.cosineSimilarity(
					hashes.get(i).getAllWordData(), 
					hashes.get(j).getAllWordData()
				);
				Resultado resultado = new Resultado(
					nomesArquivos.get(i),
					nomesArquivos.get(j),
					similaridade
				);
				resultados.add(resultado);
				totalPares++;
			}
		}
		
		// Ordena por similaridade (maior para menor)
		Collections.sort(resultados, new Comparator<Resultado>() {
			@Override
			public int compare(Resultado r1, Resultado r2) {
				return Double.compare(r2.getSimilaridade(), r1.getSimilaridade());
			}
		});
		
		// Formata a saída
		StringBuilder saida = new StringBuilder();
		saida.append("=== VERIFICADOR DE SIMILARIDADE DE TEXTOS ===\n");
		saida.append("\n");
		saida.append("Total de documentos processados: ").append(documentos.size()).append("\n");
		saida.append("\n");
		saida.append("Total de pares comparados: ").append(totalPares).append("\n");
		saida.append("\n");
		saida.append("Funcao hash utilizada: hashMultiplicativo\n");
		saida.append("\n");
		saida.append("Métrica de similaridade: Cosseno\n");
		saida.append("\n");
		saida.append("Pares com similaridade >= ").append(String.format("%.2f", limiar)).append(":\n");
		saida.append("\n");
		saida.append("---------------------------------\n");
		
		boolean encontrouParesAcimaLimiar = false;
		for (Resultado r : resultados) {
			if (r.getSimilaridade() >= limiar) {
				saida.append(r.getNomeFile1()).append(" <-> ").append(r.getNomeFile2())
					.append(" = ").append(String.format("%.2f", r.getSimilaridade())).append("\n");
				saida.append("\n");
				encontrouParesAcimaLimiar = true;
			}
		}
		
		if (!encontrouParesAcimaLimiar) {
			saida.append("\n");
		}
		saida.append("Pares com menor similaridade:\n");
		saida.append("\n");
		saida.append("---------------------------------\n");
		
		if (!resultados.isEmpty()) {
			Resultado menor = resultados.get(resultados.size() - 1);
			saida.append(menor.getNomeFile1()).append(" <-> ").append(menor.getNomeFile2())
				.append(" = ").append(String.format("%.2f", menor.getSimilaridade())).append("\n");
		}
		
		// Exibe no console
		System.out.print(saida.toString());
		
		// Salva em arquivo
		try (PrintWriter writer = new PrintWriter(new FileWriter("rha/resultado.txt"))) {
			writer.print(saida.toString());
		} catch (IOException e) {
			System.err.println("Erro ao escrever arquivo resultado.txt: " + e.getMessage());
		}
	}

}
