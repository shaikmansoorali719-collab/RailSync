package com.railsync.dp;

public class NeedlemanWunsch {

    public static int calculate(String sequence1, String sequence2) {

        int n = sequence1.length();
        int m = sequence2.length();

        int match = 1;
        int mismatch = -1;
        int gap = -2;

        int[][] dp = new int[n + 1][m + 1];

        for (int i = 1; i <= n; i++) {
            dp[i][0] = i * gap;
        }

        for (int j = 1; j <= m; j++) {
            dp[0][j] = j * gap;
        }

        for (int i = 1; i <= n; i++) {

            for (int j = 1; j <= m; j++) {

                int diagonal;

                if (sequence1.charAt(i - 1) == sequence2.charAt(j - 1)) {
                    diagonal = dp[i - 1][j - 1] + match;
                } else {
                    diagonal = dp[i - 1][j - 1] + mismatch;
                }

                int up = dp[i - 1][j] + gap;
                int left = dp[i][j - 1] + gap;

                dp[i][j] = Math.max(
                    diagonal,
                    Math.max(up, left)
                );
            }
        }

        return dp[n][m];
    }

    public static void main(String[] args) {

        String route1 = "HVG";
        String route2 = "HVGT";

        int score = calculate(route1, route2);

        System.out.println("Sequence 1: " + route1);
        System.out.println("Sequence 2: " + route2);
        System.out.println("Global Alignment Score: " + score);
    }
}