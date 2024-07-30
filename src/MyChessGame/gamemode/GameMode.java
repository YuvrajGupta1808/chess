package MyChessGame.gamemode;

import MyChessGame.board.Clock;
import MyChessGame.board.Mouse;
import MyChessGame.board.Move;
import MyChessGame.piece.*;
import MyChessGame.ChessGame;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameMode extends JPanel implements Runnable {
    private Piece[][] board;
    private List<Piece> wPieces;
    private List<Piece> bPieces;
    private boolean whiteTurn = true;
    private King wk, bk;
    private Clock whiteClock;
    private Clock blackClock;
    private JLabel whiteTimeLabel;
    private JLabel blackTimeLabel;

    Mouse mouse = new Mouse();
    private Piece selectedPiece;
    private int selectedRow;
    private int selectedCol;
    private Move moveLogic;

    private final ChessGame launcher;

    public GameMode(ChessGame launcher) {
        this.launcher = launcher;
        setLayout(new BorderLayout());

    }

    public void initializeGame() {
        JPanel boardPanel = new JPanel(new GridLayout(8, 8));
        JPanel infoPanel = new JPanel(new BorderLayout()); // Panel to hold player info and time

        initializeBoard(boardPanel);

        infoPanel.add(createInfoPanel("Player 1", "images/nopic.png", true,false), BorderLayout.NORTH);
        infoPanel.add(createInfoPanel("Player 2", "images/nopic.png", true,true), BorderLayout.SOUTH);
        infoPanel.add(createTimePanel(), BorderLayout.CENTER);
        moveLogic = new Move(board); // Initialize moveLogic
        add(boardPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);

        revalidate();  // Ensure the panel is updated with new components
    }

    private void initializeBoard(JPanel boardPanel) {
        try (BufferedReader mapReader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("board.csv"))))) {
            String line;
            int row = 0;

            board = new Piece[8][8];
            wPieces = new ArrayList<>();
            bPieces = new ArrayList<>();

            while ((line = mapReader.readLine()) != null && row < 8) {
                String[] objs = line.split(",");
                for (int col = 0; col < objs.length - 1; col++) {
                    String gameItem = objs[col];
                    Piece piece = createPiece(gameItem, col, row);
                    if (piece != null) {
                        board[row][col] = piece;
                        if (piece.getColor() == 0) {
                            bPieces.add(piece);
                        } else {
                            wPieces.add(piece);
                        }
                    }
                    JPanel square = createSquare(row, col);
                    if (piece != null) {
                        square.add(new JLabel(new ImageIcon(getClass().getClassLoader().getResource(piece.getImagePath()))));
                    }
                    boardPanel.add(square);
                }
                row++;
            }
            // Set Kings
            for (Piece piece : wPieces) {
                if (piece instanceof King) {
                    wk = (King) piece;
                }
            }
            for (Piece piece : bPieces) {
                if (piece instanceof King) {
                    bk = (King) piece;
                }
            }
        } catch (NullPointerException e) {
            System.err.println("Resource file not found: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addMouseMotionListener(mouse);
        addMouseListener(mouse);
    }

    private Piece createPiece(String gameItem, int col, int row) {
        int pieceCode = Integer.parseInt(gameItem);
        switch (pieceCode) {
            case 2: return new Pawn(1, row, col, "resources/pieces/wpawn.png");
            case 1: return new Pawn(0, row, col, "resources/pieces/bpawn.png");
            case 4: return new King(1, row, col, "resources/pieces/wking.png");
            case 5: return new King(0, row, col, "resources/pieces/bking.png");
            case 3: return new Queen(1, row, col, "resources/pieces/wqueen.png");
            case 6: return new Queen(0, row, col, "resources/pieces/bqueen.png");
            case 10: return new Bishop(1, row, col, "resources/pieces/wbishop.png");
            case 7: return new Bishop(0, row, col, "resources/pieces/bbishop.png");
            case 11: return new Knight(1, row, col, "resources/pieces/wknight.png");
            case 8: return new Knight(0, row, col, "resources/pieces/bknight.png");
            case 12: return new Rook(1, row, col, "resources/pieces/wrook.png");
            case 9: return new Rook(0, row, col, "resources/pieces/brook.png");
            default: return null;
        }
    }

    private JPanel createSquare(int row, int col) {
        JPanel square = new JPanel(new BorderLayout());
        square.setPreferredSize(new Dimension(100, 100));
        if ((row + col) % 2 == 0) {
            square.setBackground(Color.WHITE);
        } else {
            square.setBackground(new Color(139, 69, 19));
        }
        return square;
    }

    private void update() {
        if (selectedPiece != null) {
            if (whiteTurn && selectedPiece.getColor() == 0) {
                // If it's white's turn, and the selected piece is black, deselect it
                selectedPiece = null;
            } else if (!whiteTurn && selectedPiece.getColor() == 1) {
                // If it's black's turn, and the selected piece is white, deselect it
                selectedPiece = null;
            }
        }
        else{
            simulate();
        }
    }

    private void simulate() {
        if (selectedPiece != null) {
            selectedPiece.setPosition(selectedRow - 50, selectedCol-50); // Adjust the position to center the piece under the cursor
        }
    }


    private JPanel createInfoPanel(String text, String imagePath, boolean isPlayer, boolean isWhite) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(224, 100));
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        if (isPlayer && imagePath != null) {
            try {
                Image img = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource(imagePath))).getImage();
                Image scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                JLabel picLabel = new JLabel(new ImageIcon(scaledImg));
                panel.add(picLabel, BorderLayout.WEST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!isPlayer) {
            JLabel timeLabel = new JLabel();
            if (isWhite) {
                whiteTimeLabel = timeLabel;
            } else {
                blackTimeLabel = timeLabel;
            }
            timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
            timeLabel.setFont(new Font("Arial", Font.BOLD, 20));
            panel.add(timeLabel, BorderLayout.CENTER);
        }
        return panel;
    }

    private JPanel createTimePanel() {
        JPanel timePanel = new JPanel();
        timePanel.setLayout(new BorderLayout());
        whiteClock = new Clock(0, 30, 0); // Initial time of 30 minutes
        blackClock = new Clock(0, 30, 0); // Initial time of 30 minutes
        whiteTimeLabel = new JLabel(whiteClock.getTime());
        blackTimeLabel = new JLabel(blackClock.getTime());
        whiteTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        blackTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timePanel.add(whiteTimeLabel, BorderLayout.NORTH);
        timePanel.add(blackTimeLabel, BorderLayout.SOUTH);
        new Timer(1000, e -> {
            if (whiteTurn) {
                whiteClock.decr();
                whiteTimeLabel.setText(whiteClock.getTime());
            } else {
                blackClock.decr();
                blackTimeLabel.setText(blackClock.getTime());
            }
            if (whiteClock.outOfTime() || blackClock.outOfTime()) {
                ((Timer) e.getSource()).stop();
                JOptionPane.showMessageDialog(this, "Time's up!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            }
        }).start();
        return timePanel;
    }

    @Override
    public void run() {
        initializeGame();
        while (true) {
            repaint();
            try {
                Thread.sleep(16); // approximately 60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (board != null) {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    Piece piece = board[row][col];
                    if (piece != null) {
                        g.drawImage(new ImageIcon(getClass().getClassLoader().getResource(piece.getImagePath())).getImage(), col * 100, row * 100, this);
                    }
                }
            }
        }
    }



}
