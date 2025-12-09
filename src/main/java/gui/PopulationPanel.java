package gui;

import ga.Individual;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;

public class PopulationPanel extends JPanel {
  // DARK MODE PATCH START
  private Individual[] population;
  // cellSize is now dynamic
  private List<Point> mutationPoints = new ArrayList<>();
  private List<Point> crossoverPoints = new ArrayList<>();

  public PopulationPanel() {
    setBackground(new Color(24, 24, 24));
    setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 2));
  }

  public void setPopulation(Individual[] population) {
    this.population = population;
    revalidate(); // DYNAMIC SCROLLBAR PATCH
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

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    setBackground(new Color(24, 24, 24));
    if (population == null) return;

    int panelW = getWidth();
    int panelH = getHeight();
    int numCols = population[0].getChromosome().length;
    int numRows = population.length;
    int leftMargin = 60;
    int topMargin = 50;
    int rightMargin = 30;
    int bottomMargin = 50;
    int cellSize = 32; // LARGER FIXED CELL SIZE FOR SCROLL PATCH
    int gridW = cellSize * numCols;
    int gridH = cellSize * numRows;
    // Center grid in panel
    int xOffset = leftMargin + Math.max(0, (panelW - leftMargin - rightMargin - gridW) / 2);
    int yOffset = topMargin + Math.max(0, (panelH - topMargin - bottomMargin - gridH) / 2);

    int maxFitness = numCols;
    int bestFitness = Arrays.stream(population).mapToInt(Individual::getFitness).max().orElse(0);

    Graphics2D g2 = (Graphics2D) g;

    // General antialiasing for shapes
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Text antialiasing — this is the one that really fixes blurry text
    g2.setRenderingHint(
        RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    // Draw column axis (chromosome positions)
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
    g2.drawString("Chromosome Position", xOffset + gridW / 2 - 60, yOffset - 35);

    // Draw row axis (individual index)
    int rowTickStep = Math.max(1, numRows / 10);
    for (int i = 0; i < numRows; i += rowTickStep) {
      int y = yOffset + i * cellSize + cellSize / 2;
      g2.setColor(new Color(120, 120, 120));
      g2.drawLine(xOffset - 8, y - 15, xOffset + gridW, y);
      g2.setColor(new Color(220, 220, 220));
      g2.drawString(Integer.toString(i), xOffset - 25, y - 10);
    }
    g2.setColor(new Color(220, 220, 220));
    g2.drawString("Individual Index", xOffset - 150, yOffset + gridH / 2);

    // Draw grid
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
          color = new Color(60, 120, (int) (180 * ratio)); // fitness gradient
        } else {
          color = new Color(60, 60, 60);
        }

        g2.setColor(color);
        g2.fillRect(x, y, cellSize, cellSize);
        // CONSISTENT BORDER PATCH START
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(x, y, cellSize, cellSize);
        // CONSISTENT BORDER PATCH END
      }

      if (population[i].getFitness() == bestFitness) {
        // Draw a thick yellow rectangle fully outlining the best chromosome
        g2.setColor(new Color(255, 215, 0));

        g2.drawRect(
            xOffset + 1, yOffset + i * cellSize + 1, numCols * cellSize - 2 * 1, cellSize - 2 * 1);

        // Draw fitness/gen label to the right of the chromosome
        g2.setFont(new Font("Consolas", Font.BOLD, 14));
        g2.setColor(new Color(255, 215, 0));
        String label = String.format("Fitness: %d", population[i].getFitness());
        g2.drawString(
            label, xOffset + numCols * cellSize + 10, yOffset + i * cellSize + cellSize - 10);
      }
    }
  }

  @Override
  public Dimension getPreferredSize() {
    if (population != null && population.length > 0) {
      int numCols = population[0].getChromosome().length;
      int numRows = population.length;
      int leftMargin = 60;
      int topMargin = 40;
      int rightMargin = 30;
      int bottomMargin = 40;
      int cellSize = 32; // LARGER FIXED CELL SIZE FOR SCROLL PATCH
      int gridW = cellSize * numCols + leftMargin + rightMargin;
      int gridH = cellSize * numRows + topMargin + bottomMargin;
      return new Dimension(gridW, gridH);
    }
    return new Dimension(400, 400);
  }
  // DARK MODE PATCH END
}
