package org.maze;

/* keeps and provides given maze [structure] */
public class Maze {
    private int[][] maze;
    int width, height;

    public Maze(int[][] maze, int width, int height) {
        this.maze = maze;
        this.width = width;
        this.height = height;
    }

    public int getCell(int x, int y) {
        return this.maze[x][y];
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
