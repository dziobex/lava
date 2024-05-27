package org.maze;

import java.io.File;

public interface Saver {
    public static enum SaveResult {
        SUCCESS,
        NO_SPACE,
        BAD_FORMAT,
        NONSPECIFIC_FAILURE
    }

    public SaveResult Save(File out);
}
