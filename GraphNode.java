package Airline;
import java.awt.*;

// Node class to represent airport positions in GUI
public class GraphNode {
    public String name;
    public int x, y;
    public static final int RADIUS = 25;
    
    public GraphNode(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }
    
    public boolean contains(int px, int py) {
        return Math.sqrt(Math.pow(px - x, 2) + Math.pow(py - y, 2)) <= RADIUS;
    }
    
    public void draw(Graphics2D g2d, boolean isHighlighted) {
        // Draw airport circle
        if (isHighlighted) {
            g2d.setColor(new Color(255, 215, 0)); // Gold for highlighted
        } else {
            g2d.setColor(new Color(70, 130, 180)); // Steel blue
        }
        g2d.fillOval(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2);
        
        // Draw border
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2);
        
        // Draw airport name
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(name);
        int textHeight = fm.getHeight();
        
        // Draw white background for text
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x - textWidth/2 - 2, y + RADIUS + 5, textWidth + 4, textHeight);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x - textWidth/2 - 2, y + RADIUS + 5, textWidth + 4, textHeight);
        
        // Draw text
        g2d.drawString(name, x - textWidth/2, y + RADIUS + 5 + fm.getAscent());
    }
}

