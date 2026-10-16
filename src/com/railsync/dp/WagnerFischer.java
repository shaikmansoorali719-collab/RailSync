package com.railsync.dp;

public class WagnerFischer {

    public static int calculate(String text1, String text2) {

        int n = text1.length();
        int m = text2.length();

        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {

            for (int j = 1; j <= m; j++) {

                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {

                    dp[i][j] = dp[i - 1][j - 1];

                } else {

                    dp[i][j] = 1 + Math.min(
                        dp[i - 1][j - 1],
                        Math.min(
                            dp[i - 1][j],
                            dp[i][j - 1]
                        )
                    );
                }
            }
        }

        return dp[n][m];
    }

    public static void main(String[] args) {

        String station1 = "Secunderabad";
        String station2 = "Secunderbad";

        int distance = calculate(station1, station2);

        System.out.println("String 1: " + station1);
        System.out.println("String 2: " + station2);
        System.out.println("Wagner-Fischer Distance: " + distance);
    }
}