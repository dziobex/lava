package org.maze;

import java.awt.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BinaryLoader implements Loader {

    public BinaryLoader() {
    }

    @Override
    public LoadResult Load(File in) {

        try (DataInputStream dis = new DataInputStream(new FileInputStream(in))) {

            /* reading header */

            dis.skipBytes(5);   // file id + esc sign

            int columns = ByteBuffer.wrap(dis.readNBytes(2)).order(ByteOrder.LITTLE_ENDIAN).getShort();
            int rows = ByteBuffer.wrap(dis.readNBytes(2)).order(ByteOrder.LITTLE_ENDIAN).getShort();

            if (columns <= 0 || columns > 2049 || rows <= 0 || rows > 2049)
                throw new IOException();

            int entryX = ByteBuffer.wrap(dis.readNBytes(2)).order(ByteOrder.LITTLE_ENDIAN).getShort();
            int entryY = ByteBuffer.wrap(dis.readNBytes(2)).order(ByteOrder.LITTLE_ENDIAN).getShort();

            int exitX = ByteBuffer.wrap(dis.readNBytes(2)).order(ByteOrder.LITTLE_ENDIAN).getShort();
            int exitY = ByteBuffer.wrap(dis.readNBytes(2)).order(ByteOrder.LITTLE_ENDIAN).getShort();

            dis.skipBytes(12);  // reserved bits

            int wordCount = ByteBuffer.wrap(dis.readNBytes(4)).order(ByteOrder.LITTLE_ENDIAN).getInt();
            dis.skipBytes(4);   // solution offset
            int separator = dis.readUnsignedByte();
            int wall = dis.readUnsignedByte();
            int path = dis.readUnsignedByte();

            int[][] newMaze = new int[2050][2050];  // POLSKA
            int curRow = 0, curCol = 0;

            /* reading coding words */

            for (int i = 0; i < wordCount; ++i) {
                int sep = dis.readUnsignedByte();
                int value = dis.readUnsignedByte();
                int count = 1 + dis.readUnsignedByte();

                if ( sep != separator || (value != wall && value != path) )
                    throw new IOException();

                value = value == wall ? 1 : 0;

                for (int j = 0; j < count; ++j) {
                    if (curRow >= rows)     // lying!
                        return LoadResult.BAD_DIMS;

                    newMaze[curRow][curCol] = value;

                    if (++curCol >= columns) {
                        curCol = 0;
                        ++curRow;
                    }
                }
            }

            Maze.getInstance().NewData(newMaze, columns, rows, new Point(entryX - 1, entryY - 1), new Point(exitX - 1, exitY - 1));

            return LoadResult.SUCCESS;
        } catch (IOException e) {
            return LoadResult.INVALID_STRUCT;
        }
    }
}
