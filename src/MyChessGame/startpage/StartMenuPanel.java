package MyChessGame.startpage;

import MyChessGame.Launcher;
import MyChessGame.gamemode.GamePanel;
import MyChessGame.structure.SoundPlayer;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class StartMenuPanel extends JPanel {

    private BufferedImage menuBackground;
    private final Launcher lf;
    private Clip backgroundClip;


    public StartMenuPanel(Launcher lf) {
        this.lf = lf;
        try {
            menuBackground = ImageIO.read(this.getClass().getClassLoader().getResource("Background.png"));
        } catch (IOException e) {
            System.out.println("Error cant read menu background");
            e.printStackTrace();
            System.exit(-3);
        }
        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK); // Set the background to black


        playBackgroundMusic("background_music.wav");

        // Create left panel with buttons
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(300, 600)); // Set width for the left panel


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10); // Padding between buttons
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton multiplayerButton = createRoundedButton("Multiplayer");
        multiplayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playSound("/sounds/notify.wav");
                GamePanel.isChess960 = false;
                GamePanel.isAI = false; // Ensure AI mode is disabled for multiplayer
                lf.setFrame("multiplayer");
            }
        });
        leftPanel.add(multiplayerButton, gbc);

        gbc.gridy = 1;
        JButton botsButton = createRoundedButton("Bots");
        botsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playSound("/sounds/notify.wav");
                GamePanel.isChess960 = false;
                GamePanel.isAI = true; // Enable AI mode
                lf.setFrame("multiplayer");
            }
        });
        leftPanel.add(botsButton, gbc);

        gbc.gridy = 2;
        JButton variantsButton = createRoundedButton("Variants");
        variantsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playSound("/sounds/notify.wav");
                GamePanel.isAI = false; // Ensure AI mode is disabled for multiplayer
                GamePanel.isChess960 = true;
                lf.setFrame("variants");
            }
        });
        leftPanel.add(variantsButton, gbc);
        this.add(leftPanel, BorderLayout.WEST);

        gbc.gridy = 3;
        JButton exit = createRoundedButton("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the application immediately
            }
        });
        leftPanel.add(exit, gbc);
        this.add(leftPanel, BorderLayout.WEST);



        // Create a container panel with BorderLayout
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setOpaque(false);

        // Create a panel to hold the bottom right panel and center it vertically and horizontally
        JPanel centeredPanel = new JPanel(new GridBagLayout());
        centeredPanel.setOpaque(false);

        // Create a simple panel at the bottom right
        JPanel rightBottomPanel = new JPanel();
        rightBottomPanel.setPreferredSize(new Dimension(600, 600));
        rightBottomPanel.setBackground(Color.BLACK);
        JLabel gifLabel = new JLabel(new ImageIcon(this.getClass().getClassLoader().getResource("tenor.gif")));
        rightBottomPanel.add(gifLabel);

        GridBagConstraints gbcRightBottom = new GridBagConstraints();
        gbcRightBottom.gridx = 1;
        gbcRightBottom.gridy = 1;
        gbcRightBottom.insets = new Insets(150, 0, 0, 0); // Adjusting padding to move the GIF down
        gbcRightBottom.anchor = GridBagConstraints.SOUTHEAST;
        centeredPanel.add(rightBottomPanel, gbcRightBottom);

        containerPanel.add(centeredPanel, BorderLayout.CENTER);
        this.add(containerPanel, BorderLayout.CENTER);
    }

    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50); // Larger rounding for corners
                super.paintComponent(g2);
            }

            @Override
            public void updateUI() {
                super.updateUI();
                setOpaque(false);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setFocusPainted(false);
            }
        };
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Increase padding inside the button
        button.setFont(new Font("TT Rationalist", Font.BOLD, 28)); // Set font size to 28
        button.setPreferredSize(new Dimension(250, 80)); // Set button size
        return button;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground, 0, 0, getWidth(), getHeight(), this);
    }

    public void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
        }
    }

    @Override
    public void removeNotify() {
        super.removeNotify();
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
        }
    }

    private void playBackgroundMusic(String musicFile) {
        try {
            URL soundURL = this.getClass().getClassLoader().getResource(musicFile);
            if (soundURL == null) {
                System.out.println("Music file not found: " + musicFile);
                return;
            }
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioInputStream);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the music continuously
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
