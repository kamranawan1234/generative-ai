package gui;

import ga.Individual;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PopulationPanel extends JPanel {
    private Individual[] population;
    private int cellSize = 20;
    private List<Point> mutationPoints = new ArrayList<>();
    private List<Point> crossoverPoints = new ArrayList<>();

    public void setPopulation(Individual[] population) {
        this.population = population;
        repaint();
    }

    public void refresh() {
        repaint();
    }

    public void highlightMutations(int individualIndex, boolean[] mutationMask) {
        for (int j = 0; j < mutationMask.length; j++) {
            if (mutationMask[j]) mutationPoints.add(new Point(individualIndex, j));
        }
        repaint();
        new javax.swing.Timer(500, e -> {
            for (int j = 0; j < mutationMask.length; j++) mutationPoints.remove(new Point(individualIndex, j));
            repaint();
            ((javax.swing.Timer) e.getSource()).stop();
        }).start();
    }

    public void highlightCrossover(int individualIndex, boolean[] crossoverMask) {
        for (int j = 0; j < crossoverMask.length; j++) {
            if (crossoverMask[j]) crossoverPoints.add(new Point(individualIndex, j));
        }
        repaint();
        new javax.swing.Timer(500, e -> {
            for (int j = 0; j < crossoverMask.length; j++) crossoverPoints.remove(new Point(individualIndex, j));
            repaint();
            ((javax.swing.Timer) e.getSource()).stop();
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (population == null) return;

        int maxFitness = population[0].getChromosome().length;
        int bestFitness = Arrays.stream(population).mapToInt(Individual::getFitness).max().orElse(0);

        for (int i = 0; i < population.length; i++) {
            boolean[] chromosome = population[i].getChromosome();
            for (int j = 0; j < chromosome.length; j++) {
                int x = j * cellSize;
                int y = i * cellSize;

                Color color;
                if (mutationPoints.contains(new Point(i, j))) {
                    color = Color.RED;
                } else if (crossoverPoints.contains(new Point(i, j))) {
                    color = Color.GREEN;
                } else if (chromosome[j]) {
                    float ratio = (float) population[i].getFitness() / maxFitness;
                    color = new Color(0, 0, ratio); // fitness gradient
                } else {
                    color = Color.LIGHT_GRAY;
                }

                g.setColor(color);
                g.fillRect(x, y, cellSize, cellSize);
                g.setColor(Color.BLACK);
                g.drawRect(x, y, cellSize, cellSize);
            }

            if (population[i].getFitness() == bestFitness) {
                g.setColor(Color.YELLOW);
                g.drawRect(0, i * cellSize, chromosome.length * cellSize, cellSize);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (population != null && population.length > 0)
            return new Dimension(population[0].getChromosome().length * cellSize, population.length * cellSize);
        return new Dimension(400, 400);
    }
}
