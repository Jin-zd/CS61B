package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.word_net.WordNet;

import java.util.List;

public class HyponymsHandler extends NgordnetQueryHandler {
    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        WordNet wn = new WordNet("data/wordnet/synsets16.txt", "data/wordnet/hyponyms16.txt");
        String word = words.iterator().next();
        return wn.hyponyms(word).toString();
    }
}
