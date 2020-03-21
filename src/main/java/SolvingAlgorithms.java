public class SolvingAlgorithms {
    private Sudoku sudoku;
    private Field [][] grid;
    private int iterations;
    private int size;

    public SolvingAlgorithms (Sudoku sudoku, Field [][] grid){
        this.sudoku = sudoku;
        this.grid = grid;
        this.size = grid.length;
    }

    public void solve(){
        iterations = 0;
        solveWithOwnAlgorithm();
        if (sudoku.getSolvedFields() == sudoku.getFieldCount()){
            sudoku.printTable("Folgende Lösung wurde mit dem eigenen Algorithmus gefunden: ", grid);
            sudoku.setStatus("Mit dem eigenen Algorithmus in " + iterations + " Schritten gelöst. ");
        } else {
            iterations = 0;
            solveWithBacktracking();
            sudoku.printTable("Diese Lösung wurde mit dem Backtracking-Algorithmus gefunden: ", grid);
            sudoku.setStatus("Mit dem Backtracking-Algorithmus in "+ iterations + " Schritten gelöst. ");
        }
    }

    private void solveWithOwnAlgorithm() {
        while ((sudoku.getSolvedFields() < sudoku.getFieldCount()) && (iterations < 50)){
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if(grid[i][j].getFieldValue() == 0){
                        for (int v : grid[i][j].getPossibleValues()){
                            if (sudoku.isFieldSolvable(i, j, v)){
                                sudoku.setFieldValue(i,j,v);
                                break;
                            }
                        }
                    }
                }
            }
            iterations++;
        }
    }

    private boolean solveWithBacktracking(){
        int row = -1;
        int col = -1;
        boolean isEmpty = true;

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                if (grid[i][j].getFieldValue() == 0){
                    row = i;
                    col = j;
                    isEmpty = false;
                    break;
                }
            }
            if (!isEmpty){
                break;
            }
        }

        if (isEmpty){
            return true;
        }

        for (int num = 1; num <= size; num++){
            if (isSafe(row, col, num)){
                grid[row][col].setFieldValue(num);
                if (solveWithBacktracking()){
                    return true;
                } else {
                    grid[row][col].setFieldValue(0);
                }
            }
            iterations++;
        }
        return false;
    }

    private boolean isSafe(int row, int col, int num)
    {
        for (int d = 0; d < grid.length; d++){
            if (grid[row][d].getFieldValue() == num){
                return false;
            }
        }

        for (int r = 0; r < grid.length; r++){
            if (grid[r][col].getFieldValue() == num){
                return false;
            }
        }

        int sqrt = (int) Math.sqrt(grid.length);
        int boxRowStart = row - row % sqrt;
        int boxColStart = col - col % sqrt;

        for (int r = boxRowStart; r < boxRowStart + sqrt; r++){
            for (int d = boxColStart; d < boxColStart + sqrt; d++){
                if (grid[r][d].getFieldValue() == num){
                    return false;
                }
            }
        }
        return true;
    }
}
