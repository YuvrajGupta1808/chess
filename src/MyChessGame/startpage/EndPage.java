package MyChessGame.startpage;

import MyChessGame.ChessGame;
import javax.swing.*;
import java.awt.*;

public class EndPage extends JPanel {

    private final ChessGame launcher;

    public EndPage(ChessGame launcher) {
        this.launcher = launcher;
        this.setLayout(new BorderLayout());
        JLabel title = new JLabel("Game Over", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 48));
        this.add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> launcher.setFrame("start"));
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> launcher.closeGame());
        buttonPanel.add(restartButton);
        buttonPanel.add(exitButton);

        this.add(buttonPanel, BorderLayout.CENTER);
    }
}
