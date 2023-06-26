package Board;

import Interface.BoardLink;
import Pieces.Piece;

public class BoardVisual {

    private final BoardLink boardLink;

    public BoardVisual (BoardLink boardLink) {
        this.boardLink = boardLink;
    }


    public void printBoard ( ) {

        for (int row = 7; row >= 0; row--) {
            System.out.print(row + 1 + " ");
            for (int col = 0; col < 8; col++) {
                Piece piece = boardLink.getPiece(row, col);
                String color = (row + col) % 2 == 0 ? "47" : "100"; //color of background
                if (piece == null) {
                    System.out.print("\u001B[" + color + "m   \u001B[0m");
                } else {
                    String symbol = String.valueOf(piece.getUnicode());
                    String pieceColor = piece.getColor() == 'w' ? "97" : "30"; // color of piece
                    System.out.print("\u001B[" + color + ";"+ pieceColor + "m " + symbol + " \u001B[0m");
                }
            }
            System.out.println();
        }
        System.out.println("  a  b  c  d  e  f  g  h");
    }


}
