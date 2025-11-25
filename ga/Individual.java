package ga;

import java.util.Random;

public class Individual {
    private boolean[] chromosome;
    private int fitness;

    // Each individual carries its own mutation rate
    private double mutationRate;

    private static final Random rand = new Random();

public Individual(int length) {
    chromosome = new boolean[length];
    // All false by default
    for (int i = 0; i < length; i++) {
        chromosome[i] = false;
    }
    mutationRate = 0.05 + rand.nextDouble() * 0.20;
    fitness = 0; // start fitness at 0
}


    public Individual(boolean[] chromosome, double mutationRate) {
        this.chromosome = chromosome.clone();
        this.mutationRate = mutationRate;
    }

    // --- Getters / Setters ---
    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        // Optional: keep it positive, but allow large values
        if (mutationRate > 0) this.mutationRate = mutationRate;
    }

    public boolean[] getChromosome() {
        return chromosome;
    }

    public void setGene(int index, boolean value) {
        chromosome[index] = value;
    }

    public int getFitness() {
        return fitness;
    }

    // --- Evaluate fitness (number of true bits) ---
    public void evaluateFitness() {
        fitness = 0;
        for (boolean gene : chromosome) {
            if (gene) fitness++;
        }
    }

    // --- Self-adaptive mutation (ES-style) ---
    public void mutateES() {
    int n = chromosome.length;

    // ES-style parameters
    double tau  = 1.0 / Math.sqrt(2 * Math.sqrt(n));
    double tau2 = 1.0 / Math.sqrt(2 * n);

    // Evolve mutation rate (log-normal)
    mutationRate *= Math.exp(tau * rand.nextGaussian() + tau2 * rand.nextGaussian());

    // Only enforce minimum (avoid collapse to zero)
    if (mutationRate < 0.001) mutationRate = 0.001;

    // Mutate genes
    for (int i = 0; i < n; i++) {
        // If mutationRate > 1.0, treat as 100% chance to flip
        if (rand.nextDouble() < Math.min(mutationRate, 1.0)) {
            chromosome[i] = !chromosome[i];
        }
    }
}


    // --- Clone ---
    public Individual copy() {
        return new Individual(chromosome.clone(), mutationRate);
    }
}
