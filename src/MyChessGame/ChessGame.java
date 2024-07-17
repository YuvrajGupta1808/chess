package MyChessGame;

import java.awt.*;
import java.awt.event.WindowEvent;
import javax.swing.*;

import MyChessGame.gamemode.GameMode;
import MyChessGame.gamemode.MultiplayerGame;
import MyChessGame.startpage.EndPage;
import MyChessGame.startpage.StartPage;

public class ChessGame {

    private JPanel mainPanel;
    private GameMode gamePanel;
    private final JFrame jf;
    private CardLayout cl;

    public ChessGame(){
        this.jf = new JFrame();
        this.jf.setTitle("Chess Game");
        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jf.setSize(800, 600);  // Set initial size
        this.jf.setResizable(false);  // Make JFrame not resizable
        this.initMenuBar();
    }

    private void initUIComponents(){
        this.mainPanel = new JPanel();
        JPanel startPanel = new StartPage(this);
        JPanel multiplayerGame = new MultiplayerGame(this);
        this.gamePanel = new GameMode(this);
        JPanel endPanel = new EndPage(this);
        cl = new CardLayout();
        this.mainPanel.setLayout(cl);
        this.mainPanel.add(startPanel, "start");
        this.mainPanel.add(multiplayerGame, "multiplayerGame");
        this.mainPanel.add(gamePanel, "game");
        this.mainPanel.add(endPanel, "end");
        this.jf.add(mainPanel);
        this.jf.setResizable(false);
        this.setFrame("start");
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> setFrame("start"));
        JMenuItem saveGameItem = new JMenuItem("Save Game");
        // Add action listener for saving the game
        JMenuItem loadGameItem = new JMenuItem("Load Game");
        // Add action listener for loading the game
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> closeGame());

        fileMenu.add(newGameItem);
        fileMenu.add(saveGameItem);
        fileMenu.add(loadGameItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem rulesItem = new JMenuItem("Rules");
        // Add action listener to show game rules
        JMenuItem aboutItem = new JMenuItem("About");
        // Add action listener to show about information

        helpMenu.add(rulesItem);
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        this.jf.setJMenuBar(menuBar);
    }

    public void setFrame(String type){
        this.jf.setVisible(false);
        switch (type) {
            case "start" -> this.jf.setSize(1024, 800);  // Set size for start panel
            case "multiplayerGame" -> this.jf.setSize(1024, 800);  // Set size for multiplayer setup panel
            case "game" -> {
                this.jf.setSize(1024, 800);  // Update with appropriate dimensions
                (new Thread(this.gamePanel)).start();
            }
            case "end" -> this.jf.setSize(1024, 800);  // Update with appropriate dimensions
        }
        this.cl.show(mainPanel, type);
        this.jf.setVisible(true);
    }

    public GameMode getGameMode() {
        return gamePanel;
    }

    public JFrame getJf() {
        return jf;
    }

    public void closeGame(){
        this.jf.dispatchEvent(new WindowEvent(this.jf, WindowEvent.WINDOW_CLOSING));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChessGame launcher = new ChessGame();
            launcher.initUIComponents();
        });
    }
}
