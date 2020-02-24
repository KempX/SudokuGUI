import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    private Stage primaryStage;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Sudoku sudoku = new Sudoku(9);
        //sudoku.loadStartValues(startValues.getStartValues());
        //sudoku.solve();

        Presenter presenter = new Presenter(sudoku);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view.fxml"));
        loader.setController(presenter);
        GridPane root = loader.load();
        initScene(primaryStage, root);
        primaryStage.setTitle("Sudoku");
        primaryStage.show();
        this.primaryStage = primaryStage;
    }

    private void initScene(Stage primaryStage, GridPane root){
        final int width = 600;
        final int height = 400;
        Scene scene = new Scene(root, width, height);
        primaryStage.setScene(scene);
    }

    public Stage getPrimaryStage(){
        return primaryStage;
    }
}