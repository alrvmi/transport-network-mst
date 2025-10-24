import algorithms.KruskalAlgorithm;
import algorithms.PrimAlgorithm;
import graph.Graph;
import models.MSTResult;
import utils.GraphGenerator;
import utils.JSONHandler;
import utils.GraphVisualizer;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Transportation Network MST Optimization ===\n");

        String inputFile = "src/main/resources/input_graphs.json";
        String outputFile = "src/main/resources/output_results.json";
        String visualizationDir = "graph_images";

        System.out.println("Step 1: Generating 28 test graphs...");
        List<GraphGenerator.GraphInput> generatedGraphs = GraphGenerator.generateAllGraphs();
        JSONHandler.writeInputGraphs(inputFile, generatedGraphs);
        System.out.println("Generated graphs: " + generatedGraphs.size() + "\n");

        System.out.println("Step 2: Reading graphs from input file...");
        List<JSONHandler.GraphData> graphs = JSONHandler.readInputGraphs(inputFile);
        System.out.println("Loaded graphs: " + graphs.size() + "\n");

        System.out.println("Step 3: Running MST algorithms...\n");
        PrimAlgorithm prim = new PrimAlgorithm();
        KruskalAlgorithm kruskal = new KruskalAlgorithm();

        List<JSONHandler.ResultData> results = new ArrayList<>();

        for (JSONHandler.GraphData graphData : graphs) {
            int id = graphData.id;
            Graph graph = graphData.graph;

            System.out.println("Processing Graph #" + id);
            System.out.println("  Vertices: " + graph.getVertexCount());
            System.out.println("  Edges: " + graph.getEdgeCount());
            System.out.println("  Connected: " + graph.isConnected());

            MSTResult primResult = prim.findMST(graph);
            MSTResult kruskalResult = kruskal.findMST(graph);

            System.out.println(primResult);
            System.out.println(kruskalResult);

            boolean costMatch = primResult.getTotalCost() == kruskalResult.getTotalCost();
            System.out.println("  Cost Match: " + (costMatch ? "✓" : "✗"));
            System.out.println();

            results.add(new JSONHandler.ResultData(id, graph.getVertexCount(),
                    graph.getEdgeCount(), primResult, kruskalResult));

            System.out.println("  Visualizing graph #" + id + "...");
            GraphVisualizer.visualizeGraph(id, graph, primResult, visualizationDir);
        }

        System.out.println("\nStep 4: Writing results to output file...");
        JSONHandler.writeOutputResults(outputFile, results);

        System.out.println("\n=== Summary ===");
        System.out.println("Total graphs processed: " + graphs.size());
        System.out.println("Input file: " + inputFile);
        System.out.println("Output file: " + outputFile);
        System.out.println("Visualizations: " + visualizationDir + "/");
        System.out.println("\nDone!");
    }
}