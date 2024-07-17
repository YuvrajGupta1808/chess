package MyChessGame.piece;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(int color, int row, int col, String imagePath) {
        super(color, row, col, imagePath);
    }

    @Override
    public boolean isValidMove(Piece[][] board, int newRow, int newCol) {
        int direction = color == 1 ? -1 : 1;  // White moves up (-1), black moves down (+1)
        if (newRow == row + direction && newCol == col && board[newRow][newCol] == null) {
            return true; // Move forward
        } else if (newRow == row + direction && Math.abs(newCol - col) == 1 && board[newRow][newCol] != null && board[newRow][newCol].getColor() != color) {
            return true; // Capture diagonally
        }
        return false;
    }

    @Override
    public List<int[]> getPossibleMoves() {
        List<int[]> moves = new ArrayList<>();
        moves.add(new int[]{row - 1, col});
        moves.add(new int[]{row - 1, col - 1});
        moves.add(new int[]{row - 1, col + 1});
        return moves;
    }
}
