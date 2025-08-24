package Airline;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

// Main GUI Panel for graph visualization
class GraphVisualizationPanel extends JPanel {
    private MalaysiaAirlineGraph graph;
    private Map<String, GraphNode> nodePositions;
    private GraphNode draggedNode;
    private Point dragOffset;
    private String highlightedNode;
    private List<String> pathHighlight;
    private Random random;
    
    public GraphVisualizationPanel(MalaysiaAirlineGraph graph) {
        this.graph = graph;
        this.nodePositions = new HashMap<>();
        this.random = new Random();
        this.pathHighlight = new ArrayList<>();
        
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 600));
        
        // Add mouse listeners for dragging nodes
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (GraphNode node : nodePositions.values()) {
                    if (node.contains(e.getX(), e.getY())) {
                        draggedNode = node;
                        dragOffset = new Point(e.getX() - node.x, e.getY() - node.y);
                        highlightedNode = node.name;
                        repaint();
                        break;
                    }
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                draggedNode = null;
                dragOffset = null;
                highlightedNode = null;
                repaint();
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedNode != null && dragOffset != null) {
                    draggedNode.x = e.getX() - dragOffset.x;
                    draggedNode.y = e.getY() - dragOffset.y;
                    
                    // Keep nodes within bounds
                    draggedNode.x = Math.max(GraphNode.RADIUS, Math.min(getWidth() - GraphNode.RADIUS, draggedNode.x));
                    draggedNode.y = Math.max(GraphNode.RADIUS, Math.min(getHeight() - GraphNode.RADIUS, draggedNode.y));
                    
                    repaint();
                }
            }
        });
    }
    
    public void updateGraph() {
        // Remove nodes that no longer exist
        Set<String> currentAirports = graph.getAllAirports();
        nodePositions.keySet().retainAll(currentAirports);
        
        // Add new nodes with random positions
        for (String airport : currentAirports) {
            if (!nodePositions.containsKey(airport)) {
                int x = GraphNode.RADIUS + random.nextInt(Math.max(100, getWidth() - 2 * GraphNode.RADIUS));
                int y = GraphNode.RADIUS + random.nextInt(Math.max(100, getHeight() - 2 * GraphNode.RADIUS));
                nodePositions.put(airport, new GraphNode(airport, x, y));
            }
        }
        
        repaint();
    }
    
    public void arrangeNodesCircular() {
        Set<String> airports = graph.getAllAirports();
        if (airports.isEmpty()) return;
        
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int radius = Math.min(centerX, centerY) - 100;
        
        List<String> airportList = new ArrayList<>(airports);
        double angleStep = 2 * Math.PI / airportList.size();
        
        for (int i = 0; i < airportList.size(); i++) {
            double angle = i * angleStep;
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));
            
            String airport = airportList.get(i);
            if (nodePositions.containsKey(airport)) {
                nodePositions.get(airport).x = x;
                nodePositions.get(airport).y = y;
            }
        }
        
        repaint();
    }
    
    public void highlightPath(List<String> path) {
        this.pathHighlight = new ArrayList<>(path);
        repaint();
    }
    
    public void clearPathHighlight() {
        this.pathHighlight.clear();
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("Malaysia Airlines Flight Network", 10, 25);
        
        // Draw edges (flight paths) by checking actual connections
        g2d.setStroke(new BasicStroke(2));
        Set<String> drawnEdges = new HashSet<>();
        
        for (String airport : graph.getAllAirports()) {
            GraphNode sourceNode = nodePositions.get(airport);
            if (sourceNode == null) continue;
            
            // Get the actual connected airports from your Airport class
            try {
                // We need to access the connected airports somehow
                // Since we can't directly access the Airport object, we'll use findPath to check connections
                for (String otherAirport : graph.getAllAirports()) {
                    if (airport.equals(otherAirport)) continue;
                    
                    GraphNode destNode = nodePositions.get(otherAirport);
                    if (destNode == null) continue;
                    
                    String edgeKey1 = airport + "-" + otherAirport;
                    String edgeKey2 = otherAirport + "-" + airport;
                    
                    if (!drawnEdges.contains(edgeKey1) && !drawnEdges.contains(edgeKey2)) {
                        // Check if direct connection exists by finding path
                        List<String> testPath = graph.findPath(airport, otherAirport);
                        if (testPath.size() == 2) { // Direct connection (source -> destination)
                            // Check if this edge is in the highlighted path
                            boolean isPathEdge = false;
                            for (int i = 0; i < pathHighlight.size() - 1; i++) {
                                if ((pathHighlight.get(i).equals(airport) && pathHighlight.get(i + 1).equals(otherAirport)) ||
                                    (pathHighlight.get(i).equals(otherAirport) && pathHighlight.get(i + 1).equals(airport))) {
                                    isPathEdge = true;
                                    break;
                                }
                            }
                            
                            if (isPathEdge) {
                                g2d.setColor(Color.RED);
                                g2d.setStroke(new BasicStroke(4));
                            } else {
                                g2d.setColor(Color.GRAY);
                                g2d.setStroke(new BasicStroke(2));
                            }
                            
                            g2d.drawLine(sourceNode.x, sourceNode.y, destNode.x, destNode.y);
                            drawnEdges.add(edgeKey1);
                            drawnEdges.add(edgeKey2);
                        }
                    }
                }
            } catch (Exception e) {
                // Handle any exceptions silently
            }
        }
        
        // Draw nodes (airports)
        for (GraphNode node : nodePositions.values()) {
            boolean isHighlighted = node.name.equals(highlightedNode) || pathHighlight.contains(node.name);
            node.draw(g2d, isHighlighted);
        }
        
        // Draw instructions
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString("Drag airports to rearrange them", 10, getHeight() - 20);
        
        g2d.dispose();
    }
}
