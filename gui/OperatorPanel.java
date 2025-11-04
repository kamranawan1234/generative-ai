package gui;

import javax.swing.*;
import java.awt.*;

public class OperatorPanel extends JPanel {

    private JLabel currentOperator;

    public OperatorPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Current Operator"));
        currentOperator = new JLabel("None");
        currentOperator.setFont(new Font("Arial", Font.BOLD, 16));
        currentOperator.setHorizontalAlignment(SwingConstants.CENTER);
        add(currentOperator, BorderLayout.CENTER);
    }

    public void setOperator(String op) {
        currentOperator.setText(op);
    }
}
