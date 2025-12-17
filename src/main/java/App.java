import ga.GeneticAlgorithm;
import gui.*;
import javax.swing.*;

/**
 * Application entry point for the Generative AI demo.
 *
 * This class sets up a simple Swing UI that drives a minimal genetic algorithm simulation. The
 * main responsibilities are:
 * - Create and configure the GeneticAlgorithm instance 
 * - Create UI panels (population view, stats, fitness history)
 * - Create a ControlPanel wiring start/stop/step/restart callbacks to the GA 
 * - Create the MainWindow and show the UI on the Event Dispatch Thread (EDT)
 *
 * The UI callbacks update the population panel and statistics after evolving or restarting the
 * GA.
 */
public class App {
  /**
   * Program entry. Initializes the UI and the genetic algorithm on the Swing Event Dispatch Thread.
   *
   * @param args command-line arguments (not used)
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(
        () -> {
          int populationSize = 20;
          int chromosomeLength = 50;
          double mutationRate = 0.1;
          double crossoverRate = 0.1;

          GeneticAlgorithm ga =
              new GeneticAlgorithm(populationSize, chromosomeLength, mutationRate, crossoverRate);

          PopulationPanel popPanel = new PopulationPanel();
          popPanel.setPopulation(ga.getPopulation());

          StatsPanel statsPanel = new StatsPanel(ga);
          FitnessHistoryPanel histPanel = new FitnessHistoryPanel();

          final Timer[] timer = new Timer[1];

          ControlPanel controlPanel =
              new ControlPanel(
                  ga,
                  () -> {
                    if (timer[0] == null) {
                      timer[0] =
                          new Timer(
                              500,
                              e -> {
                                ga.evolveOneGeneration(popPanel);
                                popPanel.setPopulation(ga.getPopulation());
                                statsPanel.refresh();
                                histPanel.addFitness(ga.getAverageFitness());
                              });
                      timer[0].start();
                    }
                  },
                  () -> {
                    if (timer[0] != null) {
                      timer[0].stop();
                      timer[0] = null;
                    }
                  },
                  () -> {
                    ga.evolveOneGeneration(popPanel);
                    popPanel.setPopulation(ga.getPopulation());
                    statsPanel.refresh();
                    histPanel.addFitness(ga.getAverageFitness());
                  },
                  () -> {
                    ga.restart();
                    popPanel.setPopulation(ga.getPopulation());
                    statsPanel.refresh();
                    histPanel.clear();
                  });

          ga.addControlPanel(controlPanel);

          new MainWindow(ga, popPanel, statsPanel, histPanel, controlPanel);
        });
  }
}
