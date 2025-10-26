package mst;

import mst.algorithm.KruskalAlgorithm;
import mst.algorithm.PrimAlgorithm;
import mst.model.Graph;
import mst.model.MSTResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MSTPerformanceTest {

    private PrimAlgorithm primAlgorithm;
    private KruskalAlgorithm kruskalAlgorithm;
    private Random random;

    @BeforeEach
    void setUp() {
        primAlgorithm = new PrimAlgorithm();
        kruskalAlgorithm = new KruskalAlgorithm();
        random = new Random(42); // Fixed seed for reproducibility
    }

    @Test
    @DisplayName("Performance Test: Small graphs (4-6 vertices)")
    void testSmallGraphPerformance() {
        for (int vertices = 4; vertices <= 6; vertices++) {
            Graph graph = generateRandomConnectedGraph(vertices, vertices * 2);

            MSTResult primResult = primAlgorithm.findMST(graph);
            MSTResult kruskalResult = kruskalAlgorithm.findMST(graph);

            assertTrue(primResult.isSuccess());
            assertTrue(kruskalResult.isSuccess());
            assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost(), 0.001);
            assertTrue(primResult.getExecutionTimeMs() >= 0);
            assertTrue(kruskalResult.getExecutionTimeMs() >= 0);

            System.out.printf("Small graph (%d vertices): Prim=%dms, Kruskal=%dms%n",
                    vertices, primResult.getExecutionTimeMs(), kruskalResult.getExecutionTimeMs());
        }
    }

    @Test
    @DisplayName("Performance Test: Medium graphs (10-15 vertices)")
    void testMediumGraphPerformance() {
        for (int vertices = 10; vertices <= 15; vertices += 2) {
            Graph graph = generateRandomConnectedGraph(vertices, vertices * 3);

            MSTResult primResult = primAlgorithm.findMST(graph);
            MSTResult kruskalResult = kruskalAlgorithm.findMST(graph);

            assertTrue(primResult.isSuccess());
            assertTrue(kruskalResult.isSuccess());
            assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost(), 0.001);

            System.out.printf("Medium graph (%d vertices): Prim=%dms (%d ops), Kruskal=%dms (%d ops)%n",
                    vertices, primResult.getExecutionTimeMs(), primResult.getOperationCount(),
                    kruskalResult.getExecutionTimeMs(), kruskalResult.getOperationCount());
        }
    }

    @Test
    @DisplayName("Performance Test: Large graphs (20-30+ vertices)")
    void testLargeGraphPerformance() {
        int[] sizes = { 20, 25, 30, 35 };

        for (int vertices : sizes) {
            Graph graph = generateRandomConnectedGraph(vertices, vertices * 4);

            MSTResult primResult = primAlgorithm.findMST(graph);
            MSTResult kruskalResult = kruskalAlgorithm.findMST(graph);

            assertTrue(primResult.isSuccess());
            assertTrue(kruskalResult.isSuccess());
            assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost(), 0.001);

            System.out.printf("Large graph (%d vertices, %d edges): Prim=%dms (%d ops), Kruskal=%dms (%d ops)%n",
                    vertices, graph.getEdges().size(),
                    primResult.getExecutionTimeMs(), primResult.getOperationCount(),
                    kruskalResult.getExecutionTimeMs(), kruskalResult.getOperationCount());
        }
    }

    @Test
    @DisplayName("Performance Test: Dense vs Sparse graphs")
    void testDenseVsSparseGraphs() {
        int vertices = 20;

        // Sparse graph: ~V edges
        Graph sparseGraph = generateRandomConnectedGraph(vertices, vertices + 5);
        MSTResult primSparse = primAlgorithm.findMST(sparseGraph);
        MSTResult kruskalSparse = kruskalAlgorithm.findMST(sparseGraph);

        System.out.println("\n--- Sparse Graph (20 vertices, ~25 edges) ---");
        System.out.printf("Prim: %dms, %d operations%n",
                primSparse.getExecutionTimeMs(), primSparse.getOperationCount());
        System.out.printf("Kruskal: %dms, %d operations%n",
                kruskalSparse.getExecutionTimeMs(), kruskalSparse.getOperationCount());

        // Dense graph: ~V² edges
        Graph denseGraph = generateRandomConnectedGraph(vertices, vertices * (vertices - 1) / 3);
        MSTResult primDense = primAlgorithm.findMST(denseGraph);
        MSTResult kruskalDense = kruskalAlgorithm.findMST(denseGraph);

        System.out.println("\n--- Dense Graph (20 vertices, ~120 edges) ---");
        System.out.printf("Prim: %dms, %d operations%n",
                primDense.getExecutionTimeMs(), primDense.getOperationCount());
        System.out.printf("Kruskal: %dms, %d operations%n",
                kruskalDense.getExecutionTimeMs(), kruskalDense.getOperationCount());
    }

    @Test
    @DisplayName("Consistency Test: Results are reproducible")
    void testReproducibility() {
        Graph graph = generateRandomConnectedGraph(15, 40);

        // Run multiple times
        MSTResult result1 = primAlgorithm.findMST(graph);
        MSTResult result2 = primAlgorithm.findMST(graph);
        MSTResult result3 = kruskalAlgorithm.findMST(graph);
        MSTResult result4 = kruskalAlgorithm.findMST(graph);

        // Costs should be identical
        assertEquals(result1.getTotalCost(), result2.getTotalCost(), 0.001);
        assertEquals(result3.getTotalCost(), result4.getTotalCost(), 0.001);
        assertEquals(result1.getTotalCost(), result3.getTotalCost(), 0.001);

        // Operation counts should be consistent
        assertEquals(result1.getOperationCount(), result2.getOperationCount());
        assertEquals(result3.getOperationCount(), result4.getOperationCount());
    }

    @Test
    @DisplayName("Consistency Test: Operation counts are positive")
    void testOperationCountsPositive() {
        Graph graph = generateRandomConnectedGraph(10, 25);

        MSTResult primResult = primAlgorithm.findMST(graph);
        MSTResult kruskalResult = kruskalAlgorithm.findMST(graph);

        assertTrue(primResult.getOperationCount() > 0);
        assertTrue(kruskalResult.getOperationCount() > 0);
        assertTrue(primResult.getExecutionTimeMs() >= 0);
        assertTrue(kruskalResult.getExecutionTimeMs() >= 0);
    }

    @Test
    @DisplayName("Stress Test: Very large graph")
    void testVeryLargeGraph() {
        int vertices = 50;
        int edges = vertices * 5;

        Graph graph = generateRandomConnectedGraph(vertices, edges);

        long startPrim = System.currentTimeMillis();
        MSTResult primResult = primAlgorithm.findMST(graph);
        long primTime = System.currentTimeMillis() - startPrim;

        long startKruskal = System.currentTimeMillis();
        MSTResult kruskalResult = kruskalAlgorithm.findMST(graph);
        long kruskalTime = System.currentTimeMillis() - startKruskal;

        assertTrue(primResult.isSuccess());
        assertTrue(kruskalResult.isSuccess());
        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost(), 0.001);

        // Should complete in reasonable time (< 1 second)
        assertTrue(primTime < 1000, "Prim's algorithm took too long");
        assertTrue(kruskalTime < 1000, "Kruskal's algorithm took too long");

        System.out.printf("\nStress test (50 vertices, 250 edges):%n");
        System.out.printf("Prim: %dms, Kruskal: %dms%n", primTime, kruskalTime);
    }

    /**
     * Generates a random connected graph with specified vertices and edges.
     */
    private Graph generateRandomConnectedGraph(int vertices, int targetEdges) {
        List<String> nodeNames = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            nodeNames.add("N" + i);
        }

        Graph graph = new Graph(nodeNames);

        // First, create a spanning tree to ensure connectivity
        for (int i = 1; i < vertices; i++) {
            int parent = random.nextInt(i);
            double weight = 1 + random.nextDouble() * 99; // Weight between 1 and 100
            graph.addEdge("N" + parent, "N" + i, weight);
        }

        // Add additional random edges
        int currentEdges = vertices - 1;
        int maxPossibleEdges = vertices * (vertices - 1) / 2;
        int edgesToAdd = Math.min(targetEdges - currentEdges, maxPossibleEdges - currentEdges);

        int attempts = 0;
        while (currentEdges < targetEdges && attempts < edgesToAdd * 3) {
            int u = random.nextInt(vertices);
            int v = random.nextInt(vertices);

            if (u != v) {
                // Check if edge already exists
                boolean exists = false;
                for (var edge : graph.getEdges()) {
                    if ((edge.getSource() == u && edge.getDestination() == v) ||
                            (edge.getSource() == v && edge.getDestination() == u)) {
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    double weight = 1 + random.nextDouble() * 99;
                    graph.addEdge("N" + u, "N" + v, weight);
                    currentEdges++;
                }
            }
            attempts++;
        }

        return graph;
    }
}
