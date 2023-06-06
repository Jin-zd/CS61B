package ngordnet.proj2b_testing;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.main.HyponymsHandler;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.google.common.truth.Truth.assertThat;

/** Tests the case where the list of words is length greater than 1, but k is still zero. */
public class TestMultiWordK0Hyponyms {
    // this case doesn't use the NGrams dataset at all, so the choice of files is irrelevant
    public static final String WORDS_FILE = "data/ngrams/very_short.csv";
    public static final String TOTAL_COUNTS_FILE = "data/ngrams/total_counts.csv";
    public static final String SMALL_SYNSET_FILE = "data/wordnet/synsets16.txt";
    public static final String SMALL_HYPONYM_FILE = "data/wordnet/hyponyms16.txt";
    public static final String LARGE_SYNSET_FILE = "data/wordnet/synsets.txt";
    public static final String LARGE_HYPONYM_FILE = "data/wordnet/hyponyms.txt";

    /** This is an example from the spec.*/
    @Test
    public void testOccurrenceAndChangeK0() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymHandler(
                WORDS_FILE, TOTAL_COUNTS_FILE, SMALL_SYNSET_FILE, SMALL_HYPONYM_FILE);
        List<String> words = List.of("occurrence", "change");

        NgordnetQuery nq = new NgordnetQuery(words, 0, 0, 0);
        String actual = studentHandler.handle(nq);
        String expected = "[alteration, change, increase, jump, leap, modification, saltation, transition]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void ListOfWordsTest() {
        HyponymsHandler hyponymsHandler = new HyponymsHandler(
                WORDS_FILE, TOTAL_COUNTS_FILE, SMALL_SYNSET_FILE, SMALL_HYPONYM_FILE);
        List<String> words = new ArrayList<>();
        words.add("change");
        words.add("occurrence");
        NgordnetQuery q1 = new NgordnetQuery(words, 2000, 2020, 0);
        Set<String> result1 = new TreeSet<>(Set.of("alteration", "change", "increase", "jump", "leap",
                "modification", "saltation", "transition"));
        assertThat(hyponymsHandler.handle(q1)).isEqualTo(result1.toString());
        words.clear();

        words.add("transition");
        words.add("flashback");
        NgordnetQuery q2 = new NgordnetQuery(words, 2000, 2020, 0);
        Set<String> result2 = new TreeSet<>(Set.of("flashback"));
        assertThat(hyponymsHandler.handle(q2)).isEqualTo(result2.toString());
        words.clear();

        words.add("conversation");
        words.add("mutation");
        NgordnetQuery q3 = new NgordnetQuery(words, 2000, 2020, 0);
        Set<String> result3 = new TreeSet<>();
        assertThat(hyponymsHandler.handle(q3)).isEqualTo(result3.toString());
    }

    @Test
    public void kTest() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymHandler(
                "data/ngrams/top_49887_words.csv", TOTAL_COUNTS_FILE, LARGE_SYNSET_FILE, LARGE_HYPONYM_FILE);

        List<String> words1 = List.of("change", "occurrence");
        NgordnetQuery q1 = new NgordnetQuery(words1, 1950, 2020, 5);
        Set<String> result = new TreeSet<>(Set.of("change", "death", "development", "going", "increase"));
        assertThat(studentHandler.handle(q1)).isEqualTo(result.toString());

        List<String> words2 = List.of("dog", "cat");
        NgordnetQuery q2 = new NgordnetQuery(words2, 1950, 2020, 3);
        assertThat(studentHandler.handle(q2)).isEqualTo(new TreeSet<>().toString());
    }

}
