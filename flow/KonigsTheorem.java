package com.railsync.flow;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Module 4 - Network Flow
 * König's Theorem for Railway Platform-Service Conflict Analysis.
 * 
 * Theorem: In any bipartite graph, the size of the maximum matching equals
 * the size of the minimum vertex cover: |Max Matching| = |Min Vertex Cover|.
 * 
 * Railway Application: Identifies the smallest set of platforms and train services
 * needed to eliminate, cover, or inspect all conflict interactions.
 */
public class KonigsTheorem {

    private final BipartiteMatching matching;
    private final Set<Integer> coverLeft;   // Indices in L belonging to min vertex cover
    private final Set<Integer> coverRight;  // Indices in R belonging to min vertex cover

    public KonigsTheorem(BipartiteMatching matching) {
        this.matching = matching;
        this.coverLeft = new HashSet<>();
        this.coverRight = new HashSet<>();
        computeMinimumVertexCover();
    }

    private void computeMinimumVertexCover() {
        matching.computeMaximumMatching();

        int nL = matching.getLeftNodes().size();
        int nR = matching.getRightNodes().size();
        int[] matchLeft = matching.getMatchLeft();
        int[] matchRight = matching.getMatchRight();
        List<List<Integer>> adj = matching.getAdjacencyList();

        // 1. Identify unmatched vertices in L (Trains)
        boolean[] reachableL = new boolean[nL];
        boolean[] reachableR = new boolean[nR];
        Queue<Integer> queue = new ArrayDeque<>();

        for (int u = 0; u < nL; u++) {
            if (matchLeft[u] == -1) {
                reachableL[u] = true;
                queue.add(u);
            }
        }

        // 2. BFS along alternating paths
        // - From L to R: along non-matching edges
        // - From R to L: along matching edges
        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int v : adj.get(u)) {
                // If this edge (u, v) is not a matched edge
                if (matchLeft[u] != v && !reachableR[v]) {
                    reachableR[v] = true;
                    int matchedTrain = matchRight[v];
                    if (matchedTrain != -1 && !reachableL[matchedTrain]) {
                        reachableL[matchedTrain] = true;
                        queue.add(matchedTrain);
                    }
                }
            }
        }

        // 3. Min Vertex Cover:
        // Vertex Cover = (L \ ReachableL) U (R \cap ReachableR)
        for (int u = 0; u < nL; u++) {
            if (!reachableL[u]) {
                coverLeft.add(u);
            }
        }
        for (int v = 0; v < nR; v++) {
            if (reachableR[v]) {
                coverRight.add(v);
            }
        }
    }

    public int getCoverSize() {
        return coverLeft.size() + coverRight.size();
    }

    public void display() {
        System.out.println("==================================================");
        System.out.println("  KONIG'S THEOREM: CONFLICT & COVER ANALYSIS");
        System.out.println("==================================================");
        int maxMatch = matching.computeMaximumMatching();
        System.out.println("Maximum Bipartite Matching Size : " + maxMatch);
        System.out.println("Minimum Vertex Cover Size        : " + getCoverSize());
        System.out.println("Konig's Theorem Verification     : " +
            (maxMatch == getCoverSize() ? "VERIFIED (|M| == |Min-VC|)" : "FAILED"));
        System.out.println();
        System.out.println("Critical Conflict Set (Minimum Vertex Cover Nodes):");

        System.out.println("  Trains in Cover (Constrained/High-Demand Services):");
        if (coverLeft.isEmpty()) {
            System.out.println("    (None)");
        } else {
            for (int u : coverLeft) {
                System.out.println("    - " + matching.getLeftNodes().get(u));
            }
        }

        System.out.println("  Platforms in Cover (High-Congestion Terminals):");
        if (coverRight.isEmpty()) {
            System.out.println("    (None)");
        } else {
            for (int v : coverRight) {
                System.out.println("    - " + matching.getRightNodes().get(v));
            }
        }
        System.out.println();
        System.out.println("Operational Insight: Monitoring or expanding only these " + getCoverSize()
            + " entities guarantees complete coverage of all platform-service scheduling conflicts.");
    }

    public static void main(String[] args) {
        BipartiteMatching bm = BipartiteMatching.createSamplePlatformAssignment();
        KonigsTheorem kt = new KonigsTheorem(bm);
        kt.display();
    }
}
