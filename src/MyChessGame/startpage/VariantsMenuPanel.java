package MyChessGame.startpage;

import MyChessGame.Launcher;
import MyChessGame.gamemode.GamePanel;
import MyChessGame.structure.SoundPlayer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class VariantsMenuPanel extends JPanel {

    private BufferedImage menuBackground;
    private final Launcher lf;
    public ImageView avatar1;
    public ImageView avatar2;
    private ImageView whitePawn;
    private ImageView blackPawn;
    private Button changeAvatar1;
    private Button changeAvatar2;
    private Button changeGender1;
    private Button changeGender2;
    public TextField player1Name;
    public TextField player2Name;
    private Button startGame;
    public Button time5Min;
    public Button time10Min;
    public Button time15Min;
    public int selectedTime = 10; // Default to 10 minutes

    private final Image defaultMaleAvatar = new Image(getClass().getResourceAsStream("/images/nopic.png"));
    private final Image defaultFemaleAvatar = new Image(getClass().getResourceAsStream("/images/title.png"));
    private final String defaultMaleAvatarUrl = getClass().getResource("/images/nopic.png").toString();
    private final String defaultFemaleAvatarUrl = getClass().getResource("/images/title.png").toString();
    private final Image whitePawnImage = new Image(getClass().getResourceAsStream("/pieces/p1.png"));
    private final Image blackPawnImage = new Image(getClass().getResourceAsStream("/pieces/p2.png"));

    public VariantsMenuPanel(Launcher lf) {
        this.lf = lf;
        try {
            menuBackground = ImageIO.read(this.getClass().getClassLoader().getResource("Background.png"));
        } catch (IOException e) {
            System.out.println("Error can't read menu background");
            e.printStackTrace();
            System.exit(-3);
        }
        this.setLayout(new BorderLayout());
        this.setBackground(Color.BLACK); // Set the background to black

        // Create left panel with buttons
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(300, 600)); // Set width for the left panel

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 10, 20, 10); // Padding between buttons
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton chess960Button = createRoundedButton("Chess960");
        chess960Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playSound("/sounds/notify.wav");
                GamePanel.isChess960 = true; // Enable Chess960 mode
                handleStartGame();
            }
        });
        leftPanel.add(chess960Button, gbc);

        gbc.gridy = 1;
        JButton kingOfTheHillButton = createRoundedButton("Amazon");
        kingOfTheHillButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playSound("/sounds/notify.wav");
                GamePanel.isChess960 = false; // Disable Chess960 mode
                GamePanel.isamazon = true;
                handleStartGame();
            }
        });
        leftPanel.add(kingOfTheHillButton, gbc);

        gbc.gridy = 2;
        JButton threeCheckButton = createRoundedButton("Rook Switch");
        threeCheckButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundPlayer.playSound("/sounds/notify.wav");
                GamePanel.isChess960 = false; // Disable Chess960 mode
                GamePanel.isrookswitch = true;
                handleStartGame();
            }
        });
        leftPanel.add(threeCheckButton, gbc);

        this.add(leftPanel, BorderLayout.WEST);

        // Create a container panel with BorderLayout
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setOpaque(false);

        // Create a panel to hold the bottom right panel and center it vertically and horizontally
        JPanel centeredPanel = new JPanel(new GridBagLayout());
        centeredPanel.setOpaque(false);

        // Create simple panel at the bottom right
        JFXPanel rightBottomPanel = new JFXPanel();
        rightBottomPanel.setPreferredSize(new Dimension(600, 500));
        rightBottomPanel.setBackground(Color.BLACK);

        // Load the FXML file
        Platform.runLater(() -> initFX(rightBottomPanel));

        GridBagConstraints gbcRightBottom = new GridBagConstraints();
        gbcRightBottom.gridx = 0;
        gbcRightBottom.gridy = 0;
        gbcRightBottom.anchor = GridBagConstraints.NORTHWEST; // Align top-left
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

    private void initFX(JFXPanel fxPanel) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL fxmlUrl = getClass().getResource("/fxml/Multiplayer.fxml");
            if (fxmlUrl == null) {
                throw new IOException("FXML file not found.");
            }
            loader.setLocation(fxmlUrl);
            Pane root = loader.load();

            avatar1 = (ImageView) loader.getNamespace().get("avatar1");
            avatar2 = (ImageView) loader.getNamespace().get("avatar2");
            changeAvatar1 = (Button) loader.getNamespace().get("changeAvatar1");
            changeAvatar2 = (Button) loader.getNamespace().get("changeAvatar2");
            changeGender1 = (Button) loader.getNamespace().get("changeGender1");
            changeGender2 = (Button) loader.getNamespace().get("changeGender2");
            player1Name = (TextField) loader.getNamespace().get("player1Name");
            player2Name = (TextField) loader.getNamespace().get("player2Name");
            startGame = (Button) loader.getNamespace().get("startGame");
            whitePawn = (ImageView) loader.getNamespace().get("whitePawn");
            blackPawn = (ImageView) loader.getNamespace().get("blackPawn");

            // New time selection buttons
            time5Min = (Button) loader.getNamespace().get("time5Min");
            time10Min = (Button) loader.getNamespace().get("time10Min");
            time15Min = (Button) loader.getNamespace().get("time15Min");

            // Hide time labels and second player options if AI mode
            if (GamePanel.isAI) {
                //System.out.println("AI Mode is enabled");
                time5Min.setVisible(false);
                time10Min.setVisible(false);
                time15Min.setVisible(false);
                changeAvatar2.setVisible(false);
                changeGender2.setVisible(false);
            } else {
                //System.out.println("AI Mode is disabled");
            }

            // Set default avatars
            avatar1.setImage(defaultMaleAvatar);
            avatar2.setImage(GamePanel.isAI ? new Image(getClass().getResource("/images/img.png").toExternalForm()) : defaultMaleAvatar);
            player1Name.setText("Player 1");
            player2Name.setText(GamePanel.isAI ? "Stockfish" : "Player 2");

            whitePawn.setImage(whitePawnImage);
            blackPawn.setImage(blackPawnImage);

            // Set event handlers
            changeAvatar1.setOnAction(e -> handleChangeAvatar(avatar1));
            changeAvatar2.setOnAction(e -> handleChangeAvatar(avatar2));
            changeGender1.setOnAction(e -> handleChangeGender(avatar1));
            changeGender2.setOnAction(e -> handleChangeGender(avatar2));

            // Event handlers for time selection buttons
            time5Min.setOnAction(e -> handleTimeSelection(5));
            time10Min.setOnAction(e -> handleTimeSelection(10));
            time15Min.setOnAction(e -> handleTimeSelection(15));

            startGame.setOnAction(e -> handleStartGame());

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
            fxPanel.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleChangeAvatar(ImageView avatar) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Avatar Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            avatar.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    private void handleChangeGender(ImageView avatar) {
        Image currentImage = avatar.getImage();
        if (currentImage.equals(defaultMaleAvatar)) {
            avatar.setImage(defaultFemaleAvatar);
        } else {
            avatar.setImage(defaultMaleAvatar);
        }
    }

    private void handleTimeSelection(int minutes) {
        selectedTime = minutes;
        //System.out.println("Game Time: " + selectedTime);
    }

    private void handleStartGame() {
        String player1 = player1Name.getText();
        String player2 = player2Name.getText();
        String path1 = avatar1.getImage().getUrl() != null ? avatar1.getImage().getUrl() : defaultMaleAvatarUrl;
        String path2 = avatar2.getImage().getUrl() != null ? avatar2.getImage().getUrl() : defaultMaleAvatarUrl;
        lf.startGame(player1, player2, path1, path2, selectedTime);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground, 0, 0, getWidth(), getHeight(), this);
    }
}
