package gui;

import ga.GeneticAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Random;

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

    public ControlPanel(GeneticAlgorithm ga, Runnable startAction, Runnable stopAction, Runnable stepAction, Runnable restartAction) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(30, 30, 30));
        setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 2));

        // ===== BUTTONS PANEL (CENTERED HORIZONTALLY) =====
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttons.setBackground(new Color(30, 30, 30));

        // Fixed size for all buttons
        Dimension btnSize = new Dimension(110, 38);

        JButton[] btnList = {startBtn, stepBtn, restartBtn, randomizeBtn};
        for (JButton btn : btnList) {
            btn.setMinimumSize(btnSize);
            btn.setPreferredSize(btnSize);
            btn.setMaximumSize(btnSize);

            // Style buttons
            btn.setForeground(Color.WHITE);
            btn.setBackground(new Color(244, 67, 54));
            styleButton(btn);

            buttons.add(btn);
        }

        // Tooltips
        startBtn.setToolTipText("Space → Start/Stop");
        stepBtn.setToolTipText("Enter → Step");
        restartBtn.setToolTipText("X → Restart");
        randomizeBtn.setToolTipText("R → Randomize");

        // Start button initial color
        startBtn.setBackground(new Color(244, 67, 54));

        add(Box.createVerticalStrut(8)); // top padding
        add(buttons);
        add(Box.createVerticalStrut(12)); // spacing between buttons and sliders

        // ===== SLIDERS =====
        mutationSlider = new JSlider(0, 100, (int)(ga.getCurrentMutationRate() * 100));
        crossoverSlider = new JSlider(0, 100, (int)(ga.getCrossoverRate() * 100));
        populationSlider = new JSlider(10, 50, ga.getPopulationSize());

        mutationLabel = makeValueLabel("Mutation Rate", mutationSlider.getValue() + "%");
        crossoverLabel = makeValueLabel("Crossover Rate", crossoverSlider.getValue() + "%");
        populationLabel = makeValueLabel("Population Size", String.valueOf(populationSlider.getValue()));

        styleSlider(mutationSlider, new Color(255, 0, 0));
        styleSlider(crossoverSlider, new Color(0, 255, 0));
        styleSlider(populationSlider, new Color(255, 180, 120));

        populationSlider.setMajorTickSpacing(10);
        populationSlider.setMinorTickSpacing(5);
        populationSlider.setPaintTicks(true);
        populationSlider.setPaintLabels(true);

        mutationSlider.addChangeListener(e -> {
            double rate = mutationSlider.getValue() / 100.0;
            ga.setMutationRate(rate);
            mutationLabel.setText("Mutation Rate: " + mutationSlider.getValue() + "%");
            mutationLabel.revalidate();
            mutationLabel.repaint();
        });

        crossoverSlider.addChangeListener(e -> {
            double rate = crossoverSlider.getValue() / 100.0;
            ga.setCrossoverRate(rate);
            crossoverLabel.setText("Crossover Rate: " + crossoverSlider.getValue() + "%");
        });

        populationSlider.addChangeListener(e -> {
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
        add(Box.createVerticalStrut(8)); // bottom padding

        // ===== ACTIONS =====
        startBtn.addActionListener(e -> toggleStartStop(ga, startAction, stopAction));
        stepBtn.addActionListener(e -> stepAction.run());
        restartBtn.addActionListener(e -> restartAction.run());
        randomizeBtn.addActionListener(e -> randomizeParameters(ga));

        // ===== KEYBINDINGS =====
        setupKeyBindings(ga, startAction, stopAction, stepAction, restartAction);
    }

    private JLabel makeValueLabel(String title, String value) {
        JLabel label = new JLabel(title + ": " + value);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Consolas", Font.PLAIN, 13));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    private void styleSlider(JSlider slider, Color color) {
        slider.setBackground(new Color(30, 30, 30));
        slider.setForeground(color);

        slider.setUI(new javax.swing.plaf.metal.MetalSliderUI() {
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
                g.fillRect(trackRect.x + fillWidth,
                        trackRect.y + trackRect.height / 2 - 2,
                        trackRect.width - fillWidth, 4);
            }
        });
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setFont(new Font("Consolas", Font.BOLD, 14));
        button.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80)));

        button.getModel().addChangeListener(e -> {
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

    private void setupKeyBindings(GeneticAlgorithm ga, Runnable startAction, Runnable stopAction, Runnable stepAction, Runnable restartAction) {
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke("SPACE"), "toggleStartStop");
        am.put("toggleStartStop", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleStartStop(ga, startAction, stopAction);
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "step");
        am.put("step", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stepAction.run();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0), "restart");
        am.put("restart", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartAction.run();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "randomize");
        am.put("randomize", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                randomizeParameters(ga);
            }
        });
    }

    public void updateMutationSlider(GeneticAlgorithm ga) {
        double avgMutationRate = ga.getAverageMutationRate();
        mutationSlider.setValue((int)(avgMutationRate * 100));
        mutationLabel.setText("Mutation Rate: " + mutationSlider.getValue() + "%");
    }
}
