package mst;

import mst.algorithm.PrimAlgorithm;
import mst.io.JSONHandler;
import mst.model.Graph;
import mst.model.MSTResult;
import mst.algorithm.KruskalAlgorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Main application class for MST Transportation Network optimization.
 */
public class Main {

    public static void main(String[] args) {
        String inputFile = "src/main/resources/input.json";
        String outputFile = "src/main/resources/output.json";

        // Allow command line arguments
        if (args.length >= 1) {
            inputFile = args[0];
        }
        if (args.length >= 2) {
            outputFile = args[1];
        }

        try {
            System.out.println("=== MST Transportation Network Optimizer ===\n");

            // Read graphs from JSON
            JSONHandler jsonHandler = new JSONHandler();
            List<Graph> graphs = jsonHandler.readGraphs(inputFile);
            System.out.println("Loaded " + graphs.size() + " graph(s) from " + inputFile + "\n");

            // Initialize algorithms
            PrimAlgorithm primAlgorithm = new PrimAlgorithm();
            KruskalAlgorithm kruskalAlgorithm = new KruskalAlgorithm();

            // Process each graph
            List<JSONHandler.ResultPair> results = new ArrayList<>();

            for (Graph graph : graphs) {
                System.out.println("Processing " + graph.getName() + " (ID: " + graph.getGraphId() + ")");
                System.out.println("Vertices: " + graph.getVertices() + ", Edges: " + graph.getEdges().size());
                System.out.println("Nodes: " + graph.getNodeNames());

                // Check if graph is connected
                if (!graph.isConnected()) {
                    System.out.println("WARNING: Graph is not connected!\n");
                }

                // Run Prim's algorithm
                System.out.println("\nRunning Prim's Algorithm...");
                MSTResult primResult = primAlgorithm.findMST(graph);
                printResult(primResult);

                // Run Kruskal's algorithm
                System.out.println("\nRunning Kruskal's Algorithm...");
                MSTResult kruskalResult = kruskalAlgorithm.findMST(graph);
                printResult(kruskalResult);

                // Compare results
                if (primResult.isSuccess() && kruskalResult.isSuccess()) {
                    System.out.println("\n--- Comparison ---");
                    System.out.println("Cost Match: " +
                            (Math.abs(primResult.getTotalCost() - kruskalResult.getTotalCost()) < 0.001 ? "YES" : "NO"));
                    System.out.println("Prim Time: " + primResult.getExecutionTimeMs() + " ms");
                    System.out.println("Kruskal Time: " + kruskalResult.getExecutionTimeMs() + " ms");
                    System.out.println("Prim Operations: " + primResult.getOperationCount());
                    System.out.println("Kruskal Operations: " + kruskalResult.getOperationCount());
                }

                // Store results
                results.add(new JSONHandler.ResultPair(
                        graph.getGraphId(),
                        graph.getName(),
                        graph.getVertices(),
                        graph.getEdges().size(),
                        primResult,
                        kruskalResult
                ));

                System.out.println("\n" + "=".repeat(60) + "\n");
            }

            // Write results to JSON
            jsonHandler.writeResults(outputFile, results);
            System.out.println("Results written to " + outputFile);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printResult(MSTResult result) {
        if (!result.isSuccess()) {
            System.out.println("Failed: " + result.getMessage());
            return;
        }

        System.out.println("Total Cost: " + result.getTotalCost());
        System.out.println("MST Edges: " + result.getMstEdges().size());
        System.out.println("Operations: " + result.getOperationCount());
        System.out.println("Execution Time: " + result.getExecutionTimeMs() + " ms");
        System.out.println("Edges in MST:");
        for (var edge : result.getMstEdges()) {
            System.out.println("  " + edge.toStringWithNames());
        }
    }
}