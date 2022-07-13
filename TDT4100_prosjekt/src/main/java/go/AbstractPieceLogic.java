package go;

import java.util.ArrayList;

import javafx.scene.shape.Rectangle;

public abstract class AbstractPieceLogic {
    protected ArrayList<Piece> pieces;      //i GoGame brukes denne for å ha oversikt over alle brikker noensinne plassert, mens i Board kun for aktive brikker
    
    public AbstractPieceLogic() {
        this.pieces = new ArrayList<Piece>();
    }

    public abstract ArrayList<Piece> getPieces();   //returnerer alle brikker

    public abstract void placePiece(Player player, int x, int y, Rectangle rect);   //plasserer en brikke i en rute

    protected boolean validSquare(int x, int y) {       //sjekker x og y er gyldig
        if (x <= 9 && x >= 1 && y <= 9 && y >= 1) {
            return true;
        }
        return false;
    }

    protected boolean emptyCell(int x, int y) { //sjekker om feltet er tomt
        if (!validSquare(x, y)) {
            return false;
        }
        return !pieces.stream().filter(p -> p != null).anyMatch((p) -> p.getX() == x && p.getY() == y);
    }
}
