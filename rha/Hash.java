package rha;

import java.util.Arrays;

public class Hash {
	private AVL[] Hashes;
	
	public void Inserir(String word) {
		int intKey = stringToIntHash(word);
		
		int indice = func_hash(intKey);
		WordData wd = new WordData(word,indice,1);

		if(!existsKey(word)) {
			Hashes[indice].insert_ALV(wd);
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
		
		int p = 31;

		int hashValue = 0;

		// calculando o valor polinomial
		for (int i = 0; i < word.length(); i++) {
			hashValue = (hashValue * p) + word.charAt(i);
			System.out.println("CHAR:" + word.charAt(i) + " valor:" + hashValue);
		}
		System.out.println("HASH VALUE:" + hashValue);
		System.out.println("HASH VALUE2:" + (hashValue > 0 ? hashValue : -hashValue));
		System.out.println("HASH VALUE2:" +  (hashValue > 0 ? hashValue : -hashValue));
		return  (hashValue > 0 ? hashValue : -hashValue);
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
		System.out.println(Hashes.length + " - " + chave );
		return (int)(Hashes.length * (chave * A % 1));
		
	}

    public Node buscar(String word) {
		int intKey = stringToIntHash(word);
		System.out.println(intKey);
        int indice = func_hash(intKey);
        System.out.println(indice + " - " + word + " - " + intKey);
        WordData wd = new WordData(word,indice);
        System.out.println( " --------------- ");
        return Hashes[indice].search(wd);
       
    }

    public void remover(String word) {
		int intKey = stringToIntHash(word);
        int indice = func_hash(intKey);
        WordData wd = new WordData(word,indice);
        Hashes[indice].remove_ALV(wd);
    }

	//TODO: implementar os metodos insertFrequency e updateFrequency

	@Override
	public String toString() {
		return "Hash [Hashes=" + Arrays.toString(Hashes) + "]";
	}
    
    
}
