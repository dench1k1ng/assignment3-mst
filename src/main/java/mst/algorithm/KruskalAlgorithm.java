package mst.algorithm;

import mst.model.Edge;
import mst.model.Graph;
import mst.model.MSTResult;

import java.util.*;

public class KruskalAlgorithm {

    public MSTResult findMST(Graph graph) {
        long startTime = System.currentTimeMillis();
        long operationCount = 0;

        int vertices = graph.getVertices();

        // Check if graph is connected
        if (!graph.isConnected()) {
            return new MSTResult.Builder()
                    .algorithmName("Kruskal's Algorithm")
                    .success(false)
                    .message("Graph is not connected - MST cannot be formed")
                    .vertexCount(vertices)
                    .edgeCount(graph.getEdges().size())
                    .executionTimeMs(System.currentTimeMillis() - startTime)
                    .build();
        }

        List<Edge> mstEdges = new ArrayList<>();
        List<Edge> sortedEdges = new ArrayList<>(graph.getEdges());

        // Sort edges by weight
        Collections.sort(sortedEdges);
        operationCount += (long)(sortedEdges.size() * Math.log(sortedEdges.size())); // Sorting complexity

        UnionFind uf = new UnionFind(vertices);
        double totalCost = 0.0;

        // Process edges in sorted order
        for (Edge edge : sortedEdges) {
            operationCount++; // Edge examination

            int parent1 = uf.find(edge.getSource());
            int parent2 = uf.find(edge.getDestination());
            operationCount += 2; // Two find operations

            // If vertices are in different sets, add edge to MST
            if (parent1 != parent2) {
                operationCount++; // Comparison
                mstEdges.add(edge);
                totalCost += edge.getWeight();
                uf.union(edge.getSource(), edge.getDestination());
                operationCount++; // Union operation

                // Stop if we have V-1 edges
                if (mstEdges.size() == vertices - 1) {
                    break;
                }
            }
        }

        long executionTime = System.currentTimeMillis() - startTime;

        return new MSTResult.Builder()
                .algorithmName("Kruskal's Algorithm")
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
     * Union-Find (Disjoint Set) data structure with path compression and union by rank.
     */
    private static class UnionFind {
        private final int[] parent;
        private final int[] rank;

        UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX != rootY) {
                // Union by rank
                if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
        }
    }
}