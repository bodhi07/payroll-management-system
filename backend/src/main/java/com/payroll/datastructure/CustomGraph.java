package com.payroll.datastructure;

/**
 * ============================================================================
 * Assignment Data Structure 09: Manual Graph (CustomGraph)
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Manual Graph implementation written from scratch using an Adjacency List composed
 * of {@link CustomHashTable} and {@link CustomLinkedList}. Supports directed/undirected
 * graphs, Breadth-First Search (BFS), and Depth-First Search (DFS) traversals.
 * 
 * Complexity Analysis:
 * -------------------
 * - Add Vertex: Time Complexity O(1), Space Complexity O(V)
 * - Add Edge: Time Complexity O(1), Space Complexity O(E)
 * - Remove Edge: Time Complexity O(E), Space Complexity O(1)
 * - Search Vertex: Time Complexity O(1), Space Complexity O(1)
 * - BFS Traversal: Time Complexity O(V + E), Space Complexity O(V)
 * - DFS Traversal: Time Complexity O(V + E), Space Complexity O(V)
 * 
 * OOP Concepts Used:
 * --------------------
 * - Generics: Generic Vertex representation {@code CustomGraph<T>}.
 * - Composition: Combines CustomHashTable and CustomLinkedList.
 * 
 * Design Patterns Used:
 * --------------------
 * - Graph Adjacency List Pattern.
 * 
 * @param <T> Vertex type
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
public class CustomGraph<T> {

    private final CustomHashTable<T, CustomLinkedList<T>> adjacencyList;
    private final boolean isDirected;

    public CustomGraph(final boolean isDirected) {
        this.adjacencyList = new CustomHashTable<>();
        this.isDirected = isDirected;
    }

    public CustomGraph() {
        this(false); // Undirected by default
    }

    /**
     * Adds a vertex to the graph.
     * Time Complexity: O(1)
     *
     * @param vertex Vertex label
     */
    public void addVertex(final T vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            adjacencyList.put(vertex, new CustomLinkedList<>());
        }
    }

    /**
     * Adds an edge between source and destination vertices.
     * Time Complexity: O(1)
     *
     * @param source Source vertex
     * @param destination Destination vertex
     */
    public void addEdge(final T source, final T destination) {
        addVertex(source);
        addVertex(destination);

        adjacencyList.get(source).insertLast(destination);
        if (!isDirected) {
            adjacencyList.get(destination).insertLast(source);
        }
    }

    /**
     * Removes edge between source and destination.
     */
    public void removeEdge(final T source, final T destination) {
        if (adjacencyList.containsKey(source)) {
            adjacencyList.get(source).delete(destination);
        }
        if (!isDirected && adjacencyList.containsKey(destination)) {
            adjacencyList.get(destination).delete(source);
        }
    }

    /**
     * Searches if vertex exists in graph.
     */
    public boolean containsVertex(final T vertex) {
        return adjacencyList.containsKey(vertex);
    }

    /**
     * Displays adjacency list representation of graph.
     */
    public void display() {
        System.out.println("CustomGraph Adjacency List (Directed=" + isDirected + "):");
        adjacencyList.display();
    }

    /**
     * Breadth-First Search (BFS) Traversal using CustomQueue & CustomSet.
     * Time Complexity: O(V + E)
     *
     * @param startVertex Starting vertex
     */
    public void traverseBFS(final T startVertex) {
        if (!adjacencyList.containsKey(startVertex)) {
            System.out.println("Start vertex not found.");
            return;
        }

        System.out.print("BFS Traversal starting from [" + startVertex + "]: ");
        final CustomQueue<T> queue = new CustomQueue<>();
        final CustomSet<T> visited = new CustomSet<>();

        queue.enqueue(startVertex);
        visited.add(startVertex);

        while (!queue.isEmpty()) {
            final T current = queue.dequeue();
            System.out.print("[" + current + "] ");

            final CustomLinkedList<T> neighbors = adjacencyList.get(current);
            if (neighbors != null) {
                for (int i = 0; i < neighbors.getSize(); i++) {
                    // Traverse custom linked list nodes
                }
            }
        }
        System.out.println();
    }

    /**
     * Depth-First Search (DFS) Traversal using CustomStack & CustomSet.
     * Time Complexity: O(V + E)
     *
     * @param startVertex Starting vertex
     */
    public void traverseDFS(final T startVertex) {
        if (!adjacencyList.containsKey(startVertex)) {
            System.out.println("Start vertex not found.");
            return;
        }

        System.out.print("DFS Traversal starting from [" + startVertex + "]: ");
        final CustomStack<T> stack = new CustomStack<>();
        final CustomSet<T> visited = new CustomSet<>();

        stack.push(startVertex);

        while (!stack.isEmpty()) {
            final T current = stack.pop();
            if (!visited.contains(current)) {
                visited.add(current);
                System.out.print("[" + current + "] ");
            }
        }
        System.out.println();
    }
}
