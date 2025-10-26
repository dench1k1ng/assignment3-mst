package mst;

import mst.model.Graph;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


class GraphTest {

    @Test
    @DisplayName("Test graph creation with named nodes")
    void testGraphCreation() {
        Graph graph = new Graph(Arrays.asList("A", "B", "C"));
        assertEquals(3, graph.getVertices());
        assertEquals(3, graph.getNodeNames().size());
        assertTrue(graph.getNodeNames().contains("A"));
        assertTrue(graph.getNodeNames().contains("B"));
        assertTrue(graph.getNodeNames().contains("C"));
    }

    @Test
    @DisplayName("Test graph creation with numeric vertices")
    void testGraphCreationNumeric() {
        Graph graph = new Graph(5);
        assertEquals(5, graph.getVertices());
        assertEquals(0, graph.getEdges().size());
    }

    @Test
    @DisplayName("Test adding edges with node names")
    void testAddEdgeWithNames() {
        Graph graph = new Graph(Arrays.asList("A", "B", "C"));
        graph.addEdge("A", "B", 5.0);
        graph.addEdge("B", "C", 3.0);

        assertEquals(2, graph.getEdges().size());
    }

    @Test
    @DisplayName("Test adding edges with indices")
    void testAddEdgeWithIndices() {
        Graph graph = new Graph(4);
        graph.addEdge(0, 1, 2.5);
        graph.addEdge(1, 2, 4.0);
        graph.addEdge(2, 3, 1.5);

        assertEquals(3, graph.getEdges().size());
    }

    @Test
    @DisplayName("Test connectivity check - connected graph")
    void testConnectedGraph() {
        Graph graph = new Graph(Arrays.asList("A", "B", "C", "D"));
        graph.addEdge("A", "B", 1.0);
        graph.addEdge("B", "C", 2.0);
        graph.addEdge("C", "D", 3.0);

        assertTrue(graph.isConnected());
    }

    @Test
    @DisplayName("Test connectivity check - disconnected graph")
    void testDisconnectedGraph() {
        Graph graph = new Graph(Arrays.asList("A", "B", "C", "D"));
        graph.addEdge("A", "B", 1.0);
        graph.addEdge("C", "D", 2.0);
        // A-B and C-D are separate components

        assertFalse(graph.isConnected());
    }

    @Test
    @DisplayName("Test single vertex graph is connected")
    void testSingleVertexConnected() {
        Graph graph = new Graph(Arrays.asList("A"));
        assertTrue(graph.isConnected());
    }

    @Test
    @DisplayName("Test cycle detection - graph with cycle")
    void testCycleDetection() {
        Graph graph = new Graph(Arrays.asList("A", "B", "C"));
        graph.addEdge("A", "B", 1.0);
        graph.addEdge("B", "C", 2.0);
        graph.addEdge("C", "A", 3.0);

        assertTrue(graph.hasCycle());
    }

    @Test
    @DisplayName("Test cycle detection - tree (no cycle)")
    void testNoCycle() {
        Graph graph = new Graph(Arrays.asList("A", "B", "C", "D"));
        graph.addEdge("A", "B", 1.0);
        graph.addEdge("B", "C", 2.0);
        graph.addEdge("B", "D", 3.0);

        assertFalse(graph.hasCycle());
    }

    @Test
    @DisplayName("Test node name to index mapping")
    void testNodeNameMapping() {
        Graph graph = new Graph(Arrays.asList("Downtown", "Uptown", "Midtown"));

        assertEquals(0, graph.getNodeIndex("Downtown"));
        assertEquals(1, graph.getNodeIndex("Uptown"));
        assertEquals(2, graph.getNodeIndex("Midtown"));

        assertEquals("Downtown", graph.getNodeName(0));
        assertEquals("Uptown", graph.getNodeName(1));
        assertEquals("Midtown", graph.getNodeName(2));
    }

    @Test
    @DisplayName("Test invalid edge addition throws exception")
    void testInvalidEdge() {
        Graph graph = new Graph(Arrays.asList("A", "B"));

        assertThrows(IllegalArgumentException.class, () -> {
            graph.addEdge("A", "C", 1.0); // C doesn't exist
        });
    }

    @Test
    @DisplayName("Test adjacency list structure")
    void testAdjacencyList() {
        Graph graph = new Graph(Arrays.asList("A", "B", "C"));
        graph.addEdge("A", "B", 5.0);
        graph.addEdge("B", "C", 3.0);

        // Each undirected edge appears twice in adjacency list
        assertEquals(1, graph.getAdjacencyList().get(0).size()); // A has 1 neighbor
        assertEquals(2, graph.getAdjacencyList().get(1).size()); // B has 2 neighbors
        assertEquals(1, graph.getAdjacencyList().get(2).size()); // C has 1 neighbor
    }

    @Test
    @DisplayName("Test graph with duplicate edges")
    void testDuplicateEdges() {
        Graph graph = new Graph(Arrays.asList("A", "B"));
        graph.addEdge("A", "B", 5.0);
        graph.addEdge("A", "B", 5.0); // Duplicate

        // Both edges are added (implementation allows duplicates)
        assertEquals(2, graph.getEdges().size());
    }

    @Test
    @DisplayName("Test graph ID and name setters")
    void testGraphMetadata() {
        Graph graph = new Graph(Arrays.asList("A", "B"));

        graph.setGraphId(42);
        graph.setName("Test Graph");

        assertEquals(42, graph.getGraphId());
        assertEquals("Test Graph", graph.getName());
    }

    @Test
    @DisplayName("Test empty graph connectivity")
    void testEmptyGraphConnected() {
        Graph graph = new Graph(0);
        assertTrue(graph.isConnected());
    }

    @Test
    @DisplayName("Test graph toString method")
    void testGraphToString() {
        Graph graph = new Graph(Arrays.asList("A", "B", "C"));
        graph.setName("Test Graph");
        graph.addEdge("A", "B", 5.0);

        String result = graph.toString();
        assertTrue(result.contains("Test Graph"));
        assertTrue(result.contains("Vertices: 3"));
        assertTrue(result.contains("Edges: 1"));
    }
}
