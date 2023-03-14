package aoa.guessers;

import aoa.utils.FileUtils;

import java.util.*;

public class PatternAwareLetterFreqGuesser implements Guesser {
    private final List<String> words;

    public PatternAwareLetterFreqGuesser(String dictionaryFile) {
        words = FileUtils.readWords(dictionaryFile);
    }


    public List<String> getMatchedWords(String pattern) {
        List<String> filterWords = new ArrayList<>();
        Map<String, Integer> countMap = new TreeMap<>();
        int count = 0;

        for (int i = 0; i < pattern.length(); i++) {
            char ch = pattern.charAt(i);
            if (ch != '-') {
                count++;
                for (String word : words) {
                    //确保对word遍历时不越界
                    if (i <= word.length() - 1) {
                        //保证对应位置字母相同
                        if (word.charAt(i) == ch) {
                            if (!countMap.containsKey(word)) {
                                countMap.put(word, 1);
                            } else {
                                countMap.put(word, countMap.get(word) + 1);
                            }
                        }
                    }

                }
            }
        }
        //引入countMap来解决同一字母多次出现的问题
        for (String key : countMap.keySet()) {
            if (countMap.get(key) == count) {
                filterWords.add(key);
            }
        }
        //若未过滤出符合条件的单词，则以初始列表作为结果
        if (filterWords.isEmpty()) {
            filterWords = words;
        }
        //剔除长度不一致的单词
        filterWords.removeIf(word -> word.length() != pattern.length());
        return filterWords;
    }

    public Map<Character, Integer> getFreqMapThatMatchesPattern(List<String> filterWords) {
        Map<Character, Integer> matchWords = new TreeMap<>();

        for (String word : filterWords) {
            for (int i = 0; i < word.length(); i++) {
                char ch = word.charAt(i);
                if (!matchWords.containsKey(ch)) {
                    matchWords.put(ch, 1);
                } else {
                    matchWords.put(ch, matchWords.get(ch) + 1);
                }
            }
        }
        return matchWords;
    }

    @Override
    /* Returns the most common letter in the set of valid words based on the current
       PATTERN. */
    public char getGuess(String pattern, List<Character> guesses) {
        List<String> filterWords = getMatchedWords(pattern);
        Map<Character, Integer> matchWords = getFreqMapThatMatchesPattern(filterWords);

        for (char guess : guesses) {
            matchWords.remove(guess);
        }

        char firstKey = matchWords.keySet().iterator().next();
        int max = matchWords.get(firstKey);
        char ch = firstKey;

        for (char key : matchWords.keySet()) {
            if (matchWords.get(key) > max) {
                max = matchWords.get(key);
                ch = key;
            }
        }
        return ch;
    }

    public static void main(String[] args) {
        PatternAwareLetterFreqGuesser palfg = new PatternAwareLetterFreqGuesser("data/example.txt");
        System.out.println(palfg.getGuess("-o--a-", List.of('o', 'a')));
    }
}