package MyChessGame.Pieces;
public class Knight extends Piece {

    public Knight(boolean isWhite) {
        super(PieceType.KNIGHT, isWhite);
    }

    @Override
    public int getImageIndex() {
        return 4; // The index for the knight's image in the pieceImages array
    }

    @Override
    public boolean canMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {
        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);


        // Knight can move in an L-shape: two squares in one direction and one square perpendicular
        if ((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)) {
            Piece targetPiece = board[endRow][endCol];
            // Knight cannot move to a square occupied by a piece of the same color
            if (targetPiece == null || targetPiece.isWhite() != this.isWhite()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Knight clone() {
        return new Knight(this.isWhite());
    }
}
