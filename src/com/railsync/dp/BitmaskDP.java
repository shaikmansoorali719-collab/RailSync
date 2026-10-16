package com.railsync.dp;

public class BitmaskDP {

    public static int minimumRoute(int[][] distance) {

        int n = distance.length;
        int totalMasks = 1 << n;

        int[][] dp = new int[totalMasks][n];

        for (int mask = 0; mask < totalMasks; mask++) {
            for (int i = 0; i < n; i++) {
                dp[mask][i] = Integer.MAX_VALUE;
            }
        }

        dp[1][0] = 0;

        for (int mask = 1; mask < totalMasks; mask++) {

            for (int current = 0; current < n; current++) {

                if ((mask & (1 << current)) == 0) {
                    continue;
                }

                if (dp[mask][current] == Integer.MAX_VALUE) {
                    continue;
                }

                for (int next = 0; next < n; next++) {

                    if ((mask & (1 << next)) != 0) {
                        continue;
                    }

                    int newMask = mask | (1 << next);

                    int newCost = dp[mask][current]
                                + distance[current][next];

                    dp[newMask][next] = Math.min(
                        dp[newMask][next],
                        newCost
                    );
                }
            }
        }

        int fullMask = totalMasks - 1;
        int answer = Integer.MAX_VALUE;

        for (int i = 0; i < n; i++) {
            answer = Math.min(
                answer,
                dp[fullMask][i] + distance[i][0]
            );
        }

        return answer;
    }

    public static void main(String[] args) {

        int[][] distance = {
            {0, 10, 15, 20},
            {10, 0, 35, 25},
            {15, 35, 0, 30},
            {20, 25, 30, 0}
        };

        int result = minimumRoute(distance);

        System.out.println("Minimum Route Cost: " + result);
    }
}