package ga;

import java.util.Random;

public class Individual {
  private boolean[] chromosome;
  private int fitness;

  private double mutationRate;

  private static final Random rand = new Random();

  public Individual(int length) {
    chromosome = new boolean[length];

    for (int i = 0; i < length; i++) {
      chromosome[i] = false;
    }
    mutationRate = 0.05 + rand.nextDouble() * 0.20;
    fitness = 0; 
  }

  public Individual(boolean[] chromosome, double mutationRate) {
    this.chromosome = chromosome.clone();
    this.mutationRate = mutationRate;
  }

  public double getMutationRate() {
    return mutationRate;
  }

  public void setMutationRate(double mutationRate) {
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

  public void evaluateFitness() {
    fitness = 0;
    for (boolean gene : chromosome) {
      if (gene) fitness++;
    }
  }

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

  public Individual copy() {
    return new Individual(chromosome.clone(), mutationRate);
  }
}
