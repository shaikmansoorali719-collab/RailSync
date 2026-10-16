package com.railsync.dp;

public class IntervalDP {

    public static int minimumCost(int[] stations) {

        int n = stations.length;

        if (n <= 2) {
            return 0;
        }

        int[][] dp = new int[n][n];

        for (int length = 2; length < n; length++) {

            for (int i = 0; i + length < n; i++) {

                int j = i + length;

                dp[i][j] = Integer.MAX_VALUE;

                for (int k = i + 1; k < j; k++) {

                    int cost = dp[i][k]
                             + dp[k][j]
                             + stations[i] * stations[k] * stations[j];

                    dp[i][j] = Math.min(
                        dp[i][j],
                        cost
                    );
                }
            }
        }

        return dp[0][n - 1];
    }

    public static void main(String[] args) {

        int[] stations = {10, 20, 30, 40};

        int cost = minimumCost(stations);

        System.out.println("Station values:");

        for (int value : stations) {
            System.out.print(value + " ");
        }

        System.out.println();
        System.out.println("Minimum Interval DP Cost: " + cost);
    }
}