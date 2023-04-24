package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;

import java.util.List;

public class HistoryTextHandler extends NgordnetQueryHandler {
    private final NGramMap ngm;

    public HistoryTextHandler(NGramMap ngm) {
        this.ngm = ngm;
    }

    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        StringBuilder response = new StringBuilder();
        for (String word : words) {
            response.append(word).append(": {");
            TimeSeries ts = ngm.weightHistory(word, startYear, endYear);
            for (int year : ts.keySet()) {
                response.append(year).append("=").append(ts.get(year)).append(", ");
            }
            response.append("}");
            response.append("\n").append("\n");
        }
        return response.toString();
    }
}
