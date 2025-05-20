import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Solver {
    public static void UCS(Board initialboard) {
        long startTime = System.currentTimeMillis();
            
        PriorityQueue<State> queue = new PriorityQueue<>(
            Comparator.comparingInt(state -> state.cost)
        );
            
        Set<String> visited = new HashSet<>();
              
        State initialState = new State(initialboard, 0, null, null, ' ', 'U');
        queue.add(initialState);
        visited.add(initialState.board.getBoardString());
        
        int nodesVisited = 0;
        
        System.out.println("Starting search...");
        
        while (!queue.isEmpty()) {
            
            State currentState = queue.poll();
            nodesVisited++;
                        
            if (currentState.board.isSolved()) {
                long endTime = System.currentTimeMillis();
                
                System.out.println("\nSolution found!");
                System.out.println("Number of moves: " + currentState.cost);
                System.out.println("Nodes visited: " + nodesVisited);
                System.out.println("Time taken: " + (endTime - startTime) + " ms");
                               
                ioHandler.saveToFile(nodesVisited, System.currentTimeMillis() - startTime, currentState);
                printSolution(currentState);
                return;
            }
            
            List<State> nextStates = currentState.getNextStates(currentState);
            
            for (State nextState : nextStates) {
                if (!visited.contains(nextState.board.getBoardString())) {
                    queue.add(nextState);
                    visited.add(nextState.board.getBoardString());
                }else if (queue.contains(nextState)) {
                    for (State stateInQueue : queue) {
                        if (stateInQueue.equals(nextState) && stateInQueue.cost > nextState.cost) {
                            queue.remove(stateInQueue);
                            queue.add(nextState);
                            break;
                        }
                    }
                }
            }
        }        
        ioHandler.saveNoSolutionToFile(nodesVisited, System.currentTimeMillis() - startTime);
        System.out.println("No solution found.");
        System.out.println("Nodes visited: " + nodesVisited);
        System.out.println("Time taken: " + (System.currentTimeMillis() - startTime) + " ms");
    }

    public static void GBFS(Board initialboard) {
        long startTime = System.currentTimeMillis();
        
        PriorityQueue<State> queue = new PriorityQueue<>(
            Comparator.comparingInt(state -> state.getHeuristicCost())
        );
         
        Set<String> visited = new HashSet<>();
        
        State initialState = new State(initialboard, 0, null, null, ' ', 'U');
        queue.add(initialState);
        visited.add(initialState.board.getBoardString());
        
        int nodesVisited = 0;
        
        System.out.println("Starting Greedy Best-First search...");
        
        while (!queue.isEmpty()) {
            State currentState = queue.poll();
            nodesVisited++;
            
            if (currentState.board.isSolved()) {
                long endTime = System.currentTimeMillis();
                 
                System.out.println("\nSolution found!");
                System.out.println("Number of moves: " + currentState.cost);
                System.out.println("Nodes visited: " + nodesVisited);
                System.out.println("Time taken: " + (endTime - startTime) + " ms");
                
                ioHandler.saveToFile(nodesVisited, System.currentTimeMillis() - startTime, currentState);
                printSolution(currentState);
                return;
            }
            
            List<State> nextStates = currentState.getNextStates(currentState);
            
            for (State nextState : nextStates) {
                String nextStateString = nextState.board.getBoardString();
                    
                if (!visited.contains(nextStateString)) {
                    queue.add(nextState);
                    visited.add(nextStateString);
                }
            }
            
        }       
        ioHandler.saveNoSolutionToFile(nodesVisited, System.currentTimeMillis() - startTime);
        System.out.println("No solution found.");
        System.out.println("Nodes visited: " + nodesVisited);
        System.out.println("Time taken: " + (System.currentTimeMillis() - startTime) + " ms");
    }
    
    public static void AStar(Board initialBoard) {
        long startTime = System.currentTimeMillis();
                
        PriorityQueue<State> queue = new PriorityQueue<>(
            Comparator.comparingInt(state -> state.cost + state.getHeuristicCost())
        );
                
        Set<String> visited = new HashSet<>();
                
        State initialState = new State(initialBoard, 0, null, null, ' ', 'U');
        queue.add(initialState);
                
        int nodesVisited = 0;
        
        System.out.println("Starting A* search...");
        
        while (!queue.isEmpty()) {            
            State currentState = queue.poll();
            nodesVisited++;
                       
            String currentStateString = currentState.board.getBoardString();
            if (visited.contains(currentStateString)) {
                continue;
            }
            
            visited.add(currentStateString);
                        
            if (currentState.board.isSolved()) {
                long endTime = System.currentTimeMillis();
                                
                System.out.println("\nSolution found!");
                System.out.println("Number of moves: " + currentState.cost);
                System.out.println("Nodes visited: " + nodesVisited);
                System.out.println("Time taken: " + (endTime - startTime) + " ms");
                
                ioHandler.saveToFile(nodesVisited, System.currentTimeMillis() - startTime, currentState);
                printSolution(currentState);
                return;
            }
                        
            List<State> nextStates = currentState.getNextStates(currentState);
            
            for (State nextState : nextStates) {
                String nextStateString = nextState.board.getBoardString();
                if (!visited.contains(nextStateString)) {
                    queue.add(nextState);
                }
            }
            
        }
        ioHandler.saveNoSolutionToFile(nodesVisited, System.currentTimeMillis() - startTime);
        System.out.println("No solution found.");
        System.out.println("Nodes visited: " + nodesVisited);
        System.out.println("Time taken: " + (System.currentTimeMillis() - startTime) + " ms");
    }
    
    private static void printSolution(State finalState) {
        List<State> path = new ArrayList<>();
        State currentState = finalState;
        
        while (currentState != null) {
            path.add(currentState);
            currentState = currentState.parent;
        }
                
        Collections.reverse(path);
                
        System.out.println("Papan Awal:");
        path.get(0).board.printBoard(null, ' ', ' ');
          
        for (int i = 1; i < path.size(); i++) {
            State state = path.get(i);
            System.out.println("Gerakan " + i + ": " + state.movedPiece + "-" + 
                               (state.direction == 'U' ? "atas" : 
                                state.direction == 'D' ? "bawah" : 
                                state.direction == 'L' ? "kiri" : "kanan"));
            
            state.board.printBoard(state.previousPosition, state.movedPiece, state.direction);
        }
    }
}
