package MyChessGame.Pieces;

public class Amazon extends Piece {

    public Amazon(boolean isWhite) {
        super(PieceType.QUEEN, isWhite);
    }

    @Override
    public int getImageIndex() {
        return 1; // Use the same image index as the Queen for now
    }

    @Override
    public boolean canMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {
        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);

        // Check Knight-like moves
        if (rowDiff == 2 && colDiff == 1 || rowDiff == 1 && colDiff == 2) {
            return board[endRow][endCol] == null || board[endRow][endCol].isWhite() != this.isWhite();
        }

        // Check Queen-like moves
        if (startRow == endRow || startCol == endCol || rowDiff == colDiff) {
            int rowStep = Integer.compare(endRow, startRow);
            int colStep = Integer.compare(endCol, startCol);
            int currentRow = startRow + rowStep;
            int currentCol = startCol + colStep;
            while (currentRow != endRow || currentCol != endCol) {
                if (board[currentRow][currentCol] != null) {
                    return false;
                }
                currentRow += rowStep;
                currentCol += colStep;
            }
            return board[endRow][endCol] == null || board[endRow][endCol].isWhite() != this.isWhite();
        }

        return false;
    }

    @Override
    public Piece clone() {
        return null;
    }
}
