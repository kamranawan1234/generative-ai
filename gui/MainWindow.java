package gui;

import ga.GeneticAlgorithm;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    public MainWindow(GeneticAlgorithm ga, PopulationPanel popPanel, StatsPanel statsPanel, FitnessHistoryPanel histPanel, ControlPanel controlPanel) {
        setTitle("Genetic Algorithm Visualizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(popPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(statsPanel, BorderLayout.NORTH);
        rightPanel.add(histPanel, BorderLayout.CENTER);
        rightPanel.add(controlPanel, BorderLayout.SOUTH);

        add(rightPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
