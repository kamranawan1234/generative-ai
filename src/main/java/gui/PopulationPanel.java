package gui;

import ga.Individual;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;

/** Panel responsible for rendering the whole population as a grid of genes. */
public class PopulationPanel extends JPanel {
  private transient Individual[] population;
  private int fixedCellSize = -1;
  private static final int LEFT_MARGIN = 100;
  private static final int TOP_MARGIN = 50;
  private static final int RIGHT_MARGIN = 30;
  private static final int BOTTOM_MARGIN = 50;
  private List<Point> mutationPoints = new ArrayList<>();
  private List<Point> crossoverPoints = new ArrayList<>();

  /** Create the population panel and configure its appearance. */
  public PopulationPanel() {
    setBackground(new Color(24, 24, 24));
    setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 2));
  }

  /**
   * Set the population to display and compute layout parameters.
   *
   * @param population array of individuals to render
   */
  public void setPopulation(Individual[] population) {
    // Defensive copy to avoid storing externally mutable array reference
    this.population = (population == null) ? null : Arrays.copyOf(population, population.length);
    if (fixedCellSize == -1 && population != null && population.length > 0) {
      int numCols = population[0].getChromosome().length;
      int maxCell = 18;
      int availWidth = -1;
      java.awt.Container vp = SwingUtilities.getAncestorOfClass(javax.swing.JViewport.class, this);
      if (vp instanceof javax.swing.JViewport) {
        availWidth = ((javax.swing.JViewport) vp).getWidth();
      }

      if (availWidth <= 0) {
        availWidth =
            Math.max(600, java.awt.Toolkit.getDefaultToolkit().getScreenSize().width - 200);
      }

      int usableWidth = Math.max(100, availWidth - LEFT_MARGIN - RIGHT_MARGIN);
      fixedCellSize = Math.max(4, usableWidth / Math.max(1, numCols));
      fixedCellSize = Math.min(maxCell, fixedCellSize);

      while (LEFT_MARGIN + RIGHT_MARGIN + fixedCellSize * numCols > availWidth
          && fixedCellSize > 1) {
        fixedCellSize--;
      }
    }
    revalidate();
    repaint();
  }

  /** Request a repaint to refresh the visual representation. */
  public void refresh() {
    repaint();
  }

  /**
   * Temporarily highlight mutated genes for a given individual.
   *
   * @param individualIndex index of the individual in the population
   * @param mutationMask boolean mask indicating mutated gene positions
   */
  public void highlightMutations(int individualIndex, boolean[] mutationMask) {
    for (int j = 0; j < mutationMask.length; j++) {
      if (mutationMask[j]) mutationPoints.add(new Point(individualIndex, j));
    }
    repaint();
    new javax.swing.Timer(
            500,
            e -> {
              for (int j = 0; j < mutationMask.length; j++)
                mutationPoints.remove(new Point(individualIndex, j));
              repaint();
              ((javax.swing.Timer) e.getSource()).stop();
            })
        .start();
  }

  /**
   * Temporarily highlight crossover-affected genes for a given individual.
   *
   * @param individualIndex index of the individual in the population
   * @param crossoverMask boolean mask indicating crossover-affected positions
   */
  public void highlightCrossover(int individualIndex, boolean[] crossoverMask) {
    for (int j = 0; j < crossoverMask.length; j++) {
      if (crossoverMask[j]) crossoverPoints.add(new Point(individualIndex, j));
    }
    repaint();
    new javax.swing.Timer(
            500,
            e -> {
              for (int j = 0; j < crossoverMask.length; j++)
                crossoverPoints.remove(new Point(individualIndex, j));
              repaint();
              ((javax.swing.Timer) e.getSource()).stop();
            })
        .start();
  }

  /** Render the population grid and visual markers. */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    setBackground(new Color(24, 24, 24));
    if (population == null) return;

    int numCols = population[0].getChromosome().length;
    int numRows = population.length;
    int cellSize = (fixedCellSize != -1) ? fixedCellSize : 32;

    int gridW = cellSize * numCols;
    int gridH = cellSize * numRows;

    int xOffset = LEFT_MARGIN;
    int yOffset = TOP_MARGIN;

    int maxFitness = numCols;
    int bestFitness = Arrays.stream(population).mapToInt(Individual::getFitness).max().orElse(0);

    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(
        RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g2.setColor(new Color(120, 120, 120));
    g2.setFont(new Font("Consolas", Font.PLAIN, 12));
    int colTickStep = Math.max(1, numCols / 10);
    for (int j = 0; j < numCols; j += colTickStep) {
      int x = xOffset + j * cellSize;
      g2.drawLine(x, yOffset - 8, x, yOffset + gridH);
      g2.setColor(new Color(220, 220, 220));
      g2.drawString(Integer.toString(j), x - 6, yOffset - 16);
      g2.setColor(new Color(120, 120, 120));
    }
    g2.setColor(new Color(220, 220, 220));
    g2.drawString("Gene", xOffset + gridW / 2 - 15, yOffset - 35);

    int rowTickStep = Math.max(1, numRows / 10);
    for (int i = 0; i < numRows; i += rowTickStep) {
      int y = yOffset + i * cellSize + cellSize / 2;
      g2.setColor(new Color(120, 120, 120));
      g2.drawLine(xOffset - 8, y - 8, xOffset + gridW, y - 8);
      g2.setColor(new Color(220, 220, 220));
      g2.drawString(Integer.toString(i), xOffset - 30, y - 3);
    }

    g2.setColor(new Color(220, 220, 220));
    g2.setFont(new Font("Consolas", Font.PLAIN, 12));
    g2.drawString("Individual", xOffset - 95, yOffset + gridH / 2 + 5);

    for (int i = 0; i < numRows; i++) {
      boolean[] chromosome = population[i].getChromosome();
      for (int j = 0; j < numCols; j++) {
        int x = xOffset + j * cellSize;
        int y = yOffset + i * cellSize;

        Color color;
        if (mutationPoints.contains(new Point(i, j))) {
          color = new Color(255, 80, 80);
        } else if (crossoverPoints.contains(new Point(i, j))) {
          color = new Color(80, 255, 120);
        } else if (chromosome[j]) {
          float ratio = (float) population[i].getFitness() / maxFitness;
          color = new Color(60, 120, (int) (180 * ratio));
        } else {
          color = new Color(60, 60, 60);
        }

        g2.setColor(color);
        g2.fillRect(x, y, cellSize, cellSize);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(x, y, cellSize, cellSize);
      }

      if (population[i].getFitness() == bestFitness) {
        g2.setColor(new Color(255, 215, 0));
        g2.drawRect(
            xOffset + 1, yOffset + i * cellSize + 1, numCols * cellSize - 2 * 1, cellSize - 2 * 1);
        g2.setFont(new Font("Consolas", Font.BOLD, 14));
        g2.setColor(new Color(255, 215, 0));
        String label = String.format("Fitness: %d", population[i].getFitness());
        g2.drawString(
            label, xOffset + numCols * cellSize + 10, yOffset + i * cellSize + cellSize - 10);
      }
    }
  }

  /** Return a preferred size based on population dimensions and margins. */
  @Override
  public Dimension getPreferredSize() {
    if (population != null && population.length > 0) {
      int numCols = population[0].getChromosome().length;
      int numRows = population.length;
      int cellSize = (fixedCellSize != -1) ? fixedCellSize : 32;
      int gridW = cellSize * numCols + LEFT_MARGIN + RIGHT_MARGIN;
      int gridH = cellSize * numRows + TOP_MARGIN + BOTTOM_MARGIN;
      return new Dimension(gridW, gridH);
    }
    return new Dimension(400, 400);
  }
}
