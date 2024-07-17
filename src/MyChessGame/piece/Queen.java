package MyChessGame.piece;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

    public Queen(int color, int row, int col, String imagePath) {
        super(color, row, col, imagePath);
    }

    @Override
    public boolean isValidMove(Piece[][] board, int newRow, int newCol) {
        return new Rook(color, row, col, imagePath).isValidMove(board, newRow, newCol) ||
                new Bishop(color, row, col, imagePath).isValidMove(board, newRow, newCol);
    }

    @Override
    public List<int[]> getPossibleMoves() {
        List<int[]> moves = new ArrayList<>();
        moves.addAll(new Rook(color, row, col, imagePath).getPossibleMoves());
        moves.addAll(new Bishop(color, row, col, imagePath).getPossibleMoves());
        return moves;
    }
}
