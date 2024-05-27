package org.maze;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/* in need of optimization */

public class MazePanel extends JPanel {
    BufferedImage MazeImage;
    public static int cellSize = 10;

    public MazePanel() {

        cellSize = 10;
        while ( Maze.getInstance().getInstance().getWidth() * (cellSize + 1) <= 600 && Maze.getInstance().getInstance().getHeight() * (cellSize + 1) <= 535) {
            ++cellSize;
        }

        setLayout(new BorderLayout());
        createSnapshot();
    }

    public void save(File saveTo) throws IOException {
        if ( MazeImage != null )
            ImageIO.write(MazeImage, "PNG", saveTo);
    }

    void createSnapshot() {

        int width = Maze.getInstance().getHeight() * cellSize,
            height = Maze.getInstance().getWidth() * cellSize;

        //System.out.println(String.format("%d x %d", width, height));
        MazeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = MazeImage.createGraphics();
        drawMaze(g2d);
        g2d.dispose();
    }

    public void refresh() {
        createSnapshot();
        repaint();
    }

    private void drawMaze(Graphics g) {
        for (int y = 0; y < Maze.getInstance().getHeight(); ++y) {
            for (int x = 0; x < Maze.getInstance().getWidth(); ++x) {
                int cell = Maze.getInstance().getCell(x, y);

                if ( Maze.getInstance().getStartLocation().x == x && Maze.getInstance().getStartLocation().y == y ) {
                    g.setColor(Color.green);
                } else if ( Maze.getInstance().getEndLocation().x == x && Maze.getInstance().getEndLocation().y == y ) {
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
        if (MazeImage != null)
            g.drawImage(MazeImage, 0, 0, this);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(Maze.getInstance().getHeight() * cellSize, Maze.getInstance().getWidth() * cellSize);
    }

    public void setStartLocation(Point xypos) {

        Graphics2D g2d = MazeImage.createGraphics();

        //replace
        int cell = Maze.getInstance().getCell(Maze.getInstance().getStartLocation().x, Maze.getInstance().getStartLocation().y);
        g2d.setColor(cell == 0 ? Color.white : Color.BLACK);
        g2d.fillRect(Maze.getInstance().getStartLocation().x * cellSize, Maze.getInstance().getStartLocation().y * cellSize, cellSize, cellSize);
        g2d.setColor(Color.GRAY);
        g2d.drawRect(Maze.getInstance().getStartLocation().x * cellSize, Maze.getInstance().getStartLocation().y * cellSize, cellSize, cellSize);

        g2d.setColor(Color.green);
        g2d.fillRect(xypos.x * cellSize, xypos.y * cellSize, cellSize, cellSize);
        g2d.setColor(Color.GRAY);
        g2d.drawRect(xypos.x * cellSize, xypos.y * cellSize, cellSize, cellSize);
        g2d.dispose();

        Maze.getInstance().setStartLocation(xypos);
        repaint();
    }

    public void setEndLocation(Point xypos) {

        Graphics2D g2d = MazeImage.createGraphics();

        // replace
        int cell = Maze.getInstance().getCell(Maze.getInstance().getEndLocation().x, Maze.getInstance().getEndLocation().y);
        g2d.setColor(cell == 0 ? Color.white : Color.BLACK);
        g2d.fillRect(Maze.getInstance().getEndLocation().x * cellSize, Maze.getInstance().getEndLocation().y * cellSize, cellSize, cellSize);
        g2d.setColor(Color.GRAY);
        g2d.drawRect(Maze.getInstance().getEndLocation().x * cellSize, Maze.getInstance().getEndLocation().y * cellSize, cellSize, cellSize);

        g2d.setColor(Color.RED);
        g2d.fillRect(xypos.x * cellSize, xypos.y * cellSize, cellSize, cellSize);
        g2d.setColor(Color.GRAY);
        g2d.drawRect(xypos.x * cellSize, xypos.y * cellSize, cellSize, cellSize);
        g2d.dispose();

        Maze.getInstance().setEndLocation(xypos);
        repaint();
    }
}
