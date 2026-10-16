package com.railsync.randomparallel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Module 6 - Randomized and Parallel Algorithms
 * Reservoir Sampling (Algorithm R) for Continuous Railway Event Streams.
 * 
 * Selects an unbiased, uniform random sample of k items from an unbounded,
 * continuous stream of railway events (sensor telemetry, tap-in events, signal updates)
 * in a single pass using O(k) memory.
 * 
 * Theorem: At any step N >= k, every streamed event has an exact uniform
 * probability of k/N of being in the reservoir.
 */
public class ReservoirSampling<T> {

    public static class RailwayEvent {
        public final long timestamp;
        public final String eventType;
        public final String location;
        public final String details;

        public RailwayEvent(long timestamp, String eventType, String location, String details) {
            this.timestamp = timestamp;
            this.eventType = eventType;
            this.location = location;
            this.details = details;
        }

        @Override
        public String toString() {
            return String.format("[T+%04d ms] %-18s @ %-18s: %s",
                timestamp, eventType, location, details);
        }
    }

    private final int k;
    private final List<T> reservoir;
    private final Random random;
    private int count;

    public ReservoirSampling(int k) {
        if (k <= 0) throw new IllegalArgumentException("Sample size k must be positive");
        this.k = k;
        this.reservoir = new ArrayList<>(k);
        this.random = new Random();
        this.count = 0;
    }

    /**
     * Processes a single incoming event from the continuous stream.
     */
    public void processEvent(T event) {
        count++;
        if (reservoir.size() < k) {
            reservoir.add(event);
        } else {
            // Pick a random integer from [0, count - 1]
            int j = random.nextInt(count);
            if (j < k) {
                reservoir.set(j, event);
            }
        }
    }

    /**
     * Samples k elements from an iterable stream.
     */
    public List<T> sampleFromStream(Iterator<T> stream) {
        while (stream.hasNext()) {
            processEvent(stream.next());
        }
        return getReservoir();
    }

    public List<T> getReservoir() {
        return new ArrayList<>(reservoir);
    }

    public int getEventsProcessed() {
        return count;
    }

    public static void display() {
        System.out.println("==================================================");
        System.out.println("  RESERVOIR SAMPLING: CONTINUOUS EVENT STREAM");
        System.out.println("==================================================");

        int sampleSize = 5;
        ReservoirSampling<RailwayEvent> sampler = new ReservoirSampling<>(sampleSize);

        String[] eventTypes = {
            "TRACK_SENSOR_PASS", "SIGNAL_ASPECT_CHANGE", "PASSENGER_TAP_IN",
            "OVERHEAD_VOLT_SURGE", "WHEEL_TEMP_TELEMETRY", "DOOR_CYCLE_ALERT"
        };
        String[] locations = {
            "Secunderabad Jct", "Kazipet Jct", "Vijayawada Outer",
            "Guntur Platform 2", "Tenali Block 4", "Visakhapatnam Yard"
        };

        int totalStreamedEvents = 200;
        System.out.printf("Streaming %d continuous railway IoT and operational events...%n", totalStreamedEvents);

        Random rnd = new Random(42);
        for (int i = 1; i <= totalStreamedEvents; i++) {
            String type = eventTypes[rnd.nextInt(eventTypes.length)];
            String loc = locations[rnd.nextInt(locations.length)];
            String details = "Telemetry packet #" + i + " [Value: " + (50 + rnd.nextInt(250)) + " units]";
            RailwayEvent event = new RailwayEvent(i * 15L, type, loc, details);
            sampler.processEvent(event);
        }

        System.out.printf("Stream processing complete! Total events observed: %d%n", sampler.getEventsProcessed());
        System.out.printf("Uniform random sample of size k = %d maintained in memory:%n", sampleSize);
        System.out.println();

        List<RailwayEvent> sample = sampler.getReservoir();
        int idx = 1;
        for (RailwayEvent event : sample) {
            System.out.printf("  Sample #%d: %s%n", idx++, event);
        }

        System.out.println();
        System.out.printf("Mathematical Guarantee: Each event in the %d-event stream had an exact%n", totalStreamedEvents);
        System.out.printf("uniform inclusion probability of k/N = %d/%d = %.4f (%.2f%%).%n",
            sampleSize, totalStreamedEvents, (double) sampleSize / totalStreamedEvents,
            ((double) sampleSize / totalStreamedEvents) * 100);
        System.out.println("Memory Footprint: O(k) fixed space regardless of infinite stream duration.");
    }

    public static void main(String[] args) {
        display();
    }
}
