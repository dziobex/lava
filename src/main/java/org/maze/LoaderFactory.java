package org.maze;

public class LoaderFactory {
    public enum LoadType {
        TEXT,
        BINARY,
        IMAGE
    }

    public static Loader CreateLoader(LoadType type) {
        switch (type) {
            default:
            case TEXT: return new TextLoader();
            case BINARY: return new BinaryLoader();
        }
    }
}
