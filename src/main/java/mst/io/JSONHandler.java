package mst.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mst.model.Edge;
import mst.model.Graph;
import mst.model.MSTResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class JSONHandler {
    private final ObjectMapper mapper;

    public JSONHandler() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public List<Graph> readGraphs(String filePath) throws IOException {
        List<Graph> graphs = new ArrayList<>();
        JsonNode root = mapper.readTree(new File(filePath));

        JsonNode graphsNode = root.get("graphs");
        if (graphsNode != null && graphsNode.isArray()) {
            for (JsonNode graphNode : graphsNode) {
                Graph graph = parseGraph(graphNode);
                graphs.add(graph);
            }
        }

        return graphs;
    }

    private Graph parseGraph(JsonNode graphNode) {
        int id = graphNode.has("id") ? graphNode.get("id").asInt() : 0;

        // Read node names
        List<String> nodeNames = new ArrayList<>();
        JsonNode nodesNode = graphNode.get("nodes");
        if (nodesNode != null && nodesNode.isArray()) {
            for (JsonNode nodeNode : nodesNode) {
                nodeNames.add(nodeNode.asText());
            }
        }

        Graph graph = new Graph(nodeNames);
        graph.setGraphId(id);

        // Set name if provided, otherwise use default
        String name = graphNode.has("name") ? graphNode.get("name").asText() : "Graph " + id;
        graph.setName(name);

        // Read edges
        JsonNode edgesNode = graphNode.get("edges");
        if (edgesNode != null && edgesNode.isArray()) {
            for (JsonNode edgeNode : edgesNode) {
                String from = edgeNode.get("from").asText();
                String to = edgeNode.get("to").asText();
                double weight = edgeNode.get("weight").asDouble();
                graph.addEdge(from, to, weight);
            }
        }

        return graph;
    }

    /**
     * Writes MST results to a JSON file matching the output format.
     */
    public void writeResults(String filePath, List<ResultPair> results) throws IOException {
        ObjectNode root = mapper.createObjectNode();
        ArrayNode resultsArray = mapper.createArrayNode();

        for (ResultPair pair : results) {
            ObjectNode resultNode = mapper.createObjectNode();
            resultNode.put("graph_id", pair.graphId);

            // Input stats
            ObjectNode statsNode = mapper.createObjectNode();
            statsNode.put("vertices", pair.vertexCount);
            statsNode.put("edges", pair.edgeCount);
            resultNode.set("input_stats", statsNode);

            // Prim's results
            ObjectNode primNode = createAlgorithmResultNode(pair.primResult);
            resultNode.set("prim", primNode);

            // Kruskal's results
            ObjectNode kruskalNode = createAlgorithmResultNode(pair.kruskalResult);
            resultNode.set("kruskal", kruskalNode);

            resultsArray.add(resultNode);
        }

        root.set("results", resultsArray);
        mapper.writeValue(new File(filePath), root);
    }

    private ObjectNode createAlgorithmResultNode(MSTResult result) {
        ObjectNode node = mapper.createObjectNode();

        if (!result.isSuccess()) {
            node.put("success", false);
            node.put("message", result.getMessage());
            return node;
        }

        // MST edges array
        ArrayNode edgesArray = mapper.createArrayNode();
        for (Edge edge : result.getMstEdges()) {
            ObjectNode edgeNode = mapper.createObjectNode();
            edgeNode.put("from", edge.getSourceName());
            edgeNode.put("to", edge.getDestinationName());
            edgeNode.put("weight", edge.getWeight());
            edgesArray.add(edgeNode);
        }
        node.set("mst_edges", edgesArray);

        node.put("total_cost", Math.round(result.getTotalCost()));
        node.put("operations_count", result.getOperationCount());
        node.put("execution_time_ms", result.getExecutionTimeMs());

        return node;
    }

    /**
     * Helper class to pair Prim and Kruskal results for the same graph.
     */
    public static class ResultPair {
        public int graphId;
        public String graphName;
        public int vertexCount;
        public int edgeCount;
        public MSTResult primResult;
        public MSTResult kruskalResult;

        public ResultPair(int graphId, String graphName, int vertexCount, int edgeCount,
                          MSTResult primResult, MSTResult kruskalResult) {
            this.graphId = graphId;
            this.graphName = graphName;
            this.vertexCount = vertexCount;
            this.edgeCount = edgeCount;
            this.primResult = primResult;
            this.kruskalResult = kruskalResult;
        }
    }
}