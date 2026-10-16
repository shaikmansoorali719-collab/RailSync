package com.railsync.dp;

/**
 * Module 3 - Dynamic Programming
 * Optimal Binary Search Tree (OBST).
 * 
 * Computes the optimal search tree structure for frequently accessed
 * station and service records to minimize expected search cost (comparisons).
 * 
 * Given sorted keys and their access probabilities/frequencies, dynamic
 * programming is used:
 * e[i][j] = min_{i <= r <= j} (e[i][r-1] + e[r+1][j] + w[i][j])
 * Time Complexity: O(n^3) (or O(n^2) with Knuth's optimization).
 */
public class OptimalBST {

    private String[] keys;
    private double[] probabilities;
    private double[][] cost;
    private int[][] root;
    private int n;

    public OptimalBST(String[] keys, double[] probabilities) {
        this.keys = keys;
        this.probabilities = probabilities;
        this.n = keys.length;
        this.cost = new double[n + 2][n + 2];
        this.root = new int[n + 2][n + 2];
        computeOBST();
    }

    private void computeOBST() {
        // weight[i][j] is sum of probabilities from i to j
        double[][] weight = new double[n + 2][n + 2];

        for (int i = 1; i <= n; i++) {
            cost[i][i - 1] = 0;
            weight[i][i - 1] = 0;
        }

        for (int length = 1; length <= n; length++) {
            for (int i = 1; i <= n - length + 1; i++) {
                int j = i + length - 1;
                cost[i][j] = Double.MAX_VALUE;
                weight[i][j] = weight[i][j - 1] + probabilities[j - 1];

                for (int r = i; r <= j; r++) {
                    double c = cost[i][r - 1] + cost[r + 1][j] + weight[i][j];
                    if (c < cost[i][j]) {
                        cost[i][j] = c;
                        root[i][j] = r;
                    }
                }
            }
        }
    }

    public double getOptimalCost() {
        return cost[1][n];
    }

    public void displayTree() {
        System.out.println("Optimal Binary Search Tree for Railway Station Records:");
        System.out.println("Minimum Expected Search Cost: " + String.format("%.4f", getOptimalCost()));
        System.out.println("Tree Hierarchy:");
        printTreeStructure(1, n, 0, "ROOT");
    }

    private void printTreeStructure(int i, int j, int depth, String type) {
        if (i > j) {
            return;
        }
        int r = root[i][j];
        String indent = "  ".repeat(depth);
        System.out.printf("%s[%s] Key: %s (Freq: %.2f)%n", indent, type, keys[r - 1], probabilities[r - 1]);
        printTreeStructure(i, r - 1, depth + 1, "LEFT");
        printTreeStructure(r + 1, j, depth + 1, "RIGHT");
    }

    public static void main(String[] args) {
        // Sorted railway stations with operational query frequencies
        String[] stationKeys = {
            "Guntur", "Hyderabad", "Secunderabad", "Tenali", "Vijayawada"
        };
        // Normalized search query frequencies
        double[] freq = {0.15, 0.25, 0.35, 0.05, 0.20};

        OptimalBST obst = new OptimalBST(stationKeys, freq);
        obst.displayTree();
    }
}
