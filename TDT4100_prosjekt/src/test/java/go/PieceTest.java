package go;

import org.junit.jupiter.api.Test;

import javafx.scene.shape.Rectangle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

public class PieceTest {
    Piece piece;
    
    private PieceTest(){
        piece = new Piece(Player.WHITE, 1, 1);
    }

    @Test
    @DisplayName("Test clearRectangle()")
    public void testClearRectangle(){
        Rectangle rect = new Rectangle(1,2,3,4);
        piece.setRectangle(rect);
        Assertions.assertEquals(rect, piece.getRectangle());
        Assertions.assertEquals(1, piece.getRectangle().getX());
        Assertions.assertEquals(2, piece.getRectangle().getY());
        Assertions.assertEquals(3, piece.getRectangle().getWidth());
        Assertions.assertEquals(4, piece.getRectangle().getHeight());
        piece.clearRectangle();
        Assertions.assertEquals(null, piece.getRectangle());
    }
}
