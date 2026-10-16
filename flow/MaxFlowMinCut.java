package com.railsync.flow;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Module 4 - Network Flow
 * Max-Flow Min-Cut Theorem for Critical Railway Bottleneck Identification.
 * 
 * Theorem: The maximum amount of flow passing from the source to the sink is
 * equal to the total capacity of the edges in the minimum cut.
 * 
 * Railway Application: Identifies the saturated railway corridors / critical tracks
 * whose capacity constraints cap the passenger throughput of the entire network.
 */
public class MaxFlowMinCut {

    private final FlowNetwork network;
    private final int source;
    private final int sink;
    private final boolean[] inSourceSet;
    private final List<FlowEdge> minCutEdges;
    private int minCutCapacity;
    private int maxFlowValue;

    public MaxFlowMinCut(FlowNetwork network, int source, int sink) {
        this.network = network;
        this.source = source;
        this.sink = sink;
        int n = network.getVertexCount();
        this.inSourceSet = new boolean[n];
        this.minCutEdges = new ArrayList<>();
        computeMinCut();
    }

    private void computeMinCut() {
        // 1. Compute Max Flow using Dinic or Edmonds-Karp
        Dinic dinic = new Dinic(network, source, sink);
        this.maxFlowValue = dinic.getMaxFlow();

        // 2. BFS from source in residual network to identify S-partition
        Queue<Integer> queue = new ArrayDeque<>();
        inSourceSet[source] = true;
        queue.add(source);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (FlowEdge edge : network.getAdjacent(u)) {
                int v = edge.getTo();
                if (!inSourceSet[v] && edge.getResidualCapacity() > 0) {
                    inSourceSet[v] = true;
                    queue.add(v);
                }
            }
        }

        // 3. Edges with u in S and v in T with capacity > 0 form the Min-Cut
        minCutCapacity = 0;
        for (FlowEdge edge : network.getAllEdges()) {
            if (edge.getCapacity() > 0) {
                if (inSourceSet[edge.getFrom()] && !inSourceSet[edge.getTo()]) {
                    minCutEdges.add(edge);
                    minCutCapacity += edge.getCapacity();
                }
            }
        }
    }

    public int getMaxFlowValue() {
        return maxFlowValue;
    }

    public int getMinCutCapacity() {
        return minCutCapacity;
    }

    public List<FlowEdge> getMinCutEdges() {
        return minCutEdges;
    }

    public void display() {
        System.out.println("==================================================");
        System.out.println("  MAX-FLOW MIN-CUT: CRITICAL BOTTLENECK ANALYSIS");
        System.out.println("==================================================");
        System.out.println("Max-Flow Value: " + maxFlowValue + " pax/hr");
        System.out.println("Min-Cut Capacity: " + minCutCapacity + " pax/hr");
        System.out.println("Theorem Check  : " +
            (maxFlowValue == minCutCapacity ? "VERIFIED (Max-Flow == Min-Cut)" : "FAILED"));
        System.out.println();
        System.out.println("Partition S (Upstream Reached Hubs):");
        for (int i = 0; i < network.getVertexCount(); i++) {
            if (inSourceSet[i]) {
                System.out.println("  * " + network.getNodeLabel(i));
            }
        }
        System.out.println();
        System.out.println("Partition T (Downstream Saturated Hubs):");
        for (int i = 0; i < network.getVertexCount(); i++) {
            if (!inSourceSet[i]) {
                System.out.println("  * " + network.getNodeLabel(i));
            }
        }
        System.out.println();
        System.out.println("CRITICAL BOTTLENECK TRACKS (S -> T Saturated Corridors):");
        for (FlowEdge edge : minCutEdges) {
            System.out.printf("  [!] %-22s -> %-22s | Capacity: %d pax/hr (100%% Saturated)%n",
                network.getNodeLabel(edge.getFrom()),
                network.getNodeLabel(edge.getTo()),
                edge.getCapacity());
        }
        System.out.println();
        System.out.println("Strategic Recommendation: Doubling track or upgrading automatic signaling on the");
        System.out.println("above bottleneck corridors will immediately unlock increased passenger throughput.");
    }

    public static void main(String[] args) {
        FlowNetwork net = FlowNetwork.createSampleRailwayNetwork();
        MaxFlowMinCut mfmc = new MaxFlowMinCut(net, 0, 5);
        mfmc.display();
    }
}
