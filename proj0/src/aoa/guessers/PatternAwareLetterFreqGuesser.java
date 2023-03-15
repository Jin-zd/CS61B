package aoa.guessers;

import aoa.utils.FileUtils;

import java.util.*;

public class PatternAwareLetterFreqGuesser implements Guesser {
    private final List<String> words;

    public PatternAwareLetterFreqGuesser(String dictionaryFile) {
        words = FileUtils.readWords(dictionaryFile);
    }

    @Override
    /* Returns the most common letter in the set of valid words based on the current
       PATTERN. */
    public char getGuess(String pattern, List<Character> guesses) {
        List<String> filterWords = LFGHelper.getMatchedWords(words, pattern);
        Map<Character, Integer> matchWords = LFGHelper.getFrequencyMap(filterWords);
        return LFGHelper.maxFreCharacter(guesses, matchWords);
    }

    public static void main(String[] args) {
        PatternAwareLetterFreqGuesser palfg = new PatternAwareLetterFreqGuesser("data/example.txt");
        System.out.println(palfg.getGuess("-o--a-", List.of('o', 'a')));
    }
}