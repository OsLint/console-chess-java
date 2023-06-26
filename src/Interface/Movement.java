package Interface;

public interface Movement {

    boolean isValidMove(int desCol, int desRow);

    void move(int desCol, int desRow);

    void forceMove(int desCol, int desRow);


}