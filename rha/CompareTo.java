package rha;
import java.util.*;

public class CompareTo {
    // Constrói listas de palavras únicas e frequências a partir da hash do documento
    private static void extractWords(Hash hash, List<String> uniqueWords, List<Integer> frequencies) {
        // Aqui usamos um método público que percorre todos os nós
        hash.forEachWord(wd -> {
            uniqueWords.add(wd.getWord());
            frequencies.add(wd.getFrequency());
        });
    }

    /**
     * Calcula a similaridade de cosseno entre dois documentos representados por suas tabelas hash
     * @param hash1 Tabela hash do primeiro documento
     * @param hash2 Tabela hash do segundo documento
     * @return Valor da similaridade de cosseno entre 0.0 e 1.0
     */
    public static double cosineSimilarity(Hash hash1, Hash hash2) {
        List<String> words1 = new ArrayList<>();
        List<Integer> freq1 = new ArrayList<>();
        List<String> words2 = new ArrayList<>();
        List<Integer> freq2 = new ArrayList<>();
        
        extractWords(hash1, words1, freq1);
        extractWords(hash2, words2, freq2);
        
        return cosineSimilarity(words1, freq1, words2, freq2);
    }

    private static double cosineSimilarity(List<String> words1, List<Integer> freq1,
                                           List<String> words2, List<Integer> freq2) {
        Set<String> allWordsSet = new HashSet<>();
        allWordsSet.addAll(words1);
        allWordsSet.addAll(words2);
        List<String> allWords = new ArrayList<>(allWordsSet);

        List<Integer> vec1 = new ArrayList<>();
        List<Integer> vec2 = new ArrayList<>();

        for (String w : allWords) {
            int i1 = words1.indexOf(w);
            int i2 = words2.indexOf(w);
            vec1.add(i1 != -1 ? freq1.get(i1) : 0);
            vec2.add(i2 != -1 ? freq2.get(i2) : 0);
        }

        double dot = 0, norm1 = 0, norm2 = 0;
        for (int i = 0; i < allWords.size(); i++) {
            dot += vec1.get(i) * vec2.get(i);
            norm1 += vec1.get(i) * vec1.get(i);
            norm2 += vec2.get(i) * vec2.get(i);
        }

        if (norm1 == 0 || norm2 == 0) return 0.0;
        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}
