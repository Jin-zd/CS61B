package aoa.choosers;

import edu.princeton.cs.algs4.StdRandom;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RandomChooser implements Chooser {
    private final String chosenWord;
    private String pattern;

    public RandomChooser(int wordLength, String dictionaryFile) {
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

        int numWords = sameLengthWords.size();
        int randInt = StdRandom.uniform(numWords);
        chosenWord = sameLengthWords.get(randInt);

        pattern = "-".repeat(wordLength);
    }

    @Override
    public int makeGuess(char letter) {
        int count = 0;
        char[] patternArray = new char[chosenWord.length()];
        StringBuilder temPattern = new StringBuilder();
        for (int i = 0; i < pattern.length(); i++) {
            patternArray[i] = pattern.charAt(i);
        }
        for (int i = 0; i < chosenWord.length(); i++) {
            if (chosenWord.charAt(i) == letter) {
                count++;
                patternArray[i] = letter;
            }
            temPattern.append(patternArray[i]);
        }
        pattern = temPattern.toString();
        return count;
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public String getWord() {
        return chosenWord;
    }
}
