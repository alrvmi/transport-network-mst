import algorithms.KruskalAlgorithm;
import algorithms.PrimAlgorithm;
import graph.Edge;
import graph.Graph;
import models.MSTResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class MSTTest {

    @Test
    public void testIdenticalCost() {
        Graph graph = createTestGraph1();

        PrimAlgorithm prim = new PrimAlgorithm();
        KruskalAlgorithm kruskal = new KruskalAlgorithm();

        MSTResult primResult = prim.findMST(graph);
        MSTResult kruskalResult = kruskal.findMST(graph);

        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost());
    }

    @Test
    public void testEdgeCount() {
        Graph graph = createTestGraph1();

        PrimAlgorithm prim = new PrimAlgorithm();
        MSTResult result = prim.findMST(graph);

        assertEquals(graph.getVertexCount() - 1, result.getMstEdges().size());
    }

    @Test
    public void testAcyclic() {
        Graph graph = createTestGraph2();

        KruskalAlgorithm kruskal = new KruskalAlgorithm();
        MSTResult result = kruskal.findMST(graph);

        assertFalse(hasCycle(result.getMstEdges(), graph.getNodes()));
    }

    @Test
    public void testConnectivity() {
        Graph graph = createTestGraph1();

        PrimAlgorithm prim = new PrimAlgorithm();
        MSTResult result = prim.findMST(graph);

        assertTrue(isConnected(result.getMstEdges(), graph.getNodes()));
    }

    @Test
    public void testDisconnectedGraph() {
        Graph graph = createDisconnectedGraph();

        KruskalAlgorithm kruskal = new KruskalAlgorithm();
        MSTResult result = kruskal.findMST(graph);

        assertTrue(result.getMstEdges().size() < graph.getVertexCount() - 1);
    }

    @Test
    public void testExecutionTimeNonNegative() {
        Graph graph = createTestGraph2();

        PrimAlgorithm prim = new PrimAlgorithm();
        KruskalAlgorithm kruskal = new KruskalAlgorithm();

        MSTResult primResult = prim.findMST(graph);
        MSTResult kruskalResult = kruskal.findMST(graph);

        assertTrue(primResult.getExecutionTimeMs() >= 0);
        assertTrue(kruskalResult.getExecutionTimeMs() >= 0);
    }

    @Test
    public void testOperationCountNonNegative() {
        Graph graph = createTestGraph1();

        PrimAlgorithm prim = new PrimAlgorithm();
        MSTResult result = prim.findMST(graph);

        assertTrue(result.getOperationsCount() >= 0);
    }

    @Test
    public void testReproducibility() {
        Graph graph = createTestGraph2();

        KruskalAlgorithm kruskal = new KruskalAlgorithm();
        MSTResult result1 = kruskal.findMST(graph);
        MSTResult result2 = kruskal.findMST(graph);

        assertEquals(result1.getTotalCost(), result2.getTotalCost());
        assertEquals(result1.getMstEdges().size(), result2.getMstEdges().size());
    }

    @Test
    public void testEmptyGraph() {
        Graph graph = new Graph(new ArrayList<>());

        PrimAlgorithm prim = new PrimAlgorithm();
        MSTResult result = prim.findMST(graph);

        assertEquals(0, result.getTotalCost());
        assertEquals(0, result.getMstEdges().size());
    }

    @Test
    public void testSingleVertex() {
        List<String> nodes = Arrays.asList("A");
        Graph graph = new Graph(nodes);

        KruskalAlgorithm kruskal = new KruskalAlgorithm();
        MSTResult result = kruskal.findMST(graph);

        assertEquals(0, result.getTotalCost());
        assertEquals(0, result.getMstEdges().size());
    }

    private Graph createTestGraph1() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D");
        Graph graph = new Graph(nodes);
        graph.addEdge("A", "B", 1);
        graph.addEdge("A", "C", 4);
        graph.addEdge("B", "C", 2);
        graph.addEdge("B", "D", 5);
        graph.addEdge("C", "D", 3);
        return graph;
    }

    private Graph createTestGraph2() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D", "E");
        Graph graph = new Graph(nodes);
        graph.addEdge("A", "B", 4);
        graph.addEdge("A", "C", 3);
        graph.addEdge("B", "C", 2);
        graph.addEdge("B", "D", 5);
        graph.addEdge("C", "D", 7);
        graph.addEdge("C", "E", 8);
        graph.addEdge("D", "E", 6);
        return graph;
    }

    private Graph createDisconnectedGraph() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D", "E");
        Graph graph = new Graph(nodes);
        graph.addEdge("A", "B", 1);
        graph.addEdge("B", "C", 2);
        graph.addEdge("D", "E", 3);
        return graph;
    }

    private boolean hasCycle(List<Edge> edges, List<String> nodes) {
        Map<String, String> parent = new HashMap<>();
        for (String node : nodes) {
            parent.put(node, node);
        }

        for (Edge edge : edges) {
            String root1 = find(parent, edge.getFrom());
            String root2 = find(parent, edge.getTo());

            if (root1.equals(root2)) {
                return true;
            }

            parent.put(root1, root2);
        }

        return false;
    }

    private String find(Map<String, String> parent, String node) {
        if (!parent.get(node).equals(node)) {
            parent.put(node, find(parent, parent.get(node)));
        }
        return parent.get(node);
    }

    private boolean isConnected(List<Edge> edges, List<String> nodes) {
        if (nodes.isEmpty()) return true;

        Map<String, List<String>> adj = new HashMap<>();
        for (String node : nodes) {
            adj.put(node, new ArrayList<>());
        }

        for (Edge edge : edges) {
            adj.get(edge.getFrom()).add(edge.getTo());
            adj.get(edge.getTo()).add(edge.getFrom());
        }

        Set<String> visited = new HashSet<>();
        dfs(nodes.get(0), adj, visited);

        return visited.size() == nodes.size();
    }

    private void dfs(String node, Map<String, List<String>> adj, Set<String> visited) {
        visited.add(node);
        for (String neighbor : adj.get(node)) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, adj, visited);
            }
        }
    }
}