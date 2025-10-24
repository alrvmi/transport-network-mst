package utils;

import java.util.*;

public class GraphGenerator {

    public static class GraphInput {
        public int id;
        public List<String> nodes;
        public List<EdgeInput> edges;

        public GraphInput(int id, List<String> nodes, List<EdgeInput> edges) {
            this.id = id;
            this.nodes = nodes;
            this.edges = edges;
        }
    }

    public static class EdgeInput {
        public String from;
        public String to;
        public int weight;

        public EdgeInput(String from, String to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    public static List<GraphInput> generateAllGraphs() {
        List<GraphInput> graphs = new ArrayList<>();
        Random random = new Random(42);

        int[] smallVertices = {4, 4, 5, 5, 6};
        int[] smallEdges = {5, 10, 15, 20, 25};

        for (int i = 0; i < 5; i++) {
            graphs.add(generateGraph(i + 1, smallVertices[i], smallEdges[i], random));
        }

        for (int i = 0; i < 10; i++) {
            int vertices = 10 + i;
            int edges = 15 + i * 4;
            graphs.add(generateGraph(6 + i, vertices, edges, random));
        }

        for (int i = 0; i < 10; i++) {
            int vertices = 20 + i;
            int edges = 30 + i * 5;
            graphs.add(generateGraph(16 + i, vertices, edges, random));
        }

        graphs.add(generateGraph(26, 30, 60, random));
        graphs.add(generateGraph(27, 40, 90, random));
        graphs.add(generateGraph(28, 50, 120, random));

        return graphs;
    }

    private static GraphInput generateGraph(int id, int vertexCount, int targetEdges, Random random) {
        List<String> nodes = new ArrayList<>();

        for (int i = 0; i < vertexCount; i++) {
            nodes.add(generateNodeName(i));
        }

        List<EdgeInput> edges = new ArrayList<>();
        Set<String> edgeSet = new HashSet<>();

        List<Integer> treeNodes = new ArrayList<>();
        treeNodes.add(0);

        for (int i = 1; i < vertexCount; i++) {
            int parentIdx = treeNodes.get(random.nextInt(treeNodes.size()));
            int weight = random.nextInt(50) + 1;

            String from = nodes.get(parentIdx);
            String to = nodes.get(i);
            edges.add(new EdgeInput(from, to, weight));
            edgeSet.add(getEdgeKey(parentIdx, i));

            treeNodes.add(i);
        }

        int maxPossibleEdges = vertexCount * (vertexCount - 1) / 2;
        int edgesToAdd = Math.min(targetEdges - edges.size(), maxPossibleEdges - edges.size());

        for (int i = 0; i < edgesToAdd; i++) {
            int from = random.nextInt(vertexCount);
            int to = random.nextInt(vertexCount);

            if (from == to) continue;
            if (from > to) { int temp = from; from = to; to = temp; }

            String edgeKey = getEdgeKey(from, to);
            if (edgeSet.contains(edgeKey)) {
                i--;
                continue;
            }

            int weight = random.nextInt(50) + 1;
            edges.add(new EdgeInput(nodes.get(from), nodes.get(to), weight));
            edgeSet.add(edgeKey);
        }

        return new GraphInput(id, nodes, edges);
    }

    private static String generateNodeName(int index) {
        StringBuilder name = new StringBuilder();
        do {
            name.insert(0, (char) ('A' + (index % 26)));
            index = index / 26 - 1;
        } while (index >= 0);
        return name.toString();
    }

    private static String getEdgeKey(int from, int to) {
        return from + "-" + to;
    }
}