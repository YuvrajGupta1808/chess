package MyChessGame.structure;

import javax.swing.*;
import java.awt.*;

public class EvaluationBar extends JPanel {
    private double evaluation;
    private boolean evalOn; // To track if evaluation is on or off

    public EvaluationBar() {
        this.evaluation = 0.0;
        this.evalOn = true; // Default to evaluation being on
        this.setPreferredSize(new Dimension(20, 500)); // Adjust width and default height as needed
    }

    public void setEvaluation(double evaluation) {
        this.evaluation = evaluation;
        repaint();  // Ensure the bar is redrawn with the updated evaluation
    }

    public void toggleEvalOnOff(boolean isOn) {
        this.evalOn = isOn;
        repaint(); // Repaint to update the UI based on eval state
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!evalOn) {
            // When evaluation is off, fill with brown color
            g.setColor(new Color(139, 69, 19)); // Brown color
            g.fillRect(0, 0, getWidth(), getHeight());
            return; // Skip drawing the evaluation bar
        }

        int totalHeight = getHeight();  // Use dynamic height
        int barWidth = getWidth();
        int halfHeight = totalHeight / 2;

        double maxEval = 18.0;
        double scaledEvaluation = Math.max(-maxEval, Math.min(maxEval, evaluation));

        int whiteHeight = (int) ((0.5 + scaledEvaluation / (2 * maxEval)) * totalHeight);
        int blackHeight = totalHeight - whiteHeight;

        // Draw the white part
        g.setColor(Color.WHITE);
        g.fillRect(0, totalHeight - whiteHeight, barWidth, whiteHeight);

        // Draw the black part
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, barWidth, blackHeight);

        // Draw the red center line
        g.setColor(Color.RED);
        g.drawLine(0, halfHeight, barWidth, halfHeight);
    }
}
