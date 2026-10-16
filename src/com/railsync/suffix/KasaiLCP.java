package com.railsync.suffix;

public class KasaiLCP {

    private final String text;
    private final int[] suffixArray;
    private final int[] lcp;

    public KasaiLCP(String text, int[] suffixArray) {
        this.text = text;
        this.suffixArray = suffixArray;
        this.lcp = buildLCP();
    }

    private int[] buildLCP() {

        int n = text.length();

        int[] rank = new int[n];
        int[] lcp = new int[n];

        for (int i = 0; i < n; i++) {
            rank[suffixArray[i]] = i;
        }

        int commonLength = 0;

        for (int i = 0; i < n; i++) {

            int currentRank = rank[i];

            if (currentRank == 0) {
                continue;
            }

            int previousSuffix =
                    suffixArray[currentRank - 1];

            while (
                    i + commonLength < n &&
                    previousSuffix + commonLength < n &&
                    text.charAt(i + commonLength)
                            == text.charAt(
                            previousSuffix + commonLength)
            ) {
                commonLength++;
            }

            lcp[currentRank] = commonLength;

            if (commonLength > 0) {
                commonLength--;
            }
        }

        return lcp;
    }

    public int[] getLCP() {
        return lcp.clone();
    }

    public void display() {

        System.out.println();
        System.out.println("========== LCP / KASAI ==========");

        System.out.println("Railway Text:");
        System.out.println(text);

        System.out.println();
        System.out.println(
                "Rank\tSuffix Position\tLCP\tSuffix"
        );
        System.out.println(
                "------------------------------------------------"
        );

        for (int i = 0; i < suffixArray.length; i++) {

            int position = suffixArray[i];

            System.out.println(
                    i +
                    "\t" +
                    position +
                    "\t\t" +
                    lcp[i] +
                    "\t" +
                    text.substring(position)
            );
        }
    }

    public void displayRepeatedPatterns(int minimumLength) {

        System.out.println();
        System.out.println(
                "===== REPEATED SERVICE DESCRIPTIONS ====="
        );

        boolean found = false;

        for (int i = 1; i < lcp.length; i++) {

            if (lcp[i] >= minimumLength) {

                int position1 = suffixArray[i - 1];
                int position2 = suffixArray[i];

                String pattern =
                        text.substring(
                                position2,
                                position2 + lcp[i]
                        );

                System.out.println();
                System.out.println(
                        "Repeated Pattern: " + pattern
                );

                System.out.println(
                        "First occurrence position: " +
                        position1
                );

                System.out.println(
                        "Second occurrence position: " +
                        position2
                );

                System.out.println(
                        "Common length: " +
                        lcp[i]
                );

                found = true;
            }
        }

        if (!found) {

            System.out.println(
                    "No repeated service description found."
            );
        }
    }
}