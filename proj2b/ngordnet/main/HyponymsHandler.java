package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;
import ngordnet.word_net.WordNet;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {
    private final WordNet wn;
    private final NGramMap ngrams;
    public HyponymsHandler(String wordsPath, String countFile, String synsetsPath, String hyponymsPath) {
        wn = new WordNet(synsetsPath, hyponymsPath);
        ngrams = new NGramMap(wordsPath, countFile);
    }

    private Set<String> wordSetHandler(List<String> words) {
        String firstWord = words.iterator().next();
        Set<String> wordSet = wn.hyponyms(firstWord);
        for (String word : words) {
            if (word.equals(firstWord)) {
                continue;
            }
            Set<String> set = wn.hyponyms(word);
            wordSet.removeIf(singleWord -> !set.contains(singleWord));
        }
        return wordSet;
    }

    private Map<Double, String> countMapHandler(Set<String> wordSet, int startYear, int endYear) {
        Map<Double, String> countMap = new TreeMap<>();
        for (String word : wordSet) {
            double sum = 0;
            TimeSeries ts = ngrams.countHistory(word, startYear, endYear);
            for (int year : ts.keySet()) {
                sum += ts.get(year);
            }
            countMap.put(sum, word);
        }
        return countMap;
    }

    private Set<String> kSetHandler(int k, Set<String> wordSet, int startYear, int endYear) {
        Set<String> returnSet = new TreeSet<>();
        Map<Double, String> countMap = countMapHandler(wordSet, startYear, endYear);
        Map<Double, String> reverseMap = new TreeMap<>(Collections.reverseOrder());
        reverseMap.putAll(countMap);
        int count = 0;
        for (double frequency : reverseMap.keySet()) {
            if (Math.abs(frequency) <= 1e-5) {
                continue;
            }
            returnSet.add(reverseMap.get(frequency));
            count += 1;
            if (count == k) {
                break;
            }
        }
        return returnSet;
    }

    @Override
    public String handle(NgordnetQuery q) {
        int startYear = q.startYear();
        int endYear = q.endYear();
        int k = q.k();
        List<String> words = q.words();
        Set<String> wordSet = wordSetHandler(words);
        if (k == 0) {
            return wordSet.toString();
        } else {
            Set<String> returnSet = kSetHandler(k, wordSet, startYear, endYear);
            return returnSet.toString();
        }
    }
}
