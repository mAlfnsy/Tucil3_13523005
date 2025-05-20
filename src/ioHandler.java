

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ioHandler {
    public static Board readPuzzleFromFile(String filePath) throws IOException {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("test/" + filePath + ".txt"));
        } catch (FileNotFoundException e) {
            
            reader = new BufferedReader(new FileReader(filePath));
        }
        
        String[] dimensions = reader.readLine().split("\\s+");
        int rows = Integer.parseInt(dimensions[0]);
        int cols = Integer.parseInt(dimensions[1]);
        
        int numPieces = Integer.parseInt(reader.readLine());
        
        char[][] tboard = new char[rows+1][cols+1];
        for (int x = 0; x < rows + 1; x++) {
            for (int y = 0; y < cols + 1; y++) {
                tboard[x][y] = ' ';
            }
        }
        int[] exitPosition = null; 
        
        String line;
        int i = 0;
        while ((line = reader.readLine()) != null && i < rows + 1) {
            for (int j = 0; j < cols + 1; j++) {
                if (j < line.length()) {
                    tboard[i][j] = line.charAt(j);
                    if (tboard[i][j] == 'K') {
                        int exitRow = i, exitCol = j;
                        if (j == 0 && i < cols){
                            exitCol = -1;
                        }
                        if (i == 0 && j < rows){
                            exitRow = -1;
                        }
                        System.out.println(exitCol + " " + exitRow);
                        exitPosition = new int[]{exitRow, exitCol};
                    }
                }
            }
            i++;
        }
        reader.close();
        
        char[][] board = new char[rows][cols];
        if (exitPosition[0] == -1){
            for (i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    board[i][j] = tboard[i+1][j];
                
                }
            }
        }
        else if (exitPosition[1] == -1){
            for (i = 0; i < rows; i++) {

                if (i == exitPosition[0]){
                    for (int j = 0; j < cols; j++) {
                        board[i][j] = tboard[i][j+1];
                    }
                }
                else{
                    for (int j = 0; j < cols; j++) {
                        board[i][j] = tboard[i][j];
                    }
                }
            }
        }
        else{
            for (i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    board[i][j] = tboard[i][j];
                }
            }
        }

        return new Board(rows, cols, board, exitPosition);
    }

    public static void saveToFile(int nodeCount, long executionTime, State solution) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Masukkan nama file untuk menyimpan solusi:");
        String filename;
        try {
            filename = "test/" + reader.readLine() + ".txt";
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
            return;
        }
        List<State> path = new ArrayList<>();
        State current = solution;
        while (current != null) {
            path.add(current);
            current = current.getParent();
        }
        Collections.reverse(path);

        try (PrintWriter writer = new PrintWriter(filename)) {
            for (int i = 1; i < path.size(); i++) {
                State state = path.get(i);
                writer.println("Gerakan " + i + ": " + state.movedPiece + "-" + 
                                (state.direction == 'U' ? "atas" : 
                                    state.direction == 'D' ? "bawah" : 
                                    state.direction == 'L' ? "kiri" : "kanan"));

                writer.println(state.board.getOutputString());
            }
            writer.println("Visited nodes: " + nodeCount);
            writer.println("Execution time: " + executionTime + " ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveNoSolutionToFile(int nodeCount, long executionTime) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Masukkan nama file untuk menyimpan solusi:");
        String filename;
        try {
            filename = "test/" + reader.readLine() + ".txt";
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
            return;
        }
        try (PrintWriter writer = new PrintWriter(filename)) {
            writer.println("Tidak ada solusi ditemukan.");
            writer.println("Visited nodes: " + nodeCount);
            writer.println("Execution time: " + executionTime + " ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
