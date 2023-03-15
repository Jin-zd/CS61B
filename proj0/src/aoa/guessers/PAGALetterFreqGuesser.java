package aoa.guessers;

import aoa.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class PAGALetterFreqGuesser implements Guesser {
    private final List<String> words;

    public PAGALetterFreqGuesser(String dictionaryFile) {
        words = FileUtils.readWords(dictionaryFile);
    }

    //依据所给pattern筛选所有含有pattern中字母且位置正确的单词
    public List<String> getMatchedWords(String pattern) {
        List<String> filterWords = new ArrayList<>();
        Map<String, Integer> countMap = new TreeMap<>();
        int count = 0;

        for (int i = 0; i < pattern.length(); i++) {
            char ch = pattern.charAt(i);
            if (ch != '-') {
                count++;
                for (String word : words) {
                    if (i <= word.length() - 1) {
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

        for (String key : countMap.keySet()) {
            if (countMap.get(key) == count) {
                filterWords.add(key);
            }
        }

        if (filterWords.isEmpty()) {
            filterWords = words;
        }

        filterWords.removeIf(word -> word.length() != pattern.length());
        return filterWords;
    }

    //依据pattern进一步筛选与其中字母个数也相同的单词
    public List<String> furtherFilterWords(List<String> filterWords, List<Character> guesses, String pattern) {
        List<Character> removeCharacter = new ArrayList<>();
        List<String> removeWords = new ArrayList<>();
        Map<Character, Integer> freChMap = new TreeMap<>();
        //剔除含有guesses中含有，pattern中不含有的字母的单词
        for (char ch : guesses) {
            boolean flag = true;
            for (int i = 0; i < pattern.length(); i++) {
                if (pattern.charAt(i) == ch) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                removeCharacter.add(ch);
            }
        }
        for (char reCh : removeCharacter) {
            filterWords.removeIf(word -> word.indexOf(reCh) != -1);
        }

        //计算pattern中除了‘-’之外的字母的出现次数
        for (int i = 0; i < pattern.length(); i++) {
            char pa = pattern.charAt(i);
            if (pa != '-') {
                if (!freChMap.containsKey(pa)) {
                    freChMap.put(pa, 1);
                } else {
                    freChMap.put(pa, freChMap.get(pa) + 1);
                }
            }
        }

        //移除字母出现次数不同的单词
        for (String word : filterWords) {
            for (char key : freChMap.keySet()) {
                int count = 0;
                for (int i = 0; i < word.length(); i++) {
                    if (word.charAt(i) == key) {
                        count++;
                    }
                }
                if (count != freChMap.get(key)) {
                    //此处不能直接对filterWords进行remove操作，
                    //会导致filterWords改变而不能进入下一次循环
                    removeWords.add(word);
                }
            }
        }

        for (String word : removeWords) {
            filterWords.remove(word);
        }

        return filterWords;
    }

    public Map<Character, Integer> getFreqMap(List<String> filterWords) {
        Map<Character, Integer> countMap = new TreeMap<>();
        for (String word : filterWords) {
            for (int i = 0; i < word.length(); i++) {
                char ch = word.charAt(i);
                if (!countMap.containsKey(ch)) {
                    countMap.put(ch, 1);
                } else {
                    countMap.put(ch, countMap.get(ch) + 1);
                }
            }
        }
        return countMap;
    }

    @Override
    /* Returns the most common letter in the set of valid words based on the current
       PATTERN and the GUESSES that have been made. */
    public char getGuess(String pattern, List<Character> guesses) {
        List<String> filterWords = getMatchedWords(pattern);
        filterWords = furtherFilterWords(filterWords, guesses, pattern);
        Map<Character, Integer> countMap = getFreqMap(filterWords);
        for (char guess : guesses) {
            countMap.remove(guess);
        }

        char firstKey = countMap.keySet().iterator().next();
        int max = countMap.get(firstKey);
        char ch = firstKey;

        for (char key : countMap.keySet()) {
            if (countMap.get(key) > max) {
                max = countMap.get(key);
                ch = key;
            }
        }
        return ch;
    }

    public static void main(String[] args) {
        PAGALetterFreqGuesser pagalfg = new PAGALetterFreqGuesser("data/example.txt");
        System.out.println(pagalfg.getGuess("----", List.of('e')));
    }
}
