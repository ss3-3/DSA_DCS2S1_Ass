package Airline;
import java.util.*;

public class Airport {
    private String cityName;
    private Set<String> connectedAirports;
    
    public Airport(String cityName) {
        this.cityName = cityName;
        this.connectedAirports = new HashSet<>();
    }
    
    public String getCityName() {
        return cityName;
    }
    
    public Set<String> getConnectedAirports() {
        return connectedAirports;
    }
    
    public void addConnection(String airport) {
        connectedAirports.add(airport);
    }
    
    public boolean removeConnection(String airport) {
        return connectedAirports.remove(airport);
    }
    
    @Override
    public String toString() {
        if (connectedAirports.isEmpty()) {
            return cityName + " -> [No connections]";
        }
        return cityName + " -> " + connectedAirports.toString();
    }
}