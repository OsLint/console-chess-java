package Pieces;

import Interface.BoardLink;
import Interface.Movement;

import static Board.BoardLogic.pieces;

public class King extends Piece implements Movement {

    private static int castledRookID;

    public King(int col, int row, Colors colors, BoardLink boardLink) {
        super(col, row, colors, boardLink);
        this.setName("king");
    }

    @Override
    public char setUnicode() {
        return this.getColor() == 'b' ? '♚' : '♔';
    }


    @Override
    public boolean isValidMove(int destinationCol, int destinationRow) {

        if (this.isDead()) {
            return false;
        }

        int deltaCol = Math.abs(destinationCol - this.getColumn());
        int deltaRow = Math.abs(destinationRow - this.getRow());

        if (deltaCol == 3 && deltaRow == 0 || deltaCol == 4 && deltaRow == 0) {
            if (canDoCastling(destinationCol)) {
                return true;
            }
        }

        if (deltaCol > 1 || deltaRow > 1) {
            return false;
        }
        if (getBoardLink().getPiece(destinationRow, destinationCol) != null) {
                return getBoardLink().getPiece(destinationRow, destinationCol).getColor() != this.getColor();
        }

        return true;
    }

    public boolean canDoCastling(int destinationCol) {


        if (getBoardLink().isKingInCheck(pieces, this.getColor())) {
            return false;
        }


        int kingCol = this.getColumn();
        int rookCol = (destinationCol > kingCol) ? 7 : 0;
        int row = this.getRow();


        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).getColumn() == rookCol
                    && pieces.get(i).getRow() == row) {
                castledRookID = i;
            }
        }


            return !this.isMoved() && !pieces.get(castledRookID).isMoved();
    }

    @Override
    public void move(int desCol, int desRow) {

        if (canDoCastling(desCol) && isValidMove(desCol, desRow)) {

            int kingNewCol, rookNewCol;

            if (desCol == 0) {
                kingNewCol = 2;
                rookNewCol = 3;
            } else {
                kingNewCol = 6;
                rookNewCol = 5;
            }
            this.forceMove(kingNewCol, desRow);
            pieces.get(castledRookID).forceMove(rookNewCol, desRow);
            this.setMovedAlready(true);
            pieces.get(castledRookID).setMovedAlready(true);

        } else {
            super.move(desCol, desRow);
        }
    }
}