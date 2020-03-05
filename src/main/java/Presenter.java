import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Presenter implements Initializable {
    private final Sudoku sudoku;

    @FXML private Button solve;
    @FXML private Button save;
    @FXML private Button open;
    @FXML private Button print;
    @FXML private Button reset;
    @FXML private TextField field01;
    @FXML private GridPane board;
    @FXML private TextField [][] textFields;

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


        for(int i = 0; i < sudoku.getSize(); i++){
            for (int j = 0; j < sudoku.getSize(); j++){
                TextField textField = new TextField("0");
                textField.setStyle("-fx-pref-width: 2em;");
                board.setConstraints(textField, i, j);
                board.getChildren().add(textField);

                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    sudoku.setFieldValue(GridPane.getColumnIndex(textField), GridPane.getRowIndex(textField), Integer.parseInt(newValue));
                });

                sudoku.getGrid()[i][j].getProperty().addListener((observable, oldValue, newValue) -> {
                  //  textField.setText(String.valueOf(newValue));
                    textField.textProperty().setValue(String.valueOf(newValue));
                });

              //  textField.textProperty().bind(sudoku.getGrid()[i][j].getProperty().asString());
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
    }

    public void saveButtonHandler(ActionEvent actionEvent){
        Stage stage = Stage.class.cast(Control.class.cast(actionEvent.getSource()).getScene().getWindow());
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text-Datei (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                sudoku.saveValues(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openButtonHandler (ActionEvent actionEvent){
        Stage stage = Stage.class.cast(Control.class.cast(actionEvent.getSource()).getScene().getWindow());
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text-Datei (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                sudoku.openValues(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setField01(int value) {
        this.field01.setText(String.valueOf(value));
    }
}
