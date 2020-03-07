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
    private FileIO fileIO = new FileIO(this);

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

    private void generateFields(int size) {
        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                grid[i][j] = new Field(size);
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

    public void reset() {
        solvedFields = 0;
        resetFields();
    }

    private void resetFields(){
        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++) {
                grid[i][j].setFieldValue(0);
            }
        }
    }

    public int getSize(){
        return size;
    }

    public Field[][] getGrid(){
        return grid;
    }

    public FileIO getFileIO(){
        return fileIO;
    }

    public void setFieldValue (int x, int y, int value){
        if(grid[x][y].getFieldValue() != value) {
            grid[x][y].setFieldValue(value);
            removePossibleValues(x, y, value);
            calculateScore();
            if (value != 0) {
                solvedFields++;
            }
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

    public void solve(){
        solveWithOwnAlgorithm();
        if (solvedFields == fieldCount){
            System.out.printf("Iterationen: %d\n", iterations);
            printTable("Folgende Lösung wurde mit dem eigenen Algorithmus gefunden: ", grid);
        } else {
            System.out.println("Keine Lösung mit eigenem Algorithmus möglich.");
            //todo: solveWithWebAlgorithm();
        }
    }

    private void solveWithOwnAlgorithm() {
        iterations = 0;

        while ((solvedFields < fieldCount) && (iterations < 100)){
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
}