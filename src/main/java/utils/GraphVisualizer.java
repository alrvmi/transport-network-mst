package utils;

import graph.Edge;
import graph.Graph;
import models.MSTResult;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;

public class GraphVisualizer {

    private static class Point2D {
        double x, y;
        Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void visualizeGraph(int graphId, Graph graph, MSTResult mstResult, String outputDir) {
        int width = 1200;
        int height = 900;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);

        List<String> nodes = graph.getNodes();
        Map<String, Point2D> positions = calculatePositions(nodes, width, height);

        Set<String> mstEdgeKeys = new HashSet<>();
        if (mstResult != null) {
            for (Edge edge : mstResult.getMstEdges()) {
                mstEdgeKeys.add(getEdgeKey(edge.getFrom(), edge.getTo()));
            }
        }

        drawEdges(g2d, graph, positions, mstEdgeKeys);
        drawNodes(g2d, nodes, positions);
        drawTitle(g2d, graphId, graph, mstResult, width);

        g2d.dispose();

        try {
            File outputFile = new File(outputDir, String.format("graph_%02d.png", graphId));
            outputFile.getParentFile().mkdirs();
            ImageIO.write(image, "PNG", outputFile);
            System.out.println("Saved: " + outputFile.getPath());
        } catch (Exception e) {
            System.err.println("Error saving image: " + e.getMessage());
        }
    }

    private static Map<String, Point2D> calculatePositions(List<String> nodes, int width, int height) {
        Map<String, Point2D> positions = new HashMap<>();
        int n = nodes.size();

        double centerX = width / 2.0;
        double centerY = height / 2.0;
        double radius = Math.min(width, height) * 0.35;

        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n - Math.PI / 2;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            positions.put(nodes.get(i), new Point2D(x, y));
        }

        return positions;
    }

    private static void drawEdges(Graphics2D g2d, Graph graph, Map<String, Point2D> positions, Set<String> mstEdgeKeys) {
        g2d.setStroke(new BasicStroke(2));

        for (Edge edge : graph.getAllEdges()) {
            Point2D from = positions.get(edge.getFrom());
            Point2D to = positions.get(edge.getTo());

            boolean isMST = mstEdgeKeys.contains(getEdgeKey(edge.getFrom(), edge.getTo()));

            if (isMST) {
                g2d.setColor(new Color(0, 150, 0));
                g2d.setStroke(new BasicStroke(4));
            } else {
                g2d.setColor(new Color(200, 200, 200));
                g2d.setStroke(new BasicStroke(2));
            }

            g2d.drawLine((int)from.x, (int)from.y, (int)to.x, (int)to.y);

            int midX = (int)((from.x + to.x) / 2);
            int midY = (int)((from.y + to.y) / 2);

            g2d.setColor(isMST ? new Color(0, 100, 0) : Color.GRAY);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            String weightStr = String.valueOf(edge.getWeight());
            g2d.drawString(weightStr, midX - 10, midY - 5);
        }
    }

    private static void drawNodes(Graphics2D g2d, List<String> nodes, Map<String, Point2D> positions) {
        int nodeRadius = 25;
        g2d.setFont(new Font("Arial", Font.BOLD, 14));

        for (String node : nodes) {
            Point2D pos = positions.get(node);
            int x = (int)pos.x;
            int y = (int)pos.y;

            g2d.setColor(new Color(70, 130, 180));
            g2d.fillOval(x - nodeRadius, y - nodeRadius, nodeRadius * 2, nodeRadius * 2);

            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(x - nodeRadius, y - nodeRadius, nodeRadius * 2, nodeRadius * 2);

            g2d.setColor(Color.WHITE);
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(node);
            int textHeight = fm.getAscent();
            g2d.drawString(node, x - textWidth / 2, y + textHeight / 3);
        }
    }

    private static void drawTitle(Graphics2D g2d, int graphId, Graph graph, MSTResult mstResult, int width) {
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));

        String title = String.format("Graph #%d - Vertices: %d, Edges: %d",
                graphId, graph.getVertexCount(), graph.getEdgeCount());
        g2d.drawString(title, 20, 30);

        if (mstResult != null) {
            g2d.setFont(new Font("Arial", Font.PLAIN, 16));
            String mstInfo = String.format("MST Cost: %d, MST Edges: %d",
                    mstResult.getTotalCost(), mstResult.getMstEdges().size());
            g2d.drawString(mstInfo, 20, 55);
        }

        g2d.setFont(new Font("Arial", Font.ITALIC, 12));
        g2d.setColor(new Color(0, 150, 0));
        g2d.drawString("Green = MST edges", 20, 80);
        g2d.setColor(Color.GRAY);
        g2d.drawString("Gray = Non-MST edges", 150, 80);
    }

    private static String getEdgeKey(String from, String to) {
        if (from.compareTo(to) < 0) {
            return from + "-" + to;
        } else {
            return to + "-" + from;
        }
    }
}