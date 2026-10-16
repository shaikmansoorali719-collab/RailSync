package com.railsync.randomparallel;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Module 6 - Randomized and Parallel Algorithms
 * Blelloch Parallel Scan (Work-Efficient Parallel Prefix Sum).
 * 
 * Computes prefix sums with O(n) total work and O(log n) span (critical path depth).
 * Operates in two parallel phases:
 * 1. Up-Sweep (Reduce): Computes partial sums tree bottom-up.
 * 2. Down-Sweep: Distributes accumulated values top-down.
 * 
 * Railway Application:
 * Computes cumulative passenger loading, ticket volume progressions, and energy
 * consumption across sequential stations on high-speed rail corridors.
 */
public class BlellochScan {

    private static final ForkJoinPool POOL = new ForkJoinPool();

    /**
     * Executes work-efficient Blelloch scan (exclusive prefix sum) in-place.
     * Array length must be a power of 2.
     */
    public static long[] exclusiveScan(long[] input) {
        int originalLength = input.length;
        int n = 1;
        while (n < originalLength) n <<= 1;

        long[] a = new long[n];
        System.arraycopy(input, 0, a, 0, originalLength);

        // 1. Up-Sweep (Reduce) Phase
        for (int d = 0; d < log2(n); d++) {
            int stride = 1 << (d + 1);
            int half = 1 << d;
            POOL.invoke(new UpSweepTask(a, 0, n, stride, half));
        }

        // 2. Clear root value
        a[n - 1] = 0;

        // 3. Down-Sweep Phase
        for (int d = log2(n) - 1; d >= 0; d--) {
            int stride = 1 << (d + 1);
            int half = 1 << d;
            POOL.invoke(new DownSweepTask(a, 0, n, stride, half));
        }

        // Return trimmed to original length
        return Arrays.copyOf(a, originalLength);
    }

    /**
     * Inclusive prefix sum: cumulative sum including current station element.
     */
    public static long[] inclusiveScan(long[] input) {
        long[] excl = exclusiveScan(input);
        long[] incl = new long[input.length];
        for (int i = 0; i < input.length; i++) {
            incl[i] = excl[i] + input[i];
        }
        return incl;
    }

    private static int log2(int n) {
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    private static class UpSweepTask extends RecursiveAction {
        private final long[] a;
        private final int start;
        private final int end;
        private final int stride;
        private final int half;

        public UpSweepTask(long[] a, int start, int end, int stride, int half) {
            this.a = a;
            this.start = start;
            this.end = end;
            this.stride = stride;
            this.half = half;
        }

        @Override
        protected void compute() {
            int count = (end - start) / stride;
            if (count <= 1024) {
                for (int i = start; i < end; i += stride) {
                    a[i + stride - 1] += a[i + half - 1];
                }
            } else {
                int mid = start + (count / 2) * stride;
                invokeAll(
                    new UpSweepTask(a, start, mid, stride, half),
                    new UpSweepTask(a, mid, end, stride, half)
                );
            }
        }
    }

    private static class DownSweepTask extends RecursiveAction {
        private final long[] a;
        private final int start;
        private final int end;
        private final int stride;
        private final int half;

        public DownSweepTask(long[] a, int start, int end, int stride, int half) {
            this.a = a;
            this.start = start;
            this.end = end;
            this.stride = stride;
            this.half = half;
        }

        @Override
        protected void compute() {
            int count = (end - start) / stride;
            if (count <= 1024) {
                for (int i = start; i < end; i += stride) {
                    long t = a[i + half - 1];
                    a[i + half - 1] = a[i + stride - 1];
                    a[i + stride - 1] += t;
                }
            } else {
                int mid = start + (count / 2) * stride;
                invokeAll(
                    new DownSweepTask(a, start, mid, stride, half),
                    new DownSweepTask(a, mid, end, stride, half)
                );
            }
        }
    }

    public static void display() {
        System.out.println("==================================================");
        System.out.println("  BLELLOCH WORK-EFFICIENT PARALLEL SCAN");
        System.out.println("  Cumulative Corridor Passenger Statistics");
        System.out.println("==================================================");

        String[] stations = {
            "Hyderabad (HYB)",
            "Secunderabad (SC)",
            "Kazipet (KZJ)",
            "Warangal (WL)",
            "Khammam (KMT)",
            "Vijayawada (BZA)",
            "Eluru (EE)",
            "Visakhapatnam (VSKP)"
        };

        // Net boarding passenger increments per station
        long[] boardings = {280, 420, 190, 150, 210, 520, 130, 0};

        System.out.println("Net Passenger Boardings by Station:");
        for (int i = 0; i < stations.length; i++) {
            System.out.printf("  %-25s : +%d passengers%n", stations[i], boardings[i]);
        }
        System.out.println();

        long[] exclusive = exclusiveScan(boardings);
        long[] inclusive = inclusiveScan(boardings);

        System.out.println("Parallel Scan Results:");
        System.out.printf("  %-25s | %-16s | %-16s%n", "Station", "Prior Load (Excl)", "Total Onboard (Incl)");
        System.out.println("  " + "-".repeat(65));
        for (int i = 0; i < stations.length; i++) {
            System.out.printf("  %-25s | %13d pax | %14d pax%n",
                stations[i], exclusive[i], inclusive[i]);
        }

        System.out.println();
        System.out.println("Algorithm Complexity Metrics:");
        System.out.println("  Total Work : O(n) additions (Work-Efficient, matches serial scan)");
        System.out.println("  Span (Depth): O(log n) parallel critical path steps");
        System.out.println("  Hardware Concurrency: " + POOL.getParallelism() + " active worker threads");
    }

    public static void main(String[] args) {
        display();
    }
}
