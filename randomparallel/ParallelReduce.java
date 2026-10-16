package com.railsync.randomparallel;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Module 6 - Randomized and Parallel Algorithms
 * Parallel Reduce for Large-Scale Railway Data Aggregation.
 * 
 * Performs tree-based parallel reduction using Java ForkJoin framework
 * with Work O(n) and Span O(log n).
 * 
 * Railway Application:
 * Aggregates high-volume operational metrics across thousands of daily train runs:
 * total passenger counts, total revenue, peak delays, and energy consumption.
 */
public class ParallelReduce {

    private static final int SEQUENTIAL_THRESHOLD = 50_000;
    private static final ForkJoinPool POOL = new ForkJoinPool();

    public enum AggregationType {
        SUM, MAX, MIN
    }

    private static class ReduceTask extends RecursiveTask<Long> {
        private final long[] data;
        private final int start;
        private final int end;
        private final AggregationType type;

        public ReduceTask(long[] data, int start, int end, AggregationType type) {
            this.data = data;
            this.start = start;
            this.end = end;
            this.type = type;
        }

        @Override
        protected Long compute() {
            int length = end - start;
            if (length <= SEQUENTIAL_THRESHOLD) {
                return sequentialReduce(data, start, end, type);
            }

            int mid = start + length / 2;
            ReduceTask leftTask = new ReduceTask(data, start, mid, type);
            ReduceTask rightTask = new ReduceTask(data, mid, end, type);

            leftTask.fork();
            long rightResult = rightTask.compute();
            long leftResult = leftTask.join();

            return combine(leftResult, rightResult, type);
        }
    }

    private static long sequentialReduce(long[] data, int start, int end, AggregationType type) {
        long acc = (type == AggregationType.SUM) ? 0L :
                   (type == AggregationType.MAX) ? Long.MIN_VALUE : Long.MAX_VALUE;

        for (int i = start; i < end; i++) {
            acc = combine(acc, data[i], type);
        }
        return acc;
    }

    private static long combine(long a, long b, AggregationType type) {
        switch (type) {
            case SUM: return a + b;
            case MAX: return Math.max(a, b);
            case MIN: return Math.min(a, b);
            default: return a + b;
        }
    }

    public static long reduce(long[] data, AggregationType type) {
        return POOL.invoke(new ReduceTask(data, 0, data.length, type));
    }

    public static void display() {
        System.out.println("==================================================");
        System.out.println("  PARALLEL REDUCE: RAILWAY TELEMETRY AGGREGATION");
        System.out.println("==================================================");

        int recordCount = 2_000_000;
        System.out.printf("Generating %,d continuous passenger and telemetry records...%n", recordCount);

        long[] passengerData = new long[recordCount];
        Random rnd = new Random(101);
        for (int i = 0; i < recordCount; i++) {
            passengerData[i] = 100 + rnd.nextInt(900); // 100 to 1000 passengers per coach/service
        }

        System.out.println("Running Sequential Aggregation...");
        long tStartSeq = System.nanoTime();
        long seqSum = sequentialReduce(passengerData, 0, recordCount, AggregationType.SUM);
        long tSeq = System.nanoTime() - tStartSeq;

        System.out.println("Running Multi-Threaded Parallel Tree Reduction...");
        long tStartPar = System.nanoTime();
        long parSum = reduce(passengerData, AggregationType.SUM);
        long tPar = System.nanoTime() - tStartPar;

        long maxPax = reduce(passengerData, AggregationType.MAX);
        long minPax = reduce(passengerData, AggregationType.MIN);

        System.out.println();
        System.out.println("Aggregation Results:");
        System.out.printf("  Total Network Passengers : %,d pax%n", parSum);
        System.out.printf("  Peak Coach Passenger Load : %,d pax%n", maxPax);
        System.out.printf("  Min Coach Passenger Load  : %,d pax%n", minPax);
        System.out.printf("  Verification Match        : %s%n", (seqSum == parSum ? "PASSED" : "MISMATCH"));
        System.out.println();

        System.out.println("Parallel Performance Benchmark:");
        System.out.printf("  Sequential Execution Time : %.2f ms%n", tSeq / 1e6);
        System.out.printf("  Parallel Execution Time   : %.2f ms%n", tPar / 1e6);
        double speedup = (double) tSeq / tPar;
        System.out.printf("  Observed Speedup          : %.2fx (Active Pool Threads: %d)%n",
            speedup, POOL.getParallelism());
    }

    public static void main(String[] args) {
        display();
    }
}
