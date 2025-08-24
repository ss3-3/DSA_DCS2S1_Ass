package Airline;
import java.util.*;

public class MalaysiaAirlineSystem {
    private MalaysiaAirlineGraph flightNetwork;
    private Scanner scanner;
    private String staffId;
    private MalaysiaAirlineGUI guiWindow;
    Data data = new Data();
    
    public MalaysiaAirlineSystem() {
        this.flightNetwork = new MalaysiaAirlineGraph(data);
        this.scanner = new Scanner(System.in);
        this.staffId = "MAS01";
        this.guiWindow = null;
    }
    
    public void run() {
        System.out.println("Welcome to Malaysia Airlines!");
        System.out.println("Staff ID: " + staffId);
        
        while (true) {
            displayMainMenu();
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    createGraphMenu();
                    break;
                case "2":
                    searchAirport();
                    break;
                case "3":
                    flightNetwork.displayGraph();
                    break;
                case "4":
                    showGUIMode();
                    break;
                case "0":
                    if (guiWindow != null) {
                        guiWindow.dispose();
                    }
                    System.out.println("Thank you for using Malaysia Airlines System!");
                    return;
                default:
                    System.out.println("[ERROR] Invalid selection. Please try again.");
            }
        }
    }
    
    private void displayMainMenu() {
        System.out.println("\nMain Menu (Press '0' to exit)");
        System.out.println("1. Create Graph");
        System.out.println("2. Search for an Airport");
        System.out.println("3. View the MAS Flight Network (Text)");
        System.out.println("4. View the MAS Flight Network (GUI)");
        System.out.println("0. Exit");
        System.out.print("Selection: ");
    }
    
    private void showGUIMode() {
        if (flightNetwork.getAirportCount() == 0) {
            System.out.println("[WARNING] No airports in the network yet. Add some airports first!");
            return;
        }
        
        try {
            if (guiWindow == null || !guiWindow.isDisplayable()) {
                guiWindow = new MalaysiaAirlineGUI(flightNetwork);
            }
            guiWindow.setVisible(true);
            guiWindow.toFront();
            guiWindow.refresh();
            System.out.println("[SUCCESS] GUI window opened! You can continue using the console while GUI is open.");
        } catch (Exception e) {
            System.out.println("[ERROR] Error opening GUI: " + e.getMessage());
        }
    }
    
    private void createGraphMenu() {
        while (true) {
            System.out.println("\nCreate Graph: Enter \"1\" to \"5\" for updating the graph.");
            System.out.println("1. Add a vertex");
            System.out.println("2. Remove a vertex");
            System.out.println("3. Add an edge");
            System.out.println("4. Remove an edge");
            System.out.println("5. Return to the main menu");
            System.out.print("Selection: ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    addVertexMenu();
                    break;
                case "2":
                    removeVertexMenu();
                    break;
                case "3":
                    addEdgeMenu();
                    break;
                case "4":
                    removeEdgeMenu();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("[ERROR] Invalid selection. Please try again.");
            }
            
            // Refresh GUI if it's open
            if (guiWindow != null && guiWindow.isVisible()) {
                guiWindow.refresh();
            }
        }
    }
    
    private void addVertexMenu() {
        while (true) {
            System.out.println("\n[ADD VERTEX] #(1) Add a vertex");
            
            // Show current airports
            System.out.println("Current airports in the network:");
            flightNetwork.displayAvailableAirports();
            System.out.println();
            
            System.out.print("Enter the name of the city (letters only, e.g., 'Kuala Lumpur'): ");
            String cityName = scanner.nextLine();
            
            if (cityName.trim().isEmpty()) {
                System.out.println("[ERROR] City name cannot be empty. Please try again.");
                continue;
            }
            
            flightNetwork.addVertex(cityName);
            
            System.out.print("Continue adding? (Y/N): ");
            String continueChoice = scanner.nextLine().trim().toLowerCase();
            if (!continueChoice.equals("y") && !continueChoice.equals("yes")) {
                break;
            }
        }
    }
    
    private void removeVertexMenu() {
        System.out.println("\n[REMOVE VERTEX] #(2) Remove a vertex");
        
        if (flightNetwork.getAirportCount() == 0) {
            System.out.println("No airports available to remove.");
            return;
        }
        
        // Show airports with numbers for selection
        flightNetwork.displayAirportsForSelection();
        System.out.println("0. Cancel operation");
        System.out.println();
        
        System.out.print("Select airport to remove (enter number): ");
        String choice = scanner.nextLine().trim();
        
        if (choice.equals("0")) {
            System.out.println("Operation cancelled.");
            return;
        }
        
        if (!InputValidator.isValidChoice(choice, 1, flightNetwork.getAirportCount())) {
            System.out.println("[ERROR] Invalid selection. Please enter a valid number.");
            return;
        }
        
        int index = Integer.parseInt(choice);
        String airportToRemove = flightNetwork.getAirportByIndex(index);
        
        if (airportToRemove != null) {
            System.out.print("[WARNING] Are you sure you want to remove " + airportToRemove + "? (Y/N): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("y") || confirm.equals("yes")) {
                flightNetwork.removeVertex(airportToRemove);
            } else {
                System.out.println("Operation cancelled.");
            }
        }
    }
    
    private void addEdgeMenu() {
        while (true) {
            System.out.println("\n[ADD EDGE] #(3) Add an Edge");
            
            if (flightNetwork.getAirportCount() < 2) {
                System.out.println("[ERROR] Need at least 2 airports to create a flight path.");
                System.out.println("Please add more airports first.");
                return;
            }
            
            // Show available airports with numbers
            flightNetwork.displayAirportsForSelection();
            System.out.println();
            
            // Select first airport
            System.out.print("Select 1st airport (enter number): ");
            String choice1 = scanner.nextLine().trim();
            
            if (!InputValidator.isValidChoice(choice1, 1, flightNetwork.getAirportCount())) {
                System.out.println("[ERROR] Invalid selection for 1st airport.");
                continue;
            }
            
            String airport1 = flightNetwork.getAirportByIndex(Integer.parseInt(choice1));
            
            // Select second airport
            System.out.print("Select 2nd airport (enter number): ");
            String choice2 = scanner.nextLine().trim();
            
            if (!InputValidator.isValidChoice(choice2, 1, flightNetwork.getAirportCount())) {
                System.out.println("[ERROR] Invalid selection for 2nd airport.");
                continue;
            }
            
            String airport2 = flightNetwork.getAirportByIndex(Integer.parseInt(choice2));
            
            if (airport1 != null && airport2 != null) {
                flightNetwork.addEdge(airport1, airport2);
            }
            
            System.out.print("Continue adding? (Y/N): ");
            String continueChoice = scanner.nextLine().trim().toLowerCase();
            if (!continueChoice.equals("y") && !continueChoice.equals("yes")) {
                break;
            }
        }
    }
    
    private void removeEdgeMenu() {
        System.out.println("\n[REMOVE EDGE] #(4) Remove an Edge");
        
        List<String> edges = flightNetwork.displayEdgesForSelection();
        if (edges.isEmpty()) {
            return;
        }
        
        System.out.println("0. Cancel operation");
        System.out.println();
        
        System.out.print("Select flight path to remove (enter number): ");
        String choice = scanner.nextLine().trim();
        
        if (choice.equals("0")) {
            System.out.println("Operation cancelled.");
            return;
        }
        
        if (!InputValidator.isValidChoice(choice, 1, edges.size())) {
            System.out.println("[ERROR] Invalid selection. Please enter a valid number.");
            return;
        }
        
        int index = Integer.parseInt(choice) - 1;
        String[] airports = edges.get(index).split("\\|");
        
        if (airports.length == 2) {
            System.out.print("[WARNING] Are you sure you want to remove the flight path between " + 
                           airports[0] + " and " + airports[1] + "? (Y/N): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            if (confirm.equals("y") || confirm.equals("yes")) {
                flightNetwork.removeEdge(airports[0], airports[1]);
            } else {
                System.out.println("Operation cancelled.");
            }
        }
    }
    
    private void searchAirport() {
        System.out.println("\n[SEARCH] === Search for an Airport ===");
        
        if (flightNetwork.getAirportCount() < 2) {
            System.out.println("[ERROR] Need at least 2 airports to search for paths.");
            return;
        }
        
        // Show available airports with numbers
        flightNetwork.displayAirportsForSelection();
        System.out.println();
        
        // Select source airport
        System.out.print("Select source airport (enter number): ");
        String sourceChoice = scanner.nextLine().trim();
        
        if (!InputValidator.isValidChoice(sourceChoice, 1, flightNetwork.getAirportCount())) {
            System.out.println("[ERROR] Invalid selection for source airport.");
            return;
        }
        
        String source = flightNetwork.getAirportByIndex(Integer.parseInt(sourceChoice));
        
        // Select destination airport
        System.out.print("Select destination airport (enter number): ");
        String destChoice = scanner.nextLine().trim();
        
        if (!InputValidator.isValidChoice(destChoice, 1, flightNetwork.getAirportCount())) {
            System.out.println("[ERROR] Invalid selection for destination airport.");
            return;
        }
        
        String destination = flightNetwork.getAirportByIndex(Integer.parseInt(destChoice));
        
        if (source == null || destination == null) {
            System.out.println("[ERROR] Invalid airport selection.");
            return;
        }
        
        if (source.equals(destination)) {
            System.out.println("[ERROR] Source and destination cannot be the same airport.");
            return;
        }
        
        List<String> path = flightNetwork.findPath(source, destination);
        
        System.out.println("\nSearch Results:");
        if (path.isEmpty()) {
            System.out.println("[NO PATH] No flight path found between " + source + " and " + destination + ".");
        } else {
            System.out.println("[SUCCESS] Flight path found:");
            System.out.print("   ");
            for (int i = 0; i < path.size(); i++) {
                System.out.print(path.get(i));
                if (i < path.size() - 1) {
                    System.out.print(" -> ");
                }
            }
            System.out.println();
            System.out.println("Number of stops: " + (path.size() - 1));
            
            // Highlight path in GUI if it's open
            if (guiWindow != null && guiWindow.isVisible()) {
                guiWindow.showPathHighlight(source, destination);
                System.out.println("[GUI] Path highlighted in GUI window!");
            }
        }
        
        // Also perform BFS traversal from source
        if (flightNetwork.containsAirport(source)) {
            System.out.println();
            flightNetwork.bfsTraversal(source);
        }
    }
    
    public static void main(String[] args) {
        MalaysiaAirlineSystem system = new MalaysiaAirlineSystem();
        system.run();
    }
}