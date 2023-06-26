package Pieces;

import Interface.BoardLink;
import Interface.Movement;

import java.util.Scanner;

import static Board.BoardLogic.pieces;

public class Pawn extends Piece  implements Movement {


    public Pawn(int col, int row, Colors colors, BoardLink boardLink) {
        super(col, row, colors,boardLink);
        this.setName("pawn");
    }

    @Override
    public char setUnicode() {
        return this.getColor() == 'b' ? '♟' : '♙';
    }
    @Override
    public boolean isValidMove(int destinationCol, int destinationRow) {
        if (this.isDead()) {
            return false;
        }


        int direction;
        if (this.getColor() == 'w') {
            direction = 1;
        } else {
            direction = -1;
        }

        if (destinationCol == this.getColumn()) {

            if ((destinationRow == this.getRow() + direction)) {
                return getBoardLink().getPiece(destinationRow, destinationCol) == null;
            } else if ((this.getRow() == 1 && this.getColor() == 'w' ||
                    (this.getRow() == 6 && this.getColor() != 'w'))) {
                return destinationRow == this.getRow() + 2 * direction
                        && getBoardLink().getPiece(destinationRow, destinationCol) == null;
            }
        } else if (Math.abs(destinationCol - this.getColumn()) == 1 && destinationRow == getRow() + direction) {
            if (getBoardLink().getPiece(destinationRow, destinationCol) == null) {
                return false;
            } else return getBoardLink().getPiece(destinationRow, destinationCol).getColor() != this.getColor();
        }

        return false;
    }

    private boolean shouldPromote() {
        if (this.getColor() == 'w' && this.getRow() == 7) {
            return true;
        } else return this.getColor() == 'b' && this.getRow() == 0;
    }

    private void promote() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(
                "Chose new piece (Q - Queen, R - Rook, B - Bishop, N - Knight): ");
        char promotionChoice = scanner.next().toUpperCase().charAt(0);

        while (promotionChoice != 'Q' && promotionChoice != 'R' && promotionChoice != 'B' && promotionChoice != 'N') {
            System.out.println(
                    "Incorrect input (Q - Queen, R - Rook, B - Bishop, N - Knight): ");
            promotionChoice = scanner.next().toUpperCase().charAt(0);
        }

        int index = -1;
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i) == this) {
                index = i;
                break;
            }
        }

        int thisRow = pieces.get(index).getRow();
        int thisCol = pieces.get(index).getColumn();

        this.forceMove(8, 8);

        Colors chooseColor;
        if (this.getColor() == 'w') {
            chooseColor = Colors.w;
        } else {
            chooseColor = Colors.b;
        }

        switch (promotionChoice) {
            case 'Q' -> pieces.add(new Queen(thisCol, thisRow, chooseColor, getBoardLink()));
            case 'R' -> pieces.add(new Rook(thisCol, thisRow, chooseColor, getBoardLink()));
            case 'B' -> pieces.add(new Bishop(thisCol, thisRow, chooseColor, getBoardLink()));
            case 'N' -> pieces.add(new Knight(thisCol, thisRow, chooseColor, getBoardLink()));
        }
    }

    @Override
    public void move(int desCol, int desRow) {
        if (getBoardLink().getPiece(desRow, desCol) != null) {
            getBoardLink().killPiece(desRow, desCol);
        }
        setColumn(desCol);
        setRow(desRow);
        if (shouldPromote()) {
            promote();
        }
    }
    @Override
    public void forceMove(int desCol, int desRow) {
        setColumn(desCol);
        setRow(desRow);
    }


}
