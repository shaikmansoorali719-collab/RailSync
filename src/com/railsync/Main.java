package com.railsync;

import java.util.List;
import java.util.Scanner;

import com.railsync.data.RailwayData;
import com.railsync.model.Train;

import com.railsync.string.NaiveSearch;
import com.railsync.string.KMP;
import com.railsync.string.ZFunction;
import com.railsync.string.RabinKarp;
import com.railsync.string.Trie;
import com.railsync.string.AhoCorasick;

import com.railsync.suffix.SuffixArray;
import com.railsync.suffix.SAIS;
import com.railsync.suffix.KasaiLCP;
import com.railsync.suffix.SuffixAutomaton;

import com.railsync.dp.EditDistance;
import com.railsync.dp.WagnerFischer;
import com.railsync.dp.NeedlemanWunsch;
import com.railsync.dp.SmithWaterman;
import com.railsync.dp.IntervalDP;
import com.railsync.dp.BitmaskDP;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        int choice;

        do {
            System.out.println();
            System.out.println("=================================");
            System.out.println("          RAILSYNC");
            System.out.println(" Intelligent Railway Operations");
            System.out.println("=================================");
            System.out.println("1. View Railway Data");
            System.out.println("2. String Algorithms");
            System.out.println("3. Suffix Structures");
            System.out.println("4. Advanced Dynamic Programming");
            System.out.println("5. Algorithm Analysis");
            System.out.println("6. Exit");
            System.out.println("---------------------------------");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    railwayDataMenu();
                    break;

                case 2:
                    stringAlgorithmMenu(sc);
                    break;

                case 3:
                    suffixStructureMenu(sc);
                    break;

                case 4:
                    dpMenu(sc);
                    break;

                case 5:
                    System.out.println();
                    System.out.println("=================================");
                    System.out.println("       ALGORITHM ANALYSIS");
                    System.out.println("=================================");
                    System.out.println("Complexity analysis will be added next.");
                    break;

                case 6:
                    System.out.println();
                    System.out.println("Thank you for using RailSync.");
                    break;

                default:
                    System.out.println();
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 6);

        sc.close();
    }

    public static void railwayDataMenu() {

        System.out.println();
        System.out.println("=================================");
        System.out.println("        RAILWAY DATA");
        System.out.println("=================================");

        System.out.println();
        System.out.println("========== TRAIN DATA ==========");

        List<Train> trains = RailwayData.loadTrains("data/trains.csv");

        for (Train train : trains) {
            System.out.println(train);
        }

        System.out.println();
        System.out.println("========== STATION DATA ==========");
        RailwayData.loadStations("data/stations.csv");

        System.out.println();
        System.out.println("========== PASSENGER DATA ==========");
        RailwayData.loadPassengers("data/passengers.csv");

        System.out.println();
        System.out.println("========== TICKET DATA ==========");
        RailwayData.loadTickets("data/tickets.csv");

        System.out.println();
        System.out.println("========== SERVICE DATA ==========");
        RailwayData.loadServices("data/services.csv");
    }

    public static void stringAlgorithmMenu(Scanner sc) {

        int choice;

        do {
            System.out.println();
            System.out.println("=================================");
            System.out.println("       STRING ALGORITHMS");
            System.out.println("=================================");
            System.out.println("1. Naive Search");
            System.out.println("2. KMP");
            System.out.println("3. Z-Function");
            System.out.println("4. Rabin-Karp");
            System.out.println("5. Trie");
            System.out.println("6. Aho-Corasick");
            System.out.println("7. Back");
            System.out.println("---------------------------------");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            switch (choice) {

                case 1:

                    sc.nextLine();

                    System.out.println();
                    System.out.println("========== NAIVE STRING SEARCH ==========");
                    System.out.print("Enter train name or pattern to search: ");

                    String pattern = sc.nextLine();

                    List<Train> trains =
                            RailwayData.loadTrains("data/trains.csv");

                    boolean found = false;

                    System.out.println();
                    System.out.println("Matching Trains:");
                    System.out.println("---------------------------------");

                    for (Train train : trains) {

                        int result = NaiveSearch.search(
                                train.getTrainName(),
                                pattern
                        );

                        if (result != -1) {
                            System.out.println(train);
                            found = true;
                        }
                    }

                    if (!found) {
                        System.out.println("No matching trains found.");
                    }

                    break;

                case 2:

                    sc.nextLine();

                    System.out.println();
                    System.out.println("========== KMP STRING SEARCH ==========");
                    System.out.print("Enter train name or pattern to search: ");

                    String kmpPattern = sc.nextLine();

                    List<Train> kmpTrains =
                            RailwayData.loadTrains("data/trains.csv");

                    boolean kmpFound = false;

                    System.out.println();
                    System.out.println("Matching Trains:");
                    System.out.println("---------------------------------");

                    for (Train train : kmpTrains) {

                        int result = KMP.search(
                                train.getTrainName(),
                                kmpPattern
                        );

                        if (result != -1) {
                            System.out.println(train);
                            kmpFound = true;
                        }
                    }

                    if (!kmpFound) {
                        System.out.println("No matching trains found.");
                    }

                    break;

                case 3:

                    sc.nextLine();

                    System.out.println();
                    System.out.println("========== Z-FUNCTION STRING SEARCH ==========");
                    System.out.print("Enter train name or pattern to search: ");

                    String zPattern = sc.nextLine();

                    List<Train> zTrains =
                            RailwayData.loadTrains("data/trains.csv");

                    boolean zFound = false;

                    System.out.println();
                    System.out.println("Matching Trains:");
                    System.out.println("---------------------------------");

                    for (Train train : zTrains) {

                        int result = ZFunction.search(
                                train.getTrainName(),
                                zPattern
                        );

                        if (result != -1) {
                            System.out.println(train);
                            zFound = true;
                        }
                    }

                    if (!zFound) {
                        System.out.println("No matching trains found.");
                    }

                    break;

                case 4:

                    sc.nextLine();

                    System.out.println();
                    System.out.println("========== RABIN-KARP STRING SEARCH ==========");
                    System.out.print("Enter train name or pattern to search: ");

                    String rkPattern = sc.nextLine();

                    List<Train> rkTrains =
                            RailwayData.loadTrains("data/trains.csv");

                    boolean rkFound = false;

                    System.out.println();
                    System.out.println("Matching Trains:");
                    System.out.println("---------------------------------");

                    for (Train train : rkTrains) {

                        int result = RabinKarp.search(
                                train.getTrainName(),
                                rkPattern
                        );

                        if (result != -1) {
                            System.out.println(train);
                            rkFound = true;
                        }
                    }

                    if (!rkFound) {
                        System.out.println("No matching trains found.");
                    }

                    break;

                case 5:

                    sc.nextLine();

                    System.out.println();
                    System.out.println("========== TRIE PREFIX SEARCH ==========");
                    System.out.print("Enter train name prefix: ");

                    String prefix = sc.nextLine();

                    List<Train> trieTrains =
                            RailwayData.loadTrains("data/trains.csv");

                    Trie trie = new Trie();

                    for (Train train : trieTrains) {
                        trie.insert(train.getTrainName());
                    }

                    List<String> matches =
                            trie.searchPrefix(prefix);

                    System.out.println();
                    System.out.println("Matching Trains:");
                    System.out.println("---------------------------------");

                    if (matches.isEmpty()) {

                        System.out.println("No matching trains found.");

                    } else {

                        for (String name : matches) {

                            for (Train train : trieTrains) {

                                if (train.getTrainName().equals(name)) {
                                    System.out.println(train);
                                }
                            }
                        }
                    }

                    break;

                case 6:

                    sc.nextLine();

                    System.out.println();
                    System.out.println("========== AHO-CORASICK SEARCH ==========");
                    System.out.print("Enter patterns separated by spaces: ");

                    String input = sc.nextLine();

                    String[] patterns = input.split("\\s+");

                    AhoCorasick aho = new AhoCorasick();

                    for (String p : patterns) {
                        aho.addPattern(p);
                    }

                    aho.buildFailureLinks();

                    List<Train> ahoTrains =
                            RailwayData.loadTrains("data/trains.csv");

                    boolean ahoFound = false;

                    System.out.println();
                    System.out.println("Matching Trains:");
                    System.out.println("---------------------------------");

                    for (Train train : ahoTrains) {

                        List<String> result =
                                aho.search(train.getTrainName());

                        if (!result.isEmpty()) {

                            System.out.println(train);
                            System.out.println(
                                    "Matched patterns: " + result
                            );

                            ahoFound = true;
                        }
                    }

                    if (!ahoFound) {
                        System.out.println("No matching trains found.");
                    }

                    break;

                case 7:

                    System.out.println();
                    System.out.println("Returning to main menu.");
                    break;

                default:

                    System.out.println();
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 7);
    }

    public static void suffixStructureMenu(Scanner sc) {

        int choice;

        do {
            System.out.println();
            System.out.println("=================================");
            System.out.println("        SUFFIX STRUCTURES");
            System.out.println("=================================");
            System.out.println("1. Suffix Array");
            System.out.println("2. SA-IS");
            System.out.println("3. LCP / Kasai");
            System.out.println("4. Suffix Automaton");
            System.out.println("5. Back");
            System.out.println("---------------------------------");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            switch (choice) {

            	case 1:

            		sc.nextLine();

            		System.out.println();
            		System.out.println("========== SUFFIX ARRAY ==========");
            		System.out.print(
            				"Enter railway service/document text: "
            				);

            		String suffixText = sc.nextLine();

            		if (suffixText.isEmpty()) {
            			System.out.println("Text cannot be empty.");
            			break;
            		}

            		SuffixArray suffixArray =
            				new SuffixArray(suffixText);

            		suffixArray.display();

            		System.out.println();
            		System.out.print(
            				"Enter pattern to search: "
            				);

            		String suffixPattern = sc.nextLine();

            		if (suffixPattern.isEmpty()) {
            			System.out.println("Pattern cannot be empty.");
            			break;
            		}

            		suffixArray.search(suffixPattern);

            		break;
                case 2:

                    sc.nextLine();

                    System.out.println();
                    System.out.println("========== SA-IS ==========");
                    System.out.print(
                            "Enter railway service/document text: "
                    );

                    String saisText = sc.nextLine();

                    if (saisText.isEmpty()) {
                        System.out.println("Text cannot be empty.");
                        break;
                    }

                    SAIS sais = new SAIS(saisText);

                    sais.display();

                    break;

                case 3:

                    sc.nextLine();

                    System.out.println();
                    System.out.println("========== LCP / KASAI ==========");
                    System.out.print(
                            "Enter railway service/document text: "
                    );

                    String kasaiText = sc.nextLine();

                    if (kasaiText.isEmpty()) {
                        System.out.println("Text cannot be empty.");
                        break;
                    }

                    SuffixArray kasaiSuffixArray =
                            new SuffixArray(kasaiText);

                    KasaiLCP kasai =
                            new KasaiLCP(
                                    kasaiText,
                                    kasaiSuffixArray.getSuffixArray()
                            );

                    kasai.display();

                    System.out.print(
                            "Enter minimum repeated-pattern length: "
                    );

                    int minimumLength = sc.nextInt();

                    if (minimumLength <= 0) {
                        System.out.println(
                                "Minimum length must be greater than 0."
                        );
                        break;
                    }

                    kasai.displayRepeatedPatterns(minimumLength);

                    break;

                case 4:

                    sc.nextLine();

                    System.out.println();
                    System.out.println("========== SUFFIX AUTOMATON ==========");
                    System.out.print(
                            "Enter railway service/document text: "
                    );

                    String automatonText = sc.nextLine();

                    if (automatonText.isEmpty()) {
                        System.out.println("Text cannot be empty.");
                        break;
                    }

                    SuffixAutomaton automaton =
                            new SuffixAutomaton(automatonText);

                    System.out.println();
                    System.out.println(
                            "Automaton states: " +
                            automaton.getStateCount()
                    );

                    System.out.print(
                            "Enter substring to search: "
                    );

                    String substring = sc.nextLine();

                    automaton.searchService(substring);

                    break;

                case 5:

                    System.out.println();
                    System.out.println("Returning to main menu.");
                    break;

                default:

                    System.out.println();
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 5);
    }

    public static void dpMenu(Scanner sc) {

        int choice;

        do {
            System.out.println();
            System.out.println("=================================");
            System.out.println(" ADVANCED DYNAMIC PROGRAMMING");
            System.out.println("=================================");
            System.out.println("1. Edit Distance");
            System.out.println("2. Wagner-Fischer");
            System.out.println("3. Needleman-Wunsch");
            System.out.println("4. Smith-Waterman");
            System.out.println("5. Interval DP");
            System.out.println("6. Bitmask DP");
            System.out.println("7. Back");
            System.out.println("---------------------------------");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            switch (choice) {

                case 1:

                    sc.nextLine();

                    System.out.println();
                    System.out.println("========== EDIT DISTANCE ==========");
                    System.out.print("Enter first string: ");

                    String first = sc.nextLine();

                    System.out.print("Enter second string: ");

                    String second = sc.nextLine();

                    int distance =
                            EditDistance.calculate(first, second);

                    System.out.println();
                    System.out.println(
                            "Edit Distance: " + distance
                    );

                    break;

                case 2:

                    sc.nextLine();

                    System.out.println();
                    System.out.println("========== WAGNER-FISCHER ==========");
                    System.out.print("Enter first string: ");

                    String wfFirst = sc.nextLine();

                    System.out.print("Enter second string: ");

                    String wfSecond = sc.nextLine();

                    int wfDistance =
                            WagnerFischer.calculate(
                                    wfFirst,
                                    wfSecond
                            );

                    System.out.println();
                    System.out.println(
                            "Wagner-Fischer Distance: " +
                            wfDistance
                    );

                    break;

                case 3:

                    sc.nextLine();

                    System.out.println();
                    System.out.println("========== NEEDLEMAN-WUNSCH ==========");
                    System.out.print("Enter first route sequence: ");

                    String nwFirst = sc.nextLine();

                    System.out.print("Enter second route sequence: ");

                    String nwSecond = sc.nextLine();

                    int nwScore =
                            NeedlemanWunsch.calculate(
                                    nwFirst,
                                    nwSecond
                            );

                    System.out.println();
                    System.out.println(
                            "Global Alignment Score: " +
                            nwScore
                    );

                    break;

                case 4:

                    sc.nextLine();

                    System.out.println();
                    System.out.println("========== SMITH-WATERMAN ==========");
                    System.out.print("Enter first sequence: ");

                    String swFirst = sc.nextLine();

                    System.out.print("Enter second sequence: ");

                    String swSecond = sc.nextLine();

                    int swScore =
                            SmithWaterman.calculate(
                                    swFirst,
                                    swSecond
                            );

                    System.out.println();
                    System.out.println(
                            "Local Alignment Score: " +
                            swScore
                    );

                    break;

                case 5:

                    System.out.println();
                    System.out.println("========== INTERVAL DP ==========");

                    int[] stationValues = {
                            10, 20, 30, 40
                    };

                    System.out.println("Station values:");

                    for (int value : stationValues) {
                        System.out.print(value + " ");
                    }

                    int intervalCost =
                            IntervalDP.minimumCost(
                                    stationValues
                            );

                    System.out.println();
                    System.out.println(
                            "Minimum Interval DP Cost: " +
                            intervalCost
                    );

                    break;

                case 6:

                    System.out.println();
                    System.out.println("========== BITMASK DP ==========");

                    int[][] distanceMatrix = {
                            {0, 10, 15, 20},
                            {10, 0, 35, 25},
                            {15, 35, 0, 30},
                            {20, 25, 30, 0}
                    };

                    int routeCost =
                            BitmaskDP.minimumRoute(
                                    distanceMatrix
                            );

                    System.out.println(
                            "Minimum Route Cost: " +
                            routeCost
                    );

                    break;

                case 7:

                    System.out.println();
                    System.out.println("Returning to main menu.");
                    break;

                default:

                    System.out.println();
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 7);
    }
}