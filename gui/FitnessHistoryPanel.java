package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FitnessHistoryPanel extends JPanel
{
    // DARK MODE PATCH START
    private List<Double> history = new ArrayList<>();
    // HOVER PATCH START
    private int hoverGen = -1;

    public FitnessHistoryPanel()
    {
        javax.swing.ToolTipManager.sharedInstance().setInitialDelay(0);
        {
        setPreferredSize(new Dimension(400,150));
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(60,60,60)), "Fitness History", 0, 0, null, Color.WHITE));
        setBackground(new Color(30,30,30));

        addMouseMotionListener(new java.awt.event.MouseMotionAdapter()
        {
            public void mouseMoved(java.awt.event.MouseEvent e)
            {
                int w = getWidth();
                int h = getHeight();
                int size = history.size();
                int leftMargin = 60;
                int rightMargin = 30;
                int topMargin = 40;
                int bottomMargin = 40;
                int graphW = w - leftMargin - rightMargin;
                int graphH = h - topMargin - bottomMargin;
                int mx = e.getX();
                int my = e.getY();
                if (mx >= leftMargin && mx <= leftMargin + graphW && my >= topMargin && my <= topMargin + graphH && size > 0)
                {
                    double max = history.stream().mapToDouble(Double::doubleValue).max().orElse(1);
                    int closestGen = -1;
                    int minDist = Integer.MAX_VALUE;
                    for (int i = 0; i < size; i++)
                    {
                        int px = leftMargin + i * graphW / size;
                        int py = topMargin + graphH - (int)(history.get(i)/max*graphH);
                        int dist = (int)Math.sqrt((mx-px)*(mx-px) + (my-py)*(my-py));
                        if (dist < minDist)
                        {
                            minDist = dist;
                            closestGen = i;
                        }
                    }
                    // Always show tooltip and marker at closest point if mouse is inside graph
                    hoverGen = closestGen;
                    double fitness = history.get(closestGen);
                    setToolTipText(String.format("Generation: %d, Fitness: %.2f", closestGen, fitness));
                }
                else
                {
                    hoverGen = -1;
                    setToolTipText(null);
                }
                repaint();
            }
        });
    }
        // HOVER PATCH END
    }

    public void addFitness(double avgFitness)
    {
        history.add(avgFitness);
        repaint();
    }

    public void clear()
    {
        history.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        setBackground(new Color(30,30,30));
        if (history.isEmpty()) return;

        int w = getWidth();
        int h = getHeight();
        int size = history.size();
        double max = history.stream().mapToDouble(Double::doubleValue).max().orElse(1);

        int leftMargin = 60;
        int bottomMargin = 40;
        int topMargin = 40;
        int rightMargin = 30;
        int graphW = w - leftMargin - rightMargin;
        int graphH = h - topMargin - bottomMargin;

        g.setColor(new Color(60,60,60));
        g.fillRect(leftMargin, topMargin, graphW, graphH);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw axes
        g2.setColor(new Color(180,180,180));
        g2.setStroke(new BasicStroke(2));
        // Y axis
        g2.drawLine(leftMargin, topMargin, leftMargin, topMargin + graphH);
        // X axis
        g2.drawLine(leftMargin, topMargin + graphH, leftMargin + graphW, topMargin + graphH);

        // Y axis ticks and labels (fitness)
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        int numYTicks = 5;
        for (int i = 0; i <= numYTicks; i++)
        {
            int y = topMargin + graphH - (i * graphH / numYTicks);
            double fitnessVal = max * i / numYTicks;
            g2.setColor(new Color(180,180,180));
            g2.drawLine(leftMargin-8, y, leftMargin, y);
            g2.setColor(new Color(220,220,220));
            g2.drawString(String.format("%.1f", fitnessVal), leftMargin-55, y+5);
        }
        g2.setColor(new Color(220,220,220));
        g2.drawString("Fitness", leftMargin-55, topMargin-10);

        // X axis ticks and labels (generation)
        int numXTicks = Math.min(10, size);
        for (int i = 0; i <= numXTicks; i++)
        {
            int x = leftMargin + (i * graphW / numXTicks);
            int genVal = i * size / numXTicks;
            g2.setColor(new Color(180,180,180));
            g2.drawLine(x, topMargin + graphH, x, topMargin + graphH + 8);
            g2.setColor(new Color(220,220,220));
            g2.drawString(Integer.toString(genVal), x-10, topMargin + graphH + 28);
        }
        g2.setColor(new Color(220,220,220));
        g2.drawString("Generation", leftMargin + graphW/2 - 40, h - 10);

        // Draw fitness line
        for (int i = 1; i < size; i++)
        {
            int x1 = leftMargin + (i-1)*graphW/size;
            int y1 = topMargin + graphH - (int)(history.get(i-1)/max*graphH);
            int x2 = leftMargin + i*graphW/size;
            int y2 = topMargin + graphH - (int)(history.get(i)/max*graphH);
            g2.setColor(new Color(80,180,255));
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(x1,y1,x2,y2);
        }

        // Draw hover marker if applicable
        if (hoverGen >= 0 && hoverGen < size)
        {
            int hx = leftMargin + hoverGen * graphW / size;
            int hy = topMargin + graphH - (int)(history.get(hoverGen)/max*graphH);
            g2.setColor(new Color(255,220,80));
            g2.fillOval(hx-5, hy-5, 10, 10);
        }
    }
    // DARK MODE PATCH END
}
