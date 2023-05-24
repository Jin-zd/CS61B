package ngordnet.proj2b_testing;

import ngordnet.word_net.WordNet;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.google.common.truth.Truth.assertThat;

public class WordNetTest {
    @Test
    public void hyponymsTest() {
        WordNet wn = new WordNet("data/wordnet/synsets16.txt", "data/wordnet/hyponyms16.txt");
        assertThat(wn.hyponyms("flashback")).isEqualTo(Set.of("flashback"));
        assertThat(wn.hyponyms("transition")).isEqualTo(Set.of("flashback", "jump", "leap", "saltation", "transition"));
        assertThat(wn.hyponyms("change")).isEqualTo(Set.of("alteration", "change", "demotion", "increase", "jump",
                "leap", "modification", "saltation", "transition", "variation"));
    }
}
