package com.railsync.flow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Module 4 - Network Flow
 * Bipartite Matching for Railway Platform and Service Allocation.
 * 
 * Assigns arriving trains to eligible terminal platforms under operational
 * and electrification constraints using maximum bipartite matching.
 */
public class BipartiteMatching {

    private final List<String> leftNodes;   // Trains
    private final List<String> rightNodes;  // Platforms
    private final List<List<Integer>> adj;  // Train index -> List of eligible platform indices
    private final int[] matchRight;         // Platform index -> assigned train index (-1 if free)
    private final int[] matchLeft;          // Train index -> assigned platform index (-1 if free)
    private int matchingSize;

    public BipartiteMatching(List<String> trains, List<String> platforms) {
        this.leftNodes = new ArrayList<>(trains);
        this.rightNodes = new ArrayList<>(platforms);
        this.adj = new ArrayList<>();
        for (int i = 0; i < trains.size(); i++) {
            adj.add(new ArrayList<>());
        }
        this.matchRight = new int[platforms.size()];
        this.matchLeft = new int[trains.size()];
        Arrays.fill(matchRight, -1);
        Arrays.fill(matchLeft, -1);
    }

    public void addEligibility(int trainIndex, int platformIndex) {
        if (trainIndex >= 0 && trainIndex < leftNodes.size() &&
            platformIndex >= 0 && platformIndex < rightNodes.size()) {
            adj.get(trainIndex).add(platformIndex);
        }
    }

    public void addEligibility(String train, String platform) {
        int u = leftNodes.indexOf(train);
        int v = rightNodes.indexOf(platform);
        if (u != -1 && v != -1) {
            addEligibility(u, v);
        }
    }

    public int computeMaximumMatching() {
        Arrays.fill(matchRight, -1);
        Arrays.fill(matchLeft, -1);
        matchingSize = 0;

        for (int u = 0; u < leftNodes.size(); u++) {
            boolean[] visited = new boolean[rightNodes.size()];
            if (dfs(u, visited)) {
                matchingSize++;
            }
        }
        return matchingSize;
    }

    private boolean dfs(int u, boolean[] visited) {
        for (int v : adj.get(u)) {
            if (!visited[v]) {
                visited[v] = true;

                // If platform v is not yet assigned, or previously assigned train can take another platform
                if (matchRight[v] < 0 || dfs(matchRight[v], visited)) {
                    matchRight[v] = u;
                    matchLeft[u] = v;
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> getLeftNodes() {
        return leftNodes;
    }

    public List<String> getRightNodes() {
        return rightNodes;
    }

    public List<List<Integer>> getAdjacencyList() {
        return adj;
    }

    public int[] getMatchLeft() {
        return matchLeft;
    }

    public int[] getMatchRight() {
        return matchRight;
    }

    public void display() {
        System.out.println("==================================================");
        System.out.println("  MAXIMUM BIPARTITE MATCHING: TRAIN -> PLATFORM");
        System.out.println("==================================================");
        System.out.println("Total Arriving Trains: " + leftNodes.size());
        System.out.println("Total Available Platforms: " + rightNodes.size());
        System.out.println("Max Trains Successfully Assigned: " + matchingSize);
        System.out.println();
        System.out.println("Platform Assignment Schedule:");
        for (int u = 0; u < leftNodes.size(); u++) {
            int v = matchLeft[u];
            if (v != -1) {
                System.out.printf("  %-25s -> %-20s [ASSIGNED]%n", leftNodes.get(u), rightNodes.get(v));
            } else {
                System.out.printf("  %-25s -> %-20s [NO PLATFORM AVAILABLE / HOLDING]%n", leftNodes.get(u), "NONE");
            }
        }
    }

    /**
     * Creates a representative railway terminal dispatching scenario.
     */
    public static BipartiteMatching createSamplePlatformAssignment() {
        List<String> trains = List.of(
            "12701 Golconda Exp",
            "12702 Godavari Exp",
            "12703 Satavahana Exp",
            "12704 Intercity Exp",
            "12705 Krishna Exp"
        );

        List<String> platforms = List.of(
            "Platform 1 (Mainline-Long)",
            "Platform 2 (Express-Electric)",
            "Platform 3 (Intercity)",
            "Platform 4 (Branchline)"
        );

        BipartiteMatching bm = new BipartiteMatching(trains, platforms);

        // Train 0 can use Platform 0, 1
        bm.addEligibility(0, 0);
        bm.addEligibility(0, 1);

        // Train 1 can use Platform 0, 1
        bm.addEligibility(1, 0);
        bm.addEligibility(1, 1);

        // Train 2 can use Platform 1, 2
        bm.addEligibility(2, 1);
        bm.addEligibility(2, 2);

        // Train 3 can use Platform 2, 3
        bm.addEligibility(3, 2);
        bm.addEligibility(3, 3);

        // Train 4 can use Platform 2
        bm.addEligibility(4, 2);

        bm.computeMaximumMatching();
        return bm;
    }

    public static void main(String[] args) {
        BipartiteMatching bm = createSamplePlatformAssignment();
        bm.display();
    }
}
