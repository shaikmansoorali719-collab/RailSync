package com.railsync.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a Railway Flow Network supporting stations, tracks, capacities, and flows.
 */
public class FlowNetwork {
    private final int vertexCount;
    private final List<List<FlowEdge>> adjacencyList;
    private final Map<Integer, String> nodeLabels;
    private final Map<String, Integer> labelToId;
    private final List<FlowEdge> allEdges;

    public FlowNetwork(int vertexCount) {
        this.vertexCount = vertexCount;
        this.adjacencyList = new ArrayList<>(vertexCount);
        this.nodeLabels = new HashMap<>();
        this.labelToId = new HashMap<>();
        this.allEdges = new ArrayList<>();

        for (int i = 0; i < vertexCount; i++) {
            adjacencyList.add(new ArrayList<>());
            nodeLabels.put(i, "Node_" + i);
            labelToId.put("Node_" + i, i);
        }
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void setNodeLabel(int id, String label) {
        nodeLabels.put(id, label);
        labelToId.put(label, id);
    }

    public String getNodeLabel(int id) {
        return nodeLabels.getOrDefault(id, "Node_" + id);
    }

    public int getNodeId(String label) {
        return labelToId.getOrDefault(label, -1);
    }

    public FlowEdge addEdge(int u, int v, int capacity) {
        FlowEdge forward = new FlowEdge(u, v, capacity);
        FlowEdge backward = new FlowEdge(v, u, 0); // Residual reverse edge
        forward.setResidual(backward);
        backward.setResidual(forward);

        adjacencyList.get(u).add(forward);
        adjacencyList.get(v).add(backward);
        allEdges.add(forward);
        return forward;
    }

    public void addEdge(String fromLabel, String toLabel, int capacity) {
        int u = getNodeId(fromLabel);
        int v = getNodeId(toLabel);
        if (u != -1 && v != -1) {
            addEdge(u, v, capacity);
        }
    }

    public List<FlowEdge> getAdjacent(int u) {
        return adjacencyList.get(u);
    }

    public List<FlowEdge> getAllEdges() {
        return allEdges;
    }

    /**
     * Resets flow on all edges to 0.
     */
    public void resetFlows() {
        for (FlowEdge edge : allEdges) {
            // reset forward and residual
            edge.augment(-edge.getFlow());
        }
    }

    /**
     * Creates a standard South Central Railway (SCR) passenger corridor network.
     */
    public static FlowNetwork createSampleRailwayNetwork() {
        // 6 stations:
        // 0: Hyderabad (Source)
        // 1: Secunderabad
        // 2: Kazipet
        // 3: Vijayawada
        // 4: Guntur
        // 5: Visakhapatnam (Sink)
        FlowNetwork network = new FlowNetwork(6);
        network.setNodeLabel(0, "Hyderabad (HYB)");
        network.setNodeLabel(1, "Secunderabad (SC)");
        network.setNodeLabel(2, "Kazipet (KZJ)");
        network.setNodeLabel(3, "Vijayawada (BZA)");
        network.setNodeLabel(4, "Guntur (GNT)");
        network.setNodeLabel(5, "Visakhapatnam (VSKP)");

        // Track passenger capacities (passengers/hour)
        network.addEdge(0, 1, 2400); // HYB -> SC
        network.addEdge(0, 2, 1800); // HYB -> KZJ
        network.addEdge(1, 2, 1200); // SC -> KZJ
        network.addEdge(1, 3, 2000); // SC -> BZA
        network.addEdge(2, 5, 2200); // KZJ -> VSKP
        network.addEdge(3, 4, 1500); // BZA -> GNT
        network.addEdge(3, 5, 1900); // BZA -> VSKP
        network.addEdge(4, 5, 1100); // GNT -> VSKP

        return network;
    }
}
