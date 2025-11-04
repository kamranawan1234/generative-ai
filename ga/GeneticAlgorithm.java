package ga;

import gui.PopulationPanel;

import java.util.Random;

public class GeneticAlgorithm {
    private Individual[] population;
    private int populationSize;
    private int chromosomeLength;
    private double mutationRate;
    private double crossoverRate;
    private Random rand = new Random();
    private int generation = 0;

    public GeneticAlgorithm(int populationSize, int chromosomeLength, double mutationRate, double crossoverRate) {
        this.populationSize = populationSize;
        this.chromosomeLength = chromosomeLength;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        initPopulation();
    }

    private void initPopulation() {
        population = new Individual[populationSize];
        for (int i = 0; i < populationSize; i++) {
            Individual ind = new Individual(chromosomeLength);
            for (int j = 0; j < chromosomeLength; j++) {
                ind.setGene(j, rand.nextBoolean());
            }
            ind.evaluateFitness();
            population[i] = ind;
        }
    }

    public Individual[] getPopulation() {
        return population;
    }

    public int getGeneration() {
        return generation;
    }

    public double getAverageFitness() {
        double sum = 0;
        for (Individual ind : population) sum += ind.getFitness();
        return sum / populationSize;
    }

    public int getBestFitness() {
        int best = 0;
        for (Individual ind : population) if (ind.getFitness() > best) best = ind.getFitness();
        return best;
    }

    public void evolveOneGeneration(PopulationPanel panel) {
        Individual[] newPop = new Individual[populationSize];
for (int i = 0; i < populationSize; i += 2) {
    Individual parent1 = tournamentSelection();
    Individual parent2 = tournamentSelection();

    Individual child1 = new Individual(chromosomeLength);
    Individual child2 = new Individual(chromosomeLength);

    boolean doCrossover = rand.nextDouble() < crossoverRate;

    boolean[] crossoverMask1 = new boolean[chromosomeLength];
    boolean[] crossoverMask2 = new boolean[chromosomeLength];

    if (doCrossover) {
        int crossPoint = rand.nextInt(chromosomeLength);
        for (int j = 0; j < chromosomeLength; j++) {
            if (j < crossPoint) {
                child1.setGene(j, parent1.getChromosome()[j]);
                child2.setGene(j, parent2.getChromosome()[j]);
            } else {
                child1.setGene(j, parent2.getChromosome()[j]);
                child2.setGene(j, parent1.getChromosome()[j]);
                crossoverMask1[j] = true;
                crossoverMask2[j] = true;
            }
        }
    } else {
        // No crossover: children are copies of parents
        for (int j = 0; j < chromosomeLength; j++) {
            child1.setGene(j, parent1.getChromosome()[j]);
            child2.setGene(j, parent2.getChromosome()[j]);
        }
    }

    // Highlight in GUI
    panel.highlightCrossover(i, crossoverMask1);
    if (i + 1 < populationSize) panel.highlightCrossover(i + 1, crossoverMask2);

    boolean[] mutationMask1 = new boolean[chromosomeLength];
    boolean[] mutationMask2 = new boolean[chromosomeLength];
    mutate(child1, mutationMask1);
    mutate(child2, mutationMask2);

    child1.evaluateFitness();
    child2.evaluateFitness();

    panel.highlightMutations(i, mutationMask1);
    if (i + 1 < populationSize) panel.highlightMutations(i + 1, mutationMask2);

    newPop[i] = child1;
    if (i + 1 < populationSize) newPop[i + 1] = child2;
}

        population = newPop;
        generation++;
    }

    private Individual tournamentSelection() {
        Individual best = population[rand.nextInt(populationSize)];
        for (int i = 0; i < 2; i++) {
            Individual competitor = population[rand.nextInt(populationSize)];
            if (competitor.getFitness() > best.getFitness()) best = competitor;
        }
        return best;
    }

    private void mutate(Individual ind, boolean[] mask) {
        for (int i = 0; i < chromosomeLength; i++) {
            if (rand.nextDouble() < mutationRate) {
                ind.setGene(i, !ind.getChromosome()[i]);
                mask[i] = true;
            }
        }
    }

    public void restart() {
        generation = 0;
        initPopulation();
    }

    // Getter/Setter for sliders
    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public void setCrossoverRate(double crossoverRate) {
        this.crossoverRate = crossoverRate;
    }
}
