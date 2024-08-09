package MyChessGame.gamemode;

import MyChessGame.Pieces.King;
import MyChessGame.Pieces.Piece;

import java.awt.*;

public class GameState {
    private Piece[][] board;
    private Point whiteKingPosition;
    private Point blackKingPosition;

    public GameState(Piece[][] board) {
        this.board = board;
        whiteKingPosition = findKing(true);
        blackKingPosition = findKing(false);
    }

    public boolean isInCheck(boolean isWhite) {
        Point kingPosition = isWhite ? whiteKingPosition : blackKingPosition;
        return isSquareUnderAttack(kingPosition.x, kingPosition.y, !isWhite);
    }

    public boolean isCheckmate(boolean isWhite) {
        Point kingPosition = isWhite ? whiteKingPosition : blackKingPosition;
        if (!isInCheck(isWhite)) {
            return false;
        }

        if (canKingMove(kingPosition, isWhite)) {
            return false;
        }

        // Additional logic to check if any move can block the check
        // Find the checking piece
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.isWhite() != isWhite && piece.canMove(row, col, kingPosition.y, kingPosition.x, board)) {
                    // Check if we can block the check
                    if (canBlockCheck(piece, new Point(col, row), kingPosition, isWhite)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean canKingMove(Point kingPosition, boolean isWhite) {
        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},          {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };

        for (int[] dir : directions) {
            int newRow = kingPosition.y + dir[0];
            int newCol = kingPosition.x + dir[1];
            if (isValidPosition(newRow, newCol) && (board[newRow][newCol] == null || board[newRow][newCol].isWhite() != isWhite)) {
                Piece[][] tempBoard = copyBoard(board);
                tempBoard[newRow][newCol] = tempBoard[kingPosition.y][kingPosition.x];
                tempBoard[kingPosition.y][kingPosition.x] = null;
                GameState tempState = new GameState(tempBoard);
                if (!tempState.isInCheck(isWhite)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canBlockCheck(Piece checkingPiece, Point checkingPos, Point kingPos, boolean isWhite) {
        int colDiff = Math.abs(checkingPos.x - kingPos.x);
        int rowDiff = Math.abs(checkingPos.y - kingPos.y);

        if (colDiff == 0 || rowDiff == 0 || colDiff == rowDiff) {
            int colStep = (checkingPos.x == kingPos.x) ? 0 : (checkingPos.x > kingPos.x ? -1 : 1);
            int rowStep = (checkingPos.y == kingPos.y) ? 0 : (checkingPos.y > kingPos.y ? -1 : 1);

            int col = checkingPos.x + colStep;
            int row = checkingPos.y + rowStep;
            while (col != kingPos.x || row != kingPos.y) {
                for (int r = 0; r < board.length; r++) {
                    for (int c = 0; c < board[r].length; c++) {
                        Piece piece = board[r][c];
                        if (piece != null && piece.isWhite() == isWhite) {
                            if (piece.canMove(r, c, row, col, board)) {
                                return true;
                            }
                        }
                    }
                }
                col += colStep;
                row += rowStep;
            }
        }
        return false;
    }

    public boolean isStalemate(boolean isWhite) {
        if (isInCheck(isWhite)) {
            return false;
        }
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.isWhite() == isWhite) {
                    if (canMovePiece(piece, row, col)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isInsufficientMaterial() {
        int whitePieces = 0;
        int blackPieces = 0;
        for (Piece[] row : board) {
            for (Piece piece : row) {
                if (piece != null) {
                    if (piece.isWhite()) {
                        whitePieces++;
                    } else {
                        blackPieces++;
                    }
                }
            }
        }
        return (whitePieces <= 2 && blackPieces <= 2);
    }

    public Point getWhiteKingPosition() {
        return whiteKingPosition;
    }

    public void setWhiteKingPosition(Point whiteKingPosition) {
        this.whiteKingPosition = whiteKingPosition;
    }

    public Point getBlackKingPosition() {
        return blackKingPosition;
    }

    public void setBlackKingPosition(Point blackKingPosition) {
        this.blackKingPosition = blackKingPosition;
    }

    private Point findKing(boolean isWhite) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Piece piece = board[row][col];
                if (piece instanceof King && piece.isWhite() == isWhite) {
                    King king = (King) piece;
                    return new Point(col, row);
                }
            }
        }
        throw new IllegalStateException("King not found on the board.");
    }

    private boolean isSquareUnderAttack(int col, int row, boolean byWhite) {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                Piece piece = board[r][c];
                if (piece != null && piece.isWhite() == byWhite && piece.canMove(r, c, row, col, board)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canMovePiece(Piece piece, int startRow, int startCol) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (piece.canMove(startRow, startCol, row, col, board)) {
                    if (isValidPosition(row, col)) {
                        Piece original = board[row][col];
                        board[row][col] = piece;
                        board[startRow][startCol] = null;
                        boolean isKingSafe = !isInCheck(piece.isWhite());
                        board[startRow][startCol] = piece;
                        board[row][col] = original;
                        if (isKingSafe) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < board.length && col >= 0 && col < board[0].length;
    }

    public boolean isValidMove(Piece piece, int startRow, int startCol, int endRow, int endCol) {
        if (!piece.canMove(startRow, startCol, endRow, endCol, board)) {
            return false;
        }
        Piece[][] tempBoard = copyBoard(board);
        tempBoard[endRow][endCol] = piece.clone();
        tempBoard[startRow][startCol] = null;
        GameState tempState = new GameState(tempBoard);
        return !tempState.isInCheck(piece.isWhite());
    }

    private Piece[][] copyBoard(Piece[][] original) {
        Piece[][] copy = new Piece[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = new Piece[original[i].length];
            for (int j = 0; j < original[i].length; j++) {
                if (original[i][j] != null) {
                    copy[i][j] = original[i][j].clone();
                }
            }
        }
        return copy;
    }
}
