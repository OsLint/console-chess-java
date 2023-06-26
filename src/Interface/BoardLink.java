package Interface;

import Pieces.Piece;

import java.util.List;

public interface BoardLink {
    Piece getPiece (int row, int col);
    void killPiece(int row, int col);
    boolean isKingInCheck(List<Piece> pieces, char colorOfKing);
    boolean isCheckmate(List<Piece> pieces, char colorOfCurrentPlayer);
    boolean canSaveCheckmate();
}
