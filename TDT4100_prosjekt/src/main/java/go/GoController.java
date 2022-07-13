package go;

import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class GoController {

    private SaveNLoad saveNLoad = new SaveNLoadGo();

    private Game game;

    @FXML
    private TextArea scoreText;

    @FXML
    private Pane background;

    @FXML
    private void initialize() { // Lager et 9X9 rutenett som brettet til spillet
        for (int x = 0; x < 900; x += 100) {
            for (int y = 0; y < 900; y += 100) {
                Rectangle rect = new Rectangle(x, y, 100, 100);
                rect.setFill(Color.LIGHTSLATEGREY);
                rect.setStroke(Color.BLACK);
                rect.setOnMouseClicked(event -> clicked(event, rect));

                background.getChildren().add(rect);
            }
        }
        scoreText.setEditable(false);
        this.game = new GoGame(this);
        updateScoreText();
    }

    @FXML
    public void piecePlaced(Piece piece, Rectangle rect) {      //kjører når en brikke plasseres, endrer det grafiske
        piece.setRectangle(rect);
        rect.setFill(piece.getPlayer() == Player.WHITE ? Color.WHITE : Color.BLACK);
    }

    @FXML
    public void removePiece(Piece piece) {  //kjører når en brikke fjernes, endrer det grafiske
        piece.getRectangle().setFill(Color.LIGHTSLATEGREY);
        piece.clearRectangle();
        updateScoreText();
    }

    @FXML
    private void handlePass() {     //kjører når spilleren trykker på "Pass"
        game.pass();
        updateScoreText();
    }

    @FXML
    private void handleReset() {    //kjører når spilleren trykker på "Reset"
        this.game = new GoGame(this);
        ArrayList<Node> nodes = new ArrayList<Node>(background.getChildren());
        nodes.forEach((x) -> ((Rectangle) x).setFill(Color.LIGHTSLATEGRAY));
        updateScoreText();
    }

    private void clicked(MouseEvent event, Rectangle rect) {    //kjøres når brukeren trykker på et rektangel
        int x = (int) rect.getX() / 100 + 1;
        int y = (int) rect.getY() / 100 + 1;
        game.move(x, y, rect);
        updateScoreText();
    }

    private void updateScoreText() {    //oppdaterer scoreText
        try{
            scoreText.setText(game.getScoreText());
        } catch (NullPointerException e) {
        }
    }

    @FXML
    private void saveGame() {   //kjører når spilleren trykker på "Save"
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Save Game");
        dialog.setHeaderText("Save your game, so you can play again later");
        dialog.setContentText("Game:");

        String gameName = dialog.showAndWait().get();
        try { 
            saveNLoad.save(gameName, game);
        } catch (IOException e) {
            System.out.println("Your game could not be saved to file");
        } catch (IllegalArgumentException e) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("String can only contain letters and numbers");
            errorAlert.showAndWait();   
        }
    }

    @FXML
    private void loadGame() {   //kjører når spilleren trykker på "Load"
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Load Game");
        dialog.setHeaderText("Write which game you want to open");
        dialog.setContentText("Game:");

        Game tempgGame = game;
        try {
            String gameName = dialog.showAndWait().get();
            handleReset();
            saveNLoad.load(gameName, game, background.getChildren());
        } catch (IOException e) {
            game = tempgGame;
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setHeaderText("File not found");
            errorAlert.setContentText("Could not find file, make sure you typed in correct file name");
            errorAlert.showAndWait();
        }
        updateScoreText();
        fillsquares();
    }

    @FXML
    private void fillsquares() {    //fyller alle rektangler som har brikker
        game.getPieces()
                .forEach((p) -> p.getRectangle().setFill(p.getPlayer() == Player.WHITE ? Color.WHITE : Color.BLACK));
    }
}
