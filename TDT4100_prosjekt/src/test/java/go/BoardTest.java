package go;

import org.junit.jupiter.api.Test;

import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

public class BoardTest {
    private Board board;
    private Rectangle dummyRect;  

    private BoardTest(){
        dummyRect = new Rectangle();
    }
    
	@BeforeEach
	private void setup() {
		board = new Board();
	}

    @Test
    @DisplayName("Test board constructors")
    public void testBasics() {
        Assertions.assertEquals(0, board.getPieces().size());
        Assertions.assertTrue(board.getPieces().isEmpty());
        ArrayList<Piece> pieces = new ArrayList<Piece>(
                Arrays.asList(new Piece(Player.WHITE, 1, 1), new Piece(Player.BLACK, 2, 2)));
        board = new Board(pieces);
        Assertions.assertEquals(pieces, board.getPieces());
    }

    @Test
    @DisplayName("Test placePiece()")
    public void testPlacePiece() {
        Piece p1 = new Piece(Player.WHITE, 1, 1);
        Piece p2 = new Piece(Player.BLACK, 2, 2);
        ArrayList<Piece> pieces = new ArrayList<Piece>(Arrays.asList(p1, p2));
        board = new Board(pieces);
        board.placePiece(Player.WHITE, 3, 3, dummyRect);
        Assertions.assertEquals(3, board.getPieces().size());
        Assertions.assertEquals(3, board.getPieces().get(2).getX());
        Assertions.assertEquals(3, board.getPieces().get(2).getY());
        board.placePiece(Player.BLACK, 4, 4, dummyRect);
        Assertions.assertEquals(4, board.getPieces().size());
        Assertions.assertEquals(4, board.getPieces().get(3).getX());
        Assertions.assertEquals(4, board.getPieces().get(3).getY());
        Assertions.assertEquals(dummyRect, board.getPieces().get(3).getRectangle());
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.placePiece(Player.BLACK, 1, 1, dummyRect));
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.placePiece(Player.WHITE, 1, 1, dummyRect));
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.placePiece(Player.BLACK, 0, 1, dummyRect));
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.placePiece(Player.BLACK, -198, 99, dummyRect));
    }

    @Test
    @DisplayName("Test emptyCell()")
    public void testEmptyCell() {
        Assertions.assertTrue(board.emptyCell(1, 1));
        board.placePiece(Player.WHITE, 1, 1, dummyRect);
        Assertions.assertFalse(board.emptyCell(1, 1));
        Assertions.assertFalse(board.emptyCell(0, 0));
        Assertions.assertFalse(board.emptyCell(-1, 2));
    }

    @Test
    @DisplayName("Test takenPieces() for none taken and empty board")
    public void testTakenPiecesEmpty() {
        Assertions.assertEquals(0, board.takenPieces(Player.WHITE).size());
        Assertions.assertEquals(0, board.takenPieces(Player.BLACK).size());
        board.placePiece(Player.BLACK, 2, 1, dummyRect);
        board.placePiece(Player.WHITE, 2, 2, dummyRect);
        board.placePiece(Player.BLACK, 3, 2, dummyRect);
        Assertions.assertEquals(0, board.takenPieces(Player.WHITE).size());
        Assertions.assertEquals(0, board.takenPieces(Player.BLACK).size());
    }

    private void setupSingle(){
        board.placePiece(Player.BLACK, 2, 1, dummyRect);
        board.placePiece(Player.BLACK, 2, 3, dummyRect);
        board.placePiece(Player.BLACK, 1, 2, dummyRect);
        board.placePiece(Player.WHITE, 2, 2, dummyRect);
        board.placePiece(Player.BLACK, 3, 2, dummyRect);
    }

    @Test
    @DisplayName("Test takenPieces() for single")
    public void testTakenPiecesSingle() {
        setupSingle();
        Assertions.assertEquals(0, board.takenPieces(Player.WHITE).size());
        Assertions.assertEquals(1, board.takenPieces(Player.BLACK).size());
        Assertions.assertEquals(Player.WHITE, board.takenPieces(Player.BLACK).get(0).getPlayer());
        Assertions.assertEquals(2, board.takenPieces(Player.BLACK).get(0).getX());
        Assertions.assertEquals(2, board.takenPieces(Player.BLACK).get(0).getY());
    }

    private void setupSingleCorner(){
        board.placePiece(Player.BLACK, 2, 1, dummyRect);
        board.placePiece(Player.WHITE, 1, 1, dummyRect);
        board.placePiece(Player.BLACK, 1, 2, dummyRect);
    }

    @Test
    @DisplayName("Test takenPieces() for single corner")
    public void testTakenPiecesSingleCorner() {
        setupSingleCorner();
        Assertions.assertEquals(1, board.takenPieces(Player.BLACK).size());
        Assertions.assertEquals(0, board.takenPieces(Player.WHITE).size());
        Assertions.assertEquals(Player.WHITE, board.takenPieces(Player.BLACK).get(0).getPlayer());
        Assertions.assertEquals(1, board.takenPieces(Player.BLACK).get(0).getX());
        Assertions.assertEquals(1, board.takenPieces(Player.BLACK).get(0).getY());
    }

    private void setupMultiple(){
        board.placePiece(Player.BLACK, 7, 2, dummyRect);
        board.placePiece(Player.WHITE, 6, 2, dummyRect);
        board.placePiece(Player.BLACK, 8, 2, dummyRect);
        board.placePiece(Player.WHITE, 6, 3, dummyRect);
        board.placePiece(Player.BLACK, 7, 3, dummyRect);
        board.placePiece(Player.WHITE, 7, 4, dummyRect);
        board.placePiece(Player.BLACK, 8, 3, dummyRect);
        board.placePiece(Player.WHITE, 8, 4, dummyRect);
        board.placePiece(Player.WHITE, 7, 1, dummyRect);
        board.placePiece(Player.WHITE, 8, 1, dummyRect);
        board.placePiece(Player.WHITE, 9, 2, dummyRect);
        board.placePiece(Player.WHITE, 9, 3, dummyRect);
    }

    @Test
    @DisplayName("Test takenPieces() for multiple")
    public void testTakenPiecesMultiple() {
        setupMultiple();
        Assertions.assertEquals(0, board.takenPieces(Player.BLACK).size());
        Assertions.assertEquals(4, board.takenPieces(Player.WHITE).size());
        Assertions.assertEquals(Player.BLACK, board.takenPieces(Player.WHITE).get(0).getPlayer());
        Assertions.assertEquals(Player.BLACK, board.takenPieces(Player.WHITE).get(1).getPlayer());
        Assertions.assertEquals(Player.BLACK, board.takenPieces(Player.WHITE).get(2).getPlayer());
        Assertions.assertEquals(Player.BLACK, board.takenPieces(Player.WHITE).get(3).getPlayer());
        Assertions.assertTrue(board.takenPieces(Player.WHITE).stream().anyMatch(p -> p.getX() == 7 && p.getY() == 2));
        Assertions.assertTrue(board.takenPieces(Player.WHITE).stream().anyMatch(p -> p.getX() == 8 && p.getY() == 2));
        Assertions.assertTrue(board.takenPieces(Player.WHITE).stream().anyMatch(p -> p.getX() == 7 && p.getY() == 3));
        Assertions.assertTrue(board.takenPieces(Player.WHITE).stream().anyMatch(p -> p.getX() == 8 && p.getY() == 3));
    }

    private void setupMultipleCorner(){
        board.placePiece(Player.BLACK, 8, 1, dummyRect);
        board.placePiece(Player.WHITE, 7, 1, dummyRect);
        board.placePiece(Player.BLACK, 9, 1, dummyRect);
        board.placePiece(Player.WHITE, 7, 2, dummyRect);
        board.placePiece(Player.BLACK, 8, 2, dummyRect);
        board.placePiece(Player.WHITE, 8, 3, dummyRect);
        board.placePiece(Player.BLACK, 9, 2, dummyRect);
        board.placePiece(Player.WHITE, 9, 3, dummyRect);
    }

    @Test
    @DisplayName("Test takenPieces() for multiple corner")
    public void testTakenPiecesMultipleCorner() {
        setupMultipleCorner();
        Assertions.assertEquals(0, board.takenPieces(Player.BLACK).size());
        Assertions.assertEquals(4, board.takenPieces(Player.WHITE).size());
        Assertions.assertEquals(Player.BLACK, board.takenPieces(Player.WHITE).get(0).getPlayer());
        Assertions.assertEquals(Player.BLACK, board.takenPieces(Player.WHITE).get(1).getPlayer());
        Assertions.assertEquals(Player.BLACK, board.takenPieces(Player.WHITE).get(2).getPlayer());
        Assertions.assertEquals(Player.BLACK, board.takenPieces(Player.WHITE).get(3).getPlayer());
        Assertions.assertTrue(board.takenPieces(Player.WHITE).stream().anyMatch(p -> p.getX() == 8 && p.getY() == 1));
        Assertions.assertTrue(board.takenPieces(Player.WHITE).stream().anyMatch(p -> p.getX() == 9 && p.getY() == 1));
        Assertions.assertTrue(board.takenPieces(Player.WHITE).stream().anyMatch(p -> p.getX() == 8 && p.getY() == 2));
        Assertions.assertTrue(board.takenPieces(Player.WHITE).stream().anyMatch(p -> p.getX() == 9 && p.getY() == 2));
    }

    @Test
    @DisplayName("Test calculateArea() for empty board and no taken areas")
    public void testCalculateAreaEmpty() {
        Assertions.assertEquals(0, board.calculateArea(0,0));
        board.placePiece(Player.BLACK, 2, 1, dummyRect);
        board.placePiece(Player.WHITE, 2, 2, dummyRect);
        board.placePiece(Player.BLACK, 3, 2, dummyRect);
        Assertions.assertEquals(0, board.calculateArea(0,0));
    }
    
    @Test
    @DisplayName("Test calculateArea() for single")
    public void testCalculateAreaSingle() {
        setupSingle();
        board.getPieces().remove(3);
        Assertions.assertEquals(7700, board.calculateArea(0,0));
        board.placePiece(Player.WHITE, 5, 5, dummyRect);
        Assertions.assertEquals(200, board.calculateArea(0,0));
    }

    @Test
    @DisplayName("Test calculateArea() for single corner")
    public void testCalculateAreaSingleCorner() {
        setupSingleCorner();
        Assertions.assertEquals(7800, board.calculateArea(0,0));
        board.getPieces().remove(1);
        Assertions.assertEquals(7900, board.calculateArea(0,0));
        board.placePiece(Player.WHITE, 5, 5, dummyRect);
        Assertions.assertEquals(100, board.calculateArea(0,0));
    }

    @Test
    @DisplayName("Test calculateArea() for multiple")
    public void testCalculateAreaMultiple() {
        setupMultiple();
        Assertions.assertEquals(69, board.calculateArea(0,0));
        board.getPieces().remove(6);
        board.getPieces().remove(4);
        board.getPieces().remove(2);
        board.getPieces().remove(0);
        Assertions.assertEquals(73, board.calculateArea(0,0));
        board.placePiece(Player.BLACK, 5, 5, dummyRect);
        Assertions.assertEquals(5, board.calculateArea(0,0));
    }

    @Test
    @DisplayName("Test calculateArea() for multiple corner")
    public void testCalculateAreaMultipleCorner() {
        setupMultipleCorner();
        Assertions.assertEquals(73, board.calculateArea(0,0));
        board.getPieces().remove(6);
        board.getPieces().remove(4);
        board.getPieces().remove(2);
        board.getPieces().remove(0);
        Assertions.assertEquals(77, board.calculateArea(0,0));
        board.placePiece(Player.BLACK, 5, 5, dummyRect);
    }

}

