package com.railsync.string;

import java.util.List;

import com.railsync.data.RailwayData;
import com.railsync.model.Train;

public class NaiveSearch {

    public static int search(String text, String pattern) {

        int n = text.length();
        int m = pattern.length();

        for (int i = 0; i <= n - m; i++) {

            int j = 0;

            while (j < m && text.charAt(i + j) == pattern.charAt(j)) {
                j++;
            }

            if (j == m) {
                return i;
            }
        }

        return -1;
    }

    public static void main(String[] args) {

        String pattern = "Express";

        List<Train> trains = RailwayData.loadTrains("data/trains.csv");

        System.out.println("========== NAIVE SEARCH ==========");
        System.out.println("Search Pattern: " + pattern);
        System.out.println();
        System.out.println("Matching Trains:");
        System.out.println("------------------------------");

        for (Train train : trains) {

            int result = search(train.getTrainName(), pattern);

            if (result != -1) {
                System.out.println(train);
            }
        }
    }
}