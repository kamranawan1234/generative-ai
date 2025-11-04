package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FitnessHistoryPanel extends JPanel {
    private List<Double> history = new ArrayList<>();

    public FitnessHistoryPanel() {
        setPreferredSize(new Dimension(400,150));
        setBorder(BorderFactory.createTitledBorder("Fitness History"));
    }

    public void addFitness(double avgFitness) {
        history.add(avgFitness);
        repaint();
    }

    public void clear() {
        history.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (history.isEmpty()) return;

        int w = getWidth();
        int h = getHeight();
        int size = history.size();
        double max = history.stream().mapToDouble(Double::doubleValue).max().orElse(1);

        for (int i = 1; i < size; i++) {
            int x1 = (i-1)*w/size;
            int y1 = h - (int)(history.get(i-1)/max*h);
            int x2 = i*w/size;
            int y2 = h - (int)(history.get(i)/max*h);
            g.setColor(Color.BLUE);
            g.drawLine(x1,y1,x2,y2);
        }
    }
}
