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

    private final Node sentinel;
    private Node curr;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.pre = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T x) {
        Node firstNode = new Node(sentinel, x, sentinel.next);
        sentinel.next.pre = firstNode;
        sentinel.next = firstNode;
        size++;
    }

    @Override
    public void addLast(T x) {
        Node lastNode = new Node(sentinel.pre, x, sentinel);
        sentinel.pre.next = lastNode;
        sentinel.pre = lastNode;
        size++;
    }

    @Override
    public List<T> toList() {
        List<T> lst = new ArrayList<>();
        Node p = sentinel.next;
        while (p != sentinel) {
            lst.add(p.item);
            p = p.next;
        }
        return lst;
    }

    @Override
    public boolean isEmpty() {
        return sentinel.pre == sentinel && sentinel.next == sentinel;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T returnItem = sentinel.next.item;
        Node secNode = sentinel.next.next;
        sentinel.next = secNode;
        secNode.pre = sentinel;
        size--;
        return returnItem;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T returnItem = sentinel.pre.item;
        Node reserveSecNode = sentinel.pre.pre;
        sentinel.pre = reserveSecNode;
        reserveSecNode.next = sentinel;
        size--;
        return returnItem;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index > size - 1) {
            return null;
        }
        int count = 0;
        Node p = sentinel.next;
        while (true) {
            if (count == index) {
                return p.item;
            } else {
                p = p.next;
                count++;
            }
        }
    }

    @Override
    public T getRecursive(int index) {
        if (index < 0 || index > size - 1) {
            return null;
        }
        if (curr == null) {
            curr = sentinel.next;
        }
        if (index == 0) {
            return curr.item;
        }
        curr = curr.next;
        return getRecursive(index - 1);
    }

    public static void main(String[] args) {
        Deque<Integer> lld = new LinkedListDeque<>();
        lld.addFirst(1);
        lld.addFirst(0);
        lld.addLast(2);
        System.out.print(lld.toList());
    }
}
