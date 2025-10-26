
/*
 * Classe dedicada para armazenar as informacoes de data no no da AVL
 */

public class WordData {
    protected String word;
    protected int hash;
    protected int frequency;


    // construtores
    public WordData(String word, int hash, int frequency) {
        this.word = word;
        this.hash = hash;
        this.frequency = frequency;
    }

    public WordData() {
        this(null, 0, 0);
    }

    // getters e setters
    public String getWord() {
        return this.word;
    }

    public int getFrequency() {
        return this.frequency;
    }

    public int getHash() {
        return this.hash;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setFrequency(int Frequency) {
        this.frequency = Frequency;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }
}
