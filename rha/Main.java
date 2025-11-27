package rha;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.io.FileFilter;


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
		String diretorioDocumentos = args[0];

		double limiar = Double.parseDouble(args[1]);
		
		String modo = args[2];
		
		File dir = new File(diretorioDocumentos);
		FileFilter filtro = new FileFilter() {

                public boolean accept(File f)
                {
                    return f.getName().endsWith("txt");
                }
            };
	     File[] arquivos = dir.listFiles(filtro);

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
