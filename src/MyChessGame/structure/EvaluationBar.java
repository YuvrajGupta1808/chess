package MyChessGame.structure;

import javax.swing.*;
import java.awt.*;

public class EvaluationBar extends JPanel {
    private double evaluation;

    public EvaluationBar() {
        this.evaluation = 0.0;
        this.setPreferredSize(new Dimension(20, 500)); // Adjust size as needed
    }

    public void setEvaluation(double evaluation) {
        System.out.println("Setting evaluation to: " + evaluation); // Debug statement
        this.evaluation = evaluation;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int totalHeight = 700;
        int barWidth = getWidth();
        int halfHeight = totalHeight / 2;
        int whiteHeight;
        int blackHeight;

        double maxEval = 18.0;
        double scaledEvaluation = Math.max(-maxEval, Math.min(maxEval, evaluation));
        int barHeight = (int) ((scaledEvaluation / maxEval) * halfHeight);

        whiteHeight = (int) (Math.max(0, (0.5 + evaluation / 20.0) * totalHeight));
        blackHeight = totalHeight - whiteHeight;


        // Draw the white part
        g.setColor(Color.WHITE);
        g.fillRoundRect(0, totalHeight - whiteHeight, barWidth, whiteHeight, 20, 20);

        // Draw the black part
        g.setColor(Color.BLACK);
        g.fillRoundRect(0, 0, barWidth, blackHeight, 20, 20);

        // Draw the red center line
        g.setColor(Color.RED);
        g.drawLine(0, halfHeight, barWidth, halfHeight);

        //System.out.println("Drawing white part with height: " + whiteHeight); // Debug statement
        //System.out.println("Drawing black part with height: " + blackHeight); // Debug statement
    }
}
