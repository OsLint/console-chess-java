package Ver02;

/**
 * G.U.I Projekt 02 "Szachy"
 *
 * @author Oskar Kalbarczyk S27773
 * @version 11
 **/


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static java.lang.Character.toUpperCase;

public class S27773Projekt02 { //klasa main


    public static List<Piece> pieces = new ArrayList<> ();

    public static void main ( String[] args ) {

        //Ustawienie kodowania strumienia wyjściowego na UTF_8
        System.setOut (
                new PrintStream (
                        new FileOutputStream (FileDescriptor.out), true, StandardCharsets.UTF_8));


        Scanner scanner = new Scanner (System.in);
        Board board = new Board ();

        //Powitanie
        System.out.println ("---Oskar Kalbarczyk---");
        System.out.println ("Student ID: s27773");
        System.out.println ("Przedstawia: G.U.I. Projekt 02 \"Szachy\"");

        //Nowa gra czy wczytanie zapisu?
        System.out.println ("Wybierz opcje:");
        System.out.println ("1. Nowa Gra");
        System.out.println ("2. Wczytaj zapis gry");


            int choice = scanner.nextInt ();


            switch (choice) {
                case 2 ->  {
                    System.out.print ("Podaj nazwe pliku który chcesz wczytać: ");
                    System.out.println ("(pamiętaj o dodaniu rozszerzenia: \".bin\"");
                    String fileName = scanner.nextLine ();
                    fileName = scanner.nextLine ();
                    System.out.println("Podano nazwe pliku");
                    FileHandler.loadPieces(fileName,pieces);

                    for (int i = 0; i < pieces.size(); i++) {
                        System.out.println(pieces.get(i));
                    }

                }

                case 1 -> {
                    Board.startingBoard ();
                    for (int i = 0; i < pieces.size(); i++) {
                        System.out.println(pieces.get(i));
                    }

                }
                default -> throw new InputMismatchException("Niepoprawny wybór");

            }


        int inGameChoice;

        boolean gameOngoing = true; // gra w trakcie
        boolean isWhiteTurn = true; // ruch gracza białych bierek
        boolean drawOffered = false; // propozycja remisu



        while (gameOngoing) {
            board.printBoard ();
            System.out.println ("Ruch: " + (isWhiteTurn ? "Białych" : "Czarnych"));
            System.out.println ("Wybierz opcje:");
            System.out.println ("1. Wykonaj ruch bierką");
            System.out.println ("2. Zapisz i wyjdź z gry");
            System.out.println ("3. Zaproponuj/Przyjmij remis");
            System.out.println ("4. Poddaj się");

            inGameChoice = scanner.nextInt ();

            //Jeśli gracz wykona ruch o 2 pola pionkiem to jego boolean
            //justMove2Squares jest ustawiony na true
            //natomiast jeśli ponownie następuje jego ruch boolean ten jest
            //zmieniany na false
            GameLogic.setJustMove2SquaresToFalse(isWhiteTurn ? 'w' : 'b');



            switch (inGameChoice) {
                case 1:
                    boolean moveSuccessful = false;
                    while (!moveSuccessful) {

                        try {

                            System.out.print ("Wybierz bierkę (n.p. E4): ");
                            Scanner inputScan = new Scanner (System.in);

                            String inputChoosePiece = inputScan.nextLine ();
                            char startCharacter = inputChoosePiece.charAt (0);
                            char startNumber = inputChoosePiece.charAt (1);


                            int startingCol = PlayerInput.returnCol (startCharacter);
                            int startingRow = PlayerInput.returnRow (startNumber);

                            int pieceIndex = PlayerInput.ShowSelectedPieceIndex (startingCol, startingRow);

                            boolean correctColor = false;


                            if (isWhiteTurn && pieces.get (pieceIndex).getColor () == 'w') {
                                correctColor = true;
                            } else if (!isWhiteTurn && pieces.get (pieceIndex).getColor () == 'b') {
                                correctColor = true;
                            }


                            if (correctColor) {
                                System.out.print ("Wbierz pole docelowe (e.g. E2): ");

                                String inputDestination = inputScan.nextLine ();

                                char destCharacter = inputDestination.charAt (0);
                                char destNumber = inputDestination.charAt (1);

                                int destRow = PlayerInput.returnRow (destNumber);
                                int destCol = PlayerInput.returnCol (destCharacter);

                                try {
                                    if (pieces.get (pieceIndex).isValidMove (destCol, destRow)) {
                                       // ruch testowy by sprawdzić czy powoduje on szacha
                                            boolean moveCreatesCheckmate = false;
                                                int indexOfPieceKilledDuringTest = -1;
                                                int killedPieceRow = -1;
                                                int killedPieceCol = -1;
                                                int ourPieceRow = pieces.get(pieceIndex).getRow();
                                                int ourPieceCol = pieces.get(pieceIndex).getColumn();
                                                boolean weKilledSomeone = false;
                                            //sprawdzamy czy w testowym ruchu zbijamy bierkę
                                            if(Board.takenBy(destRow,destCol) != null ) {
                                                //jeśli tak pozyskujemy jej Index
                                                for (int i = 0; i < pieces.size(); i++) {
                                                    if (pieces.get(i).getColumn() ==  destCol
                                                    &&pieces.get(i).getRow() ==  destRow ) {
                                                        indexOfPieceKilledDuringTest = i;
                                                       //oraz pozycje na planszy
                                                         killedPieceCol =
                                                                 pieces.get(indexOfPieceKilledDuringTest).getColumn();
                                                         killedPieceRow =
                                                                 pieces.get(indexOfPieceKilledDuringTest).getRow();
                                                       //testowo zbijamy bierke
                                                        pieces.get(indexOfPieceKilledDuringTest)
                                                                .forceMove(8,8);
                                                        //zapisujemy że zbiliśmy bierke w ruchu testowym
                                                        weKilledSomeone = true;
                                                    }
                                                }
                                            }

                                            //testowy ruch
                                            pieces.get (pieceIndex).forceMove (destCol, destRow);
                                            //sprawdzamy czy występuje szach po wykonaniu ruchu
                                           moveCreatesCheckmate = GameLogic.isCheckmate(pieces,isWhiteTurn ? 'w' : 'b');
                                            //przywracamy bierke na swoje miejsce
                                           pieces.get (pieceIndex).forceMove (ourPieceCol, ourPieceRow);
                                           //jeśli zbiliśmy bierkę to ją "ożywiamy"
                                           if (weKilledSomeone) {
                                               pieces.get(indexOfPieceKilledDuringTest).forceMove(killedPieceCol
                                                       ,killedPieceRow);
                                           }

                                           //jeśli ruch nie powoduje szacha to go wykonujemy
                                        if(!moveCreatesCheckmate) {
                                            pieces.get (pieceIndex).move (destCol, destRow);
                                            //  System.out.println ("Bierka: " + pieces[pieceIndex] + "Wykonała ruch na: "
                                            //          + destCharacter + destNumber);
                                            moveSuccessful = true;
                                        }else {
                                            System.out.println("Twój król jest w szachu lub będzie po tym ruchu");
                                        }


                                    } else {
                                        System.out.println ("Ruch niepoprawny spróbuj jeszcze raz");
                                    }
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    System.out.println ("Bierka nie odnaleziona ruch niepoprawny");
                                }
                            } else {
                                System.out.println ("Ruch niepoprawny spróbuj jeszcze raz");
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println ("Nie znaleziono bierki spróbuj jeszcze raz");
                        }

                    }

                    isWhiteTurn = !isWhiteTurn;

                    if (GameLogic.isCheckmate (pieces, isWhiteTurn ? 'w' : 'b') && !GameLogic.canSaveCheckmate()) {
                        System.out.println ("Szach-mat! Gracz " + (isWhiteTurn ? "Czarny" : "Biały") + " wygrywa!");
                        gameOngoing = false;
                    } else if (GameLogic.isKingInCheck (pieces, isWhiteTurn ? 'w' : 'b')) {
                        System.out.println ("Król " + (isWhiteTurn ? "Biały" : "Czarny") + " jest w szachu!");

                    }


                    break;
                case 2:
                    System.out.print ("Podaj nazwe pliku w jakim chcesz zapisać gre: ");
                    System.out.println ("(Pamiętaj o rozszerzeniu \".bin\"");
                    String fileName = scanner.nextLine();
                    fileName = scanner.nextLine();
                    FileHandler.saveBoard (fileName);
                    System.out.println ("Zapisano gre. Do zobaczenia!");
                    gameOngoing = false;
                    break;
                case 3:
                    if (drawOffered) {
                        System.out.println ("Zaakceptowano remis. Gra zakończona remisem!.");
                        gameOngoing = false;
                    } else {
                        drawOffered = true;
                        System.out.println ("Zaproponowano remis. Oczekiwanie na decyzję drugiego gracza.");
                        isWhiteTurn = !isWhiteTurn;
                    }
                    break;
                case 4:
                    System.out.println ("Gracz " + (isWhiteTurn ? "Biały" : "Czarny")
                            + " Poddał się. Koniec gry...");
                    gameOngoing = false;
                    break;

                default:
                    System.out.println ("Nieprawidłowy wybór. Proszę spróbuj ponownie.");
                    break;
            }

            // Resetujemy propozycje remisu w przypadku jeśli drugi gracz się nie zgodzi
            if (gameOngoing && inGameChoice!= 3) {
                drawOffered = false;
            }
        }

        scanner.close ();
    }

}

class GameLogic {
    public static int IndexOfCheckedKing;
    public static int IndexOfThreateningPiece;

    public static void killPiece ( int row, int col ) {
        for (int i = 0; i < S27773Projekt02.pieces.size (); i++) {
            if (S27773Projekt02.pieces.get (i).getRow () == row && S27773Projekt02.pieces.get (i).getColumn () == col) {
                System.out.println ("Zbito: " + S27773Projekt02.pieces.get (i).toString ());
                S27773Projekt02.pieces.get (i).move (8, 8);
                S27773Projekt02.pieces.get (i).setDead (true);
            }
        }
    }

    public static boolean isKingInCheck ( List<Piece> pieces, char colorOfKing ) {
        int kingRow = -1;
        int kingCol = -1;

        int kingInQuestion = -1;

        //znajdujemy id Króla oraz jego położenie

        for (int i = 0; i < pieces.size (); i++) {
            if (pieces.get (i).getColor () == colorOfKing && pieces.get (i).getId () == 1) {
                kingRow = pieces.get (i).getRow ();
                kingCol = pieces.get (i).getColumn ();
                kingInQuestion = i;
            }
        }

        for (int i = 0; i < pieces.size (); i++) {
            if (pieces.get (i).isValidMove (kingCol, kingRow) && pieces.get (i).getColor () != colorOfKing) {
                IndexOfThreateningPiece = i;
                IndexOfCheckedKing = kingInQuestion;
                return true;
            }
        }

        return false;
    }

    public static boolean isCheckmate ( List<Piece> pieces, char colorOfCurrentPlayer ) {

        //Czy król jest w szachu
        boolean isKingInCheck = isKingInCheck (pieces, colorOfCurrentPlayer);

        if (!isKingInCheck) {
            return false;
        }

        // Sprawdź czy istnieje legalny ruch dla gracza,który jest w szachu
        for (int col = 0; col <= 7; col++) {
            for (int row = 0; row <= 7; row++) {
                if (pieces.get (IndexOfCheckedKing).isValidMove (col, row)) {
                    return true;
                }
            }

        }


        return false;
    }


    public static boolean canSaveCheckmate () {
        //sprawdzamy, na jakim polu znajduje się bierka która szachuje króla
        int pieceCol = S27773Projekt02.pieces.get(IndexOfThreateningPiece).getColumn();
        int pieceRow = S27773Projekt02.pieces.get(IndexOfThreateningPiece).getRow();

        //sprawdzamy, czy jakakolwiek bierka może go zbić (bierki nie
        // mogą się ruszyć na pole okupowane przez bierkę tego samego koloru).
        for (int i = 0; i < S27773Projekt02.pieces.size(); i++) {
            if(S27773Projekt02.pieces.get(i).isValidMove(pieceCol,pieceRow))
                return true;
        }
        return false;
    }


    //metoda  ta jest wykożystywana
    //do ustawienia booleanu justMove2Squares
    //na False po wykonaniu ruchu przez
    //gracza przeciwnego koloru
    public static void setJustMove2SquaresToFalse (char color) {
        for (int i = 0; i < S27773Projekt02.pieces.size(); i++) {
            if (S27773Projekt02.pieces.get(i).getColor() == color)
            S27773Projekt02.pieces.get(i).setJustMoved2Squares(false);
        }
    }


}

class Board {
    public Board ( ) {
        //printBoard ();
    }

    public void printBoard ( ) {

        for (int row = 7; row >= 0; row--) {
            System.out.print(row + 1 + " ");
            for (int col = 0; col < 8; col++) {
                Piece piece = Board.takenBy(row, col);
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

    public char printSquares ( int row, int col ) {
        if (isBlackSquare (row, col)) {
            //czarny kwadrat
            return '\u25A0';
        } else {
            //biały kwadrat
            return '\u25A1';
        }
    }

    public boolean isBlackSquare ( int row, int col ) {
        if (row % 2 == 0) {
            return col % 2 == 0;
        } else {
            return col % 2 == 1;
        }
    }

    public static Piece takenBy ( int row, int col ) {

        for (int i = 0; i < S27773Projekt02.pieces.size (); i++) {
            try {
                if (S27773Projekt02.pieces.get (i).getColumn () == col
                        && S27773Projekt02.pieces.get (i).getRow () == row) {
                    return S27773Projekt02.pieces.get (i);
                }
            } catch (NullPointerException e) {
                i++;
            }
        }
        return null;

    }

    public static void startingBoard ( ) {
        int index = 0;
        // Tworzenie pionków
        for (int col = 0; col < 8; col++) {
            S27773Projekt02.pieces.add (new Pawn (col, 1, Colors.w));
            S27773Projekt02.pieces.add (new Pawn (col, 6, Colors.b));
        }

        // Tworzenie wież
        S27773Projekt02.pieces.add (new Rook (0, 0, Colors.w));
        S27773Projekt02.pieces.add (new Rook (7, 0, Colors.w));
        S27773Projekt02.pieces.add (new Rook (0, 7, Colors.b));
        S27773Projekt02.pieces.add (new Rook (7, 7, Colors.b));

        // Tworzenie skoczków
        S27773Projekt02.pieces.add (new Knight (1, 0, Colors.w));
        S27773Projekt02.pieces.add (new Knight (6, 0, Colors.w));
        S27773Projekt02.pieces.add (new Knight (1, 7, Colors.b));
        S27773Projekt02.pieces.add (new Knight (6, 7, Colors.b));

        // Tworzenie gońców
       S27773Projekt02.pieces.add (new Bishop (2, 0, Colors.w));
       S27773Projekt02.pieces.add (new Bishop (5, 0, Colors.w));
        S27773Projekt02.pieces.add (new Bishop (2, 7, Colors.b));
        S27773Projekt02.pieces.add (new Bishop (5, 7, Colors.b));

        // Tworzenie królowych
        S27773Projekt02.pieces.add (new Queen (3, 0, Colors.w));
        S27773Projekt02.pieces.add (new Queen (3, 7, Colors.b));

        // Tworzenie królów
        S27773Projekt02.pieces.add (new King (4, 0, Colors.w));
        S27773Projekt02.pieces.add (new King (4, 7, Colors.b));
    }


}

//Klasa odpowiedzialna za obsługę plików
class FileHandler {
    String fileName;
    static byte[] bytes = new byte[(S27773Projekt02.pieces.size () * 2)];

    //Zapis planszy
    static public void saveBoard ( String fileName ) {

        for (int i = 0; i < S27773Projekt02.pieces.size (); i++) {
            savePeace (S27773Projekt02.pieces.get (i), i);
        }
        try (FileOutputStream fos = new FileOutputStream (fileName)) {
            fos.write (bytes);
        } catch (IOException e) {
            e.printStackTrace ();
        }
        System.out.println ("Zapisano plik: " + fileName);
    }

    //Zapis pojedyńczej bierki
    public static void savePeace ( Piece piece, int i ) {
        // << 5 ustawiamy na najbardziej znaczącym bicie                   // >> wyrzuca poza bajt
        byte b1 = (byte) ((piece.getId () << 5) | (piece.getColumn () << 1) | (piece.getRow () >> 3));

        byte b2 = (byte) ((piece.getRow () << 5) | (colorToByte (piece) << 4));
        bytes[i * 2] = b1;
        bytes[i * 2 + 1] = b2;
    }

    //przypisanie wartości bitowej do koloru bierki
    public static byte colorToByte ( Piece piece ) {
        if (piece.getColor () == 'b') {
            return 0;
        } else {
            return 1;
        }
    }

    public static Colors byteToColor ( boolean bit ) {
        if (!bit) {
            return Colors.b;
        } else {
            return Colors.w;
        }
    }

    //2DO: Odczytywanie pliku

    public static void loadPieces(String fileName, List<Piece> pieces) {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(fileName))) {
            byte b;
            while(((b =dis.readByte()) >= -1)) {
                int id = b & 0b00000111;
                int column = (b >> 3) & 0b00001111;
                int row = (b >> 7) | ((dis.readByte() & 0b00001111) << 1);
                boolean isWhite = ((b >> 6) & 0b00000001) == 0;
                switch (id) {
                    case 0:
                        pieces.add(new Pawn(column, row, isWhite ? Colors.w : Colors.b));
                        break;
                    case 1:
                        pieces.add(new King(column, row, isWhite ? Colors.w : Colors.b));
                        break;
                    case 2:
                        pieces.add(new Queen(column, row, isWhite ? Colors.w : Colors.b));
                        break;
                    case 3:
                        pieces.add(new Rook(column, row, isWhite ? Colors.w : Colors.b));
                        break;
                    case 4:
                        pieces.add(new Bishop(column, row, isWhite ? Colors.w : Colors.b));
                        break;
                    case 5:
                        pieces.add(new Knight(column, row, isWhite ? Colors.w : Colors.b));
                        break;
                    default:
                        // Nieznany identyfikator bierki - pomiń
                        break;
                }
            }
            System.out.println("Pomyślnie odczytano plik " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

//Interfejs poruszania
interface Movement {

    public boolean isValidMove ( int desCol, int desRow );

    void move ( int desCol, int desRow );

    void forceMove ( int desCol, int desRow );


}

//Enum dostępnych kolorów bierek
enum Colors {
    w ("white"), b ("black");

    Colors ( String fullName ) {
    }
}

abstract class Piece implements Movement {
    private String name;
    private char unicode;
    private int id;
    private int column; //x
    private int row; //y
    private char color;

    private boolean isDead;

    private boolean moved = false;

    boolean justMoved2Squares = false;


    public Piece ( int col, int row, Colors colors ) {
        this.column = col;
        this.row = row;
        switch (colors) {
            case w -> this.color = 'w';
            case b -> this.color = 'b';
            default -> throw new IllegalArgumentException ("Ten kolor nie istnieje");
        }
        this.unicode = setUnicode ();
        this.id = setId ();
    }

    //Poruszanie bierki

    @Override
    public void move ( int desCol, int desRow ) {
        if (Board.takenBy (desRow, desCol) != null) {
            System.out.println ("Zbito: " + Board.takenBy (desRow, desCol).toString ());
            GameLogic.killPiece (desRow, desCol);
            this.moved = true;

        }
        this.column = desCol;
        this.row = desRow;
    }

    @Override
    public void forceMove ( int desCol, int desRow ) {
       // System.out.println ("Wymuszono Ruch: " + this.toString () + " Nowa pozycja" +
       //         " Columna: " + desCol + " Rząd: " + desRow);
        this.column = desCol;
        this.row = desRow;
    }

    //Setter movedAlready

    public void setMovedAlready(boolean moved) {
        this.moved = moved;
    }

    //setter czy pionek poruszył się właśnie o dwa pola
    public void setJustMoved2Squares(boolean justMoved2Squares) {
        this.justMoved2Squares = justMoved2Squares;
    }

    //Zbijanie bierki

    public void setDead ( boolean dead ) {
        isDead = dead;
    }

    //Seter Przedstawienia graficznego
    public char setUnicode ( ) {
        return ' ';
    }


    //Setter id pionu w zapisie binarnym
    public int setId ( ) {
        return -1;
    }

    //Setter imienia
    public void setName ( String name ) {
        this.name = name;
    }

    //Getery odpowiadające za sprawdzenie aktualnej pozycji pionu
    public int getRow ( ) {
        return row;
    }

    public int getColumn ( ) {
        return column;
    }

    //Getter id pionu w zapisie binarnym
    public int getId ( ) {
        return id;
    }


    //Czy pionek jest zbity?

    public boolean isDead ( ) {
        return isDead;
    }

    //Getter Przedstawienia graficznego
    public char getUnicode ( ) {
        return unicode;
    }

    //Getter czy bierka się ruszyła

    public boolean isMoved() {
        return moved;
    }

    //Getter koloru pionu
    public char getColor ( ) {
        return color;
    }

    //getter czy pionek poruszył się właśnie o dwa pola
    public boolean isJustMoved2Squares() {
        return justMoved2Squares;
    }



    //Metoda zmiany pozycji

    @Override
    public boolean isValidMove ( int destinationCol, int destinationRow ) {
        return false;
    }


    @Override
    public String toString ( ) {
        return "Piece{" +
                "name='" + name + '\'' +
                ", unicode=" + unicode +
                ", id=" + id +
                ", column=" + column +
                ", row=" + row +
                ", color=" + color +
                '}';
    }
}

//Pionek
class Pawn extends Piece {


    public Pawn ( int col, int row, Colors colors ) {
        super (col, row, colors);
        this.setName ("pawn");
    }

    @Override
    public char setUnicode ( ) {
        return this.getColor () == 'b' ? '\u265F' : '\u2659';
    }

    @Override
    public int setId ( ) {
        return 0;
    }


    //x=col
    //y=row

    @Override
    public boolean isValidMove ( int destinationCol, int destinationRow ) {
        if (this.isDead ()) {
            return false;
        }

        int direction;
        if (this.getColor () == 'w') {
            direction = 1;
        } else {
            direction = -1;
        }

        if (destinationCol == this.getColumn ()) {

            if ((destinationRow == this.getRow () + direction)) {
                //Ruch o jedno pole do przodu
                if (Board.takenBy (destinationRow, destinationCol) == null) {
                    return true;
                }
                return false;
            } else if ((this.getRow () == 1 && this.getColor () == 'w' ||
                    (this.getRow () == 6 && this.getColor () != 'w'))) {
                // Ruch o dwa pola do przodu tylko na pierwszym ruchu pionka
                return destinationRow == this.getRow () + 2 * direction
                        && Board.takenBy (destinationRow, destinationCol) == null;
            }
        } else if (Math.abs (destinationCol - this.getColumn ()) == 1 && destinationRow == getRow () + direction) {
            //Ruch na skos w przypadku bicia przeciwnika
            if (Board.takenBy (destinationRow, destinationCol) == null) {
                return false;
            } else if (Board.takenBy (destinationRow, destinationCol).getColor () != this.getColor ()) {
                return true;
            }
        }

        return false;
    }

    private boolean shouldPromote ( ) {
        if (this.getColor () == 'w' && this.getRow () == 7) {
            return true;
        } else if (this.getColor () == 'b' && this.getRow () == 0) {
            return true;
        }
        return false;
    }

    private void promote ( ) {
        Scanner scanner = new Scanner (System.in);
        System.out.println (
                "Wybierz figurę na którą awansuje pion (Q - Królowa, R - wieża, B - goniec, N - skoczek): ");
        char promotionChoice = scanner.next ().toUpperCase ().charAt (0);

        while (promotionChoice != 'Q' && promotionChoice != 'R' && promotionChoice != 'B' && promotionChoice != 'N') {
            System.out.println (
                    "Nieprawidłowy wybór. Spróbuj ponownie (Q - hetman, R - wieża, B - goniec, N - skoczek): ");
            promotionChoice = scanner.next ().toUpperCase ().charAt (0);
        }

        int index = -1;
        for (int i = 0; i < S27773Projekt02.pieces.size (); i++) {
            if (S27773Projekt02.pieces.get (i) == this) {
                index = i;
                break;
            }
        }

        int thisRow = S27773Projekt02.pieces.get (index).getRow ();
        int thisCol = S27773Projekt02.pieces.get (index).getColumn ();

        this.forceMove(8, 8);

        Colors chooseColor;
        if (this.getColor () == 'w') {
            chooseColor = Colors.w;
        } else {
            chooseColor = Colors.b;
        }

        switch (promotionChoice) {
            case 'Q':
                S27773Projekt02.pieces.add (new Queen (thisCol, thisRow, chooseColor));
                break;
            case 'R':
                S27773Projekt02.pieces.add (new Rook (thisCol, thisRow, chooseColor));
                break;
            case 'B':
                S27773Projekt02.pieces.add (new Bishop (thisCol, thisRow, chooseColor));
                break;
            case 'N':
                S27773Projekt02.pieces.add (new Knight (thisCol, thisRow, chooseColor));
                break;
        }
    }

    @Override
    public void move ( int desCol, int desRow ) {
        super.move (desCol, desRow);
        if (shouldPromote ()) {
            promote ();
        }
        if ((this.getRow () == 1 && this.getColor () == 'w' ||
                (this.getRow () == 6 && this.getColor () != 'w'))) {
            this.setJustMoved2Squares(true);
        }
    }

    //gracz musi wybrać miejsce, na którym znajduje się aktualnie
    //pionek, który dopiero co wykonał ruch o dwa pola

    /**
     *
     * @param destCol Kolumna w której znajduje się pion którego chcemy zbić
     * @param destRow Rząd w którym znajduje się pion którego chcemy zbić
     * @return
     */
    public boolean canDoEnPassant (int destCol,int destRow) {
       int JustMovedPawnID = -1;



       //sprawdzamy id piona, który poruszył się dopiero o 2 pola
        for (int i = 0; i < S27773Projekt02.pieces.size(); i++) {
            if (Board.takenBy(destRow,destCol).getRow() == destRow
            && Board.takenBy(destRow,destCol).getColumn() == destCol) {
                JustMovedPawnID = i;
            }
        }
        //Ustalamy rząd (kierunek) na który poruszy się gracz po zbiciu w przelocie
        int newRow;
        if(S27773Projekt02.pieces.get(JustMovedPawnID).getColor() == 'w') {
            newRow = destRow - 1;

        }else {
            newRow = destRow + 1;
        }



        //jeśli nie odnaleziono bierki ruch nieprawidłowy
        if(JustMovedPawnID == -1) {
            return false;
        }
        //Sprawdzamy, czy pion ten poruszył się dopiero o dwa pola
        if (S27773Projekt02.pieces.get(JustMovedPawnID).isJustMoved2Squares()) {
            //Sprawdzamy czy  na polu które chcemy się ruszyć znajduje się bierka
            if(Board.takenBy(newRow,destCol) == null) {
                return false;
            }




        }
        return false;
    }


}

//Wieża

class Rook extends Piece {


    public Rook ( int col, int row, Colors colors ) {
        super (col, row, colors);
        this.setName ("rook");
    }

    @Override
    public char setUnicode ( ) {
        return this.getColor () == 'b' ? '\u265C' : '\u2656';
    }

    @Override
    public int setId ( ) {
        return 3;
    }

    //x=col
    //y=row

    @Override
    public boolean isValidMove ( int destinationCol, int destinationRow ) {

        if (this.isDead ()) {
            return false;
        }

        // Sprawdzenie, czy docelowe pole znajduje się w tym samym rzędzie lub kolumnie co wieża
        if (destinationCol != this.getColumn () && destinationRow != this.getRow ()) {
            return false; // ruch nie jest prawidłowy
        }

        // Sprawdzenie, czy docelowe pole jest puste lub zajęte przez figurę przeciwnego koloru
        //Ruch nie jest prawidłowy jeśli w docelowym polu nie znajduje się pion innego koloru

        if (Board.takenBy (destinationRow, destinationCol) != null) {
            if (Board.takenBy (destinationRow, destinationCol).getColor () == this.getColor ()) {
                return false;
            }
        }
        // Sprawdzenie, czy po drodze do docelowego pola nie ma innych figur
        int start, end;
        if (destinationCol == this.getColumn ()) {
            start = Math.min (this.getRow (), destinationRow);
            end = Math.max (this.getRow (), destinationRow);
            for (int i = start + 1; i < end; i++) {
                if (Board.takenBy (i, this.getColumn ()) != null) {
                    return false; // po drodzę znajduje się inna bierka ruch nie jest prawidłowy
                }
            }
        } else { //destinationRow = this.getRow();
            start = Math.min (this.getColumn (), destinationCol);
            end = Math.max (this.getColumn (), destinationCol);
            for (int i = start + 1; i < end; i++) {
                if (Board.takenBy (this.getRow (), i) != null) {
                    return false;
                }
            }
        }

        return true; //ruch jest prawidłowy

    }
}

//Królowa

class Queen extends Piece {


    public Queen ( int col, int row, Colors colors ) {
        super (col, row, colors);
        this.setName ("gueen");
    }

    @Override
    public char setUnicode ( ) {
        return this.getColor () == 'b' ? '\u265B' : '\u2655';
    }

    @Override
    public int setId ( ) {
        return 2;
    }

    @Override
    public boolean isValidMove ( int destinationCol, int destinationRow ) {

        if (this.isDead ()) {
            return false;
        }

        int deltaCol = Math.abs (destinationCol - this.getColumn ());
        int deltaRow = Math.abs (destinationRow - this.getRow ());

        // Sprawdź, czy ruch jest poziomy, pionowy lub po przekątnej
        if (deltaCol != deltaRow && deltaCol * deltaRow != 0) {
            return false;
        }

        int stepCol = (destinationCol > this.getColumn ()) ? 1 : (destinationCol < this.getColumn ()) ? -1 : 0;
        int stepRow = (destinationRow > this.getRow ()) ? 1 : (destinationRow < this.getRow ()) ? -1 : 0;

        int column = this.getColumn () + stepCol;
        int row = this.getRow () + stepRow;

        //Sprawdź czy coś blokuje drogę Królowej
        while (column != destinationCol && row != destinationRow) {
            if (Board.takenBy (row, column) != null) {
                return false; // napotkała przeszkodę
            }
            column += stepCol;
            row += stepRow;
        }

        // Sprawdź, czy docelowe pole jest wolne lub zawiera przeciwnika
        if (Board.takenBy (destinationRow, destinationCol) == null
                || Board.takenBy (destinationRow, destinationCol).getColor () != this.getColor ()) {
            return true;
        }


        return false;
    }
}

//Król

class King extends Piece {

    private static int castledRookID;

    public King ( int col, int row, Colors colors ) {
        super (col, row, colors);
        this.setName ("king");
    }

    @Override
    public char setUnicode ( ) {
        return this.getColor () == 'b' ? '\u265A' : '\u2654';
    }

    @Override
    public int setId ( ) {
        return 1;
    }

    @Override
    public boolean isValidMove ( int destinationCol, int destinationRow ) {

        if (this.isDead ()) {
            return false;
        }

        int deltaCol = Math.abs (destinationCol - this.getColumn ());
        int deltaRow = Math.abs (destinationRow - this.getRow ());

        //Sprawdzamy, czy występuje próba wykonania Roszady
        //Gracz musi wybrać pole na którym znajduje się wieża
        //   krótka roszada                         długa roszada
        if(deltaCol == 3 && deltaRow == 0  || deltaCol == 4 && deltaRow == 0) {
            if (canDoCastling(destinationCol)) {
                return true;
            }
        }

        //Sprawdzamy czy ruch odbywa się o jedno polę w każdą stronę
        if (deltaCol > 1 || deltaRow > 1) {
            return false;
        }
        //Sprawdzamy czy na polu znajduje się przeciwnik
        if (Board.takenBy (destinationRow, destinationCol) != null) {
            if (Board.takenBy (destinationRow, destinationCol).getColor () == this.getColor ()) {
                return false; //kolor jest taki sam ruch nieprawidłowy
            }
        }

        return true; //nie widzimy problemu z ruchem
    }

    public boolean canDoCastling ( int destinationCol ){


        // Sprawdzenie, czy król nie jest w szachu
        if (GameLogic.isKingInCheck(S27773Projekt02.pieces, this.getColor())) {
            return false;
        }


            int kingCol = this.getColumn();
            int rookCol = (destinationCol > kingCol) ? 7:0;
            int row = this.getRow();

            //odnajdujemy index wieży

            for (int i = 0; i < S27773Projekt02.pieces.size(); i++) {
                if (S27773Projekt02.pieces.get(i).getColumn() == rookCol
                        && S27773Projekt02.pieces.get(i).getRow() == row ) {
                    castledRookID = i;
                }
            }



            // Sprawdzenie, czy król i wieża nie poruszyły się wcześniej

            if (this.isMoved()|| S27773Projekt02.pieces.get(castledRookID).isMoved()) {
                return false;
            }



        return true;
    }

    @Override
    public void move(int desCol, int desRow) {

        if(canDoCastling(desCol) && isValidMove(desCol,desRow)) {

            int kingNewCol, rookNewCol;

            //sprawdzamy lokalizacje wieży
                if(desCol == 0) {
                    kingNewCol = 2;
                    rookNewCol = 3;
                }else {
                    kingNewCol = 6;
                    rookNewCol = 5;
                }
            //poruszamy króla dwa pola w kierunku wieży
            this.forceMove(kingNewCol, desRow);
             //poruszamy wieże na nowe pole
            S27773Projekt02.pieces.get(castledRookID).forceMove(rookNewCol,desRow);
            //Ustawiamy wartość boolenu odpowiadającego za sprawdzenie
            //czy bierka się ruszyła na true.
            this.setMovedAlready(true);
            S27773Projekt02.pieces.get(castledRookID).setMovedAlready(true);

        } else {
            super.move(desCol, desRow);
        }
    }
}

//Skoczek

class Knight extends Piece {


    public Knight ( int col, int row, Colors colors ) {
        super (col, row, colors);
        this.setName ("knight");
    }

    @Override
    public char setUnicode ( ) {
        return this.getColor () == 'w' ? '\u2658' : '\u265E';
    }

    @Override
    public int setId ( ) {
        return 5;
    }

    //x=col
    //y=row
    @Override
    public boolean isValidMove ( int destinationCol, int destinationRow ) {

        if (this.isDead ()) {
            return false;
        }


        int dCol = Math.abs (destinationCol - this.getColumn ());
        int dRow = Math.abs (destinationRow - this.getRow ());

        if ((dCol == 1 && dRow == 2) || (dCol == 2 && dRow == 1)) {
            //Sprawdzamy czy w docelowym polu znajduje się pionek i do którego gracza on należy
            if (Board.takenBy (destinationRow, destinationCol) == null
                    || Board.takenBy (destinationRow, destinationCol).getColor () != this.getColor ()) {
                //S27773Projekt02.killPiece (destinationRow,destinationCol);
                return true;
            }

        }
        return false;
    }
}

//Goniec

class Bishop extends Piece {


    public Bishop ( int col, int row, Colors colors ) {
        super (col, row, colors);
        this.setName ("bishop");
    }

    @Override
    public char setUnicode ( ) {
        return this.getColor () == 'b' ? '\u265D' : '\u2657';
    }

    @Override
    public int setId ( ) {
        return 4;
    }

    @Override
    public boolean isValidMove ( int destinationCol, int destinationRow ) {
        if (this.isDead ()) {
            return false;
        }


        int deltaCol = Math.abs (destinationCol - this.getColumn ());
        int deltaRow = Math.abs (destinationRow - this.getRow ());

        // Sprawdź, czy ruch jest po przekątnej
        if (deltaCol != deltaRow) {
            return false;
        }

        // Sprawdź, czy droga do celu jest wolna
        int stepCol = (destinationCol > this.getColumn ()) ? 1 : -1; //sprawdzamy czy ruch jest w "górę" lub dół
        int stepRow = (destinationRow > this.getRow ()) ? 1 : -1;

        int column = this.getColumn () + stepCol;
        int row = this.getRow () + stepRow;

        //Sprawdź czy coś blokuje drogę gońca
        while (column != destinationCol && row != destinationRow) {
            if (Board.takenBy (row, column) != null) {
                return false; //goniec napotkał przeszkodę
            }
            column += stepCol;
            row += stepRow;
        }

        // Sprawdź, czy docelowe pole jest wolne lub zawiera przeciwnika
        if (Board.takenBy (destinationRow, destinationCol) == null
                || Board.takenBy (destinationRow, destinationCol).getColor () != this.getColor ()) {
            return true;
        }


        return false;
    }
}


class PlayerInput {

    static int returnRow ( char number ) {
        int row; //number
        switch (number) {
            case '1' -> row = 0;
            case '2' -> row = 1;
            case '3' -> row = 2;
            case '4' -> row = 3;
            case '5' -> row = 4;
            case '6' -> row = 5;
            case '7' -> row = 6;
            case '8' -> row = 7;
            default -> row =  10;  //Nadaje wartość znajdującą się po za mapą przez co ruch będzie niepoprawny.
        }
        return row;
    }

    static int returnCol ( char character ) {
        int col; //char
        switch (toUpperCase(character)) {
            case 'A' -> col = 0;
            case 'B' -> col = 1;
            case 'C' -> col = 2;
            case 'D' -> col = 3;
            case 'E' -> col = 4;
            case 'F' -> col = 5;
            case 'G' -> col = 6;
            case 'H' -> col = 7;

            default -> col = 10; //Nadaje wartość znajdującą się po za mapą, przez co ruch będzie niepoprawny.

        }



        return col;
    }

    public static int ShowSelectedPieceIndex ( int startingCol, int startingRow ) {

        int i;


        for (i = 0; i < S27773Projekt02.pieces.size (); i++) {
            if (S27773Projekt02.pieces.get (i).getColumn () == startingCol
                    && S27773Projekt02.pieces.get (i).getRow () == startingRow) {
                return i;
            }
        }

        //System.out.println ("Bierka nie znaleziona");
        return i;

    }
}








