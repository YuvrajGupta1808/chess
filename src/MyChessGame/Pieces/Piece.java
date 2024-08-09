package MyChessGame.Pieces;


public abstract class Piece {
    private PieceType type;
    private boolean isWhite;

    public Piece(PieceType type, boolean isWhite) {
        this.type = type;
        this.isWhite = isWhite;
    }

    public PieceType getType() {
        return type;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public int getImageIndex() {
        switch (type) {
            case KING:
                return 0;
            case QUEEN:
                return 1;
            case ROOK:
                return 2;
            case BISHOP:
                return 3;
            case KNIGHT:
                return 4;
            case PAWN:
                return 5;
            default:
                throw new IllegalArgumentException("Unknown piece type");
        }
    }

    public abstract boolean canMove(int startRow, int startCol, int endRow, int endCol, Piece[][] board);

    public void move(int startRow, int startCol, int endRow, int endCol, Piece[][] board) {
    }

    public abstract Piece clone();

}
