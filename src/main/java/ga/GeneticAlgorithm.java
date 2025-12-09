package ga;

import gui.ControlPanel;
import gui.PopulationPanel;
import java.util.Random;

public class GeneticAlgorithm {

  private Individual[] population;
  private int populationSize;
  private int chromosomeLength;

  private double globalMutationRate; // slider value
  private double crossoverRate;

  private Random rand = new Random();
  private int generation = 0;

  private ControlPanel controlPanel;

  public GeneticAlgorithm(
      int populationSize, int chromosomeLength, double mutationRate, double crossoverRate) {

    this.populationSize = populationSize;
    this.chromosomeLength = chromosomeLength;
    this.globalMutationRate = mutationRate;
    this.crossoverRate = crossoverRate;

    initPopulation();
  }

  // ============================
  // Getters / Setters
  // ============================
  public int getPopulationSize() {
    return populationSize;
  }

  public void setPopulationSize(int newSize) {
    this.populationSize = newSize;
    restart();
  }

  public double getMutationRate() {
    return globalMutationRate;
  }

  public void setMutationRate(double mutationRate) {
    this.globalMutationRate = mutationRate;
    for (Individual ind : population) ind.setMutationRate(mutationRate);
    updateMutationSlider();
  }

  public double getAverageMutationRate() {
    double total = 0;
    for (Individual ind : population) total += ind.getMutationRate();
    return total / populationSize;
  }

  public double getCurrentMutationRate() {
    return getAverageMutationRate();
  }

  public double getCrossoverRate() {
    return crossoverRate;
  }

  public void setCrossoverRate(double rate) {
    this.crossoverRate = rate;
  }

  public int getGeneration() {
    return generation;
  }

  public Individual[] getPopulation() {
    return population;
  }

  // ============================
  // Initialization
  // ============================
  private void initPopulation() {
    population = new Individual[populationSize];
    for (int i = 0; i < populationSize; i++) {
      Individual ind = new Individual(chromosomeLength); // all genes false
      ind.setMutationRate(globalMutationRate);
      // Don't evaluate fitness yet, starts at 0
      population[i] = ind;
    }
  }

  public double getAverageFitness() {
    double sum = 0;
    for (Individual ind : population) sum += ind.getFitness();
    return sum / populationSize;
  }

  public int getBestFitness() {
    int best = 0;
    for (Individual ind : population) best = Math.max(best, ind.getFitness());
    return best;
  }

  // ============================
  // Diversity Tracking
  // ============================
  public double getDiversity() {
    double totalDistance = 0;
    int n = population.length;
    for (int i = 0; i < n; i++) {
      boolean[] chr1 = population[i].getChromosome();
      for (int j = i + 1; j < n; j++) {
        boolean[] chr2 = population[j].getChromosome();
        int dist = 0;
        for (int k = 0; k < chromosomeLength; k++) if (chr1[k] != chr2[k]) dist++;
        totalDistance += dist;
      }
    }
    // normalize 0..1
    return totalDistance / ((n * (n - 1) / 2.0) * chromosomeLength);
  }

  // ============================
  // Evolution
  // ============================
  public void evolveOneGeneration(PopulationPanel panel) {

    Individual[] newPop = new Individual[populationSize];

    for (int i = 0; i < populationSize; i += 2) {

      Individual parent1 = tournamentSelection();
      Individual parent2 = tournamentSelection();

      Individual child1 = new Individual(chromosomeLength);
      Individual child2 = new Individual(chromosomeLength);

      boolean[] crossMask1 = new boolean[chromosomeLength];
      boolean[] crossMask2 = new boolean[chromosomeLength];

      boolean doCross = rand.nextDouble() < crossoverRate;

      // --- CROSSOVER ---
      if (doCross) {
        int point = rand.nextInt(chromosomeLength);
        for (int j = 0; j < chromosomeLength; j++) {
          if (j < point) {
            child1.setGene(j, parent1.getChromosome()[j]);
            child2.setGene(j, parent2.getChromosome()[j]);
          } else {
            child1.setGene(j, parent2.getChromosome()[j]);
            child2.setGene(j, parent1.getChromosome()[j]);
            crossMask1[j] = true;
            crossMask2[j] = true;
          }
        }
      } else {
        for (int j = 0; j < chromosomeLength; j++) {
          child1.setGene(j, parent1.getChromosome()[j]);
          child2.setGene(j, parent2.getChromosome()[j]);
        }
      }

      panel.highlightCrossover(i, crossMask1);
      if (i + 1 < populationSize) panel.highlightCrossover(i + 1, crossMask2);

      // --- SELF-ADAPTIVE MUTATION ---
      boolean[] mutMask1 = new boolean[chromosomeLength];
      boolean[] mutMask2 = new boolean[chromosomeLength];

      mutateSelfAdaptive(child1, mutMask1, parent1);
      mutateSelfAdaptive(child2, mutMask2, parent2);

      child1.evaluateFitness();
      child2.evaluateFitness();

      panel.highlightMutations(i, mutMask1);
      if (i + 1 < populationSize) panel.highlightMutations(i + 1, mutMask2);

      newPop[i] = child1;
      if (i + 1 < populationSize) newPop[i + 1] = child2;
    }

    population = newPop;
    generation++;

    // --- update slider automatically ---
    updateMutationSlider();
  }

  // ============================
  // Tournament Selection
  // ============================
  private Individual tournamentSelection() {
    Individual best = population[rand.nextInt(populationSize)];
    for (int i = 0; i < 2; i++) {
      Individual competitor = population[rand.nextInt(populationSize)];
      if (competitor.getFitness() > best.getFitness()) best = competitor;
    }
    return best;
  }

  // ============================
  // Self-Adaptive Mutation
  // ============================
  private void mutateSelfAdaptive(Individual child, boolean[] mask, Individual parent) {

    double parentRate = parent.getMutationRate();

    // ES-style log-normal mutation (no upper clamp)
    double tau = 0.1;
    double newRate = parentRate * Math.exp(rand.nextGaussian() * tau);
    if (newRate < 0.001) newRate = 0.001;

    child.setMutationRate(newRate);

    // mutate genes
    for (int i = 0; i < chromosomeLength; i++) {
      if (rand.nextDouble() < Math.min(newRate, 1.0)) { // treat >1 as 100%
        child.setGene(i, !child.getChromosome()[i]);
        mask[i] = true;
      }
    }
  }

  // ============================
  // Restart
  // ============================
  public void restart() {
    generation = 0;
    initPopulation();
    updateMutationSlider();
  }

  // ============================
  // UI Notification
  // ============================
  private void updateMutationSlider() {
    if (controlPanel != null) controlPanel.updateMutationSlider(this);
  }

  public void addControlPanel(ControlPanel controlPanel) {
    this.controlPanel = controlPanel;
  }
}
