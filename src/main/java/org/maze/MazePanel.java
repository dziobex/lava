package org.maze;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MazePanel extends JPanel {
    private Maze maze;
    private BufferedImage mazeImage;
    private static int cellSize = 10;

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mazeImage != null)
            g.drawImage(mazeImage, 0, 0, this);
    }

    private void drawMaze(Graphics g) {
        for (int x = 0; x < maze.getHeight(); ++x) {
            for (int y = 0; y < maze.getWidth(); ++y) {
                int cell = maze.getCell(x, y);

                g.setColor(cell == 1 ? Color.BLACK : cell == 2 ? Color.GREEN : cell == 3 ? Color.RED : Color.WHITE);
                g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(maze.getHeight() * cellSize, maze.getWidth() * cellSize);
    }
}
