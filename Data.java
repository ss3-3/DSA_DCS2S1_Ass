package Airline;

import java.util.*;

public class Data {
    // Map of airport name to Airport object
    public Map<String, Airport> airports = new HashMap<>();
    
    // List of all flight paths
    public List<FlightPath> flightPaths = new ArrayList<>();
    
    // Airport objects
    public Airport kualaLumpur = new Airport("Kuala Lumpur");
    public Airport langkawi = new Airport("Langkawi");
    public Airport alorSetar = new Airport("Alor Setar");
    public Airport kotaBharu = new Airport("Kota Bharu");
    public Airport kualaTerengganu = new Airport("Kuala Terengganu");
    public Airport kuantan = new Airport("Kuantan");
    public Airport johorBahru = new Airport("Johor Bahru");
    public Airport penang = new Airport("Penang");
    public Airport kuching = new Airport("Kuching");
    public Airport sibu = new Airport("Sibu");
    public Airport bintulu = new Airport("Bintulu");
    public Airport miri = new Airport("Miri");
    public Airport kotaKinabalu = new Airport("Kota Kinabalu");
    public Airport sandakan = new Airport("Sandakan");
    public Airport tawau = new Airport("Tawau");
    public Airport labuan = new Airport("Labuan");
    public Airport bandarSeriBegawan = new Airport("Bandar Seri Begawan");
    public Airport singapore = new Airport("Singapore");
    
    // List of all airports for easy access
    public List<Airport> allAirports = Arrays.asList(
        kualaLumpur, langkawi, alorSetar, kotaBharu, kualaTerengganu, 
        kuantan, johorBahru, penang, kuching, sibu, bintulu, 
        miri, kotaKinabalu, sandakan, tawau, labuan, 
        bandarSeriBegawan, singapore
    );
    
    public Data() {
        // Initialize airports map
        for (Airport airport : allAirports) {
            airports.put(airport.getCityName(), airport);
        }
        
        // KL main hub connections
        List<String> klDestinations = Arrays.asList(
            "Langkawi", "Alor Setar", "Kota Bharu", "Kuala Terengganu", 
            "Kuantan", "Johor Bahru", "Penang", "Kuching", "Sibu", 
            "Bintulu", "Miri", "Kota Kinabalu", "Sandakan", "Tawau", 
            "Labuan", "Bandar Seri Begawan", "Singapore"
        );
        
        // Add KL outbound connections and flight paths
        for (String destination : klDestinations) {
            kualaLumpur.addConnection(destination);
            flightPaths.add(new FlightPath("Kuala Lumpur", destination));
            
            // Add return connections to KL
            airports.get(destination).addConnection("Kuala Lumpur");
            flightPaths.add(new FlightPath(destination, "Kuala Lumpur"));
        }
        
        // Kuching regional routes
        addBidirectionalConnection("Kuching", "Sibu");
        addBidirectionalConnection("Kuching", "Bintulu");
        addBidirectionalConnection("Kuching", "Miri");
        addBidirectionalConnection("Bintulu", "Miri");
        
        // Sabah routes from Kota Kinabalu
        addBidirectionalConnection("Kota Kinabalu", "Sandakan");
        addBidirectionalConnection("Kota Kinabalu", "Tawau");
        addBidirectionalConnection("Kota Kinabalu", "Labuan");
        
        // Miri ↔ Labuan
        addBidirectionalConnection("Miri", "Labuan");
    }
    
    // Helper method to add bidirectional connections
    private void addBidirectionalConnection(String city1, String city2) {
        // Add connections in both airports
        airports.get(city1).addConnection(city2);
        airports.get(city2).addConnection(city1);
        
        // Add flight paths in both directions
        flightPaths.add(new FlightPath(city1, city2));
        flightPaths.add(new FlightPath(city2, city1));
    }
    
    // Utility method to print all airports and their connections
    public void printAllAirports() {
        System.out.println("=== Airport Connections ===");
        for (Airport airport : allAirports) {
            System.out.println(airport);
        }
    }
    
    // Utility method to print all flight paths
    public void printAllFlightPaths() {
        System.out.println("\n=== Flight Paths ===");
        for (FlightPath path : flightPaths) {
            System.out.println(path);
        }
    }
}
