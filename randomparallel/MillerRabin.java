package com.railsync.randomparallel;

import java.math.BigInteger;
import java.util.Random;

/**
 * Module 6 - Randomized and Parallel Algorithms
 * Miller-Rabin Probabilistic Primality Test for Large Numerical Processing.
 * 
 * Determines whether a large integer is prime or composite in polynomial time
 * O(k * log^3(n)).
 * 
 * Railway Application:
 * - Cryptographic verification of digital ticketing PNR barcodes and QR signatures.
 * - Secure communication token generation for railway interlock signaling.
 * - Selecting large prime moduli for collision-resistant railway hash tables.
 */
public class MillerRabin {

    private static final Random random = new Random();

    /**
     * Performs Miller-Rabin primality test with k rounds.
     * Error probability for composite numbers <= (1/4)^k.
     */
    public static boolean isProbablePrime(long n, int k) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0) return false;

        // Write n - 1 as 2^s * d, where d is odd
        long d = n - 1;
        int s = 0;
        while (d % 2 == 0) {
            d /= 2;
            s++;
        }

        // Run k rounds of witness tests
        for (int i = 0; i < k; i++) {
            // Pick random base a in [2, n - 2]
            long a = 2 + (long) (random.nextDouble() * (n - 3));
            if (!checkWitness(a, d, s, n)) {
                return false; // Composite
            }
        }
        return true; // Probably prime
    }

    private static boolean checkWitness(long a, long d, int s, long n) {
        long x = modularExponentiation(a, d, n);

        if (x == 1 || x == n - 1) {
            return true;
        }

        for (int r = 1; r < s; r++) {
            x = multiplyMod(x, x, n);
            if (x == n - 1) {
                return true;
            }
            if (x == 1) {
                return false;
            }
        }

        return false;
    }

    /**
     * Modular exponentiation: (base^exp) % mod using binary exponentiation.
     */
    public static long modularExponentiation(long base, long exp, long mod) {
        long result = 1;
        base = base % mod;

        while (exp > 0) {
            if ((exp & 1) == 1) {
                result = multiplyMod(result, base, mod);
            }
            base = multiplyMod(base, base, mod);
            exp >>= 1;
        }
        return result;
    }

    /**
     * Multiplies two 64-bit numbers modulo mod avoiding overflow.
     */
    private static long multiplyMod(long a, long b, long mod) {
        return BigInteger.valueOf(a)
                .multiply(BigInteger.valueOf(b))
                .mod(BigInteger.valueOf(mod))
                .longValue();
    }

    public static void display() {
        System.out.println("==================================================");
        System.out.println("  MILLER-RABIN PRIMALITY TEST (LARGE NUMERICAL)");
        System.out.println("==================================================");

        int rounds = 20; // Error bound: (1/4)^20 ≈ 9.1 x 10^-13
        System.out.printf("Testing numerical values with k = %d independent witness rounds...%n", rounds);
        System.out.println("Theoretical Error Bound <= (1/4)^20 ≈ 9.09 x 10^-13");
        System.out.println();

        long[] testCandidates = {
            1000000007L,       // Railway Hash Prime (10^9 + 7)
            2147483647L,       // 8th Mersenne Prime (2^31 - 1)
            1000000009L,       // Large Prime
            561L,              // Smallest Carmichael Number (composite, defeats Fermat test)
            1105L,             // Carmichael Number (composite)
            99999999977L,      // Very large prime for cryptographic booking token
            1270112702L        // Composite booking code
        };

        for (long num : testCandidates) {
            boolean isPrime = isProbablePrime(num, rounds);
            System.out.printf("  Candidate: %-15d | Result: %-16s | Confidence: > 99.9999999999%%%n",
                num, isPrime ? "PRIME [VALID]" : "COMPOSITE [INVALID]");
        }

        System.out.println();
        System.out.println("Carmichael Number Detection Demonstration:");
        System.out.println("  Carmichael numbers (e.g. 561 = 3 * 11 * 17) fool Fermat's test a^(n-1) = 1 (mod n)");
        System.out.println("  Miller-Rabin successfully exposes 561 as COMPOSITE by checking non-trivial roots of 1.");
    }

    public static void main(String[] args) {
        display();
    }
}
