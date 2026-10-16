package com.railsync.randomparallel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.railsync.model.Train;

/**
 * Module 6 - Randomized and Parallel Algorithms
 * Randomized QuickSort for Ranking Railway Records.
 * 
 * Uses random pivot selection to guarantee expected O(n log n) running time,
 * completely eliminating worst-case O(n^2) degradation on pre-sorted or
 * reverse-sorted railway timetables.
 */
public class RandomizedQuickSort {

    private static final Random random = new Random();

    public static <T> void sort(List<T> list, Comparator<T> comparator) {
        if (list == null || list.size() <= 1) return;
        quickSort(list, 0, list.size() - 1, comparator);
    }

    private static <T> void quickSort(List<T> list, int low, int high, Comparator<T> comparator) {
        if (low < high) {
            int pivotIndex = partition(list, low, high, comparator);
            quickSort(list, low, pivotIndex - 1, comparator);
            quickSort(list, pivotIndex + 1, high, comparator);
        }
    }

    private static <T> int partition(List<T> list, int low, int high, Comparator<T> comparator) {
        // Randomized pivot selection: pick uniform random index in [low, high]
        int randomPivotIdx = low + random.nextInt(high - low + 1);
        Collections.swap(list, randomPivotIdx, high);

        T pivot = list.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (comparator.compare(list.get(j), pivot) <= 0) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, high);
        return i + 1;
    }

    public static void display() {
        System.out.println("==================================================");
        System.out.println("  RANDOMIZED QUICKSORT: TRAIN CAPACITY & SCHEDULE");
        System.out.println("==================================================");

        List<Train> trains = new ArrayList<>(List.of(
            new Train("12702", "Godavari Express", "Hyderabad", "Visakhapatnam", 1500),
            new Train("12704", "Intercity Express", "Vijayawada", "Guntur", 900),
            new Train("12701", "Golconda Express", "Secunderabad", "Guntur", 1200),
            new Train("12705", "Krishna Express", "Guntur", "Hyderabad", 1100),
            new Train("12703", "Satavahana Express", "Secunderabad", "Vijayawada", 1000)
        ));

        System.out.println("Before Sorting (Random Arrival Order):");
        for (Train t : trains) {
            System.out.println("  " + t);
        }

        // Sort by passenger capacity descending (high demand trains first)
        sort(trains, (t1, t2) -> Integer.compare(t2.getCapacity(), t1.getCapacity()));

        System.out.println();
        System.out.println("After Randomized QuickSort (Ranked by Capacity Descending):");
        int rank = 1;
        for (Train t : trains) {
            System.out.printf("  Rank %d: %-25s | Capacity: %d pax%n", rank++, t.getTrainName(), t.getCapacity());
        }

        System.out.println();
        System.out.println("Worst-case Quadratic O(n^2) Avoidance:");
        System.out.println("  Deterministic QuickSort on already-sorted timetables degrades to O(n^2).");
        System.out.println("  Randomized QuickSort guarantees O(n log n) expected time regardless of input ordering.");
    }

    public static void main(String[] args) {
        display();
    }
}
