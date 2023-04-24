package ngordnet.ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Arrays;
import java.util.Collection;
import java.util.TreeMap;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;
    private final TreeMap<String, TimeSeries> wordRecord;
    private final TimeSeries countRecord;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        wordRecord = new TreeMap<>();
        countRecord = new TimeSeries();
        In in = new In(wordsFilename);

        while (in.hasNextLine()) {
            String[] datas = in.readLine().split("\t");
            String word = datas[0];
            int year = Integer.parseInt(datas[1]);
            double count = Double.parseDouble(datas[2]);
            if (!wordRecord.containsKey(word)) {
                wordRecord.put(word, new TimeSeries());
            }
            wordRecord.get(word).put(year, count);
        }

        in = new In(countsFilename);
        while (in.hasNextLine()) {
            String[] nums = in.readLine().split(",");
            int year = Integer.parseInt(nums[0]);
            double count = Double.parseDouble(nums[1]);
            countRecord.put(year, count);
        }

     }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        if (!wordRecord.containsKey(word)) {
            return new TimeSeries();
        }
        TimeSeries ts = wordRecord.get(word);
        return new TimeSeries(ts, startYear, endYear);
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy,
     * not a link to this NGramMap's TimeSeries. In other words, changes made
     * to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word) {
        return countHistory(word, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return new TimeSeries(countRecord, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        TimeSeries ts = countHistory(word, startYear, endYear);
        return ts.dividedBy(countRecord);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to
     * all words recorded in that year. If the word is not in the data files, return an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        return weightHistory(word, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS
     * between STARTYEAR and ENDYEAR, inclusive of both ends. If a word does not exist in
     * this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries sum = new TimeSeries();
        for (String word : words) {
            if (wordRecord.containsKey(word)) {
                TimeSeries wordWeightHistory = weightHistory(word, startYear, endYear);
                for (int year : wordWeightHistory.years()) {
                    double count = wordWeightHistory.get(year);
                    if (!sum.containsKey(year)) {
                        sum.put(year, count);
                    } else {
                        sum.put(year, sum.get(year) + count);
                    }
                }
            }
        }
        return sum;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        return summedWeightHistory(words, MIN_YEAR, MAX_YEAR);
    }
}
