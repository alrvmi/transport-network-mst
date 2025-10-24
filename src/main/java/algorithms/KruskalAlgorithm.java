package algorithms;

import graph.Edge;
import graph.Graph;
import models.MSTResult;
import java.util.*;

public class KruskalAlgorithm {
    private long operationsCount = 0;

    private static class UnionFind {
        private final Map<String, String> parent;
        private final Map<String, Integer> rank;

        public UnionFind(List<String> nodes) {
            parent = new HashMap<>();
            rank = new HashMap<>();

            for (String node : nodes) {
                parent.put(node, node);
                rank.put(node, 0);
            }
        }

        public String find(String x) {
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x)));
            }
            return parent.get(x);
        }

        public boolean union(String x, String y) {
            String rootX = find(x);
            String rootY = find(y);

            if (rootX.equals(rootY)) {
                return false;
            }

            int rankX = rank.get(rootX);
            int rankY = rank.get(rootY);

            if (rankX < rankY) {
                parent.put(rootX, rootY);
            } else if (rankX > rankY) {
                parent.put(rootY, rootX);
            } else {
                parent.put(rootY, rootX);
                rank.put(rootX, rankX + 1);
            }
            return true;
        }
    }

    public MSTResult findMST(Graph graph) {
        operationsCount = 0;
        long startTime = System.nanoTime();

        List<String> nodes = graph.getNodes();
        int vertexCount = nodes.size();

        if (vertexCount == 0) {
            return createEmptyResult(startTime, graph);
        }

        List<Edge> edges = new ArrayList<>(graph.getAllEdges());
        edges = mergeSort(edges);
        operationsCount += (long) (edges.size() * Math.log(edges.size()) / Math.log(2));

        UnionFind uf = new UnionFind(nodes);

        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;

        for (Edge edge : edges) {
            operationsCount++;

            String from = edge.getFrom();
            String to = edge.getTo();

            operationsCount += 2;

            if (uf.union(from, to)) {
                mstEdges.add(edge);
                totalCost += edge.getWeight();
                operationsCount++;

                if (mstEdges.size() == vertexCount - 1) {
                    break;
                }
            }
        }

        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;

        return new MSTResult("Kruskal", mstEdges, totalCost, vertexCount,
                graph.getEdgeCount(), operationsCount, executionTimeMs);
    }

    private List<Edge> mergeSort(List<Edge> edges) {
        if (edges.size() <= 1) {
            return edges;
        }

        int mid = edges.size() / 2;
        List<Edge> left = mergeSort(new ArrayList<>(edges.subList(0, mid)));
        List<Edge> right = mergeSort(new ArrayList<>(edges.subList(mid, edges.size())));

        return merge(left, right);
    }

    private List<Edge> merge(List<Edge> left, List<Edge> right) {
        List<Edge> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i).compareTo(right.get(j)) <= 0) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }

        while (i < left.size()) result.add(left.get(i++));
        while (j < right.size()) result.add(right.get(j++));

        return result;
    }

    private MSTResult createEmptyResult(long startTime, Graph graph) {
        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;
        return new MSTResult("Kruskal", new ArrayList<>(), 0, 0, 0, 0, executionTimeMs);
    }
}