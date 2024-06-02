package org.maze;

import java.awt.*;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;

public class TextSaver implements Saver {

    @Override
    public Saver.SaveResult Save(File out) {
        int direction_steps = 0;
        Point current_cell, alter_cell;
        String solution_path = "", printable = "";
        char direction = 'O', formerDirection = 'O';

        //Might not have a maze loaded. Throw a warning if that's the case.
        //Loaded mazes are vetted, so there HAS to be a start point
        if (Maze.getInstance().getStartLocation() == null) return SaveResult.NO_MAZE;

        //If there's no solution, solve the maze!
        Maze.getInstance().solveMaze();

        //Make a new file to save to,
        try (PrintWriter output = new PrintWriter(out)) {
            current_cell = Maze.getInstance().getEndLocation();
            alter_cell = current_cell;
            output.println("START");

            //Then within:
            //Write the correct path backwards
            while (current_cell != Maze.getInstance().getStartLocation()) {
                current_cell = Maze.getInstance().parent[current_cell.y][current_cell.x];

                if (current_cell.y - alter_cell.y == 1) solution_path += "N";
                else if (current_cell.y - alter_cell.y == -1) solution_path += "S";
                else if (current_cell.x - alter_cell.x == 1) solution_path += "E";
                else if (current_cell.x - alter_cell.x == -1) solution_path += "W";

                alter_cell = current_cell;
            }

            //Reverse it, so it's the actual path forwards
            solution_path = new StringBuilder(solution_path).reverse().toString();

            //And then re-traverse and print to file
            for(char piece : solution_path.toCharArray()) {
                direction = piece;
                if (direction != formerDirection && formerDirection != 'O') {
                    output.println("FORWARD " + direction_steps);
                    switch(direction) {
                        default:
                        case 'N':
                            printable = (formerDirection == 'W' ? "TURN LEFT" : "TURN RIGHT");
                            break;
                        case 'S':
                            printable = (formerDirection == 'E' ? "TURN LEFT" : "TURN RIGHT");
                            break;
                        case 'E':
                            printable = (formerDirection == 'N' ? "TURN LEFT" : "TURN RIGHT");
                            break;
                        case 'W':
                            printable = (formerDirection == 'S' ? "TURN LEFT" : "TURN RIGHT");
                            break;
                    }
                    output.println(printable);
                    direction_steps = 0;
                }
                ++direction_steps;
                formerDirection = piece;
            }

            //Print the last step too!
            output.println("FORWARD " + direction_steps + "\nSTOP");


        } catch (IOException e) {
            return SaveResult.NO_SPACE;
        }
        return SaveResult.SUCCESS;
    }
}
