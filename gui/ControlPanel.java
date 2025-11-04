package gui;

import ga.GeneticAlgorithm;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    private JButton startBtn = new JButton("Start");
    private JButton stopBtn = new JButton("Stop");
    private JButton stepBtn = new JButton("Step");
    private JButton restartBtn = new JButton("Restart");

    private JSlider mutationSlider;
    private JSlider crossoverSlider;

    public ControlPanel(GeneticAlgorithm ga, Runnable startAction, Runnable stopAction, Runnable stepAction, Runnable restartAction) {
        setLayout(new GridLayout(2,1));
        JPanel buttons = new JPanel();
        buttons.add(startBtn);
        buttons.add(stopBtn);
        buttons.add(stepBtn);
        buttons.add(restartBtn);
        add(buttons);

        mutationSlider = new JSlider(0,100,(int)(ga.getMutationRate()*100));
        mutationSlider.setBorder(BorderFactory.createTitledBorder("Mutation Rate (%)"));
        mutationSlider.addChangeListener(e -> ga.setMutationRate(mutationSlider.getValue()/100.0));

        crossoverSlider = new JSlider(0,100,(int)(ga.getCrossoverRate()*100));
        crossoverSlider.setBorder(BorderFactory.createTitledBorder("Crossover Rate (%)"));
        crossoverSlider.addChangeListener(e -> ga.setCrossoverRate(crossoverSlider.getValue()/100.0));

        JPanel sliders = new JPanel(new GridLayout(2,1));
        sliders.add(mutationSlider);
        sliders.add(crossoverSlider);
        add(sliders);

        startBtn.addActionListener(e -> startAction.run());
        stopBtn.addActionListener(e -> stopAction.run());
        stepBtn.addActionListener(e -> stepAction.run());
        restartBtn.addActionListener(e -> restartAction.run());
    }
}
