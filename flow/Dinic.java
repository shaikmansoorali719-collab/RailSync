package com.railsync.flow;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/**
 * Module 4 - Network Flow
 * Dinic's Algorithm (Level Graph + Blocking Flow).
 * 
 * Computes maximum passenger flow on large-scale railway networks.
 * Uses BFS to construct level graphs and DFS with current-edge pointers to find
 * blocking flows in O(V^2 * E) time (much faster in practice than Edmonds-Karp).
 */
public class Dinic {

    private final FlowNetwork network;
    private final int source;
    private final int sink;
    private final int[] level;
    private final int[] work;
    private int maxFlow;
    private int phaseCount;

    public Dinic(FlowNetwork network, int source, int sink) {
        this.network = network;
        this.source = source;
        this.sink = sink;
        int n = network.getVertexCount();
        this.level = new int[n];
        this.work = new int[n];
        computeMaxFlow();
    }

    private void computeMaxFlow() {
        maxFlow = 0;
        phaseCount = 0;

        while (bfsLevelGraph()) {
            Arrays.fill(work, 0);
            while (true) {
                int pushed = sendBlockingFlow(source, Integer.MAX_VALUE);
                if (pushed <= 0) break;
                maxFlow += pushed;
            }
            phaseCount++;
        }
    }

    private boolean bfsLevelGraph() {
        Arrays.fill(level, -1);
        level[source] = 0;
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(source);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (FlowEdge edge : network.getAdjacent(u)) {
                int v = edge.getTo();
                if (level[v] < 0 && edge.getResidualCapacity() > 0) {
                    level[v] = level[u] + 1;
                    queue.add(v);
                }
            }
        }

        return level[sink] >= 0;
    }

    private int sendBlockingFlow(int u, int flow) {
        if (u == sink) {
            return flow;
        }

        var edges = network.getAdjacent(u);
        for (; work[u] < edges.size(); work[u]++) {
            FlowEdge edge = edges.get(work[u]);
            int v = edge.getTo();

            if (level[v] == level[u] + 1 && edge.getResidualCapacity() > 0) {
                int currentFlow = Math.min(flow, edge.getResidualCapacity());
                int pushed = sendBlockingFlow(v, currentFlow);

                if (pushed > 0) {
                    edge.augment(pushed);
                    return pushed;
                }
            }
        }

        return 0;
    }

    public int getMaxFlow() {
        return maxFlow;
    }

    public int getPhaseCount() {
        return phaseCount;
    }

    public void display() {
        System.out.println("==================================================");
        System.out.println("  DINIC'S LARGE-SCALE PASSENGER NETWORK FLOW");
        System.out.println("==================================================");
        System.out.println("Source Terminal: " + network.getNodeLabel(source));
        System.out.println("Sink Terminal: " + network.getNodeLabel(sink));
        System.out.println("BFS Level Graph Phases: " + phaseCount);
        System.out.println("Max Passenger Flow: " + maxFlow + " passengers/hour");
        System.out.println();
        System.out.println("Corridor Flow Allocations:");
        for (FlowEdge edge : network.getAllEdges()) {
            if (edge.getCapacity() > 0) {
                System.out.printf("  %-25s -> %-25s | Flow: %5d / %5d pax/hr%n",
                    network.getNodeLabel(edge.getFrom()),
                    network.getNodeLabel(edge.getTo()),
                    edge.getFlow(),
                    edge.getCapacity());
            }
        }
    }

    public static void main(String[] args) {
        FlowNetwork net = FlowNetwork.createSampleRailwayNetwork();
        Dinic dinic = new Dinic(net, 0, 5);
        dinic.display();
    }
}
