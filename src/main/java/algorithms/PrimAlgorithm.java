package algorithms;

import graph.Edge;
import graph.Graph;
import models.MSTResult;
import java.util.*;

public class PrimAlgorithm {
    private long operationsCount = 0;

    public MSTResult findMST(Graph graph) {
        operationsCount = 0;
        long startTime = System.nanoTime();

        List<String> nodes = graph.getNodes();
        int vertexCount = nodes.size();

        if (vertexCount == 0) {
            return createEmptyResult(startTime, graph);
        }

        Set<String> inMST = new HashSet<>();
        Map<String, Integer> key = new HashMap<>();
        Map<String, String> parent = new HashMap<>();

        for (String node : nodes) {
            key.put(node, Integer.MAX_VALUE);
        }

        PriorityQueue<NodeKey> pq = new PriorityQueue<>();

        String startNode = nodes.get(0);
        key.put(startNode, 0);
        pq.offer(new NodeKey(startNode, 0));

        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;

        while (!pq.isEmpty()) {
            NodeKey current = pq.poll();
            String u = current.node;
            operationsCount++;

            if (inMST.contains(u)) {
                continue;
            }

            inMST.add(u);

            if (parent.containsKey(u)) {
                Edge mstEdge = new Edge(parent.get(u), u, key.get(u));
                mstEdges.add(mstEdge);
                totalCost += key.get(u);
            }

            for (Edge edge : graph.getEdges(u)) {
                String v = edge.getTo();
                int weight = edge.getWeight();
                operationsCount++;

                if (!inMST.contains(v) && weight < key.get(v)) {
                    key.put(v, weight);
                    parent.put(v, u);
                    pq.offer(new NodeKey(v, weight));
                    operationsCount++;
                }
            }
        }

        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;

        return new MSTResult("Prim", mstEdges, totalCost, vertexCount,
                graph.getEdgeCount(), operationsCount, executionTimeMs);
    }

    private MSTResult createEmptyResult(long startTime, Graph graph) {
        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;
        return new MSTResult("Prim", new ArrayList<>(), 0, 0, 0, 0, executionTimeMs);
    }

    private static class NodeKey implements Comparable<NodeKey> {
        String node;
        int key;

        NodeKey(String node, int key) {
            this.node = node;
            this.key = key;
        }

        @Override
        public int compareTo(NodeKey other) {
            return Integer.compare(this.key, other.key);
        }
    }
}