package gui;

import ga.GeneticAlgorithm;

import javax.swing.*;
import java.awt.*;

public class StatsPanel extends JPanel {
    private JLabel generationLabel = new JLabel("Generation: 0");
    private JLabel avgFitnessLabel = new JLabel("Avg Fitness: 0");
    private JLabel bestFitnessLabel = new JLabel("Best Fitness: 0");
    private GeneticAlgorithm ga;

    public StatsPanel(GeneticAlgorithm ga) {
        this.ga = ga;
        setLayout(new GridLayout(3,1));
        setBorder(BorderFactory.createTitledBorder("Stats"));
        add(generationLabel);
        add(avgFitnessLabel);
        add(bestFitnessLabel);
    }

    public void refresh() {
        generationLabel.setText("Generation: " + ga.getGeneration());
        avgFitnessLabel.setText(String.format("Avg Fitness: %.2f", ga.getAverageFitness()));
        bestFitnessLabel.setText("Best Fitness: " + ga.getBestFitness());
    }
}
