package org.maze;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextSaver implements Saver {
    private Maze maze;

    public TextSaver() {
        this.maze = null;
    }

    @Override
    public Saver.SaveResult Save(File out) {
        int width = 0, height = 0, direction_steps = 0;
        Point current_cell;
        char direction = 'O';

        //Make a new file to save to,
        try (PrintWriter output = new PrintWriter(out)) {
            current_cell = Maze.getInstance().getStartLocation();
            output.println("START\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Start tracking the path - we're going to go ^ > v <
        while (!current_cell.equals(Maze.getInstance().getEndLocation())) {
            current_cell = Maze.getInstance().getEndLocation();
        }

        return SaveResult.SUCCESS;
    }

    private boolean isWalkable(Point cell) {
        return (cell.x >= 0 && cell.x < Maze.getInstance().getWidth() && cell.y >= 0 && cell.y < Maze.getInstance().getHeight() && Maze.getInstance().maze[cell.y][cell.x] == 0);
    }
}
