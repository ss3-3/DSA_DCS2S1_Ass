package Airline;

import java.util.*;

public class MalaysiaAirlineGraph {
    private Map<String, Airport> airports;
    
    public MalaysiaAirlineGraph() {
        this.airports = new HashMap<>();
    }
    
    // Add vertex (airport)
    public boolean addVertex(String cityName) {
        String formattedName = InputValidator.validateAndFormatCityName(cityName);
        
        if (formattedName == null) {
            System.out.println("Invalid city name! City name cannot be empty or contain only spaces.");
            return false;
        }
        
        if (!InputValidator.isValidCityName(formattedName)) {
            System.out.println("Invalid city name! Please use only letters and spaces (minimum 2 characters).");
            return false;
        }
        
        if (airports.containsKey(formattedName)) {
            System.out.println("Airport " + formattedName + " already exists!");
            return false;
        }
        
        airports.put(formattedName, new Airport(formattedName));
        System.out.println("Airport " + formattedName + " has been added successfully.");
        return true;
    }
    
    // Remove vertex (airport)
    public boolean removeVertex(String cityName) {
        String formattedName = InputValidator.validateAndFormatCityName(cityName);
        
        if (formattedName == null || !airports.containsKey(formattedName)) {
            System.out.println("Airport " + (formattedName != null ? formattedName : "invalid name") + " does not exist!");
            return false;
        }
        
        // Show connections that will be removed
        Airport airportToRemove = airports.get(formattedName);
        if (!airportToRemove.getConnectedAirports().isEmpty()) {
            System.out.println("Removing connections from " + formattedName + " to: " + 
                             airportToRemove.getConnectedAirports());
        }
        
        // Remove all edges connected to this airport
        for (Airport airport : airports.values()) {
            airport.removeConnection(formattedName);
        }
        
        airports.remove(formattedName);
        System.out.println("Airport " + formattedName + " has been removed successfully.");
        return true;
    }
    
    // Add edge (flight path)
    public boolean addEdge(String source, String destination) {
        String formattedSource = InputValidator.validateAndFormatCityName(source);
        String formattedDest = InputValidator.validateAndFormatCityName(destination);
        
        if (formattedSource == null || formattedDest == null) {
            System.out.println("Invalid airport names! Names cannot be empty.");
            return false;
        }
        
        if (formattedSource.equals(formattedDest)) {
            System.out.println("Cannot create flight path from an airport to itself!");
            return false;
        }
        
        if (!airports.containsKey(formattedSource)) {
            System.out.println("Source airport " + formattedSource + " does not exist!");
            return false;
        }
        if (!airports.containsKey(formattedDest)) {
            System.out.println("Destination airport " + formattedDest + " does not exist!");
            return false;
        }
        
        // Check if edge already exists
        if (airports.get(formattedSource).getConnectedAirports().contains(formattedDest)) {
            System.out.println("Flight path between " + formattedSource + " and " + formattedDest + " already exists!");
            return false;
        }
        
        // Add bidirectional edge (undirected graph)
        airports.get(formattedSource).addConnection(formattedDest);
        airports.get(formattedDest).addConnection(formattedSource);
        
        System.out.println("Flight path created between " + formattedSource + " and " + formattedDest + ".");
        return true;
    }
    
    // Remove edge (flight path)
    public boolean removeEdge(String source, String destination) {
        String formattedSource = InputValidator.validateAndFormatCityName(source);
        String formattedDest = InputValidator.validateAndFormatCityName(destination);
        
        if (formattedSource == null || formattedDest == null) {
            System.out.println("Invalid airport names! Names cannot be empty.");
            return false;
        }
        
        if (!airports.containsKey(formattedSource) || !airports.containsKey(formattedDest)) {
            System.out.println("One or both airports do not exist!");
            return false;
        }
        
        boolean removed1 = airports.get(formattedSource).removeConnection(formattedDest);
        boolean removed2 = airports.get(formattedDest).removeConnection(formattedSource);
        
        if (removed1 && removed2) {
            System.out.println("Flight path between " + formattedSource + " and " + formattedDest + " has been removed.");
            return true;
        } else {
            System.out.println("No flight path exists between " + formattedSource + " and " + formattedDest + ".");
            return false;
        }
    }
    
    // BFS traversal to find path between two airports
    public List<String> findPath(String source, String destination) {
        String formattedSource = InputValidator.validateAndFormatCityName(source);
        String formattedDest = InputValidator.validateAndFormatCityName(destination);
        
        if (formattedSource == null || formattedDest == null ||
            !airports.containsKey(formattedSource) || !airports.containsKey(formattedDest)) {
            return new ArrayList<>();
        }
        
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>();
        
        queue.offer(formattedSource);
        visited.add(formattedSource);
        parent.put(formattedSource, null);
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            
            if (current.equals(formattedDest)) {
                // Reconstruct path
                List<String> path = new ArrayList<>();
                String node = formattedDest;
                while (node != null) {
                    path.add(0, node);
                    node = parent.get(node);
                }
                return path;
            }
            
            for (String neighbor : airports.get(current).getConnectedAirports()) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parent.put(neighbor, current);
                    queue.offer(neighbor);
                }
            }
        }
        
        return new ArrayList<>(); // No path found
    }
    
    // BFS traversal starting from a given airport
    public void bfsTraversal(String startAirport) {
        String formattedStart = InputValidator.validateAndFormatCityName(startAirport);
        
        if (formattedStart == null || !airports.containsKey(formattedStart)) {
            System.out.println("Airport " + (formattedStart != null ? formattedStart : "invalid name") + " does not exist!");
            return;
        }
        
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        
        queue.offer(formattedStart);
        visited.add(formattedStart);
        
        System.out.println("BFS Traversal starting from " + formattedStart + ":");
        
        while (!queue.isEmpty()) {
            String current = queue.poll();
            System.out.print(current + " ");
            
            // Sort neighbors for consistent output
            List<String> neighbors = new ArrayList<>(airports.get(current).getConnectedAirports());
            Collections.sort(neighbors);
            
            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
        System.out.println();
    }
    
    // Display the entire flight network
    public void displayGraph() {
        if (airports.isEmpty()) {
            System.out.println("No airports in the network.");
            return;
        }
        
        System.out.println("\n=== Malaysia Airlines Flight Network ===");
        List<String> sortedAirports = new ArrayList<>(airports.keySet());
        Collections.sort(sortedAirports);
        
        for (String airport : sortedAirports) {
            System.out.println("   " + airports.get(airport));
        }
        System.out.println("==========================================");
    }
    
    // Display available airports with numbers for selection
    public void displayAirportsForSelection() {
        if (airports.isEmpty()) {
            System.out.println("No airports available in the network.");
            return;
        }
        
        List<String> sortedAirports = new ArrayList<>(airports.keySet());
        Collections.sort(sortedAirports);
        
        System.out.println("Available airports:");
        for (int i = 0; i < sortedAirports.size(); i++) {
            System.out.println("   " + (i + 1) + ". " + sortedAirports.get(i));
        }
    }
    
    // Get airport by index (for selection)
    public String getAirportByIndex(int index) {
        List<String> sortedAirports = new ArrayList<>(airports.keySet());
        Collections.sort(sortedAirports);
        
        if (index >= 1 && index <= sortedAirports.size()) {
            return sortedAirports.get(index - 1);
        }
        return null;
    }
    
    // Display available airports (simple list)
    public void displayAvailableAirports() {
        if (airports.isEmpty()) {
            System.out.println("No airports available in the network.");
            return;
        }
        
        List<String> sortedAirports = new ArrayList<>(airports.keySet());
        Collections.sort(sortedAirports);
        
        System.out.print("Available airports: ");
        for (int i = 0; i < sortedAirports.size(); i++) {
            System.out.print(sortedAirports.get(i));
            if (i < sortedAirports.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }
    
    // Display all existing edges with numbers for selection
    public List<String> displayEdgesForSelection() {
        List<String> edgesList = new ArrayList<>();
        
        if (airports.isEmpty()) {
            System.out.println("No airports in the network.");
            return edgesList;
        }
        
        Set<String> displayedEdges = new HashSet<>();
        List<String> sortedAirports = new ArrayList<>(airports.keySet());
        Collections.sort(sortedAirports);
        
        System.out.println("Existing flight paths:");
        for (String airport : sortedAirports) {
            Airport currentAirport = airports.get(airport);
            List<String> connections = new ArrayList<>(currentAirport.getConnectedAirports());
            Collections.sort(connections);
            
            for (String connection : connections) {
                // Avoid duplicate display (since graph is undirected)
                String edge1 = airport + "-" + connection;
                String edge2 = connection + "-" + airport;
                
                if (!displayedEdges.contains(edge1) && !displayedEdges.contains(edge2)) {
                    String edgeDisplay = airport + " <-> " + connection;
                    edgesList.add(airport + "|" + connection); // Store for selection
                    System.out.println("   " + (edgesList.size()) + ". " + edgeDisplay);
                    displayedEdges.add(edge1);
                    displayedEdges.add(edge2);
                }
            }
        }
        
        if (edgesList.isEmpty()) {
            System.out.println("No flight paths exist in the network.");
        }
        
        return edgesList;
    }
    
    // Check if airport exists
    public boolean containsAirport(String cityName) {
        String formattedName = InputValidator.validateAndFormatCityName(cityName);
        return formattedName != null && airports.containsKey(formattedName);
    }
    
    // Get all airports
    public Set<String> getAllAirports() {
        return airports.keySet();
    }
    
    public int getAirportCount() {
        return airports.size();
    }
}