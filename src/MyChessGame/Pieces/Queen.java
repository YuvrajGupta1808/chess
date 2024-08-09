package MyChessGame.Pieces;

public class Queen extends Piece {

    public Queen(boolean isWhite) {
        super(PieceType.QUEEN, isWhite);
    }

    @Override
    public int getImageIndex() {
        return 1; // The index for the queen's image in the pieceImages array
    }

    @Override
    public boolean canMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {
        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);

        // Check if move is to the same position
        if (rowDiff == 0 && colDiff == 0) {
            return false;
        }

        if (rowDiff == colDiff || startRow == endRow || startCol == endCol) {
            int rowStep = Integer.signum(endRow - startRow);
            int colStep = Integer.signum(endCol - startCol);

            int currentRow = startRow + rowStep;
            int currentCol = startCol + colStep;

            while (currentRow != endRow || currentCol != endCol) {
                if (board[currentRow][currentCol] != null) {
                    return false;
                }
                currentRow += rowStep;
                currentCol += colStep;
            }

            Piece targetPiece = board[endRow][endCol];
            if (targetPiece == null || targetPiece.isWhite() != this.isWhite()) {
                return true;
            } else {
            }
        }
        return false;
    }

    @Override
    public Queen clone() {
        return new Queen(this.isWhite());
    }
}
