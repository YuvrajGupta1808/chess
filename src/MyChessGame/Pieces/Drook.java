package MyChessGame.Pieces;

public class Drook extends Piece {
    public Drook(boolean isWhite) {
        super(PieceType.ROOK, isWhite);
    }

    @Override
    public int getImageIndex() {
        return 2; // Assuming it uses the rook's image
    }

    @Override
    public boolean canMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {
        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);

        // Drook moves like a rook but with a maximum of 2 steps
        if ((rowDiff == 0 || colDiff == 0) && (rowDiff <= 2 && colDiff <= 2)) {
            // Check if path is clear
            int rowStep = Integer.signum(endRow - startRow);
            int colStep = Integer.signum(endCol - startCol);

            for (int i = 1; i <= Math.max(rowDiff, colDiff); i++) {
                int newRow = startRow + i * rowStep;
                int newCol = startCol + i * colStep;

                if (board[newRow][newCol] != null) {
                    if (i == Math.max(rowDiff, colDiff) && board[newRow][newCol].isWhite() != this.isWhite()) {
                        // Allow capturing an opponent's piece
                        return true;
                    }
                    return false; // Path is blocked
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public Drook clone() {
        return new Drook(this.isWhite());
    }
}
