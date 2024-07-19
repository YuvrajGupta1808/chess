package MyChessGame.piece;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight(int color, int row, int col, String imagePath) {
        super(color, row, col, imagePath);
    }

    @Override
    public boolean isValidMove(Piece[][] board, int newRow, int newCol) {
        int rowDiff = Math.abs(newRow - row);
        int colDiff = Math.abs(newCol - col);
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)
                && (board[newRow][newCol] == null || board[newRow][newCol].getColor() != color);
    }

    @Override
    public List<int[]> getPossibleMoves() {
        List<int[]> moves = new ArrayList<>();
        int[][] knightMoves = {
                {row + 2, col + 1}, {row + 2, col - 1}, {row - 2, col + 1}, {row - 2, col - 1},
                {row + 1, col + 2}, {row + 1, col - 2}, {row - 1, col + 2}, {row - 1, col - 2}
        };
        for (int[] move : knightMoves) {
            moves.add(move);
        }
        return moves;
    }
}
