import javafx.stage.FileChooser;

import java.io.*;
import java.util.*;

public class Sudoku {
    private int size;
    private int fieldCount;
    public int solvedFields;
    private int iterations;
    private Field [][] grid;
    private StartValues startValues;
    private Score [] rows;
    private Score [] columns;
    private Score [] blocks;


    public Sudoku(int size){
        generateGame(size);
    }

    private void generateGame(int size) {
        grid = new Field[size][size];
        this.size = size;
        fieldCount = size * size;
        solvedFields = 0;
        iterations = 0;
        startValues = new StartValues();
        rows = new Score[size];
        columns = new Score[size];
        blocks = new Score[size];

        ResetFields(size);
        GenerateScoreArrays(size);
        SetBlocks();
    }
    private void ResetFields(int size) {
        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                grid[i][j] = new Field();
                grid[i][j].reset(size);
            }
        }
    }

    private void GenerateScoreArrays(int size) {
        for(int i = 0; i< size; i++){
            rows[i] = new Score(size);
            columns[i] = new Score(size);
            blocks[i] = new Score(size);
        }
    }
    private void SetBlocks() {
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

    // todo: Diese Methode brauchts nicht, wenn aus dem File geladen wird.
    public void loadStartValues(){
        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if (startValues.getStartValues()[i][j] != 0) {
                    setFieldValue(j, i, startValues.getStartValues()[i][j]);
                }
            }
        }
        printTable("Die Vorgabe ist: ", grid);
    }

    public void openValues(File file) throws IOException {
        //todo: Wie geht die Initialisierung, wenn Daten aus dem File geladen werden?
        Scanner scanner = new Scanner(file);
        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                grid[j][i].setFieldValue(scanner.nextInt());
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

    public void setFieldValue (int x, int y, int value){
        grid[x][y].setFieldValue(value);
        removePossibleValues(x, y, value);
        calculateScore();
        solvedFields++;
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
        while (solvedFields < fieldCount){
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
        printTable("Die Lösung ist: ", grid);
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
