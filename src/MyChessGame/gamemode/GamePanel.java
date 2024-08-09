package MyChessGame.gamemode;

import MyChessGame.Launcher;
import MyChessGame.Pieces.*;
import MyChessGame.structure.EvaluationBar;
import MyChessGame.structure.SoundPlayer;
import javafx.animation.FillTransition;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class GamePanel extends JPanel {
    private static final int TILE_SIZE = 90;
    private static final int BOARD_SIZE = 8;
    private static final int PIECE_SIZE = 60;
    private static final int MARGIN = 30;
    private BufferedImage[][] pieceImages;
    private Piece[][] board;
    private Point selectedPiece;
    private Piece selectedPieceType;
    private boolean dragging;
    private Point mousePos;
    private Point clickOffset;
    private boolean whiteTurn = true;
    private GameState gameState;
    private Stockfish stockfish;
    private EvaluationBar evaluationBar;
    public boolean rotate = false;
    private Color lightColor = new Color(222,184,135);
    private Color darkColor = new Color(139, 69, 19);
    private JFXPanel fxPanel;
    private Launcher launcher;

    private String player1;
    private String player2;
    private String player1Avatar;
    private String player2Avatar;
    private int gameTime;

    private Label player1NameLabel;
    private Label player2NameLabel;
    private ImageView player1Pic;
    private ImageView player2Pic;
    private Label player1Time;
    private Label player2Time;

    private Timer player1Timer;
    private Timer player2Timer;
    private int player1TimeRemaining;
    private int player2TimeRemaining;

    private ComboBox<String> themeComboBox;
    private javafx.scene.control.Button rotateButton;
    private javafx.scene.control.Button evalButton;

    private StackPane evaluationBarContainer;
    private JPanel rightBottomContainer;
    private Rectangle[][] rectangles = new Rectangle[BOARD_SIZE][BOARD_SIZE];

    public static boolean isAI = false;
    public static boolean isChess960 = false;
    public static boolean isamazon = false;
    public static boolean isrookswitch = false;
    private boolean whiteRookSwapped = false;
    private boolean blackRookSwapped = false;


    public GamePanel() {
        // Constructor should be empty, initialization will be done later
    }

    public void initialize(String player1, String player2, String player1Avatar, String player2Avatar, int gameTime) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Avatar = player1Avatar;
        this.player2Avatar = player2Avatar;
        this.gameTime = gameTime;

        player1TimeRemaining = gameTime * 60;
        player2TimeRemaining = gameTime * 60;
        whiteTurn = true;

        loadPieceImages();
        if (isChess960) {
            initBoardChess960();
        } else {
            initBoard();
        }
        gameState = new GameState(board);
        stockfish = new Stockfish();
        stockfish.startEngine("/Users/Yuvraj/Downloads/untitled2/stockfish/16.1/bin/stockfish");

        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (gameState == null) {
                    return;
                }
                drawChessBoard(g);
                drawPieces(g);

                if (dragging && selectedPieceType != null) {
                    drawPiece(g, selectedPieceType, mousePos.x - clickOffset.x, mousePos.y - clickOffset.y);
                }

                if (gameState.isInCheck(whiteTurn)) {
                    drawCheckIndicator(g);
                }
            }
        };
        boardPanel.setPreferredSize(new Dimension(BOARD_SIZE * TILE_SIZE + 2 * MARGIN, BOARD_SIZE * TILE_SIZE + 2 * MARGIN));
        add(boardPanel, BorderLayout.CENTER);

        evaluationBar = new EvaluationBar();
        evaluationBar.setVisible(false);

        rightBottomContainer = new JPanel(new BorderLayout());
        rightBottomContainer.setBackground(Color.BLACK);
        rightBottomContainer.setPreferredSize(new Dimension(244, 820));
        add(rightBottomContainer, BorderLayout.EAST);

        fxPanel = new JFXPanel();
        rightBottomContainer.add(fxPanel, BorderLayout.CENTER);
        initFX(fxPanel);

        if (!isAI) {
            startTimers();
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseReleased(e);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDragged(e);
            }
        });
    }

    private void initFX(JFXPanel fxPanel) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL fxmlUrl = getClass().getResource("/fxml/board.fxml");
            if (fxmlUrl == null) {
                throw new IOException("FXML file not found.");
            }
            loader.setLocation(fxmlUrl);
            Pane root = loader.load();

            player1NameLabel = (Label) loader.getNamespace().get("player1NameLabel");
            player2NameLabel = (Label) loader.getNamespace().get("player2NameLabel");
            player1Pic = (ImageView) loader.getNamespace().get("player1Pic");
            player2Pic = (ImageView) loader.getNamespace().get("player2Pic");
            player1Time = (Label) loader.getNamespace().get("player1Time");
            player2Time = (Label) loader.getNamespace().get("player2Time");

            themeComboBox = (ComboBox<String>) loader.getNamespace().get("themeComboBox");
            rotateButton = (javafx.scene.control.Button) loader.getNamespace().get("rotateButton");
            evalButton = (javafx.scene.control.Button) loader.getNamespace().get("evalButton");
            evaluationBarContainer = (StackPane) loader.getNamespace().get("evaluationBarContainer");

            ImageView rotateImageView = (ImageView) loader.getNamespace().get("rotateImageView");
            rotateImageView.setImage(new Image(getClass().getResourceAsStream("/images/rotate.png")));

            SwingNode swingNode = new SwingNode();
            Platform.runLater(() -> {
                swingNode.setContent(evaluationBar);
                evaluationBarContainer.getChildren().clear();  // Clear any previous content
                evaluationBarContainer.getChildren().add(swingNode);
                StackPane.setAlignment(swingNode, javafx.geometry.Pos.CENTER);  // Center the SwingNode within the StackPane
            });

            if (player1NameLabel != null) {
                player1NameLabel.setText(player1);
            }
            if (player2NameLabel != null) {
                player2NameLabel.setText(isAI ? "Stockfish" : player2);
            }

            if (player1Avatar != null && !player1Avatar.isEmpty()) {
                player1Pic.setImage(new Image(player1Avatar));
            }

            if (isAI) {
                player2Pic.setImage(new Image(getClass().getResource("/images/img.png").toExternalForm()));
            } else if (player2Avatar != null && !player2Avatar.isEmpty()) {
                player2Pic.setImage(new Image(player2Avatar));
            }

            themeComboBox.setOnAction(e -> changeTheme(themeComboBox.getValue()));
            rotateButton.setOnAction(e -> rotateBoard());
            evalButton.setOnAction(e -> toggleEvaluation());

            Scene scene = new Scene(root);
            URL cssUrl = getClass().getResource("/style.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("CSS file not found.");
            }
            fxPanel.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toggleEvaluation() {
        if (evaluationBar != null) {
            boolean currentlyVisible = evaluationBar.isVisible();
            evaluationBar.setVisible(!currentlyVisible);  // Toggle visibility
            evaluationBar.toggleEvalOnOff(!currentlyVisible);  // Update evalOn state in the evaluation bar
            evalButton.setText(currentlyVisible ? "Eval On" : "Eval Off");
            //System.out.println("Evaluation toggled: " + (!currentlyVisible ? "ON" : "OFF"));
            //System.out.println("Evaluation bar currently visible: " + evaluationBar.isVisible());
            //System.out.println("Eval Button text set to: " + evalButton.getText());
        }
    }




    private void makeAIMove() {
        String fen = getFenFromBoard();
        String bestMove = stockfish.getBestMove(fen, 1000);

        if (bestMove == null || bestMove.length() < 4) {
            return;
        }

        char startFile = bestMove.charAt(0);
        char startRank = bestMove.charAt(1);
        char endFile = bestMove.charAt(2);
        char endRank = bestMove.charAt(3);

        int startCol = startFile - 'a';
        int startRow = BOARD_SIZE - (startRank - '0');
        int endCol = endFile - 'a';
        int endRow = BOARD_SIZE - (endRank - '0');

        selectedPiece = new Point(startCol, startRow);
        selectedPieceType = board[startRow][startCol];
        board[startRow][startCol] = null;

        if (selectedPieceType != null && selectedPieceType instanceof King) {
            ((King) selectedPieceType).setMoved();
            if (selectedPieceType.isWhite()) {
                gameState.setWhiteKingPosition(new Point(endCol, endRow));
            } else {
                gameState.setBlackKingPosition(new Point(endCol, endRow));
            }
        }

        board[endRow][endCol] = selectedPieceType;
        selectedPiece = null;
        selectedPieceType = null;
        whiteTurn = !whiteTurn;
        repaint();
    }

    private void changeTheme(String theme) {
        switch (theme) {
            case "Blue":
                lightColor = Color.CYAN;
                darkColor = Color.BLUE.darker();
                break;
            case "Green":
                lightColor = Color.GREEN.brighter();
                darkColor = Color.GREEN.darker();
                break;
            default:
                lightColor = Color.WHITE;
                darkColor = Color.GRAY;
                break;
        }
        repaint();
    }

    private void rotateBoard() {
        rotate = !rotate;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawChessBoard(g);
        drawPieces(g);

        if (dragging && selectedPieceType != null) {
            drawPiece(g, selectedPieceType, mousePos.x - clickOffset.x, mousePos.y - clickOffset.y);
        }

        if (gameState.isInCheck(whiteTurn)) {
            drawCheckIndicator(g);
        }
    }

    private void drawChessBoard(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(MARGIN - 5, MARGIN - 5, TILE_SIZE * BOARD_SIZE + 10, TILE_SIZE * BOARD_SIZE + 10);

        boolean isWhite = true;
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                int drawRow = rotate ? BOARD_SIZE - 1 - row : row;
                int drawCol = rotate ? BOARD_SIZE - 1 - col : col;

                g.setColor(isWhite ? lightColor : darkColor);
                g.fillRect(MARGIN + drawCol * TILE_SIZE, MARGIN + drawRow * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                isWhite = !isWhite;
            }
            isWhite = !isWhite;
        }

        g.setColor(Color.BLACK);
        for (int i = 0; i < BOARD_SIZE; i++) {
            char colLabel = (char) ('A' + i);
            String rowLabel = String.valueOf(BOARD_SIZE - i);
            g.drawString(String.valueOf(colLabel), MARGIN + i * TILE_SIZE + TILE_SIZE / 2 - g.getFontMetrics().stringWidth(String.valueOf(colLabel)) / 2, MARGIN - 10);
            g.drawString(rowLabel, MARGIN - 20, MARGIN + i * TILE_SIZE + TILE_SIZE / 2 + g.getFontMetrics().getHeight() / 2);
        }
    }

    private void loadPieceImages() {
        pieceImages = new BufferedImage[2][6];
        String[] pieceNames = {"king", "queen", "rook", "bishop", "n", "pawn"};
        String[] colors = {"w", "b"};

        try {
            for (int color = 0; color < 2; color++) {
                for (int piece = 0; piece < 6; piece++) {
                    String path = "resources/pieces/" + colors[color] + pieceNames[piece] + ".png";
                    pieceImages[color][piece] = ImageIO.read(new File(path));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading images: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initBoard() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];


        if(isrookswitch){
            board[0][0] = new Drook(false);
        } else {
            board[0][0] = new Rook(false);
        }
        board[0][1] = new Knight(false);
        board[0][2] = new Bishop(false);
        if(isamazon){
            board[0][3] = new Amazon(false);
        } else {
            board[0][3] = new Queen(false);
        }
        board[0][4] = new King(false, new Point(4, 0));
        board[0][5] = new Bishop(false);
        board[0][6] = new Knight(false);
        if(isrookswitch){
            board[0][7] = new Drook(false);
        } else {
        board[0][7] = new Rook(false);
        }
        for (int col = 0; col < BOARD_SIZE; col++) {
            board[1][col] = new Pawn(false);
        }

        for (int col = 0; col < BOARD_SIZE; col++) {
            board[6][col] = new Pawn(true);
        }
        if(isrookswitch){
            board[7][0] = new Drook(true);
        } else {
            board[7][0] = new Rook(true);
        }
        board[7][1] = new Knight(true);
        board[7][2] = new Bishop(true);
        if(isamazon){
            board[7][3] = new Amazon(true);
        }else {
            board[7][3] = new Queen(true);
        }
        board[7][4] = new King(true, new Point(4, 7));
        board[7][5] = new Bishop(true);
        board[7][6] = new Knight(true);
        if(isrookswitch){
            board[7][7] = new Drook(true);
        } else {
            board[7][7] = new Rook(true);
        }
    }

    private void initBoardChess960() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];

        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            positions.add(i);
        }

        Collections.shuffle(positions);

        Piece[] whitePieces = new Piece[8];
        Piece[] blackPieces = new Piece[8];

        for (int i = 0; i < 8; i++) {
            if (positions.get(i) % 2 == 0) {
                whitePieces[positions.get(i)] = new Bishop(true);
                blackPieces[positions.get(i)] = new Bishop(false);
                break;
            }
        }
        for (int i = 0; i < 8; i++) {
            if (positions.get(i) % 2 == 1) {
                whitePieces[positions.get(i)] = new Bishop(true);
                blackPieces[positions.get(i)] = new Bishop(false);
                break;
            }
        }

        int rook1 = -1, rook2 = -1, king = -1;
        for (int i = 0; i < 8; i++) {
            if (whitePieces[positions.get(i)] == null) {
                if (king == -1) {
                    king = positions.get(i);
                    whitePieces[positions.get(i)] = new King(true, new Point(positions.get(i), 7));
                    blackPieces[positions.get(i)] = new King(false, new Point(positions.get(i), 0));
                } else if (rook1 == -1) {
                    rook1 = positions.get(i);
                    whitePieces[positions.get(i)] = new Rook(true);
                    blackPieces[positions.get(i)] = new Rook(false);
                } else if (rook2 == -1) {
                    rook2 = positions.get(i);
                    whitePieces[positions.get(i)] = new Rook(true);
                    blackPieces[positions.get(i)] = new Rook(false);
                    break;
                }
            }
        }

        for (int i = 0; i < 8; i++) {
            if (whitePieces[positions.get(i)] == null) {
                if (positions.get(i) < 3) {
                    whitePieces[positions.get(i)] = new Knight(true);
                    blackPieces[positions.get(i)] = new Knight(false);
                } else {
                    whitePieces[positions.get(i)] = new Queen(true);
                    blackPieces[positions.get(i)] = new Queen(false);
                }
            }
        }

        for (int i = 0; i < 8; i++) {
            board[0][i] = blackPieces[i];
            board[7][i] = whitePieces[i];
        }

        for (int col = 0; col < BOARD_SIZE; col++) {
            board[1][col] = new Pawn(false);
            board[6][col] = new Pawn(true);
        }
    }

    private void handleMousePressed(MouseEvent e) {
        int col = (e.getX() - MARGIN) / TILE_SIZE;
        int row = (e.getY() - MARGIN) / TILE_SIZE;
        if (rotate) {
            col = BOARD_SIZE - 1 - col;
            row = BOARD_SIZE - 1 - row;
        }
        if (col < BOARD_SIZE && row < BOARD_SIZE && col >= 0 && row >= 0 && board[row][col] != null) {
            if (board[row][col].isWhite() == whiteTurn) {
                selectedPiece = new Point(col, row);
                selectedPieceType = board[row][col];
                board[row][col] = null;
                dragging = true;
                clickOffset = new Point((e.getX() - MARGIN) % TILE_SIZE, (e.getY() - MARGIN) % TILE_SIZE);
                mousePos = e.getPoint();
                animateBox(row, col);
                repaint();
            }
        }
    }

    private void handleMouseReleased(MouseEvent e) {
        if (dragging) {
            int col = (e.getX() - MARGIN) / TILE_SIZE;
            int row = (e.getY() - MARGIN) / TILE_SIZE;
            if (rotate) {
                col = BOARD_SIZE - 1 - col;
                row = BOARD_SIZE - 1 - row;
            }

            if (col >= 0 && col < BOARD_SIZE && row >= 0 && row < BOARD_SIZE) {
                if (selectedPieceType != null && gameState.isValidMove(selectedPieceType, selectedPiece.y, selectedPiece.x, row, col)) {
                    //SoundPlayer.playSound("/sounds/capture.wav");
                    if (selectedPieceType instanceof Pawn) {
                        Pawn pawn = (Pawn) selectedPieceType;
                        resetTwoSteppedStatus();
                        if (Math.abs(selectedPiece.x - col) == 1 && board[row][col] == null) {
                            if (pawn.isWhite() && selectedPiece.y == 3) {
                                board[row + 1][col] = null;
                            } else if (!pawn.isWhite() && selectedPiece.y == 4) {
                                board[row - 1][col] = null;
                            }
                        }
                        if (Math.abs(selectedPiece.y - row) == 2) {
                            pawn.setTwoStepped(true);
                        }
                        pawn.setFirstMove(false);

                        if ((pawn.isWhite() && row == 0) || (!pawn.isWhite() && row == 7)) {
                            selectedPieceType = getPromotionChoice(pawn.isWhite());
                        }

                    }
                    board[row][col] = selectedPieceType;
                    board[selectedPiece.y][selectedPiece.x] = null;

                    if (selectedPieceType instanceof King) {
                        King king = (King) selectedPieceType;
                        king.setMoved();
                        if (Math.abs(col - selectedPiece.x) == 2) {
                            int rookCol = (col == 6) ? 7 : 0;
                            int newRookCol = (col == 6) ? 5 : 3;
                            Piece rook = board[row][rookCol];
                            board[row][rookCol] = null;
                            board[row][newRookCol] = rook;
                            ((Rook) rook).setMoved();
                        }

                        if (king.isWhite()) {
                            gameState.setWhiteKingPosition(new Point(col, row));
                        } else {
                            gameState.setBlackKingPosition(new Point(col, row));
                        }
                    }

                    if (selectedPieceType instanceof Rook) {
                        ((Rook) selectedPieceType).setMoved();
                    }

                    whiteTurn = !whiteTurn;
                    if (evaluationBar != null && evaluationBar.isVisible()) {
                        updateEvaluation();
                    }

                    if (isrookswitch||isamazon||isChess960){
                        rotateBoard();
                    }

                } else {
                    board[selectedPiece.y][selectedPiece.x] = selectedPieceType;
                }
            } else {
                board[selectedPiece.y][selectedPiece.x] = selectedPieceType;
            }

            dragging = false;
            selectedPiece = null;
            selectedPieceType = null;
            mousePos = null;
            if (isAI && !whiteTurn) {
                makeAIMove();
            }
            repaint();
            getResultMessage();
        }
    }

    private void animateBox(int row, int col) {
        Rectangle rect = rectangles[row][col];
        FillTransition fillTransition = new FillTransition(Duration.millis(500), rect);
        fillTransition.setFromValue(javafx.scene.paint.Color.YELLOW);
        fillTransition.setToValue((row + col) % 2 == 0 ? javafx.scene.paint.Color.BEIGE : javafx.scene.paint.Color.BROWN);
        fillTransition.setAutoReverse(true);
        fillTransition.setCycleCount(2);
        fillTransition.play();
    }

    private Piece getPromotionChoice(boolean isWhite) {
        String[] options = {"Queen", "Rook", "Bishop", "Knight"};
        int choice = JOptionPane.showOptionDialog(this, "Choose a piece for promotion:",
                "Pawn Promotion",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        switch (choice) {
            case 0:
                return new Queen(isWhite);
            case 1:
                return new Rook(isWhite);
            case 2:
                return new Bishop(isWhite);
            case 3:
                return new Knight(isWhite);
            default:
                return new Queen(isWhite);
        }
    }

    private void showEndPage(String result) {
        //System.out.println("Game over: " + result);
        rightBottomContainer.removeAll();

        JPanel endPanel = new JPanel(new BorderLayout());
        endPanel.setBackground(Color.BLACK);

        JLabel resultLabel = new JLabel(result, SwingConstants.CENTER);
        resultLabel.setFont(new Font("Founders Grotesk", Font.PLAIN, 30)); // Regular font, not bold
        resultLabel.setForeground(Color.WHITE);
        endPanel.add(resultLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.BLACK);
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 20)); // Arrange buttons vertically with spacing

        JButton rematchButton = new JButton("Rematch");
        rematchButton.setFont(new Font("Founders Grotesk", Font.PLAIN, 20)); // Regular font, not bold
        rematchButton.setForeground(Color.BLACK);
        rematchButton.setBackground(Color.WHITE);
        rematchButton.setPreferredSize(new Dimension(200, 60)); // Increase size of the button
        rematchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rightBottomContainer.add(fxPanel, BorderLayout.CENTER);
                initialize(player1, player2, player1Avatar, player2Avatar, gameTime); // Start a new game
                endPanel.setVisible(false);
            }
        });

        JButton multiplayerButton = new JButton("Multiplayer Menu");
        multiplayerButton.setFont(new Font("Founders Grotesk", Font.PLAIN, 20)); // Regular font, not bold
        multiplayerButton.setForeground(Color.BLACK);
        multiplayerButton.setBackground(Color.WHITE);
        multiplayerButton.setPreferredSize(new Dimension(200, 60)); // Increase size of the button
        multiplayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        buttonPanel.add(rematchButton);
        buttonPanel.add(multiplayerButton);

        endPanel.add(buttonPanel, BorderLayout.SOUTH);

        rightBottomContainer.add(endPanel, BorderLayout.CENTER);
        rightBottomContainer.revalidate();
        rightBottomContainer.repaint();
    }


    private void resetGameState() {
        //System.out.println("Resetting game state...");

        // Reset board and piece variables
        board = null;
        pieceImages = null;
        selectedPiece = null;
        selectedPieceType = null;
        dragging = false;
        mousePos = null;
        clickOffset = null;

        // Reset turn and game-related variables
        whiteTurn = true;
        gameState = null;

        // Reset AI and custom game modes
        isAI = false;
        isChess960 = false;
        isamazon = false;
        isrookswitch = false;


        // Reset timers
        if (player1Timer != null) {
            player1Timer.cancel();
            player1Timer = null;
        }
        if (player2Timer != null) {
            player2Timer.cancel();
            player2Timer = null;
        }
        player1TimeRemaining = gameTime * 60;
        player2TimeRemaining = gameTime * 60;

        // Reset UI elements if necessary
        if (evaluationBar != null) {
            evaluationBar.setEvaluation(0.0);
            evaluationBar.setVisible(false);
        }
        rotate = false;
    }


    private void handleMouseDragged(MouseEvent e) {
        if (dragging) {
            mousePos = e.getPoint();
            repaint();
        }
    }

    private void drawPieces(Graphics g) {
        if (board == null) {
            return;
        }
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                int drawRow = rotate ? BOARD_SIZE - 1 - row : row;
                int drawCol = rotate ? BOARD_SIZE - 1 - col : col;
                Piece piece = board[row][col];
                if (piece != null) {
                    int x = MARGIN + drawCol * TILE_SIZE + (TILE_SIZE - PIECE_SIZE) / 2;
                    int y = MARGIN + drawRow * TILE_SIZE + (TILE_SIZE - PIECE_SIZE) / 2;
                    drawPiece(g, piece, x, y);
                }
            }
        }
    }

    private void drawPiece(Graphics g, Piece piece, int x, int y) {
        int pieceType = piece.getImageIndex();
        int color = piece.isWhite() ? 0 : 1;
        g.drawImage(pieceImages[color][pieceType], x, y, PIECE_SIZE, PIECE_SIZE, null);
    }

    private void drawCheckIndicator(Graphics g) {
        g.setColor(new Color(255, 0, 0, 128)); // Light red color with some transparency
        SoundPlayer.playSound("/sounds/move-self.wav");

        King king = null;

        if (whiteTurn) {
            if (gameState.getWhiteKingPosition() != null) {
                king = (King) board[gameState.getWhiteKingPosition().y][gameState.getWhiteKingPosition().x];
            }
        } else {
            if (gameState.getBlackKingPosition() != null) {
                king = (King) board[gameState.getBlackKingPosition().y][gameState.getBlackKingPosition().x];
            }
        }

        // If king is still null, exit the method
        if (king == null) {
            return;
        }

        Point kingPosition = king.getPosition();

        // Highlight the king's current position
        int kingX = MARGIN + (rotate ? BOARD_SIZE - 1 - kingPosition.x : kingPosition.x) * TILE_SIZE;
        int kingY = MARGIN + (rotate ? BOARD_SIZE - 1 - kingPosition.y : kingPosition.y) * TILE_SIZE;
        g.fillRect(kingX, kingY, TILE_SIZE, TILE_SIZE);

        // Possible king moves: one square in any direction
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < dx.length; i++) {
            int newRow = kingPosition.y + dy[i];
            int newCol = kingPosition.x + dx[i];

            if (newRow >= 0 && newRow < BOARD_SIZE && newCol >= 0 && newCol < BOARD_SIZE) {
                if (king.canMove(kingPosition.y, kingPosition.x, newRow, newCol, board)) {
                    int x = MARGIN + (rotate ? BOARD_SIZE - 1 - newCol : newCol) * TILE_SIZE;
                    int y = MARGIN + (rotate ? BOARD_SIZE - 1 - newRow : newRow) * TILE_SIZE;
                    g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }


    private void resetTwoSteppedStatus() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = board[row][col];
                if (piece instanceof Pawn) {
                    ((Pawn) piece).setTwoStepped(false);
                }
            }
        }
    }

    private void updateEvaluation() {
        String fen = getFenFromBoard();
        Double evaluation = stockfish.getEvaluation(fen, 10);
        if (evaluation != null) {
            evaluationBar.setEvaluation(evaluation);
        }
    }

    private String getFenFromBoard() {
        StringBuilder fen = new StringBuilder();
        for (int row = 0; row < BOARD_SIZE; row++) {
            int emptyCount = 0;
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = board[row][col];
                if (piece == null) {
                    emptyCount++;
                } else {
                    if (emptyCount > 0) {
                        fen.append(emptyCount);
                        emptyCount = 0;
                    }
                    char pieceChar = getPieceChar(piece);
                    fen.append(pieceChar);
                }
            }
            if (emptyCount > 0) {
                fen.append(emptyCount);
            }
            if (row < BOARD_SIZE - 1) {
                fen.append('/');
            }
        }
        fen.append(' ').append(whiteTurn ? 'w' : 'b').append(" - - 0 1");
        return fen.toString();
    }

    private char getPieceChar(Piece piece) {
        char pieceChar;
        switch (piece.getImageIndex()) {
            case 0: // King
                pieceChar = 'k';
                break;
            case 1: // Queen
                pieceChar = 'q';
                break;
            case 2: // Rook
                pieceChar = 'r';
                break;
            case 3: // Bishop
                pieceChar = 'b';
                break;
            case 4: // Knight
                pieceChar = 'n';
                break;
            case 5: // Pawn
            default:
                pieceChar = 'p';
                break;
        }
        return piece.isWhite() ? Character.toUpperCase(pieceChar) : pieceChar;
    }

    private void startTimers() {
        player1Timer = new Timer();
        player2Timer = new Timer();

        player1Timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (whiteTurn) {
                    Platform.runLater(() -> updatePlayer1Time());
                }
            }
        }, 0, 1000);

        player2Timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!whiteTurn) {
                    Platform.runLater(() -> updatePlayer2Time());
                }
            }
        }, 0, 1000);
    }

    private void updatePlayer1Time() {
        player1TimeRemaining--;
        if (player1TimeRemaining <= 0) {
            player1Time.setText("00:00");
            player1Timer.cancel();
        } else {
            player1Time.setText(formatTime(player1TimeRemaining));
        }
    }

    private void updatePlayer2Time() {
        player2TimeRemaining--;
        if (player2TimeRemaining <= 0) {
            player2Time.setText("00:00");
            player2Timer.cancel();
        } else {
            player2Time.setText(formatTime(player2TimeRemaining));
        }
    }

    private String formatTime(int timeInSeconds) {
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void getResultMessage() {
        if (player1TimeRemaining <= 0) {
            showEndPage("Black wins by timeout!");
        } else if (player2TimeRemaining <= 0) {
            showEndPage("White wins by timeout!");
        } else if (gameState.isCheckmate(whiteTurn)) {
            showEndPage((whiteTurn ? "Black" : "White") + " wins by checkmate!");
        }else if (gameState.isCheckmate(!whiteTurn)) {
            showEndPage((whiteTurn ? "Black" : "White") + " wins by checkmate!");
        }else if (gameState.isStalemate(!whiteTurn)) {
            showEndPage("Stalemate!");
        }else if (gameState.isStalemate(whiteTurn)) {
            showEndPage("Stalemate!");
        } else if (gameState.isInsufficientMaterial()) {
            showEndPage("Draw by insufficient material!");
        }
    }
}
