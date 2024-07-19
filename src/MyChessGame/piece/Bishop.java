package MyChessGame.piece;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {

    public Bishop(int color, int row, int col, String imagePath) {
        super(color, row, col, imagePath);
    }

    @Override
    public boolean isValidMove(Piece[][] board, int newRow, int newCol) {
        if (Math.abs(newRow - row) == Math.abs(newCol - col)) {
            int rowDirection = Integer.compare(newRow, row);
            int colDirection = Integer.compare(newCol, col);
            for (int i = 1; i < Math.abs(newRow - row); i++) {
                if (board[row + i * rowDirection][col + i * colDirection] != null) {
                    return false;
                }
            }
            return board[newRow][newCol] == null || board[newRow][newCol].getColor() != color;
        }
        return false;
    }

    @Override
    public List<int[]> getPossibleMoves() {
        List<int[]> moves = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            moves.add(new int[]{row + i, col + i});
            moves.add(new int[]{row + i, col - i});
            moves.add(new int[]{row - i, col + i});
            moves.add(new int[]{row - i, col - i});
        }
        return moves;
    }
}
