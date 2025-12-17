package ga;

import java.util.Random;

/** Simple individual representation containing a boolean chromosome and mutation rate. */
public class Individual {
  private boolean[] chromosome;
  private int fitness;
  private double mutationRate;
  private static final Random rand = new Random();

  /**
   * Create an individual with an empty chromosome of the given length.
   *
   * @param length chromosome length
   */
  public Individual(int length) {
    chromosome = new boolean[length];

    for (int i = 0; i < length; i++) {
      chromosome[i] = false;
    }
    mutationRate = 0.05 + rand.nextDouble() * 0.20;
    fitness = 0;
  }

  /**
   * Create an individual from an existing chromosome and mutation rate.
   *
   * @param chromosome boolean array representing genes
   * @param mutationRate initial mutation rate for the individual
   */
  public Individual(boolean[] chromosome, double mutationRate) {
    this.chromosome = chromosome.clone();
    this.mutationRate = mutationRate;
  }

  /** Get the individual's mutation rate. */
  public double getMutationRate() {
    return mutationRate;
  }

  /**
   * Set the mutation rate if positive.
   *
   * @param mutationRate new mutation rate
   */
  public void setMutationRate(double mutationRate) {
    if (mutationRate > 0) this.mutationRate = mutationRate;
  }

  /** Return the chromosome array reference. */
  public boolean[] getChromosome() {
    return chromosome.clone();
  }

  /**
   * Set a single gene in the chromosome.
   *
   * @param index gene index
   * @param value gene value
   */
  public void setGene(int index, boolean value) {
    chromosome[index] = value;
  }

  /** Get the cached fitness value. */
  public int getFitness() {
    return fitness;
  }

  /** Evaluate and cache the fitness for this individual. */
  public void evaluateFitness() {
    fitness = 0;
    for (boolean gene : chromosome) {
      if (gene) fitness++;
    }
  }

  /** Mutate the chromosome using an evolution strategies style self-adaptive mutation rate. */
  public void mutateES() {
    int n = chromosome.length;

    double tau = 1.0 / Math.sqrt(2 * Math.sqrt(n));
    double tau2 = 1.0 / Math.sqrt(2 * n);

    mutationRate *= Math.exp(tau * rand.nextGaussian() + tau2 * rand.nextGaussian());

    if (mutationRate < 0.001) mutationRate = 0.001;

    for (int i = 0; i < n; i++) {
      if (rand.nextDouble() < Math.min(mutationRate, 1.0)) {
        chromosome[i] = !chromosome[i];
      }
    }
  }

  /**
   * Create a copy of this individual.
   *
   * @return cloned individual instance
   */
  public Individual copy() {
    return new Individual(chromosome.clone(), mutationRate);
  }
}
