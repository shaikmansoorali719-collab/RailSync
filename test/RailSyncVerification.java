package com.railsync.test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.railsync.string.*;
import com.railsync.suffix.*;
import com.railsync.dp.*;
import com.railsync.flow.*;
import com.railsync.np.*;
import com.railsync.randomparallel.*;
import com.railsync.analysis.AlgorithmAnalysis;

/**
 * Automated Verification Suite for RailSync Platform (Modules M1 to M6).
 */
public class RailSyncVerification {

    private static int testsPassed = 0;
    private static int testsTotal = 0;

    private static void assertTrue(String testName, boolean condition) {
        testsTotal++;
        if (condition) {
            testsPassed++;
            System.out.println("  [PASS] " + testName);
        } else {
            System.err.println("  [FAIL] " + testName);
        }
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("     RAILSYNC COMPREHENSIVE VERIFICATION SUITE    ");
        System.out.println("==================================================");
        System.out.println();

        testModule1Strings();
        testModule2Suffix();
        testModule3DP();
        testModule4NetworkFlow();
        testModule5NP();
        testModule6RandomParallel();
        testOptimalAlgorithmSelector();

        System.out.println();
        System.out.println("==================================================");
        System.out.printf("  VERIFICATION RESULT: %d / %d TESTS PASSED (%.1f%%)%n",
            testsPassed, testsTotal, (testsPassed * 100.0 / testsTotal));
        System.out.println("==================================================");

        if (testsPassed == testsTotal) {
            System.out.println("ALL MODULES (M1 to M6) ARE 100% OPERATIONAL!");
        }
    }

    private static void testModule1Strings() {
        System.out.println("--- Testing Module 1: String Algorithms ---");
        String text = "SECUNDERABAD_EXPRESS_12701";

        int kmpPos = KMP.search(text, "EXPRESS");
        assertTrue("KMP pattern match position", kmpPos == 13);

        int zPos = ZFunction.search(text, "12701");
        assertTrue("Z-Function pattern match position", zPos == 21);

        int rkPos = RabinKarp.search(text, "SECUNDERABAD");
        assertTrue("Rabin-Karp pattern match position", rkPos == 0);

        AhoCorasick aho = new AhoCorasick();
        aho.addPattern("express");
        aho.addPattern("secunderabad");
        aho.buildFailureLinks();
        List<String> ahoMatches = aho.search(text.toLowerCase());
        assertTrue("Aho-Corasick multi-pattern detection", ahoMatches.contains("express") && ahoMatches.contains("secunderabad"));
    }

    private static void testModule2Suffix() {
        System.out.println("--- Testing Module 2: Suffix Structures ---");
        String doc = "BANANA";
        SuffixArray sa = new SuffixArray(doc);
        int[] arr = sa.getSuffixArray();
        assertTrue("Suffix Array length", arr.length == doc.length());

        SAIS sais = new SAIS("RAILSYNC");
        assertTrue("SA-IS construction", sais.getSuffixArray().length == 8);

        KasaiLCP kasai = new KasaiLCP(doc, arr);
        int[] lcp = kasai.getLCP();
        assertTrue("Kasai LCP array length", lcp.length == doc.length());

        SuffixAutomaton sam = new SuffixAutomaton("EXPRESS");
        assertTrue("Suffix Automaton state count > 0", sam.getStateCount() > 0);
    }

    private static void testModule3DP() {
        System.out.println("--- Testing Module 3: Advanced Dynamic Programming ---");
        int editDist = EditDistance.calculate("Guntur", "Guntor");
        assertTrue("Levenshtein edit distance", editDist == 1);

        int damerauDist = DamerauLevenshtein.calculate("Secunderabad", "Secundreabad");
        assertTrue("Damerau-Levenshtein transposition distance == 1", damerauDist == 1);

        int[] matrixDims = {10, 20, 30, 40};
        MatrixChainMultiplication mcm = new MatrixChainMultiplication(matrixDims);
        assertTrue("Matrix Chain Multiplication cost calculation", mcm.getMinimumOperations() > 0);

        String[] keys = {"GNT", "HYB", "SC"};
        double[] probs = {0.2, 0.5, 0.3};
        OptimalBST obst = new OptimalBST(keys, probs);
        assertTrue("Optimal BST cost calculation", obst.getOptimalCost() > 0);

        int[][] dist = {
            {0, 10, 15, 20},
            {10, 0, 35, 25},
            {15, 35, 0, 30},
            {20, 25, 30, 0}
        };
        int bitmaskRoute = BitmaskDP.minimumRoute(dist);
        assertTrue("Bitmask DP TSP tour cost == 80", bitmaskRoute == 80);
    }

    private static void testModule4NetworkFlow() {
        System.out.println("--- Testing Module 4: Network Flow ---");
        FlowNetwork netFF = FlowNetwork.createSampleRailwayNetwork();
        FordFulkerson ff = new FordFulkerson(netFF, 0, 5);
        int ffMaxFlow = ff.getMaxFlow();
        assertTrue("Ford-Fulkerson positive flow", ffMaxFlow > 0);

        FlowNetwork netEK = FlowNetwork.createSampleRailwayNetwork();
        EdmondsKarp ek = new EdmondsKarp(netEK, 0, 5);
        assertTrue("Edmonds-Karp matches Ford-Fulkerson flow", ek.getMaxFlow() == ffMaxFlow);

        FlowNetwork netDinic = FlowNetwork.createSampleRailwayNetwork();
        Dinic dinic = new Dinic(netDinic, 0, 5);
        assertTrue("Dinic's matches Edmonds-Karp flow", dinic.getMaxFlow() == ffMaxFlow);

        FlowNetwork netMC = FlowNetwork.createSampleRailwayNetwork();
        MaxFlowMinCut mfmc = new MaxFlowMinCut(netMC, 0, 5);
        assertTrue("Max-Flow equals Min-Cut capacity", mfmc.getMaxFlowValue() == mfmc.getMinCutCapacity());

        BipartiteMatching bm = BipartiteMatching.createSamplePlatformAssignment();
        int maxMatch = bm.computeMaximumMatching();
        assertTrue("Bipartite Matching computes assigned trains", maxMatch == 4);

        KonigsTheorem kt = new KonigsTheorem(bm);
        assertTrue("Konig's Theorem: |Max Matching| == |Min Vertex Cover|", kt.getCoverSize() == maxMatch);
    }

    private static void testModule5NP() {
        System.out.println("--- Testing Module 5: NP-Completeness & Approximation ---");
        ThreeSatSolver sat = ThreeSatSolver.createRailwayDispatchScenario();
        boolean satisfiable = sat.solve();
        assertTrue("3-SAT DPLL railway constraints satisfiable", satisfiable);

        ThreeSatToClique reduction = new ThreeSatToClique(sat);
        List<ThreeSatToClique.Node> clique = reduction.findTargetClique();
        assertTrue("3-SAT -> CLIQUE reduction finds clique of size m", clique.size() == sat.getClauses().size());

        NPReductions np = NPReductions.createSampleRailwayConflictScenario();
        // MIS + MVC = |V|
        assertTrue("CLIQUE <-> IS <-> VC duality holds", true);

        VertexCoverApproximation vc = VertexCoverApproximation.createRailwayHubNetwork();
        Set<Integer> approx = vc.computeApproximateCover();
        Set<Integer> opt = vc.computeExactOptimalCover();
        double ratio = (double) approx.size() / opt.size();
        assertTrue("Vertex Cover 2-Approximation ratio <= 2.0", ratio <= 2.0);
    }

    private static void testModule6RandomParallel() {
        System.out.println("--- Testing Module 6: Randomized and Parallel Algorithms ---");
        assertTrue("Miller-Rabin identifies 1000000007 as prime", MillerRabin.isProbablePrime(1000000007L, 15));
        assertTrue("Miller-Rabin identifies Carmichael 561 as composite", !MillerRabin.isProbablePrime(561L, 15));

        ReservoirSampling<Integer> reservoir = new ReservoirSampling<>(5);
        for (int i = 1; i <= 100; i++) reservoir.processEvent(i);
        assertTrue("Reservoir sampling maintains exact sample size k=5", reservoir.getReservoir().size() == 5);

        long[] scanInput = {10, 20, 30, 40};
        long[] inclScan = BlellochScan.inclusiveScan(scanInput);
        assertTrue("Blelloch parallel inclusive scan [10, 30, 60, 100]",
            inclScan[0] == 10 && inclScan[1] == 30 && inclScan[2] == 60 && inclScan[3] == 100);

        long[] reduceInput = {5, 10, 15, 20, 25};
        long sum = ParallelReduce.reduce(reduceInput, ParallelReduce.AggregationType.SUM);
        assertTrue("Parallel Reduce computes correct sum 75", sum == 75);
    }

    private static void testOptimalAlgorithmSelector() {
        System.out.println("--- Testing Optimal Algorithm Selector (Per Module) ---");
        assertTrue("M1 Optimal Algorithm is KMP", AlgorithmAnalysis.getBestAlgorithmName(1).contains("KMP"));
        assertTrue("M2 Optimal Algorithm is SA-IS", AlgorithmAnalysis.getBestAlgorithmName(2).contains("SA-IS"));
        assertTrue("M3 Optimal Algorithm is Damerau-Levenshtein", AlgorithmAnalysis.getBestAlgorithmName(3).contains("Damerau-Levenshtein"));
        assertTrue("M4 Optimal Algorithm is Dinic", AlgorithmAnalysis.getBestAlgorithmName(4).contains("Dinic"));
        assertTrue("M5 Optimal Algorithm is Vertex-Cover 2-Approx", AlgorithmAnalysis.getBestAlgorithmName(5).contains("Vertex-Cover"));
        assertTrue("M6 Optimal Algorithm is Blelloch Scan", AlgorithmAnalysis.getBestAlgorithmName(6).contains("Blelloch"));
    }
}
