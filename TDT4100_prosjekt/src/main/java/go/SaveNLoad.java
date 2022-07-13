package go;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.scene.Node;

public interface SaveNLoad {
    public void save(String filename, Game game) throws IOException, IllegalArgumentException;

    public Game load(String filename, Game game, ObservableList<Node> rectangles) throws IOException;
}
