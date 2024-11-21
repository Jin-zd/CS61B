package ngordnet.word_net;

import edu.princeton.cs.algs4.In;

import java.util.*;

public class WordNet {
    Graph graph;
    In in;

    public WordNet(String synsetsPath, String hyponymsPath) {
        creatGraph(synsetsPath, hyponymsPath);
    }

    private void creatGraph(String synsetsPath, String hyponymsPath) {
        in = new In(synsetsPath);
        creatNode();
        in = new In(hyponymsPath);
        creatEdge();
    }

    private void creatNode() {
        Map<Integer, String> nodeMap = new TreeMap<>();
        while (in.hasNextLine()) {
            String[] line = in.readLine().split(",");
            int vex = Integer.parseInt(line[0]);
            String words = line[1];
            nodeMap.put(vex, words);
        }
        int vexNums = nodeMap.keySet().size();
        graph = new Graph(vexNums);
        for (int vex : nodeMap.keySet()) {
            graph.addNode(vex, nodeMap.get(vex));
        }
    }

    private void creatEdge() {
        while (in.hasNextLine()) {
            String[] line = in.readLine().split(",");
            int v = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                int w = Integer.parseInt(line[i]);
                graph.addEdge(v, w);
            }
        }
    }

    public Set<String> hyponyms(String word) {
        Set<String> wordSet = new TreeSet<>();
        Set<Integer> startVexSet = graph.getMatchVex(word);
        Set<Integer> vexSet = new TreeSet<>();
        for (int vex : startVexSet) {
            vexSet.addAll(graph.DepthFirstPaths(vex));
        }
        for (int vex : vexSet) {
            String[] words = graph.getWord(vex);
            wordSet.addAll(Arrays.asList(words));
        }
        return wordSet;
    }
}
