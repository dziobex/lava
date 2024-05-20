package org.maze;

import java.awt.*;

/* keeps and provides given maze [structure] */
public class Maze {
    private int[][] maze;
    private int width, height;

    Point start, end;

    public Maze(int[][] maze, int width, int height) {
        this.maze = maze;
        this.width = width;
        this.height = height;
        this.start = null;
        this.end = null;
    }

    public Maze(int[][] maze, int width, int height, Point start, Point end) {
        this.maze = maze;
        this.width = width;
        this.height = height;
        this.start = start;
        this.end = end;
    }

    public int getCell(int x, int y) {
        return this.maze[y][x];
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() { return this.height; }

    public Point getStartLocation() { return this.start; }
    public Point getEndLocation() { return this.end; }
    public void setStartLocation(Point newStartLocation) { this.start = newStartLocation; }
    public void setEndLocation(Point newEndLocation) { this.end = newEndLocation; }
}
