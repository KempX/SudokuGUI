import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage primaryStage;
    GridPane board = new GridPane();

    VBox menu = new VBox();
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Sudoku sudoku = new Sudoku(9);

        Presenter presenter = new Presenter(sudoku);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
        loader.setController(presenter);
        BorderPane root = loader.load();

        initScene(primaryStage, root);
        primaryStage.setTitle("Sudoku");
        primaryStage.show();
        this.primaryStage = primaryStage;
    }

    private void initScene(Stage primaryStage, BorderPane root){
        final int width = 460;
        final int height = 350;
        Scene scene = new Scene(root, width, height);
        primaryStage.setScene(scene);
    }
}