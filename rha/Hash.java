package rha;

import java.util.Arrays;

public class Hash {
	private AVL[] Hashes;
	
	public void Inserir(String word) {
		int intKey = stringToIntHash(word);

		int indice = func_hash(intKey);

		if(!existsKey(intKey)) {
			Hashes[indice].insert_ALV(intKey);
		} else {
			// somo mais um nela
		}
	}

	private boolean existsKey(String word) {
		/*
		 * retorno falso se a key nao existe na hash table e true se existe.
		 */
		return buscar(word) == null ?  false : true;
	}

	// calculando hash de uma String
	private int stringToIntHash(String word) {
		/*
		 * Metodo responsavel por transformar uma string em uma key de inteiro
		 * dessa forma podemos calcular o hash
		 */

		int p = 31;

		long hashValue = 0;

		// calculando o valor polinomial
		for (int i = 0; i < word.length(); i++) {
			hashValue = (hashValue * p) + word.charAt(i);
		}

		return (int) (hashValue > 0 ? hashValue : -hashValue);
	}
	
	public Hash(int tamanho) {
		Hashes = new AVL[tamanho];
	        for (int i = 0; i < tamanho; i++) {
	        	Hashes[i] = new AVL();
	        }
	    }
	
	// metodo de dispersao da MULTIPLICACAO
	private int func_hash(int chave) {
		double A = 0.6180339887;
		return (int)(Hashes.length * (chave * A % 1));
	}

    public Node buscar(String word) {
		int intKey = stringToIntHash(word);

        int indice = func_hash(intKey);
        return Hashes[indice].search(intKey);
    }

    public void remover(String word) {
		int intKey = stringToIntHash(word);

        int indice = func_hash(intKey);
        Hashes[indice].remove_ALV(intKey);
    }

	//TODO: implementar os metodos insertFrequency e updateFrequency

	@Override
	public String toString() {
		return "Hash [Hashes=" + Arrays.toString(Hashes) + "]";
	}
    
    
}
