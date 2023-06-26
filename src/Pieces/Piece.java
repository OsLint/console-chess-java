package Pieces;

import Interface.BoardLink;
import Interface.Movement;

public abstract class Piece implements Movement {
    private String name;
    private final char unicode;
    private int column;
    private int row;
    private final char color;
    private boolean isDead;
    private boolean moved = false;
    private final BoardLink boardLink;


    public Piece(int col, int row, Colors colors,BoardLink boardLink) {
        this.column = col;
        this.row = row;
        switch (colors) {
            case w -> this.color = 'w';
            case b -> this.color = 'b';
            default -> throw new IllegalArgumentException("Wrong color.");
        }
        this.unicode = setUnicode();
        this.boardLink = boardLink;
    }

    @Override
    public void move(int desCol, int desRow) {
        setColumn(desCol);
        setRow(desRow);
    }

    @Override
    public void forceMove(int desCol, int desRow) {
        this.column = desCol;
        this.row = desRow;
    }

    @Override
    public boolean isValidMove(int destinationCol, int destinationRow) {
        return false;
    }


    public void setColumn(int column) {
        this.column = column;
    }

    public void setRow(int row) {
        this.row = row;
    }
    public void setMovedAlready(boolean moved) {
        this.moved = moved;
    }
    public void setDead(boolean dead) {
        isDead = dead;
    }
    public char setUnicode() {
        return ' ';
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
    public boolean isDead() {
        return isDead;
    }
    public char getUnicode() {
        return unicode;
    }
    public boolean isMoved() {
        return moved;
    }

    public char getColor() {
        return color;
    }
    public BoardLink getBoardLink() {
        return boardLink;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Pieces.Piece{" +
                "name='" + name + '\'' +
                ", unicode=" + unicode +
                ", column=" + column +
                ", row=" + row +
                ", color=" + color +
                '}';
    }

}


