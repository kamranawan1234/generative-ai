package ga;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import gui.ControlPanel;
import gui.PopulationPanel;
import java.util.Random;

/** Simple genetic algorithm orchestrator that manages a population of Individuals. */
public class GeneticAlgorithm {
  private Individual[] population;
  private int populationSize;
  private int chromosomeLength;
  private double globalMutationRate;
  private double crossoverRate;
  private Random rand = new Random();
  private int generation = 0;
  private transient ControlPanel controlPanel;

  /**
   * Create a new GeneticAlgorithm with the supplied parameters and initialize the population.
   *
   * @param populationSize number of individuals
   * @param chromosomeLength length of each individual's chromosome
   * @param mutationRate initial global mutation rate
   * @param crossoverRate crossover probability
   */
  public GeneticAlgorithm(
      int populationSize, int chromosomeLength, double mutationRate, double crossoverRate) {

    this.populationSize = populationSize;
    this.chromosomeLength = chromosomeLength;
    this.globalMutationRate = mutationRate;
    this.crossoverRate = crossoverRate;

    initPopulation();
  }

  /** Get the configured population size. */
  public int getPopulationSize() {
    return populationSize;
  }

  /**
   * Set a new population size and restart the population.
   *
   * @param newSize new population size
   */
  public void setPopulationSize(int newSize) {
    this.populationSize = newSize;
    restart();
  }

  /** Get the current global mutation rate. */
  public double getMutationRate() {
    return globalMutationRate;
  }

  /**
   * Set a new global mutation rate and propagate it to individuals.
   *
   * @param mutationRate new mutation rate
   */
  public void setMutationRate(double mutationRate) {
    this.globalMutationRate = mutationRate;
    for (Individual ind : population) ind.setMutationRate(mutationRate);
    updateMutationSlider();
  }

  /** Compute the average mutation rate across the population. */
  public double getAverageMutationRate() {
    double total = 0;
    for (Individual ind : population) total += ind.getMutationRate();
    return total / populationSize;
  }

  /** Alias for the current average mutation rate. */
  public double getCurrentMutationRate() {
    return getAverageMutationRate();
  }

  /** Get the current crossover probability. */
  public double getCrossoverRate() {
    return crossoverRate;
  }

  /**
   * Set a new crossover probability.
   *
   * @param rate new crossover probability
   */
  public void setCrossoverRate(double rate) {
    this.crossoverRate = rate;
  }

  /** Return the current generation counter. */
  public int getGeneration() {
    return generation;
  }

  /** Return the current population array. */
  public Individual[] getPopulation() {
    return (population == null) ? null : population.clone();
  }

  /** Initialize the population with fresh individuals using configured parameters. */
  private void initPopulation() {
    population = new Individual[populationSize];
    for (int i = 0; i < populationSize; i++) {
      Individual ind = new Individual(chromosomeLength);
      ind.setMutationRate(globalMutationRate);
      population[i] = ind;
    }
  }

  /** Compute the average fitness across the population. */
  public double getAverageFitness() {
    double sum = 0;
    for (Individual ind : population) sum += ind.getFitness();
    return sum / populationSize;
  }

  /** Return the best fitness value found in the population. */
  public int getBestFitness() {
    int best = 0;
    for (Individual ind : population) best = Math.max(best, ind.getFitness());
    return best;
  }

  /** Compute a simple diversity measure as normalized Hamming distance across the population. */
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

    return totalDistance / ((n * (n - 1) / 2.0) * chromosomeLength);
  }

  /**
   * Evolve the population by one generation using tournament selection, crossover, and mutation.
   * The PopulationPanel is used to visualize crossovers and mutations.
   *
   * @param panel visualization panel used to highlight events
   */
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

    updateMutationSlider();
  }

  /** Select an individual using a small tournament and return the winner. */
  private Individual tournamentSelection() {
    Individual best = population[rand.nextInt(populationSize)];
    for (int i = 0; i < 2; i++) {
      Individual competitor = population[rand.nextInt(populationSize)];
      if (competitor.getFitness() > best.getFitness()) best = competitor;
    }
    return best;
  }

  /**
   * Apply self-adaptive mutation to a child using the parent's mutation rate as a base. The mask
   * array will be set to true for positions that changed.
   */
  private void mutateSelfAdaptive(Individual child, boolean[] mask, Individual parent) {

    double parentRate = parent.getMutationRate();

    double tau = 0.1;
    double newRate = parentRate * Math.exp(rand.nextGaussian() * tau);
    if (newRate < 0.001) newRate = 0.001;

    child.setMutationRate(newRate);

    for (int i = 0; i < chromosomeLength; i++) {
      if (rand.nextDouble() < Math.min(newRate, 1.0)) {
        child.setGene(i, !child.getChromosome()[i]);
        mask[i] = true;
      }
    }
  }

  /** Restart the GA by reinitializing the population and resetting the generation counter. */
  public void restart() {
    generation = 0;
    initPopulation();
    updateMutationSlider();
  }

  /** Propagate mutation rate updates to the control panel UI if present. */
  private void updateMutationSlider() {
    if (controlPanel != null) controlPanel.updateMutationSlider(this);
  }

  /**
   * Attach a ControlPanel to this GA instance for UI synchronization.
   *
   * @param controlPanel control panel to attach
   */
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  public void addControlPanel(ControlPanel controlPanel) {
    // Store reference to UI control panel; marked transient to avoid exposing internal
    // state during (de)serialization and to indicate this is a UI-only binding.
    this.controlPanel = controlPanel;
  }
}
