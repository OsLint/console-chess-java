package Pieces;

import Interface.BoardLink;
import Interface.Movement;

public class Rook extends Piece implements Movement {


    public Rook(int col, int row, Colors colors, BoardLink boardLink) {
        super(col, row, colors,boardLink);
        this.setName("rook");
    }

    @Override
    public char setUnicode() {
        return this.getColor() == 'b' ? '♜' : '♖';
    }
    @Override
    public boolean isValidMove(int destinationCol, int destinationRow) {

        if (this.isDead()) {
            return false;
        }

        if (destinationCol != this.getColumn() && destinationRow != this.getRow()) {
            return false;
        }


        if (getBoardLink().getPiece(destinationRow, destinationCol) != null) {
            if (getBoardLink().getPiece(destinationRow, destinationCol).getColor() == this.getColor()) {
                return false;
            }
        }
        int start, end;
        if (destinationCol == this.getColumn()) {
            start = Math.min(this.getRow(), destinationRow);
            end = Math.max(this.getRow(), destinationRow);
            for (int i = start + 1; i < end; i++) {
                if (getBoardLink().getPiece(i, this.getColumn()) != null) {
                    return false;
                }
            }
        } else {
            start = Math.min(this.getColumn(), destinationCol);
            end = Math.max(this.getColumn(), destinationCol);
            for (int i = start + 1; i < end; i++) {
                if (getBoardLink().getPiece(this.getRow(), i) != null) {
                    return false;
                }
            }
        }

        return true;

    }

    @Override
    public void move(int desCol, int desRow) {
        if (getBoardLink().getPiece(desRow, desCol) != null) {
            getBoardLink().getPiece(desRow, desCol);
        }
        setColumn(desCol);
        setRow(desRow);
    }
}