package MyChessGame.Pieces;

import java.awt.*;

public class King extends Piece {
    private boolean hasMoved = false;
    private Point position; // Adding position to the King class


    public King(boolean isWhite, Point initialPosition) {
        super(PieceType.KING, isWhite);
        this.position = initialPosition;
    }

    @Override
    public int getImageIndex() {
        return 0; // The index for the king's image in the pieceImages array
    }

    @Override
    public boolean canMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {
        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);

        // Check if move is to the same position
        if (rowDiff == 0 && colDiff == 0) {
            return false;
        }

        // King can move one square in any direction
        if (rowDiff <= 1 && colDiff <= 1) {
            Piece targetPiece = board[endRow][endCol];
            if (targetPiece == null || targetPiece.isWhite() != this.isWhite()) {
                // Ensure the king does not move into check
                if (!isSquareUnderAttack(endRow, endCol, !this.isWhite(), board)) {
                    return true;
                }
            }
        }

        // Castling
        if (!hasMoved && rowDiff == 0 && colDiff == 2) {
            if (endCol == 2) {
                // Queen-side castling
                return canCastle(startRow, 0, board);
            } else if (endCol == 6) {
                // King-side castling
                return canCastle(startRow, 7, board);
            }
        }

        return false;
    }

    private boolean canCastle(int row, int rookCol, Piece[][] board) {
        if (board[row][rookCol] instanceof Rook) {
            Rook rook = (Rook) board[row][rookCol];
            if (!rook.hasMoved()) {
                // Check if path is clear
                int colStep = (rookCol == 0) ? -1 : 1;
                for (int col = 4 + colStep; col != rookCol; col += colStep) {
                    if (board[row][col] != null || isSquareUnderAttack(row, col, !this.isWhite(), board)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean isSquareUnderAttack(int row, int col, boolean byWhite, Piece[][] board) {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                Piece piece = board[r][c];
                if (piece != null && piece.isWhite() == byWhite) {
                    boolean canMove = piece.canMove(r, c, row, col, board);
                    if (canMove) {
                        //System.out.println(piece.getClass().getSimpleName() + " at (" + r + ", " + c + ") can attack square (" + row + ", " + col + ")");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void setMoved() {
        this.hasMoved = true;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }

    @Override
    public King clone() {
        King cloned = new King(this.isWhite(), this.position);
        cloned.hasMoved = this.hasMoved;
        return cloned;
    }
}
