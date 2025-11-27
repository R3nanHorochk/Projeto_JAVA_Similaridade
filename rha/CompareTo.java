package rha;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
public class CompareTo {
   
    public static int findFrequency(List<WordData> list, String word) {
    for (WordData wd : list) {
        if (wd.getWord().equals(word)) {
            return wd.getFrequency();
        }
    }
    return 0; // não encontrou
    }

    public static double cosineSimilarity(List<WordData> words1, List<WordData> words2) {
        Set<String> allWordsSet = new HashSet<>();
        for(int i = 0 ; i < words1.size() ; i++){
            allWordsSet.add(words1.get(i).getWord());
        }

        for(int i = 0 ; i < words2.size() ; i++){
            allWordsSet.add(words2.get(i).getWord());
        }
        List<String> allWords = new ArrayList<>(allWordsSet);

        List<Integer> vec1 = new ArrayList<>();
        List<Integer> vec2 = new ArrayList<>();

        for (String w : allWords) {
            int i1 = findFrequency(words1,w);
            int i2 = findFrequency(words2,w);
            vec1.add(i1 != -1 ? i1 : 0);
            vec2.add(i2 != -1 ? i2 : 0);
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
