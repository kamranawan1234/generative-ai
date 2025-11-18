package ga;

import gui.PopulationPanel;
import gui.ControlPanel;

import java.util.Random;

public class GeneticAlgorithm {
    private Individual[] population;
    private int populationSize;
    private int chromosomeLength;
    private double crossoverRate;
    private double mutationRate;  // Global mutation rate
    private Random rand = new Random();
    private int generation = 0;
    private ControlPanel controlPanel;  // Reference to ControlPanel to notify for updates

    public GeneticAlgorithm(int populationSize, int chromosomeLength, double mutationRate, double crossoverRate) {
        this.populationSize = populationSize;
        this.chromosomeLength = chromosomeLength;
        this.mutationRate = mutationRate;  // Initialize the global mutation rate
        this.crossoverRate = crossoverRate;
        initPopulation();
    }

    // ===== POPULATION SIZE CONTROL =====
    public int getPopulationSize() { return populationSize; }

    public void setPopulationSize(int newSize) {
        this.populationSize = newSize;
        restart();
    }

    // ===== MUTATION RATE CONTROL =====
    public double getMutationRate() { return mutationRate; }

    public double getCurrentMutationRate() {
        // Calculate the average mutation rate from all individuals
        double sum = 0;
        for (Individual individual : population) {
            sum += individual.getMutationRate();
        }
        return sum / populationSize;  // Return average mutation rate
    }

    public double getAverageMutationRate() {
        double totalMutationRate = 0.0;
        for (Individual individual : population) {
            totalMutationRate += individual.getMutationRate();  // Sum up all mutation rates
        }
        return totalMutationRate / populationSize;  // Return the average mutation rate
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
        // Optionally update each individual mutation rate too
        for (Individual individual : population) {
            individual.setMutationRate(mutationRate);  // Sync the global mutation rate with each individual
        }
        // Notify ControlPanel to update the slider when the mutation rate is changed
        updateMutationRateAndNotify();
    }

    // ===== INITIALIZATION =====
    private void initPopulation() {
        population = new Individual[populationSize];

        for (int i = 0; i < populationSize; i++) {
            Individual ind = new Individual(chromosomeLength);

            for (int j = 0; j < chromosomeLength; j++) {
                ind.setGene(j, rand.nextBoolean());
            }

            // Assign each individual the global mutation rate initially
            ind.setMutationRate(mutationRate);

            ind.evaluateFitness();
            population[i] = ind;
        }
    }

    public Individual[] getPopulation() { return population; }
    public int getGeneration() { return generation; }

    public double getAverageFitness() {
        double sum = 0;
        for (Individual ind : population) sum += ind.getFitness();
        return sum / populationSize;
    }

    public int getBestFitness() {
        int best = 0;
        for (Individual ind : population)
            if (ind.getFitness() > best) best = ind.getFitness();
        return best;
    }

    // ===== EVOLUTION =====
    public void evolveOneGeneration(PopulationPanel panel) {
        Individual[] newPop = new Individual[populationSize];

        for (int i = 0; i < populationSize; i += 2) {
            Individual parent1 = tournamentSelection();
            Individual parent2 = tournamentSelection();

            Individual child1 = new Individual(chromosomeLength);
            Individual child2 = new Individual(chromosomeLength);

            boolean doCross = rand.nextDouble() < crossoverRate;

            boolean[] crossMask1 = new boolean[chromosomeLength];
            boolean[] crossMask2 = new boolean[chromosomeLength];

            if (doCross) {
                int crossPoint = rand.nextInt(chromosomeLength);

                for (int j = 0; j < chromosomeLength; j++) {
                    if (j < crossPoint) {
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

            // ===== SELF-ADAPTIVE MUTATION =====
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
    }

    // ===== TOURNAMENT SELECTION =====
    private Individual tournamentSelection() {
        Individual best = population[rand.nextInt(populationSize)];
        for (int i = 0; i < 2; i++) {
            Individual competitor = population[rand.nextInt(populationSize)];
            if (competitor.getFitness() > best.getFitness())
                best = competitor;
        }
        return best;
    }

    // ===== SELF-ADAPTIVE MUTATION =====
    private void mutateSelfAdaptive(Individual child, boolean[] mask, Individual parent) {
        // Mutation rate itself mutates (log-normal)
        double currentRate = parent.getMutationRate();
        double newRate = currentRate * Math.exp(rand.nextGaussian() * 0.1);

        newRate = Math.max(0.001, Math.min(0.5, newRate)); // clamp
        child.setMutationRate(newRate);

        // Notify ControlPanel to update the slider when mutation rate changes
        updateMutationRateAndNotify();

        // Mutate genes using child's new mutation rate
        for (int i = 0; i < chromosomeLength; i++) {
            if (rand.nextDouble() < newRate) {
                child.setGene(i, !child.getChromosome()[i]);
                mask[i] = true;
            }
        }
    }

    // ===== RESET =====
    public void restart() {
        generation = 0;
        initPopulation();
    }

    // Notify ControlPanel to update mutation rate slider when it changes
    public void updateMutationRateAndNotify() {
        System.out.println(controlPanel);
        if (controlPanel != null) {
            controlPanel.updateMutationSlider(this);  // This will update the slider in real time
        }
    }

    // Setter for ControlPanel to link it to the GeneticAlgorithm
    public void addControlPanel(ControlPanel controlPanel) {
        this.controlPanel = controlPanel;
    }

    // crossoverRate still comes from your slider
    public void setCrossoverRate(double rate) { this.crossoverRate = rate; }
    public double getCrossoverRate() { return crossoverRate; }
}
