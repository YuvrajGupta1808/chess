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
            System.out.println("No piece at starting position (" + startRow + ", " + startCol + ")");
            return false;
        }
        System.out.println("Move from (" + startRow + ", " + startCol + ") to (" + endRow + ", " + endCol + ")");
        return piece.isValidMove(board, endRow, endCol);
    }

    public void makeMove(int startRow, int startCol, int endRow, int endCol) {
        if (isValidMove(startRow, startCol, endRow, endCol)){
            Piece piece = board[startRow][startCol];
            board[startRow][startCol] = null;
            board[endRow][endCol] = piece;
            piece.setPosition(endRow, endCol);
            System.out.println("Moved piece to (" + endRow + ", " + endCol + ")");
        } else {
            System.out.println("Move failed from (" + startRow + ", " + startCol + ") to (" + endRow + ", " + endCol + ")");
        }
    }
}
