package mst.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to generate CSV summary from JSON results for analysis
 */
public class ResultAnalyzer {

    public static class ResultSummary {
        public int graphId;
        public String graphName;
        public int vertices;
        public int edges;
        public double density;
        public double primCost;
        public int primOperations;
        public long primTime;
        public double kruskalCost;
        public int kruskalOperations;
        public long kruskalTime;
        public boolean costMatch;

        @Override
        public String toString() {
            return String.format("%d,%s,%d,%d,%.3f,%.1f,%d,%d,%.1f,%d,%d,%s",
                    graphId, graphName, vertices, edges, density,
                    primCost, primOperations, primTime,
                    kruskalCost, kruskalOperations, kruskalTime,
                    costMatch ? "YES" : "NO");
        }
    }

    public static void generateCSVReport(String jsonFile, String csvFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File(jsonFile));
        JsonNode results = root.get("results");

        List<ResultSummary> summaries = new ArrayList<>();

        for (JsonNode result : results) {
            ResultSummary summary = new ResultSummary();
            summary.graphId = result.get("graph_id").asInt();

            // Get graph name from input.json mapping
            summary.graphName = getGraphName(summary.graphId);

            JsonNode inputStats = result.get("input_stats");
            summary.vertices = inputStats.get("vertices").asInt();
            summary.edges = inputStats.get("edges").asInt();

            // Calculate graph density: 2E / (V * (V-1))
            if (summary.vertices > 1) {
                summary.density = (2.0 * summary.edges) / (summary.vertices * (summary.vertices - 1));
            } else {
                summary.density = 0.0;
            }

            JsonNode prim = result.get("prim");
            summary.primCost = prim.get("total_cost").asDouble();
            summary.primOperations = prim.get("operations_count").asInt();
            summary.primTime = prim.get("execution_time_ms").asLong();

            JsonNode kruskal = result.get("kruskal");
            summary.kruskalCost = kruskal.get("total_cost").asDouble();
            summary.kruskalOperations = kruskal.get("operations_count").asInt();
            summary.kruskalTime = kruskal.get("execution_time_ms").asLong();

            summary.costMatch = Math.abs(summary.primCost - summary.kruskalCost) < 0.001;

            summaries.add(summary);
        }

        // Write CSV file
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile))) {
            writer.println(
                    "Graph_ID,Graph_Name,Vertices,Edges,Density,Prim_Cost,Prim_Ops,Prim_Time_ms,Kruskal_Cost,Kruskal_Ops,Kruskal_Time_ms,Cost_Match");
            for (ResultSummary summary : summaries) {
                writer.println(summary);
            }
        }

        System.out.println("CSV report generated: " + csvFile);
    }

    private static String getGraphName(int graphId) {
        switch (graphId) {
            case 1:
                return "Small_Simple_Path";
            case 2:
                return "Small_Pentagon";
            case 3:
                return "Small_Complete_K4";
            case 4:
                return "Medium_City_Districts";
            case 5:
                return "Medium_Dense_Network";
            case 6:
                return "Large_Metropolitan";
            case 7:
                return "Large_Sparse_Network";
            case 8:
                return "Large_Dense_Network";
            case 9:
                return "Edge_Case_Single_Vertex";
            case 10:
                return "Edge_Case_Two_Vertices";
            default:
                return "Unknown_Graph_" + graphId;
        }
    }

    public static void main(String[] args) {
        try {
            generateCSVReport("src/main/resources/output.json", "analysis_results.csv");
        } catch (IOException e) {
            System.err.println("Error generating CSV report: " + e.getMessage());
        }
    }
}
