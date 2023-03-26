import java.util.ArrayList;
import java.util.List;

public class LinkedListDeque<T> implements Deque<T> {
    private class Node {
        Node pre;
        T item;
        Node next;

        public Node(Node pre, T item, Node next) {
            this.pre = pre;
            this.item = item;
            this.next = next;
        }
    }

    private final Node sentinal;
    private int size;

    public LinkedListDeque() {
        sentinal = new Node(null, null, null);
        sentinal.pre = sentinal;
        sentinal.next = sentinal;
        size = 0;
    }

    @Override
    public void addFirst(T x) {
        Node firstNode = new Node(sentinal, x, sentinal.next);
        sentinal.next.pre = firstNode;
        sentinal.next = firstNode;
        size++;
    }

    @Override
    public void addLast(T x) {
        Node lastNode = new Node(sentinal.pre, x, sentinal);
        sentinal.pre.next = lastNode;
        sentinal.pre = lastNode;
        size++;
    }

    @Override
    public List<T> toList() {
        List<T> lst = new ArrayList<>();
        Node p = sentinal.next;
        while (p != sentinal) {
            lst.add(p.item);
            p = p.next;
        }
        return lst;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        return null;
    }

    @Override
    public T removeLast() {
        return null;
    }

    @Override
    public T get(int index) {
        return null;
    }

    @Override
    public T getRecursive(int index) {
        return null;
    }

    public static void main(String[] args) {
        Deque<Integer> lld = new LinkedListDeque<>();
        lld.addFirst(1);
        lld.addFirst(0);
        lld.addLast(2);
        System.out.print(lld.toList());
    }

}
