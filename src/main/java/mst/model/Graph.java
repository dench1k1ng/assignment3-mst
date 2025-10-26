package mst.model;

import java.util.*;

public class Graph {
    private final int vertices;
    private final List<Edge> edges;
    private final Map<Integer, List<Edge>> adjacencyList;
    private String name;
    private int graphId;
    private final List<String> nodeNames;
    private final Map<String, Integer> nodeNameToIndex;

    public Graph(int vertices) {
        this.vertices = vertices;
        this.edges = new ArrayList<>();
        this.adjacencyList = new HashMap<>();
        this.nodeNames = new ArrayList<>();
        this.nodeNameToIndex = new HashMap<>();

        for (int i = 0; i < vertices; i++) {
            adjacencyList.put(i, new ArrayList<>());
        }
    }

    public Graph(List<String> nodeNames) {
        this.vertices = nodeNames.size();
        this.edges = new ArrayList<>();
        this.adjacencyList = new HashMap<>();
        this.nodeNames = new ArrayList<>(nodeNames);
        this.nodeNameToIndex = new HashMap<>();

        for (int i = 0; i < vertices; i++) {
            adjacencyList.put(i, new ArrayList<>());
            nodeNameToIndex.put(nodeNames.get(i), i);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setGraphId(int graphId) {
        this.graphId = graphId;
    }

    public int getGraphId() {
        return graphId;
    }

    public List<String> getNodeNames() {
        return new ArrayList<>(nodeNames);
    }

    public String getNodeName(int index) {
        if (index >= 0 && index < nodeNames.size()) {
            return nodeNames.get(index);
        }
        return String.valueOf(index);
    }

    public Integer getNodeIndex(String name) {
        return nodeNameToIndex.get(name);
    }

    public int getVertices() {
        return vertices;
    }

    public List<Edge> getEdges() {
        return new ArrayList<>(edges);
    }

    public Map<Integer, List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    /**
     * Adds an undirected edge to the graph using numeric indices.
     */
    public void addEdge(int source, int destination, double weight) {
        if (source < 0 || source >= vertices || destination < 0 || destination >= vertices) {
            throw new IllegalArgumentException("Invalid vertex index");
        }

        String srcName = getNodeName(source);
        String dstName = getNodeName(destination);
        Edge edge = new Edge(source, destination, weight, srcName, dstName);
        edges.add(edge);

        adjacencyList.get(source).add(edge);
        adjacencyList.get(destination).add(new Edge(destination, source, weight, dstName, srcName));
    }

    /**
     * Adds an undirected edge to the graph using node names.
     */
    public void addEdge(String sourceName, String destName, double weight) {
        Integer sourceIdx = nodeNameToIndex.get(sourceName);
        Integer destIdx = nodeNameToIndex.get(destName);

        if (sourceIdx == null || destIdx == null) {
            throw new IllegalArgumentException("Invalid node name: " + sourceName + " or " + destName);
        }

        Edge edge = new Edge(sourceIdx, destIdx, weight, sourceName, destName);
        edges.add(edge);

        adjacencyList.get(sourceIdx).add(edge);
        adjacencyList.get(destIdx).add(new Edge(destIdx, sourceIdx, weight, destName, sourceName));
    }

    /**
     * Checks if the graph is connected using BFS.
     */
    public boolean isConnected() {
        if (vertices == 0) return true;

        boolean[] visited = new boolean[vertices];
        Queue<Integer> queue = new LinkedList<>();

        queue.offer(0);
        visited[0] = true;
        int visitedCount = 1;

        while (!queue.isEmpty()) {
            int current = queue.poll();

            for (Edge edge : adjacencyList.get(current)) {
                int neighbor = edge.getDestination();
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                    visitedCount++;
                }
            }
        }

        return visitedCount == vertices;
    }

    /**
     * Checks if the graph contains cycles using Union-Find.
     */
    public boolean hasCycle() {
        UnionFind uf = new UnionFind(vertices);
        Set<String> processedEdges = new HashSet<>();

        for (Edge edge : edges) {
            String edgeKey = Math.min(edge.getSource(), edge.getDestination()) + "-" +
                    Math.max(edge.getSource(), edge.getDestination());

            if (processedEdges.contains(edgeKey)) continue;
            processedEdges.add(edgeKey);

            int parent1 = uf.find(edge.getSource());
            int parent2 = uf.find(edge.getDestination());

            if (parent1 == parent2) {
                return true;
            }

            uf.union(edge.getSource(), edge.getDestination());
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph: ").append(name != null ? name : "Unnamed").append("\n");
        sb.append("Vertices: ").append(vertices).append(", Edges: ").append(edges.size()).append("\n");
        sb.append("Edges:\n");

        Set<String> printed = new HashSet<>();
        for (Edge edge : edges) {
            String key = Math.min(edge.getSource(), edge.getDestination()) + "-" +
                    Math.max(edge.getSource(), edge.getDestination());
            if (!printed.contains(key)) {
                sb.append("  ").append(edge).append("\n");
                printed.add(key);
            }
        }

        return sb.toString();
    }

    /**
     * Helper class for Union-Find (Disjoint Set) operations.
     */
    public static class UnionFind {
        private final int[] parent;
        private final int[] rank;

        public UnionFind(int size) {
            parent = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // Path compression
            }
            return parent[x];
        }

        public void union(int x, int y) {
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