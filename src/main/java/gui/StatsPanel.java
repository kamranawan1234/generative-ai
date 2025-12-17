package gui;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import ga.GeneticAlgorithm;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/** Panel that shows generation, average fitness and best fitness values. */
public class StatsPanel extends JPanel {
  private JLabel generationLabel = new JLabel("Generation: 0");
  private JLabel avgFitnessLabel = new JLabel("Avg Fitness: 0");
  private JLabel bestFitnessLabel = new JLabel("Best Fitness: 0");
  private transient GeneticAlgorithm ga;

  /**
   * Create the stats panel and bind it to a GeneticAlgorithm instance.
   *
   * @param ga genetic algorithm used to obtain stats
   */
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  public StatsPanel(GeneticAlgorithm ga) {
    this.ga = ga;

    setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));

    Font titleFont = new Font("Consolas", Font.BOLD, 14);
    setBorder(
        BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60)),
            "Stats",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            titleFont,
            Color.WHITE));

    setBackground(new Color(30, 30, 30));

    Font labelFont = new Font("Consolas", Font.BOLD, 18);
    Color fg = new Color(220, 220, 220);
    generationLabel.setFont(labelFont);
    generationLabel.setForeground(fg);
    avgFitnessLabel.setFont(labelFont);
    avgFitnessLabel.setForeground(fg);
    bestFitnessLabel.setFont(labelFont);
    bestFitnessLabel.setForeground(new Color(120, 255, 180));

    add(generationLabel);
    add(avgFitnessLabel);
    add(bestFitnessLabel);
  }

  /** Refresh displayed statistics from the GeneticAlgorithm instance. */
  public void refresh() {
    generationLabel.setText(String.format("Gen: %d", ga.getGeneration()));
    avgFitnessLabel.setText(String.format("Avg Fitness: %.2f", ga.getAverageFitness()));
    bestFitnessLabel.setText(String.format("Best: %d", ga.getBestFitness()));
  }
}
