package gui;

import ga.GeneticAlgorithm;

import javax.swing.*;
import java.awt.*;

public class StatsPanel extends JPanel
{
    // DARK MODE PATCH START
    private JLabel generationLabel = new JLabel("Generation: 0");
    private JLabel avgFitnessLabel = new JLabel("Avg Fitness: 0");
    private JLabel bestFitnessLabel = new JLabel("Best Fitness: 0");
    private GeneticAlgorithm ga;

    public StatsPanel(GeneticAlgorithm ga)
    {
        this.ga = ga;
        setLayout(new GridLayout(1,3,10,0));
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(60,60,60)), "Stats", 0, 0, null, Color.WHITE));
        setBackground(new Color(30,30,30));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 18);
        Color fg = new Color(220,220,220);
        generationLabel.setFont(labelFont);
        generationLabel.setForeground(fg);
        avgFitnessLabel.setFont(labelFont);
        avgFitnessLabel.setForeground(fg);
        bestFitnessLabel.setFont(labelFont);
        bestFitnessLabel.setForeground(new Color(120,255,180));

        add(generationLabel);
        add(avgFitnessLabel);
        add(bestFitnessLabel);
    }

    public void refresh()
    {
    generationLabel.setText(String.format("Gen: %d", ga.getGeneration()));
    avgFitnessLabel.setText(String.format("Avg Fitness: %.2f", ga.getAverageFitness()));
    bestFitnessLabel.setText(String.format("Best: %d", ga.getBestFitness()));
    }
    // DARK MODE PATCH END
}
