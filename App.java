import ga.GeneticAlgorithm;
import gui.*;

import javax.swing.*;

public class App
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() ->
        {
            int populationSize = 20;
            int chromosomeLength = 20;
            double mutationRate = 0.1;
            double crossoverRate = 0.75;

            GeneticAlgorithm ga = new GeneticAlgorithm(populationSize, chromosomeLength, mutationRate, crossoverRate);

            PopulationPanel popPanel = new PopulationPanel();
            popPanel.setPopulation(ga.getPopulation());

            StatsPanel statsPanel = new StatsPanel(ga);
            FitnessHistoryPanel histPanel = new FitnessHistoryPanel();

            final Timer[] timer = new Timer[1];

            ControlPanel controlPanel = new ControlPanel(
                    ga,
                    () ->
                    { // start
                        if (timer[0] == null)
                        {
                            timer[0] = new Timer(500, e ->
                            {
                                ga.evolveOneGeneration(popPanel);
                                popPanel.setPopulation(ga.getPopulation());
                                statsPanel.refresh();
                                histPanel.addFitness(ga.getAverageFitness());
                            });
                            timer[0].start();
                        }
                    },
                    () ->
                    { // stop
                        if (timer[0] != null)
                        {
                            timer[0].stop();
                            timer[0] = null;
                        }
                    },
                    () ->
                    { // step
                        ga.evolveOneGeneration(popPanel);
                        popPanel.setPopulation(ga.getPopulation());
                        statsPanel.refresh();
                        histPanel.addFitness(ga.getAverageFitness());
                    },
                    () ->
                    { // restart
                        ga.restart();
                        popPanel.setPopulation(ga.getPopulation());
                        statsPanel.refresh();
                        histPanel.clear();
                    }
            );

            ga.addControlPanel(controlPanel);

            new MainWindow(ga, popPanel, statsPanel, histPanel, controlPanel);
        });
    }
}
