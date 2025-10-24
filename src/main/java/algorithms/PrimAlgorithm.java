package algorithms;

import graph.Edge;
import graph.Graph;
import models.MSTResult;

import java.util.*;

public class PrimAlgorithm {
    private long operationCount = 0;

    public MSTResult findMST(Graph graph) {
        operationCount = 0;
        long startTime = System.nanoTime();

        int vertices = graph.getVertices();

        // Handle edge cases
        if (vertices == 0) {
            return createEmptyResult(startTime);
        }

        boolean[] inMST = new boolean[vertices];
        int[] key = new int[vertices];
        int[] parent = new int[vertices];

        Arrays.fill(key, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        // Priority queue: [vertex, key_value]
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));

        // Start from vertex 0
        key[0] = 0;
        pq.offer(new int[]{0, 0});

        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];
            operationCount++; // Poll operation

            if (inMST[u]) continue;

            inMST[u] = true;

            // Add edge to MST (except for starting vertex)
            if (parent[u] != -1) {
                Edge edge = new Edge(parent[u], u, key[u]);
                mstEdges.add(edge);
                totalCost += key[u];
            }

            // Update keys of adjacent vertices
            for (Edge edge : graph.getEdges(u)) {
                int v = edge.getDestination();
                int weight = edge.getWeight();
                operationCount++; // Comparison operation

                if (!inMST[v] && weight < key[v]) {
                    key[v] = weight;
                    parent[v] = u;
                    pq.offer(new int[]{v, key[v]});
                    operationCount++; // Priority queue insertion
                }
            }
        }

        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;

        return new MSTResult("Prim's Algorithm", mstEdges, totalCost,
                vertices, graph.getEdgeCount(), operationCount, executionTimeMs);
    }

    private MSTResult createEmptyResult(long startTime) {
        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;
        return new MSTResult("Prim's Algorithm", new ArrayList<>(), 0,
                0, 0, 0, executionTimeMs);
    }
}