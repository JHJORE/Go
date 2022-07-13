package go;

import javafx.scene.shape.Rectangle;

public class Piece {        //inneholder informasjon om en brikke, hvilken spiller som har brikken, posisjonenen dens og hvilket rektangel den hører til

    private Player player;
    private int x;
    private int y;
    private Rectangle rect;

    public Piece(Player player, int x, int y) {
        this.player = player;
        this.x = x;
        this.y = y;
    }

    public void setRectangle(Rectangle rect) {
        this.rect = rect;
    }

    public void clearRectangle() {
        this.rect = null;
    }

    public Rectangle getRectangle() {
        return rect;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Player getPlayer() {
        return player;
    }
}
