package aoa.guessers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class LFGHelper {
    public static char maxFreCharacter(List<Character> guesses, Map<Character, Integer> countMap) {
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

    public static Map<Character, Integer> getFrequencyMap(List<String> words) {
        Map<Character, Integer> countMap = new TreeMap<>();
        for (String word : words) {
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

    public static List<String> getMatchedWords(List<String> words, String pattern) {
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

    //依据pattern进一步筛选与其中字母个数也相同的单词
    public static List<String> furtherFilterWords(List<String> filterWords, List<Character> guesses, String pattern) {
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
}
