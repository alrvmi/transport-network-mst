package utils;

import com.google.gson.*;
import graph.Edge;
import graph.Graph;
import models.MSTResult;

import java.io.*;
import java.util.*;

public class JSONHandler {

    public static void writeInputGraphs(String filename, List<GraphGenerator.GraphInput> graphs) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonObject root = new JsonObject();
        JsonArray graphsArray = new JsonArray();

        for (GraphGenerator.GraphInput graphInput : graphs) {
            JsonObject graphObj = new JsonObject();
            graphObj.addProperty("id", graphInput.id);

            JsonArray nodesArray = new JsonArray();
            for (String node : graphInput.nodes) {
                nodesArray.add(node);
            }
            graphObj.add("nodes", nodesArray);

            JsonArray edgesArray = new JsonArray();
            for (GraphGenerator.EdgeInput edge : graphInput.edges) {
                JsonObject edgeObj = new JsonObject();
                edgeObj.addProperty("from", edge.from);
                edgeObj.addProperty("to", edge.to);
                edgeObj.addProperty("weight", edge.weight);
                edgesArray.add(edgeObj);
            }
            graphObj.add("edges", edgesArray);

            graphsArray.add(graphObj);
        }

        root.add("graphs", graphsArray);

        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(root, writer);
            System.out.println("Input graphs written to " + filename);
        } catch (IOException e) {
            System.err.println("Error writing input file: " + e.getMessage());
        }
    }

    public static List<GraphData> readInputGraphs(String filename) {
        List<GraphData> graphs = new ArrayList<>();

        try (FileReader reader = new FileReader(filename)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray graphsArray = root.getAsJsonArray("graphs");

            for (JsonElement graphElement : graphsArray) {
                JsonObject graphObj = graphElement.getAsJsonObject();

                int id = graphObj.get("id").getAsInt();
                JsonArray nodesArray = graphObj.getAsJsonArray("nodes");
                List<String> nodes = new ArrayList<>();
                for (JsonElement nodeElement : nodesArray) {
                    nodes.add(nodeElement.getAsString());
                }

                Graph graph = new Graph(nodes);
                JsonArray edgesArray = graphObj.getAsJsonArray("edges");

                for (JsonElement edgeElement : edgesArray) {
                    JsonObject edge = edgeElement.getAsJsonObject();
                    String from = edge.get("from").getAsString();
                    String to = edge.get("to").getAsString();
                    int weight = edge.get("weight").getAsInt();

                    graph.addEdge(from, to, weight);
                }

                graphs.add(new GraphData(id, graph));
            }
        } catch (Exception e) {
            System.err.println("Error reading input file: " + e.getMessage());
            e.printStackTrace();
        }

        return graphs;
    }

    public static void writeOutputResults(String filename, List<ResultData> results) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonObject root = new JsonObject();
        JsonArray resultsArray = new JsonArray();

        for (ResultData result : results) {
            JsonObject resultObj = new JsonObject();
            resultObj.addProperty("graph_id", result.graphId);

            JsonObject inputStats = new JsonObject();
            inputStats.addProperty("vertices", result.vertices);
            inputStats.addProperty("edges", result.edges);
            resultObj.add("input_stats", inputStats);

            resultObj.add("prim", createAlgorithmResult(result.primResult));
            resultObj.add("kruskal", createAlgorithmResult(result.kruskalResult));

            resultsArray.add(resultObj);
        }

        root.add("results", resultsArray);

        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(root, writer);
            System.out.println("Results written to " + filename);
        } catch (IOException e) {
            System.err.println("Error writing output file: " + e.getMessage());
        }
    }

    private static JsonObject createAlgorithmResult(MSTResult result) {
        JsonObject obj = new JsonObject();

        JsonArray edgesArray = new JsonArray();
        for (Edge edge : result.getMstEdges()) {
            JsonObject edgeObj = new JsonObject();
            edgeObj.addProperty("from", edge.getFrom());
            edgeObj.addProperty("to", edge.getTo());
            edgeObj.addProperty("weight", edge.getWeight());
            edgesArray.add(edgeObj);
        }
        obj.add("mst_edges", edgesArray);

        obj.addProperty("total_cost", result.getTotalCost());
        obj.addProperty("operations_count", result.getOperationsCount());
        obj.addProperty("execution_time_ms", Math.round(result.getExecutionTimeMs() * 100.0) / 100.0);

        return obj;
    }

    public static class GraphData {
        public int id;
        public Graph graph;

        public GraphData(int id, Graph graph) {
            this.id = id;
            this.graph = graph;
        }
    }

    public static class ResultData {
        public int graphId;
        public int vertices;
        public int edges;
        public MSTResult primResult;
        public MSTResult kruskalResult;

        public ResultData(int graphId, int vertices, int edges,
                          MSTResult primResult, MSTResult kruskalResult) {
            this.graphId = graphId;
            this.vertices = vertices;
            this.edges = edges;
            this.primResult = primResult;
            this.kruskalResult = kruskalResult;
        }
    }
}