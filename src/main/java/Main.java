import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Game game = new Game(9);
        StartValues startValues = new StartValues();
        game.loadStartValues(startValues.getStartValues());
        game.solve();

        Presenter presenter = new Presenter(game);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
        loader.setController(presenter);
        GridPane root = loader.load();
        initScene(primaryStage, root);
        primaryStage.setTitle("Sudoku");
        primaryStage.show();
    }

    private void initScene(Stage primaryStage, GridPane root){
        final int width = 300;
        final int height = 300;
        Scene scene = new Scene(root, width, height);
        primaryStage.setScene(scene);
    }
}