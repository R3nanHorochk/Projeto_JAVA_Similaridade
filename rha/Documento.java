package rha;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.Normalizer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Documento {
    protected String arquivo;

    protected String[] stopWords = {
        "a", "à", "ao", "aos", "as", "às", "o", "os",
        "um", "uma", "uns", "umas",
        "de", "do", "da", "dos", "das",
        "em", "no", "na", "nos", "nas",
        "por", "per", "para", "com", "sem", "sob", "sobre",
        "ante", "até", "após", "entre", "desde", "contra",
        "como", "que", "porque", "porquê", "pois", "se", "mas",
        "ou", "e", "nem", "também", "tanto", "quanto",
        "quando", "onde", "enquanto", "então", "assim",
        "isto", "isso", "aquilo", "este", "esta", "estes", "estas",
        "esse", "essa", "esses", "essas", "aquele", "aquela", "aqueles", "aquelas",
        "eu", "tu", "ele", "ela", "nós", "vós", "eles", "elas",
        "me", "mim", "comigo", "te", "ti", "contigo",
        "lhe", "lhes", "nos", "conosco", "vos", "convosco",
        "meu", "minha", "meus", "minhas",
        "teu", "tua", "teus", "tuas",
        "seu", "sua", "seus", "suas",
        "dele", "dela", "deles", "delas",
        "esteja", "estive", "estava", "estamos", "estão", "estava", "estiveram",
        "era", "fui", "foi", "foram", "ser", "sou", "são", "somos",
        "tem", "têm", "ter", "tenho", "tinha", "tinham",
        "haver", "há", "havia", "haviam",
        "vai", "vão", "vou", "vamos", "iria", "irão",
        "dá", "dei", "dado", "dar", "damos", "dão",
        "faz", "fazia", "fazem", "fiz", "feito",
        "pode", "podem", "posso", "poder", "podia",
        "depois", "antes", "agora", "ainda", "muito", "muitos", "muita", "muitas",
        "pouco", "poucos", "pouca", "poucas",
        "todo", "toda", "todos", "todas",
        "cada", "outro", "outra", "outros", "outras",
        "algum", "alguns", "alguma", "algumas",
        "nenhum", "nenhuma", "ninguém", "nada",
        "tudo", "algo", "seu", "sua", "seus", "suas",
        "meu", "minha", "meus", "minhas",
        "teu", "tua", "teus", "tuas",
        "dele", "dela", "deles", "delas",
        "lá", "aqui", "ali", "cá", "daqui", "dali", "acolá",
        "sim", "não", "já", "só", "também", "mesmo", "próprio",
        "outros", "outras", "tal", "tais",
        "sem", "nem", "sob", "sobre", "para", "por", "pela", "pelas", "pelos",
        "será", "seriam", "ser", "sendo", "estado",
        "esteve", "estavam", "estava", "estávamos",
        "tinha", "tiveram", "tiver", "tendo",
        "entretanto", "todavia", "portanto", "contudo", "assim", "então",
        "desde", "durante", "após", "antes", "atrás",
        "mim", "ti", "si", "consigo", "comigo",
        "mesmo", "mesma", "mesmos", "mesmas"
    };

    // constructors
    public Documento(String arquivo) {
        this.arquivo = arquivo;
    }

    public Documento() {
        this(null);
    }

    // --------------------------- methods ------------------------------------
    // calcula a frequencia de palavras com uma hash interna
    private void wordFrequencyFile(List<String> wordsNormalized) {
        Hash wordsFrequency = new Hash(26);
        
        for(String word : wordsNormalized) {
            int frequency = wordsFrequency.searchFrequency(word);

            if (frequency > 0) {
                wordsFrequency.updateFrequency(word, frequency + 1);
            } else {
                wordsFrequency.insertFrequency(word, 1);
            }
        }

    }

    public void wordFrequency(String filePath) {
        wordFrequencyFile(filePath);
    }

    // NORMALIZA 1 ARQUIVO
    private List<String> normalizeFile(String filePath) {
        StringBuilder conteudo = new StringBuilder();
        String[] words;
        List<String> filteredWords = new LinkedList<>();

        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                conteudo.append(scanner.nextLine()).append(" ");
            }

            String text = conteudo.toString();

            //clean pontuation text
            text = Normalizer.normalize(text, Normalizer.Form.NFD);
            text = text.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

            text = text.toLowerCase();

            text = text.replaceAll("[^a-zà-ú\\s]", " ");

            words = text.trim().split("\\s+");

            // Normalize text O(n * T) onde n tamanho arquivo e T tamanho vetor stopWords
            for (String word : words) {
                if (word.isEmpty()) {
                    continue;
                }
                boolean isStopWord = false;
                for (String stopWord :  stopWords) {
                    if (word.equals(stopWord)) {
                        isStopWord = true;
                        break;
                    } 
                }
                if(!isStopWord && !word.isEmpty()) {
                    filteredWords.add(word);
                }
            }

            return filteredWords;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<String> normalize(String filePath) {
        return normalizeFile(filePath);
    }
}
