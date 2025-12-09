// ...existing code...
package gui;

import ga.GeneticAlgorithm;
import java.awt.*;
import javax.swing.*;

public class MainWindow extends JFrame {
  // DARK MODE PATCH START
  public MainWindow(
      GeneticAlgorithm ga,
      PopulationPanel popPanel,
      StatsPanel statsPanel,
      FitnessHistoryPanel histPanel,
      ControlPanel controlPanel) {
    setTitle("Genetic Algorithm Visualizer");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    getContentPane().setBackground(new Color(24, 24, 24));
    popPanel.setAutoscrolls(true);
    JScrollPane scrollPane =
        new JScrollPane(
            popPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setBackground(new Color(24, 24, 24));
    scrollPane.getViewport().setBackground(new Color(24, 24, 24));
    scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 2));
    // Set fixed viewport size for reliable scrolling
    scrollPane.getViewport().setPreferredSize(new Dimension(800, 600));
    // Increase scroll speed
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
  // DARK MODE PATCH END
}
