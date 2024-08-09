package MyChessGame.Pieces;

public class Bishop extends Piece {

    public Bishop(boolean isWhite) {
        super(PieceType.BISHOP, isWhite);
    }

    @Override
    public int getImageIndex() {
        return 3; // The index for the bishop's image in the pieceImages array
    }

    @Override
    public boolean canMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {

        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);

        // Check if move is to the same position
        if (rowDiff == 0 && colDiff == 0) {
            return false;
        }

        // Bishops move diagonally, so the absolute row and column differences must be equal
        if (rowDiff == colDiff) {
            int rowStep = Integer.signum(endRow - startRow);
            int colStep = Integer.signum(endCol - startCol);



            int currentRow = startRow + rowStep;
            int currentCol = startCol + colStep;

            while (currentRow != endRow || currentCol != endCol) {
                // Debug the current position in the path

                if (board[currentRow][currentCol] != null) {
                    return false;
                }
                currentRow += rowStep;
                currentCol += colStep;
            }

            Piece targetPiece = board[endRow][endCol];
            if (targetPiece == null) {
                return true;
            } else if (targetPiece.isWhite() != this.isWhite()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Bishop clone() {
        return new Bishop(this.isWhite());
    }
}
