package MyChessGame.board;

import MyChessGame.piece.Piece;

public class Move {
    private Piece[][] board;

    public Move(Piece[][] board) {
        this.board = board;
    }

    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol) {
        Piece piece = board[startRow][startCol];
        if (piece == null) {
            return false;
        }
        return piece.isValidMove(board, endRow, endCol);
    }

    public void makeMove(int startRow, int startCol, int endRow, int endCol) {
        Piece piece = board[startRow][startCol];
        board[startRow][startCol] = null;
        board[endRow][endCol] = piece;
        piece.setPosition(endRow, endCol);
    }
}
