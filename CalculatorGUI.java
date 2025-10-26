import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CalculatorGUI extends JFrame implements ActionListener {
    private final JTextField display = new JTextField("0");

    private String currentInput = "";     // what's being typed now
    private Double firstNum = null;       // stored left operand
    private String operator = null;       // "+", "-", "*", "/"
    private boolean justEvaluated = false;

    public CalculatorGUI() {
        setTitle("Simple Calculator");
        setSize(320, 440);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // display
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setFont(new Font("Segoe UI", Font.BOLD, 28));
        display.setEditable(false);
        display.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        display.setBackground(new Color(245,245,245));

        // buttons
        String[] labels = {
                "7","8","9","/",
                "4","5","6","*",
                "1","2","3","-",
                "0",".","=","+",
                "C","⌫"
        };
        JPanel grid = new JPanel(new GridLayout(5,4,10,10));
        grid.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        grid.setBackground(new Color(235,235,235));

        for (String lab : labels) {
            JButton b = new JButton(lab);
            b.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            b.setFocusPainted(false);
            b.setBackground(Color.WHITE);
            b.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));
            b.addActionListener(this);
            grid.add(b);
        }

        setLayout(new BorderLayout());
        add(display, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.matches("[0-9]")) {
            if (justEvaluated) { currentInput = ""; justEvaluated = false; }
            if (currentInput.equals("0")) currentInput = "";   // avoid leading zeros
            currentInput += cmd;
            display.setText(currentInput);
            return;
        }

        if (cmd.equals(".")) {
            if (justEvaluated) { currentInput = ""; justEvaluated = false; }
            if (currentInput.isEmpty()) currentInput = "0";
            if (!currentInput.contains(".")) {
                currentInput += ".";
                display.setText(currentInput);
            }
            return;
        }

        if (cmd.matches("[+\\-*/]")) {
            setOperator(cmd);
            return;
        }

        if (cmd.equals("=")) {
            evaluateIfPossible();
            return;
        }

        if (cmd.equals("C")) {
            clearAll();
            return;
        }

        if (cmd.equals("⌫")) {
            if (!currentInput.isEmpty() && !justEvaluated) {
                currentInput = currentInput.substring(0, currentInput.length() - 1);
                display.setText(currentInput.isEmpty() ? "0" : currentInput);
            }
        }
    }

    /* ---------- logic ---------- */

    private void setOperator(String op) {
        // If user presses an operator right after result, continue from that result
        if (justEvaluated) { justEvaluated = false; }

        if (!currentInput.isEmpty()) {
            double entry = Double.parseDouble(currentInput);
            if (firstNum == null) {
                firstNum = entry;
            } else if (operator != null) {
                // chain: compute previous op first
                firstNum = compute(firstNum, entry, operator);
                display.setText(format(firstNum));
            }
            currentInput = "";
        } else if (firstNum == null) {
            // no entry yet, treat display as first number
            firstNum = parseDisplay();
        }
        operator = op; // set/replace operator
    }

    private void evaluateIfPossible() {
        if (operator == null || currentInput.isEmpty()) {
            // nothing to do
            return;
        }
        double second = Double.parseDouble(currentInput);
        try {
            double result = compute(firstNum == null ? 0 : firstNum, second, operator);
            display.setText(format(result));
            // prepare for next operations
            currentInput = String.valueOf(result);
            firstNum = null;
            operator = null;
            justEvaluated = true;
        } catch (ArithmeticException ex) {
            display.setText("Error");
            clearAll(); // reset state
        }
    }

    private double compute(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/":
                if (Math.abs(b) < 1e-15) throw new ArithmeticException("div0");
                return a / b;
            default: return b;
        }
    }

    private void clearAll() {
        currentInput = "";
        firstNum = null;
        operator = null;
        justEvaluated = false;
        display.setText("0");
    }

    private double parseDisplay() {
        String t = display.getText();
        if (t.equals("Error") || t.isEmpty()) return 0.0;
        try { return Double.parseDouble(t); } catch (NumberFormatException e) { return 0.0; }
    }

    private String format(double v) {
        if (v == (long) v) return String.valueOf((long) v);
        return String.valueOf(v);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalculatorGUI().setVisible(true));
    }
}

