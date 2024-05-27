package org.maze;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MazePanel extends JPanel {
    Maze maze;
    BufferedImage mazeImage;
    public static int cellSize = 10;

    public MazePanel(Maze maze) {
        this.maze = maze;

        cellSize = 10;
        while ( this.maze.getWidth() * (cellSize + 1) <= 600 && this.maze.getHeight() * (cellSize + 1) <= 535) {
            ++cellSize;
        }

        setLayout(new BorderLayout());
        createSnapshot();
    }

    public void save(File saveTo) throws IOException {
        if ( mazeImage != null )
            ImageIO.write(mazeImage, "PNG", saveTo);
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

    public void refresh() {
        createSnapshot();
        repaint();
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
                    g.setColor(cell == 1 ? Color.BLACK : cell == 3 ? Color.ORANGE : Color.WHITE);
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

        Graphics2D g2d = mazeImage.createGraphics();

        //replace
        int cell = maze.getCell(maze.getStartLocation().x, maze.getStartLocation().y);
        g2d.setColor(cell == 0 ? Color.white : Color.BLACK);
        g2d.fillRect(maze.getStartLocation().x * cellSize, maze.getStartLocation().y * cellSize, cellSize, cellSize);
        g2d.setColor(Color.GRAY);
        g2d.drawRect(maze.getStartLocation().x * cellSize, maze.getStartLocation().y * cellSize, cellSize, cellSize);

        g2d.setColor(Color.green);
        g2d.fillRect(xypos.x * cellSize, xypos.y * cellSize, cellSize, cellSize);
        g2d.setColor(Color.GRAY);
        g2d.drawRect(xypos.x * cellSize, xypos.y * cellSize, cellSize, cellSize);
        g2d.dispose();

        maze.setStartLocation(xypos);
        repaint();
    }

    public void setEndLocation(Point xypos) {

        Graphics2D g2d = mazeImage.createGraphics();

        // replace
        int cell = maze.getCell(maze.getEndLocation().x, maze.getEndLocation().y);
        g2d.setColor(cell == 0 ? Color.white : Color.BLACK);
        g2d.fillRect(maze.getEndLocation().x * cellSize, maze.getEndLocation().y * cellSize, cellSize, cellSize);
        g2d.setColor(Color.GRAY);
        g2d.drawRect(maze.getEndLocation().x * cellSize, maze.getEndLocation().y * cellSize, cellSize, cellSize);

        g2d.setColor(Color.RED);
        g2d.fillRect(xypos.x * cellSize, xypos.y * cellSize, cellSize, cellSize);
        g2d.setColor(Color.GRAY);
        g2d.drawRect(xypos.x * cellSize, xypos.y * cellSize, cellSize, cellSize);
        g2d.dispose();

        maze.setEndLocation(xypos);
        repaint();
    }

    public Maze getMaze() { return this.maze; }
}
