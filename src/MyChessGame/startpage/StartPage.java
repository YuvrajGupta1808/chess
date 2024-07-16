package MyChessGame.startpage;

import MyChessGame.ChessGame;
import MyChessGame.gamemode.MultiplayerGame;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class StartPage extends JPanel {

    private final ChessGame launcher;
    private BufferedImage menuBackground;

    public StartPage(ChessGame launcher) {
        this.launcher = launcher;
        this.setLayout(new BorderLayout());

        try {
            menuBackground = ImageIO.read(this.getClass().getClassLoader().getResource("images/Chess.png"));
        } catch (IOException e) {
            System.out.println("Error: can't read menu background");
            e.printStackTrace();
            System.exit(-3);
        }

        // Create a heading label
        JLabel heading = new JLabel("Chess Game", SwingConstants.CENTER);
        heading.setFont(new Font("Serif", Font.BOLD, 48));
        heading.setForeground(Color.WHITE);  // Adjust color as needed
        this.add(heading, BorderLayout.NORTH);

        // Create a panel for buttons with transparent background
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        // Create buttons with images
        JButton startButton = createImageButton("images/PlayMultiplayer.png", 500, 100);
        startButton.addActionListener(e -> launcher.setFrame("multiplayerGame"));

        JButton aiButton = createImageButton("images/PlayBots.png", 500, 100);
        aiButton.addActionListener(e -> launcher.setFrame("game")); // Replace with appropriate logic

        JButton threeDButton = createImageButton("images/PlayIn3D.png", 500, 100);
        threeDButton.addActionListener(e -> launcher.setFrame("game")); // Replace with appropriate logic

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding between buttons
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(startButton, gbc);

        gbc.gridy = 1;
        buttonPanel.add(aiButton, gbc);

        gbc.gridy = 2;
        buttonPanel.add(threeDButton, gbc);

        // Center the button panel in the background
        this.add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createImageButton(String imagePath, int width, int height) {
        JButton button = new JButton();
        try {
            BufferedImage img = ImageIO.read(this.getClass().getClassLoader().getResource(imagePath));
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImg));
        } catch (IOException e) {
            e.printStackTrace();
        }
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        button.setPreferredSize(new Dimension(width, height));  // Set size to match the image dimensions
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(menuBackground, 0, 0, getWidth(), getHeight(), this);
    }
}
