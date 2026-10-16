package com.railsync.flow;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * Module 4 - Network Flow
 * Edmonds-Karp Algorithm (BFS-based Shortest Augmenting Paths).
 * 
 * Computes maximum passenger flow and analyzes railway line capacities.
 * Guarantees polynomial time O(V * E^2).
 */
public class EdmondsKarp {

    private final FlowNetwork network;
    private final int source;
    private final int sink;
    private int maxFlow;
    private int iterations;

    public EdmondsKarp(FlowNetwork network, int source, int sink) {
        this.network = network;
        this.source = source;
        this.sink = sink;
        computeMaxFlow();
    }

    private void computeMaxFlow() {
        maxFlow = 0;
        iterations = 0;
        int n = network.getVertexCount();

        while (true) {
            FlowEdge[] edgeTo = new FlowEdge[n];
            Queue<Integer> queue = new ArrayDeque<>();
            queue.add(source);

            while (!queue.isEmpty() && edgeTo[sink] == null) {
                int u = queue.poll();

                for (FlowEdge edge : network.getAdjacent(u)) {
                    int v = edge.getTo();
                    if (v != source && edgeTo[v] == null && edge.getResidualCapacity() > 0) {
                        edgeTo[v] = edge;
                        queue.add(v);
                    }
                }
            }

            if (edgeTo[sink] == null) {
                break; // No augmenting path exists
            }

            // Find bottleneck capacity along the shortest path
            int bottleneck = Integer.MAX_VALUE;
            for (FlowEdge e = edgeTo[sink]; e != null; e = edgeTo[e.getFrom()]) {
                bottleneck = Math.min(bottleneck, e.getResidualCapacity());
            }

            // Augment flow
            for (FlowEdge e = edgeTo[sink]; e != null; e = edgeTo[e.getFrom()]) {
                e.augment(bottleneck);
            }

            maxFlow += bottleneck;
            iterations++;
        }
    }

    public int getMaxFlow() {
        return maxFlow;
    }

    public int getIterations() {
        return iterations;
    }

    public void display() {
        System.out.println("==================================================");
        System.out.println("  EDMONDS-KARP RAILWAY CAPACITY ANALYSIS");
        System.out.println("==================================================");
        System.out.println("Origin Station: " + network.getNodeLabel(source));
        System.out.println("Destination Station: " + network.getNodeLabel(sink));
        System.out.println("BFS Augmenting Path Iterations: " + iterations);
        System.out.println("Maximum Railway Flow: " + maxFlow + " passengers/hour");
        System.out.println();
        System.out.println("Capacity Analysis Summary:");
        int saturatedCount = 0;
        for (FlowEdge edge : network.getAllEdges()) {
            if (edge.getCapacity() > 0) {
                boolean isSaturated = (edge.getFlow() == edge.getCapacity());
                if (isSaturated) saturatedCount++;
                System.out.printf("  %-22s -> %-22s | Flow: %5d / %5d [%s]%n",
                    network.getNodeLabel(edge.getFrom()),
                    network.getNodeLabel(edge.getTo()),
                    edge.getFlow(),
                    edge.getCapacity(),
                    isSaturated ? "SATURATED (BOTTLENECK)" : "UNDER CAPACITY");
            }
        }
        System.out.println("Total Saturated Tracks: " + saturatedCount);
    }

    public static void main(String[] args) {
        FlowNetwork net = FlowNetwork.createSampleRailwayNetwork();
        EdmondsKarp ek = new EdmondsKarp(net, 0, 5);
        ek.display();
    }
}
