package ga;

import java.util.Random;

public class Individual {
    private boolean[] chromosome;
    private int fitness;
    private double mutationRate; // Mutation rate for the individual
    private Random rand = new Random();

    public Individual(int length) {
        chromosome = new boolean[length];
        this.mutationRate = 0.05 + rand.nextDouble() * 0.25; // Random mutation rate between 0.05 and 0.30
    }

    // Getter and Setter for Mutation Rate
    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    // Getter for Chromosome
    public boolean[] getChromosome() {
        return chromosome;
    }

    // Setter for a gene in the chromosome
    public void setGene(int index, boolean value) {
        chromosome[index] = value;
    }

    // Fitness Evaluation (counts the number of true genes in the chromosome)
    public int getFitness() {
        return fitness;
    }

    // Evaluate the fitness of the individual
    public void evaluateFitness() {
        fitness = 0;
        for (boolean gene : chromosome) {
            if (gene) fitness++;
        }
    }

    // Mutate the individual's chromosome based on its mutation rate
    public void mutate() {
        for (int i = 0; i < chromosome.length; i++) {
            if (rand.nextDouble() < mutationRate)  // If random number is less than the mutation rate, flip the gene
            {
                chromosome[i] = !chromosome[i];
            }
        }
    }
}
