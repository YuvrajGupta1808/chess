package MyChessGame.gamemode;

import MyChessGame.ChessGame;
import MyChessGame.startpage.GameWindow;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MultiplayerGame extends JPanel {

    private final ChessGame launcher;
    private BufferedImage background;

    private JLabel player1PicLabel;
    private JLabel player2PicLabel;

    public MultiplayerGame(ChessGame launcher) {
        this.launcher = launcher;
        this.setLayout(new BorderLayout());

        try {
            background = ImageIO.read(this.getClass().getClassLoader().getResource("images/multiback.png"));
        } catch (IOException e) {
            System.out.println("Error: can't read background image");
            e.printStackTrace();
            System.exit(-3);
        }

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Player 1 picture and name
        JLayeredPane player1Pane = createImageUploadPane("images/nopic.png", e -> uploadPicture(player1PicLabel));
        JTextField player1NameField = createStyledTextField("Player 1");

        JPanel player1Panel = new JPanel(new GridBagLayout());
        player1Panel.setOpaque(false);
        GridBagConstraints p1Gbc = new GridBagConstraints();
        p1Gbc.insets = new Insets(40, 90, 20, 10);
        p1Gbc.gridx = 0;
        p1Gbc.gridy = 0;
        player1Panel.add(player1Pane, p1Gbc);
        p1Gbc.gridy = 1;
        p1Gbc.insets = new Insets(20, 90, 20, 0);
        player1Panel.add(player1NameField, p1Gbc);

        // Player 2 picture and name
        JLayeredPane player2Pane = createImageUploadPane("images/nopic.png", e -> uploadPicture(player2PicLabel));
        JTextField player2NameField = createStyledTextField("Player 2");

        JPanel player2Panel = new JPanel(new GridBagLayout());
        player2Panel.setOpaque(false);
        GridBagConstraints p2Gbc = new GridBagConstraints();
        p2Gbc.insets = new Insets(40, -10, 20, 20);
        p2Gbc.gridx = 0;
        p2Gbc.gridy = 0;
        player2Panel.add(player2Pane, p2Gbc);
        p2Gbc.gridy = 1;
        p2Gbc.insets = new Insets(20, 0, 20, 20);
        player2Panel.add(player2NameField, p2Gbc);

        // Add player panels to center panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(player1Panel, gbc);
        gbc.gridx = 1;
        centerPanel.add(player2Panel, gbc);

        // Time selection buttons
        JPanel timePanel = new JPanel(new GridLayout(1, 3, 20, 20));
        timePanel.setOpaque(false);
        JButton fiveMinButton = createRoundedButton("5 min", Color.BLACK, Color.WHITE);
        JButton tenMinButton = createRoundedButton("10 min", Color.BLACK, Color.WHITE);
        JButton fifteenMinButton = createRoundedButton("15 min", Color.BLACK, Color.WHITE);
        timePanel.add(fiveMinButton);
        timePanel.add(tenMinButton);
        timePanel.add(fifteenMinButton);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(timePanel, gbc);

        // Start game button
        JButton startGameButton = createRoundedButton("Start Game", Color.WHITE, Color.BLACK);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        centerPanel.add(startGameButton, gbc);

        this.add(centerPanel, BorderLayout.CENTER);

        // Add action listeners for time buttons
        fiveMinButton.addActionListener(e -> setSelectedTime("5 minutes"));
        tenMinButton.addActionListener(e -> setSelectedTime("10 minutes"));
        fifteenMinButton.addActionListener(e -> setSelectedTime("15 minutes"));

        // Add action listener for start game button
        startGameButton.addActionListener(e -> startGame(player1NameField.getText(), player2NameField.getText(), selectedTime));
    }

    private String selectedTime = "5 minutes"; // default time

    private void setSelectedTime(String time) {
        this.selectedTime = time;
    }

    private JButton createRoundedButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(textColor);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(getText(), x, y);
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(150, 50);
            }
        };
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setForeground(textColor);
        button.setFont(new Font("Roboto", Font.BOLD, 18));
        return button;
    }

    private void uploadPicture(JLabel picLabel) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                BufferedImage img = ImageIO.read(selectedFile);
                Image scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                picLabel.setIcon(new ImageIcon(scaledImg));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startGame(String player1Name, String player2Name, String selectedTime) {
        new GameWindow(player1Name, player2Name, 0, 0, Integer.parseInt(selectedTime.split(" ")[0]));
    }

    private JLabel createImageLabel(String imagePath) {
        JLabel label = new JLabel();
        try {
            BufferedImage img = ImageIO.read(this.getClass().getClassLoader().getResource(imagePath));
            Image scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaledImg));
        } catch (IOException e) {
            e.printStackTrace();
        }
        label.setPreferredSize(new Dimension(100, 100));
        return label;
    }

    private JTextField createStyledTextField(String text) {
        JTextField textField = new JTextField(text, 10);
        textField.setFont(new Font("Arial", Font.PLAIN, 20));
        textField.setHorizontalAlignment(JTextField.CENTER);
        return textField;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }

    private JLayeredPane createImageUploadPane(String imagePath, ActionListener uploadAction) {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(100, 100));

        JLabel imageLabel = createImageLabel(imagePath);
        imageLabel.setBounds(0, 0, 100, 100);
        imageLabel.setBorder(new LineBorder(Color.BLACK, 2)); // Add border to image label
        layeredPane.add(imageLabel, JLayeredPane.DEFAULT_LAYER);

        JButton uploadButton = createUploadButton();
        uploadButton.setBounds(70, 0, 30, 30);
        uploadButton.addActionListener(uploadAction);
        layeredPane.add(uploadButton, JLayeredPane.PALETTE_LAYER);

        if (imagePath.equals("images/nopic.png")) {
            player1PicLabel = imageLabel;
        } else {
            player2PicLabel = imageLabel;
        }

        return layeredPane;
    }

    private JButton createUploadButton() {
        JButton button = new JButton();
        try {
            BufferedImage img = ImageIO.read(this.getClass().getClassLoader().getResource("images/plus.png"));
            Image scaledImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImg));
        } catch (IOException e) {
            e.printStackTrace();
        }
        button.setContentAreaFilled(false);
        button.setBorderPainted(true); // Add border to upload button
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(Color.BLACK, 1)); // Add border to upload button
        return button;
    }
}
