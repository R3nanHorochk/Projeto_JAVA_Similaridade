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
		String str = "Hello World Hello";
		String[] words = str.split(" ");
		int tamanho = words.length;
		int i2 = 0;
		for(int i = 0 ; i < tamanho ; i++) {
			i2 = charactere(words[i]);
		}
		
	}

}
