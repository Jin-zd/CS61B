package aoa.choosers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import edu.princeton.cs.algs4.StdRandom;
import aoa.utils.FileUtils;

public class EvilChooser implements Chooser {
    private String pattern;
    private List<String> wordPool;

    public EvilChooser(int wordLength, String dictionaryFile) {
        List<String> wordList = new ArrayList<>();
        List<String> sameLengthWords = new ArrayList<>();

        if (wordLength < 1) {
            throw new IllegalArgumentException("error!");
        }
        try {
            wordList = Files.readAllLines(Paths.get(dictionaryFile));
        } catch (IOException er) {
            er.printStackTrace();
        }
        for (String word : wordList) {
            if (word.length() == wordLength) {
                sameLengthWords.add(word);
            }
        }

        if (sameLengthWords.isEmpty()) {
            throw new IllegalStateException("error!");
        }

        wordPool = sameLengthWords;
        pattern = "-".repeat(wordLength);
    }

    @Override
    public int makeGuess(char letter) {
        Map<String, List<String>> patternMatch = new TreeMap<>();
        for (String word : wordPool) {
            StringBuilder buildPattern = new StringBuilder(pattern);
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == letter) {
                    buildPattern.replace(i, i + 1, String.valueOf(letter));
                }
            }
            String keyPattern = String.valueOf(buildPattern);
            if (!patternMatch.containsKey(keyPattern)) {
                patternMatch.put(keyPattern, new ArrayList<>());
            }
            patternMatch.get(keyPattern).add(word);
        }

        String firstKey = patternMatch.keySet().iterator().next();
        int max = patternMatch.get(firstKey).size();
        wordPool = patternMatch.get(firstKey);
        pattern = firstKey;
        for (String key : patternMatch.keySet()) {
            int size = patternMatch.get(key).size();
            if (size > max) {
                max = size;
                wordPool = patternMatch.get(key);
                pattern = key;
            }
        }

        int count = 0;
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == letter) {
                count++;
            }
        }

        return count;
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public String getWord() {
        int numWords = wordPool.size();
        int randInt = StdRandom.uniform(numWords);
        return wordPool.get(randInt);
    }
}
