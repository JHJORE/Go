package go;

import java.util.ArrayList;

import javafx.scene.shape.Rectangle;

public interface Game {

    public void move(int x, int y, Rectangle rect);

    public void pass();

    public void placePiece(Player player, int x, int y, Rectangle rect);

    public void emptyLastRemovedPiece();

    public ArrayList<Piece> getPieces();

    public String getScoreText();

    public double getScoreBlack();

    public double getScoreWhite();

    public boolean getGameEnded();
    
    public void setGameEnded(boolean parseBoolean);

    public void setScoreBlack(String score);

    public void setScoreWhite(String score);
}