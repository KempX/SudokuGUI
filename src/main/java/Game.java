import java.util.*;

public class Game {
    private int size;
    private int fieldCount;
    public int solvedFields;
    private int iterations;
    private Field [][] grid;
    private Score [] rows;
    private Score [] columns;
    private Score [] blocks;


    public Game (int size){
        generateGame(size);
    }

    private void generateGame(int size) {
        grid = new Field[size][size];
        this.size = size;
        fieldCount = size * size;
        solvedFields = 0;
        iterations = 0;
        rows = new Score[size];
        columns = new Score[size];
        blocks = new Score[size];

        // Reset Fields
        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                grid[i][j] = new Field();
                grid[i][j].reset(size);
            }
        }

        // Generate Score Arrays
        for(int i = 0; i< size; i++){
            rows[i] = new Score(size);
            columns[i] = new Score(size);
            blocks[i] = new Score(size);
        }

        // Set Blocks
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

    public void loadStartValues(int [][] startValues){
        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if (startValues[i][j] != 0) {
                    setFieldValue(j, i, startValues[i][j]);
                }
            }
        }
        printTable("Die Vorgabe ist: ", grid);
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
