package ngordnet.word_net;

import java.util.*;

public class Graph {

    private int vexNums;
    private final ArrayList<Integer>[] adjTable;
    private final TreeMap<Integer, String> nodeMap;

    public Graph(int vexNums) {
        this.vexNums = 0;
        nodeMap = new TreeMap<>();
        adjTable = new ArrayList[vexNums];
        for (int i = 0; i < adjTable.length; i++) {
            adjTable[i] = new ArrayList<>();
        }
    }

    public void addNode(int v, String word) {
        vexNums += 1;
        nodeMap.put(v, word);
    }

    public void addEdge(int v, int w) {
        if (nodeMap.containsKey(w)) {
            if (v >= 0 && v < adjTable.length) {
                adjTable[v].add(w);
            }
        }
    }

    public String[] getWord(int v) {
        return nodeMap.get(v).split(" ");
    }

    public Iterable<Integer> adj(int v) {
        Set<Integer> set = new TreeSet<>();
        if (v > 0 && v < adjTable.length) {
            if (adjTable[v].isEmpty()) {
                return null;
            }
            set.addAll(adjTable[v]);
        }
        return set;
    }

    public Set<Integer> getMatchVex(String word) {
        Set<Integer> set = new TreeSet<>();
        for (int vex : nodeMap.keySet()) {
            if (nodeMap.get(vex).contains(word)) {
                set.add(vex);
            }
        }
        return set;
    }

    public Set<Integer> DepthFirstPaths(int start) {
        Set<Integer> set = new TreeSet<>();
        boolean[] marked = new boolean[V()];
        DFS(start, marked);
        for (int i = 0; i < V(); i++) {
            if (marked[i]) {
                set.add(i);
            }
        }
        return set;
    }

    private void DFS(int v, boolean[] marked) {
        marked[v] = true;
        if (adj(v) == null) {
            return;
        }
        for (int w : adj(v)) {
            if (!marked[w]) {
                DFS(w, marked);
            }
        }
    }

    public int V() {
        return vexNums;
    }

    public int degrees(int v) {
        int degree = 0;
        for (int w : adj(v)) {
            degree += 1;
        }
        return degree;
    }

}
