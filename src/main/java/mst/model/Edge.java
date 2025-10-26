package mst.model;

import java.util.Objects;

public class Edge implements Comparable<Edge> {
    private final int source;
    private final int destination;
    private final double weight;
    private final String sourceName;
    private final String destinationName;

    public Edge(int source, int destination, double weight) {
        this(source, destination, weight, null, null);
    }

    public Edge(int source, int destination, double weight, String sourceName, String destinationName) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
        this.sourceName = sourceName;
        this.destinationName = destinationName;
    }

    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    @Override
    public int compareTo(Edge other) {
        return Double.compare(this.weight, other.weight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return ((source == edge.source && destination == edge.destination) ||
                (source == edge.destination && destination == edge.source)) &&
                Double.compare(edge.weight, weight) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Math.min(source, destination),
                Math.max(source, destination), weight);
    }

    @Override
    public String toString() {
        if (sourceName != null && destinationName != null) {
            return String.format("(%s-%s, %.2f)", sourceName, destinationName, weight);
        }
        return String.format("(%d-%d, %.2f)", source, destination, weight);
    }

    public String toStringWithNames() {
        if (sourceName != null && destinationName != null) {
            return String.format("%s -> %s (%.2f)", sourceName, destinationName, weight);
        }
        return String.format("%d -> %d (%.2f)", source, destination, weight);
    }
}