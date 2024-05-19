package org.maze;
import javax.swing.*;
import java.awt.*;

/* component that draws the maze */

public class MazePanel extends JPanel {
    private Maze maze;

    private static int cellSize = 10;

    public MazePanel(Maze maze) {
        this.maze = maze;
    }

    @Override
    protected void paintComponent(Graphics g) {
        System.out.println("h");
        super.paintComponent(g);
        drawMaze(g);
    }

    private void drawMaze(Graphics g) {
        for (int x = 0; x < maze.getHeight(); ++x) {
            for (int y = 0; y < maze.getWidth(); ++y) {
                g.setColor(maze.getCell(x, y) == 1 ? Color.BLACK : Color.WHITE);
                g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(maze.getWidth() * cellSize, maze.getHeight() * cellSize);
    }
}
