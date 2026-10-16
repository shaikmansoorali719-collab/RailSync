package com.railsync.flow;

import java.util.ArrayList;
import java.util.List;

/**
 * Module 4 - Network Flow
 * Ford-Fulkerson Algorithm (DFS-based Augmenting Paths).
 * 
 * Models passenger flow through railway track connections from an origin
 * terminal (source) to a destination hub (sink).
 */
public class FordFulkerson {

    private final FlowNetwork network;
    private final int source;
    private final int sink;
    private int maxFlow;
    private final List<String> augmentationLog;

    public FordFulkerson(FlowNetwork network, int source, int sink) {
        this.network = network;
        this.source = source;
        this.sink = sink;
        this.augmentationLog = new ArrayList<>();
        computeMaxFlow();
    }

    private void computeMaxFlow() {
        maxFlow = 0;
        int n = network.getVertexCount();

        while (true) {
            boolean[] visited = new boolean[n];
            List<FlowEdge> path = new ArrayList<>();
            int bottleneck = dfs(source, Integer.MAX_VALUE, visited, path);

            if (bottleneck == 0) {
                break;
            }

            maxFlow += bottleneck;
            StringBuilder log = new StringBuilder();
            log.append(String.format("Augmented +%d pax/hr along path: ", bottleneck));
            for (int i = 0; i < path.size(); i++) {
                FlowEdge e = path.get(i);
                if (i == 0) log.append(network.getNodeLabel(e.getFrom()));
                log.append(" -> ").append(network.getNodeLabel(e.getTo()));
            }
            augmentationLog.add(log.toString());
        }
    }

    private int dfs(int u, int currentBottleneck, boolean[] visited, List<FlowEdge> path) {
        if (u == sink) {
            return currentBottleneck;
        }

        visited[u] = true;

        for (FlowEdge edge : network.getAdjacent(u)) {
            int v = edge.getTo();
            if (!visited[v] && edge.getResidualCapacity() > 0) {
                int pushed = dfs(v, Math.min(currentBottleneck, edge.getResidualCapacity()), visited, path);
                if (pushed > 0) {
                    edge.augment(pushed);
                    path.add(0, edge);
                    return pushed;
                }
            }
        }

        return 0;
    }

    public int getMaxFlow() {
        return maxFlow;
    }

    public List<String> getAugmentationLog() {
        return augmentationLog;
    }

    public void display() {
        System.out.println("==================================================");
        System.out.println("  FORD-FULKERSON PASSENGER FLOW ANALYSIS");
        System.out.println("==================================================");
        System.out.println("Source Origin: " + network.getNodeLabel(source));
        System.out.println("Sink Destination: " + network.getNodeLabel(sink));
        System.out.println();
        System.out.println("Augmentation Path Trace:");
        for (String log : augmentationLog) {
            System.out.println("  " + log);
        }
        System.out.println();
        System.out.println("Maximum Railway Corridor Flow: " + maxFlow + " passengers/hour");
        System.out.println();
        System.out.println("Track Utilization:");
        for (FlowEdge edge : network.getAllEdges()) {
            if (edge.getCapacity() > 0) {
                System.out.printf("  %-25s -> %-25s | Flow: %5d / %5d pax/hr (%5.1f%%)%n",
                    network.getNodeLabel(edge.getFrom()),
                    network.getNodeLabel(edge.getTo()),
                    edge.getFlow(),
                    edge.getCapacity(),
                    (edge.getFlow() * 100.0 / edge.getCapacity()));
            }
        }
    }

    public static void main(String[] args) {
        FlowNetwork net = FlowNetwork.createSampleRailwayNetwork();
        FordFulkerson ff = new FordFulkerson(net, 0, 5);
        ff.display();
    }
}
