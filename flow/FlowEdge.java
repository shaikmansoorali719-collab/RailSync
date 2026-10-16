package com.railsync.flow;

/**
 * Represents a directed edge with flow and capacity in a railway network.
 */
public class FlowEdge {
    private final int u;
    private final int v;
    private final int capacity;
    private int flow;
    private FlowEdge residual;

    public FlowEdge(int u, int v, int capacity) {
        this.u = u;
        this.v = v;
        this.capacity = capacity;
        this.flow = 0;
    }

    public int getFrom() {
        return u;
    }

    public int getTo() {
        return v;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getFlow() {
        return flow;
    }

    public FlowEdge getResidual() {
        return residual;
    }

    public void setResidual(FlowEdge residual) {
        this.residual = residual;
    }

    public int getResidualCapacity() {
        return capacity - flow;
    }

    public void augment(int bottleneck) {
        this.flow += bottleneck;
        this.residual.flow -= bottleneck;
    }

    @Override
    public String toString() {
        return String.format("%d -> %d (flow: %d / cap: %d)", u, v, flow, capacity);
    }
}
