import java.util.*;

class State {
    Board board;
    int cost;
    State parent;
    int[] previousPosition;
    char movedPiece;        
    char direction;         

    public State(Board board, int cost, State parent, 
                int[] previousPosition, char movedPiece, char direction) {
        this.board = board;
        this.cost = cost;
        this.parent = parent;
        this.previousPosition = previousPosition;
        this.movedPiece = movedPiece;
        this.direction = direction;
    }

    public State getParent() {
        return parent;
    }

    public List<State> getNextStates(State currentState) {
        List<State> nextStates = new ArrayList<>();
        Map<Character, List<int[]>> piecePositions = new HashMap<>();
        int rows = board.getRows();
        int cols = board.getCols();
        char[][] boardGrid = board.getBoard();
    
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char piece = boardGrid[i][j];
                if (piece != '.' && piece != 'K') {
                    piecePositions.computeIfAbsent(piece, k -> new ArrayList<>())
                                  .add(new int[]{i, j});
                }
            }
        }
        
        for (Map.Entry<Character, List<int[]>> entry : piecePositions.entrySet()) {
            
            char piece = entry.getKey();
            List<int[]> positions = entry.getValue();
                    
            boolean isHorizontal = true;
            if (positions.size() > 1) {
                isHorizontal = positions.get(0)[0] == positions.get(1)[0];
            }
                        
            if (piece == 'P' && board.isPrimaryHorizontal()) {
                
                positions.sort(Comparator.comparingInt(pos -> pos[1]));
                            
                int leftmostCol = positions.get(0)[1];
                int rightmostCol = positions.get(positions.size() - 1)[1];

                int row = positions.get(0)[0];
            
                // exit ada di kiri, gerakan P ke kiri
                if (leftmostCol == 0 && board.getExitPosition()[1] == -1) {                
                    Board newBoard = new Board(board);
                                    
                    int[] previousRightmost = positions.get(positions.size() - 1);

                    newBoard.getBoard()[row][leftmostCol] = piece;
                    newBoard.getBoard()[row][previousRightmost[1]] = '.';
                    
                    if (piece == 'P') {
                        newBoard.setPrimaryPosition(new int[]{row, leftmostCol});
                    }
                                       
                    nextStates.add(new State(
                        newBoard, 
                        currentState.cost + 1, 
                        currentState,
                        new int[]{previousRightmost[0], previousRightmost[1]},
                        piece,
                        'L'
                    ));
                }
              
                // exit ada di kanan, gerakan P ke kanan
                if (rightmostCol == cols-1 && board.getExitPosition()[1] == cols) {                    
                    Board newBoard = new Board(this.board);
                                       
                    int[] previousLeftmost = positions.get(0);                   
                    
                    newBoard.getBoard()[row][rightmostCol] = piece;
                    newBoard.getBoard()[row][previousLeftmost[1]] = '.';                    
                    
                    if (piece == 'P') {
                        newBoard.setPrimaryPosition(new int[]{row, rightmostCol});
                    }
                                        
                    nextStates.add(new State(
                        newBoard, 
                        currentState.cost + 1, 
                        currentState,
                        new int[]{previousLeftmost[0], previousLeftmost[1]},
                        piece,
                        'R'
                    ));
                } 
            }
            else if (piece == 'P' && !board.isPrimaryHorizontal()) {
                int topmostRow = positions.get(0)[0];
                int col = positions.get(0)[1];

                // exit ada di atas, gerakan P ke atas
                if (topmostRow == 0 && board.getExitPosition()[0] == -1) {
                    Board newBoard = new Board(this.board);
                                        
                    int[] previousBottommost = positions.get(positions.size() - 1);
                                        
                    newBoard.getBoard()[topmostRow][col] = piece;
                    newBoard.getBoard()[previousBottommost[0]][col] = '.';
                                        
                    if (piece == 'P') {
                        newBoard.setPrimaryPosition(new int[]{topmostRow, col});
                    }
                                        
                    nextStates.add(new State(
                        newBoard, 
                        currentState.cost + 1, 
                        currentState,
                        new int[]{previousBottommost[0], previousBottommost[1]},
                        piece,
                        'U'
                    ));
                }  
                int bottommostRow = positions.get(positions.size() - 1)[0];
                
                // exit ada di bawah, gerakan P ke bawah
                if (bottommostRow == rows - 1 && board.getExitPosition()[0] == rows) {
                    
                    Board newBoard = new Board(this.board);
                    
                    int[] previousTopmost = positions.get(0);
                                        
                    newBoard.getBoard()[bottommostRow][col] = piece;
                    newBoard.getBoard()[previousTopmost[0]][col] = '.';
                                        
                    if (piece == 'P') {
                        newBoard.setPrimaryPosition(new int[]{bottommostRow, col}); 
                    }    
                    nextStates.add(new State(
                        newBoard, 
                        currentState.cost + 1, 
                        currentState,
                        new int[]{previousTopmost[0], previousTopmost[1]},
                        piece,
                        'D'
                    ));
                }
            }
            if (isHorizontal) {                
                positions.sort(Comparator.comparingInt(pos -> pos[1]));                
                
                int leftmostCol = positions.get(0)[1];
                int row = positions.get(0)[0];
                
                if (leftmostCol > 0 && boardGrid[row][leftmostCol - 1] == '.') {
                    
                    Board newBoard = new Board(this.board);
                    
                    
                    int[] previousRightmost = positions.get(positions.size() - 1);
                                        
                    newBoard.getBoard()[row][leftmostCol - 1] = piece;
                    newBoard.getBoard()[row][previousRightmost[1]] = '.';
                                        
                    if (piece == 'P') {
                        newBoard.setPrimaryPosition(new int[]{row, leftmostCol - 1});
                    }
                            
                    nextStates.add(new State(
                        newBoard, 
                        currentState.cost + 1, 
                        currentState,
                        new int[]{previousRightmost[0], previousRightmost[1]},
                        piece,
                        'L'
                    ));
                }
                                
                int rightmostCol = positions.get(positions.size() - 1)[1];

                if (rightmostCol < cols-1 && boardGrid[row][rightmostCol + 1] == '.') {
                    
                    Board newBoard = new Board(this.board);
                                    
                    int[] previousLeftmost = positions.get(0);
                                     
                    newBoard.getBoard()[row][rightmostCol + 1] = piece;
                    newBoard.getBoard()[row][leftmostCol] = '.';
                                     
                    if (piece == 'P') {
                         newBoard.setPrimaryPosition(new int[]{row, leftmostCol + 1}); 
                    }
                    
                    nextStates.add(new State(
                        newBoard, 
                        currentState.cost + 1, 
                        currentState,
                        new int[]{previousLeftmost[0], previousLeftmost[1]},
                        piece,
                        'R'
                    ));
                }
            } else { 
                positions.sort(Comparator.comparingInt(pos -> pos[0]));
                                
                int topmostRow = positions.get(0)[0];
                int col = positions.get(0)[1];
                
                if (topmostRow > 0 && boardGrid[topmostRow - 1][col] == '.') {
                    
                    Board newBoard = new Board(this.board);
                                        
                    int[] previousBottommost = positions.get(positions.size() - 1);
                                        
                    newBoard.getBoard()[topmostRow - 1][col] = piece;
                    newBoard.getBoard()[previousBottommost[0]][col] = '.';
                                        
                    if (piece == 'P') {
                        newBoard.setPrimaryPosition(new int[]{topmostRow - 1, col}); 
                    }
                                        
                    nextStates.add(new State(
                        newBoard, 
                        currentState.cost + 1, 
                        currentState,
                        new int[]{previousBottommost[0], previousBottommost[1]},
                        piece,
                        'U'
                    ));
                }
                                
                int bottommostRow = positions.get(positions.size() - 1)[0];

                if (bottommostRow < rows - 1 && boardGrid[bottommostRow + 1][col] == '.') {
                    
                    Board newBoard = new Board(this.board);
                                        
                    int[] previousTopmost = positions.get(0);
                                        
                    newBoard.getBoard()[bottommostRow + 1][col] = piece;
                    newBoard.getBoard()[topmostRow][col] = '.';
                                        
                    if (piece == 'P') {
                        newBoard.setPrimaryPosition(new int[]{topmostRow + 1, col}); 
                    }
                    
                    nextStates.add(new State(
                        newBoard, 
                        currentState.cost + 1, 
                        currentState,
                        new int[]{previousTopmost[0], previousTopmost[1]},
                        piece,
                        'D'
                    ));
                }
            }
        }
        return nextStates;
    }

    public int getHeuristicCost() {
        
        int rows = board.getRows();
        int cols = board.getCols();
        char[][] boardGrid = board.getBoard();

        List<int[]> primaryPiecePositions = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (boardGrid[i][j] == 'P') {
                    primaryPiecePositions.add(new int[]{i, j});
                }
            }
        }

        if (primaryPiecePositions.isEmpty()) {
            return 0;
        }

        if (board.isPrimaryHorizontal()) {
            primaryPiecePositions.sort(Comparator.comparingInt(pos -> pos[1]));
            
            int[] leftmost = primaryPiecePositions.get(0);
            int[] rightmost = primaryPiecePositions.get(primaryPiecePositions.size() - 1);
            
            if (board.getExitPosition()[1] == -1) {
                return leftmost[1];
            } else {
                return cols - 1 - rightmost[1];
            }
        } else {
            primaryPiecePositions.sort(Comparator.comparingInt(pos -> pos[0]));
            
            int[] topmost = primaryPiecePositions.get(0);
            int[] bottommost = primaryPiecePositions.get(primaryPiecePositions.size() - 1);
            
            if (board.getExitPosition()[0] == -1) {  
                return topmost[0];
            } else {
                return rows - 1 - bottommost[0];
            }
        }
    }
}
