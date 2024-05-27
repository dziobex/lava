package org.maze;

import java.io.File;

public interface Loader {
    public static enum LoadResult {
        SUCCESS,
        BAD_DIMS,
        BAD_CHARS,
        INVALID_STRUCT
    };

    public LoadResult Load(File in);
    public Maze GetMaze();
}
