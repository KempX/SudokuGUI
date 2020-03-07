import java.io.*;
import java.util.Scanner;

public class FileIO {
    private Sudoku sudoku;

    public FileIO(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    public void openValues(File file) throws IOException {
        int value;
        Scanner scanner = new Scanner(file);

        sudoku.reset();

        for(int i = 0; i < sudoku.getSize(); i++){
            for (int j = 0; j < sudoku.getSize(); j++){
                value = scanner.nextInt();

                if (value != 0) {
                    sudoku.setFieldValue(j, i, value);
                }
            }
        }
        sudoku.printTable("Folgende Werte wurden per File geladen: ", sudoku.getGrid());
    }

    public void saveValues(File file) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
        for(int i = 0; i < sudoku.getSize(); i++){
            for (int j = 0; j < sudoku.getSize(); j++){
                out.print(sudoku.getGrid()[j][i].getFieldValue() + "  ");
            }
            out.println();
        }
        out.close();
    }
}
