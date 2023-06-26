import Board.BoardLogic;
import Board.BoardVisual;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import java.util.Scanner;

import static Board.BoardLogic.pieces;
import static java.lang.System.exit;

public class Main {

    public static void main ( String[] args ) {

        System.setOut (
                new PrintStream(
                        new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));


        Scanner scanner = new Scanner (System.in);
        BoardLogic boardLogic = new BoardLogic();
        BoardVisual boardVisual = new BoardVisual(boardLogic);

        System.out.println("""
                Welcome to Console Chess Implementation in Java
                @author Oskar Kalbarczyk
                Visit my gitHub at: https://github.com/oskalbarczyk
                Or my youtube channel: https://www.youtube.com/@codewithoskar
                """);

        System.out.println ("Type \"1\" to start new game!");
        int choice = scanner.nextInt ();


        if(choice == 1){
            boardLogic.startingBoard ();
        }else {
            exit(0);
        }





        int inGameChoice;

        boolean gameOngoing = true; // gra w trakcie
        boolean isWhiteTurn = true; // ruch gracza białych bierek
        boolean drawOffered = false; // propozycja remisu



        while (gameOngoing) {
            boardVisual.printBoard ();
            System.out.println ("Turn of: " + (isWhiteTurn ? "White Player" : "Black Player"));
            System.out.println ("Choose option:");
            System.out.println ("1. Move Piece");
            //System.out.println ("2. Zapisz i wyjdź z gry");
            System.out.println ("2. Offer a draw/Take a draw");
            System.out.println ("3. Surrender");

            inGameChoice = scanner.nextInt ();


            switch (inGameChoice) {
                case 1 -> {
                    boolean moveSuccessful = false;
                    while (!moveSuccessful) {

                        try {

                            System.out.print("Pick a piece (example E4): ");
                            Scanner inputScan = new Scanner(System.in);

                            String inputChoosePiece = inputScan.nextLine();
                            char startCharacter = inputChoosePiece.charAt(0);
                            char startNumber = inputChoosePiece.charAt(1);


                            int startingCol = PlayerInput.returnCol(startCharacter);
                            int startingRow = PlayerInput.returnRow(startNumber);

                            int pieceIndex = PlayerInput.ShowSelectedPieceIndex(startingCol, startingRow);

                            boolean correctColor = false;


                            if (isWhiteTurn && pieces.get(pieceIndex).getColor() == 'w') {
                                correctColor = true;
                            } else if (!isWhiteTurn && pieces.get(pieceIndex).getColor() == 'b') {
                                correctColor = true;
                            }


                            if (correctColor) {
                                System.out.print("Select a target field (example E2): ");

                                String inputDestination = inputScan.nextLine();

                                char destCharacter = inputDestination.charAt(0);
                                char destNumber = inputDestination.charAt(1);

                                int destRow = PlayerInput.returnRow(destNumber);
                                int destCol = PlayerInput.returnCol(destCharacter);

                                try {
                                    if (pieces.get(pieceIndex).isValidMove(destCol, destRow)) {
                                        boolean moveCreatesCheckmate;
                                        int indexOfPieceKilledDuringTest = -1;
                                        int killedPieceRow = -1;
                                        int killedPieceCol = -1;
                                        int ourPieceRow = pieces.get(pieceIndex).getRow();
                                        int ourPieceCol = pieces.get(pieceIndex).getColumn();
                                        boolean weKilledSomeone = false;
                                        if (boardLogic.getPiece(destRow, destCol) != null) {
                                            for (int i = 0; i < pieces.size(); i++) {
                                                if (pieces.get(i).getColumn() == destCol
                                                        && pieces.get(i).getRow() == destRow) {
                                                    indexOfPieceKilledDuringTest = i;
                                                    killedPieceCol =
                                                            pieces.get(indexOfPieceKilledDuringTest).getColumn();
                                                    killedPieceRow =
                                                            pieces.get(indexOfPieceKilledDuringTest).getRow();
                                                    pieces.get(indexOfPieceKilledDuringTest)
                                                            .forceMove(8, 8);
                                                    weKilledSomeone = true;
                                                }
                                            }
                                        }
                                        pieces.get(pieceIndex).forceMove(destCol, destRow);
                                        moveCreatesCheckmate = boardLogic.isCheckmate(pieces, isWhiteTurn ? 'w' : 'b');
                                        pieces.get(pieceIndex).forceMove(ourPieceCol, ourPieceRow);
                                        if (weKilledSomeone) {
                                            pieces.get(indexOfPieceKilledDuringTest).forceMove(killedPieceCol
                                                    , killedPieceRow);
                                        }

                                        if (!moveCreatesCheckmate) {
                                            pieces.get(pieceIndex).move(destCol, destRow);
                                            moveSuccessful = true;
                                        } else {
                                            System.out.println("Your king is in check or will be after this move.");
                                        }


                                    } else {
                                        System.out.println("Invalid move, try again.");
                                    }
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    System.out.println("Piece not found Incorrect move");
                                }
                            } else {
                                System.out.println("Invalid move, try again.");
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                            System.out.println("Piece not found Incorrect move");
                        }

                    }
                    isWhiteTurn = !isWhiteTurn;
                    if (boardLogic.isCheckmate(pieces, isWhiteTurn ? 'w' : 'b') && !boardLogic.canSaveCheckmate()) {
                        System.out.println("Check Mate! " + (isWhiteTurn ? "Black" : "White") + "Player wins!");
                        gameOngoing = false;
                    } else if (boardLogic.isKingInCheck(pieces, isWhiteTurn ? 'w' : 'b')) {
                        System.out.println("The " + (isWhiteTurn ? "White" : "Black") + "King is in check!");

                    }
                }
                case 2 -> {
                    if(!drawOffered){
                        drawOffered = true;
                        System.out.println("A draw was offered. Waiting for the other player's decision.");
                        isWhiteTurn = !isWhiteTurn;
                    }else {
                        System.out.println("Draw accepted. The game ended in a draw!.");
                        gameOngoing = false;
                    }
                }
                case 3 -> {
                    System.out.println("The " + (isWhiteTurn ? "White" : "Black")
                            + "Player Gave up. End of the game...");
                    gameOngoing = false;
                }
                default -> System.out.println("Invalid selection. Please try again.");
            }

            if (gameOngoing) {
                drawOffered = false;
            }
        }

        scanner.close ();
    }

}