package models;

import graph.Edge;
import java.util.List;

public class MSTResult {
    private final String algorithmName;
    private final List<Edge> mstEdges;
    private final int totalCost;
    private final int vertexCount;
    private final int edgeCount;
    private final long operationCount;
    private final double executionTimeMs;

    public MSTResult(String algorithmName, List<Edge> mstEdges, int totalCost,
                     int vertexCount, int edgeCount, long operationCount,
                     double executionTimeMs) {
        this.algorithmName = algorithmName;
        this.mstEdges = mstEdges;
        this.totalCost = totalCost;
        this.vertexCount = vertexCount;
        this.edgeCount = edgeCount;
        this.operationCount = operationCount;
        this.executionTimeMs = executionTimeMs;
    }

    // Getters
    public String getAlgorithmName() { return algorithmName; }
    public List<Edge> getMstEdges() { return mstEdges; }
    public int getTotalCost() { return totalCost; }
    public int getVertexCount() { return vertexCount; }
    public int getEdgeCount() { return edgeCount; }
    public long getOperationCount() { return operationCount; }
    public double getExecutionTimeMs() { return executionTimeMs; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Algorithm: ").append(algorithmName).append("\n");
        sb.append("Total Cost: ").append(totalCost).append("\n");
        sb.append("MST Edges (").append(mstEdges.size()).append("):\n");
        for (Edge e : mstEdges) {
            sb.append("  ").append(e).append("\n");
        }
        sb.append("Operations: ").append(operationCount).append("\n");
        sb.append("Execution Time: ").append(executionTimeMs).append(" ms\n");
        return sb.toString();
    }
}