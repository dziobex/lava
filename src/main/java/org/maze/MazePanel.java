package org.maze;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class MazePanel extends JPanel {
    private Maze maze;
    private BufferedImage mazeImage;
    public static int cellSize = 10;

    public MazePanel(Maze maze) {
        this.maze = maze;
        setLayout(new BorderLayout());
        createSnapshot();
    }

    void createSnapshot() {
        int width = maze.getHeight() * cellSize,
            height = maze.getWidth() * cellSize;

        //System.out.println(String.format("%d x %d", width, height));
        mazeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = mazeImage.createGraphics();
        drawMaze(g2d);
        g2d.dispose();
    }

    private void drawMaze(Graphics g) {
        for (int y = 0; y < maze.getHeight(); ++y) {
            for (int x = 0; x < maze.getWidth(); ++x) {
                int cell = maze.getCell(x, y);

                if ( maze.getStartLocation().x == x && maze.getStartLocation().y == y ) {
                    g.setColor(Color.green);
                } else if ( maze.getEndLocation().x == x && maze.getEndLocation().y == y ) {
                    g.setColor(Color.red);
                } else {
                    g.setColor(cell == 1 ? Color.BLACK : Color.WHITE);
                }
                g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mazeImage != null)
            g.drawImage(mazeImage, 0, 0, this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(maze.getHeight() * cellSize, maze.getWidth() * cellSize);
    }

    public void setStartLocation(Point xypos) {
        maze.setStartLocation(xypos);
        createSnapshot();
        repaint();
    }

    public void setEndLocation(Point xypos) {
        maze.setEndLocation(xypos);
        createSnapshot();
        repaint();
    }

    public Maze getMaze() { return this.maze; }
}
