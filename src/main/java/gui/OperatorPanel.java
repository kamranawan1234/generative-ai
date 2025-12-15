package gui;

import java.awt.*;
import javax.swing.*;

public class OperatorPanel extends JPanel {
  private JLabel currentOperator;

  public OperatorPanel() {
    setLayout(new BorderLayout());
    setBorder(
        BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60)),
            "Current Operator",
            0,
            0,
            null,
            Color.WHITE));
    setBackground(new Color(30, 30, 30));
    currentOperator = new JLabel("None");
    currentOperator.setFont(new Font("Consolas", Font.BOLD, 18));
    currentOperator.setForeground(new Color(220, 220, 220));
    currentOperator.setHorizontalAlignment(SwingConstants.CENTER);
    add(currentOperator, BorderLayout.CENTER);
  }

  public void setOperator(String op) {
    currentOperator.setText(op);
  }
}
