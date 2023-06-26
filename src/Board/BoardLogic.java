package Board;

import Interface.BoardLink;
import Pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BoardLogic implements BoardLink {
    public static int IndexOfCheckedKing;
    public static int IndexOfThreateningPiece;
    public static List<Piece> pieces = new ArrayList<>();

    @Override
    public  void killPiece(int row, int col) {
        for (Piece piece : pieces) {
            if (piece.getRow() == row && piece.getColumn() == col) {
                System.out.println("Killed: " + piece);
                piece.move(8, 8);
                piece.setDead(true);
            }
        }
    }
    @Override
    public boolean isKingInCheck(List<Piece> pieces, char colorOfKing) {
        int kingRow = -1;
        int kingCol = -1;

        int kingInQuestion = -1;

        //find king id and his row and col

        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).getColor() == colorOfKing && Objects.equals(pieces.get(i).getName(), "king")) {
                kingRow = pieces.get(i).getRow();
                kingCol = pieces.get(i).getColumn();
                kingInQuestion = i;
            }
        }

        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).isValidMove(kingCol, kingRow) && pieces.get(i).getColor() != colorOfKing) {
                IndexOfThreateningPiece = i;
                IndexOfCheckedKing = kingInQuestion;
                return true;
            }
        }

        return false;
    }
    @Override
    public  boolean isCheckmate(List<Piece> pieces, char colorOfCurrentPlayer) {

        boolean isKingInCheck = isKingInCheck(pieces, colorOfCurrentPlayer);

        if (!isKingInCheck) {
            return false;
        }

        for (int col = 0; col <= 7; col++) {
            for (int row = 0; row <= 7; row++) {
                if (pieces.get(IndexOfCheckedKing).isValidMove(col, row)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean canSaveCheckmate() {
        int pieceCol = pieces.get(IndexOfThreateningPiece).getColumn();
        int pieceRow = pieces.get(IndexOfThreateningPiece).getRow();

        for (Piece piece : pieces) {
            if (piece.isValidMove(pieceCol, pieceRow))
                return true;
        }
        return false;
    }

    public void startingBoard() {
        for (int col = 0; col < 8; col++) {
            pieces.add(new Pawn(col, 1, Colors.w,this));
            pieces.add(new Pawn(col, 6, Colors.b,this));
        }
        pieces.add(new Rook(0, 0, Colors.w,this));
        pieces.add(new Rook(7, 0, Colors.w,this));
        pieces.add(new Rook(0, 7, Colors.b,this));
        pieces.add(new Rook(7, 7, Colors.b,this));

        pieces.add(new Knight(1, 0, Colors.w,this));
        pieces.add(new Knight(6, 0, Colors.w,this));
        pieces.add(new Knight(1, 7, Colors.b,this));
        pieces.add(new Knight(6, 7, Colors.b,this));

        pieces.add(new Bishop(2, 0, Colors.w,this));
        pieces.add(new Bishop(5, 0, Colors.w,this));
        pieces.add(new Bishop(2, 7, Colors.b,this));
        pieces.add(new Bishop(5, 7, Colors.b,this));

        pieces.add(new Queen(3, 0, Colors.w,this));
        pieces.add(new Queen(3, 7, Colors.b,this));

        pieces.add(new King(4, 0, Colors.w,this));
        pieces.add(new King(4, 7, Colors.b,this));
    }

    @Override
    public Piece getPiece(int row, int col) {

        for (int i = 0; i < pieces.size(); i++) {
            try {
                if (pieces.get(i).getColumn() == col
                        && pieces.get(i).getRow() == row) {
                    return pieces.get(i);
                }
            } catch (NullPointerException e) {
                i++;
            }
        }
        return null;

    }


}
