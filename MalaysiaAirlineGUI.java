package Airline;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

// GUI Window class
class MalaysiaAirlineGUI extends JFrame {
    private MalaysiaAirlineGraph flightNetwork;
    private GraphVisualizationPanel graphPanel;
    private JTextArea infoArea;
    
    public MalaysiaAirlineGUI(MalaysiaAirlineGraph graph) {
        this.flightNetwork = graph;
        initializeGUI();
        
        setTitle("Malaysia Airlines Flight Network - GUI Mode");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
    }
    
    private void initializeGUI() {
        graphPanel = new GraphVisualizationPanel(flightNetwork);
        
        setLayout(new BorderLayout());
        add(graphPanel, BorderLayout.CENTER);
        
        // Right panel for information
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(250, 0));
        
        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        JScrollPane scrollPane = new JScrollPane(infoArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Network Info"));
        
        // Control buttons
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Controls"));
        
        JButton arrangeBtn = new JButton("Arrange Circular");
        JButton refreshBtn = new JButton("Refresh Display");
        JButton closeBtn = new JButton("Close GUI");
        
        buttonPanel.add(arrangeBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(closeBtn);
        
        rightPanel.add(buttonPanel, BorderLayout.NORTH);
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(rightPanel, BorderLayout.EAST);
        
        // Event handlers
        arrangeBtn.addActionListener(e -> {
            graphPanel.arrangeNodesCircular();
            updateInfoArea();
        });
        
        refreshBtn.addActionListener(e -> {
            graphPanel.updateGraph();
            if (flightNetwork.getAirportCount() > 0) {
                graphPanel.arrangeNodesCircular(); // Auto arrange after refresh
            }
            updateInfoArea();
        });
        
        closeBtn.addActionListener(e -> {
            dispose();
        });
        
        // Initial setup
        graphPanel.updateGraph();
        if (flightNetwork.getAirportCount() > 0) {
            graphPanel.arrangeNodesCircular(); // Auto arrange on startup
        }
        updateInfoArea();
    }
    
    private void updateInfoArea() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Flight Network ===\n\n");
        
        sb.append("Total Airports: ").append(flightNetwork.getAirportCount()).append("\n\n");
        
        if (flightNetwork.getAirportCount() > 0) {
            sb.append("AIRPORTS:\n");
            List<String> airports = new ArrayList<>(flightNetwork.getAllAirports());
            Collections.sort(airports);
            for (String airport : airports) {
                sb.append("• ").append(airport).append("\n");
            }
            
            sb.append("\nCONNECTIONS:\n");
            Set<String> displayedConnections = new HashSet<>();
            int connectionCount = 0;
            
            for (String airport : airports) {
                for (String otherAirport : airports) {
                    if (!airport.equals(otherAirport)) {
                        String connection1 = airport + "-" + otherAirport;
                        String connection2 = otherAirport + "-" + airport;
                        
                        if (!displayedConnections.contains(connection1) && 
                            !displayedConnections.contains(connection2)) {
                            
                            List<String> testPath = flightNetwork.findPath(airport, otherAirport);
                            if (testPath.size() == 2) {
                                sb.append("• ").append(airport).append(" <-> ").append(otherAirport).append("\n");
                                displayedConnections.add(connection1);
                                displayedConnections.add(connection2);
                                connectionCount++;
                            }
                        }
                    }
                }
            }
            
            if (connectionCount == 0) {
                sb.append("No connections exist yet.\n");
            }
        } else {
            sb.append("No airports in network.\n");
            sb.append("Add airports using the console menu first.\n");
        }
        
        sb.append("\n--- Instructions ---\n");
        sb.append("• Drag airports to move them\n");
        sb.append("• Use console menu to add/remove\n");
        sb.append("• Click 'Refresh Display' after changes\n");
        
        infoArea.setText(sb.toString());
        infoArea.setCaretPosition(0);
    }
    
    public void showPathHighlight(String source, String destination) {
        List<String> path = flightNetwork.findPath(source, destination);
        if (!path.isEmpty()) {
            graphPanel.highlightPath(path);
        }
    }
    
    public void refresh() {
        graphPanel.updateGraph();
        if (flightNetwork.getAirportCount() > 0) {
            graphPanel.arrangeNodesCircular(); // Auto arrange after refresh
        }
        updateInfoArea();
    }
}