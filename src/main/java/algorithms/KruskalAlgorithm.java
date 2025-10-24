package algorithms;

import graph.Edge;
import graph.Graph;
import models.MSTResult;

import java.util.*;

public class KruskalAlgorithm {
    private long operationCount = 0;

    // Union-Find (Disjoint Set Union) data structure
    private static class UnionFind {
        private final int[] parent;
        private final int[] rank;

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) {
                return false; // Already in same set
            }

            // Union by rank
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            return true;
        }
    }

    public MSTResult findMST(Graph graph) {
        operationCount = 0;
        long startTime = System.nanoTime();

        int vertices = graph.getVertices();

        if (vertices == 0) {
            return createEmptyResult(startTime);
        }

        // Get all edges and sort by weight
        List<Edge> edges = new ArrayList<>(graph.getAllEdges());
        Collections.sort(edges); // Uses Edge's compareTo method
        operationCount += edges.size() * Math.log(edges.size()); // Approximate sort operations

        UnionFind uf = new UnionFind(vertices);
        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;

        // Process edges in order of weight
        for (Edge edge : edges) {
            operationCount++; // Comparison/processing operation

            int source = edge.getSource();
            int destination = edge.getDestination();

            // Check if adding this edge creates a cycle
            if (uf.union(source, destination)) {
                mstEdges.add(edge);
                totalCost += edge.getWeight();
                operationCount += 2; // Union operations (find + union)

                // MST complete when we have V-1 edges
                if (mstEdges.size() == vertices - 1) {
                    break;
                }
            }
        }

        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;

        return new MSTResult("Kruskal's Algorithm", mstEdges, totalCost,
                vertices, graph.getEdgeCount(), operationCount, executionTimeMs);
    }

    private MSTResult createEmptyResult(long startTime) {
        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;
        return new MSTResult("Kruskal's Algorithm", new ArrayList<>(), 0,
                0, 0, 0, executionTimeMs);
    }
}