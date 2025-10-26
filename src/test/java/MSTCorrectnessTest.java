import mst.algorithm.KruskalAlgorithm;
import mst.algorithm.PrimAlgorithm;
import mst.model.Edge;
import mst.model.Graph;
import mst.model.MSTResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Correctness tests for MST algorithms.
 */
class MSTCorrectnessTest {

    private PrimAlgorithm primAlgorithm;
    private KruskalAlgorithm kruskalAlgorithm;

    @BeforeEach
    void setUp() {
        primAlgorithm = new PrimAlgorithm();
        kruskalAlgorithm = new KruskalAlgorithm();
    }

    @Test
    @DisplayName("Test 1: Small connected graph - Basic correctness")
    void testSmallConnectedGraph() {
        Graph graph = new Graph(Arrays.asList("A", "B", "C", "D"));
        graph.addEdge("A", "B", 1);
        graph.addEdge("A", "C", 4);
        graph.addEdge("B", "C", 2);
        graph.addEdge("C", "D", 3);
        graph.addEdge("B", "D", 5);

        MSTResult primResult = primAlgorithm.findMST(graph);
        MSTResult kruskalResult = kruskalAlgorithm.findMST(graph);

        // Both should succeed
        assertTrue(primResult.isSuccess());
        assertTrue(kruskalResult.isSuccess());

        // Total costs should be identical
        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost(), 0.001);

        // Expected cost: 1 + 2 + 3 = 6
        assertEquals(6.0, primResult.getTotalCost(), 0.001);

        // Number of edges should be V-1
        assertEquals(3, primResult.getMstEdges().size());
        assertEquals(3, kruskalResult.getMstEdges().size());
    }

    @Test
    @DisplayName("Test 2: MST has exactly V-1 edges")
    void testMSTEdgeCount() {
        Graph graph = new Graph(Arrays.asList("A", "B", "C", "D", "E"));
        graph.addEdge("A", "B", 4);
        graph.addEdge("A", "C", 3);
        graph.addEdge("B", "C", 2);
        graph.addEdge("B", "D", 5);
        graph.addEdge("C", "D", 7);
        graph.addEdge("C", "E", 8);
        graph.addEdge("D", "E", 6);

        MSTResult primResult = primAlgorithm.findMST(graph);
        MSTResult kruskalResult = kruskalAlgorithm.findMST(graph);

        assertEquals(4, primResult.getMstEdges().size()); // V-1 = 5-1 = 4
        assertEquals(4, kruskalResult.getMstEdges().size());
    }

    @Test
    @DisplayName("Test 3: MST is acyclic")
    void testMSTAcyclic() {
        Graph graph = new Graph(Arrays.asList("A", "B", "C", "D"));
        graph.addEdge("A", "B", 1);
        graph.addEdge("B", "C", 2);
        graph.addEdge("C", "D", 3);
        graph.addEdge("D", "A", 4);

        MSTResult primResult = primAlgorithm.findMST(graph);
        MSTResult kruskalResult = kruskalAlgorithm.findMST(graph);

        // Create graphs from MST edges and check for cycles
        assertFalse(hasCycle(primResult.getMstEdges(), graph.getVertices()));
        assertFalse(hasCycle(kruskalResult.getMstEdges(), graph.getVertices()));
    }

    @Test
    @DisplayName("Test 4: MST connects all vertices")
    void testMSTConnectsAll() {
        Graph graph = new Graph(Arrays.asList("A", "B", "C", "D", "E"));
        graph.addEdge("A", "B", 1);
        graph.addEdge("B", "C", 2);
        graph.addEdge("C", "D", 3);
        graph.addEdge("D", "E", 4);

        MSTResult primResult = primAlgorithm.findMST(graph);
        MSTResult kruskalResult = kruskalAlgorithm.findMST(graph);

        assertTrue(connectsAllVertices(primResult.getMstEdges(), graph.getVertices()));
        assertTrue(connectsAllVertices(kruskalResult.getMstEdges(), graph.getVertices()));
    }

    @Test
    @DisplayName("Test 5: Disconnected graph handling")
    void testDisconnectedGraph() {
        Graph graph = new Graph(Arrays.asList("A", "B", "C", "D"));
        graph.addEdge("A", "B", 1);
        graph.addEdge("C", "D", 2);
        // A-B and C-D are disconnected

        MSTResult primResult = primAlgorithm.findMST(graph);
        MSTResult kruskalResult = kruskalAlgorithm.findMST(graph);

        assertFalse(primResult.isSuccess());
        assertFalse(kruskalResult.isSuccess());
        assertNotNull(primResult.getMessage());
        assertNotNull(kruskalResult.getMessage());
    }

    @Test
    @DisplayName("Test 6: Single vertex graph")
    void testSingleVertex() {
        Graph graph = new Graph(Arrays.asList("A"));

        MSTResult primResult = primAlgorithm.findMST(graph);
        MSTResult kruskalResult = kruskalAlgorithm.findMST(graph);

        assertTrue(primResult.isSuccess());
        assertTrue(kruskalResult.isSuccess());
        assertEquals(0, primResult.getMstEdges().size());
        assertEquals(0, kruskalResult.getMstEdges().size());
        assertEquals(0.0, primResult.getTotalCost());
    }

    @Test
    @DisplayName("Test 7: Graph with duplicate edge weights")
    void testDuplicateWeights() {
        Graph graph = new Graph(Arrays.asList("A", "B", "C", "D"));
        graph.addEdge("A", "B", 5);
        graph.addEdge("B", "C", 5);
        graph.addEdge("C", "D", 5);
        graph.addEdge("D", "A", 5);

        MSTResult primResult = primAlgorithm.findMST(graph);
        MSTResult kruskalResult = kruskalAlgorithm.findMST(graph);

        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost(), 0.001);
        assertEquals(15.0, primResult.getTotalCost(), 0.001); // 3 edges * 5
    }

    @Test
    @DisplayName("Test 8: Complete graph (all vertices connected)")
    void testCompleteGraph() {
        Graph graph = new Graph(Arrays.asList("A", "B", "C", "D"));
        graph.addEdge("A", "B", 1);
        graph.addEdge("A", "C", 2);
        graph.addEdge("A", "D", 3);
        graph.addEdge("B", "C", 4);
        graph.addEdge("B", "D", 5);
        graph.addEdge("C", "D", 6);

        MSTResult primResult = primAlgorithm.findMST(graph);
        MSTResult kruskalResult = kruskalAlgorithm.findMST(graph);

        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost(), 0.001);
        assertEquals(3, primResult.getMstEdges().size()); // V-1
        assertEquals(6.0, primResult.getTotalCost(), 0.001); // 1 + 2 + 3
    }

    // Helper method to check if edges form a cycle
    private boolean hasCycle(List<Edge> edges, int vertices) {
        Graph.UnionFind uf = new Graph.UnionFind(vertices);

        for (Edge edge : edges) {
            int parent1 = uf.find(edge.getSource());
            int parent2 = uf.find(edge.getDestination());

            if (parent1 == parent2) {
                return true;
            }

            uf.union(edge.getSource(), edge.getDestination());
        }

        return false;
    }

    // Helper method to check if edges connect all vertices
    private boolean connectsAllVertices(List<Edge> edges, int vertices) {
        Graph.UnionFind uf = new Graph.UnionFind(vertices);

        for (Edge edge : edges) {
            uf.union(edge.getSource(), edge.getDestination());
        }

        // Check if all vertices are in the same component
        int root = uf.find(0);
        for (int i = 1; i < vertices; i++) {
            if (uf.find(i) != root) {
                return false;
            }
        }

        return true;
    }
}
