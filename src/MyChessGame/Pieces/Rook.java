package MyChessGame.Pieces;
public class Rook extends Piece {
    private boolean hasMoved = false;

    public Rook(boolean isWhite) {
        super(PieceType.ROOK, isWhite);
    }

    @Override
    public int getImageIndex() {
        return 2; // The index for the rook's image in the pieceImages array
    }

    @Override
    public boolean canMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {

        if (startRow != endRow && startCol != endCol) {
            return false; // Rooks move in straight lines only
        }

        if (startRow == endRow) { // Moving horizontally
            int colIncrement = (endCol - startCol) > 0 ? 1 : -1;
            for (int col = startCol + colIncrement; col != endCol; col += colIncrement) {
                if (!isValidPosition(startRow, col) || board[startRow][col] != null) {
                    return false;
                }
            }
        } else { // Moving vertically
            int rowIncrement = (endRow - startRow) > 0 ? 1 : -1;
            for (int row = startRow + rowIncrement; row != endRow; row += rowIncrement) {
                if (!isValidPosition(row, startCol) || board[row][startCol] != null) {
                    return false;
                }
            }
        }

        return board[endRow][endCol] == null || board[endRow][endCol].isWhite() != isWhite();
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setMoved() {
        this.hasMoved = true;
    }

    @Override
    public Queen clone() {
        return new Queen(this.isWhite());
    }
}
