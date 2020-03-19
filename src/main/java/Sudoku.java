import javafx.beans.property.SimpleStringProperty;

public class Sudoku {
    private int size;
    private int fieldCount;
    private int solvedFields;
    private Field [][] grid;
    private Score [] rows;
    private Score [] columns;
    private Score [] blocks;
    private SimpleStringProperty status = new SimpleStringProperty();
    private FileIO fileIO = new FileIO(this);
    private SolvingAlgorithms solvingAlgorithms;

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

    public void solve (){
        solvingAlgorithms = new SolvingAlgorithms(this, getGrid());
        solvingAlgorithms.solve();
    }

    public boolean isFieldSolvable (int i, int j, int v){
        int rowScore = rows[j].getScore(v);
        int columnScore = columns[i].getScore(v);
        int blockScore = blocks[grid[i][j].getBlock()].getScore(v);

        if (rowScore == 1 || columnScore == 1 || blockScore == 1){
            return true;
        } else {
            return false;
        }
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

    public void setStatus (String text){
        status.set(text);
    }

    public SimpleStringProperty getStatus(){
        return status;
    }

    public int getSize(){
        return size;
    }

    public int getSolvedFields(){
        return solvedFields;
    }

    public int getFieldCount(){
        return fieldCount;
    }

    public Field[][] getGrid(){
        return grid;
    }

    public FileIO getFileIO(){
        return fileIO;
    }
}