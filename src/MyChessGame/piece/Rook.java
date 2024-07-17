package MyChessGame.piece;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    public Rook(int color, int row, int col, String imagePath) {
        super(color, row, col, imagePath);
    }

    @Override
    public boolean isValidMove(Piece[][] board, int newRow, int newCol) {
        if (newRow != row && newCol != col) {
            return false; // Rook moves in straight lines
        }
        int rowDirection = Integer.compare(newRow, row);
        int colDirection = Integer.compare(newCol, col);

        int r = row + rowDirection;
        int c = col + colDirection;
        while (r != newRow || c != newCol) {
            if (board[r][c] != null) {
                return false; // Path is blocked
            }
            r += rowDirection;
            c += colDirection;
        }
        return board[newRow][newCol] == null || board[newRow][newCol].getColor() != color;
    }

    @Override
    public List<int[]> getPossibleMoves() {
        List<int[]> moves = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            moves.add(new int[]{row + i, col});
            moves.add(new int[]{row - i, col});
            moves.add(new int[]{row, col + i});
            moves.add(new int[]{row, col - i});
        }
        return moves;
    }
}
