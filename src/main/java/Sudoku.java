import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.*;

public class Sudoku {
    private int size;
    private int fieldCount;
    private int solvedFields;
    private int iterations;
    private Field [][] grid;
    private Score [] rows;
    private Score [] columns;
    private Score [] blocks;
   // private IntegerProperty [][] numbers = new SimpleIntegerProperty();
    ObservableList<ObservableList<Integer>> matrix = FXCollections.observableArrayList();


    public Sudoku(int size){
        this.size = size;
        fieldCount = size * size;
        grid = new Field[size][size];
        rows = new Score[size];
        columns = new Score[size];
        blocks = new Score[size];

        generateFields(size);
        generateScores(size);
        setBlocks();
    }

    public void reset() {
        solvedFields = 0;
        resetFields();
    }

    private void generateFields(int size) {
        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                grid[i][j] = new Field(size);
            }
        }
    }

    private void resetFields(){
        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++) {
                grid[i][j].setFieldValue(0);
            }
        }
    }

    private void generateScores(int size) {
        for(int i = 0; i< size; i++){
            rows[i] = new Score(size);
            columns[i] = new Score(size);
            blocks[i] = new Score(size);
        }
    }



    private void setBlocks() { //todo: funktioniert nur bei 3x3
        int b = 0;
        for(int bi = 0; bi < 3; bi++){
            for(int bj = 0; bj < 3; bj++){
                for(int i = 0; i < 3; i++){
                    for(int j = 0; j < 3; j++){
                        grid[(bi * 3) + i][(bj * 3) + j].setBlock(b);
                    }
                }
                b++;
            }
        }
    }

    public int getSize(){
        return size;
    }

    public void openValues(File file) throws IOException {
        int value;
        Scanner scanner = new Scanner(file);

        this.reset();

        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                value = scanner.nextInt();

                if (value != 0) {
                    setFieldValue(j, i, value);
                }

            }
        }
        printTable("Folgende Werte wurden per File geladen: ",grid);
    }

    public void saveValues(File file) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                out.print(grid[j][i].getFieldValue() + "  ");
            }
            out.println();
        }
        out.close();
    }

    public int getFieldCount() {
        return fieldCount;
    }

    public Field[][] getGrid(){
        return grid;
    }

    public void setFieldValue (int x, int y, int value){
        grid[x][y].setFieldValue(value);
        removePossibleValues(x, y, value);
        calculateScore();
        if (value != 0) {
            solvedFields++;
        }
    }

    public void removePossibleValues (int x, int y, int value){
        for(int i = 0; i < size; i++){
            grid[i][y].removePossibleValue(value);
        }

        for(int j = 0; j < size; j++){
            grid[x][j].removePossibleValue(value);
        }

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(grid[i][j].getBlock() == grid[x][y].getBlock()){
                    grid[i][j].removePossibleValue(value);
                }
            }
        }

        grid[x][y].addPossibleValue(value);
    }

    public void solve(){
        for (int t = 0; t < 100; t++){
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if(grid[i][j].getFieldValue() == 0){
                        for (int v : grid[i][j].getPossibleValues()){
                            if (rows[j].getScore(v) == 1 || columns[i].getScore(v) == 1 || blocks[grid[i][j].getBlock()].getScore(v) == 1){
                                setFieldValue(i,j,v);
                                break;
                            }
                        }
                    }
                }
            }
            iterations++;
        }
        if (solvedFields < fieldCount){
            System.out.println("Keine Lösung mit diesem Algorithmus möglich.");
        } else {
            printTable("Die Lösung ist: ", grid);
        }
    }

    public void printTable(String text, Field [][] table) {
        System.out.println(text);
        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                System.out.print(table[j][i].getFieldValue() + "  ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void calculateScore(){
        for (int i = 0; i < size; i++){
            rows[i].reset(size);
            columns[i].reset(size);
            blocks[i].reset(size);
        }

        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                for (int v : grid[i][j].getPossibleValues()){
                    columns[i].increaseScore(v);
                    rows[j].increaseScore(v);
                    blocks[grid[i][j].getBlock()].increaseScore(v);
                }
            }
        }
    }
}
