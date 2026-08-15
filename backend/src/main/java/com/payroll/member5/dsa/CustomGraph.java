package com.payroll.member5.dsa;

import java.util.*;

/**
 * ============================================================================
 * Member 05 Data Structure: Custom Generic Graph
 * ============================================================================
 */
public class CustomGraph<T> {

    private final Map<T, List<T>> adjacencyList;

    public CustomGraph() {
        this.adjacencyList = new HashMap<>();
    }

    public void addVertex(final T vertex) {
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    public void addEdge(final T source, final T destination) {
        addVertex(source);
        addVertex(destination);
        if (!adjacencyList.get(source).contains(destination)) {
            adjacencyList.get(source).add(destination);
        }
    }

    public List<T> getNeighbors(final T vertex) {
        return adjacencyList.getOrDefault(vertex, Collections.emptyList());
    }

    public Set<T> getVertices() {
        return adjacencyList.keySet();
    }

    public int getVertexCount() {
        return adjacencyList.size();
    }
}
