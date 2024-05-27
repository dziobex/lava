package org.maze;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

/* keeps and provides given maze [structure] */
/* maybe singleton? need to check */

public class Maze {
    int[][] maze;
    int width, height;
    Point start, end;

    public Maze(int[][] maze, int width, int height) {
        this.maze = maze;
        this.width = width;
        this.height = height;
        this.start = null;
        this.end = null;
    }

    public Maze(int[][] maze, int width, int height, Point start, Point end) {
        this(maze, width, height);
        this.start = start;
        this.end = end;
    }

    public int getCell(int x, int y) { return this.maze[y][x]; }

    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }

    public Point getStartLocation() { return this.start; }
    public Point getEndLocation() { return this.end; }

    public void setStartLocation(Point newStartLocation) { this.start = newStartLocation; }
    public void setEndLocation(Point newEndLocation) { this.end = newEndLocation; }

    public void clearPath() {
        for ( int y = 0; y < height; ++y )
            for ( int x = 0; x < width; ++x )
                if ( maze[y][x] == 3 )
                    maze[y][x] = 0;
    }

    /* SOLVING MODULES */

    public void solveMaze() {
        // delete all the path from the maze
        clearPath();

        if (bfs()) {
            JOptionPane.showMessageDialog(null, "Znaleziono ścieżkę :)", "Hurra!", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Path found!");
        } else {
            JOptionPane.showMessageDialog(null, "Nie znaleziono ścieżki :(", "O nie!", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("No path found.");
        }
    }

    // change to A* / J* if you want

    private boolean bfs() {
        int rows = height;
        int cols = width;
        boolean[][] visited = new boolean[rows][cols];
        Point[][] parent = new Point[rows][cols];

        Queue<Point> queue = new LinkedList<>();
        queue.add(start);
        visited[start.y][start.x] = true;

        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

        while (!queue.isEmpty()) {
            Point current = queue.poll();
            int currX = current.x;
            int currY = current.y;

            if (currX == end.x && currY == end.y) {
                markPath(parent, currX, currY);
                return true;
            }

            for (int[] dir : directions) {
                int newX = currX + dir[0];
                int newY = currY + dir[1];

                if ( isValidMove(newX, newY, rows, cols, visited) ) {
                    queue.add(new Point(newX, newY));
                    visited[newY][newX] = true;
                    parent[newY][newX] = current;
                }
            }
        }

        return false;
    }

    private boolean isValidMove(int x, int y, int rows, int cols, boolean[][] visited) {
        return (x == end.x && y == end.y) || (x >= 0 && x < cols && y >= 0 && y < rows && maze[y][x] == 0 && !visited[y][x]);
    }

    private void markPath(Point[][] parent, int x, int y) {
        while ( !(x == start.x && y == start.y) ) {
            maze[y][x] = 3;
            Point p = parent[y][x];
            x = p.x;
            y = p.y;
        }
    }
}
