import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage primaryStage;
    GridPane board = new GridPane();
    //  root.getChildren().add(board);

    VBox menu = new VBox();
    //    root.getChildren().add(menu);
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




   /*     GridPane board = new GridPane();
        for (int blockColumn = 0; blockColumn < 3 ; blockColumn++) {
            for (int blockRow = 0; blockRow < 3; blockRow++) {

                GridPane box = new GridPane();
                box.setStyle("-fx-background-color: black, -fx-control-inner-background; -fx-background-insets: 0, 2; -fx-padding: 2;");
                for (int column = 0; column < 3; column++) {
                    for (int row = 0 ; row < 3; row++) {
                        TextField textField = new TextField("0");
                        textField.setStyle("-fx-pref-width: 2em;");
                        GridPane.setConstraints(textField, column, row);
                        box.getChildren().add(textField);
                    }
                }

                GridPane.setConstraints(box, blockColumn, blockRow);
                board.getChildren().add(box);

            }
        }*/



        initScene(primaryStage, root);
        primaryStage.setTitle("Sudoku");
        primaryStage.show();
        this.primaryStage = primaryStage;
    }

    private void initScene(Stage primaryStage, BorderPane root){
        final int width = 450;
        final int height = 350;
        Scene scene = new Scene(root, width, height);
        primaryStage.setScene(scene);
    }
}