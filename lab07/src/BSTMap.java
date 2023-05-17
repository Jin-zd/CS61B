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

    private int hasChild(BSTNode node) {
        if (node == null) {
            return 0;
        }
        if (node.left == null && node.right == null) {
            return 0;
        }
        if (node.left != null && node.right != null) {
            return 2;
        }
        return 1;
    }

    private BSTNode newRoot(BSTNode temp, BSTNode tempParent, boolean left) {
        boolean first = true;
        if (left) {
            temp = temp.left;
            while(temp.right != null) {
                if (first) {
                    tempParent = tempParent.left;
                    first = false;
                }
                temp = temp.right;
                tempParent = tempParent.right;
            }
        } else {
            temp = temp.right;
            while(temp.left != null) {
                if (first) {
                    tempParent = tempParent.right;
                    first = false;
                }
                temp = temp.left;
                tempParent = tempParent.left;
            }
        }
        return temp;
    }

    private void exchangeNode(BSTNode p, BSTNode temp, BSTNode parent, BSTNode tempParent, boolean left) {
        temp = newRoot(temp, tempParent, left);
        BSTNode pLeft = p.left;
        BSTNode pRight = p.right;
        p.left = temp.left;
        p.right = temp.right;
        if (parent != null) {
            if (left) {
                parent.left = temp;
            } else {
                parent.right = temp;
            }
        }
        if (tempParent != null) {
            if (tempParent.equals(p)) {
                temp.left = p;
                temp.right = pRight;
                return;
            }
            tempParent.left = p;
        }
        temp.left = pLeft;
        temp.right = pRight;
    }

    @Override
    public V remove(K key) {
        if (node == null) {
            return null;
        }
        V returnValue = null;
        BSTNode parent = null;
        BSTNode p = node;
        boolean left = true;
        while(p != null) {
            if (p.key.compareTo(key) < 0) {
                parent = p;
                p = p.right;
                left = false;
            } else if (p.key.compareTo(key) > 0){
                parent = p;
                p = p.left;
                left = true;
            }
            if (p != null && p.key.equals(key)) {
                returnValue = p.value;
                if (parent == null) {
                    if (hasChild(p) == 0) {
                        node = null;
                        break;
                    }
                    if (p.left != null) {
                        parent = p.left;
                        left = true;
                    } else {
                        parent = p.right;
                        left = false;
                    }
                    node = newRoot(p, p, left);
                    exchangeNode(p, p, null, p, left);
                } else if (hasChild(p) == 0) {
                    if (left) {
                        parent.left = null;
                    } else {
                        parent.right = null;
                    }
                    break;
                } else if (hasChild(p) == 1) {
                    if (p.right != null) {
                        if (left) {
                            parent.left = p.right;
                        } else {
                            parent.right = p.right;
                        }
                    } else {
                        if (left) {
                            parent.left = p.left;
                        } else {
                            parent.left = p.right;
                        }
                    }
                    break;
                } else if (hasChild(p) == 2) {
                    exchangeNode(p, p, parent, p, left);
                    if (left) {
                        parent.left = p.left;
                    } else {
                        parent.left = p.right;
                    }
                }
            }
        }
        if (returnValue != null) {
            size -= 1;
        }
        return returnValue;
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
