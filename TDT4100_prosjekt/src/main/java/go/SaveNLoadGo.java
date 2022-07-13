package go;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

public class SaveNLoadGo implements SaveNLoad {
    public void save(String filename, Game game) throws IOException, IllegalArgumentException {
        if (!validStringName(filename)){
            throw new IllegalArgumentException("Invalid filename");
        }
        Files.createDirectories(getFileFolderPath());
        try (PrintWriter writer = new PrintWriter(getFilePath(filename).toFile())) {
            writer.println(game.getScoreBlack());
            writer.println(game.getScoreWhite());
            writer.println(game.getGameEnded());
            game.getPieces().forEach((x) -> writer.println(x.getPlayer() + ";" + x.getX() + ";" + x.getY()));//Rectangle til brukken blir ikke lagret ettersom det ikke gir mening når det konverteres til tekst fil. Vi kan rekonsturere rectangle når vi åpner filen igjen med den lagretet informasjon.
        }
        catch (Exception e){
            throw new IOException("Could not save file");
        }
    }

    private boolean validStringName(String name) {
        return name.matches("[a-zA-Z0-9]+");
    }

    public Game load(String filename, Game game, ObservableList<Node> rectangles) throws IOException {
        try {
            Scanner scanner = new Scanner(getFilePath(filename).toFile());
            game.setScoreBlack(scanner.nextLine());
            game.setScoreWhite(scanner.nextLine());
            game.setGameEnded(Boolean.parseBoolean(scanner.nextLine()));
            while (scanner.hasNextLine()) {
                String[] properties = scanner.nextLine().split(";");
                int x = Integer.parseInt(properties[1]);
                int y = Integer.parseInt(properties[2]);
                game.placePiece((properties[0].equals("WHITE") ? Player.WHITE : Player.BLACK),
                x, y,
                toRectangle(x, y, rectangles)); // Rekonsturerer Rectangle objektet til brikken
            }
        }
        catch (Exception e) {
            System.out.println(e);
            throw new IOException("Could not load file");
        }
        return game;
    }

    private Path getFilePath(String filename) {
        return getFileFolderPath().resolve(filename + ".txt");
    }

    private Path getFileFolderPath() {
        return Path.of(System.getProperty("user.home"), "prosjektfiles", "save");
    }

    private Rectangle toRectangle(int x, int y, ObservableList<Node> rectangles) {
        ArrayList<Node> nodes = new ArrayList<Node>(rectangles);
        try {
            return (Rectangle) nodes.stream()
                    .filter((i) -> ((Rectangle) i).getX() == (x * 100 - 100)
                            && ((Rectangle) i).getY() == (y * 100 - 100))
                    .toList().get(0); // denne kan skape problemer hvis x eller y ikke er riktige
        } catch (Exception e) {
            return null;
        }
    }

}
