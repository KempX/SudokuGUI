import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class Presenter implements Initializable {
    private final Sudoku sudoku;

    @FXML private VBox menu;
    @FXML private Button solve;
    @FXML private Button save;
    @FXML private Button open;
    @FXML private Button print;
    @FXML private Button reset;
    @FXML private GridPane board;
    @FXML private Label status;

    public Presenter(Sudoku sudoku){
        this.sudoku = sudoku;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        solve.setOnAction(this::solveButtonHandler);
        save.setOnAction(this::saveButtonHandler);
        open.setOnAction(this::openButtonHandler);
        print.setOnAction(this::printButtonHandler);
        reset.setOnAction(this::resetButtonHandler);

        sudoku.getStatus().addListener((observable, oldValue, newValue) -> status.setText(newValue));

        Image img = new Image("file:background.png");
        ImageView imageView = new ImageView(img);

        for(int i = 0; i < sudoku.getSize(); i++){
            for (int j = 0; j < sudoku.getSize(); j++){
                TextField textField = new TextField("0");
                textField.setStyle("-fx-text-fill: white; -fx-pref-width: 2em; -fx-background-color: white; ");
                textField.setOnMouseClicked(e -> textField.selectAll());
                board.setConstraints(textField, i, j);
                board.setHgap(4);
                board.setVgap(4);
                board.getChildren().add(textField);

                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    Integer columnIndex = GridPane.getColumnIndex(textField);
                    Integer rowIndex = GridPane.getRowIndex(textField);

                    if (newValue.matches("\\d")){
                        sudoku.setFieldValue(columnIndex, rowIndex, Integer.parseInt(newValue));
                    } else {
                        sudoku.setFieldValue(columnIndex, rowIndex, 0);
                        textField.setText("0");
                    }
                });

                sudoku.getGrid()[i][j].getProperty().addListener((observable, oldValue, newValue) -> {
                    textField.textProperty().setValue(String.valueOf(newValue));

                    if (newValue.intValue() == 0){
                        textField.setStyle("-fx-text-fill: white; -fx-pref-width: 2em; -fx-background-color: white; ");
                    } else {
                        textField.setStyle("-fx-text-fill: black; -fx-pref-width: 2em; -fx-background-color: white; ");
                    }
                });
            }
        }
    }

    public void solveButtonHandler(ActionEvent actionEvent){
        sudoku.solve();
    }

    public void printButtonHandler(ActionEvent actionEvent){
        sudoku.printTable("Das aktuelle Sudoku: ", sudoku.getGrid());
    }

    public void resetButtonHandler(ActionEvent actionEvent){
        sudoku.reset();
        sudoku.setStatus("Sudoku zurückgesetzt. ");
    }

    public void saveButtonHandler(ActionEvent actionEvent){
        Stage stage = Stage.class.cast(Control.class.cast(actionEvent.getSource()).getScene().getWindow());
        FileChooser fileChooser = new FileChooser();
        String description = "Text-Datei (*.txt)";
        String extensions = "*.txt";
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extensions);
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                sudoku.getFileIO().saveValues(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openButtonHandler (ActionEvent actionEvent){
        Stage stage = Stage.class.cast(Control.class.cast(actionEvent.getSource()).getScene().getWindow());
        FileChooser fileChooser = new FileChooser();
        String description = "Text-Datei (*.txt)";
        String extensions = "*.txt";
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extensions);
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                sudoku.getFileIO().openValues(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}