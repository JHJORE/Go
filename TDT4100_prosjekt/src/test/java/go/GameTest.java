package go;

import org.junit.jupiter.api.Test;

import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

public class GameTest {
    private Game game;
    private GoController controller;
    private Rectangle dummyRect;
    
	@BeforeEach
    public void setup() {
        controller = new GoController();
        game = new GoGame(controller);
        dummyRect = new Rectangle();
    }

    @Test
    @DisplayName("Test game constructors")
    public void testBasics() {
        Assertions.assertEquals(0, game.getScoreBlack());
        Assertions.assertEquals(0.5, game.getScoreWhite());
        Assertions.assertFalse(game.getGameEnded());
        ArrayList<Piece> pieces = new ArrayList<Piece>(
                Arrays.asList(new Piece(Player.WHITE, 1, 1), new Piece(Player.BLACK, 2, 2)));
        Assertions.assertEquals(2, pieces.size());
    }

    @Test
    @DisplayName("Test game move")
    public void testMove() {
        Assertions.assertEquals(0, game.getScoreBlack());
        Assertions.assertEquals(0.5, game.getScoreWhite());
        game.move(1, 1, dummyRect);
        Assertions.assertEquals(1, game.getScoreBlack());
        Assertions.assertFalse(game.getGameEnded());
        ArrayList<Piece> pieces = new ArrayList<Piece>(
                Arrays.asList(new Piece(Player.WHITE, 1, 1), new Piece(Player.BLACK, 2, 2)));
        Assertions.assertEquals(2, pieces.size());
        game.move(2, 1, dummyRect);
        Assertions.assertEquals(1.5, game.getScoreWhite());
        Assertions.assertFalse(game.getGameEnded());
    }

    @Test
    @DisplayName("Test pass")
    public void testPass() {
        Assertions.assertEquals(0, game.getScoreBlack());
        Assertions.assertEquals(0.5, game.getScoreWhite());
        game.pass();
        Assertions.assertEquals(0, game.getScoreBlack());
        Assertions.assertEquals(0.5, game.getScoreWhite());
        Assertions.assertFalse(game.getGameEnded());
        ArrayList<Piece> pieces = new ArrayList<Piece>(
                Arrays.asList(new Piece(Player.WHITE, 1, 1), new Piece(Player.BLACK, 2, 2)));
        Assertions.assertEquals(2, pieces.size());
        game.move(2, 2, dummyRect);
        game.move(3, 3, dummyRect);
        Assertions.assertEquals(1, game.getScoreBlack());
        Assertions.assertEquals(1.5, game.getScoreWhite());
        game.pass();
        Assertions.assertEquals(1, game.getScoreBlack());
        Assertions.assertEquals(1.5, game.getScoreWhite());
        game.move(3, 4, dummyRect);
        // end game
        game.pass();
        Assertions.assertFalse(game.getGameEnded());
        Assertions.assertEquals(2, game.getScoreBlack());
        Assertions.assertEquals(1.5, game.getScoreWhite());
        game.pass();
        Assertions.assertTrue(game.getGameEnded());
        Assertions.assertEquals(2, Math.round(game.getScoreBlack()));
        Assertions.assertEquals(1.5, game.getScoreWhite());
        Assertions.assertTrue(game.getScoreWhite() < game.getScoreBlack());
    }

    @Test
    @DisplayName("Test placePiece()")
    public void testPlacePiece(){
        game.placePiece(Player.BLACK, 1, 1, dummyRect);
        ArrayList<Piece> pieces = game.getPieces();
        Assertions.assertEquals(pieces.get(pieces.size()-1).getPlayer(), Player.BLACK);
        Assertions.assertEquals(pieces.get(pieces.size()-1).getX(), 1);
        Assertions.assertEquals(pieces.get(pieces.size()-1).getY(), 1);
        Assertions.assertEquals(pieces.get(pieces.size()-1).getRectangle(), dummyRect);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            game.placePiece(Player.BLACK, 1, 1, dummyRect);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            game.placePiece(Player.BLACK, -1, -11, dummyRect);
        });
        Assertions.assertEquals(0.5, game.getScoreWhite());
        Assertions.assertEquals(0, game.getScoreBlack());
        Assertions.assertFalse(game.getGameEnded());
    }

    @Test
    @DisplayName("Test emptyLastRemovedPiece()")
    public void testEmptyLastRemovedPiece(){
        game.placePiece(Player.BLACK, 1, 1, dummyRect);
        game.placePiece(Player.WHITE, 2, 2, dummyRect);
        game.emptyLastRemovedPiece();
        Assertions.assertEquals(0.5, game.getScoreWhite());
        Assertions.assertEquals(0, game.getScoreBlack());
        Assertions.assertFalse(game.getGameEnded());
    }

    @Test
    @DisplayName("Test placePiece")
    public void testplacepiece(){
        game.placePiece(Player.WHITE, 1 ,1 , new Rectangle());
        Assertions.assertEquals(1, game.getPieces().size());
        game.placePiece(Player.WHITE, 1 ,2 , new Rectangle());
        Assertions.assertEquals(2, game.getPieces().size());
        Assertions.assertFalse(game.getGameEnded());  
    }

    @Test
    @DisplayName("Test getScoreText")
    public void testGetScoreText(){
        Assertions.assertEquals("Turn:BlackScoreBlack:0ScoreWhite:0.5", game.getScoreText().replaceAll("\\s",""));
        game.move(2, 1, new Rectangle());
        Assertions.assertEquals("Turn:WhiteScoreBlack:1ScoreWhite:0.5", game.getScoreText().replaceAll("\\s",""));
        game.move(1, 1, new Rectangle());
        Assertions.assertEquals("Turn:BlackScoreBlack:1ScoreWhite:1.5", game.getScoreText().replaceAll("\\s",""));

        //Game ends
        game.pass();
        game.pass();
        Assertions.assertEquals("Thewinneriswhite.ScoreBlack:1ScoreWhite:1.5", game.getScoreText().replaceAll("\\s",""));
    }


    @Test
    @DisplayName("Test surrounded pieces")
    public void testSurroundedPieces() {
        game.move(2, 1, dummyRect);
        Assertions.assertEquals(1, game.getScoreBlack());
        Assertions.assertFalse(game.getGameEnded());
        game.move(2, 2, dummyRect);
        Assertions.assertEquals(1.5, game.getScoreWhite());
        Assertions.assertFalse(game.getGameEnded());
        game.move(2, 3, dummyRect);
        Assertions.assertEquals(2, game.getScoreBlack());
        Assertions.assertFalse(game.getGameEnded());
        game.move(2, 4, dummyRect);
        Assertions.assertEquals(2.5, game.getScoreWhite());
        Assertions.assertFalse(game.getGameEnded());
        game.move(1, 2, dummyRect);
        Assertions.assertEquals(3, game.getScoreBlack());
        Assertions.assertFalse(game.getGameEnded());
        game.move(2, 5, dummyRect);
        Assertions.assertEquals(3.5, game.getScoreWhite());
        Assertions.assertFalse(game.getGameEnded());
        game.move(1, 2, dummyRect);
        game.move(3, 2, dummyRect);
        Assertions.assertEquals(2.5, game.getScoreWhite());
        Assertions.assertEquals(4, game.getScoreBlack());

    }

    @Test
    @DisplayName("Test remove with corners")
    public void testCornerTaken() {
        // top left corner
        game.move(2, 1, dummyRect);
        Assertions.assertEquals(1, game.getScoreBlack());
        Assertions.assertFalse(game.getGameEnded());
        game.move(1, 1, dummyRect);
        Assertions.assertEquals(1.5, game.getScoreWhite());
        Assertions.assertFalse(game.getGameEnded());
        game.move(1, 2, dummyRect);
        Assertions.assertEquals(2, game.getScoreBlack());
        Assertions.assertEquals(0.5, game.getScoreWhite());
        Assertions.assertFalse(game.getGameEnded());

        // top right corner
        game.move(8, 1, dummyRect);
        Assertions.assertEquals(1.5, game.getScoreWhite());
        Assertions.assertFalse(game.getGameEnded());
        game.move(9, 1, dummyRect);
        Assertions.assertEquals(3, game.getScoreBlack());
        Assertions.assertFalse(game.getGameEnded());
        game.move(9, 2, dummyRect);
        Assertions.assertEquals(2, game.getScoreBlack());
        Assertions.assertEquals(2.5, game.getScoreWhite());
        Assertions.assertFalse(game.getGameEnded());

        // Bottom left corner
        game.move(1, 8, dummyRect);
        Assertions.assertEquals(3, game.getScoreBlack());
        Assertions.assertFalse(game.getGameEnded());
        game.move(1, 9, dummyRect);
        Assertions.assertEquals(3.5, game.getScoreWhite());
        Assertions.assertFalse(game.getGameEnded());
        game.move(2, 9, dummyRect);
        Assertions.assertEquals(4, game.getScoreBlack());
        Assertions.assertEquals(2.5, game.getScoreWhite());
        Assertions.assertFalse(game.getGameEnded());

        // bottom right corner
        game.move(8, 9, dummyRect);
        Assertions.assertEquals(3.5, game.getScoreWhite());
        Assertions.assertFalse(game.getGameEnded());
        game.move(9, 9, dummyRect);
        Assertions.assertEquals(5, game.getScoreBlack());
        Assertions.assertFalse(game.getGameEnded());
        game.move(9, 8, dummyRect);
        Assertions.assertEquals(4, game.getScoreBlack());
        Assertions.assertEquals(4.5, game.getScoreWhite());
        Assertions.assertFalse(game.getGameEnded());
    }

    @Test
    @DisplayName("Test taken walls")
    public void testTakenWalls() {
        // top
        game.move(4, 1, dummyRect);
        game.move(4, 2, dummyRect);
        game.move(5, 1, dummyRect);
        game.move(5, 2, dummyRect);
        game.move(4, 8, dummyRect);
        game.move(3, 1, dummyRect);
        game.move(5, 8, dummyRect);
        game.move(6, 1, dummyRect);
        Assertions.assertEquals(2, game.getScoreBlack());
        Assertions.assertEquals(4.5, game.getScoreWhite());
        Assertions.assertFalse(game.getGameEnded());

        // Try bottom, but cannot take your own piece
        game.move(3, 9, dummyRect);
        game.move(4, 9, dummyRect);
        game.move(6, 9, dummyRect);
        game.move(5, 9, dummyRect);
        Assertions.assertEquals(4, game.getScoreBlack());
        Assertions.assertEquals(5.5, game.getScoreWhite());
        Assertions.assertFalse(game.getGameEnded());
    }

    @Test
    @DisplayName("Test game move with ko")
    public void testMoveWithKo() {
        game.move(2, 1, dummyRect);
        Assertions.assertEquals(game.getScoreBlack(), 1);
        Assertions.assertFalse(game.getGameEnded());
        game.move(2, 2, dummyRect);
        Assertions.assertEquals(game.getScoreWhite(), 1.5);
        Assertions.assertFalse(game.getGameEnded());
        game.move(2, 3, dummyRect);
        Assertions.assertEquals(game.getScoreBlack(), 2);
        Assertions.assertFalse(game.getGameEnded());
        game.move(1, 3, dummyRect);
        Assertions.assertEquals(game.getScoreWhite(), 2.5);
        Assertions.assertFalse(game.getGameEnded());
        game.move(1, 2, dummyRect);
        Assertions.assertEquals(game.getScoreBlack(), 3);
        Assertions.assertFalse(game.getGameEnded());
        game.move(4, 2, dummyRect);
        Assertions.assertEquals(game.getScoreWhite(), 3.5);
        Assertions.assertFalse(game.getGameEnded());
        game.move(1, 1, dummyRect);
        Assertions.assertEquals(game.getScoreBlack(), 4);
        Assertions.assertFalse(game.getGameEnded());
        game.move(3, 3, dummyRect);
        Assertions.assertEquals(game.getScoreWhite(), 4.5);
        Assertions.assertFalse(game.getGameEnded());
        game.move(3, 2, dummyRect);
        Assertions.assertEquals(game.getScoreBlack(), 5);
        Assertions.assertEquals(game.getScoreWhite(), 3.5);
        game.move(2, 2, dummyRect);
        Assertions.assertEquals(5, game.getScoreBlack());
        Assertions.assertEquals(3.5, game.getScoreWhite());
    }


}