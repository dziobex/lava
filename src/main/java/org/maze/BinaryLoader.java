package org.maze;

import java.awt.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BinaryLoader implements Loader {
    private Maze maze;

    public BinaryLoader() {
        this.maze = null;
    }

    @Override
    public LoadResult Load(File in) {

        try (DataInputStream dis = new DataInputStream(new FileInputStream(in))) {
            // NAGŁÓWEK

            int fileId = dis.readInt();
            int escape = dis.readUnsignedByte();
            int columns = dis.readUnsignedShort();
            int rows = dis.readUnsignedShort();
            int entryX = dis.readUnsignedShort();
            int entryY = dis.readUnsignedShort();
            int exitX = dis.readUnsignedShort();
            int exitY = dis.readUnsignedShort();

            dis.skipBytes(12);// reserved bits

            // coding stuff

            int wordCount = dis.readInt();
            int solutionOffset = dis.readInt();
            int separator = dis.readUnsignedByte();
            int wall = dis.readUnsignedByte();
            int path = dis.readUnsignedByte();

            System.out.println(String.format("%d x %d", rows, columns));

            if (columns <= 0 || rows <= 0)
                return LoadResult.ILLEGAL_DIMS;

            rows = rows * 2 + 1;
            columns = columns * 2 + 1;

            int[][] newMaze = new int[rows][columns];
            int currentRow = 0;
            int currentCol = 0;

            // translating coding words into the maze

            for (int i = 0; i < wordCount; ++i) {
                int sep = dis.readUnsignedByte();
                if (sep != separator) {
                    System.out.println("A");
                    return LoadResult.INVALID_STRUCT;
                }

                int value = dis.readUnsignedByte();
                if ( value != wall && value != path)
                    return LoadResult.INVALID_STRUCT;
                value = value == wall ? 1 : 0;

                int count = 1 + dis.readUnsignedByte();
                if ( count >= 255)
                    count--;

                for (int j = 0; j < count; j++) {
                    if (currentRow >= rows) {
                        //System.out.println("A");
                        //return LoadResult.BAD_DIMS;
                    }

                    newMaze[currentRow][currentCol] = value;
                    currentCol++;
                    if (currentCol >= columns) {
                        currentCol = 0;
                        currentRow++;
                    }
                }
            }

            this.maze = new Maze(newMaze, columns, rows, new Point(entryX - 1, entryY - 1), new Point(exitX - 1, exitY - 1));

            return LoadResult.SUCCESS;
        } catch (IOException e) {
            return LoadResult.INVALID_STRUCT;
        }
    }

    @Override
    public Maze GetMaze() {
        return this.maze;
    }
}
