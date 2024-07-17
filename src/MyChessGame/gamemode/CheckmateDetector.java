package MyChessGame.gamemode;

import MyChessGame.piece.*;

import java.util.*;

public class CheckmateDetector {
    private final Piece[][] board;
    private final List<Piece> wPieces;
    private final List<Piece> bPieces;
    private final King wk;
    private final King bk;

    public CheckmateDetector(Piece[][] board, List<Piece> wPieces, List<Piece> bPieces, King wk, King bk) {
        this.board = board;
        this.wPieces = wPieces;
        this.bPieces = bPieces;
        this.wk = wk;
        this.bk = bk;
    }

    public boolean isKingInCheck(King king) {
        for (Piece piece : (king.getColor() == 1 ? bPieces : wPieces)) {
            for (int[] move : piece.getPossibleMoves()) {
                if (piece.isValidMove(board, move[0], move[1]) &&
                        move[0] == king.getRow() && move[1] == king.getCol()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCheckmate(King king) {
        if (!isKingInCheck(king)) {
            return false;
        }

        for (int[] move : king.getPossibleMoves()) {
            if (!isMovePuttingKingInCheck(king, move)) {
                return false;
            }
        }

        if (canCaptureThreat(king)) {
            return false;
        }

        if (canBlockThreat(king)) {
            return false;
        }

        return true;
    }

    private boolean isMovePuttingKingInCheck(King king, int[] move) {
        Piece originalPiece = board[move[0]][move[1]];
        board[move[0]][move[1]] = king;
        board[king.getRow()][king.getCol()] = null;

        boolean inCheck = isKingInCheck(king);

        board[move[0]][move[1]] = originalPiece;
        board[king.getRow()][king.getCol()] = king;

        return inCheck;
    }

    private boolean canCaptureThreat(King king) {
        for (Piece piece : (king.getColor() == 1 ? wPieces : bPieces)) {
            for (Piece threat : getThreats(king)) {
                for (int[] move : piece.getPossibleMoves()) {
                    if (piece.isValidMove(board, move[0], move[1]) &&
                            move[0] == threat.getRow() && move[1] == threat.getCol()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean canBlockThreat(King king) {
        for (Piece piece : (king.getColor() == 1 ? wPieces : bPieces)) {
            for (Piece threat : getThreats(king)) {
                for (int[] move : getBlockingSquares(king, threat)) {
                    for (int[] pieceMove : piece.getPossibleMoves()) {
                        if (piece.isValidMove(board, pieceMove[0], pieceMove[1]) &&
                                pieceMove[0] == move[0] && pieceMove[1] == move[1]) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private List<Piece> getThreats(King king) {
        List<Piece> threats = new ArrayList<>();
        for (Piece piece : (king.getColor() == 1 ? bPieces : wPieces)) {
            for (int[] move : piece.getPossibleMoves()) {
                if (piece.isValidMove(board, move[0], move[1]) &&
                        move[0] == king.getRow() && move[1] == king.getCol()) {
                    threats.add(piece);
                }
            }
        }
        return threats;
    }

    private List<int[]> getBlockingSquares(King king, Piece threat) {
        List<int[]> blockingSquares = new ArrayList<>();

        int xDirection = Integer.compare(threat.getRow(), king.getRow());
        int yDirection = Integer.compare(threat.getCol(), king.getCol());

        int x = king.getRow() + xDirection;
        int y = king.getCol() + yDirection;

        while (x != threat.getRow() || y != threat.getCol()) {
            blockingSquares.add(new int[]{x, y});
            x += xDirection;
            y += yDirection;
        }

        return blockingSquares;
    }
}
