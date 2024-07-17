package MyChessGame.piece;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(int color, int row, int col, String imagePath) {
        super(color, row, col, imagePath);
    }

    @Override
    public boolean isValidMove(Piece[][] board, int newRow, int newCol) {
        int rowDiff = Math.abs(newRow - row);
        int colDiff = Math.abs(newCol - col);
        return rowDiff <= 1 && colDiff <= 1 && (board[newRow][newCol] == null || board[newRow][newCol].getColor() != color);
    }

    @Override
    public List<int[]> getPossibleMoves() {
        List<int[]> moves = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 || j != 0) {
                    moves.add(new int[]{row + i, col + j});
                }
            }
        }
        return moves;
    }
}
