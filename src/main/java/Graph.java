package graph;

import java.util.*;

public class Graph {
    private final int vertices;
    private final List<List<Edge>> adjacencyList;
    private final List<Edge> allEdges;

    public Graph(int vertices) {
        this.vertices = vertices;
        this.adjacencyList = new ArrayList<>(vertices);
        this.allEdges = new ArrayList<>();

        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int source, int destination, int weight) {
        if (source < 0 || source >= vertices || destination < 0 || destination >= vertices) {
            throw new IllegalArgumentException("Invalid vertex index");
        }

        Edge edge = new Edge(source, destination, weight);
        adjacencyList.get(source).add(edge);
        adjacencyList.get(destination).add(new Edge(destination, source, weight));

        // Store only one instance for undirected graph
        if (source <= destination) {
            allEdges.add(edge);
        }
    }

    public int getVertices() {
        return vertices;
    }

    public List<Edge> getEdges(int vertex) {
        return adjacencyList.get(vertex);
    }

    public List<Edge> getAllEdges() {
        return new ArrayList<>(allEdges);
    }

    public int getEdgeCount() {
        return allEdges.size();
    }

    public boolean isConnected() {
        if (vertices == 0) return true;

        boolean[] visited = new boolean[vertices];
        dfs(0, visited);

        for (boolean v : visited) {
            if (!v) return false;
        }
        return true;
    }

    private void dfs(int vertex, boolean[] visited) {
        visited[vertex] = true;
        for (Edge edge : adjacencyList.get(vertex)) {
            if (!visited[edge.getDestination()]) {
                dfs(edge.getDestination(), visited);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph with ").append(vertices).append(" vertices and ")
                .append(allEdges.size()).append(" edges:\n");
        for (Edge edge : allEdges) {
            sb.append(edge).append("\n");
        }
        return sb.toString();
    }
}