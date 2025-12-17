package gui;

import ga.GeneticAlgorithm;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.*;

/** Control panel containing buttons and sliders to operate the genetic algorithm. */
public class ControlPanel extends JPanel {
  private final JButton startBtn = new JButton("Start");
  private final JButton stepBtn = new JButton("Step");
  private final JButton restartBtn = new JButton("Restart");
  private final JButton randomizeBtn = new JButton("Randomize");
  private final JSlider mutationSlider;
  private final JSlider crossoverSlider;
  private final JSlider populationSlider;
  private final JLabel mutationLabel;
  private final JLabel crossoverLabel;
  private final JLabel populationLabel;
  private final Random random = new Random();
  private boolean isRunning = false;

  /**
   * Create a control panel wired to the provided actions and GA instance.
   *
   * @param ga the genetic algorithm instance
   * @param startAction runnable invoked to start automated evolution
   * @param stopAction runnable invoked to stop automated evolution
   * @param stepAction runnable invoked to perform a single evolution step
   * @param restartAction runnable invoked to restart the population
   */
  public ControlPanel(
      GeneticAlgorithm ga,
      Runnable startAction,
      Runnable stopAction,
      Runnable stepAction,
      Runnable restartAction) {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBackground(new Color(30, 30, 30));
    setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 2));

    JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
    buttons.setBackground(new Color(30, 30, 30));

    Dimension btnSize = new Dimension(110, 38);

    JButton[] btnList = {startBtn, stepBtn, restartBtn, randomizeBtn};
    for (JButton btn : btnList) {
      btn.setMinimumSize(btnSize);
      btn.setPreferredSize(btnSize);
      btn.setMaximumSize(btnSize);

      btn.setForeground(Color.WHITE);
      btn.setBackground(new Color(244, 67, 54));
      styleButton(btn);

      buttons.add(btn);
    }

    startBtn.setToolTipText("Space → Start/Stop");
    stepBtn.setToolTipText("Enter → Step");
    restartBtn.setToolTipText("X → Restart");
    randomizeBtn.setToolTipText("R → Randomize");
    startBtn.setBackground(new Color(244, 67, 54));

    add(Box.createVerticalStrut(8));
    add(buttons);
    add(Box.createVerticalStrut(12));

    mutationSlider = new JSlider(0, 100, (int) (ga.getCurrentMutationRate() * 100));
    crossoverSlider = new JSlider(0, 100, (int) (ga.getCrossoverRate() * 100));
    populationSlider = new JSlider(10, 50, ga.getPopulationSize());

    mutationLabel = makeValueLabel("Mutation Rate", mutationSlider.getValue() + "%");
    crossoverLabel = makeValueLabel("Crossover Rate", crossoverSlider.getValue() + "%");
    populationLabel =
        makeValueLabel("Population Size", String.valueOf(populationSlider.getValue()));

    styleSlider(mutationSlider, new Color(255, 0, 0));
    styleSlider(crossoverSlider, new Color(0, 255, 0));
    styleSlider(populationSlider, new Color(255, 180, 120));

    populationSlider.setMajorTickSpacing(10);
    populationSlider.setMinorTickSpacing(5);
    populationSlider.setPaintTicks(true);
    populationSlider.setPaintLabels(true);

    mutationSlider.addChangeListener(
        e -> {
          double rate = mutationSlider.getValue() / 100.0;
          ga.setMutationRate(rate);
          mutationLabel.setText("Mutation Rate: " + mutationSlider.getValue() + "%");
          mutationLabel.revalidate();
          mutationLabel.repaint();
        });

    crossoverSlider.addChangeListener(
        e -> {
          double rate = crossoverSlider.getValue() / 100.0;
          ga.setCrossoverRate(rate);
          crossoverLabel.setText("Crossover Rate: " + crossoverSlider.getValue() + "%");
        });

    populationSlider.addChangeListener(
        e -> {
          ga.setPopulationSize(populationSlider.getValue());
          populationLabel.setText("Population Size: " + populationSlider.getValue());
        });

    JPanel sliders = new JPanel(new GridLayout(3, 2, 10, 5));
    sliders.setBackground(new Color(30, 30, 30));
    sliders.add(mutationLabel);
    sliders.add(mutationSlider);
    sliders.add(crossoverLabel);
    sliders.add(crossoverSlider);
    sliders.add(populationLabel);
    sliders.add(populationSlider);

    add(sliders);
    add(Box.createVerticalStrut(8));

    startBtn.addActionListener(e -> toggleStartStop(ga, startAction, stopAction));
    stepBtn.addActionListener(e -> stepAction.run());
    restartBtn.addActionListener(e -> restartAction.run());
    randomizeBtn.addActionListener(e -> randomizeParameters(ga));

    setupKeyBindings(ga, startAction, stopAction, stepAction, restartAction);
  }

  /**
   * Create a consistent label for slider values.
   *
   * @param title small title for the value
   * @param value textual value to display
   * @return configured JLabel
   */
  private JLabel makeValueLabel(String title, String value) {
    JLabel label = new JLabel(title + ": " + value);
    label.setForeground(Color.WHITE);
    label.setFont(new Font("Consolas", Font.PLAIN, 13));
    label.setHorizontalAlignment(SwingConstants.LEFT);
    return label;
  }

  /**
   * Apply custom slider UI styling using the given color.
   *
   * @param slider slider to style
   * @param color primary color for the slider UI
   */
  private void styleSlider(JSlider slider, Color color) {
    slider.setBackground(new Color(30, 30, 30));
    slider.setForeground(color);

    slider.setUI(
        new javax.swing.plaf.basic.BasicSliderUI(slider) {
          @Override
          public void paintThumb(Graphics g) {
            g.setColor(color);
            g.fillRect(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
          }

          @Override
          public void paintTrack(Graphics g) {
            int value = slider.getValue() - slider.getMinimum();
            int range = slider.getMaximum() - slider.getMinimum();
            double percent = value / (double) range;
            int fillWidth = (int) (percent * trackRect.width);

            g.setColor(color);
            g.fillRect(trackRect.x, trackRect.y + trackRect.height / 2 - 2, fillWidth, 4);

            g.setColor(new Color(60, 60, 60));
            g.fillRect(
                trackRect.x + fillWidth,
                trackRect.y + trackRect.height / 2 - 2,
                trackRect.width - fillWidth,
                4);
          }
        });
  }

  /**
   * Configure button visual properties and interactive styling.
   *
   * @param button the button to style
   */
  private void styleButton(JButton button) {
    button.setFocusPainted(false);
    button.setFont(new Font("Consolas", Font.BOLD, 14));
    button.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));

    button
        .getModel()
        .addChangeListener(
            e -> {
              if (button.getModel().isPressed()) {
                if (button != startBtn) {
                  button.setBackground(new Color(76, 175, 80));
                }
              } else {
                if (button != startBtn) {
                  button.setBackground(new Color(244, 67, 54));
                }
              }
            });
  }

  /**
   * Toggle between running and stopped state. Updates button text and appearance.
   *
   * @param ga the genetic algorithm instance
   * @param startAction action to start automated evolution
   * @param stopAction action to stop automated evolution
   */
  private void toggleStartStop(GeneticAlgorithm ga, Runnable startAction, Runnable stopAction) {
    if (!isRunning) {
      startAction.run();
      startBtn.setText("Stop");
      startBtn.setBackground(new Color(76, 175, 80));
    } else {
      stopAction.run();
      startBtn.setText("Start");
      startBtn.setBackground(new Color(244, 67, 54));
    }
    isRunning = !isRunning;
  }

  /**
   * Pick and apply a random set of parameters and update the GA and UI.
   *
   * @param ga the genetic algorithm instance
   */
  private void randomizeParameters(GeneticAlgorithm ga) {
    int newMutation = random.nextInt(101);
    int newCrossover = random.nextInt(101);
    int newPopulation = 10 + random.nextInt(41);

    mutationSlider.setValue(newMutation);
    crossoverSlider.setValue(newCrossover);
    populationSlider.setValue(newPopulation);

    ga.setMutationRate(newMutation / 100.0);
    ga.setCrossoverRate(newCrossover / 100.0);
    ga.setPopulationSize(newPopulation);

    mutationLabel.setText("Mutation Rate: " + newMutation + "%");
    crossoverLabel.setText("Crossover Rate: " + newCrossover + "%");
    populationLabel.setText("Population Size: " + newPopulation);
  }

  /**
   * Configure global key bindings for control actions (space/enter/X/R).
   *
   * @param ga the genetic algorithm instance
   * @param startAction action to start evolution
   * @param stopAction action to stop evolution
   * @param stepAction action to perform a single step
   * @param restartAction action to restart population
   */
  private void setupKeyBindings(
      GeneticAlgorithm ga,
      Runnable startAction,
      Runnable stopAction,
      Runnable stepAction,
      Runnable restartAction) {
    InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap am = getActionMap();

    im.put(KeyStroke.getKeyStroke("SPACE"), "toggleStartStop");
    am.put(
        "toggleStartStop",
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            toggleStartStop(ga, startAction, stopAction);
          }
        });

    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "step");
    am.put(
        "step",
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            stepAction.run();
          }
        });

    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0), "restart");
    am.put(
        "restart",
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            restartAction.run();
          }
        });

    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "randomize");
    am.put(
        "randomize",
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            randomizeParameters(ga);
          }
        });
  }

  /**
   * Update the mutation slider to reflect the current average mutation rate from the GA.
   *
   * @param ga the genetic algorithm instance
   */
  public void updateMutationSlider(GeneticAlgorithm ga) {
    double avgMutationRate = ga.getAverageMutationRate();
    mutationSlider.setValue((int) (avgMutationRate * 100));
    mutationLabel.setText("Mutation Rate: " + mutationSlider.getValue() + "%");
  }
}
