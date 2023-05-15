package hashmap;

import java.util.*;

/**
 * A hash table-backed Map implementation. Provides amortized constant time
 * access to elements via get(), remove(), and put() in the best case.
 * <p>
 * Assumes null keys will never be inserted, and does not resize down upon remove().
 *
 * @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private int size;
    private double currLoadFactor;
    private final double loadFactor;

    /**
     * Constructors
     */
    public MyHashMap() {
        buckets = createTable(16);
        size = 0;
        currLoadFactor = 0;
        loadFactor = 0.75;
    }

    public MyHashMap(int initialCapacity) {
        buckets = createTable(initialCapacity);
        size = 0;
        currLoadFactor = 0;
        loadFactor = 0.75;
    }

    /**
     * MyHashMap constructor that creates a backing array of initialCapacity.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialCapacity initial size of backing array
     * @param loadFactor      maximum load factor
     */
    public MyHashMap(int initialCapacity, double loadFactor) {
        buckets = createTable(initialCapacity);
        size = 0;
        currLoadFactor = 0;
        this.loadFactor = loadFactor;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     * <p>
     * The only requirements of a hash table bucket are that we can:
     * 1. Insert items (`add` method)
     * 2. Remove items (`remove` method)
     * 3. Iterate through items (`iterator` method)
     * <p>
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     * <p>
     * Override this method to use different data structures as
     * the underlying bucket type
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     * <p>
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    private Node getNode(K key) {
        if (buckets == null) {
            return null;
        }
        for (Collection<Node> bucket : buckets) {
            if (bucket == null || bucket.isEmpty()) {
                continue;
            }
            for (Node node : bucket) {
                if (node.key.equals(key)) {
                    return node;
                }
            }
        }
//        int index = Math.floorMod(key.hashCode() & 0x7fffffff, buckets.length);
//        for (Node node : buckets[index]) {
//            if (node.key.equals(key)) {
//                return node;
//            }
//        }
        return null;
    }

    private void loadFactorCalculator() {
        currLoadFactor = (double) size / buckets.length;
    }

    private void resize() {
        if (currLoadFactor < loadFactor) {
            return;
        }
        Collection<Node>[] tempBuckets = createTable(buckets.length * 2);
        for (int i  = 0; i < buckets.length; i++) {
            if (buckets[i] == null || buckets[i].isEmpty()) {
                continue;
            }
            for (Node node : buckets[i]) {
                int hash = node.key.hashCode() & 0x7fffffff;
                int index;
                if ((hash & buckets.length) == 0) {
                    index = i;
                } else {
                    index = i + buckets.length;
                }
                if (tempBuckets[index] == null) {
                    tempBuckets[index] = createBucket();
                }
                tempBuckets[index].add(node);
            }
        }
        buckets = tempBuckets;
    }

    @Override
    public void put(K key, V value) {
        if (buckets == null) {
            return;
        }
        if (containsKey(key)) {
            Objects.requireNonNull(getNode(key)).value = value;
        } else {
            Node newNode = new Node(key, value);
            int index = Math.floorMod(newNode.key.hashCode() & 0x7fffffff, buckets.length);
            if (buckets[index] == null) {
                buckets[index] = createBucket();
            }
            buckets[index].add(newNode);
            size += 1;
            loadFactorCalculator();
            resize();
        }
    }

    @Override
    public V get(K key) {
        Node node = getNode(key);
        if (node != null) {
            return node.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return getNode(key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        buckets = null;
        size = 0;
        currLoadFactor = 0;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }
}
