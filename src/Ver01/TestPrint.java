package Ver01;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;


public class TestPrint {

    public static void main(String[] args) {

        printNewBoard(S27773Projekt02.pieces);

    }
    public static void printNewBoard(List<Piece> pieces) {
        Board board = new Board();
        Board.startingBoard();
        for (int row = 7; row >= 0; row--) {
            System.out.print(row + 1 + " ");
            for (int col = 0; col < 8; col++) {
                Piece piece = board.takenBy(row, col);
                String color = (row + col) % 2 == 0 ? "47" : "100"; // kolor tła
                if (piece == null) {
                    System.out.print("\u001B[" + color + "m   \u001B[0m");
                } else {
                    String symbol = String.valueOf(piece.getUnicode());
                    String pieceColor = piece.getColor() == 'w' ? "97" : "30"; // kolor pionka
                    System.out.print("\u001B[" + color + ";"+ pieceColor + "m " + symbol + " \u001B[0m");
                }
            }
            System.out.println();
        }
        System.out.println("  a  b  c  d  e  f  g  h");
    }

}
