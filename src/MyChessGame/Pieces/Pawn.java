package MyChessGame.Pieces;

public class Pawn extends Piece {
    private boolean firstMove = true;
    private boolean twoStepped = false;

    public Pawn(boolean isWhite) {
        super(PieceType.PAWN, isWhite);
    }

    @Override
    public int getImageIndex() {
        return 5; // The index for the pawn's image in the pieceImages array
    }

    @Override
    public boolean canMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {
        int rowDiff = endRow - startRow;
        int colDiff = Math.abs(endCol - startCol);

        if (isWhite()) {
            if (startRow == 6 && endRow == 4 && startCol == endCol && board[5][startCol] == null && board[4][startCol] == null) {
                return true; // Double move on first move
            }
            if (rowDiff == -1 && colDiff == 0 && board[endRow][endCol] == null) {
                return true; // Single move
            }
            if (rowDiff == -1 && colDiff == 1 && board[endRow][endCol] != null && !board[endRow][endCol].isWhite()) {
                return true; // Capture move
            }
            // En passant
            if (startRow == 3 && rowDiff == -1 && colDiff == 1 && board[startRow][endCol] instanceof Pawn) {
                Pawn adjacentPawn = (Pawn) board[startRow][endCol];
                if (!adjacentPawn.isWhite() && adjacentPawn.isTwoStepped()) {
                    return true;
                }
            }
        } else {
            if (startRow == 1 && endRow == 3 && startCol == endCol && board[2][startCol] == null && board[3][startCol] == null) {
                return true; // Double move on first move
            }
            if (rowDiff == 1 && colDiff == 0 && board[endRow][endCol] == null) {
                return true; // Single move
            }
            if (rowDiff == 1 && colDiff == 1 && board[endRow][endCol] != null && board[endRow][endCol].isWhite()) {
                return true; // Capture move
            }
            // En passant
            if (startRow == 4 && rowDiff == 1 && colDiff == 1 && board[startRow][endCol] instanceof Pawn) {
                Pawn adjacentPawn = (Pawn) board[startRow][endCol];
                if (adjacentPawn.isWhite() && adjacentPawn.isTwoStepped()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isFirstMove() {
        return firstMove;
    }

    public void setFirstMove(boolean firstMove) {
        this.firstMove = firstMove;
    }

    public boolean isTwoStepped() {
        return twoStepped;
    }

    public void setTwoStepped(boolean twoStepped) {
        this.twoStepped = twoStepped;
    }

    @Override
    public Pawn clone() {
        Pawn cloned = new Pawn(this.isWhite());
        cloned.firstMove = this.firstMove;
        cloned.twoStepped = this.twoStepped;
        return cloned;
    }
}



