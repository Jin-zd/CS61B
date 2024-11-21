package ngordnet.ngrams;

import org.apache.commons.collections.list.AbstractLinkedList;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        for (int key : ts.keySet()) {
            if (key < startYear) {
                continue;
            }
            if (key > endYear) {
                break;
            }
            this.put(key, ts.get(key));
        }
    }

    /**
     * Returns all years for this TimeSeries (in any order).
     */
    public List<Integer> years() {
        return new ArrayList<>(this.keySet());
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        List<Double> dataList = new ArrayList<>();
        for (int key : this.keySet()) {
            dataList.add(this.get(key));
        }
        return dataList;
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     *
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries returnTs = new TimeSeries();
        if (this.isEmpty() && ts.isEmpty()) {
            return returnTs;
        }
        for (int year : this.keySet()) {
            if (ts.containsKey(year)) {
                double sum = this.get(year) + ts.get(year);
                returnTs.put(year, sum);
            }else {
                returnTs.put(year, this.get(year));
            }
        }
        for (int year : ts.keySet()) {
            if (!returnTs.containsKey(year)) {
                returnTs.put(year, ts.get(year));
            }
        }
        return returnTs;
    }

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     *
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries ts) {
        TimeSeries returnTs = new TimeSeries();
        if (this.isEmpty()) {
            return returnTs;
        }
        for (int key : this.keySet()) {
            if (!ts.containsKey(key)) {
                throw new IllegalArgumentException();
            }
            double result = this.get(key) / ts.get(key);
            returnTs.put(key, result);
        }
        return returnTs;
    }
}
