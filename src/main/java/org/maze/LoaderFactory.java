package org.maze;

public class LoaderFactory {
    public enum LoadType {
        TEXT,
        BINARY
    }

    public static Loader CreateLoader(LoadType type) {
        switch (type) {
            case TEXT: return new TextLoader();
            case BINARY: return new BinaryLoader();
            default: throw new IllegalArgumentException("Nie znany typ pliku: " + type);
        }
    }
}
