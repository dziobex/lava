package org.maze;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BinarySaver implements Saver {

    @Override
    public Saver.SaveResult Save(File out) {
        int build_step_count = 0, direction_steps = 0, walk_counter = 0;
        char char_brick = '&', formerBrick = '&';
        ByteBuffer resizable = ByteBuffer.allocate(4);
        Point current_cell, alter_cell;
        String solution_path = "", printable = "";
        char direction = 'O', formerDirection = 'O';

        //Horrendously cursed
        ArrayList<Point> solution_walks = new ArrayList<>();
        ArrayList<Triplet<Byte, Byte, Byte>> build_bricks = new ArrayList<>();

        //Might not have a maze loaded. Throw a warning if that's the case.
        //Loaded mazes are vetted, so there HAS to be a start point
        if (Maze.getInstance().getStartLocation() == null) return SaveResult.NO_MAZE;

        //If there's no solution, solve the maze!
        Maze.getInstance().solveMaze();

        //Time for Binary Building.

        try (FileOutputStream output = new FileOutputStream(out)) {
            current_cell = Maze.getInstance().getEndLocation();
            alter_cell = current_cell;

            //Identifier + Escape Sign
            output.write(new byte[]{0x43, 0x42, 0x52, 0x52, 0x1B});
            //Maze Dimensions
            output.write(resizable.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short)Maze.getInstance().getHeight()).array());
            output.write(resizable.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short)Maze.getInstance().getWidth()).array());
            //Entry
            output.write(resizable.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short)(Maze.getInstance().getStartLocation().x + 1)).array());
            output.write(resizable.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short)(Maze.getInstance().getStartLocation().y + 1)).array());
            //Exit
            output.write(resizable.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short)(Maze.getInstance().getEndLocation().x + 1)).array());
            output.write(resizable.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short)(Maze.getInstance().getEndLocation().y + 1)).array());
            //Reserved
            for (int i = 0; i < 3; ++i) output.write(new byte[]{(byte)0xFF, 0x00, 0x00, 0x00});

            //That's the initials. Time to preprocess everything...
            //[1/2] The Maze's Build
            for ( int y = 0; y < Maze.getInstance().getHeight(); ++y ) {
                for (int x = 0; x < Maze.getInstance().getWidth(); ++x) {
                    char_brick = (Maze.getInstance().getCell(x, y) == 1 ? 'X' : ' ');

                    if (build_step_count == 256 || (char_brick != formerBrick && formerBrick != '&')) {
                        build_bricks.add(new Triplet<Byte, Byte, Byte>((byte) '#', (byte) formerBrick, (byte)(build_step_count - 1)));
                        build_step_count = 0;
                    }

                    ++build_step_count;
                    formerBrick = char_brick;
                }

                if (build_step_count > 0) {
                    build_bricks.add(new Triplet<Byte, Byte, Byte>((byte) '#', (byte) char_brick, (byte) (build_step_count - 1)));
                    build_step_count = 0;
                    formerBrick = '&';
                }
            }
            //Counter
            output.write(resizable.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(build_bricks.size()).array());
            //Offset
            output.write(resizable.allocate(4).putInt(1).array());
            //Separator, Wall, Path
            output.write(new byte[]{(byte)'#', (byte)'X', (byte)' '});
            //The entire maze...
            for (Triplet t : build_bricks) {
                output.write(new byte[]{(byte)t.getFirst(), (byte)t.getSecond(), (byte)t.getThird()});
            }

            //[2/2] The Maze's Solution
            //Necessitas
            output.write(new byte[]{0x43, 0x42, 0x52, 0x52});

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

            //Traverse it and make it an arrayList of actual steps
            for(char piece : solution_path.toCharArray()) {
                direction = piece;
                if (direction_steps == 256 || (direction != formerDirection && formerDirection != 'O')) {
                    solution_walks.add(new Point((int)formerDirection, (byte)(direction_steps - 1)));
                    direction_steps = 0;
                }
                ++direction_steps;
                formerDirection = piece;
            }
            solution_walks.add(new Point((int)formerDirection, direction_steps - 1));

            //Back to printing binary!
            //Counter of steps
            output.write(resizable.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short)(solution_walks.size())).array());

            //Steps!
            for(Point fake_p : solution_walks) {
                output.write(new byte[]{(byte)fake_p.x, (byte)fake_p.y});
            }

            //and we're done.
        } catch (IOException e) {
            return SaveResult.NO_SPACE;
        }

        return SaveResult.SUCCESS;
    }
}
