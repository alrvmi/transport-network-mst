package models;

import graph.Edge;
import java.util.List;

public class MSTResult {
    private final String algorithmName;
    private final List<Edge> mstEdges;
    private final int totalCost;
    private final int vertexCount;
    private final int originalEdgeCount;
    private final long operationsCount;
    private final double executionTimeMs;

    public MSTResult(String algorithmName, List<Edge> mstEdges, int totalCost,
                     int vertexCount, int originalEdgeCount, long operationsCount,
                     double executionTimeMs) {
        this.algorithmName = algorithmName;
        this.mstEdges = mstEdges;
        this.totalCost = totalCost;
        this.vertexCount = vertexCount;
        this.originalEdgeCount = originalEdgeCount;
        this.operationsCount = operationsCount;
        this.executionTimeMs = executionTimeMs;
    }

    public String getAlgorithmName() { return algorithmName; }
    public List<Edge> getMstEdges() { return mstEdges; }
    public int getTotalCost() { return totalCost; }
    public int getVertexCount() { return vertexCount; }
    public int getOriginalEdgeCount() { return originalEdgeCount; }
    public long getOperationsCount() { return operationsCount; }
    public double getExecutionTimeMs() { return executionTimeMs; }

    @Override
    public String toString() {
        return String.format("\n=== %s ===\nTotal Cost: %d\nMST Edges: %d/%d\nOperations: %d\nTime: %.3f ms",
                algorithmName, totalCost, mstEdges.size(), originalEdgeCount, operationsCount, executionTimeMs);
    }
}