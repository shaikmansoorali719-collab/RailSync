package com.railsync.dp;

/**
 * Module 3 - Dynamic Programming
 * Matrix Chain Multiplication (MCM) for Railway Data Query Optimization.
 * 
 * Determines the most efficient way to multiply a sequence of relational matrices
 * representing multi-hop railway schedules, passenger connections, and station transfers.
 * Computes optimal parenthesization to minimize total scalar multiplications.
 */
public class MatrixChainMultiplication {

    private int[] dims;
    private int n;
    private int[][] m;
    private int[][] s;

    public MatrixChainMultiplication(int[] dimensions) {
        this.dims = dimensions;
        this.n = dimensions.length - 1; // number of matrices
        this.m = new int[n + 1][n + 1];
        this.s = new int[n + 1][n + 1];
        solve();
    }

    private void solve() {
        for (int i = 1; i <= n; i++) {
            m[i][i] = 0;
        }

        for (int length = 2; length <= n; length++) {
            for (int i = 1; i <= n - length + 1; i++) {
                int j = i + length - 1;
                m[i][j] = Integer.MAX_VALUE;

                for (int k = i; k <= j - 1; k++) {
                    int cost = m[i][k] + m[k + 1][j] + dims[i - 1] * dims[k] * dims[j];
                    if (cost < m[i][j]) {
                        m[i][j] = cost;
                        s[i][j] = k;
                    }
                }
            }
        }
    }

    public int getMinimumOperations() {
        return m[1][n];
    }

    public String getOptimalParenthesization() {
        StringBuilder sb = new StringBuilder();
        buildOptimalOrder(1, n, sb);
        return sb.toString();
    }

    private void buildOptimalOrder(int i, int j, StringBuilder sb) {
        if (i == j) {
            sb.append("Dataset_M").append(i);
        } else {
            sb.append("(");
            buildOptimalOrder(i, s[i][j], sb);
            sb.append(" x ");
            buildOptimalOrder(s[i][j] + 1, j, sb);
            sb.append(")");
        }
    }

    public void display() {
        System.out.println("Railway Matrix Chain Optimization:");
        System.out.println("Matrix Dimensions (Railway Dataset Matrices):");
        for (int i = 1; i <= n; i++) {
            System.out.printf("  Dataset_M%d: %d x %d%n", i, dims[i - 1], dims[i]);
        }
        System.out.println("Minimum Scalar Operations: " + getMinimumOperations());
        System.out.println("Optimal Join Order: " + getOptimalParenthesization());
    }

    public static void main(String[] args) {
        // e.g. Dimensions of Railway Matrices:
        // M1: Trains x Stations (10x20)
        // M2: Stations x Platforms (20x30)
        // M3: Platforms x Services (30x15)
        // M4: Services x Passenger Bookings (15x50)
        int[] dims = {10, 20, 30, 15, 50};
        MatrixChainMultiplication mcm = new MatrixChainMultiplication(dims);
        mcm.display();
    }
}
