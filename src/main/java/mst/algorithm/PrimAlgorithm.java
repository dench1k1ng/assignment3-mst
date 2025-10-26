package mst.algorithm;

import mst.model.Edge;
import mst.model.Graph;
import mst.model.MSTResult;

import java.util.*;


public class PrimAlgorithm {

    public MSTResult findMST(Graph graph) {
        long startTime = System.currentTimeMillis();
        long operationCount = 0;

        int vertices = graph.getVertices();

        // Check if graph is connected
        if (!graph.isConnected()) {
            return new MSTResult.Builder()
                    .algorithmName("Prim's Algorithm")
                    .success(false)
                    .message("Graph is not connected - MST cannot be formed")
                    .vertexCount(vertices)
                    .edgeCount(graph.getEdges().size())
                    .executionTimeMs(System.currentTimeMillis() - startTime)
                    .build();
        }

        List<Edge> mstEdges = new ArrayList<>();
        boolean[] inMST = new boolean[vertices];
        PriorityQueue<EdgeWithVertex> pq = new PriorityQueue<>();

        // Start from vertex 0
        inMST[0] = true;
        operationCount++; // Initial vertex selection

        // Add all edges from vertex 0 to priority queue
        for (Edge edge : graph.getAdjacencyList().get(0)) {
            pq.offer(new EdgeWithVertex(edge, edge.getDestination()));
            operationCount++; // Queue insertion
        }

        double totalCost = 0.0;

        // Continue until we have V-1 edges in MST
        while (!pq.isEmpty() && mstEdges.size() < vertices - 1) {
            EdgeWithVertex current = pq.poll();
            operationCount++; // Queue extraction

            int vertex = current.vertex;

            // Skip if vertex is already in MST
            if (inMST[vertex]) {
                operationCount++; // Comparison
                continue;
            }

            // Add edge to MST
            mstEdges.add(current.edge);
            totalCost += current.edge.getWeight();
            inMST[vertex] = true;
            operationCount++; // Edge addition

            // Add all edges from newly added vertex
            for (Edge edge : graph.getAdjacencyList().get(vertex)) {
                if (!inMST[edge.getDestination()]) {
                    pq.offer(new EdgeWithVertex(edge, edge.getDestination()));
                    operationCount++; // Queue insertion
                }
                operationCount++; // Comparison for each edge
            }
        }

        long executionTime = System.currentTimeMillis() - startTime;

        return new MSTResult.Builder()
                .algorithmName("Prim's Algorithm")
                .mstEdges(mstEdges)
                .totalCost(totalCost)
                .vertexCount(vertices)
                .edgeCount(graph.getEdges().size())
                .operationCount(operationCount)
                .executionTimeMs(executionTime)
                .success(true)
                .build();
    }

    /**
     * Helper class to store edge with its destination vertex for priority queue.
     */
    private static class EdgeWithVertex implements Comparable<EdgeWithVertex> {
        Edge edge;
        int vertex;

        EdgeWithVertex(Edge edge, int vertex) {
            this.edge = edge;
            this.vertex = vertex;
        }

        @Override
        public int compareTo(EdgeWithVertex other) {
            return Double.compare(this.edge.getWeight(), other.edge.getWeight());
        }
    }
}