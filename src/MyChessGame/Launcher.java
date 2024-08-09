package MyChessGame;

import java.awt.*;
import java.awt.event.WindowEvent;
import javax.swing.*;

import MyChessGame.gamemode.GamePanel;
import MyChessGame.startpage.MultiplayerMenuPanel;
import MyChessGame.startpage.StartMenuPanel;
import MyChessGame.startpage.VariantsMenuPanel;
import javafx.scene.image.Image;

public class Launcher {
    private JPanel mainPanel;
    private final JFrame jf;
    private CardLayout cl;
    private GamePanel gamePanel;


    public Launcher() {
        this.jf = new JFrame();             // creating a new JFrame object
        this.jf.setTitle("My Chess Game"); // setting the title of the JFrame window.
        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initUIComponents() {
        this.mainPanel = new JPanel(); // create a new main panel
        JPanel startPanel = new StartMenuPanel(this); // create a new start panel
        JPanel variantsPanel = new VariantsMenuPanel(this); // create a new variants panel
        this.gamePanel = new GamePanel();


        cl = new CardLayout(); // creating a new CardLayout Panel
        this.mainPanel.setLayout(cl); // set the layout of the main panel to our card layout
        this.mainPanel.add(startPanel, "start"); // add the start panel to the main panel
        this.mainPanel.add(gamePanel, "game"); // add the game panel to the main panel
        this.mainPanel.add(variantsPanel, "variants"); // add the variants panel to the main panel
        this.jf.add(mainPanel); // add the main panel to the JFrame
        this.jf.setResizable(false); // make the JFrame not resizable
        this.setFrame("start"); // set the current panel to start panel
    }

    public void startGame(String player1, String player2, String path1, String path2, int gameTime) {
        //System.out.println("Starting game with players: " + player1 + " and " + player2);
        //System.out.println("Avatars: " + path1 + " and " + path2);
        //System.out.println("Game time: " + gameTime + " minutes");

        gamePanel.initialize(player1, player2, path1, path2, gameTime);
        setFrame("game");
    }

    public void setFrame(String type) {
        this.jf.setVisible(false); // hide the JFrame

        if ("multiplayer".equals(type) || "bots".equals(type)) {
            // Remove the existing multiplayer panel if necessary
            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof MultiplayerMenuPanel) {
                    mainPanel.remove(comp);
                    break;
                }
            }
            JPanel multiplayerPanel = new MultiplayerMenuPanel(this);
            this.mainPanel.add(multiplayerPanel, "multiplayer");
        }

        switch (type) {
            case "start":
                this.jf.setSize(1024, 820); // Set the size for the start panel
                break;
            case "variants":
                this.jf.setSize(1024, 820); // Set the size for the start panel
                break;
            case "multiplayer":
            case "bots":
                this.jf.setSize(1024, 820); // Set the size for the multiplayer/bots panel
                break;
            case "game":
                this.jf.setSize(1024, 820); // Set the size for the game panel
                break;
        }
        this.cl.show(mainPanel, type); // change current panel shown on main panel to the panel denoted by type.
        this.jf.setVisible(true); // show the JFrame
    }

    public JFrame getJf() {
        return jf;
    }

    public void closeGame() {
        this.jf.dispatchEvent(new WindowEvent(this.jf, WindowEvent.WINDOW_CLOSING));
    }

    public static void main(String[] args) {
        System.setProperty("javafx.embed.swing", "true");
        System.setProperty("javafx.logging", "none");
        SwingUtilities.invokeLater(() -> {
            Launcher launcher = new Launcher();
            launcher.initUIComponents();
        });
    }
}
