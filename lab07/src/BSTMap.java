import edu.princeton.cs.algs4.BST;

import javax.lang.model.util.Elements;
import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private class BSTNode {
        K key;
        V value;
        BSTNode left;
        BSTNode right;

        public BSTNode(K key, V value, BSTNode left, BSTNode right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    private BSTNode node;
    private int size;

    public BSTMap() {
        node = null;
        size = 0;
    }

    private BSTNode put(K key, V value, BSTNode node) {
        if (node == null) {
            return new BSTNode(key, value, null, null);
        } else if (key.equals(node.key)) {
            node.value = value;
        } else if (key.compareTo(node.key) < 0) {
            node.left = put(key, value, node.left);
        } else if (key.compareTo(node.key) > 0) {
            node.right = put(key, value, node.right);
        }
        return node;
    }

    @Override
    public void put(K key, V value) {
        if (!containsKey(key)) {
            size += 1;
        }
        node = put(key, value, node);
    }

    private V get(K key, BSTNode node) {
        if (node == null) {
            return null;
        }
        if (key.equals(node.key)) {
            return node.value;
        } else if (key.compareTo(node.key) < 0) {
            return get(key, node.left);
        } else if (key.compareTo(node.key) > 0) {
            return get(key, node.right);
        }
        return null;
    }

    @Override
    public V get(K key) {
        return get(key, node);
    }

    private boolean containsKey(K key, BSTNode node) {
        if (node == null) {
            return false;
        }
        if (key.equals(node.key)) {
            return true;
        } else if (key.compareTo(node.key) < 0) {
            return containsKey(key, node.left);
        } else if (key.compareTo(node.key) > 0) {
            return containsKey(key, node.right);
        }
        return false;
    }

    @Override
    public boolean containsKey(K key) {
        return containsKey(key, node);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        node = null;
    }

    private void inorderTraversal(BSTNode node, TreeSet<K> set) {
        if (node == null) {
            return;
        }
        inorderTraversal(node.left, set);
        set.add(node.key);
        inorderTraversal(node.right, set);
    }

    @Override
    public Set<K> keySet() {
        TreeSet<K> set = new TreeSet<>();
        inorderTraversal(node, set);
        return set;
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }


    private class BSTMapIterator implements Iterator<K> {
        Set<K> set;

        public BSTMapIterator() {
            set = keySet();
        }
        @Override
        public boolean hasNext() {
            return set.iterator().hasNext();
        }

        @Override
        public K next() {
            return set.iterator().next();
        }
    }
    @Override
    public Iterator<K> iterator() {
        return new BSTMapIterator();
    }
}
