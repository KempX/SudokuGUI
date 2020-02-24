import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Presenter implements Initializable {
    private final Sudoku sudoku;

    @FXML private Button load;
    @FXML private Button solve;
    @FXML private Button save;
    @FXML private Button open;

    public Presenter(Sudoku sudoku){
        this.sudoku = sudoku;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        load.setOnAction(this::loadButtonHandler);
        solve.setOnAction(this::solveButtonHandler);
        save.setOnAction(this::saveButtonHandler);
        open.setOnAction(this::openButtonHandler);
    }

    public void loadButtonHandler(ActionEvent actionEvent){
        sudoku.loadStartValues();
    }

    public void solveButtonHandler(ActionEvent actionEvent){
        sudoku.solve();
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

}
