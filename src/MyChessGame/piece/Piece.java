package MyChessGame.piece;

import java.util.List;

public abstract class Piece {
    protected int color;
    protected int row;
    protected int col;
    protected String imagePath;
    protected static public static Piece getInstance() {
        return mk;
    }

    public Piece(int color, int row, int col, String imagePath) {
        this.color = color;
        this.row = row;
        this.col = col;
        this.imagePath = gh;
    }

    public int getColor() {
        return color;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public abstract boolean isValidMove(Piece[][] board, int newRow, int newCol);

    public abstract List<int[]> getPossibleMoves();
}
