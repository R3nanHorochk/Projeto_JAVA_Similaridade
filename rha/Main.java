package rha;

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
		Documento d1 = new Documento("/workspaces/Projeto_JAVA_Similaridade/documentos/doc1.txt");
		Hash h1 = d1.wordFrequency( "/workspaces/Projeto_JAVA_Similaridade/documentos/doc1.txt" ) ;
		//System.out.println(h1);

		Documento d2 = new Documento("/workspaces/Projeto_JAVA_Similaridade/documentos/doc2.txt");
		Hash h2 = d2.wordFrequency( "/workspaces/Projeto_JAVA_Similaridade/documentos/doc2.txt" ) ;
		//System.out.println(h2);

		double similarity = CompareTo.cosineSimilarity(h1, h2);
        System.out.printf("Similaridade: %.2f%%\n", similarity);
		
	}

}
