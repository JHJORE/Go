package go;

import org.junit.jupiter.api.Test;

import javafx.scene.shape.Rectangle;

import static org.junit.jupiter.api.Assertions.assertThrows;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

public class SaveNLoadTest {
    private GoController controller;
    private Game game;
    private SaveNLoad saveNLoad;

    @BeforeEach
    public void setup() {
        controller = new GoController();
        saveNLoad = new SaveNLoadGo();
        game = new GoGame(controller);
    }

    @Test
    @DisplayName("Test save()")
    public void testSave() throws IOException {
        game.move(1, 1, new Rectangle());
        Assertions.assertEquals(1, game.getScoreBlack());
        Assertions.assertEquals(0.5, game.getScoreWhite());
        saveNLoad.save("testSave", game);// regualer save
        Path file = Path.of(System.getProperty("user.home"), "prosjektfiles", "save", "testSave.txt");
        // sjekker at filen finnes i filsystemet

        game.move(1, 2, new Rectangle());
        game.move(1, 3, new Rectangle());
        game.move(1, 4, new Rectangle());
        assertThrows(IllegalArgumentException.class, () -> {
			saveNLoad.save("./12", this.game);//test if the file name is illegal
            });
        }
        
    
    @Test
    @DisplayName("Test load()")
    public void testLoad() throws IOException {
        Assertions.assertEquals(0, game.getScoreBlack());
        Assertions.assertEquals(0.5, game.getScoreWhite());
        game.move(1, 2, new Rectangle());
        Assertions.assertEquals(1, game.getScoreBlack());
        Assertions.assertEquals(0.5, game.getScoreWhite());
        saveNLoad.save("testSave", game);

        game.move(1, 3, new Rectangle());
        Assertions.assertEquals(1, game.getScoreBlack());
        Assertions.assertEquals(1.5, game.getScoreWhite());
        game = new GoGame(controller);

        ObservableList<Node> rectangles = FXCollections.observableArrayList(Arrays.asList(new Rectangle(0, 100, 100, 100), new Rectangle(0, 200, 100, 100)));
        assertThrows(IOException.class, () -> {
            saveNLoad.load("dontexcist", this.game, rectangles);
        });
        assertThrows(IOException.class, () -> {
            saveNLoad.load(".&12¨", this.game, rectangles);
        });
        saveNLoad.load("testSave", game, rectangles);
        Assertions.assertEquals(1, game.getScoreBlack());
        Assertions.assertEquals(0.5, game.getScoreWhite());
        Assertions.assertEquals(Player.BLACK, game.getPieces().get(0).getPlayer());
        Assertions.assertEquals(1, game.getPieces().get(0).getX());
        Assertions.assertEquals(2, game.getPieces().get(0).getY());
        Assertions.assertEquals(1, game.getPieces().size());
        game.move(1, 3, new Rectangle());
        

        
    }
}
