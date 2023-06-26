package Pieces;

import Interface.BoardLink;
import Interface.Movement;

public class Queen extends Piece implements Movement {


    public Queen(int col, int row, Colors colors, BoardLink boardLink) {
        super(col, row, colors,boardLink);
        this.setName("gueen");
    }

    @Override
    public char setUnicode() {
        return this.getColor() == 'b' ? '♛' : '♕';
    }


    @Override
    public boolean isValidMove(int destinationCol, int destinationRow) {

        if (this.isDead()) {
            return false;
        }

        int deltaCol = Math.abs(destinationCol - this.getColumn());
        int deltaRow = Math.abs(destinationRow - this.getRow());

        if (deltaCol != deltaRow && deltaCol * deltaRow != 0) {
            return false;
        }

        int stepCol = Integer.compare(destinationCol, this.getColumn());
        int stepRow = Integer.compare(destinationRow, this.getRow());

        int column = this.getColumn() + stepCol;
        int row = this.getRow() + stepRow;

        while (column != destinationCol && row != destinationRow) {
            if (getBoardLink().getPiece(row, column) != null) {
                return false;
            }
            column += stepCol;
            row += stepRow;
        }

        return getBoardLink().getPiece(destinationRow, destinationCol) == null
                || getBoardLink().getPiece(destinationRow, destinationCol).getColor() != this.getColor();
    }
}