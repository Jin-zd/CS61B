package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;
import ngordnet.plotting.Plotter;
import org.knowm.xchart.XYChart;

import java.util.ArrayList;
import java.util.List;

public class HistoryHandler extends NgordnetQueryHandler {

    private final NGramMap ngm;

    public HistoryHandler(NGramMap ngm) {
        this.ngm = ngm;
    }

    @Override
    public String handle(NgordnetQuery q) {
        System.out.println("Got query that looks like:");
        System.out.println("Words: " + q.words());
        System.out.println("Start Year: " + q.startYear());
        System.out.println("End Year: " + q.endYear());

        System.out.println("""
                But I'm totally ignoring that and just plotting a parabola
                and a sine wave, because your job will be to figure out how to
                actually use the query data.""");


        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        List<TimeSeries> tsList = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (String word : words) {
            labels.add(word);
            tsList.add(ngm.weightHistory(word, startYear, endYear));
        }

        XYChart chart = Plotter.generateTimeSeriesChart(labels, tsList);
        return Plotter.encodeChartAsString(chart);
    }
}
