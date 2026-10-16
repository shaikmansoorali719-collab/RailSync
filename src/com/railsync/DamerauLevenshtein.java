package com.railsync.dp;

import java.util.HashMap;
import java.util.Map;

/**
 * Module 3 - Dynamic Programming
 * Damerau-Levenshtein Distance Algorithm.
 * 
 * Computes the minimum number of single-character edits (insertions,
 * deletions, substitutions, and transpositions of two adjacent characters)
 * required to transform one string into another.
 * 
 * Railway Application: Automatic correction and fuzzy matching of mistyped
 * station names, passenger names, and service alert codes.
 */
public class DamerauLevenshtein {

    /**
     * Restricted Damerau-Levenshtein (Optimal String Alignment distance)
     * allows transposition of adjacent characters where each substring is edited at most once.
     */
    public static int calculate(String s1, String s2) {
        if (s1 == null) s1 = "";
        if (s2 == null) s2 = "";

        int n = s1.length();
        int m = s2.length();

        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;

                int deletion = dp[i - 1][j] + 1;
                int insertion = dp[i][j - 1] + 1;
                int substitution = dp[i - 1][j - 1] + cost;

                dp[i][j] = Math.min(deletion, Math.min(insertion, substitution));

                // Adjacent character transposition
                if (i > 1 && j > 1 
                        && s1.charAt(i - 1) == s2.charAt(j - 2) 
                        && s1.charAt(i - 2) == s2.charAt(j - 1)) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 2][j - 2] + 1);
                }
            }
        }

        return dp[n][m];
    }

    /**
     * Suggests the closest matching station name from a list of valid station names.
     */
    public static String suggestStation(String query, String[] validStations) {
        String bestMatch = null;
        int minDistance = Integer.MAX_VALUE;

        for (String station : validStations) {
            int d = calculate(query.toLowerCase(), station.toLowerCase());
            if (d < minDistance) {
                minDistance = d;
                bestMatch = station;
            }
        }
        return bestMatch + " (Distance: " + minDistance + ")";
    }

    public static void main(String[] args) {
        String[] stations = {
            "Secunderabad", "Vijayawada", "Guntur", "Tenali", "Visakhapatnam"
        };

        String typo = "Secundreabad"; // Transposition 'er' -> 're'
        System.out.println("Typo entered: " + typo);
        System.out.println("Damerau-Levenshtein distance to 'Secunderabad': " + calculate(typo, "Secunderabad"));
        System.out.println("Closest suggested station: " + suggestStation(typo, stations));
    }
}
