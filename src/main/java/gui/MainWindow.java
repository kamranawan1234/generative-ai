package gui;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import ga.GeneticAlgorithm;
import java.awt.*;
import javax.swing.*;

public class MainWindow extends JFrame {
  /**
   * Main application window that arranges the population view and control panels.
   *
   * @param ga genetic algorithm instance used by UI components
   * @param popPanel panel that visualizes the population
   * @param statsPanel panel showing numeric statistics
   * @param histPanel panel showing fitness history over generations
   * @param controlPanel panel providing control actions
   */
  @SuppressFBWarnings("MC_OVERRIDABLE_METHOD_CALL_IN_CONSTRUCTOR")
  public MainWindow(
      GeneticAlgorithm ga,
      PopulationPanel popPanel,
      StatsPanel statsPanel,
      FitnessHistoryPanel histPanel,
      ControlPanel controlPanel) {
    setTitle("Genetic Algorithm Visualizer");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // Defer altering the content pane background until after construction to avoid
    // calling potentially-overridable methods from the constructor (avoids a SpotBugs
    // MC_OVERRIDABLE_METHOD_CALL_IN_CONSTRUCTOR warning).
    javax.swing.SwingUtilities.invokeLater(
        () -> getContentPane().setBackground(new Color(24, 24, 24)));
    popPanel.setAutoscrolls(true);
    JScrollPane scrollPane =
        new JScrollPane(
            popPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setBackground(new Color(24, 24, 24));
    scrollPane.getViewport().setBackground(new Color(24, 24, 24));
    scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 2));
    scrollPane.getViewport().setPreferredSize(new Dimension(800, 600));
    scrollPane.getVerticalScrollBar().setUnitIncrement(32);
    scrollPane.getVerticalScrollBar().setBlockIncrement(128);
    scrollPane.getHorizontalScrollBar().setUnitIncrement(32);
    scrollPane.getHorizontalScrollBar().setBlockIncrement(128);
    add(scrollPane, BorderLayout.CENTER);

    JPanel rightPanel = new JPanel(new BorderLayout());
    rightPanel.setBackground(new Color(24, 24, 24));
    rightPanel.add(statsPanel, BorderLayout.NORTH);
    rightPanel.add(histPanel, BorderLayout.CENTER);
    rightPanel.add(controlPanel, BorderLayout.SOUTH);

    add(rightPanel, BorderLayout.EAST);

    setExtendedState(JFrame.MAXIMIZED_BOTH);
    setLocationRelativeTo(null);
    setVisible(true);
  }
}
