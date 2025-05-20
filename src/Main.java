import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
         
        System.out.print("Enter the path to the puzzle file: ");
        String filePath = scanner.nextLine();
        
         
        System.out.println("Select algorithm:");
        System.out.println("1. UCS (Uniform Cost Search)");
        System.out.println("2. Greedy Best First Search");
        System.out.println("3. A* (A-Star)");
        System.out.print("Enter your choice (1-3): ");
        int algorithmChoice = scanner.nextInt();
        
        try {
             
            Board puzzle = ioHandler.readPuzzleFromFile(filePath);
            
            System.out.println("Papan Awal:");
            puzzle.printBoard(null, ' ', ' ');
            System.out.println("Posisi Exit (K): (" + puzzle.getExitPosition()[0] + ", " + puzzle.getExitPosition()[1] + ")");
            
             
            switch (algorithmChoice) {
                case 1:
                    Solver.UCS(puzzle);
                    break;
                case 2:
                    Solver.GBFS(puzzle);
                    break;
                case 3:
                     
                    Solver.AStar(puzzle);
                    break;
                default:
                    System.out.println("Invalid choice. Using UCS by default.");
                    Solver.UCS(puzzle);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }  
}
