import java.util.ArrayList;
import java.util.List;

public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int nextFirst;
    private int nextLast;
    private int size;

    private double useRate;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        nextFirst = 4;
        nextLast = 5;
        size = 0;
    }

    private int circleCalculate(int x, int y, boolean add) {
        if (add) {
            if (x + y > items.length - 1) {
                x = x + y - items.length;
            } else {
                x = x + y;
            }
        } else {
            if (x - y < 0) {
                x = x + items.length - y;
            } else {
                x = x - y;
            }
        }
        return x;
    }

    private void useRateCalculate() {
        useRate = size / (double) items.length;
    }

    private void sizeCalculate() {
        if (size == items.length) {
            resizeUp(size * 2);
        }
        size += 1;
        useRateCalculate();
    }

    private void resizeUp(int capacity) {
        T[] temItems = (T[]) new Object[capacity];
        int first = circleCalculate(nextFirst, 1, true);
        int i = first;
        int j = 0;
        while (j < size) {
            if (i < first) {
                temItems[i] = items[i];
            } else {
                temItems[i + capacity / 2] = items[i];
            }
            i = circleCalculate(i, 1, true);
            j += 1;
        }
        items = temItems;
        nextFirst = circleCalculate(first + capacity / 2, 1, false);
    }

    private void resizeDown() {
        if (useRate >= 0.25 && items.length / 2 <= 8) {
            return;
        }
        int capacity = items.length / 2;
        T[] temItems = (T[]) new Object[capacity];
        int first = circleCalculate(nextFirst, 1, true);
        int i = first;
        int j = 0;
        while (j < size) {
            if (i < capacity) {
                temItems[i] = items[i];
            } else {
                temItems[i - capacity] = items[i];
            }
            i = circleCalculate(i, 1, true);
            j += 1;
        }
        items = temItems;
        nextFirst = circleCalculate(first - capacity, 1, false);
        useRateCalculate();
    }

    @Override
    public void addFirst(T x) {
        sizeCalculate();
        items[nextFirst] = x;
        nextFirst = circleCalculate(nextFirst, 1, false);
    }

    @Override
    public void addLast(T x) {
        sizeCalculate();
        items[nextLast] = x;
        nextLast = circleCalculate(nextLast, 1, true);
    }

    @Override
    public List<T> toList() {
        List<T> lst = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            lst.add(get(i));
        }
        return lst;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        T returnT = get(0);
        nextFirst = circleCalculate(nextFirst, 1, true);
        size -= 1;
        useRateCalculate();
        resizeDown();
        return returnT;
    }

    @Override
    public T removeLast() {
        T returnT = get(size - 1);
        nextLast = circleCalculate(nextLast, 1, false);
        size -= 1;
        useRateCalculate();
        resizeDown();
        return returnT;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index > size - 1) {
            return null;
        }
        int realFirst = circleCalculate(nextFirst, 1, true);
        int realIndex = circleCalculate(realFirst, index, true);
        return items[realIndex];
    }
}
