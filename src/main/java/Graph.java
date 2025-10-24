package graph;

import java.util.*;

public class Graph {
    private final List<String> nodes;
    private final Map<String, Integer> nodeToIndex;
    private final List<List<Edge>> adjacencyList;
    private final List<Edge> allEdges;

    public Graph(List<String> nodes) {
        this.nodes = new ArrayList<>(nodes);
        this.nodeToIndex = new HashMap<>();
        this.adjacencyList = new ArrayList<>();
        this.allEdges = new ArrayList<>();

        for (int i = 0; i < nodes.size(); i++) {
            String node = nodes.get(i);
            nodeToIndex.put(node, i);
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(String from, String to, int weight) {
        if (!nodeToIndex.containsKey(from) || !nodeToIndex.containsKey(to)) {
            throw new IllegalArgumentException("Invalid node: " + from + " or " + to);
        }

        Edge edge = new Edge(from, to, weight);
        Edge reverseEdge = new Edge(to, from, weight);

        int fromIdx = nodeToIndex.get(from);
        int toIdx = nodeToIndex.get(to);

        adjacencyList.get(fromIdx).add(edge);
        adjacencyList.get(toIdx).add(reverseEdge);

        allEdges.add(edge);
    }

    public int getVertexCount() {
        return nodes.size();
    }

    public int getEdgeCount() {
        return allEdges.size();
    }

    public List<String> getNodes() {
        return new ArrayList<>(nodes);
    }

    public List<Edge> getEdges(String node) {
        Integer index = nodeToIndex.get(node);
        if (index == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(adjacencyList.get(index));
    }

    public List<Edge> getAllEdges() {
        return new ArrayList<>(allEdges);
    }

    public boolean isConnected() {
        if (nodes.isEmpty()) return true;

        Set<String> visited = new HashSet<>();
        dfs(nodes.get(0), visited);

        return visited.size() == nodes.size();
    }

    private void dfs(String node, Set<String> visited) {
        visited.add(node);
        for (Edge edge : getEdges(node)) {
            if (!visited.contains(edge.getTo())) {
                dfs(edge.getTo(), visited);
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Graph{vertices=%d, edges=%d}", nodes.size(), allEdges.size());
    }
}