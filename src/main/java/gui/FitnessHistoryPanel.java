package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayDeque;
import java.util.Deque;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class FitnessHistoryPanel extends JPanel {
  private static final int MAX_HISTORY = 500;

  private final Deque<Double> history = new ArrayDeque<>();
  private int hoverGen = -1;

  private static final Color BACKGROUND_COLOR = new Color(30, 30, 30);
  private static final Color AXIS_COLOR = new Color(180, 180, 180);
  private static final Color LABEL_COLOR = new Color(220, 220, 220);
  private static final Color LINE_COLOR = new Color(80, 180, 255);
  private static final Color HOVER_COLOR = new Color(255, 220, 80);
  private static final Color GRAPH_BG = new Color(60, 60, 60);

  /** Create the fitness history panel and configure mouse interactions. */
  public FitnessHistoryPanel() {
    setPreferredSize(new Dimension(400, 250));
    Font titleFont = new Font("Consolas", Font.BOLD, 14);
    TitledBorder border =
        BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(GRAPH_BG),
            "Fitness History",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            titleFont,
            Color.WHITE);
    setBorder(border);

    setBackground(BACKGROUND_COLOR);

    ToolTipManager.sharedInstance().setInitialDelay(0);

    addMouseMotionListener(
        new MouseAdapter() {
          @Override
          public void mouseMoved(MouseEvent e) {
            hoverGen = getClosestGeneration(e.getX(), e.getY());
            repaint();
          }
        });
  }

  /**
   * Append a fitness value to the history and request a repaint.
   *
   * @param fitness fitness value to record
   */
  public void addFitness(double fitness) {
    if (history.size() >= MAX_HISTORY) {
      history.pollFirst();
    }
    history.addLast(fitness);
    repaint();
  }

  /** Clear the stored fitness history and reset hover state. */
  public void clear() {
    history.clear();
    hoverGen = -1;
    repaint();
  }

  /**
   * Find the closest generation point to the supplied mouse coordinates.
   *
   * @param mx mouse x coordinate
   * @param my mouse y coordinate
   * @return index of the closest generation or -1 if none
   */
  private int getClosestGeneration(int mx, int my) {
    if (history.isEmpty()) return -1;

    int w = getWidth(), h = getHeight();
    int leftMargin = 60, rightMargin = 30, topMargin = 40, bottomMargin = 60;
    int graphW = w - leftMargin - rightMargin;
    int graphH = h - topMargin - bottomMargin;

    Double[] histArray = history.toArray(new Double[0]);
    double max = histArray[0];
    for (double f : histArray) if (f > max) max = f;

    int closest = -1;
    int minDist = Integer.MAX_VALUE;

    for (int i = 0; i < histArray.length; i++) {
      int x = leftMargin + i * graphW / histArray.length;
      int y = topMargin + graphH - (int) (histArray[i] / max * graphH);
      int dist = (int) Math.hypot(mx - x, my - y);
      if (dist < minDist) {
        minDist = dist;
        closest = i;
      }
    }

    if (closest >= 0) {
      Double f = histArray[closest];
      setToolTipText(String.format("Generation: %d, Fitness: %.2f", closest, f));
    } else {
      setToolTipText(null);
    }
    return closest;
  }

  /** Paint the background graph, axes, fitness line, and hover marker. */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    setBackground(BACKGROUND_COLOR);

    if (history.isEmpty()) return;

    Graphics2D g2 = (Graphics2D) g.create();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int w = getWidth(), h = getHeight();
    int leftMargin = 60, rightMargin = 30, topMargin = 40, bottomMargin = 60;
    int graphW = w - leftMargin - rightMargin;
    int graphH = h - topMargin - bottomMargin;

    g2.setColor(GRAPH_BG);
    g2.fillRect(leftMargin, topMargin, graphW, graphH);

    drawAxes(g2, leftMargin, topMargin, graphW, graphH);

    drawFitnessLine(g2, leftMargin, topMargin, graphW, graphH);

    drawHoverMarker(g2, leftMargin, topMargin, graphW, graphH);

    g2.dispose();
  }

  /** Draw axes and tick labels for the fitness graph. */
  private void drawAxes(Graphics2D g2, int left, int top, int graphW, int graphH) {
    g2.setColor(AXIS_COLOR);
    g2.setStroke(new BasicStroke(2));
    g2.drawLine(left, top, left, top + graphH);
    g2.drawLine(left, top + graphH, left + graphW, top + graphH);

    g2.setFont(new Font("Consolas", Font.PLAIN, 12));
    int numYTicks = 5;
    Double[] histArray = history.toArray(new Double[0]);
    double max = histArray[0];
    for (double f : histArray) if (f > max) max = f;

    for (int i = 0; i <= numYTicks; i++) {
      int y = top + graphH - i * graphH / numYTicks;
      double val = max * i / numYTicks;
      g2.setColor(AXIS_COLOR);
      g2.drawLine(left - 8, y, left, y);
      g2.setColor(LABEL_COLOR);
      g2.drawString(String.format("%.1f", val), left - 55, y + 5);
    }
    g2.setColor(LABEL_COLOR);
    g2.drawString("Fitness", left - 55, top - 10);

    int numXTicks = Math.min(10, histArray.length);
    for (int i = 0; i <= numXTicks; i++) {
      int x = left + i * graphW / numXTicks;
      int val = i * histArray.length / numXTicks;
      g2.setColor(AXIS_COLOR);
      g2.drawLine(x, top + graphH, x, top + graphH + 8);
      g2.setColor(LABEL_COLOR);
      g2.drawString(Integer.toString(val), x - 10, top + graphH + 28);
    }
    g2.setColor(LABEL_COLOR);
    g2.drawString("Generation", left + graphW / 2 - 35, top + graphH + 50);
  }

  /** Draw the polyline representing fitness over time. */
  private void drawFitnessLine(Graphics2D g2, int left, int top, int graphW, int graphH) {
    Double[] histArray = history.toArray(new Double[0]);
    double max = histArray[0];
    for (double f : histArray) if (f > max) max = f;

    g2.setColor(LINE_COLOR);
    g2.setStroke(new BasicStroke(2));

    for (int i = 1; i < histArray.length; i++) {
      int x1 = left + (i - 1) * graphW / histArray.length;
      int y1 = top + graphH - (int) (histArray[i - 1] / max * graphH);
      int x2 = left + i * graphW / histArray.length;
      int y2 = top + graphH - (int) (histArray[i] / max * graphH);
      g2.drawLine(x1, y1, x2, y2);
    }
  }

  /** Draw a marker for the currently hovered generation, if any. */
  private void drawHoverMarker(Graphics2D g2, int left, int top, int graphW, int graphH) {
    if (hoverGen < 0 || history.isEmpty()) return;

    Double[] histArray = history.toArray(new Double[0]);
    double max = histArray[0];
    for (double f : histArray) if (f > max) max = f;

    int x = left + hoverGen * graphW / histArray.length;
    int y = top + graphH - (int) (histArray[hoverGen] / max * graphH);

    g2.setColor(HOVER_COLOR);
    g2.fillOval(x - 5, y - 5, 10, 10);
  }
}
