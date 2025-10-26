package mst.model;

import java.util.List;

public class MSTResult {
    private final String algorithmName;
    private final List<Edge> mstEdges;
    private final double totalCost;
    private final int vertexCount;
    private final int edgeCount;
    private final long operationCount;
    private final long executionTimeMs;
    private final boolean success;
    private final String message;

    private MSTResult(Builder builder) {
        this.algorithmName = builder.algorithmName;
        this.mstEdges = builder.mstEdges;
        this.totalCost = builder.totalCost;
        this.vertexCount = builder.vertexCount;
        this.edgeCount = builder.edgeCount;
        this.operationCount = builder.operationCount;
        this.executionTimeMs = builder.executionTimeMs;
        this.success = builder.success;
        this.message = builder.message;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public List<Edge> getMstEdges() {
        return mstEdges;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getEdgeCount() {
        return edgeCount;
    }

    public long getOperationCount() {
        return operationCount;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        if (!success) {
            return String.format("%s: %s", algorithmName, message);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("=== %s Results ===\n", algorithmName));
        sb.append(String.format("Total Cost: %.2f\n", totalCost));
        sb.append(String.format("Vertices: %d, MST Edges: %d\n", vertexCount, mstEdges.size()));
        sb.append(String.format("Operations: %d\n", operationCount));
        sb.append(String.format("Execution Time: %d ms\n", executionTimeMs));
        sb.append("MST Edges:\n");
        for (Edge edge : mstEdges) {
            sb.append(String.format("  %s\n", edge));
        }
        return sb.toString();
    }

    public static class Builder {
        private String algorithmName;
        private List<Edge> mstEdges;
        private double totalCost;
        private int vertexCount;
        private int edgeCount;
        private long operationCount;
        private long executionTimeMs;
        private boolean success = true;
        private String message = "";

        public Builder algorithmName(String algorithmName) {
            this.algorithmName = algorithmName;
            return this;
        }

        public Builder mstEdges(List<Edge> mstEdges) {
            this.mstEdges = mstEdges;
            return this;
        }

        public Builder totalCost(double totalCost) {
            this.totalCost = totalCost;
            return this;
        }

        public Builder vertexCount(int vertexCount) {
            this.vertexCount = vertexCount;
            return this;
        }

        public Builder edgeCount(int edgeCount) {
            this.edgeCount = edgeCount;
            return this;
        }

        public Builder operationCount(long operationCount) {
            this.operationCount = operationCount;
            return this;
        }

        public Builder executionTimeMs(long executionTimeMs) {
            this.executionTimeMs = executionTimeMs;
            return this;
        }

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public MSTResult build() {
            return new MSTResult(this);
        }
    }
}