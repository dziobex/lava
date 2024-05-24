package org.maze;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class MazeLoader {
    public static enum LoadResult {
        SUCCESS,
        BAD_DIMS,
        ILLEGAL_DIMS,
        BAD_CHARS
    };

    private Maze maze;

    public MazeLoader() {
        maze = null;
    }

    public LoadResult LoadText(File in) {
        int width = -1, height = 0;
        List<String> lines = new ArrayList<String>();

        try(BufferedReader br = new BufferedReader(new FileReader(in))) {
            for(String line; (line = br.readLine()) != null; ) {
                if ( width == -1)
                    width = line.length();
                else if ( width != line.length() )
                    return LoadResult.BAD_DIMS;
                if ( line.length() > 0 ) {
                    lines.add(line);
                    ++height;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if ( width < 3 || width > 2049 || height < 3 || height > 2049 )
            return LoadResult.BAD_DIMS;

        // put the maze into the proper format
        int[][] mazeData = new int[2050][2050];
        Point startPos = null, endPos = null;
        for ( int y = 0; y < height; ++y ) {
            for ( int x = 0; x < width; ++x ) {
                char getChar = lines.get(y).charAt(x);

                if ( getChar == 'P' )
                    startPos = new Point(x, y);
                else if ( getChar == 'K')
                    endPos = new Point(x, y);
                else if ( getChar == ' ' || getChar == 'X' )
                    mazeData[y][x] = getChar == ' ' ? 0 : 1;
                else
                    return LoadResult.BAD_CHARS;
            }
        }

        this.maze = new Maze(mazeData, width, height, startPos, endPos);

        //System.out.println(String.format("%d, %d", width, height));
        return LoadResult.SUCCESS;
    }

    public Maze GetMaze() { return this.maze; }
}
