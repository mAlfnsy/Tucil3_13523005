import java.util.*;

public class Board {
    private int rows;
    private int cols;
    private char[][] board;
    private int[] exitPosition;
    private int[] primaryPosition;
    private boolean IsPrimaryHorizontal;
    
    public Board(int rows, int cols, char[][] board) {
        this(rows, cols, board, null);
    }
    
    public Board(int rows, int cols, char[][] board, int[] exitPos) {
        this.rows = rows;
        this.cols = cols;
        this.board = new char[rows][cols];
        
        // Copy the board
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.board[i][j] = board[i][j];
                
                // Find the exit position ('K') if not provided
                if (exitPos == null && board[i][j] == 'K') {
                    this.exitPosition = new int[]{i, j};
                }
                
                // Find the primary piece position ('P')
                if (board[i][j] == 'P' && primaryPosition == null) {
                    primaryPosition = new int[]{i, j};
                }
            }
        }
        
        // Use provided exit position if available
        if (exitPos != null) {
            this.exitPosition = new int[]{exitPos[0], exitPos[1]};
        }
        
        // Verify exitPosition is set
        if (this.exitPosition == null) {
            System.err.println("Warning: Exit position (K) not found in the puzzle!");
        }
        
        // Determine if the primary piece is horizontal or vertical
        determineOrientationOfPrimaryPiece();
    }
    
    public Board(Board other) {
        this.rows = other.rows;
        this.cols = other.cols;
        this.board = new char[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                this.board[i][j] = other.board[i][j];
            }
        }
        
        this.exitPosition = (other.exitPosition != null) ? new int[]{other.exitPosition[0], other.exitPosition[1]} : null;
        this.primaryPosition = (other.primaryPosition != null) ? 
                                    new int[]{other.primaryPosition[0], other.primaryPosition[1]} : null;
        this.IsPrimaryHorizontal = other.IsPrimaryHorizontal;
    }
    
    // Getter for exitPosition
    public int[] getExitPosition() {
        return exitPosition;
    }

    public int getRows() {
        return rows;
    }
    
    public int getCols() {
        return cols;
    }

    public char[][] getBoard() {
        return board;
    }

    public boolean isPrimaryHorizontal() {
        return IsPrimaryHorizontal;
    }

    public void setPrimaryPosition(int[] position) {
        this.primaryPosition = position;
    }


    private void determineOrientationOfPrimaryPiece() {
        // Check if there's a primary piece to the right of the found position
        if (primaryPosition != null) {
            if (primaryPosition[1] + 1 < cols && 
                board[primaryPosition[0]][primaryPosition[1] + 1] == 'P') {
                IsPrimaryHorizontal = true;
            } else if (primaryPosition[0] + 1 < rows && 
                       board[primaryPosition[0] + 1][primaryPosition[1]] == 'P') {
                IsPrimaryHorizontal = false;
            } else {
                // Single cell piece, defaulting to horizontal
                IsPrimaryHorizontal = true;
            }
            
        } else {
            System.err.println("Warning: Primary piece (P) not found in the puzzle!");
        }
    }
    
    public boolean isSolved() {
        if (exitPosition == null || primaryPosition == null) {
            return false;
        }

        // Find all positions of the primary piece
        List<int[]> primaryPiecePositions = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == 'P') {
                    primaryPiecePositions.add(new int[]{i, j});
                }
            }
        }

        if (primaryPiecePositions.isEmpty()) {
            return true;
        }

        if (IsPrimaryHorizontal) {
            primaryPiecePositions.sort(Comparator.comparingInt(pos -> pos[1]));
            
            int[] leftmost = primaryPiecePositions.get(0);
            int[] rightmost = primaryPiecePositions.get(primaryPiecePositions.size() - 1);
            
            // jika exit ada di kiri
            if (exitPosition[1] == -1) {
                return exitPosition[0] == leftmost[0] && rightmost[1] == -1;
            }
            else{
                return exitPosition[0] == rightmost[0] && leftmost[1] == cols;
            }
            
        
        } else {// Vertical
            primaryPiecePositions.sort(Comparator.comparingInt(pos -> pos[0]));

            // Get bottommost position of the primary piece
            int[] topmost = primaryPiecePositions.get(0);
            int[] bottommost = primaryPiecePositions.get(primaryPiecePositions.size() - 1);
            
            // jika exit ada di atas
            if (exitPosition[0] == -1) {
                return exitPosition[1] == topmost[1] && bottommost[0] == -1;
            } 
            else{ // exit di bawah
                return exitPosition[1] == bottommost[1] && topmost[0] == rows;
            }
        }
        
    }

    public String getBoardString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(board[i][j]);
            }
        }
        return sb.toString();
    }

    public String getOutputString() {
        StringBuilder sb = new StringBuilder();

        if (exitPosition[0] == -1){
            for (int i = 0; i < cols; i++) {
                if (i == exitPosition[1]) {
                    sb.append("K");
                } else {
                    sb.append("  ");
                }
            }
            sb.append("\n");
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    sb.append(board[i][j]);
                    sb.append(" ");
                }
                sb.append("\n");
            }
        } else if (exitPosition[1] == -1) {
            for (int i = 0; i < rows; i++) {
                if (i == exitPosition[0]) {
                    sb.append("K ");
                    for (int j = 0; j < cols; j++) {
                        sb.append(board[i][j] + " ");
                    }
                } else {
                    sb.append("  ");
                    for (int j = 0; j < cols; j++) {
                        sb.append(board[i][j] + " ");
                    }
                }
                sb.append("\n");
            }
        } else if (exitPosition[0] == rows) {
            for (int i = 0; i < rows+1; i++) {
                if (i == exitPosition[0]) {
                    for (int j = 0; j < cols; j++) {
                        if (j == exitPosition[1]) {
                            sb.append("K ");
                        } else {
                            sb.append("  ");
                        }
                    }
                    sb.append("\n");
                } else {
                    for (int j = 0; j < cols; j++) {
                        sb.append(board[i][j] + " ");
                    }
                    sb.append("\n");
                }
            }
        } else {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    sb.append(board[i][j] + " ");
                }
                if (i == exitPosition[0]) {
                    sb.append("K");
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    public void printBoard(int[] previousPosition, char movedPiece, char direction) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char cell = board[i][j];
                
                // ANSI color codes for console output
                String primaryPieceColor = "\u001B[31m"; // Red
                String exitColor = "\u001B[32m"; // Green
                String movedPieceColor = "\u001B[33m"; // Yellow
                String resetColor = "\u001B[0m";
                
                // Determine if this position corresponds to the moved piece
                boolean isMovedPiece = false;
                if (previousPosition != null && movedPiece != ' ') {
                    if (cell == movedPiece) {
                        isMovedPiece = true;
                    }
                }
                
                // Apply colors based on cell content
                if (cell == 'P') {
                    System.out.print(primaryPieceColor + cell + resetColor);
                } else if (cell == 'K') {
                    System.out.print(exitColor + cell + resetColor);
                } else if (isMovedPiece) {
                    System.out.print(movedPieceColor + cell + resetColor);
                } else {
                    System.out.print(cell);
                }
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}
