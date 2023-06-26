package Pieces;

import Interface.BoardLink;
import Interface.Movement;

public class Knight extends Piece implements Movement {


    public Knight(int col, int row, Colors colors, BoardLink boardLink) {
        super(col, row, colors,boardLink);
        this.setName("knight");
    }

    @Override
    public char setUnicode() {
        return this.getColor() == 'w' ? '♘' : '♞';
    }
    @Override
    public boolean isValidMove(int destinationCol, int destinationRow) {

        if (this.isDead()) {
            return false;
        }


        int dCol = Math.abs(destinationCol - this.getColumn());
        int dRow = Math.abs(destinationRow - this.getRow());

        if ((dCol == 1 && dRow == 2) || (dCol == 2 && dRow == 1)) {
            return getBoardLink().getPiece(destinationRow, destinationCol) == null
                    || getBoardLink().getPiece(destinationRow, destinationCol).getColor() != this.getColor();

        }
        return false;
    }
}