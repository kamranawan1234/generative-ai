package ga;

import static org.junit.jupiter.api.Assertions.*;

import gui.PopulationPanel;
import org.junit.jupiter.api.Test;

public class GeneticAlgorithmTest {

  @Test
  public void testInitPopulationAndEvolve() {
    GeneticAlgorithm ga = new GeneticAlgorithm(10, 5, 0.05, 0.1);
    assertNotNull(ga.getPopulation(), "Population should be initialized");
    assertEquals(10, ga.getPopulation().length, "Population size should match");
    assertEquals(0, ga.getGeneration(), "Initial generation should be 0");

    PopulationPanel panel = new PopulationPanel();
    ga.evolveOneGeneration(panel);
    assertEquals(1, ga.getGeneration(), "Generation should increment after evolveOneGeneration");
    assertEquals(10, ga.getPopulation().length, "Population size should remain same after evolve");
  }

  @Test
  public void testSetPopulationSizeAndRestart() {
    GeneticAlgorithm ga = new GeneticAlgorithm(8, 6, 0.02, 0.05);
    ga.setPopulationSize(12);
    assertEquals(12, ga.getPopulationSize(), "Population size getter should reflect new size");
    assertEquals(0, ga.getGeneration(), "Restart should reset generation to 0");
    assertEquals(12, ga.getPopulation().length, "Population array should match new size");
  }

  @Test
  public void testSetMutationRatePropagation() {
    GeneticAlgorithm ga = new GeneticAlgorithm(6, 4, 0.03, 0.1);
    ga.setMutationRate(0.2);
    for (Individual ind : ga.getPopulation()) {
      assertEquals(
          0.2, ind.getMutationRate(), 1e-9, "Individuals should receive new mutation rate");
    }
  }
}
